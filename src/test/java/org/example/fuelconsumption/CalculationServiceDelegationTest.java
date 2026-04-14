package org.example.fuelconsumption;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class CalculationServiceDelegationTest {

    @Test
    void getConnectionShouldDelegateToDatabaseConnection() throws Exception {
        Connection expectedConnection = mock(Connection.class);
        CalculationService service = new CalculationService();

        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(expectedConnection);

            Connection actualConnection = service.getConnection();

            assertSame(expectedConnection, actualConnection);
        }
    }
}