package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.rdv;
import tn.esprit.services.RdvService;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Parent root = FXMLLoader.load(getClass().getResource("/rdvList.fxml"));
            Stage stage = (Stage) datefield.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement de la vue
        }
    }


    @FXML
    private void EventPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void RDVPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAcc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void EntrainementPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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



    @FXML
    private void displayEventSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void displayRdvSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAcc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void displayEntSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowForumSwitch1(ActionEvent event) {
        try {
            /*Parent root = FXMLLoader.load(getClass().getResource("/updateQuestionForm.fxml"));
            // Get the controller for the update form
            UpdateQuestionFormController updateController = new UpdateQuestionFormController();*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main_Forum_Page.fxml"));
            Parent root = loader.load();
            ForumPageController controller = loader.getController();


            Stage stage = new Stage();
            stage.setTitle("Forum Page");
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }











    @FXML
    public void showProductsSwitch(ActionEvent event) {
        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/displayFrontProduit.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Produits");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showAccueilSwitch(ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent pt;
        PreparedStatement pst;
        try {
            pt = FXMLLoader.load(getClass().getResource("/welcome-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pt);
            stage.setScene(scene);
            stage.setTitle("Bienvenue!");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignInController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }





}
