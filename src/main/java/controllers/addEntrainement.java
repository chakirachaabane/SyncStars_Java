package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entrainements;
import services.EntrainementServices;
import java.io.IOException;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class addEntrainement {

    private final EntrainementServices entrainementServices = new EntrainementServices();

    @FXML
    private TextField niveauTF;

    @FXML
    private TextField objectifTF;

    @FXML
    private TextField dureeTF;

    @FXML
    private TextField nomEntrainementTF;

    @FXML
    private TextField periodeTF;

    @FXML
    void listEntrainement(ActionEvent event) {

        try {
            Stage stage = (Stage) niveauTF.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            System.out.println("Entraînement ajouté avec succès !");
        } catch (Exception e) {
            showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Période.");
        }

    }

    @FXML
    void addEntrainement(ActionEvent event) {
        // Validation des champs obligatoires
        if (nomEntrainementTF.getText().isEmpty()) {
            showAlert("Champ obligatoire", "Le nom de l'entraînement est requis.");
            return;
        }

        // Validation des champs numériques
        try {
            int duree = Integer.parseInt(dureeTF.getText());
            int periode = Integer.parseInt(periodeTF.getText());

            // Création d'un nouvel entraînement avec les données saisies
            entrainements nouvelEntrainement = new entrainements(
                    0, // L'id sera attribué automatiquement dans la base de données
                    niveauTF.getText(),
                    duree,
                    objectifTF.getText(),
                    nomEntrainementTF.getText(),
                    periode
            );

            // Ajout de l'entraînement en utilisant le service
            entrainementServices.add(nouvelEntrainement);
            try {


                sendNotificationEmail();  // Send notification email
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Success");
                alert.setContentText("entrainement added successfully!");

                alert.showAndWait();
                changeScene();


            }catch(Exception e){
                System.out.println(e.getMessage());
            }




        // Fermeture de la fenêtre actuelle et affichage de la vue getEntrainement.fxml
            Stage stage = (Stage) niveauTF.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            System.out.println("Entraînement ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Période.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'entraînement : " + e.getMessage());
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'entraînement.");
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
    private void sendNotificationEmail() {
        try {
            System.out.println("sendNotificationEmail is being called");
            // SMTP server properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // Sender's email credentials
            String username = "zoghlami.dhirar.10@gmail.com";
            String password =  "badt mwvs cgpd bueg";  // Add your password/ key  here

            javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Receiver's email
            String receiverEmail = "feten.azizi21@gmail.com";

            if (receiverEmail != null) {
                sendEmail(session, username, receiverEmail,"Nouvel entraînement ajouté : ");
                System.out.println("Notification email sent successfully.");
            } else {
                System.out.println("Receiver email not found.");
            }

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(javax.mail.Session session, String from, String to, String content) throws MessagingException {
        System.out.println("sendEmail is being called");
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("New entrainement Notification");
        message.setText(content);
        Transport.send(message);
    }


    private void changeScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
            Parent root = loader.load();

            // Récupérer la référence à la scène depuis la racine chargée par le FXMLLoader
            Scene scene = new Scene(root);

            // Récupérer la référence à la fenêtre depuis la scène
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Fermer la fenêtre actuelle (celle associée à la source de l'événement, si disponible)
            Stage currentStage = (Stage) niveauTF.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

