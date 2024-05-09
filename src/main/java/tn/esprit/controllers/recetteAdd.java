package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteList.fxml"));
            Stage nouvelleStage = new Stage();
            nouvelleStage.setScene(new Scene(loader.load()));
            nouvelleStage.show();
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

    @FXML
    private void EntrainementPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }










    @FXML
    public void allerVersUser(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayUsers-view.fxml"));
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
    public void allerAjouterUser(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addUserAdmin-view.fxml"));
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
    public void allerVersProduits(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayProduit.fxml"));
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
    public void allerAjouterProduits(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addProduit.fxml"));
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
    public void allerListCat(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayCategorie.fxml"));
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
    public void allerAjouterCat(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCategorie.fxml"));
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



}
