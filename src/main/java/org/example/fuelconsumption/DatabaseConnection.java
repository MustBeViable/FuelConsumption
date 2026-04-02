package org.example.fuelconsumption;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties FILE_PROPERTIES = loadExternalProperties();

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getConfig("DB_URL", "jdbc:mariadb://localhost:3306/fuel_calculator_localization");
        String user = getConfig("DB_USER", "fuel_user");
        String password = getConfig("DB_PASSWORD", "fuel_password");
        return DriverManager.getConnection(url, user, password);
    }

    private static String getConfig(String key, String defaultValue) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        String fileValue = FILE_PROPERTIES.getProperty(key);
        if (fileValue != null && !fileValue.isBlank()) {
            return fileValue;
        }

        return defaultValue;
    }

    private static Properties loadExternalProperties() {
        Properties properties = new Properties();
        Path configPath = Path.of("config", "db.properties");

        if (Files.exists(configPath)) {
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                properties.load(inputStream);
            } catch (IOException ignored) {
                // Fall back to environment variables and defaults.
            }
        }

        return properties;
    }
}
