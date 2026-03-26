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

        ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("en", "US"));

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml"),
                bundle
        );

        Scene scene = new Scene(fxmlLoader.load(), 420, 320);

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