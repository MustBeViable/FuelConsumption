package org.example.fuelconsumption;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Stage stage;
    private final LocalizationService localizationService = new LocalizationService();
    private final CalculationService calculationService = new CalculationService();

    public void setTitle(String title) {
        if (stage != null) {
            stage.setTitle(title);
        }
    }

    public LocalizationService getLocalizationService() {
        return localizationService;
    }

    public CalculationService getCalculationService() {
        return calculationService;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 420, 320);

        HelloController controller = fxmlLoader.getController();
        controller.setApp(this);
        controller.initializeApp();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
