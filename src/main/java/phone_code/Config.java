package phone_code;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.models.AuthenticatorConfigModel;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> configProperties = new ArrayList<>();

        ProviderConfigProperty urlProperty = new ProviderConfigProperty();
        urlProperty.setName("codeUrlTemplate");
        urlProperty.setLabel("OTP URL Template");
        urlProperty.setType(ProviderConfigProperty.STRING_TYPE);
        urlProperty.setHelpText("Example: https://some-host.tld/accounts/sendCode?phone={phone}&code={code}&realm={realm}");
        configProperties.add(urlProperty);

        ProviderConfigProperty fakeSendingCodeProperty = new ProviderConfigProperty();
        fakeSendingCodeProperty.setName("fakeSendingCode");
        fakeSendingCodeProperty.setLabel("Fake Sending OTP");
        fakeSendingCodeProperty.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        fakeSendingCodeProperty.setHelpText("Enable this to bypass sending the OTP for testing purposes.");
        configProperties.add(fakeSendingCodeProperty);

        ProviderConfigProperty logoUrlProperty = new ProviderConfigProperty();
        logoUrlProperty.setName("logoUrl");
        logoUrlProperty.setLabel("Logo URL");
        logoUrlProperty.setType(ProviderConfigProperty.STRING_TYPE);
        logoUrlProperty.setHelpText("URL of the logo to display in pages.");
        configProperties.add(logoUrlProperty);

        ProviderConfigProperty codeLengthProperty = new ProviderConfigProperty();
        codeLengthProperty.setName("codeLength");
        codeLengthProperty.setLabel("OTP Length");
        codeLengthProperty.setType(ProviderConfigProperty.STRING_TYPE);
        codeLengthProperty.setHelpText("Number of digits in the OTP code (e.g., 4, 6).");
        configProperties.add(codeLengthProperty);

        ProviderConfigProperty secondsToEnableResendingProperty = new ProviderConfigProperty();
        secondsToEnableResendingProperty.setName("secondsToEnableResending");
        secondsToEnableResendingProperty.setLabel("Seconds to Enable Resending");
        secondsToEnableResendingProperty.setType(ProviderConfigProperty.STRING_TYPE);
        secondsToEnableResendingProperty.setHelpText("Number of seconds before the 'resend OTP' option is enabled again.");
        configProperties.add(secondsToEnableResendingProperty);

        ProviderConfigProperty enablePhoneCallProperty = new ProviderConfigProperty();
        enablePhoneCallProperty.setName("enablePhoneCall");
        enablePhoneCallProperty.setLabel("Enable Phone Call");
        enablePhoneCallProperty.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        enablePhoneCallProperty.setHelpText("Enable this to deliver OTP via a phone call instead of SMS.");
        configProperties.add(enablePhoneCallProperty);

        ProviderConfigProperty separateCodeInputsProperty = new ProviderConfigProperty();
        separateCodeInputsProperty.setName("separateCodeInputs");
        separateCodeInputsProperty.setLabel("Separate OTP Inputs");
        separateCodeInputsProperty.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        separateCodeInputsProperty.setHelpText("If enabled, the user enters OTP digits in separate input fields instead of one.");
        configProperties.add(separateCodeInputsProperty);

        ProviderConfigProperty autoSubmitCodeProperty = new ProviderConfigProperty();
        autoSubmitCodeProperty.setName("autoSubmitCode");
        autoSubmitCodeProperty.setLabel("Auto Submit OTP");
        autoSubmitCodeProperty.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        autoSubmitCodeProperty.setHelpText("If enabled, the form submits automatically when all OTP digits are entered.");
        configProperties.add(autoSubmitCodeProperty);

        return configProperties;
    }

    public static <T> T getConfig(AuthenticationFlowContext context, String key, Class<T> type) {
        return getConfig(context, key, type, null);
    }

    public static <T> T getConfig(AuthenticationFlowContext context, String key, Class<T> type, T defaultValue) {
        AuthenticatorConfigModel cfg = context.getAuthenticatorConfig();
        if (cfg == null || cfg.getConfig() == null) return defaultValue;

        Object value = cfg.getConfig().get(key);
        if (value == null) return defaultValue;

        String strValue;
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (list.isEmpty()) return defaultValue;
            strValue = String.valueOf(list.get(0));
        } else {
            strValue = String.valueOf(value);
        }

        if (strValue.isEmpty()) return defaultValue;

        if (type == String.class) return type.cast(strValue);
        if (type == Boolean.class) return type.cast(Boolean.parseBoolean(strValue));
        if (type == Integer.class) {
            try { return type.cast(Integer.parseInt(strValue)); }
            catch (NumberFormatException e) { return defaultValue; }
        }

        return type.cast(strValue);
    }
}
