package org.example.fuelconsumption;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DatabaseConnectionTest {


    @Test
    void privateConstructorShouldBeInvocableForCoverage() throws Exception {
        Constructor<DatabaseConnection> constructor =
                DatabaseConnection.class.getDeclaredConstructor();

        constructor.setAccessible(true);
        DatabaseConnection instance = constructor.newInstance();

        assertNotNull(instance);
    }

    static class TestDriver implements Driver {
        Connection connectionToReturn = mock(Connection.class);
        String lastUrl;
        Properties lastProperties;

        @Override
        public Connection connect(String url, Properties info) {
            this.lastUrl = url;
            this.lastProperties = info;
            return connectionToReturn;
        }

        @Override
        public boolean acceptsURL(String url) {
            return url != null && url.startsWith("jdbc:mariadb:");
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