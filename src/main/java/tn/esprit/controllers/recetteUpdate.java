package tn.esprit.controllers;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.recette;
import tn.esprit.services.RecetteService;

import java.io.IOException;

public class recetteUpdate {

    @FXML
    private TextField Nomfield;

    @FXML
    private ComboBox<String> problemeComboBox;

    @FXML
    private TextField Ingredientsfield;

    @FXML
    private TextField etapesfield;

    private RecetteService recetteService;

    private recette selectedRecette;



    @FXML
    private Label nomErrorLabel;

    @FXML
    private Label ingredientsErrorLabel;

    @FXML
    private Label etapesErrorLabel;


    @FXML
    private Label problemeErrorLabel;




    public recetteUpdate() {
        recetteService = new RecetteService();
    }

    public void initData(recette selectedRecette) {
        this.selectedRecette = selectedRecette;
        Nomfield.setText(selectedRecette.getNom_recette());
        Ingredientsfield.setText(selectedRecette.getIngredients());
        etapesfield.setText(selectedRecette.getEtapes());
        problemeComboBox.setValue(selectedRecette.getProbleme());
    }

    @FXML
    public void modifierRecette(ActionEvent actionEvent) {
        // Récupérer les nouvelles données depuis les champs de saisie
        String nouveauNom = Nomfield.getText();
        String nouveauxIngredients = Ingredientsfield.getText();
        String nouvellesEtapes = etapesfield.getText();
        String nouveauProbleme = problemeComboBox.getValue();

        // Réinitialiser les libellés d'erreur
        resetErrorLabels();

        // Vérifier si les champs obligatoires ne sont pas vides
        boolean isError = false;
        if (nouveauNom.isEmpty()) {
            showError(nomErrorLabel, "Veuillez saisir le nom de la recette.");
            isError = true;
        } else if (nouveauNom.matches(".*\\d.*")) { // Vérifier s'il y a des chiffres dans le nom
            showError(nomErrorLabel, "Le nom de la recette ne doit pas contenir de chiffres.");
            isError = true;
        }
        if (nouveauxIngredients.isEmpty()) {
            showError(ingredientsErrorLabel, "Veuillez saisir les ingrédients.");
            isError = true;
        }
        if (nouvellesEtapes.isEmpty()) {
            showError(etapesErrorLabel, "Veuillez saisir les étapes.");
            isError = true;
        }
        if (nouveauProbleme == null) {
            showError(problemeErrorLabel, "Veuillez sélectionner un problème.");
            isError = true;
        }

        // Si des erreurs sont détectées, arrêtez la fonction
        if (isError) {
            return;
        }

        // Mettre à jour la recette dans la base de données
        selectedRecette.setNom_recette(nouveauNom);
        selectedRecette.setIngredients(nouveauxIngredients);
        selectedRecette.setEtapes(nouvellesEtapes);
        selectedRecette.setProbleme(nouveauProbleme);

        recetteService.updateRecette(selectedRecette);

        // Afficher un message de succès uniquement si la modification a réussi
        showAlert("Modification réussie", "La recette a été modifiée avec succès dans la base de données.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(Label errorLabel, String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setTextFill(Color.RED); // Définir la couleur du texte en rouge
    }

    private void resetErrorLabels() {
        nomErrorLabel.setText("");
        ingredientsErrorLabel.setText("");
        etapesErrorLabel.setText("");
        problemeErrorLabel.setText("");
    }

    @FXML
    public void consulterListeRecettesButtonAction(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteList.fxml"));
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
    public void ConsulterRDVButtonAction(ActionEvent actionEvent) {
        try {
            // Charger rdvListBack.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/rdvListBack.fxml"));
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
    public void handleConsulterListeRecettesButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteList.fxml"));
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
    public void handleAjouterRecettesButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteAdd.fxml"));
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



}
