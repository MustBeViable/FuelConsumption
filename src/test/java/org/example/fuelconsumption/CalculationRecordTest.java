package org.example.fuelconsumption;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class CalculationRecordTest {

    @Test
    void constructorAndGettersShouldReturnCorrectValues() {
        CalculationRecord record = new CalculationRecord(
                150.0,
                6.5,
                1.89,
                9.75,
                18.43,
                "en_US"
        );

        assertEquals(150.0, record.getDistance());
        assertEquals(6.5, record.getConsumption());
        assertEquals(1.89, record.getPrice());
        assertEquals(9.75, record.getTotalFuel());
        assertEquals(18.43, record.getTotalCost());
        assertEquals("en_US", record.getLanguage());
    }
}