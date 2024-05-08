package com.example.pidevproject;

import Controllers.ForumPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainJavaFx extends Application {

    @Override
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}

