package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.models.recette;
import tn.esprit.services.RecetteService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class recetteList {

    @FXML
    private TableView<recette> tableViewRecettes;

    @FXML
    private TableColumn<recette, Integer> idColumn;

    @FXML
    private TableColumn<recette, String> nomColumn;

    @FXML
    private TableColumn<recette, String> ingredientsColumn;

    @FXML
    private TableColumn<recette, String> etapesColumn;

    @FXML
    private TableColumn<recette, String> problemeColumn;

    @FXML
    private TableColumn<recette, Void> actionsColumn;

    @FXML
    private TextField searchTextField;

    private RecetteService recetteService;

    public recetteList() {
        recetteService = new RecetteService();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_recette()));
        ingredientsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIngredients()));
        etapesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtapes()));
        problemeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProbleme()));

        // Centre le contenu de chaque cellule dans la TableView
        idColumn.setCellFactory(column -> {
            return new TableCell<recette, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item));
                        setAlignment(Pos.CENTER);
                    }
                }
            };
        });

        // Ajout des boutons Modifier et Supprimer
        actionsColumn.setCellFactory(param -> new TableCell<recette, Void>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setStyle("-fx-background-color: #B360ACFF; -fx-text-fill: white; -fx-padding: 5px 10px;");
                deleteButton.setStyle("-fx-background-color: #69BFA7FF; -fx-text-fill: white; -fx-padding: 5px 10px;");

                editButton.setOnAction(event -> {
                    recette selectedRecette = getTableView().getItems().get(getIndex());
                    openRecetteUpdate(selectedRecette);
                });

                deleteButton.setOnAction(event -> {
                    recette selectedRecette = getTableView().getItems().get(getIndex());
                    // Logique pour supprimer la recette
                    recetteService.deleteRecette(selectedRecette); // Supprimer la recette de la base de données
                    tableViewRecettes.getItems().remove(selectedRecette); // Supprimer la recette de la TableView
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(editButton, deleteButton));
                }
            }
        });


        try {
            List<recette> recettes = recetteService.getAllRecettes();
            tableViewRecettes.getItems().addAll(recettes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openRecetteUpdate(recette selectedRecette) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteUpdate.fxml"));
            Parent root = loader.load();

            recetteUpdate controller = loader.getController();
            controller.initData(selectedRecette);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AllerVersFormulaire(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteAdd.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void searchRecette(ActionEvent actionEvent) {
        String searchQuery = searchTextField.getText().trim().toLowerCase();

        // Liste pour stocker les recettes trouvées
        List<recette> foundRecettes = new ArrayList<>();

        // Faire la recherche dans la table des recettes
        for (recette recette : tableViewRecettes.getItems()) {
            if (recette.getNom_recette().toLowerCase().contains(searchQuery)) {
                foundRecettes.add(recette);
            }
        }

        // Effacer la liste actuelle de la TableView
        tableViewRecettes.getItems().clear();

        // Si des recettes sont trouvées, les ajouter à la TableView, sinon afficher un message
        if (!foundRecettes.isEmpty()) {
            tableViewRecettes.getItems().addAll(foundRecettes);
        } else {
            // Afficher un message dans la TableView
            tableViewRecettes.setPlaceholder(new Label("Aucune recette comportant ce nom."));
        }
    }

}
