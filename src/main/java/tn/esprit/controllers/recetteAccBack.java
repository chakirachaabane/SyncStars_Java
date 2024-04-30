package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.services.RecetteService;

import java.io.IOException;

public class recetteAccBack {

    @FXML
    private Button prendreRdvButton;

    private RecetteService recetteService = new RecetteService();

    @FXML
    public void initialize() {
        // Ajout du gestionnaire d'événements au bouton "Ajouter recette"
        prendreRdvButton.setOnAction(event -> {
            try {
                // Chargement du fichier recetteAdd.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteAdd.fxml"));
                Parent root = loader.load();

                // Affichage de la scène
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

}
