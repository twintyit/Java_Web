package itstep.learning.services.cache;

public interface ConfigService {
    int getMaxAgeForCategory();

    int getMaxAgeForProduct();

    int getMaxAgeForToken();
}
