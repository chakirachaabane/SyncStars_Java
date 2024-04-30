package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.models.recette;
import tn.esprit.services.RecetteService;
import javafx.scene.paint.Color;

import java.io.IOException;

public class recetteAdd {

    @FXML
    private TextField Nomfield;

    @FXML
    private TextField Ingredientsfield;

    @FXML
    private TextField etapesfield;

    @FXML
    private ComboBox<String> problemeComboBox;

    @FXML
    private Label nomErrorLabel;

    @FXML
    private Label ingredientsErrorLabel;

    @FXML
    private Label etapesErrorLabel;

    @FXML
    private Label problemeErrorLabel;


    private RecetteService recetteService;

    public recetteAdd() {
        recetteService = new RecetteService();
    }

    @FXML
    void initialize() {
        // Vérifier si la ComboBox est vide avant de l'initialiser
        if (problemeComboBox.getItems().isEmpty()) {
            // Remplir la ComboBox avec les options
            problemeComboBox.getItems().addAll("Obésité", "Diabète", "Anorexie", "Déséquilibre alimentaire", "Cholestérol");
        }
    }

    @FXML
    void ajouterRecette(ActionEvent event) {
        // Récupérer les données du formulaire
        String nom = Nomfield.getText();
        String ingredients = Ingredientsfield.getText();
        String etapes = etapesfield.getText();
        String probleme = problemeComboBox.getValue();

        // Réinitialiser les libellés d'erreur
        resetErrorLabels();

        // Vérifier si les champs obligatoires ne sont pas vides
        boolean isError = false;
        if (nom.isEmpty()) {
            nomErrorLabel.setText("Veuillez saisir le nom de la recette.");
            isError = true;
        } else if (nom.matches(".*\\d.*")) { // Vérifier s'il y a des chiffres dans le nom
            nomErrorLabel.setText("Le nom de la recette ne doit pas contenir de chiffres.");
            nomErrorLabel.setTextFill(Color.RED); // Définir la couleur du texte en rouge
            isError = true;
        }
        if (ingredients.isEmpty()) {
            ingredientsErrorLabel.setText("Veuillez saisir les ingrédients.");
            isError = true;
        }
        if (etapes.isEmpty()) {
            etapesErrorLabel.setText("Veuillez saisir les étapes.");
            isError = true;
        }
        if (probleme == null) {
            problemeErrorLabel.setText("Veuillez sélectionner un problème.");
            isError = true;
        }

        // Si des erreurs sont détectées, arrêtez la fonction
        if (isError) {
            return;
        }



        // Créer une nouvelle recette avec les données récupérées
        recette nouvelleRecette = new recette();
        nouvelleRecette.setNom_recette(nom);
        nouvelleRecette.setIngredients(ingredients);
        nouvelleRecette.setEtapes(etapes);
        nouvelleRecette.setProbleme(probleme);

        // Appeler la méthode addRecette de RecetteService pour ajouter la recette à la base de données
        boolean ajoutReussi = recetteService.addRecette(nouvelleRecette);

        // Afficher une alerte de succès si l'ajout a réussi
        if (ajoutReussi) {
            afficherAlerte("Succès", "Recette ajoutée avec succès !");
            // Réinitialiser les champs après l'ajout
            Nomfield.clear();
            Ingredientsfield.clear();
            etapesfield.clear();
            problemeComboBox.getSelectionModel().clearSelection();
        } else {
            afficherAlerte("Erreur", "Une erreur est survenue lors de l'ajout de la recette.");
        }
    }



    private void resetErrorLabels() {
        nomErrorLabel.setText("");
        ingredientsErrorLabel.setText("");
        etapesErrorLabel.setText("");
        problemeErrorLabel.setText("");
    }

    private void afficherAlerte(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    public void consulterListeRecettesButtonAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteList.fxml"));
            Stage nouvelleStage = new Stage();
            nouvelleStage.setScene(new Scene(loader.load()));
            nouvelleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
