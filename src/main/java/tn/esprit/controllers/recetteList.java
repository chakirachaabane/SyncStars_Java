package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_recette()));
        ingredientsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIngredients()));
        etapesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtapes()));
        problemeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProbleme()));

        // Centre le contenu de chaque cellule dans la TableView

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

        configureTableView(); // Appel de la méthode pour configurer les colonnes du TableView
    }

    private void openRecetteUpdate(recette selectedRecette) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteUpdate.fxml"));
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


    public void getEventList(ActionEvent actionEvent) {
        try {
            // Charger rdvListBack.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }

    private void configureTableView() {
        // Ajustement de la largeur des colonnes
        nomColumn.setPrefWidth(80);
        ingredientsColumn.setPrefWidth(80);
        etapesColumn.setPrefWidth(80);
        problemeColumn.setPrefWidth(80);

        // Centrage du contenu des colonnes
        nomColumn.setStyle("-fx-alignment: CENTER;");
        ingredientsColumn.setStyle("-fx-alignment: CENTER;");
        etapesColumn.setStyle("-fx-alignment: CENTER;");
        problemeColumn.setStyle("-fx-alignment: CENTER;");
    }

    public void AllerVersFormulaire(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteAdd.fxml"));
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


    public void getCatList(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategorieEve.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Close the current stage
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void ConsulterRDVButtonAction(ActionEvent actionEvent) {
        try {
            // Charger rdvListBack.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvListBack.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }

    @FXML
    public void handleConsulterListeRecettesButtonAction(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteList.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAjouterRecettesButtonAction(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteAdd.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getFormatsList(ActionEvent event) {

        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormat.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Close the current stage
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void handleListeExerciceButtonAction(ActionEvent event) {


        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getExercice.fxml"));
            Parent root = loader.load();

            // Create a new stage

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Pass nouvelEntrainement to the loaded controller
            getExercice controller = loader.getController();
            controller.setIdEntrainemnt(-1);


            // Close the current stage
            currentStage.close();

            // Show the new stage
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void EntrainementPath(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Close the current stage
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






    @FXML
    public void allerVersUser(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayUsers-view.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }

    @FXML
    public void allerAjouterUser(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addUserAdmin-view.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }


    @FXML
    public void allerVersProduits(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayProduit.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }


    @FXML
    public void allerAjouterProduits(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addProduit.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }

    @FXML
    public void allerListCat(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayCategorie.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }

    @FXML
    public void allerAjouterCat(ActionEvent actionEvent) {
        try {
            // Charger recetteList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCategorie.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }



}
