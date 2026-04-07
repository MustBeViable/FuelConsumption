package org.example.fuelconsumption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalculationService {

    private static final String INSERT_SQL = """
            INSERT INTO calculation_records
            (distance, consumption, price, total_fuel, total_cost, language)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    public void saveCalculation(CalculationRecord rec) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setDouble(1, rec.getDistance());
            statement.setDouble(2, rec.getConsumption());
            statement.setDouble(3, rec.getPrice());
            statement.setDouble(4, rec.getTotalFuel());
            statement.setDouble(5, rec.getTotalCost());
            statement.setString(6, rec.getLanguage());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save calculation record", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
