package views;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import model.exercices;
import javafx.scene.layout.*;
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

        // Load the exercise image
        Image exerciseImage = new Image("file:C:\\Users\\Admin\\Desktop\\projetfeten2\\src\\main\\java\\views\\images\\"+exercice.getId()+".gif");
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
