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
import model.entrainements;
import services.EntrainementServices;

import java.sql.SQLException;

public class modifEntrainementController {

    @FXML
    private Label idLabel;

    @FXML
    private TextField niveauTF;

    @FXML
    private TextField dureeTF;

    @FXML
    private TextField objectifTF;

    @FXML
    private TextField nomEntrainementTF;

    @FXML
    private TextField periodeTF;

    private entrainements entrainementToUpdate;

    private final EntrainementServices entrainementServices = new EntrainementServices();


    public void initData(entrainements entrainement) {
        this.entrainementToUpdate = entrainement;
        System.out.println(entrainement.toString());

       idLabel.setText(String.valueOf(entrainement.getId()));

        niveauTF.setText(entrainement.getNiveau());
        dureeTF.setText(String.valueOf(entrainement.getDuree()));
        objectifTF.setText(entrainement.getObjectif());
        nomEntrainementTF.setText(entrainement.getNom_entrainement());
        periodeTF.setText(String.valueOf(entrainement.getPeriode()));
    }

    @FXML
    private void modifEntrainement() {
        // Implement update logic using entrainementToUpdate...
        // Close the current stage after updaupting
        try {
            entrainementToUpdate.setNiveau(niveauTF.getText());
            entrainementToUpdate.setDuree(Integer.parseInt(dureeTF.getText()));
            entrainementToUpdate.setObjectif(objectifTF.getText());
            entrainementToUpdate.setNom_entrainement(nomEntrainementTF.getText());
            entrainementToUpdate.setPeriode(Integer.parseInt(periodeTF.getText()));

            entrainementServices.update(entrainementToUpdate);

                 } catch (SQLException e) {
            System.out.println("Erreur lors de la modif : " + e.getMessage());
            // Gérer l'erreur selon vos besoins
        }

    }

    @FXML
    void listEntrainement(ActionEvent event) {

        try{  Stage stage = (Stage) niveauTF.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            System.out.println("Entraînement ajouté avec succès !");
        } catch (Exception e) {
            showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Période.");
        }

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
