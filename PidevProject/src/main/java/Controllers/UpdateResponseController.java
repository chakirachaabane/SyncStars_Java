package Controllers;

import Services.ResponseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Response;

import java.sql.SQLException;


public class UpdateResponseController {
    private Response responsetoUpdate;

    @FXML
    private TextField Response_update;

    @FXML
    Button update_button;

    @FXML
    Button Annuler_button;

    public void setResponse(Response response) {
        this.responsetoUpdate = response;
        if (response != null) {
            Response_update.setText(response.getContent());
        }
    }

    @FXML
    private void updateResponse() {
        ResponseService RSERVICE = new ResponseService();
        if (responsetoUpdate != null && !Response_update.getText().isEmpty()) {
            responsetoUpdate.setContent(Response_update.getText()); // Use getText() to get the text from the TextField
            // Call the update method in the QuestionService
            try {

                RSERVICE.update(responsetoUpdate);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Response Modifier avec succès!");
                alert.showAndWait();

                // Get the current stage and close it
                Stage stage = (Stage) Response_update.getScene().getWindow();
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
        Stage stage = (Stage) Response_update.getScene().getWindow();
        stage.close();
    }
}
