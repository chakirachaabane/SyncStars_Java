package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import tn.esprit.models.recette;
import tn.esprit.services.RecetteService;

import java.util.List;

public class recetteObesite {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private HBox recettesPane; // Utiliser un HBox pour disposer les recettes horizontalement

    public void initialize() {
        // Instancier le service de gestion des recettes
        RecetteService recetteService = new RecetteService();

        // Récupérer toutes les recettes liées au problème "Obésité"
        List<recette> recettesObesite = recetteService.getByProbleme("Obésité");

        // Afficher les recettes dans le pane dédié
        afficherRecettes(recettesObesite);
    }

    private void afficherRecettes(List<recette> recettes) {
        int count = 0; // Pour compter le nombre de recettes ajoutées à chaque ligne
        for (recette r : recettes) {
            // Créer un nouveau pane avec un cadre vert
            Pane recettePane = new Pane();
            recettesPane.setSpacing(10); // Définir un espacement de 10 pixels entre les panneaux de recette

            recettePane.setPrefWidth(300); // Largeur du pane de recette
            recettePane.setPrefHeight(120); // Hauteur du pane de recette
            recettePane.setStyle("-fx-background-color: rgba(105, 191, 167, 0.8);" +
                    "-fx-background-radius: 20;" +
                    "-fx-border-radius: 20;" +
                    "-fx-border-color: #69BFA7;" +
                    "-fx-border-width: 5;");

            // Ajouter les champs d'une recette à l'intérieur du pane
            Label nomRecetteLabel = new Label("Nom : " + r.getNom_recette());
            nomRecetteLabel.setLayoutX(10); // Ajuster la position X
            nomRecetteLabel.setLayoutY(10);
            nomRecetteLabel.setFont(Font.font("Arial", 18)); // Modifier la police d'écriture
            recettePane.getChildren().add(nomRecetteLabel);

            Label ingredientsLabel = new Label("Ingrédients : " + r.getIngredients());
            ingredientsLabel.setLayoutX(10);
            ingredientsLabel.setLayoutY(40);
            ingredientsLabel.setFont(Font.font("Arial", 14));
            recettePane.getChildren().add(ingredientsLabel);

            Label etapesLabel = new Label("Étapes : " + r.getEtapes());
            etapesLabel.setLayoutX(10);
            etapesLabel.setLayoutY(70);
            etapesLabel.setFont(Font.font("Arial", 14));
            recettePane.getChildren().add(etapesLabel);

            // Ajouter un petit espace entre les champs de recette
            Insets insets = new Insets(5); // Marge de 5 pixels
            nomRecetteLabel.setPadding(insets);
            ingredientsLabel.setPadding(insets);
            etapesLabel.setPadding(insets);

            // Ajouter le pane à l'HBox en commençant par la droite
            recettesPane.getChildren().add(0, recettePane);

            // Incrémenter le compteur
            count++;

            // Si le compteur atteint 3, créer une nouvelle ligne
            if (count == 2) {
                recettesPane.getChildren().add(0, new Pane()); // Ajouter un espace entre les lignes
                count = 0; // Réinitialiser le compteur
            }
        }

        // Ajouter un remplissage à droite pour ajuster les recettes encore plus à droite
        recettesPane.setPadding(new Insets(0, 10, 0, 0));
    }
}
