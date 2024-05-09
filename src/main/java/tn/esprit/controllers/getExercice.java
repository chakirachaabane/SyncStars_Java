package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.models.exercices;
import tn.esprit.services.ExercicesServices;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.SQLException;

public class getExercice {
    @FXML
    private Button ajoutexercice;

    @FXML
    private TableView<exercices> tableExercices;
    private final ExercicesServices exercicesServices = new ExercicesServices();

    public int getIdEntrainemnt() {
        return idEntrainemnt;
    }

    public void setIdEntrainemnt(int idEntrainemnt) {
        this.idEntrainemnt = idEntrainemnt;
        init();
    }

    private int idEntrainemnt ;


    @FXML
    public void initialize() {}
    public void init(){
        TableColumn<exercices, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<exercices, String> nomExerciceColumn = new TableColumn<>("Nom de l'exercice");
        nomExerciceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_exercice()));

        TableColumn<exercices, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        TableColumn<exercices, Integer> dureeColumn = new TableColumn<>("Durée");
        dureeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuree()).asObject());

        TableColumn<exercices, Integer> nombresDeFoisColumn = new TableColumn<>("Nombre de fois");
        nombresDeFoisColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNombres_de_fois()).asObject());

        TableColumn<exercices, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setOnAction(event -> handleModifier(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleSupprimer(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(editButton, deleteButton);
                    container.setSpacing(10);
                    setGraphic(container);
                }
            }
        });

        tableExercices.getColumns().addAll(idColumn, nomExerciceColumn, typeColumn, dureeColumn, nombresDeFoisColumn, actionCol);
        //System.out.println(idEntrainemnt);
      //  System.out.println("   erer er ");
        loadExercices(idEntrainemnt);
    }

    private void loadExercices(int id) {
        try {
            if (id ==-1){
                tableExercices.getItems().addAll(exercicesServices.getAll());
            }else
            {
            tableExercices.getItems().addAll(exercicesServices.getByIdEntrainemnt(id));}
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des exercices : " + e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }

    private void handleModifier(exercices exercice) {
        try {
            // Close current window
            Stage stage = (Stage) tableExercices.getScene().getWindow();
            stage.close();

            // Load getEntrainement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierExercice.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Pass nouvelEntrainement to the loaded controller
            modifExerciceController controller = loader.getController();
            controller.initData(exercice);
            controller.setIdEntrainement(idEntrainemnt);

            // Rafraîchir les données ou mettre à jour l'interface utilisateur si nécessaire
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }

    private void handleSupprimer(exercices exercice) {
        try {
            exercicesServices.delete(exercice.getId());
            tableExercices.getItems().remove(exercice);  // Mettre à jour l'interface utilisateur
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }
    @FXML
    public void addexercice(ActionEvent actionEvent) {
        try{  Stage stage = (Stage) ajoutexercice.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addExercice.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
            addExercice controller = loader.getController();
            controller.setIdEntrainement(this.idEntrainemnt);

            System.out.println("Entraînement ajouté avec succès !");
        } catch (Exception e) {
           // showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Période.");
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
    public void handleConsulterListeRecettesButtonAction(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteList.fxml"));
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

    @FXML
    private void EntrainementPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addEntrainement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
