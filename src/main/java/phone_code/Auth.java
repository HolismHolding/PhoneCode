package phone_code;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.jboss.logging.Logger;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

public class Auth implements Authenticator {

    private static final Logger LOG = Logger.getLogger(Auth.class);
    private final CodeSender codeSender = new CodeSender();
    private final CodeVerifier codeVerifier = new CodeVerifier();
    private final CodeGenerator codeGenerator = new CodeGenerator();

    private void showPhoneForm(AuthenticationFlowContext context, String phone, String errorKey) {
        LoginFormsProvider form = context.form();
        form.setAttribute("phone", phone);
        form.setAttribute("phoneErrorKey", errorKey);
        form.setAttribute("defaultCodeLength", CodeGenerator.DEFAULT_CODE_LENGTH);
        String logoUrl = Config.getConfig(context, "logoUrl", String.class);
        if (logoUrl != null) {
            form.setAttribute("logoUrl", logoUrl);
        }
        context.challenge(form.createForm("phone.ftl"));
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String phone = (String) context.getSession().getAttribute("phone");
        String errorKey = (String) context.getSession().getAttribute("phoneErrorKey");
        context.getSession().removeAttribute("phoneErrorKey");
        showPhoneForm(context, phone, errorKey);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formParams = context.getHttpRequest().getDecodedFormParameters();
        LoginFormsProvider form = context.form();
        form.setAttribute("defaultCodeLength", CodeGenerator.DEFAULT_CODE_LENGTH);

        if (formParams.containsKey("backToPhone")) {
            String phone = context.getAuthenticationSession().getClientNote("phone");
            showPhoneForm(context, phone, null);
            return;
        }

        if (formParams.containsKey("code")) {
            codeVerifier.verify(context, formParams.getFirst("code"));
            return;
        }

        String phone = formParams.getFirst("phone");
        if (phone == null || phone.isEmpty()) {
            context.getSession().setAttribute("phoneErrorKey", "emptyPhoneText");
            authenticate(context);
            return;
        }

        KeycloakSession session = context.getSession();
        RealmModel realm = context.getRealm();
        UserProvider users = session.users();
        UserModel user = users.getUserByUsername(realm, phone);

        if (user == null) {
            user = users.addUser(realm, phone);
            user.setEnabled(true);
        }

        Integer codeLength = Config.getConfig(context, "codeLength", Integer.class);
        String code = codeGenerator.generate(codeLength);
        context.getAuthenticationSession().setAuthNote("code", code);
        context.getAuthenticationSession().setAuthNote("phone", phone);

        if (codeSender.send(context, phone, code)) {
            LoginFormsProvider codeFormProvider = context.form();
            CodeFormHelper.apply(context, codeFormProvider, phone, code.length());
            context.challenge(codeFormProvider.createForm("code.ftl"));
        } else {
            String error = (String) session.getAttribute("codeErrorKey");
            context.getSession().setAttribute("phoneErrorKey", error != null ? error : "codeSendFailed");
            authenticate(context);
        }
    }

    @Override public boolean requiresUser() { return false; }
    @Override public boolean configuredFor(KeycloakSession s, RealmModel r, UserModel u) { return true; }
    @Override public void setRequiredActions(KeycloakSession s, RealmModel r, UserModel u) {}
    @Override public void close() {}
}
