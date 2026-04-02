package org.example.fuelconsumption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalizationService {

    private static final String SELECT_SQL = """
            SELECT `key`, value
            FROM localization_strings
            WHERE language = ?
            """;

    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();
    private Map<String, String> currentStrings = Collections.emptyMap();
    private String currentLanguage;

    public Map<String, String> loadStrings(String language) {
        if (cache.containsKey(language)) {
            currentLanguage = language;
            currentStrings = cache.get(language);
            return currentStrings;
        }

        Map<String, String> loadedStrings = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL)) {

            statement.setString(1, language);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    loadedStrings.put(resultSet.getString("key"), resultSet.getString("value"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load localization strings for language: " + language, e);
        }

        if (loadedStrings.isEmpty() && !"en_US".equals(language)) {
            return loadStrings("en_US");
        }

        cache.put(language, loadedStrings);
        currentLanguage = language;
        currentStrings = loadedStrings;
        return currentStrings;
    }

    public String getString(String key) {
        if (currentStrings.containsKey(key)) {
            return currentStrings.get(key);
        }
        if (!"en_US".equals(currentLanguage) && cache.containsKey("en_US")) {
            return cache.get("en_US").getOrDefault(key, key);
        }
        return key;
    }

    public Set<String> getAllKeys() {
        return currentStrings.keySet();
    }
}
