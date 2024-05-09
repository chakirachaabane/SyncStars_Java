package tn.esprit.views;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import tn.esprit.models.exercices;
import javafx.geometry.Pos;

public class ExerciceCardView extends FlowPane {
int poids ;
    public ExerciceCardView(exercices exercice,int poids) {
        this.poids = poids ;
        setPrefWidth(200);
        setPrefHeight(200);
        setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-padding: 10px;");

        int result = (int)((poids * exercice.getNombres_de_fois()) / 100.0);

        // Create labels for exercise details
        Label lblNom = new Label(exercice.getNom_exercice());
        Label lblType = new Label("Type: " + exercice.getType());
        Label lblDuree = new Label("Durée: " + exercice.getDuree() + " minutes");
        Label lblNombresDeFois = new Label("Nombre de fois: " + result);


        System.out.println(exercice.getId());
        // Load the exercise image

        // Image exerciseImage = new Image("C:\\Users\\HP\\Downloads\\IntegrationV\\IntegrationV\\src\\main\\java\\tn\\esprit\\views\\images\\"+exercice.getId()+".gif");
        //Image exerciseImage = new Image("C:\\Users\\LENOVO\\OneDrive - ESPRIT\\Bureau\\IntegrationFinale\\IntegrationFinale\\src\\main\\java\\tn\\esprit\\views\\images\\"+ exercice.getId()+".gif");
        Image exerciseImage = new Image("file:///C:/Users/LENOVO/OneDrive - ESPRIT/Bureau/IntegrationFinale/IntegrationFinale/src/main/java/tn/esprit/views/images/"+ exercice.getId()+".gif");

        ImageView imageView = new ImageView(exerciseImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        // Add labels and image to the card
        getChildren().addAll(lblNom, lblType, lblDuree, lblNombresDeFois, imageView);
        setAlignment(Pos.CENTER_LEFT);
        setVgap(8);
        setHgap(10);
    }
}
