package phone_code;

import java.security.SecureRandom;

public class CodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    public static final int DEFAULT_CODE_LENGTH = 4;
    private static final int MAX_LENGTH = 9;

    public String generate(Integer codeLength) {
        int len = (codeLength != null && codeLength > 0) ? codeLength : DEFAULT_CODE_LENGTH;
        if (len > MAX_LENGTH) len = MAX_LENGTH;
        int bound = pow10(len);
        int code = RANDOM.nextInt(bound);
        return String.format("%0" + len + "d", code);
    }

    private static int pow10(int n) {
        int v = 1;
        for (int i = 0; i < n; i++) v *= 10;
        return v;
    }
}
