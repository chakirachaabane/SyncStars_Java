package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Format;
import tn.esprit.services.FormatService;

import java.io.IOException;

public class AjouterFormatController {


        @FXML
        private TextField nomtf;

        private FormatService sf = new FormatService();

        public void AjouterFormat(ActionEvent actionEvent) {

            // Check if any of the fields are empty
            if (nomtf.getText().isEmpty()) {
                // Display an error alert
                displayAlert("Error", "Please fill in all the fields.");
                return;
            }

            String Nom = nomtf.getText();

            Format f = new Format();
            f.setNom(Nom);
            sf.insert(f);
            // Display a success alert
            displayAlert("Success", "Event added successfully.");

            redirectToNewScene();
        }

        private void displayAlert(String title, String content) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }

        private void redirectToNewScene() {

            try {
                // Load the FXML file for the new scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
                Parent root = loader.load();

                // Get the stage from the current scene
                Stage stage = (Stage) nomtf.getScene().getWindow();

                // Create a new scene
                Scene scene = new Scene(root);

                // Set the new scene
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void getEventList(ActionEvent event) {
            try {
                // Get the current stage
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Close the current stage
                currentStage.close();

                // Show the new stage
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void generatePieChart(ActionEvent event) {

            try {
                // Get the current stage
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PieChartEvent.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Close the current stage
                currentStage.close();

                // Show the new stage
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    public void getCatList(ActionEvent event) {

        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategorieEve.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Close the current stage
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFormatList(ActionEvent event) {

        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormat.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Close the current stage
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
