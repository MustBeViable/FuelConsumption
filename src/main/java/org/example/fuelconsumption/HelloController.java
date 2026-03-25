package org.example.fuelconsumption;

import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloController {

    private HelloApplication app;

    private char orientation = 'L';

    private ResourceBundle rb = ResourceBundle.getBundle(
            "fuelConsumptionBundle",
            new Locale("en", "EN")
    );

    public void setApp(HelloApplication app) {
        this.app = app;
    }

    public void updateWindowTitle() {
        if (app != null) {
            app.setTitle(rb.getString("app.title"));
        }
    }

    @FXML
    private VBox vBox;

    @FXML
    private Label lblDistance;

    @FXML
    private Label lblFuel;

    @FXML
    private Label lblResult;

    @FXML
    private TextField txtDistance;

    @FXML
    private TextField txtFuel;

    @FXML
    private Button btnCalculate;

    @FXML
    private void initialize() {
        updateLanguage(rb);
        handleOrientation();
    }

    @FXML
    private void handleCalculate() {
        try {
            double distance = Double.parseDouble(txtDistance.getText());
            double fuel = Double.parseDouble(txtFuel.getText());

            if (distance <= 0 || fuel < 0) {
                lblResult.setText(rb.getString("invalid.input"));
                return;
            }

            double consumption = (fuel / distance) * 100;

            lblResult.setText(
                    MessageFormat.format(
                            rb.getString("result.label"),
                            String.format("%.2f", consumption)
                    )
            );

        } catch (Exception e) {
            lblResult.setText(rb.getString("invalid.input"));
        }
    }

    private void handleOrientation() {
        if (orientation == 'L') {
            vBox.setAlignment(Pos.TOP_LEFT);
            txtDistance.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            txtFuel.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        } else {
            vBox.setAlignment(Pos.TOP_RIGHT);
            txtDistance.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            txtFuel.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
    }

    private void updateLanguage(ResourceBundle newBundle) {
        this.rb = newBundle;

        lblDistance.setText(rb.getString("distance.label"));
        lblFuel.setText(rb.getString("fuel.label"));
        btnCalculate.setText(rb.getString("calculate.button"));
        lblResult.setText("");

        updateWindowTitle();
    }

    @FXML
    private void handleEnglish() {
        orientation = 'L';
        updateLanguage(ResourceBundle.getBundle("fuelConsumptionBundle", new Locale("en", "EN")));
        handleOrientation();
    }

    @FXML
    private void handleFrench() {
        orientation = 'L';
        updateLanguage(ResourceBundle.getBundle("fuelConsumptionBundle", new Locale("fr", "FR")));
        handleOrientation();
    }

    @FXML
    private void handleJapanese() {
        orientation = 'L';
        updateLanguage(ResourceBundle.getBundle("fuelConsumptionBundle", new Locale("ja", "JP")));
        handleOrientation();
    }

    @FXML
    private void handlePersian() {
        orientation = 'R';
        updateLanguage(ResourceBundle.getBundle("fuelConsumptionBundle", new Locale("fa", "IR")));
        handleOrientation();
    }
}