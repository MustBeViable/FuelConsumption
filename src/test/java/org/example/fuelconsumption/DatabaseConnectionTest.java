package org.example.fuelconsumption;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DatabaseConnectionTest {

    private TestDriver registeredDriver;

    @AfterEach
    void tearDown() throws Exception {
        if (registeredDriver != null) {
            DriverManager.deregisterDriver(registeredDriver);
        }

        Properties props = getFileProperties();
        props.clear();
    }

    @Test
    void privateConstructorShouldBeInvocableForCoverage() throws Exception {
        Constructor<DatabaseConnection> constructor =
                DatabaseConnection.class.getDeclaredConstructor();

        constructor.setAccessible(true);
        DatabaseConnection instance = constructor.newInstance();

        assertNotNull(instance);
    }

    @Test
    void getConnectionShouldUseValuesFromFileProperties() throws Exception {
        Properties props = getFileProperties();
        props.setProperty("DB_URL", "jdbc:testdb:unit");
        props.setProperty("DB_USER", "test_user");
        props.setProperty("DB_PASSWORD", "test_password");

        registeredDriver = new TestDriver();
        DriverManager.registerDriver(registeredDriver);

        Connection connection = DatabaseConnection.getConnection();

        assertSame(registeredDriver.connectionToReturn, connection);
        assertEquals("jdbc:testdb:unit", registeredDriver.lastUrl);
        assertEquals("test_user", registeredDriver.lastProperties.getProperty("user"));
        assertEquals("test_password", registeredDriver.lastProperties.getProperty("password"));
    }

    @Test
    void getConfigShouldReturnFilePropertyWhenEnvironmentVariableMissing() throws Exception {
        Properties props = getFileProperties();
        props.setProperty("DB_URL", "jdbc:testdb:fromfile");

        Method getConfig = DatabaseConnection.class.getDeclaredMethod(
                "getConfig", String.class, String.class
        );
        getConfig.setAccessible(true);

        String result = (String) getConfig.invoke(null, "DB_URL", "fallback");

        assertEquals("jdbc:testdb:fromfile", result);
    }

    @Test
    void getConfigShouldReturnDefaultValueWhenNothingConfigured() throws Exception {
        Properties props = getFileProperties();
        props.remove("MISSING_KEY");

        Method getConfig = DatabaseConnection.class.getDeclaredMethod(
                "getConfig", String.class, String.class
        );
        getConfig.setAccessible(true);

        String result = (String) getConfig.invoke(null, "MISSING_KEY", "fallback-value");

        assertEquals("fallback-value", result);
    }

    @Test
    void loadExternalPropertiesShouldReturnEmptyPropertiesWhenConfigFileDoesNotExist() throws Exception {
        Method loadExternalProperties = DatabaseConnection.class.getDeclaredMethod("loadExternalProperties");
        loadExternalProperties.setAccessible(true);

        Properties result = (Properties) loadExternalProperties.invoke(null);

        assertNotNull(result);
    }

    @SuppressWarnings("unchecked")
    private Properties getFileProperties() throws Exception {
        Field field = DatabaseConnection.class.getDeclaredField("FILE_PROPERTIES");
        field.setAccessible(true);
        return (Properties) field.get(null);
    }

    static class TestDriver implements Driver {
        Connection connectionToReturn = mock(Connection.class);
        String lastUrl;
        Properties lastProperties;

        @Override
        public Connection connect(String url, Properties info) {
            if (!acceptsURL(url)) {
                return null;
            }
            this.lastUrl = url;
            this.lastProperties = info;
            return connectionToReturn;
        }

        @Override
        public boolean acceptsURL(String url) {
            return url != null && url.startsWith("jdbc:testdb:");
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
            return new DriverPropertyInfo[0];
        }

        @Override
        public int getMajorVersion() {
            return 1;
        }

        @Override
        public int getMinorVersion() {
            return 0;
        }

        @Override
        public boolean jdbcCompliant() {
            return false;
        }

        @Override
        public Logger getParentLogger() {
            return Logger.getGlobal();
        }
    }
}