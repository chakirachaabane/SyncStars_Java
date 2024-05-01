package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    String typeUser = "";


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Chargement du fichier FXML...");
       // typeUser = "Admin" ;
     /*   FXMLLoader loader ;
        if (typeUser.equals("Admin")) {
            loader = new FXMLLoader(getClass().getResource("/addEntrainement.fxml"));
        }
        else {
            loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
        }
        Parent root = loader.load();*/
        Button frontButton = new Button("Front");
        Button backButton = new Button("Back");

        System.out.println("Fichier FXML chargé avec succès.");
        frontButton.setOnAction(e -> {
            System.out.println("Front button clicked!");
            try {
                // Load the new scene from FXML file

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
                Parent root = loader.load();
                // Create a new stage for the new scene
                Stage newStage = new Stage();
                newStage.setTitle("New Scene");
                newStage.setScene(new Scene(root, 800, 400)); // Set scene dimensions
                newStage.show();
            } catch (Exception ex) {
                System.err.println("Error loading FXML file: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> {
            System.out.println("Back button clicked!");
            // Add your desired action here for the Back button
            try {
                // Load the new scene from FXML file

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/addEntrainement.fxml"));
                Parent root = loader.load();
                // Create a new stage for the new scene
                Stage newStage = new Stage();
                newStage.setTitle("New Scene");
                newStage.setScene(new Scene(root, 800, 400)); // Set scene dimensions
                newStage.show();
            } catch (Exception ex) {
                System.err.println("Error loading FXML file: " + ex.getMessage());
            }
        });

        // Arrange buttons horizontally using an HBox
        HBox buttonBox = new HBox(10); // 10 pixels spacing
        buttonBox.getChildren().addAll(frontButton, backButton);

        // Create a scene and set it on the stage
        Scene scene = new Scene(buttonBox, 800, 400); // Width, height
        primaryStage.setScene(scene);
        //Scene scene = new Scene(root);
        primaryStage.setTitle("SyncStars");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}