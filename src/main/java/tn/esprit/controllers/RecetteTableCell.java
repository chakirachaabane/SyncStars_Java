package tn.esprit.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import tn.esprit.models.recette;

public class RecetteTableCell extends TableCell<recette, recette> {

    private final VBox content;

    public RecetteTableCell() {
        content = new VBox();
        content.setSpacing(5);
        Label nomLabel = new Label();
        Label ingredientsLabel = new Label();
        Label etapesLabel = new Label();
        Label problemeLabel = new Label();
        content.getChildren().addAll(nomLabel, ingredientsLabel, etapesLabel, problemeLabel);
    }

    @Override
    protected void updateItem(recette item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            ((Label) content.getChildren().get(0)).setText("Nom: " + item.getNom_recette());
            ((Label) content.getChildren().get(1)).setText("Ingredients: " + item.getIngredients());
            ((Label) content.getChildren().get(2)).setText("Etapes: " + item.getEtapes());
            ((Label) content.getChildren().get(3)).setText("Problème: " + item.getProbleme());
            setGraphic(content);
        }
    }
}
