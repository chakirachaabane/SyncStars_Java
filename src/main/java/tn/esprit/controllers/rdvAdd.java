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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class rdvAdd {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String ACCOUNT_SID = dotenv.get("ACCOUNT_SID");
    private static final String AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

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
            sendSMS();
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

    private void sendSMS() {
        // Remplacer les valeurs suivantes par votre numéro Twilio et le numéro de téléphone de destination
        String twilioNumber = "+13342922542"; // Votre numéro Twilio
        String recipientNumber = "+21696348060"; // Numéro de téléphone du destinataire

        // Message à envoyer
        String messageBody = "Rendez-vous chez AlignVibe confirmé !";

        // Envoyer le message SMS
        Message message = Message.creator(
                new PhoneNumber(recipientNumber),
                new PhoneNumber(twilioNumber),
                messageBody
        ).create();

        System.out.println("Message SID: " + message.getSid());
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
    private void goToRdvList(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/rdvList.fxml"));
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
}
