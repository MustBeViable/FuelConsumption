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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class HelloController {

    private HelloApplication app;
    private ResourceBundle rb;
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
    }

    public void updateWindowTitle() {
        if (app != null && rb != null) {
            app.setTitle(rb.getString("app.title") + " Elias Rinne");
        }
    }

    @FXML
    private void initialize() {
        updateLanguage(ResourceBundle.getBundle("messages", new Locale("en", "US")));
        handleOrientation();
    }

    @FXML
    private void handleCalculate() {
        try {
            double distance = Double.parseDouble(txtDistance.getText());
            double consumptionPer100Km = Double.parseDouble(txtConsumption.getText());
            double pricePerLiter = Double.parseDouble(txtPrice.getText());

            if (distance <= 0 || consumptionPer100Km < 0 || pricePerLiter < 0) {
                lblResult.setText(rb.getString("invalid.input"));
                return;
            }

            double totalFuel = (consumptionPer100Km / 100.0) * distance;
            double totalCost = totalFuel * pricePerLiter;

            lblResult.setText(
                    MessageFormat.format(
                            rb.getString("result.label"),
                            String.format(Locale.US, "%.2f", totalFuel),
                            String.format(Locale.US, "%.2f", totalCost)
                    )
            );

        } catch (NumberFormatException e) {
            lblResult.setText(rb.getString("invalid.input"));
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

    private void updateLanguage(ResourceBundle newBundle) {
        try {
            this.rb = newBundle;

            lblDistance.setText(rb.getString("distance.label"));
            lblConsumption.setText(rb.getString("consumption.label"));
            lblPrice.setText(rb.getString("price.label"));
            btnCalculate.setText(rb.getString("calculate.button"));
            lblResult.setText("");

            updateWindowTitle();
        } catch (MissingResourceException e) {
            lblResult.setText("Missing resource file.");
        }
    }

    @FXML
    private void handleEnglish() {
        orientation = 'L';
        updateLanguage(ResourceBundle.getBundle("messages", new Locale("en", "US")));
        handleOrientation();
    }

    @FXML
    private void handleFrench() {
        orientation = 'L';
        updateLanguage(ResourceBundle.getBundle("messages", new Locale("fr", "FR")));
        handleOrientation();
    }

    @FXML
    private void handleJapanese() {
        orientation = 'L';
        updateLanguage(ResourceBundle.getBundle("messages", new Locale("ja", "JP")));
        handleOrientation();
    }

    @FXML
    private void handlePersian() {
        orientation = 'R';
        updateLanguage(ResourceBundle.getBundle("messages", new Locale("fa", "IR")));
        handleOrientation();
    }
}