package itstep.learning.services.random;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltGenerator implements RandomStringGenerator  {
    private static final int LENGTH = 16;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        byte[] salt = new byte[LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
