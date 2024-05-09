package tn.esprit.controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class rdvAcc {

    @FXML
    private Button prendreRdvButton;

    @FXML
    private Button ConsulterRdvButton;

    @FXML
    private Button EspaceRecetteButton;

    @FXML
    private Pane chatBubble;

    @FXML
    private Button question1Button;

    @FXML
    private Button question2Button;

    @FXML
    private Button question3Button;

    @FXML
    private Button question4Button;

    @FXML
    private Label responseLabel;

    @FXML
    private ImageView firstGifImageView;

    @FXML
    private ImageView secondGifImageView;

    @FXML
    private Label floatingMessage;


    @FXML
    void showChatBubble() {
        hideFloatingMessage();
        chatBubble.setVisible(true);

        // Changer la visibilité des images
        firstGifImageView.setVisible(false);
        secondGifImageView.setVisible(true);
    }


    @FXML
    private void handleQuestion1() {
        showResponse("Nos horaires sont de 08:00 à 17:00.");
    }

    @FXML
    private void handleQuestion2() {
        showResponse("Pour prendre un RDV allez vers espace Rendez-vous puis cliquez sur Prendre un RDV et remplissez le formulaire");
    }

    @FXML
    private void handleQuestion3() {
        showResponse("Les créateurs de AlignVibe sont un groupe de jeunes ingénieurs motivés à aider les gens à améliorer leur lifestyle");
    }

    @FXML
    private void handleQuestion4() {
        showResponse("Si aucune de ses questions ne correspond à votre besoin, veuillez nous contacter sur notre mail : hamdi.azza@esprit.tn");
    }


    private void showResponseWithTypingEffect(String response) {
        // Initialiser le texte de la réponse avec une chaîne vide
        responseLabel.setText("");

        // Durée de la transition pour chaque lettre (ms)
        int letterTransitionDuration = 100;

        // Afficher chaque lettre avec un effet de dactylographie
        Timeline timeline = new Timeline();
        for (int i = 0; i <= response.length(); i++) {
            int finalI = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * letterTransitionDuration), event -> {
                // Ajouter une lettre de plus à chaque itération
                responseLabel.setText(response.substring(0, finalI));
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }


    private void showFloatingMessage() {
        floatingMessage.setVisible(true);
        // Le message à afficher
        String message = "Besoin d'aide ?";

        // Initialiser le message avec une chaîne vide
        floatingMessage.setText("");

        // Définir la position initiale en dehors de la zone visible
        floatingMessage.setTranslateX(400);

        // Créer une transition de translation pour faire apparaître le message de la droite vers la gauche
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), floatingMessage);
        translateTransition.setToX(0);

        // Ajouter une transition de rotation pour un effet plus flottant
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0), floatingMessage);
        rotateTransition.setByAngle(10); // Angle de rotation (en degrés)
        rotateTransition.setCycleCount(TranslateTransition.INDEFINITE); // Répéter indéfiniment

        // Définir le texte du message après l'animation
        translateTransition.setOnFinished(event -> {
            floatingMessage.setText(message);

            // Lancer la transition de rotation
            rotateTransition.play();

            // Ajouter l'effet de dactilographie (typing effect)
            showFloatingMessageWithTyping();

            // Ajouter l'effet de flottement vertical
            showFloatingVertical();
        });

        // Lancer la transition de translation
        translateTransition.play();
    }

    @FXML
    void hideFloatingMessage() {
        // Masquer le message flottant
        floatingMessage.setVisible(false);
    }


    private void showFloatingVertical() {
        // Créer une transition de translation verticale
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), floatingMessage);
        translateTransition.setFromY(0);
        translateTransition.setToY(-20); // Déplacement de -20 pixels vers le haut
        translateTransition.setAutoReverse(true); // Pour un effet de retour
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE); // Pour répéter indéfiniment

        // Lancer la transition
        translateTransition.play();
    }







    @FXML
    public void initialize() {
        // Cacher le deuxième GIF initialement
        secondGifImageView.setVisible(false);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(1500), event -> showFloatingMessage())
        );
        timeline.play();

        // Ajout d'un gestionnaire d'événements au bouton "Prendre un RDV"
        prendreRdvButton.setOnAction(event -> {
            // Charger rdvAdd.fxml
            loadFXML("/rdvAdd.fxml");
        });

        // Ajout d'un gestionnaire d'événements au bouton "Consulter liste RDV"
        ConsulterRdvButton.setOnAction(event -> {
            // Charger rdvList.fxml
            loadFXML("/rdvList.fxml");
        });

        // Ajout d'un gestionnaire d'événements au bouton "Consulter Espace Recette"
        EspaceRecetteButton.setOnAction(event -> {
            // Charger recetteAcc.fxml
            loadFXML("/recetteAcc.fxml");
        });

        // Ajout d'un gestionnaire d'événements à chaque bouton de question
        question1Button.setOnAction(event -> handleQuestion1());
        question2Button.setOnAction(event -> handleQuestion2());
        question3Button.setOnAction(event -> handleQuestion3());
        question4Button.setOnAction(event -> handleQuestion4());


    }

    @FXML
    private void showResponse(String response) {
        showResponseWithTypingEffect(response);
        // Afficher la réponse et cacher les boutons des questions
        responseLabel.setText(response);
        responseLabel.setVisible(true);
        responseLabel.setWrapText(true); // Permettre le retour à la ligne
        responseLabel.setMaxWidth(chatBubble.getPrefWidth()); // Définir la largeur maximale sur celle de la bulle de discussion
        question1Button.setVisible(false);
        question2Button.setVisible(false);
        question3Button.setVisible(false);
        question4Button.setVisible(false);
    }




    private void loadFXML(String fxmlFilePath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void hideChatBubble() {
        chatBubble.setVisible(false);
        // Afficher les questions et cacher la réponse lors de la fermeture de la bulle de discussion
        showQuestions();
    }
    @FXML
    private void showQuestions() {
        responseLabel.setText(""); // Effacer le texte de la réponse
        responseLabel.setVisible(false); // Cacher la réponse
        // Afficher les boutons des questions
        question1Button.setVisible(true);
        question2Button.setVisible(true);
        question3Button.setVisible(true);
        question4Button.setVisible(true);
    }

    @FXML
    void showFirstGif() {
        firstGifImageView.setVisible(true);
        secondGifImageView.setVisible(false);
        hideChatBubble();
    }

    private void showFloatingMessageWithTyping() {
        // Le message à afficher
        String message = "Besoin d'aide ?";

        // Durée de la transition pour chaque lettre (ms)
        int letterTransitionDuration = 100;

        // Initialiser le message avec une chaîne vide
        floatingMessage.setText("");

        // Afficher chaque lettre avec un effet "typing"
        Timeline timeline = new Timeline();
        for (int i = 0; i <= message.length(); i++) {
            int finalI = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * letterTransitionDuration), event -> {
                // Ajouter une lettre de plus à chaque itération
                floatingMessage.setText(message.substring(0, finalI));
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }




    public void showChatBubbleWithAnimation() {
        // Créer une transition de translation
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), chatBubble);
        translateTransition.setFromX(800);
        translateTransition.setToX(800);
        translateTransition.setFromY(630);
        translateTransition.setToY(480);

        // Créer une transition de fondu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), chatBubble);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        // Inverser la visibilité des images pour afficher le deuxième GIF
        firstGifImageView.setVisible(true);
        secondGifImageView.setVisible(false);

        // Exécuter les transitions en séquence
        translateTransition.play();
        fadeTransition.play();
    }

    @FXML
    private void EventPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void RDVPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAcc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void EntrainementPath(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
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

    public void addrdv(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAdd.fxml"));
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
    private void displayEventSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void displayRdvSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAcc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void displayEntSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowForumSwitch1(ActionEvent event) {
        try {
            /*Parent root = FXMLLoader.load(getClass().getResource("/updateQuestionForm.fxml"));
            // Get the controller for the update form
            UpdateQuestionFormController updateController = new UpdateQuestionFormController();*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main_Forum_Page.fxml"));
            Parent root = loader.load();
            ForumPageController controller = loader.getController();


            Stage stage = new Stage();
            stage.setTitle("Forum Page");
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @FXML
    public void showProductsSwitch(ActionEvent event) {
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

    @FXML
    private void showAccueilSwitch(ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent pt;
        PreparedStatement pst;
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


}
