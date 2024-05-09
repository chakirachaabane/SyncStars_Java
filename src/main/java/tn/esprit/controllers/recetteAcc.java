package tn.esprit.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class recetteAcc {

    @FXML
    private HBox imageBox;

    @FXML
    private Button retourButton; // Ajout du bouton "Retour"

    // Définir une durée de transition plus courte pour un défilement plus fluide
    private static final Duration TRANSITION_DURATION = Duration.seconds(2.5);

    public void initialize() {
        // Créer une timeline pour gérer le défilement infini
        Timeline timeline = new Timeline(
                // Ajouter une action pour chaque intervalle de transition
                new KeyFrame(TRANSITION_DURATION, event -> {
                    // Déplacer la première image à la fin de la liste
                    imageBox.getChildren().add(imageBox.getChildren().remove(0));
                })
        );
        // Répéter la timeline indéfiniment
        timeline.setCycleCount(Timeline.INDEFINITE);
        // Démarrer la timeline
        timeline.play();
    }

    @FXML
    public void handleMouseEntered() {
        // Implémentez ce que vous voulez faire lorsque la souris entre dans la zone des images
    }

    @FXML
    public void handleMouseExited() {
        // Implémentez ce que vous voulez faire lorsque la souris sort de la zone des images
    }

    @FXML
    public void retourButtonClicked(ActionEvent event) {
        // Récupérer la scène actuelle à partir de l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Charger et afficher rdvAcc.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/rdvAcc.fxml")); // Vérifiez le chemin ici
            stage.setScene(new Scene(root));
            stage.setTitle("Espace Rdv");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple, en affichant un message d'erreur
        }
    }

    @FXML
    public void diabeteButtonClicked(ActionEvent event) {
        // Récupérer la scène actuelle à partir de l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Charger et afficher recetteDiabete.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/recetteDiabete.fxml")); // Vérifiez le chemin ici
            stage.setScene(new Scene(root));
            stage.setTitle("Recette Diabète");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple, en affichant un message d'erreur
        }
    }


    @FXML
    private void handleObesiteButtonClicked(ActionEvent event) {
        // Charger le fichier FXML de recetteObésité.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteObesite.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void AnorexieButtonClicked(ActionEvent event) {
        // Récupérer la scène actuelle à partir de l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Charger et afficher recetteDiabete.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/recetteAnorexie.fxml")); // Vérifiez le chemin ici
            stage.setScene(new Scene(root));
            stage.setTitle("Recette Diabète");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple, en affichant un message d'erreur
        }
    }
    @FXML
    public void DesequilibreAlButtonClicked(ActionEvent event) {
        // Récupérer la scène actuelle à partir de l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Charger et afficher recetteDiabete.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/recetteDesequilibreAl.fxml")); // Vérifiez le chemin ici
            stage.setScene(new Scene(root));
            stage.setTitle("Recette Diabète");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple, en affichant un message d'erreur
        }
    }
    @FXML
    public void CholesterolButtonClicked(ActionEvent event) {
        // Récupérer la scène actuelle à partir de l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Charger et afficher recetteDiabete.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/recetteCholesterol.fxml")); // Vérifiez le chemin ici
            stage.setScene(new Scene(root));
            stage.setTitle("Recette Diabète");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, par exemple, en affichant un message d'erreur
        }
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

    @FXML
    private void EntrainementPath(ActionEvent event) {
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

}
