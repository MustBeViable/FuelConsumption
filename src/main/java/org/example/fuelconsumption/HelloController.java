package org.example.fuelconsumption;

import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.MessageFormat;
import java.util.Locale;

public class HelloController {

    private HelloApplication app;
    private LocalizationService localizationService;
    private CalculationService calculationService;
    private String currentLanguage = "en_US";
    private char orientation = 'L';

    @FXML
    private VBox vBox;

    @FXML
    private HBox languageBox;

    @FXML
    private Label lblDistance;

    @FXML
    private Label lblConsumption;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblResult;

    @FXML
    private TextField txtDistance;

    @FXML
    private TextField txtConsumption;

    @FXML
    private TextField txtPrice;

    @FXML
    private Button btnCalculate;

    public void setApp(HelloApplication app) {
        this.app = app;
        this.localizationService = app.getLocalizationService();
        this.calculationService = app.getCalculationService();
    }

    public void initializeApp() {
        setLanguage("en_US");
        handleOrientation();
    }

    public void updateWindowTitle() {
        if (app != null) {
            app.setTitle(getText("app.title") + " Elias Rinne");
        }
    }

    @FXML
    private void initialize() {
        // JavaFX lifecycle hook. Actual app initialization is done in initializeApp().
    }

    @FXML
    private void handleCalculate() {
        try {
            double distance = Double.parseDouble(txtDistance.getText());
            double consumptionPer100Km = Double.parseDouble(txtConsumption.getText());
            double pricePerLiter = Double.parseDouble(txtPrice.getText());

            if (distance <= 0 || consumptionPer100Km < 0 || pricePerLiter < 0) {
                lblResult.setText(getText("invalid.input"));
                return;
            }

            double totalFuel = (consumptionPer100Km / 100.0) * distance;
            double totalCost = totalFuel * pricePerLiter;

            String formattedResult = MessageFormat.format(
                    getText("result.label"),
                    String.format(Locale.US, "%.2f", totalFuel),
                    String.format(Locale.US, "%.2f", totalCost)
            );

            CalculationRecord rec = new CalculationRecord(
                    distance,
                    consumptionPer100Km,
                    pricePerLiter,
                    totalFuel,
                    totalCost,
                    currentLanguage
            );

            try {
                calculationService.saveCalculation(rec);
                lblResult.setText(formattedResult + " | " + getText("save.success"));
            } catch (Exception e) {
                lblResult.setText(formattedResult + " | " + getText("save.error"));
            }

        } catch (NumberFormatException e) {
            lblResult.setText(getText("invalid.input"));
        }
    }

    private void handleOrientation() {
        if (orientation == 'L') {
            vBox.setAlignment(Pos.TOP_LEFT);
            vBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

            lblDistance.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            lblConsumption.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            lblPrice.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            lblResult.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

            txtDistance.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            txtConsumption.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            txtPrice.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

            btnCalculate.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

            if (languageBox != null) {
                languageBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                languageBox.setAlignment(Pos.CENTER);
            }

        } else {
            vBox.setAlignment(Pos.TOP_RIGHT);
            vBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

            lblDistance.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            lblConsumption.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            lblPrice.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            lblResult.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            txtDistance.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            txtConsumption.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            txtPrice.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            btnCalculate.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            if (languageBox != null) {
                languageBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                languageBox.setAlignment(Pos.CENTER);
            }
        }
    }

    private void setLanguage(String language) {
        currentLanguage = language;
        try {
            localizationService.loadStrings(language);
            lblDistance.setText(getText("distance.label"));
            lblConsumption.setText(getText("consumption.label"));
            lblPrice.setText(getText("price.label"));
            btnCalculate.setText(getText("calculate.button"));
            lblResult.setText("");
            updateWindowTitle();
        } catch (Exception e) {
            lblResult.setText("Database localization error.");
        }
    }

    private String getText(String key) {
        String value = localizationService.getString(key);
        return value != null ? value : ('!' + key + '!');
    }

    @FXML
    private void handleEnglish() {
        orientation = 'L';
        setLanguage("en_US");
        handleOrientation();
    }

    @FXML
    private void handleFrench() {
        orientation = 'L';
        setLanguage("fr_FR");
        handleOrientation();
    }

    @FXML
    private void handleJapanese() {
        orientation = 'L';
        setLanguage("ja_JP");
        handleOrientation();
    }

    @FXML
    private void handlePersian() {
        orientation = 'R';
        setLanguage("fa_IR");
        handleOrientation();
    }
}
