package tn.esprit.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Data;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomeController implements Initializable {

    @FXML
    private ImageView imageViewUser;
    @FXML
    private Label userNameTf;
    @FXML
    private ComboBox comboBoxUser;
    private Stage stage;
    private Scene scene;
    private Parent pt;
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        String imagePath = "file:\\C:\\Users\\user\\Desktop\\SecondProject1\\public\\FrontOffice\\img\\"+Data.user.getImage();

        // Load the image
        Image image = new Image(imagePath);
        // Set the image to the ImageView
        imageViewUser.setImage(image);

        comboBoxUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals("Gérer mon compte")) {
                System.out.println("Modifier profile is selected!");
                // Do something when "Modifier profile" is selected
                showUserSwitch();
            }
            if (newValue != null && newValue.equals("Se déconnecter")) {
                System.out.println("Se deconnecter is selected!");
                Data.user=null;
                clearEmailFileContent();
                Data.currentUserMail="";
                loginSwitch();
            }
        });

        // Ajout d'éléments à la liste déroulante
        comboBoxUser.getItems().addAll( "Gérer mon compte", "Se déconnecter");

        // Set a default value
       userNameTf.setText(Data.user.getFirstname()+" "+Data.user.getLastname());
    }


    private void loginSwitch() {
        try {
            pt = FXMLLoader.load(getClass().getResource("/signIn-view.fxml"));
            stage = new Stage();
            scene = new Scene(pt);
            // Close the current stage
            Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
            currentStage.close();
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignUpController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void showUserSwitch() {
        try {
            pt = FXMLLoader.load(getClass().getResource("/showUser-view.fxml"));
            stage = new Stage();
            scene = new Scene(pt);
            // Close the current stage
            Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
            currentStage.close();
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(WelcomeController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showProductsSwitch(ActionEvent event) {
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


    private void clearEmailFileContent() {
        try {
            // Get the path to the email.txt file in the resources directory
            Path filePath = Paths.get(getClass().getResource("/email.txt").toURI());

            // Write an empty string to the file to clear its content
            Files.write(filePath, "".getBytes(StandardCharsets.UTF_8));

            System.out.println("File content cleared successfully.");
        } catch (IOException e) {
            System.err.println("Error clearing email file content: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
