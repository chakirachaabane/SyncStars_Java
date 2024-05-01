package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class  ViewUtils {

    public static void loadScene(String fxmlFileName, AnchorPane rootPane) {
        try {
            Parent parent = FXMLLoader.load(ViewUtils.class.getResource(fxmlFileName));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérer la scène actuelle
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
