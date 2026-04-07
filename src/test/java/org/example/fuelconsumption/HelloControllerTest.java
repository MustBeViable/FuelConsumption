package org.example.fuelconsumption;

import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HelloControllerTest {

    private HelloController controller;
    private HelloApplication app;
    private LocalizationService localizationService;
    private CalculationService calculationService;

    @BeforeAll
    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX already started
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new HelloController();

        app = mock(HelloApplication.class);
        localizationService = mock(LocalizationService.class);
        calculationService = mock(CalculationService.class);

        when(app.getLocalizationService()).thenReturn(localizationService);
        when(app.getCalculationService()).thenReturn(calculationService);

        when(localizationService.getString(anyString())).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            return switch (key) {
                case "app.title" -> "Fuel Calculator";
                case "distance.label" -> "Distance";
                case "consumption.label" -> "Consumption";
                case "price.label" -> "Price";
                case "calculate.button" -> "Calculate";
                case "invalid.input" -> "Invalid input";
                case "result.label" -> "Fuel: {0}, Cost: {1}";
                case "save.success" -> "Saved";
                case "save.error" -> "Save failed";
                default -> key;
            };
        });

        setField("vBox", new VBox());
        setField("languageBox", new HBox());
        setField("lblDistance", new Label());
        setField("lblConsumption", new Label());
        setField("lblPrice", new Label());
        setField("lblResult", new Label());
        setField("txtDistance", new TextField());
        setField("txtConsumption", new TextField());
        setField("txtPrice", new TextField());
        setField("btnCalculate", new Button());

        controller.setApp(app);
    }

    @Test
    void setAppShouldAssignServicesFromApplication() throws Exception {
        assertSame(localizationService, getField("localizationService"));
        assertSame(calculationService, getField("calculationService"));
    }

    @Test
    void initializeAppShouldSetEnglishAndLeftOrientation() throws Exception {
        controller.initializeApp();

        assertEquals("en_US", getField("currentLanguage"));
        assertEquals('L', getField("orientation"));
        assertEquals("Distance", ((Label) getField("lblDistance")).getText());
        assertEquals("Consumption", ((Label) getField("lblConsumption")).getText());
        assertEquals("Price", ((Label) getField("lblPrice")).getText());
        assertEquals("Calculate", ((Button) getField("btnCalculate")).getText());
    }

    @Test
    void updateWindowTitleShouldCallAppSetTitle() {
        controller.updateWindowTitle();
        verify(app).setTitle("Fuel Calculator Elias Rinne");
    }

    @Test
    void handleCalculateShouldComputeAndSaveSuccessfully() throws Exception {
        ((TextField) getField("txtDistance")).setText("200");
        ((TextField) getField("txtConsumption")).setText("7.5");
        ((TextField) getField("txtPrice")).setText("1.80");

        invokePrivate("handleCalculate");

        Label lblResult = (Label) getField("lblResult");
        assertTrue(lblResult.getText().contains("Fuel: 15.00, Cost: 27.00"));
        assertTrue(lblResult.getText().contains("Saved"));

        verify(calculationService).saveCalculation(any(CalculationRecord.class));
    }

    @Test
    void handleCalculateShouldShowSaveErrorWhenSaveFails() throws Exception {
        doThrow(new RuntimeException("save failed"))
                .when(calculationService).saveCalculation(any(CalculationRecord.class));

        ((TextField) getField("txtDistance")).setText("100");
        ((TextField) getField("txtConsumption")).setText("6");
        ((TextField) getField("txtPrice")).setText("2");

        invokePrivate("handleCalculate");

        Label lblResult = (Label) getField("lblResult");
        assertTrue(lblResult.getText().contains("Fuel: 6.00, Cost: 12.00"));
        assertTrue(lblResult.getText().contains("Save failed"));
    }

    @Test
    void handleCalculateShouldRejectInvalidNumberFormat() throws Exception {
        ((TextField) getField("txtDistance")).setText("abc");
        ((TextField) getField("txtConsumption")).setText("6");
        ((TextField) getField("txtPrice")).setText("2");

        invokePrivate("handleCalculate");

        assertEquals("Invalid input", ((Label) getField("lblResult")).getText());
        verify(calculationService, never()).saveCalculation(any());
    }

    @Test
    void handleCalculateShouldRejectNegativeValues() throws Exception {
        ((TextField) getField("txtDistance")).setText("-1");
        ((TextField) getField("txtConsumption")).setText("6");
        ((TextField) getField("txtPrice")).setText("2");

        invokePrivate("handleCalculate");

        assertEquals("Invalid input", ((Label) getField("lblResult")).getText());
        verify(calculationService, never()).saveCalculation(any());
    }

    @Test
    void handleEnglishShouldSetEnglishAndLeftOrientation() throws Exception {
        invokePrivate("handleEnglish");

        assertEquals("en_US", getField("currentLanguage"));
        assertEquals('L', getField("orientation"));
    }

    @Test
    void handleFrenchShouldSetFrenchAndLeftOrientation() throws Exception {
        invokePrivate("handleFrench");

        assertEquals("fr_FR", getField("currentLanguage"));
        assertEquals('L', getField("orientation"));
    }

    @Test
    void handleJapaneseShouldSetJapaneseAndLeftOrientation() throws Exception {
        invokePrivate("handleJapanese");

        assertEquals("ja_JP", getField("currentLanguage"));
        assertEquals('L', getField("orientation"));
    }

    @Test
    void handlePersianShouldSetPersianAndRightOrientation() throws Exception {
        invokePrivate("handlePersian");

        assertEquals("fa_IR", getField("currentLanguage"));
        assertEquals('R', getField("orientation"));
    }

    @Test
    void handleOrientationShouldSetLeftToRightLayoutWhenOrientationIsL() throws Exception {
        setField("orientation", 'L');

        invokePrivate("handleOrientation");

        VBox vBox = (VBox) getField("vBox");
        Label lblDistance = (Label) getField("lblDistance");
        TextField txtDistance = (TextField) getField("txtDistance");

        assertEquals(Pos.TOP_LEFT, vBox.getAlignment());
        assertEquals(NodeOrientation.LEFT_TO_RIGHT, vBox.getNodeOrientation());
        assertEquals(NodeOrientation.LEFT_TO_RIGHT, lblDistance.getNodeOrientation());
        assertEquals(NodeOrientation.LEFT_TO_RIGHT, txtDistance.getNodeOrientation());
    }

    @Test
    void handleOrientationShouldSetRightToLeftFieldsWhenOrientationIsR() throws Exception {
        setField("orientation", 'R');

        invokePrivate("handleOrientation");

        VBox vBox = (VBox) getField("vBox");
        Label lblDistance = (Label) getField("lblDistance");
        TextField txtDistance = (TextField) getField("txtDistance");

        assertEquals(Pos.TOP_RIGHT, vBox.getAlignment());
        assertEquals(NodeOrientation.LEFT_TO_RIGHT, vBox.getNodeOrientation());
        assertEquals(NodeOrientation.RIGHT_TO_LEFT, lblDistance.getNodeOrientation());
        assertEquals(NodeOrientation.RIGHT_TO_LEFT, txtDistance.getNodeOrientation());
    }

    @Test
    void setLanguageShouldPopulateTextsAndClearResult() throws Exception {
        ((Label) getField("lblResult")).setText("old");

        invokePrivate("setLanguage", String.class, "en_US");

        assertEquals("en_US", getField("currentLanguage"));
        assertEquals("Distance", ((Label) getField("lblDistance")).getText());
        assertEquals("Consumption", ((Label) getField("lblConsumption")).getText());
        assertEquals("Price", ((Label) getField("lblPrice")).getText());
        assertEquals("Calculate", ((Button) getField("btnCalculate")).getText());
        assertEquals("", ((Label) getField("lblResult")).getText());
    }

    @Test
    void setLanguageShouldShowDatabaseErrorWhenLocalizationFails() throws Exception {
        doThrow(new RuntimeException("db fail")).when(localizationService).loadStrings("en_US");

        invokePrivate("setLanguage", String.class, "en_US");

        assertEquals("Database localization error.", ((Label) getField("lblResult")).getText());
    }

    @Test
    void getTextShouldReturnWrappedKeyWhenValueIsNull() throws Exception {
        when(localizationService.getString("missing")).thenReturn(null);

        String value = (String) invokePrivate("getText", String.class, "missing");

        assertEquals("!missing!", value);
    }

    private void setField(String name, Object value) throws Exception {
        Field field = HelloController.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object getField(String name) throws Exception {
        Field field = HelloController.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(controller);
    }

    private Object invokePrivate(String methodName) throws Exception {
        Method method = HelloController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    private Object invokePrivate(String methodName, Class<?> paramType, Object arg) throws Exception {
        Method method = HelloController.class.getDeclaredMethod(methodName, paramType);
        method.setAccessible(true);
        return method.invoke(controller, arg);
    }
}