package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.exercices;
import services.ExercicesServices;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

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
        loadExercices(idEntrainemnt);
    }

    private void loadExercices(int id) {
        try {
            tableExercices.getItems().addAll(exercicesServices.getByIdEntrainemnt(id));
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
}
