package tn.esprit.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class recetteAcc {

    @FXML
    private HBox imageBox;

    @FXML
    private Button retourButton; // Ajout du bouton "Retour"

    // Définir une durée de transition plus courte pour un défilement plus fluide
    private static final Duration TRANSITION_DURATION = Duration.seconds(2.5);

    public void initialize() {
        // Créer une timeline pour gérer le défilement infini
        Timeline timeline = new Timeline(
                // Ajouter une action pour chaque intervalle de transition
                new KeyFrame(TRANSITION_DURATION, event -> {
                    // Déplacer la première image à la fin de la liste
                    imageBox.getChildren().add(imageBox.getChildren().remove(0));
                })
        );
        // Répéter la timeline indéfiniment
        timeline.setCycleCount(Timeline.INDEFINITE);
        // Démarrer la timeline
        timeline.play();
    }

    @FXML
    public void handleMouseEntered() {
        // Implémentez ce que vous voulez faire lorsque la souris entre dans la zone des images
    }

    @FXML
    public void handleMouseExited() {
        // Implémentez ce que vous voulez faire lorsque la souris sort de la zone des images
    }

    @FXML
    public void retourButtonClicked(ActionEvent event) {
        // Récupérer la scène actuelle à partir de l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Charger et afficher rdvAcc.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/rdvAcc.fxml")); // Vérifiez le chemin ici
            stage.setScene(new Scene(root));
            stage.setTitle("Espace Rdv");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple, en affichant un message d'erreur
        }
    }

    @FXML
    public void diabeteButtonClicked(ActionEvent event) {
        // Récupérer la scène actuelle à partir de l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Charger et afficher recetteDiabete.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/recetteDiabete.fxml")); // Vérifiez le chemin ici
            stage.setScene(new Scene(root));
            stage.setTitle("Recette Diabète");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple, en affichant un message d'erreur
        }
    }


    @FXML
    private void handleObesiteButtonClicked(ActionEvent event) {
        // Charger le fichier FXML de recetteObésité.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteObesite.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
