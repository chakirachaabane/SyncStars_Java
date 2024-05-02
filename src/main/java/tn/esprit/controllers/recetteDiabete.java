package tn.esprit.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontWeight;
import tn.esprit.models.recette;
import tn.esprit.services.RecetteService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class recetteDiabete {

    @FXML
    private AnchorPane anchorPane;

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
        int recetteCount = 0; // Compteur de recettes sur la ligne actuelle

        // Parcourir toutes les recettes
        for (recette r : recettes) {
            // Créer un nouveau pane avec un cadre vert
            Pane recettePane = new Pane();
            recettePane.setLayoutX(100 + (recetteCount % 2) * 290); // Position X en fonction du compteur de recettes
            recettePane.setLayoutY(y);
            recettePane.setPrefWidth(261);
            recettePane.setPrefHeight(180); // Ajuster la hauteur pour accommoder le bouton PDF
            recettePane.setStyle("-fx-background-color: rgba(105, 191, 167, 0.8);" +
                    "-fx-background-radius: 20;" +
                    "-fx-border-radius: 20;" +
                    "-fx-border-color: #69BFA7;" +
                    "-fx-border-width: 5;");

            // Ajouter le nom de la recette centré en haut du cadre
            javafx.scene.control.Label nomRecetteLabel = new javafx.scene.control.Label(r.getNom_recette());
            nomRecetteLabel.setPrefWidth(261);
            nomRecetteLabel.setPrefHeight(30);
            nomRecetteLabel.setAlignment(javafx.geometry.Pos.CENTER);
            nomRecetteLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            nomRecetteLabel.setFont(javafx.scene.text.Font.font("Arial", FontWeight.BOLD, 16));
            recettePane.getChildren().add(nomRecetteLabel);

            // Ajouter les ingrédients
            javafx.scene.control.Label ingredientsLabel = new javafx.scene.control.Label("Ingrédients : " + r.getIngredients());
            ingredientsLabel.setLayoutY(40);
            ingredientsLabel.setFont(javafx.scene.text.Font.font("Arial", 14));
            recettePane.getChildren().add(ingredientsLabel);

            // Ajouter les étapes
            javafx.scene.control.Label etapesLabel = new javafx.scene.control.Label("Étapes : " + r.getEtapes());
            etapesLabel.setLayoutY(70);
            etapesLabel.setFont(javafx.scene.text.Font.font("Arial", 14));
            recettePane.getChildren().add(etapesLabel);

            // Ajouter le bouton PDF
            javafx.scene.control.Button pdfButton = new javafx.scene.control.Button("PDF");
            pdfButton.setLayoutY(130);
            pdfButton.setPrefWidth(241);
            pdfButton.setPrefHeight(30);
            pdfButton.setStyle("-fx-background-color: #18593B;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 5;");
            pdfButton.setOnAction(event -> genererPDF(r));
            recettePane.getChildren().add(pdfButton);

            // Ajouter le pane à l'anchorPane
            anchorPane.getChildren().add(recettePane);

            // Incrémenter le compteur de recettes sur la ligne actuelle
            recetteCount++;

            // Si deux recettes ont été affichées sur la ligne actuelle, passer à la ligne suivante
            if (recetteCount % 2 == 0) {
                y += 210; // Ajuster la distance entre les lignes des recettes
                recetteCount = 0; // Réinitialiser le compteur pour la nouvelle ligne
            }
        }
    }


    private void genererPDF(recette r) {
        genererPDF(r.getNom_recette(), r.getIngredients(), r.getEtapes());
    }

    public void genererPDF(String nomRecette, String ingredients, String etapes) {
        try {
            // Créer un document PDF
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\HP\\Desktop\\recette_" + nomRecette + ".pdf"));
            document.open();

            // Charger le logo
            Image logo = Image.getInstance("C:\\Users\\HP\\IdeaProjects\\projetpi - Version FRONT +Back + Calendrier + TRI +Recherche\\src\\main\\resources\\img\\logoAlignVibe.png");
            logo.scaleToFit(100, 100); // Redimensionner le logo si nécessaire

            // Ajouter le logo en haut à gauche de la page
            document.add(logo);

            // Ajouter le titre "Un style de vie pour chaque maladie !" en couleur #69BFA7
            // Définir la couleur pour le titre et le message "Bon appétit !" en turquoise
            BaseColor turquoiseColor = new BaseColor(105, 191, 167); // Correspond à #69BFA7
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, turquoiseColor);
            Font bonAppetitFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, turquoiseColor);
            Paragraph titreParagraph = new Paragraph("Un style de vie pour chaque maladie !", titleFont);
            titreParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titreParagraph);
            // Ajouter le nom de la recette
            Paragraph nomRecetteParagraph = new Paragraph("Recette : " + nomRecette);
            nomRecetteParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(nomRecetteParagraph);

            // Ajouter les ingrédients
            Paragraph ingredientsParagraph = new Paragraph("Ingrédients : " + ingredients);
            ingredientsParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(ingredientsParagraph);

            // Ajouter les étapes
            Paragraph etapesParagraph = new Paragraph("Étapes : " + etapes);
            etapesParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(etapesParagraph);

            // Créer une instance de Rectangle pour représenter la bordure
            Rectangle border = new Rectangle(PageSize.A4);

            // Définir les propriétés de la bordure
            border.setBorder(Rectangle.BOX);
            border.setBorderWidth(5);
            border.setBorderColor(new BaseColor(75, 83, 32)); // Couleur vert militaire

            // Ajouter la bordure au document
            document.add(border);

            // Ajouter le message "Bon appétit !" avec un smiley
            Font bonAppetitFont2 = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(105, 191, 167));
            Paragraph bonAppetitParagraph = new Paragraph("Bon appétit ! \u263A", bonAppetitFont2); // Smiley unicode
            bonAppetitParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(bonAppetitParagraph);

            document.close();

            // Afficher un message de confirmation
            showAlert(Alert.AlertType.INFORMATION, "PDF Généré", "Le fichier PDF a été généré avec succès !");
        } catch (DocumentException | IOException e) {
            System.err.println("Erreur lors de la génération du PDF pour la recette : " + nomRecette);
            e.printStackTrace();
            // Afficher une alerte en cas d'erreur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la génération du PDF !");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
