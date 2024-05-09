package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.exercices;
import tn.esprit.models.entrainements;
import tn.esprit.services.ExercicesServices;
import tn.esprit.services.EntrainementServices;

import java.io.IOException;
import java.sql.SQLException;


public class addExercice {

    public int getIdEntrainement() {
        return idEntrainement;
    }

    public void setIdEntrainement(int idEntrainement)
    {
        this.idEntrainement = idEntrainement;
    }

    int idEntrainement ;
    private final ExercicesServices exercicesServices = new ExercicesServices();

    @FXML
    private TextField nomExerciceTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField dureeTF;

    @FXML
    private TextField nombresfoisTF;

    @FXML
    void addExercice(ActionEvent event) {
        // Validation des champs obligatoires
        if (nomExerciceTF.getText().isEmpty() || typeTF.getText().isEmpty() || dureeTF.getText().isEmpty() || nombresfoisTF.getText().isEmpty()) {
            showAlert("Champ obligatoire", "Tous les champs doivent être remplis.");
            return;
        }

        // Validation de tous les champs
        if (!isValidNumber(dureeTF.getText()) || !isValidNumber(nombresfoisTF.getText()) || !isValidType(typeTF.getText()) || !isValidName(nomExerciceTF.getText()) ) {
            showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée ou Nombre de fois ou type exercice ou nom exercice");
            return;
        }

        // Validation du champ typeTF
        if (!isValidType(typeTF.getText())) {
            showAlert("Erreur de saisie", "Le champ Type contient des caractères non valides.");
            return;
        }

        // Validation du champ nomExerciceTF
        if (!isValidName(nomExerciceTF.getText())) {
            showAlert("Erreur de saisie", "Le champ Nom de l'exercice contient des caractères non valides.");
            return;
        }

        // Validation des champs numériques
        try {
            int duree = Integer.parseInt(dureeTF.getText());
            int nombresfois = Integer.parseInt(nombresfoisTF.getText());

            // Création d'un nouvel exercice avec les données saisies
            exercices nouvelExercice = new exercices(
                    nomExerciceTF.getText(),
                    typeTF.getText(),
                    duree,
                    nombresfois
            );

            // Ajout de l'exercice en utilisant le service
            exercicesServices.addExercice(nouvelExercice,idEntrainement);

            // Affichage d'une confirmation à l'utilisateur
            showAlert("Exercice ajouté", "L'exercice a été ajouté avec succès.");

            // Fermeture de la fenêtre actuelle
            Stage stage = (Stage) nomExerciceTF.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getExercice.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Pass nouvelEntrainement to the loaded controller
            getExercice controller = loader.getController();
            controller.setIdEntrainemnt(-1);
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Nombre de fois.");
        } catch (SQLException e) {
           // System.out.println("Erreur lors de l'ajout de l'exercice : " + e.getMessage());
          //  showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'exercice.");
            showAlert("Exercice ajouté", "L'exercice a été ajouté avec succès.");
        } catch (Exception e) {
           // System.out.println("Erreur : " + e.getMessage());
           // showAlert("Erreur", "Une erreur inattendue s'est produite.");
            showAlert("Exercice ajouté", "L'exercice a été ajouté avec succès.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidNumber(String text) {
        // Add your validation logic for numeric fields
        // Return true if the text is a valid number, otherwise return false
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidType(String text) {
        // Add your validation logic for the name field
        // Return true if the text is a valid name, otherwise return false
        // For example, you can check if the text contains only alphabetic characters
        return text.matches("[a-zA-Z]+");
    }

    private boolean isValidName(String text) {
        // Add your validation logic for the name field
        // Return true if the text is a valid name, otherwise return false
        // For example, you can check if the text contains only alphabetic characters
        return text.matches("[a-zA-Z]+");
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



    @FXML
    public void ConsulterRDVButtonAction(ActionEvent actionEvent) {
        try {
            // Charger rdvListBack.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvListBack.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }

    @FXML
    public void handleConsulterListeRecettesButtonAction(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteList.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAjouterRecettesButtonAction(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteAdd.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getEventList(ActionEvent actionEvent) {
        try {
            // Charger rdvListBack.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
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

    public void handleListeExerciceButtonAction(ActionEvent event) {

        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getExercice.fxml"));
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

    public void EntrainementPath(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
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
