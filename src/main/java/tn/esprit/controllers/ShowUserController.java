package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.models.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowUserController implements Initializable {

    @FXML
    private Label firstNameTf;
    @FXML
    private Label lastNameTf;
    @FXML
    private Label emailTf;
    @FXML
    private Label dobTf;
    @FXML
    private Label telephoneTf;
    @FXML
    private Label addressTf;
    @FXML
    private ImageView imageViewUser;

    private Stage stage;
    private Scene scene;
    private Parent pt;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameTf.setText(Data.user.getFirstname());
        lastNameTf.setText(Data.user.getLastname());
        emailTf.setText(Data.user.getEmail());
        dobTf.setText(String.valueOf(Data.user.getBirth_date()));
        telephoneTf.setText(String.valueOf(Data.user.getPhone_number()));
        addressTf.setText(Data.user.getAddress());

        String imagePath = "file:\\C:\\Users\\user\\Desktop\\SecondProject1\\public\\FrontOffice\\img\\"+Data.user.getImage();
        // Load the image
        Image image = new Image(imagePath);
        // Set the image to the ImageView
        imageViewUser.setImage(image);

    }

    @FXML
    private void modifyUserClicked(ActionEvent event) {

        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/modifyUser-view.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modifier compte");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ShowUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void deleteUserClicked(ActionEvent event) {


// Inside your method where you want to show the confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer suppression");
        alert.setHeaderText("Supprimer Votre compte");
        alert.setContentText("Vous êtes sûr?");

// Add OK and Cancel buttons to the alert dialog
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

// Show the alert and wait for the user's response
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

// Check the user's response
        if (result == ButtonType.OK) {
            // User clicked OK, proceed with deletion
            UserService.deleteUser(Data.user.getId());
            Data.user = null;
            loginSwitch(event);
        } else {
            // User clicked Cancel or closed the dialog, do nothing or handle accordingly
        }


    }
    private void loginSwitch(ActionEvent event) {
        try {
            pt = FXMLLoader.load(getClass().getResource("/signIn-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pt);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignUpController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void returnClicked(ActionEvent event) {
        try {
            pt = FXMLLoader
                    .load(getClass().getResource("/welcome-view.fxml"));

            scene = new Scene(pt);
            stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Welcome");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignInController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }



}
