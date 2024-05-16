package tn.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.controllers.EventItemController;
import tn.esprit.services.EvenementService;
import tn.esprit.models.Evenement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EventItemController {

    @FXML
    private Label titre;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Label date;

    @FXML
    private Label heure;

    @FXML
    private Label place;

    @FXML
    private Label adresse;

    @FXML
    private Label format;

    @FXML
    private Label categorie;

    public void initData(Evenement event) {
        titre.setText(event.getTitre());
        descriptionLabel.setText(event.getDescription());
        if (event.getImage() != null && !event.getImage().isEmpty()) {
            try {
                String imageUrl = event.getImage();

                // Check if the image path is a valid URL or a local file
                if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                    File imageFile = new File(imageUrl);
                    if (!imageFile.exists()) {
                        // If the file doesn't exist, try to construct the full path
                        imageUrl = "C:/Users/LENOVO/OneDrive - ESPRIT/Images/integration chakira +nawres+feten+azza+aziz/integration chakira +nawres+feten+azza - Copie2/integration chakira +nawres+feten/SecondProject1/public/FrontOffice/img/" + imageUrl;
                        imageFile = new File(imageUrl);
                        if (!imageFile.exists()) {
                            System.err.println("Fichier d'image introuvable: " + imageUrl);
                            return;
                        }
                    }
                    imageUrl = imageFile.toURI().toString();
                }

                Image image = new Image(imageUrl, 700, 500, false, true);
                imageView.setImage(image);

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
                e.printStackTrace();
            }
        }
        date.setText(event.getDate().toLocalDate().toString());
        heure.setText(event.getHeure().toString());
        place.setText(String.valueOf(event.getNbPlaces()) + " places");
        adresse.setText(event.getAdresse());
        format.setText(event.getFormat().getNom());
        categorie.setText(event.getCategorie().getNom());

    }

    public void getBack(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
