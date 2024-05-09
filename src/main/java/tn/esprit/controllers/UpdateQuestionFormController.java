package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.Question;
import tn.esprit.services.QuestionService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UpdateQuestionFormController {

    private Question questionToUpdate;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private TextField attachmentField;

    @FXML
    private Button saveButton;


    public void setQuestion(Question question) {
        this.questionToUpdate = question;
        if (question != null) {
            titleField.setText(question.getTitle());
            contentArea.setText(question.getContent());
            attachmentField.setText(question.getAttachment());
        }
    }

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> updateQuestion());
    }


    @FXML
    private void updateQuestion() {
        QuestionService QSERVICE = new QuestionService();
        if (questionToUpdate != null && !titleField.getText().isEmpty() && !contentArea.getText().isEmpty()) {
            questionToUpdate.setTitle(titleField.getText()); // Use getText() to get the text from the TextField
            questionToUpdate.setContent(contentArea.getText()); // Use getText() to get the text from the TextField
            questionToUpdate.setAttachment(attachmentField.getText()); // Use getText() to get the text from the TextField
            // Call the update method in the QuestionService
            try {

                QSERVICE.update(questionToUpdate);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Question Modifier avec succès!");
                alert.showAndWait();

                // Get the current stage and close it
                Stage stage = (Stage) titleField.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle any exceptions that occur during the update process
            }
        }
        else{
            // Afficher un message d'erreur si un champ est vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        }
    }

    @FXML
    public void closeTab() {

        // Get the current stage and close it
        Stage stage = (Stage) titleField.getScene().getWindow();
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
            attachmentField.setText(selectedFile.getAbsolutePath());
        }
    }


}

