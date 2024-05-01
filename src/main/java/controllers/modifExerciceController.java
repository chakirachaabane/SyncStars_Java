package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.exercices;
import services.ExercicesServices;

import java.sql.SQLException;

public class modifExerciceController {

    @FXML
    private Label idLabel;

    @FXML
    private TextField nomExerciceTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField dureeTF;

    @FXML
    private TextField nombresDeFoisTF;

    private exercices exerciceToUpdate;

    public int getIdEntrainement() {
        return idEntrainement;
    }

    public void setIdEntrainement(int idEntrainement) {
        this.idEntrainement = idEntrainement;
    }

    int idEntrainement;

    private final ExercicesServices exerciceServices = new ExercicesServices();

    public void initData(exercices exercice) {
        this.exerciceToUpdate = exercice;

        idLabel.setText(String.valueOf(exercice.getId()));
        nomExerciceTF.setText(exercice.getNom_exercice());
        typeTF.setText(exercice.getType());
        dureeTF.setText(String.valueOf(exercice.getDuree()));
        nombresDeFoisTF.setText(String.valueOf(exercice.getNombres_de_fois()));
    }

    @FXML
    private void modifExercice() {
        try {
            exerciceToUpdate.setNom_exercice(nomExerciceTF.getText());
            exerciceToUpdate.setType(typeTF.getText());
            exerciceToUpdate.setDuree(Integer.parseInt(dureeTF.getText()));
            exerciceToUpdate.setNombres_de_fois(Integer.parseInt(nombresDeFoisTF.getText()));

            exerciceServices.update(exerciceToUpdate);

            // Fermeture de la fenêtre actuelle
            Stage stage = (Stage) nomExerciceTF.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getExercice.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Pass nouvelEntrainement to the loaded controller
            getExercice controller = loader.getController();
            controller.setIdEntrainemnt(idEntrainement);
            closeStage();
            System.out.println("Exercice modifié avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }
    @FXML
    void listExercices(ActionEvent event) {
        try {
            Stage stage = (Stage) nomExerciceTF.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewExercices.fxml")); // Assurez-vous que le chemin est correct
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            System.out.println("Liste des exercices affichée avec succès !");
        } catch (Exception e) {
            showAlert("Erreur lors du chargement de la liste des exercices", "Impossible de charger la vue des exercices.");
        }
    }

    private void closeStage() {
        Stage stage = (Stage) nomExerciceTF.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

