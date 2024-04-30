package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class rdvAcc {

    @FXML
    private Button prendreRdvButton;

    @FXML
    private Button ConsulterRdvButton;

    @FXML
    private Button EspaceRecetteButton;

    @FXML
    public void initialize() {
        // Ajout d'un gestionnaire d'événements au bouton "Prendre un RDV"
        prendreRdvButton.setOnAction(event -> {
            // Charger rdvAdd.fxml
            loadFXML("/Fxml/rdvAdd.fxml");
        });

        // Ajout d'un gestionnaire d'événements au bouton "Consulter liste RDV"
        ConsulterRdvButton.setOnAction(event -> {
            // Charger rdvList.fxml
            loadFXML("/Fxml/rdvList.fxml");
        });

        // Ajout d'un gestionnaire d'événements au bouton "Consulter Espace Recette"
        EspaceRecetteButton.setOnAction(event -> {
            // Charger recetteAcc.fxml
            loadFXML("/Fxml/recetteAcc.fxml");
        });
    }

    private void loadFXML(String fxmlFilePath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
