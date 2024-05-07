package tn.esprit.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.models.User;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static tn.esprit.controllers.SignInController.conn;

public class SignUpController implements Initializable {
    /**
     * ************************** Décalaration variables SceneBuilder
     * ****************************
     */
    @FXML
    private TextField firstNameTf;

    @FXML
    private TextField lastNameTf;

    @FXML
    private TextField emailTf;

    @FXML
    private PasswordField passwordTf;

    @FXML
    private TextField cinTf;

    @FXML
    private TextField addressTf;

    @FXML
    private DatePicker birthDateTf;

    @FXML
    private TextField phoneNumberTf;

    @FXML
    private Label errorMsgAddress;

    @FXML
    private Label errorMsgBirthDate;

    @FXML
    private Label errorMsgCin;

    @FXML
    private Label errorMsgEmail;

    @FXML
    private Label errorMsgFirstName;

    @FXML
    private Label errorMsgGender;

    @FXML
    private Label errorMsgLastName;

    @FXML
    private Label errorMsgPhoneNumber;

    @FXML
    private Label errorMsgPassword;

    @FXML
    private RadioButton female_gender;

    @FXML
    private RadioButton male_gender;

    @FXML
    private CheckBox showPassword;

    private UserService userService;
    private User user1;

    private Stage stage;
    private Scene scene;
    private Parent pt;

    private String image = "userImage.png";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        firstNameTf.setPromptText("Entrez votre prénom");
        lastNameTf.setPromptText("Entrez votre nom");
        emailTf.setPromptText("Entrez votre email");
        passwordTf.setPromptText("Entrez votre mdps");
        addressTf.setPromptText("Entrez votre adresse");
        birthDateTf.setPromptText("Entrez votre ddn");
        cinTf.setPromptText("Entrez votre cin");
        phoneNumberTf.setPromptText(("Entez votre num"));

    }



    /**
     * ************************** Méthode appelée lorsque on clique sur le
     * button confirmer signUp * ****************************
     */
    @FXML
    void confirmClicked(ActionEvent event) throws NoSuchAlgorithmException {

        String firstname = firstNameTf.getText();
        String lastname = lastNameTf.getText();
        String email = emailTf.getText();
        String password = passwordTf.getText();
        String address = addressTf.getText();
        LocalDate birthDate = birthDateTf.getValue();
        String gender = null;
        String role = "Client";
        int cin = 0;
        int phone_number = 0;
        LocalDate currentDate = LocalDate.now();
        errorMsgFirstName.setText("");
        errorMsgLastName.setText("");
        errorMsgEmail.setText("");
        errorMsgPassword.setText("");
        errorMsgCin.setText("");
        errorMsgAddress.setText("");
        errorMsgPhoneNumber.setText("");
        errorMsgGender.setText("");
        errorMsgBirthDate.setText("");

        /**
         * ************************** Controle Saisie *
         * ****************************
         */
        if (firstname.isEmpty()) {
            errorMsgFirstName.setText("Prénom obligatoire!");
        } else if (!firstname.matches("[a-zA-Z]+")) {
            errorMsgFirstName.setText("Uniquement des lettres!");
        } else if (firstNameTf.getText().toString().length() < 3) {
            errorMsgFirstName.setText("Minimum 3 caractères!");
        }

        if (lastname.isEmpty()) {
            errorMsgLastName.setText("Nom obligatoire!");
        } else if (!lastname.matches("[a-zA-Z]+")) {
            errorMsgLastName.setText("Uniquement des lettres!");
        } else if (lastNameTf.getText().toString().length() < 3) {
            errorMsgLastName.setText("Minimum 3 caractères!");
        }

        if (cinTf.getText().isEmpty()) {
            errorMsgCin.setText("CIN obligatoire!");
        } else if (!cinTf.getText().matches("\\d+")) {
            errorMsgCin.setText("Uniquement des chiffres!");
        } else if (cinTf.getText().toString().length() != 8) {
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

        if (password.isEmpty()) {
            errorMsgPassword.setText("MDP obligatoire!");
        } else if (passwordTf.getText().toString().length() < 6) {
            errorMsgPassword.setText("Minimum 6 caractères!");
        }

        if (address.isEmpty()) {
            errorMsgAddress.setText("Adresse obligatoire!");
        } else if (addressTf.getText().toString().length() < 6) {
            errorMsgAddress.setText("Minimum 6 caractères!");
        }

        if (email.isEmpty()) {
            errorMsgEmail.setText("Email obligatoire!");
        } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            errorMsgEmail.setText("Email non valide");
        } else {
            boolean isValid = isValidEmail(emailTf.getText());
            if (isValid) {
                // email is valid
                String sql = "SELECT * FROM user WHERE email=?";
                PreparedStatement st;
                ResultSet res = null;
                try {
                    st = conn.prepareStatement(sql);
                    st.setString(1, email);
                    res = st.executeQuery();

                    if (res.next()) {
                        errorMsgEmail.setText(" Email déja utilisé!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                // email is not valid
                errorMsgEmail.setText(" Email non valide !");
            }
        }

        if (phoneNumberTf.getText().isEmpty()) {
            errorMsgPhoneNumber.setText("Teléphone obligatoire!");
        } else if (!phoneNumberTf.getText().matches("\\d+")) {
            errorMsgPhoneNumber.setText("Uniquement des chiffres!");
        } else if (phoneNumberTf.getText().toString().length() < 8 || phoneNumberTf.getText().toString().length() > 8) {
            errorMsgPhoneNumber.setText("Exactement 8 chiffres!");
        } else {
            phone_number = Integer.parseInt(phoneNumberTf.getText().trim());
        }

        if (birthDate == null) {
            errorMsgBirthDate.setText("Date obligatoire!");
        } else if (birthDate.isAfter(currentDate)) {
            errorMsgBirthDate.setText("Date non valide!");
        }

        /**
         * ************************** Si tous les champs sont valides *
         * ****************************
         */
        if (errorMsgCin.getText().equals("") && errorMsgFirstName.getText().equals("") && errorMsgLastName.getText().equals("") && errorMsgEmail.getText().equals("") && errorMsgPassword.getText().equals("") && errorMsgBirthDate.getText().equals("") && errorMsgGender.getText().equals("") && errorMsgAddress.getText().equals("") && errorMsgPhoneNumber.getText().equals("")) {

            //****1**** Ajout et cryptage de l'image
            if (selectedFile != null) {
                try {
                    // Définir le répertoire de destination

                    String destinationDirectory =  "C:\\Users\\user\\Desktop\\SecondProject1\\public\\FrontOffice\\img";

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
                    image = encryptedFileName;  // dans bd
                    System.out.println("Image uploaded to: " + destinationPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No image selected.");
            }

            //****2**** Cryptage de password
            String hashedPassword = UserService.hashPassword(password);

            //****3**** Instancier le user
            user1 = new User(cin, firstname, lastname, email, hashedPassword, address, phone_number, birthDate, gender, role, image);

            //****4**** Ajout de user
            userService = new UserService();
            userService.addUser(user1);

            //****5**** vider les champs
            clear();
            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Ajout");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Le compte a été crée avec succès.");
            successAlert.showAndWait();
            loginSwitch(event);

        }

    }

    /**
     * ************************** Méthode appelée lorsque on clique sur le button radio gender * ****************************
     */
    private boolean maleSelected = false;
    private boolean femaleSelected = false;

    @FXML
    private void swipeRadioButton() {
        // Bind radio buttons' selected properties to their respective boolean variables
        male_gender.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                maleSelected = true;
                femaleSelected = false;
                female_gender.setSelected(false); // Deselect the other radio button
            }
        });

        female_gender.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                femaleSelected = true;
                maleSelected = false;
                male_gender.setSelected(false); // Deselect the other radio button
            }
        });
    }

    /**
     * ******************* Méthode pour crypter un nom de fichier avec MD5 (nom
     * de l'image) ************************
     */
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

    /**
     * ******************* Méthode pour vérifier la validation d'un email
     * ************************
     */
    public static boolean isValidEmail(String email) {
        boolean isValid = false;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
            isValid = true;
        } catch (AddressException ex) {
            // email is not valid
        }
        return isValid;
    }

    /**
     * ******************* Méthode permet d'affiche le password * ************************
     */
    @FXML
    private void showPassword(ActionEvent event) {
        if (showPassword.isSelected()) {
            passwordTf.setPromptText(passwordTf.getText());
            passwordTf.setText("");
        } else {
            passwordTf.setText(passwordTf.getPromptText());
            passwordTf.setPromptText("");
        }
    }

    /**
     * ******************* Méthode permet de choisir une image et l'afficher* ************************
     */
    @FXML
    private ImageView imageView;
    private File selectedFile;

    @FXML
    private void showSelectedImageClicked(ActionEvent event) throws IOException {

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
    public void clear() {

        firstNameTf.setText("");
        lastNameTf.setText("");
        emailTf.setText("");
        passwordTf.setText("");
        addressTf.setText("");
        cinTf.setText("");
        phoneNumberTf.setText("");
        birthDateTf.setValue(null);
        female_gender.setSelected(false);
        male_gender.setSelected(false);

    }

    private void loginSwitch(ActionEvent event) {
        try {
            pt = FXMLLoader.load(getClass().getResource("/signIn-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pt);
            stage.setScene(scene);
            stage.setTitle("Login");
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
                    .load(getClass().getResource("/signIn-view.fxml"));

            scene = new Scene(pt);
            stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignInController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}

