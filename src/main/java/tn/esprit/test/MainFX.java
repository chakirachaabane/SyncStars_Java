package tn.esprit.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.controllers.ForumPageController;

import java.io.IOException;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage)  throws  Exception{
        //  FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("AlignVibe");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    /*@Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main_Forum_Page.fxml"));
        Parent root = loader.load();

        // Get the controller associated with the FXML file
        ForumPageController controller = loader.getController();


        // Set up the stage
        primaryStage.setTitle("Forum Page");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }*/

    public static void main(String[] args) {
        launch(args);
    }
}





