package phone_code;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.jboss.logging.Logger;

import jakarta.ws.rs.core.Response;

public class CodeVerifier {

    private static final Logger LOG = Logger.getLogger(CodeVerifier.class);

    public void verify(AuthenticationFlowContext context, String enteredCode) {
        String expectedCode = context.getAuthenticationSession().getAuthNote("code");
        String phone = context.getAuthenticationSession().getAuthNote("phone");

        LOG.infof("Phone: %s, Expected OTP: %s, Entered OTP: %s", phone, expectedCode, enteredCode);

        if (expectedCode != null && expectedCode.equals(enteredCode)) {
            RealmModel realm = context.getRealm();
            UserModel user = context.getSession().users().getUserByUsername(realm, phone);
            context.setUser(user);
            context.success();
        } else {
            LoginFormsProvider form = context.form();
            form.setAttribute("phone", phone);
            form.setAttribute("codeErrorKey", "invalidCodeText");
            Response codeForm = form.createForm("code.ftl");
            context.challenge(codeForm);
        }
    }
}
