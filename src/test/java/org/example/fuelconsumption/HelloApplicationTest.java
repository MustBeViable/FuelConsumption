package org.example.fuelconsumption;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HelloApplicationTest {

    @Test
    void getLocalizationServiceShouldReturnServiceInstance() {
        HelloApplication app = new HelloApplication();

        assertNotNull(app.getLocalizationService());
    }

    @Test
    void getCalculationServiceShouldReturnServiceInstance() {
        HelloApplication app = new HelloApplication();

        assertNotNull(app.getCalculationService());
    }

    @Test
    void setTitleShouldDoNothingWhenStageIsNull() {
        HelloApplication app = new HelloApplication();

        assertDoesNotThrow(() -> app.setTitle("Test title"));
    }

    @Test
    void setTitleShouldDelegateToStageWhenStageExists() throws Exception {
        HelloApplication app = new HelloApplication();
        Stage mockStage = mock(Stage.class);

        Field stageField = HelloApplication.class.getDeclaredField("stage");
        stageField.setAccessible(true);
        stageField.set(app, mockStage);

        app.setTitle("Fuel Calculator");

        verify(mockStage).setTitle("Fuel Calculator");
    }
}