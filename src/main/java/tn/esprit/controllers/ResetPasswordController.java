package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Data;
import tn.esprit.services.UserService;
import tn.esprit.utils.MyDatabase;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResetPasswordController implements Initializable {

    @FXML
    private TextField codeTf;

    @FXML
    private TextField emailTf;

    @FXML
    private Label texteTf;

    @FXML
    private Button envoyerButton;

    @FXML
    private Button validerButton;

    @FXML
    private Button passwordButton;


    @FXML
    private Label emailErrorMsg;
    @FXML
    private Label codeErrorMsg;
    @FXML
    private Label passwordErrorMsg;

    @FXML
    private PasswordField passwordTf;

    @FXML
    private PasswordField repeatedPasswordTf;

    private Stage stage;
    private Scene scene;
    private Parent pt;

    UserService userService;

    static Connection conn = MyDatabase.getInstance().getConnection();

    PreparedStatement pst;

    private int resetCode=0;
    private int id=0;

    private int code=1;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Valider le code
        validerButton.setDisable(true);
        validerButton.setVisible(false);
        codeTf.setDisable(true);
        codeTf.setVisible(false);

        //Valider le mdps
        passwordButton.setDisable(true);
        passwordButton.setVisible(false);
        passwordTf.setDisable(true);
        passwordTf.setVisible(false);
        repeatedPasswordTf.setDisable(true);
        repeatedPasswordTf.setVisible(false);
    }

    @FXML
    void envoyerButtonAction(ActionEvent event) throws SQLException, MessagingException {

        emailErrorMsg.setText("");
        if (emailTf.getText().trim().isEmpty()) {
            emailErrorMsg.setText("Champ email vide ! ");
        }  else if (!emailTf.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            emailErrorMsg.setText("Email non valide !");
        }else {
            emailErrorMsg.setText("");
            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(Data.username, Data.password);
                        }
                    });

            String recipientEmail = emailTf.getText();
            System.out.println(recipientEmail);

            String sql = "Select * from user where email = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, recipientEmail);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);

                Random random = new Random();
                resetCode = 100000+random.nextInt(900000);
                String emailContent ="<html>"
                        + "<head>"
                        + "<style>"
                        + "body { font-family: Arial, sans-serif; background-color: #DCEDFF; }" // Setting clear blue background
                        + ".container { max-width: 600px; margin: auto; }"
                        + ".header { background-color: #57d2af; color: #ffffff; padding: 20px; text-align: center; }" // Increased padding for header
                        + ".logo { text-align: center; margin-bottom: 20px; }"
                        + ".logo img { width: 150px; border-radius: 50%; border: 4px solid #57d2af; box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.2); }" // Added border and shadow to logo
                        + ".content { padding: 30px; color: #333333; }"
                        + ".content p { margin-bottom: 20px; }"
                        + ".reset-code { color: orange; }" // Changed reset code text color to orange
                        + ".footer { background-color: #f5f5f5; padding: 20px; text-align: center; }"
                        + "</style>"
                        + "</head>"
                        + "<body>"
                        + "<div class='container'>"
                        + "<div class='header'>"
                        + "<h2 style='margin: 0;'>Réinitialiser mot de passe</h2>"
                        + "</div>"
                        + "<div class='logo'>"
                        + "<img src='https://i.imgur.com/qhBk2US.png' alt='AlignVibe Logo'>"
                        + "</div>"
                        + "<div class='content'>"
                        + "<p>Bonjour " + rs.getString(3) + ",</p>"
                        + "<p>Merci pour votre confiance. Votre code de réinitialisation est : <span class='reset-code'><strong>" + resetCode + "</strong></span></p>" // Fixed the style attribute and class name
                        + "</div>"
                        + "<div class='footer'>"
                        + "<p>Equipe AlignVibe</p>"
                        + "</div>"
                        + "</div>"
                        + "</body>"
                        + "</html>";
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(Data.username));

                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipientEmail));
                message.setSubject("Réinitialiser mot de passe");
                message.setContent(emailContent, "text/html; charset=utf-8");
                Transport.send(message);

                System.out.println("Email envoyé avec succes !");

                envoyerButton.setDisable(true);
                envoyerButton.setVisible(false);
                emailTf.setDisable(true);
                emailTf.setVisible(false);

                texteTf.setText("Code :");
                validerButton.setDisable(false);
                validerButton.setVisible(true);
                codeTf.setDisable(false);
                codeTf.setVisible(true);
            } else {
                emailErrorMsg.setText("Email inexistant!");
            }
        }
    }

    @FXML
    void validButtonAction(ActionEvent event) throws SQLException{
        codeErrorMsg.setText("");
        if (codeTf.getText().isEmpty()) {
            codeErrorMsg.setText("Code obligatoire!");
        } else if (!codeTf.getText().matches("\\d+")) {
            codeErrorMsg.setText("Uniquement des chiffres!");
        } else if (codeTf.getText().toString().length() !=6) {
            codeErrorMsg.setText("Exactement 6 chiffres!");
        } else {
            code = Integer.parseInt(codeTf.getText());
            if (resetCode == code) {
                validerButton.setDisable(true);
                validerButton.setVisible(false);
                codeTf.setDisable(true);
                codeTf.setVisible(false);
                texteTf.setText("Nouveau mot de passe :");
                passwordButton.setDisable(false);
                passwordButton.setVisible(true);
                passwordTf.setDisable(false);
                passwordTf.setVisible(true);
                repeatedPasswordTf.setDisable(false);
                repeatedPasswordTf.setVisible(true);
                passwordTf.setPromptText("Entrez nouveau mdps");
                repeatedPasswordTf.setPromptText("Retapez mdps");
            } else {
                //Code incorrect
                codeErrorMsg.setText("Code incorrect!");
            }
        }

    }

    @FXML
    void passwordButtonAction(ActionEvent event) throws SQLException {
        passwordErrorMsg.setText("");
        if (passwordTf.getText().isEmpty()) {
            passwordErrorMsg.setText("MDP obligatoire!");
        } else if (passwordTf.getText().toString().length() < 6) {
            passwordErrorMsg.setText("Minimum 6 caractères!");
        } else if (!passwordTf.getText().equals(repeatedPasswordTf.getText())){
          passwordErrorMsg.setText("Mdps non identiques!");
        }else {
            String sql = "update user set password = ? where id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, userService.hashPassword(passwordTf.getText()));
            pst.setInt(2, id);
            pst.executeUpdate();
            return_btn_clicked(event);

        }
    }

    @FXML
    private void return_btn_clicked(ActionEvent event) {
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
