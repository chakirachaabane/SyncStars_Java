package Controllers;

import Services.ResponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Response;

import java.sql.SQLException;

public class AddResponseController {


    @FXML
    private TextField contenttf;


    @FXML
    void AddResponse(ActionEvent event) throws SQLException {

        ResponseService ServiceRes = new ResponseService();

        // Récupérer les valeurs des champs
        String content = contenttf.getText();

        if (content.isEmpty() ) {
            // Afficher un message d'erreur si un champ est vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {
            // Créer un nouvel Response avec les valeurs des champs
            Response e = new Response(content,4,3);

            // Ajouter Response en utilisant le service
            ServiceRes.add(e);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Response ajouté avec succès!");
            alert.showAndWait();

        }
    }

}