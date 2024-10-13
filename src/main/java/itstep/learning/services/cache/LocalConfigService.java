package itstep.learning.services.cache;

import com.google.inject.Inject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocalConfigService implements ConfigService {
    private final Map<String, String> configMap = new HashMap<>();

    @Inject
    public LocalConfigService() {
        loadConfig();
    }

    private void loadConfig() {
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("cache.ini")) {
            if (input == null) {
                throw new RuntimeException("Cannot find configuration file 'cache.ini'");
            }

            Properties prop = new Properties();
            prop.load(input);

            // Загружаем данные в карту
            configMap.put("categories_max_age", prop.getProperty("categories_max_age"));
            configMap.put("products_max_age", prop.getProperty("products_max_age"));
            configMap.put("tokens_max_age", prop.getProperty("tokens_max_age"));
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration: " + e.getMessage());
        }
    }

    @Override
    public int getMaxAgeForCategory() {
        return Integer.parseInt(configMap.get("categories_max_age"));
    }

    @Override
    public int getMaxAgeForProduct() {
        return Integer.parseInt(configMap.get("products_max_age"));
    }

    @Override
    public int getMaxAgeForToken() {
        return Integer.parseInt(configMap.get("tokens_max_age"));
    }
}
