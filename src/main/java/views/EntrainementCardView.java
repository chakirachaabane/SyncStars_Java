package views;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import model.entrainements;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import model.exercices;
import services.ExercicesServices;

import java.util.List;

public class EntrainementCardView extends FlowPane {


    private final ExercicesServices exercicesServices = new ExercicesServices();

    entrainements entrainement ;
    int poids ;

    public EntrainementCardView(entrainements entrainement,String objectif,int poids) {
        // Set the size and style of the card
        this.poids = poids;
        this.entrainement = entrainement ;
        setPrefWidth(200);
        setPrefHeight(250);
        setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-padding: 10px;");

        // Specify the absolute file path to the image

        Image exerciseImage = new Image("file:C:\\Users\\Admin\\Desktop\\projetfeten2\\src\\main\\java\\views\\images\\pertedepo.jpg");
      if (objectif.equals("se muscler"))
      { exerciseImage = new Image("file:C:\\Users\\Admin\\Desktop\\projetfeten2\\src\\main\\java\\views\\images\\semuscler.jpg");
      }
        ImageView imageView = new ImageView(exerciseImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        // Create labels for the properties of entrainements
        Label lblNom = new Label(entrainement.getNom_entrainement());
        Label lblPeriode = new Label("Période: " + entrainement.getPeriode());
        Label lblObjectif = new Label("Objectif: " + entrainement.getObjectif());

        // Add labels to the card
        getChildren().addAll(lblNom, lblPeriode, lblObjectif,imageView);
        setAlignment(Pos.TOP_LEFT);
        setVgap(8);
        setHgap(10);

        // Set click handler
        setOnMouseClicked(event -> handleCardClick());
    }

    private void openImageStage(Image image) {
        // Create a new stage to display the image
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400); // Set as per requirement
        imageView.setFitWidth(600); // Set as per requirement

        Stage newStage = new Stage();
        newStage.setTitle("Image View");
        newStage.setScene(new Scene(new StackPane(imageView), 600, 400));
        newStage.show();
    }

    private void handleCardClick() {
        // Create the Exercise List View
        List<exercices> exercicesList = null ;
        try {
             exercicesList = exercicesServices.getByIdEntrainemnt(entrainement.getId()) ;
        }
         catch (Exception e )
         {}

        ExerciceListView exerciceListView = new ExerciceListView(exercicesList,poids);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(exerciceListView);
        scrollPane.setFitToWidth(true);
        // Create a new stage to display the Exercise List View
        Stage newStage = new Stage();
        newStage.setTitle("Liste des exercices");
        newStage.setScene(new Scene(scrollPane, 800, 600)); // Adjust size as needed
        newStage.show();
    }
}
