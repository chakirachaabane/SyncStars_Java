package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.exercices;
import services.ExercicesServices;

import java.sql.SQLException;


public class addExercice {

    public int getIdEntrainement() {
        return idEntrainement;
    }

    public void setIdEntrainement(int idEntrainement) {
        this.idEntrainement = idEntrainement;
    }

    int idEntrainement ;
    private final ExercicesServices exercicesServices = new ExercicesServices();

    @FXML
    private TextField nomExerciceTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField dureeTF;

    @FXML
    private TextField nombresfoisTF;

    @FXML
    void addExercice(ActionEvent event) {
        // Validation des champs obligatoires
        if (nomExerciceTF.getText().isEmpty() || typeTF.getText().isEmpty() || dureeTF.getText().isEmpty() || nombresfoisTF.getText().isEmpty()) {
            showAlert("Champ obligatoire", "Tous les champs doivent être remplis.");
            return;
        }

        // Validation des champs numériques
        try {
            int duree = Integer.parseInt(dureeTF.getText());
            int nombresfois = Integer.parseInt(nombresfoisTF.getText());

            // Création d'un nouvel exercice avec les données saisies
            exercices nouvelExercice = new exercices(
                    nomExerciceTF.getText(),
                    typeTF.getText(),
                    duree,
                    nombresfois
            );

            // Ajout de l'exercice en utilisant le service
            exercicesServices.addExercice(nouvelExercice,idEntrainement);

            // Affichage d'une confirmation à l'utilisateur
            showAlert("Exercice ajouté", "L'exercice a été ajouté avec succès.");

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
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Nombre de fois.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'exercice : " + e.getMessage());
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'exercice.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            showAlert("Erreur", "Une erreur inattendue s'est produite.");
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
