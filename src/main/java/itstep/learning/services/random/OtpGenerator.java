package itstep.learning.services.random;

import java.security.SecureRandom;

public class OtpGenerator implements RandomStringGenerator {
    private static final String DIGITS = "0123456789";
    private static final int LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }
}