package tn.esprit.controllers;

import tn.esprit.models.Data;
import tn.esprit.models.Question;
import tn.esprit.services.QuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.controllers.ForumPageController;

import java.io.File;
import java.io.IOException;
import java.security.Provider;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddQuestionController {

    ForumPageController EFS = new ForumPageController();

    @FXML
    private TextField titretf;

    @FXML
    private TextField contenttf;

    @FXML
    private TextField attachmenttf;

    @FXML
    void AddQuestion(ActionEvent event) throws SQLException {

        QuestionService ServiceQue = new QuestionService();

        // Récupérer les valeurs des champs
        String titre = titretf.getText();
        String content = contenttf.getText();
        String attachment = attachmenttf.getText();

        if (titre.isEmpty() || content.isEmpty() ) {
            // Afficher un message d'erreur si un champ est vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {
            // Créer un nouvel Evenement avec les valeurs des champs
            Question e = new Question(titre, Data.user.getId(),content,attachment);

            // Ajouter l'Evenement en utilisant le service
            ServiceQue.add(e);
            EFS.sendNotificationEmail(e);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Question ajouté avec succès!");
            alert.showAndWait();
            // Get the current stage and close it
            Stage stage = (Stage) titretf.getScene().getWindow();
            stage.close();
            System.out.println("Question Ajouté Avec Succées");

        }
    }

    @FXML
    public void closeTab() {

        // Get the current stage and close it
        Stage stage = (Stage) titretf.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleChooseFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        // Set initial directory (optional)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Filter file types if needed (optional)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            attachmenttf.setText(selectedFile.getAbsolutePath());
        }
    }


}