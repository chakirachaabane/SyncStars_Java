
package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tn.esprit.models.Categorie;
import tn.esprit.models.Category;
import tn.esprit.models.Evenement;
import tn.esprit.services.CategorieService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class addCategorie  {

    private final CategorieService cs = new CategorieService();
    @FXML
    private TextArea descriptionCArea;

    @FXML
    private TextField disponibiliteTF;

    @FXML
    private TextField nomCTF;

    @FXML
    private Text DescriptioncTextError;

    @FXML
    private Text DisponibiliteTextError;

    @FXML
    private Text NomcTextError;

    @FXML
    private Button RetourButoon;



    public addCategorie() {
    }

    @FXML
    void addCategories(ActionEvent event) throws SQLException, IOException {
        boolean isValid = true;
        if (nomCTF.getText().isEmpty()) {
            NomcTextError .setText("Le nom de catégorie est vide");
           NomcTextError.setFill(Color.RED);
            isValid = false;
        } else if (!nomCTF.getText().matches("[\\p{L} -]+")) {
            NomcTextError.setText("Le nom de catégorie doit être de type string ");
            NomcTextError.setFill(Color.RED);
            isValid = false;
        }else if (nomCTF.getText().length() < 3) {
        NomcTextError.setText("Le nom du catégorie doit être d'au moins 3 caractères");
            NomcTextError.setFill(Color.RED);
            isValid = false;
        } else if (nomCTF.getText().length() > 40) {
            NomcTextError.setText("Le nom e catégorie doit être moins que  40 caractères");
            NomcTextError.setFill(Color.RED);
            isValid = false;
        }else {
            NomcTextError.setText("");
        }

        if (descriptionCArea.getText().isEmpty()) {
            DescriptioncTextError.setText("La description de catégorie est vide");
           DescriptioncTextError.setFill(Color.RED);
            isValid = false;
        }else if (descriptionCArea.getText().length() <10) {
            DescriptioncTextError.setText("La description  de catégorie doit être d'au moins 10 caractères");
            DescriptioncTextError.setFill(Color.RED);
            isValid = false;
        } else if (descriptionCArea.getText().length() > 500) {
            DescriptioncTextError.setText("La description de catégorie doit être moins que de 500 caractères");
            DescriptioncTextError.setFill(Color.RED);
            isValid = false;
        } else {
            DescriptioncTextError.setText("");
        }
        if (disponibiliteTF.getText().isEmpty()) {
            DisponibiliteTextError.setText("La disponibilité de catégorie est vide");
            DisponibiliteTextError.setFill(Color.RED);
            isValid = false;
        }

        else if (!disponibiliteTF.getText().isEmpty()) {
            try {
                int disponibilite = Integer.parseInt(disponibiliteTF.getText());
                if (disponibilite < 0) {
                    DisponibiliteTextError.setText("La disponibilité de catégorie doit être un nombre positif");
                    DisponibiliteTextError.setFill(Color.RED);
                    isValid = false;
                } else {
                    DisponibiliteTextError.setText("");
                }
            } catch (NumberFormatException e) {
                DisponibiliteTextError.setText("La disponibilité de catégorie doit être un nombre entier");
                DisponibiliteTextError.setFill(Color.RED);
                isValid = false;
            }}
        else {
            DisponibiliteTextError.setText("");
        }
        if (isValid) {
            String categorieName = nomCTF.getText();
            if (cs.isCategorieExists(categorieName)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une catégorie avec le même nom existe déjà. Veuillez utiliser un nom différent.");
                alert.showAndWait();
                return;
            }
            cs.add(new Categorie(this.nomCTF.getText(), this.descriptionCArea.getText(), Integer.parseInt(this.disponibiliteTF.getText())));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Notifications.create()
                    .title("Succès")
                    .text("Catégorie ajoutée avec succès!")
                    .hideAfter(Duration.seconds(2.5))
                    .position(Pos.BOTTOM_RIGHT)
                    .graphic(new ImageView(new Image("/images/tik1.png")))
                    .owner(currentStage)
                    .onAction(event1 -> {
                        currentStage.close();
                    })
                    .show();
            loadPage("/displayCategorie.fxml", event);
        }
        }

    private  void loadPage(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void RetourDisplay(ActionEvent event) {
        loadPage("/displayCategorie.fxml", event);
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
    public void handleConsulterListeRecettesButtonAction(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteList.fxml"));
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
    void getEventList(ActionEvent event) {
        // Define action to perform when Gestion Evénement button is clicked
    }



}
