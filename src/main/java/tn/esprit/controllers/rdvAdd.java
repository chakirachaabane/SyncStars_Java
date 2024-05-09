package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.esprit.models.rdv;
import tn.esprit.services.RdvService;
import com.twilio.rest.api.v2010.account.Message;
import io.github.cdimascio.dotenv.Dotenv;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class rdvAdd {

//    private static final Dotenv dotenv = Dotenv.load();
//    private static final String ACCOUNT_SID = dotenv.get("ACCOUNT_SID");
//    private static final String AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
//    static {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//    }

    @FXML
    private DatePicker datefield;

    @FXML
    private TextField Horairefield;

    @FXML
    private ComboBox<String> problemeComboBox;


    @FXML
    private Label dateErrorLabel;

    @FXML
    private Label horaireErrorLabel;

    @FXML
    private Label successLabel;

    private RdvService rdvService = new RdvService();

    public void addRdv(ActionEvent event) {
        // Réinitialiser les messages d'erreur
        dateErrorLabel.setText("");
        horaireErrorLabel.setText("");

        // Vérifier si la date est sélectionnée et postérieure à la date actuelle
        LocalDate selectedDate = datefield.getValue();
        if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
            dateErrorLabel.setTextFill(Color.RED);
            dateErrorLabel.setText("Veuillez sélectionner une date valide.");
            return;
        }

        // Vérifier le format de l'horaire et s'il est valide
        String horaire = Horairefield.getText().trim();
        if (!isHoraireValide(horaire)) {
            horaireErrorLabel.setTextFill(Color.RED);
            horaireErrorLabel.setText("Veuillez saisir un horaire valide (08:00 - 11:30 ou 14:30 - 17:00).");
            return;
        }

        // Vérifier si le champ problème est rempli
        String probleme = problemeComboBox.getValue();
        if (probleme == null || probleme.isEmpty()) {
            horaireErrorLabel.setTextFill(Color.RED);
            horaireErrorLabel.setText("Veuillez sélectionner le problème.");
            return;
        }

        // Vérifier si le rendez-vous existe déjà pour la date et l'horaire donnés
        if (rdvService.isRdvExist(selectedDate, horaire)) {
            dateErrorLabel.setTextFill(Color.RED);
            dateErrorLabel.setText("Ce rendez-vous est déjà pris. Veuillez modifier la date ou l'horaire.");
            return;
        }

        // Créer un nouvel objet rdv avec les informations saisies
        rdv newRdv = new rdv(selectedDate.toString(), horaire, probleme);

        // Ajouter le rendez-vous à la base de données
        boolean ajoutReussi = rdvService.addRdv(newRdv);

        // Si l'ajout est réussi, afficher le message de succès et réinitialiser les champs
        if (ajoutReussi) {
            successLabel.setTextFill(Color.GREEN);
            successLabel.setText("Rendez-vous ajouté avec succès !");

            // Réinitialiser les champs
            datefield.setValue(null);
            Horairefield.clear();
            problemeComboBox.getSelectionModel().clearSelection();
        } else {
            // Si l'ajout a échoué, afficher un message d'erreur
            successLabel.setTextFill(Color.RED);
            successLabel.setText("Erreur lors de l'ajout du rendez-vous. Veuillez réessayer.");
        }
    }





    // Vérifier si l'horaire est valide
    private boolean isHoraireValide(String horaire) {
        if (horaire.isEmpty()) {
            return false;
        }

        // Définir les plages horaires valides
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return horaire.matches("(0[8-9]|1[0-1]):[0-5][0-9]") || horaire.matches("1[4-6]:[0-5][0-9]");
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
    private void goToRdvList(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvList.fxml"));
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





}
