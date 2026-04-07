package org.example.fuelconsumption;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocalizationServiceTest {

    @Test
    void loadStringsShouldLoadAndCacheLanguage() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("key")).thenReturn("distance.label", "calculate.button");
        when(resultSet.getString("value")).thenReturn("Distance", "Calculate");

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(connection);

            LocalizationService service = new LocalizationService();

            Map<String, String> map1 = service.loadStrings("en_US");
            Map<String, String> map2 = service.loadStrings("en_US");

            assertEquals("Distance", map1.get("distance.label"));
            assertEquals("Calculate", map1.get("calculate.button"));
            assertSame(map1, map2);

            verify(connection, times(1)).prepareStatement(anyString());
        }
    }

    @Test
    void loadStringsShouldFallbackToEnglishWhenLanguageHasNoRows() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet emptyResultSet = mock(ResultSet.class);
        ResultSet englishResultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(emptyResultSet, englishResultSet);

        when(emptyResultSet.next()).thenReturn(false);

        when(englishResultSet.next()).thenReturn(true, false);
        when(englishResultSet.getString("key")).thenReturn("app.title");
        when(englishResultSet.getString("value")).thenReturn("Fuel Calculator");

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(connection);

            LocalizationService service = new LocalizationService();
            Map<String, String> result = service.loadStrings("fr_FR");

            assertEquals("Fuel Calculator", result.get("app.title"));
        }
    }

    @Test
    void loadStringsShouldThrowRuntimeExceptionOnSqlError() throws Exception {
        Connection connection = mock(Connection.class);

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("SQL fail"));

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(connection);

            LocalizationService service = new LocalizationService();

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> service.loadStrings("ja_JP"));

            assertTrue(ex.getMessage().contains("Failed to load localization strings"));
            assertNotNull(ex.getCause());
            assertTrue(ex.getCause() instanceof SQLException);
        }
    }

    @Test
    void getStringShouldReturnCurrentLanguageValue() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("key")).thenReturn("price.label");
        when(resultSet.getString("value")).thenReturn("Price");

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(connection);

            LocalizationService service = new LocalizationService();
            service.loadStrings("en_US");

            assertEquals("Price", service.getString("price.label"));
        }
    }

    @Test
    void getStringShouldFallbackToEnglishCache() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet englishResultSet = mock(ResultSet.class);
        ResultSet frenchResultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(englishResultSet, frenchResultSet);

        when(englishResultSet.next()).thenReturn(true, false);
        when(englishResultSet.getString("key")).thenReturn("save.success");
        when(englishResultSet.getString("value")).thenReturn("Saved");

        when(frenchResultSet.next()).thenReturn(false);

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(connection);

            LocalizationService service = new LocalizationService();
            service.loadStrings("en_US");
            service.loadStrings("fr_FR");

            assertEquals("Saved", service.getString("save.success"));
        }
    }

    @Test
    void getStringShouldReturnKeyWhenNothingFound() {
        LocalizationService service = new LocalizationService();
        assertEquals("missing.key", service.getString("missing.key"));
    }

    @Test
    void getAllKeysShouldReturnCurrentKeys() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("key")).thenReturn("distance.label", "price.label");
        when(resultSet.getString("value")).thenReturn("Distance", "Price");

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(connection);

            LocalizationService service = new LocalizationService();
            service.loadStrings("en_US");

            Set<String> keys = service.getAllKeys();

            assertEquals(2, keys.size());
            assertTrue(keys.contains("distance.label"));
            assertTrue(keys.contains("price.label"));
        }
    }
}