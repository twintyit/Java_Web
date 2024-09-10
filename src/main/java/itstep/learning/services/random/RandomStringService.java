package itstep.learning.services.random;


import com.google.inject.Inject;

public class RandomStringService {

    private final RandomStringGenerator fileNameGenerator;
    private final RandomStringGenerator saltGenerator;
    private final RandomStringGenerator otpGenerator;
    private final RandomStringGenerator passwordGenerator;

    @Inject
    public RandomStringService(FileNameGenerator fileNameGenerator,
                               SaltGenerator saltGenerator,
                               OtpGenerator otpGenerator,
                               PasswordGenerator passwordGenerator) {
        this.fileNameGenerator = fileNameGenerator;
        this.saltGenerator = saltGenerator;
        this.otpGenerator = otpGenerator;
        this.passwordGenerator = passwordGenerator;
    }

    public String generateFileName() {
        return fileNameGenerator.generate();
    }

    public String generateSalt() {
        return saltGenerator.generate();
    }

    public String generateOTP() {
        return otpGenerator.generate();
    }

    public String generatePassword() {
        return passwordGenerator.generate();
    }
}
