package itstep.learning.services.random;

import java.security.SecureRandom;

public class PasswordGenerator implements RandomStringGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_+=<>?";
    private static final int LENGTH = 12;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
