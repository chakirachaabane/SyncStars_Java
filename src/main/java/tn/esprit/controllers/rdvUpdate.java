package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.rdv;
import tn.esprit.services.RdvService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class rdvUpdate {

    @FXML
    private DatePicker datefield;

    @FXML
    private TextField Horairefield;

    @FXML
    private TextField Problemefield;

    private RdvService rdvService;

    private int rdvId;

    public rdvUpdate() {
        rdvService = new RdvService();
    }

    public void initData(rdv rdv) {
        rdvId = rdv.getId();
        LocalDate date = LocalDate.parse(rdv.getdate_rdv());
        datefield.setValue(date);
        Horairefield.setText(rdv.gethoraire_rdv());
        Problemefield.setText(rdv.getProbleme());
    }

    @FXML
    void updateRdv(ActionEvent event) {
        // Vérifier si la date sélectionnée est valide
        if (!isValidDate(datefield.getValue())) {
            showAlert("Veuillez sélectionner une date valide (postérieure ou égale à la date actuelle).");
            return;
        }

        String horaire = Horairefield.getText();
        String probleme = Problemefield.getText();

        // Vérifier si l'horaire est valide
        if (!isValidHoraire(horaire)) {
            showAlert("Veuillez saisir un horaire valide.");
            return;
        }

        // Vérifier si le problème est valide
        if (!isValidProbleme(probleme)) {
            showAlert("Veuillez sélectionner un problème valide.");
            return;
        }

        // Formatage de la date
        LocalDate localDate = datefield.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = localDate.format(formatter);

        rdv updatedRdv = new rdv(rdvId, date, horaire, probleme);

        try {
            rdvService.updateRdv(updatedRdv);
            showAlert("Rendez-vous mis à jour avec succès !");
        } catch (Exception e) {
            showAlert("Erreur lors de la mise à jour du rendez-vous : " + e.getMessage());
        }
    }

    @FXML
    void goToRdvList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Fxml/rdvList.fxml"));
            Stage stage = (Stage) datefield.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement de la vue
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now());
    }

    private boolean isValidHoraire(String horaire) {
        // Vérifier si l'horaire est entre 08:00 et 11:30 ou entre 14:30 et 17:00
        // Exemple d'implémentation :
        return (horaire.compareTo("08:00") >= 0 && horaire.compareTo("11:30") <= 0) ||
                (horaire.compareTo("14:30") >= 0 && horaire.compareTo("17:00") <= 0);
    }

    private boolean isValidProbleme(String probleme) {
        // Vérifier si le problème est parmi les choix donnés
        return probleme.equals("Obésité") ||
                probleme.equals("Diabète") ||
                probleme.equals("Anorexie") ||
                probleme.equals("Déséquilibre alimentaire") ||
                probleme.equals("Cholestérol");
    }
}
