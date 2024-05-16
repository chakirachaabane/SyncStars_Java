package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.models.Evenement;
import tn.esprit.services.EvenementService;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class detailsEvent {

    @FXML
    private ImageView imageView;

    @FXML
    private Label titre;

    @FXML
    private Label adresse;

    @FXML
    private Label description;

    @FXML
    private Label places;

    @FXML
    private Label date;

    private EvenementService es;

    public detailsEvent() {
        this.es = new EvenementService();
    }


    public void afficherDetailsEvent(int id) {
        try {
            Evenement e = es.getById(id);

            if (e != null) {
                titre.setText(e.getTitre());
                adresse.setText(e.getAdresse());
                description.setText(e.getDescription());
                String imageUrl = e.getImage();
                if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                    File imageFile = new File(imageUrl);
                    if (imageFile.exists()) {
                        imageUrl = imageFile.toURI().toURL().toString();
                    } else {
                        imageUrl = "file:///C:/Users/LENOVO/OneDrive - ESPRIT/Images/integration chakira +nawres+feten+azza+aziz/integration chakira +nawres+feten+azza - Copie2/integration chakira +nawres+feten/SecondProject1/public/FrontOffice/img/" + imageUrl;

                        imageFile = new File(imageUrl.replace("file:///", ""));
                        if (!imageFile.exists()) {
                            System.err.println("Fichier d'image introuvable : " + imageUrl);
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erreur");
                            alert.setHeaderText(null);
                            alert.setContentText("Fichier d'image introuvable : " + imageUrl);
                            alert.showAndWait();
                            return;
                        }
                    }
                }

                Image image = new Image(imageUrl, 700, 500, false, true);
                imageView.setImage(image);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("URL d'image incorrecte : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
