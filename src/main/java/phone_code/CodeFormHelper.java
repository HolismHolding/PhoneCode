package phone_code;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.forms.login.LoginFormsProvider;

public class CodeFormHelper {

    public static void apply(AuthenticationFlowContext context, LoginFormsProvider form, String phone, Integer codeLength) {
        Integer secondsToEnableResending = Config.getConfig(context, "secondsToEnableResending", Integer.class);
        Boolean enablePhoneCall = Config.getConfig(context, "enablePhoneCall", Boolean.class);
        Boolean separateCodeInputs = Config.getConfig(context, "separateCodeInputs", Boolean.class);
        Boolean autoSubmitCode = Config.getConfig(context, "autoSubmitCode", Boolean.class);
        String logoUrl = Config.getConfig(context, "logoUrl", String.class);

        form.setAttribute("phone", phone);
        if (codeLength != null) {
            form.setAttribute("codeLength", codeLength);
        }
        form.setAttribute("defaultCodeLength", CodeGenerator.DEFAULT_CODE_LENGTH);
        form.setAttribute("secondsToEnableResending", secondsToEnableResending);
        form.setAttribute("enablePhoneCall", enablePhoneCall);
        form.setAttribute("separateCodeInputs", separateCodeInputs);
        form.setAttribute("autoSubmitCode", autoSubmitCode);
        if (logoUrl != null) {
            form.setAttribute("logoUrl", logoUrl);
        }
    }
}
