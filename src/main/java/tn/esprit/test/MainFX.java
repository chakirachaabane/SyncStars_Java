package tn.esprit.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

public class MainFX extends Application {
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayFrontProduit.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("AlignVibe");
        primaryStage.setScene(scene);
        primaryStage.show();
        Button sidebarProduit = (Button) root.lookup("#sidebarProduit");
        Button sidebarCategorie = (Button) root.lookup("#sidebarCategorie");

    }

    public static void main(String[] args) {
        launch(args);
    }
}

