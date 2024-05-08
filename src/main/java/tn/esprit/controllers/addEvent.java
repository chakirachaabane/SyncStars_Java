package tn.esprit.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.Category;
import tn.esprit.models.Evenement;
import tn.esprit.models.Format;
import tn.esprit.services.CategoryService;
import tn.esprit.services.EvenementService;
import tn.esprit.services.FormatService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class addEvent {

    @FXML
    private TextField titretf;

    @FXML
    private TextField adressetf;

    @FXML
    private ChoiceBox<String> formatEvetf;

    @FXML
    private ChoiceBox<String> categorieEvetf;

    @FXML
    private DatePicker datetf;

    @FXML
    private TextField imagetf;

    @FXML
    private ComboBox<String> heuretf;

    @FXML
    private TextField descriptiontf;

    @FXML
    private TextField nbPlacestf;

    @FXML
    private TextField imageTF;

    @FXML
    private Pane imagePane;

    private EvenementService evenementService = new EvenementService();
    private CategoryService categoryService = new CategoryService();
    private FormatService formatService = new FormatService();



    @FXML
    private void AjouterEvenement() {
        // Check if any of the fields are empty
        if (titretf.getText().isEmpty() || adressetf.getText().isEmpty() ||
                formatEvetf.getSelectionModel().isEmpty() || categorieEvetf.getSelectionModel().isEmpty() ||
                datetf.getValue() == null || heuretf.getSelectionModel().isEmpty() || descriptiontf.getText().isEmpty() ||
                nbPlacestf.getText().isEmpty()) {
            // Display an error alert
            displayAlert("Error", "Please fill in all the fields.");
            return;
        }

        // Check if nbPlaces is a valid number
        String nbPlacesText = nbPlacestf.getText();
        int nbPlaces;
        try {
            nbPlaces = Integer.parseInt(nbPlacesText);
        } catch (NumberFormatException e) {
            // Display an error alert
            displayAlert("Error", "Please enter a valid number for Nombre places.");
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate selectedDate = datetf.getValue();
        if (selectedDate.isBefore(currentDate)) {
            // Display an error alert
            displayAlert("Error", "Please select a date after the current date.");
            return;
        }

        // Now you can proceed with adding the event using the entered values
        String titre = titretf.getText();
        String adresse = adressetf.getText();
        String format = formatEvetf.getSelectionModel().getSelectedItem().toString();
        String categorie = categorieEvetf.getSelectionModel().getSelectedItem().toString();
        LocalDate date = datetf.getValue();
        String heure = heuretf.getSelectionModel().getSelectedItem().toString();
        String image = imageTF.getText();
        String description = descriptiontf.getText();

        // Implement the logic to add the event with the provided details
        // Create the event object
        Evenement event = new Evenement();
        event.setTitre(titre);
        event.setAdresse(adresse);
        event.setDate(date);
        event.setHeure(Time.valueOf(heure));
        event.setImage(image);
        event.setDescription(description);
        event.setNbPlaces(nbPlaces);
        event.setCategorie(categoryService.readByName(categorie));
        event.setFormat(formatService.readByName(format));
        // Call the service method to insert the event into the database
        evenementService.insert(event);
        // Display a success alert
        displayAlert("Success", "Event added successfully.");

        // Redirect to a new scene
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
            Stage stage = (Stage) titretf.getScene().getWindow();

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

    @FXML
    void chooseImageFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String selectedFileName = selectedFile.getName();
            // Set the image path to the imageTF text field
            imageTF.setText(selectedFile.getAbsolutePath());
            // Load the selected image into the imagePane
            Image image = new Image(selectedFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100); // Adjust the width as needed
            imageView.setFitHeight(100); // Adjust the height as needed
            imagePane.getChildren().clear(); // Clear existing content
            imagePane.getChildren().add(imageView); // Add the image to the pane

            // Move the selected file to the target directory
            File targetDir = new File("C:/Users/LENOVO/Documents/piDevJava/src/main/resources/img/");
            File newFile = new File(targetDir, selectedFileName);
            try {
                Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void initialize() {
        // Populate the choice box for categories
        // Populate the choice box for categories
        List<Category> categories = categoryService.readAll();
        ObservableList<String> categoryNameList = FXCollections.observableArrayList();
        for (Category category : categories) {
            categoryNameList.add(category.getNom());
        }
        categorieEvetf.setItems(categoryNameList);

        // Populate the choice box for formats
        List<Format> formats = formatService.readAll();
        ObservableList<String> formatNameList = FXCollections.observableArrayList();
        for (Format format : formats) {
            formatNameList.add(format.getNom());
        }
        formatEvetf.setItems(formatNameList);
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

    public void getFormatsList(ActionEvent event) {

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

