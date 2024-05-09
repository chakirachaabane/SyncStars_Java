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
            Image image = new Image(new File(event.getImage()).toURI().toString());
            imageView.setImage(image);
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
