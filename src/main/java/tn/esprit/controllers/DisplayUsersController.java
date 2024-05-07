package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.models.Data;
import tn.esprit.models.User;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DisplayUsersController implements Initializable {

    @FXML
    private TableView<User> usersTab;
    @FXML
    private TableColumn<?, ?> cinColumn;
    @FXML
    private TableColumn<?, ?> fNameColumn;
    @FXML
    private TableColumn<?, ?> lNameColumn;
    @FXML
    private TableColumn<?, ?> emailColumn;
    @FXML
    private TableColumn<?, ?> dobColumn;
    @FXML
    private TableColumn<?, ?> roleColumn;


    @FXML
    private ImageView imageViewUser;
    @FXML
    private Label userNameTf;
    @FXML
    private ComboBox comboBoxUser;

    @FXML
    private TextField searchTf;
    private Stage stage;
    private Scene scene;
    private Parent pt;

    private ArrayList<User> usersList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showUsers();

        //Déselection btn supprimer

        usersTab.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

        });


        String imagePath = "file:\\C:\\Users\\Nawres\\Desktop\\SecondProject1\\public\\FrontOffice\\img\\"+ Data.user.getImage();
        // Load the image
        Image image = new Image(imagePath);
        // Set the image to the ImageView
        imageViewUser.setImage(image);

        // Ajout d'éléments à la liste déroulante
        comboBoxUser.getItems().addAll( "Gérer mon compte","Se déconnecter");

        comboBoxUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals("Gérer mon compte")) {
                showUserAdminSwitch();
                System.out.println("Modifier profile is selected!");
            }

            if (newValue != null && newValue.equals("Se déconnecter")) {
                System.out.println("Se deconnecter is selected!");
                Data.user=null;
                //Session
                clearEmailFileContent();
                Data.currentUserMail="";

                loginSwitch();
            }
        });


        // Set a default value
        userNameTf.setText(Data.user.getFirstname()+" "+Data.user.getLastname());

    }


    public void showUsers() {

        if (searchTf.getText().isEmpty()) {
            usersList = UserService.displayUsers();
        }else{
            usersList = (ArrayList<User>) UserService.displayUsers().stream().filter(user->user.getFirstname().toLowerCase().contains(searchTf.getText().toLowerCase()) || user.getLastname().toLowerCase().contains(searchTf.getText().toLowerCase()) || (user.getFirstname()+" "+ user.getLastname()).toLowerCase().contains(searchTf.getText().toLowerCase())).collect(Collectors.toList());
        }
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        fNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("birth_date"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        if (usersTab != null && usersTab instanceof TableView) {
            // Cast usersTab to TableView<users> and set its items
            ((TableView<User>) usersTab).setItems(FXCollections.observableArrayList(usersList));
        }
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

    private void showUserAdminSwitch() {
        try {
            pt = FXMLLoader.load(getClass().getResource("/showUserAdmin-view.fxml"));
            stage = new Stage();
            scene = new Scene(pt);
            // Close the current stage
            Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
            currentStage.close();
            stage.setTitle("User infos");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(ShowUserAdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void addAdminSwitch() {
        try {
            pt = FXMLLoader.load(getClass().getResource("/addUserAdmin-view.fxml"));
            stage = new Stage();
            scene = new Scene(pt);
            // Close the current stage
            Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
            currentStage.close();
            stage.setTitle("Ajouter un admin");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(AddUserAdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void statisticsSwitch() {
        try {
            pt = FXMLLoader.load(getClass().getResource("/usersStatistics-view.fxml"));
            stage = new Stage();
            scene = new Scene(pt);
            // Close the current stage
            Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
            currentStage.close();
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(AddUserAdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void dashboardUserSwitch(ActionEvent event) {
        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/displayUsers-view.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Tableau de bord");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void addProductSwitch(ActionEvent event) {
        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/addProduit.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter un produit");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void listProductsSwitch(ActionEvent event) {
        try {
            Parent parent2 = FXMLLoader.load(getClass().getResource("/displayProduit.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Liste des Produits");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void addCategorySwitch(ActionEvent event) {
        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/addCategorie.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter une catégorie");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void displayCategorySwitch(ActionEvent event) {
        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/displayCategorie.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Liste des catégories");
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


