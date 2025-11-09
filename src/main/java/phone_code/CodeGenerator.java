package phone_code;

import java.security.SecureRandom;

public class CodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_OTP_LENGTH = 6;

    public String generate(Integer codeLength) {
        int length = (codeLength != null && codeLength > 0) ? codeLength : DEFAULT_OTP_LENGTH;
        int bound = (int) Math.pow(10, length);
        int code = RANDOM.nextInt(bound);
        return String.format("%0" + length + "d", code);
    }
}
