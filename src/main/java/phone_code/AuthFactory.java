package phone_code;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

import static org.keycloak.models.AuthenticationExecutionModel.Requirement;

public class AuthFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "phone-code-authenticator";
    static Auth SINGLETON = new Auth();

    private static final Requirement[] REQUIREMENT_CHOICES = {
        Requirement.REQUIRED,
        Requirement.ALTERNATIVE,
        Requirement.DISABLED
    };

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    @Override
    public void init(Config.Scope config) { }

    @Override
    public void postInit(KeycloakSessionFactory factory) { }

    @Override
    public void close() { }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getReferenceCategory() {
        return "phone-code";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getDisplayType() {
        return "Phone code";
    }

    @Override
    public String getHelpText() {
        return "Minimal custom authenticator for phone code flow.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return phone_code.Config.getConfigProperties();
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }
}
