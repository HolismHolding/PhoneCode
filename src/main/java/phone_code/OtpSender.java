package phone_code;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.*;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CodeSender {

    private static final Logger LOG = Logger.getLogger(CodeSender.class);

    public boolean send(AuthenticationFlowContext context, String phone, String code) {
        try {
            RealmModel realm = context.getRealm();
            Boolean fakeSending = Config.getConfig(context, "fakeSendingCode", Boolean.class, false);
            if (fakeSending) {
                LOG.info("Fake sending OTP enabled — skipping actual send for phone: " + phone + " and OTP: " + code);
                return true;
            }

            String urlTemplate = Config.getConfig(context, "codeUrlTemplate", String.class);

            if (urlTemplate == null || urlTemplate.isEmpty()) {
                LOG.error("OTP URL template not configured");
                context.getSession().setAttribute("codeErrorKey", "codeConfigMissing");
                return false;
            }

            String urlString = urlTemplate
                    .replace("{phone}", phone)
                    .replace("{code}", code)
                    .replace("{realm}", realm.getName());

            LOG.info("Sending OTP request to URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(new byte[0]);
            }

            int status = conn.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream()
            ));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();
            conn.disconnect();

            LOG.info("OTP server response (" + status + "): " + response);

            String message = null;
            String resp = response.toString().trim();
            if (resp.startsWith("{") && resp.endsWith("}")) {
                int idx = resp.indexOf("\"message\"");
                if (idx >= 0) {
                    int colon = resp.indexOf(":", idx);
                    if (colon >= 0) {
                        int startQuote = resp.indexOf("\"", colon);
                        int endQuote = resp.indexOf("\"", startQuote + 1);
                        if (startQuote >= 0 && endQuote >= 0) {
                            message = resp.substring(startQuote + 1, endQuote);
                        }
                    }
                }
            }

            if (status == 200) {
                return true;
            } else {
                context.getSession().setAttribute("codeErrorKey", message != null ? message : "codeSendFailed");
                return false;
            }

        } catch (Exception e) {
            LOG.error("Failed to send OTP: " + e.getMessage(), e);
            context.getSession().setAttribute("codeErrorKey", "codeSendFailed");
            return false;
        }
    }
}
