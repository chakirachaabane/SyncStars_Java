package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.models.Data;
import tn.esprit.models.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyUserAdminController implements Initializable {
    @FXML
    private TextField firstNameTf;

    @FXML
    private TextField lastNameTf;

    @FXML
    private TextField cinTf;

    @FXML
    private RadioButton male_gender;

    @FXML
    private RadioButton female_gender;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private TextField telephoneTf;

    @FXML
    private TextField addressTf;
    @FXML
    private Label errorMsgFirstName;
    @FXML
    private Label errorMsgLastName;
    @FXML
    private Label errorMsgCin;
    @FXML
    private Label errorMsgAddress;
    @FXML
    private Label errorMsgBirthDate;
    @FXML
    private Label errorMsgGender;
    @FXML
    private Label errorMsgPhoneNumber;


    private boolean maleSelected = false;
    private boolean femaleSelected = false;
    String dataGender;

    @FXML
    private ImageView imageView;
    private File selectedFile;

    private String image = Data.user.getImage();
    private User user1;
    private UserService userService;

    private Stage stage;
    private Scene scene;
    private Parent pt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameTf.setText(Data.user.getFirstname());
        lastNameTf.setText(Data.user.getLastname());
        cinTf.setText(String.valueOf(Data.user.getCin()));
        telephoneTf.setText(String.valueOf(Data.user.getPhone_number()));
        addressTf.setText(Data.user.getAddress());
        dobPicker.setValue(Data.user.getBirth_date());
        dataGender = Data.user.getGender();

        if (dataGender.equals("Male")) {
            male_gender.setSelected(true); // select male radio button
            female_gender.setSelected(false); // select male radio button
        } else if (dataGender.equals("Female")) {
            female_gender.setSelected(true); // select male radio button
            male_gender.setSelected(false); // select male radio button
        }

        // Add listener to toggle selected radio button and update dataGender
        male_gender.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                female_gender.setSelected(false);
                dataGender = "Male";
            }
        });

        female_gender.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                male_gender.setSelected(false);
                dataGender = "Female";
            }
        });

    }


    @FXML
    private void btn_addimage_clicked(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            // Create an Image object from the selected file
            Image image = new Image(selectedFile.toURI().toString());
            System.out.println(selectedFile.getName());
            // Set the image of the ImageView
            imageView.setImage(image);
        }

    }

    private String encryptMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void valideModifyBtn_clicked(ActionEvent event) throws NoSuchAlgorithmException {

        String firstName = firstNameTf.getText();
        String lastName = lastNameTf.getText();
        String address = addressTf.getText();
        LocalDate birthDate = dobPicker.getValue();
        String gender = Data.user.getGender();
        int cin = 0;
        int telephone = 0;
        LocalDate currentDate = LocalDate.now();
        errorMsgFirstName.setText("");
        errorMsgLastName.setText("");
        errorMsgCin.setText("");
        errorMsgAddress.setText("");
        errorMsgPhoneNumber.setText("");
        errorMsgGender.setText("");
        errorMsgBirthDate.setText("");

        if (firstName.isEmpty()) {
            errorMsgFirstName.setText("Prénom obligatoire!");
        } else if (!firstName.matches("[a-zA-Z]+")) {
            errorMsgFirstName.setText("Uniquement des lettres!");
        } else if (firstNameTf.getText().toString().length() < 3) {
            errorMsgFirstName.setText("Minimum 3 caractères!");
        }

        if (lastName.isEmpty()) {
            errorMsgLastName.setText("Nom obligatoire!");
        } else if (!lastName.matches("[a-zA-Z]+")) {
            errorMsgLastName.setText("Uniquement des lettres!");
        } else if (lastNameTf.getText().toString().length() < 3) {
            errorMsgLastName.setText("Minimum 3 caractères!");
        }

        if (cinTf.getText().isEmpty()) {
            errorMsgCin.setText("CIN obligatoire!");
        } else if (!cinTf.getText().matches("\\d+")) {
            errorMsgCin.setText("Uniquement des chiffres!");
        } else if (cinTf.getText().toString().length() < 8 || cinTf.getText().toString().length() > 8) {
            errorMsgCin.setText("Exactement 8 chiffres!");
        } else {
            cin = Integer.parseInt(cinTf.getText().trim());
        }

        if (!male_gender.isSelected() && !female_gender.isSelected()) {
            errorMsgGender.setText("Choisir votre sexe!");
        } else if (male_gender.isSelected()) {
            gender = male_gender.getText();
        } else {
            gender = female_gender.getText();
        }

        if (birthDate == null) {
            errorMsgBirthDate.setText("Date obligatoire!");
        } else if (birthDate.isAfter(currentDate)) {
            errorMsgBirthDate.setText("Date non valide!");
        }


        if (telephoneTf.getText().isEmpty()) {
            errorMsgPhoneNumber.setText("Teléphone obligatoire!");
        } else if (!telephoneTf.getText().matches("\\d+")) {
            errorMsgPhoneNumber.setText("Uniquement des chiffres!");
        } else if (telephoneTf.getText().toString().length() < 8 || telephoneTf.getText().toString().length() > 8) {
            errorMsgPhoneNumber.setText("Exactement 8 chiffres!");
        } else {
            telephone = Integer.parseInt(telephoneTf.getText().trim());
        }
        if (address.isEmpty()) {
            errorMsgAddress.setText("Adresse obligatoire!");
        } else if (addressTf.getText().toString().length() < 6) {
            errorMsgAddress.setText("Minimum 6 caractères!");
        }
        if (errorMsgCin.getText().equals("") && errorMsgFirstName.getText().equals("") && errorMsgLastName.getText().equals("") && errorMsgBirthDate.getText().equals("") && errorMsgGender.getText().equals("") && errorMsgAddress.getText().equals("") && errorMsgPhoneNumber.getText().equals("")) {

            if (selectedFile != null) {
                try {
                    // Définir le répertoire de destination
                    String destinationDirectory = "C:\\Users\\user\\Desktop\\SecondProject1\\public\\FrontOffice\\img";
                    File destinationDir = new File(destinationDirectory);

                    // S'assurer que le répertoire de destination existe
                    if (!destinationDir.exists()) {
                        destinationDir.mkdirs();
                    }

                    // Diviser le nom de fichier en nom de base et extension
                    String fileName = selectedFile.getName();
                    int lastDotIndex = fileName.lastIndexOf('.');
                    String baseName = fileName.substring(0, lastDotIndex);
                    String extension = fileName.substring(lastDotIndex);

                    // Crypter le nom de base du fichier
                    String encryptedBaseName = encryptMD5(baseName);

                    // Concaténer le nom de base crypté avec l'extension
                    String encryptedFileName = encryptedBaseName + extension;

                    // Déplacer le fichier sélectionné vers le répertoire de destination avec le nom crypté
                    Path sourcePath = selectedFile.toPath();
                    Path destinationPath = Paths.get(destinationDirectory, encryptedFileName);
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    image = encryptedFileName;
                    System.out.println("Image uploaded to: " + destinationPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No image selected.");
            }

            user1 = new User(Data.user.getId(), cin, firstName, lastName, birthDate, address, telephone, gender, image);

            //****4**** Modifier de user
            userService = new UserService();
            userService.modifyUser(user1);
            Data.user = userService.getUserData(Data.user.getEmail());
            showUserAdminSwitch(event);
        }
    }
    private void showUserAdminSwitch(ActionEvent event) {
        try {
            pt = FXMLLoader.load(getClass().getResource("/showUserAdmin-view.fxml"));
            stage = new Stage();
            scene = new Scene(pt);
            // Close the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Assuming comboBox is part of your current scene
            currentStage.close();
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(ShowUserAdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void returnClicked(ActionEvent event) {
        try {
            pt = FXMLLoader
                    .load(getClass().getResource("/showUserAdmin-view.fxml"));

            scene = new Scene(pt);
            stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("User infos");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(ShowUserAdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
