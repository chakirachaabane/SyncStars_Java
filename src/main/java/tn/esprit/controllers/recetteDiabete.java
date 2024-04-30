package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import tn.esprit.models.recette;
import tn.esprit.services.RecetteService;

import java.util.List;

public class recetteDiabete {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label label;

    public void initialize() {
        // Instancier le service de gestion des recettes
        RecetteService recetteService = new RecetteService();

        // Récupérer toutes les recettes liées au problème "Diabète"
        List<recette> recettesDiabete = recetteService.getByProbleme("Diabète");

        // Afficher les recettes dans la fenêtre
        afficherRecettes(recettesDiabete);
    }

    private void afficherRecettes(List<recette> recettes) {
        int y = 200; // Position Y initiale pour afficher les recettes
        for (recette r : recettes) {
            // Créer un nouveau pane avec un cadre vert
            Pane recettePane = new Pane();
            recettePane.setLayoutX(100);
            recettePane.setLayoutY(y);
            recettePane.setPrefWidth(261);
            recettePane.setPrefHeight(100);
            recettePane.setStyle("-fx-background-color: rgba(105, 191, 167, 0.8);" +
                    "-fx-background-radius: 20;" +
                    "-fx-border-radius: 20;" +
                    "-fx-border-color: #69BFA7;" +
                    "-fx-border-width: 5;");

            // Ajouter le nom de la recette centré en haut du cadre
            Label nomRecetteLabel = new Label(r.getNom_recette());
            nomRecetteLabel.setPrefWidth(261);
            nomRecetteLabel.setPrefHeight(30);
            nomRecetteLabel.setAlignment(Pos.CENTER);
            nomRecetteLabel.setTextAlignment(TextAlignment.CENTER);
            nomRecetteLabel.setFont(Font.font("Arial", 16));
            recettePane.getChildren().add(nomRecetteLabel);

            // Ajouter les ingrédients
            Label ingredientsLabel = new Label("Ingrédients : " + r.getIngredients());
            ingredientsLabel.setLayoutX(10);
            ingredientsLabel.setLayoutY(40);
            ingredientsLabel.setFont(Font.font("Arial", 14));
            recettePane.getChildren().add(ingredientsLabel);

            // Ajouter les étapes
            Label etapesLabel = new Label("Étapes : " + r.getEtapes());
            etapesLabel.setLayoutX(10);
            etapesLabel.setLayoutY(70);
            etapesLabel.setFont(Font.font("Arial", 14));
            recettePane.getChildren().add(etapesLabel);

            // Ajouter le pane à l'anchorPane
            anchorPane.getChildren().add(recettePane);

            // Incrémenter la position Y pour afficher la recette suivante
            y += 130; // Ajuster la distance entre les recettes
        }
    }
}
