package org.example.fuelconsumption;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloApplication extends Application {
    private Stage stage;

    public void setTitle(String title) {
        if (stage != null) {
            stage.setTitle(title);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        Locale enUS = new Locale("en", "US");

        ResourceBundle bundle = ResourceBundle.getBundle(
                "fuelConsumptionBundle_en_US"
        );

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml"),
                bundle
        );

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        HelloController controller = fxmlLoader.getController();
        controller.setApp(this);
        controller.updateWindowTitle();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}