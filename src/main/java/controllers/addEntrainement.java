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


                sendNotificationEmail(nouvelEntrainement);  // Send notification email
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Thread added successfully!");

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


    private void sendNotificationEmail(entrainements entrainement) throws MessagingException {
        // SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");  // Updated to use port 587

        // Sender's email credentials
        String username = "feten.azizi@esprit.tn";
        String password = "211JFT5051";  // Change to your actual password

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Receiver's email
        String receiverEmail = "feten.azizi@esprit.tn";

        if (receiverEmail != null) {
            sendEmail(session, username, receiverEmail, createEmailContent(entrainement));
            System.out.println("Notification email sent successfully.");
        } else {
            System.out.println("Receiver email not found.");
        }

    }

    private String createEmailContent(entrainements entrainement) {


        try {

            EntrainementServices es = new EntrainementServices(); // Création d'une instance de EntrainementServices
            es.add(entrainement); // Ajout de l'entraînement
            //sendNotificationEmail(entrainement);  // Envoyer une notification par e-mail
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Training session added successfully!");

            alert.showAndWait();
            changeScene();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "New training session added: " + entrainement.getNom_entrainement() +
                "\nLevel: " + entrainement.getNiveau() +
                "\nDuration: " + entrainement.getDuree() + " minutes" +
                "\nObjective: " + entrainement.getObjectif() +
                "\nPeriod: " + entrainement.getPeriode() + " days";
    }


    private void sendEmail(Session ignoredSession, String receiverEmail, String subject, String content) {
        try {
            // Configuration de la session SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // Adresse email et mot de passe de l'expéditeur
            String username = "votre_adresse_email@gmail.com";
            String password = "votre_mot_de_passe";

            // Création d'une session avec authentification
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Création du message email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject(subject);
            message.setText(content);

            // Envoi du message
            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + receiverEmail);
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
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

