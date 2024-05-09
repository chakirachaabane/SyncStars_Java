package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.entrainements;
import tn.esprit.services.EntrainementServices;
import java.io.IOException;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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

        // Check all fields are filled
        if (isEmptyField(nomEntrainementTF) || isEmptyField(niveauTF) || isEmptyField(dureeTF) || isEmptyField(periodeTF)) {
            showAlert("Champ obligatoire", "Tous les champs sont requis.");
            return;
        }

        // Check all fields
        if (!isValidNumber(dureeTF.getText()) || !isValidNumber(periodeTF.getText()) ||!isValidNomEntrainement(nomEntrainementTF.getText()) || !isValidNiveau(niveauTF.getText()) || !isValidObjectif(objectifTF.getText()) ) {
            showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée ou  Période ou nom de l'entrainement ou le niveau ou l'objectif ");
            return;
        }

        // Perform additional validation checks for nomEntrainementTF, niveauTF, and objectifTF
        if (!isValidNomEntrainement(nomEntrainementTF.getText())) {
            showAlert("Erreur de saisie", "Le nom de l'entraînement n'est pas valide.");
            return;
        }

        if (!isValidNiveau(niveauTF.getText())) {
            showAlert("Erreur de saisie", "Le niveau n'est pas valide.");
            return;
        }

        if (!isValidObjectif(objectifTF.getText())) {
            showAlert("Erreur de saisie", "L'objectif n'est pas valide.");
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
    private void sendNotificationEmail(entrainements nouvelEntrainement) {
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

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Receiver's email
            String receiverEmail = "feten.azizi21@gmail.com";

            if (receiverEmail != null) {
                sendEmail(session, username, receiverEmail,"Nouvel entraînement ajouté : " + nouvelEntrainement.getNom_entrainement());
                System.out.println("Notification email sent successfully.");
            } else {
                System.out.println("Receiver email not found.");
            }

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(Session session, String from, String to, String content) throws MessagingException {
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

    private boolean isEmptyField(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private boolean isValidNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidNomEntrainement(String text) {
        // Add your validation logic for the nomEntrainementTF field
        // Return true if the text is valid, otherwise return false
        // Example validation: The text should contain only letters and spaces
        return text.matches("[a-zA-Z ]+");
    }

    private boolean isValidNiveau(String text) {
        // Add your validation logic for the niveauTF field
        // Return true if the text is valid, otherwise return false
        // Example validation: The text should be one of the predefined levels (e.g., beginner, intermediate, advanced)
        List<String> validLevels = Arrays.asList("avancée", "debutant", "intermediaire");
        return validLevels.contains(text.toLowerCase());
    }

    private boolean isValidObjectif(String text) {
        // Add your validation logic for the niveauTF field
        // Return true if the text is valid, otherwise return false
        // Example validation: The text should be one of the predefined levels (e.g., beginner, intermediate, advanced)
        List<String> validLevels = Arrays.asList("se muscler", "perte de poids");
        return validLevels.contains(text.toLowerCase());
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

    public void getEventList(ActionEvent actionEvent) {
        try {
            // Charger rdvListBack.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
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

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Pass nouvelEntrainement to the loaded controller
            getExercice controller = loader.getController();
            controller.setIdEntrainemnt(-1);


            // Close the current stage
            currentStage.close();

            // Show the new stage
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void EntrainementPath(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
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


}

