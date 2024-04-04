package org.example.controllers;

import org.example.model.Evenement;
import org.example.services.ServicesEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class AjouterEvenementController {
    @FXML
    private TextField titretf;

    @FXML
    private TextField adressetf;

    @FXML
    private TextField formatEvetf;

    @FXML
    private TextField categorieEvetf;


    @FXML
    private TextField imagetf;

    @FXML
    void AjouterEvenement(ActionEvent event) {

        ServicesEvenement serviceEve = new ServicesEvenement();

        // Récupérer les valeurs des champs
        String titre = titretf.getText();
        String adresse = adressetf.getText();
        String formatEve = formatEvetf.getText();
        String categorieEve = categorieEvetf.getText();
        java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());

        String image = imagetf.getText();

        if (titre.isEmpty() || adresse.isEmpty() || formatEve.isEmpty() || categorieEve.isEmpty() || date == null) {
            // Afficher un message d'erreur si un champ est vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {
                // Créer un nouvel Evenement avec les valeurs des champs
                Evenement e = new Evenement(0, titre, adresse, date, image, categorieEve, formatEve);

                // Ajouter l'Evenement en utilisant le service
                serviceEve.ajouter(e);

                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Evenement ajouté avec succès!");
                alert.showAndWait();

            }
        }

}
