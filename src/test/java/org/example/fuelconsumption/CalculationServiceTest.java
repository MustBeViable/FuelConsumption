package org.example.fuelconsumption;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculationServiceTest {

    @Test
    void saveCalculationShouldSetAllParametersAndExecuteUpdate() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        CalculationService service = new CalculationService() {
            @Override
            public Connection getConnection() {
                return connection;
            }
        };

        CalculationRecord record = new CalculationRecord(
                200.0, 7.2, 1.95, 14.4, 28.08, "fi_FI"
        );

        service.saveCalculation(record);

        verify(connection).prepareStatement(contains("INSERT INTO calculation_records"));
        verify(statement).setDouble(1, 200.0);
        verify(statement).setDouble(2, 7.2);
        verify(statement).setDouble(3, 1.95);
        verify(statement).setDouble(4, 14.4);
        verify(statement).setDouble(5, 28.08);
        verify(statement).setString(6, "fi_FI");
        verify(statement).executeUpdate();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void saveCalculationShouldThrowRuntimeExceptionWhenSqlFails() throws Exception {
        Connection connection = mock(Connection.class);

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        CalculationService service = new CalculationService() {
            @Override
            public Connection getConnection() {
                return connection;
            }
        };

        CalculationRecord record = new CalculationRecord(
                100.0, 5.0, 2.0, 5.0, 10.0, "en_US"
        );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.saveCalculation(record));

        assertEquals("Failed to save calculation record", ex.getMessage());
        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof SQLException);
    }
}