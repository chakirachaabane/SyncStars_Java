package tn.esprit.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.models.Data;
import tn.esprit.utils.MyDatabase;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

//Captcha

import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import javafx.scene.image.ImageView;
import com.octo.captcha.service.CaptchaService;
import com.github.cage.Cage;
import com.github.cage.GCage;

public class SignInController implements Initializable {

    @FXML
    private Label loginErrorMsg;
    @FXML
    private Label passwordErrorMsg;
    @FXML
    private Label emailErrorMsg;
    @FXML
    private TextField emailTf;
    @FXML
    private PasswordField passwordTf;
    @FXML
    private CheckBox showPassword;
    private Stage stage;
    private Scene scene;
    private Parent pt;
    static Connection conn = MyDatabase.getInstance().getConnection();
    PreparedStatement pst;

    //Captcha
    @FXML
    private ImageView captchaImageView;
    @FXML
    private TextField repeatedCaptchaTf;
    @FXML
    private Label captchaErrorMsg;
    private CaptchaService captchaservice;
    private String captchaText;
    private Cage cage = new GCage();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailTf.setPromptText("Entrez votre email");
        passwordTf.setPromptText("Entrez votre mdps");
        repeatedCaptchaTf.setPromptText("Retapez texte");
        captchaText = cage.getTokenGenerator().next();
        System.out.println(captchaText);
        BufferedImage image = cage.drawImage(captchaText);
        WritableImage fxImage = SwingFXUtils.toFXImage(image, null);
        captchaImageView.setImage(fxImage);
    }

    @FXML
    private void signUpClicked(ActionEvent event) {

        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/signUp-view.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("S'inscrire");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loginClicked(ActionEvent event) throws NoSuchAlgorithmException, IOException, SQLException {

        UserService user = new UserService();
        captchaErrorMsg.setText("");
        emailErrorMsg.setText(" ");
        passwordErrorMsg.setText(" ");
        loginErrorMsg.setText("");
        if(repeatedCaptchaTf.getText().trim().isEmpty()){
          captchaErrorMsg.setText("Captcha obligatoire!");
        }
        else if (!repeatedCaptchaTf.getText().equals(captchaText)){
         captchaErrorMsg.setText("Texte incorrect!");
        }

        if (emailTf.getText().trim().isEmpty() && passwordTf.getText().trim().isEmpty()) {
            emailErrorMsg.setText("Champ email vide !! ");
            passwordErrorMsg.setText("Champ mdps vide !! ");
        } else if (emailTf.getText().trim().isEmpty() && !passwordTf.getText().trim().isEmpty()) {
            emailErrorMsg.setText("Champ email vide !!  ");
            passwordErrorMsg.setText("");
        } else if (passwordTf.getText().trim().isEmpty() && !emailTf.getText().trim().isEmpty()) {
            passwordErrorMsg.setText("Champ mdps vide !! ");
            emailErrorMsg.setText("");
        } else {
            try {

                String sql = "Select id,cin,first_name,last_name,gender,dob,roles,role,email,password,tel,address,image from user where email=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, emailTf.getText());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) { //email existe
                    BCrypt.Result result = BCrypt.verifyer().verify(passwordTf.getText().toCharArray(), rs.getString("password"));
                    System.out.println(result.verified);
                    if(result.verified == true){
                        if (captchaErrorMsg.getText().equals("")){
                            Data.user = user.getUserData(emailTf.getText());
                            if(Data.user.getRole().equals("Client")) {
                                welcomeSwitch(event);
                            }else {
                                dashboardSwitch(event);
                            }
                        }
                    }else{
                        loginErrorMsg.setText("Mot de passe incorrect !! ");
                    }

                } else {
                    loginErrorMsg.setText("Email incorrect !! ");
                    passwordTf.setText("");
                }

            } catch (SQLException ex) {
                Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void dashboardSwitch(ActionEvent event) {
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
    private void showPasswordClicked(ActionEvent event) {

        if (showPassword.isSelected()) {
            passwordTf.setPromptText(passwordTf.getText());
            passwordTf.setText("");
        } else {
            passwordTf.setText(passwordTf.getPromptText());
            passwordTf.setPromptText("");
        }

    }

    private void welcomeSwitch(ActionEvent event) {
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
    private void forgotPasswordButton(ActionEvent event) {
        try {
            pt = FXMLLoader.load(getClass().getResource("/resetPassword-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pt);
            stage.setScene(scene);
            stage.setTitle("Réinitialiser mot de passe!");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignInController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void captchaButtonClicked (ActionEvent event) {
        captchaText = cage.getTokenGenerator().next();
        System.out.println(captchaText);
        BufferedImage image = cage.drawImage(captchaText);
        WritableImage fxImage = SwingFXUtils.toFXImage(image, null);
        captchaImageView.setImage(fxImage);

    }

}
