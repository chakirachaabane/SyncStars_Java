package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import tn.esprit.models.entrainements;
import tn.esprit.services.EntrainementServices;
import tn.esprit.views.EntrainementCardView;
import javafx.event.ActionEvent;

import okhttp3.*;
import java.io.IOException;
import okhttp3.OkHttpClient;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class serachEntrainementfront {

    private final OkHttpClient client = new OkHttpClient();
    @FXML
    private final String apiKey = "sk-proj-05hf4Tl94PenfEN8AHeqT3BlbkFJMxFsmjBDVBx3z83r8zq5";

    @FXML
    private ComboBox<String> objectifComboBox;

    @FXML
    private TilePane entrainementsTilePane;

    @FXML
    private TextField poidsTF;

    private EntrainementServices entrainementService = new EntrainementServices();

    @FXML
    public void initialize() {
        // Initialize the ComboBox with options
        ObservableList<String> objectifOptions = FXCollections.observableArrayList("se muscler", "perte de poids");
        objectifComboBox.setItems(objectifOptions);
    }

    @FXML
    void searchEntrainements() {
        // Clear existing cards in the TilePane
        entrainementsTilePane.getChildren().clear();

        // Get the selected objectif from the ComboBox
        String selectedObjectif = objectifComboBox.getSelectionModel().getSelectedItem();
        int poids = Integer.parseInt(poidsTF.getText());
        if (selectedObjectif != null && !selectedObjectif.isEmpty()) {
            try {
                // Retrieve entrainements filtered by the selected objectif
                List<entrainements> filteredEntrainements = entrainementService.getByTypeEntrainemnt(selectedObjectif); //(selectedObjectif);

                // Display the filtered entrainements using EntrainementCardView
                for (entrainements entrainement : filteredEntrainements) {

                    EntrainementCardView cardView = new EntrainementCardView(entrainement,selectedObjectif,poids);
                    entrainementsTilePane.getChildren().add(cardView);
                }
//               String messageToSend = "Hello, world!"; // You can replace this with any message
//               sendMessageToChatGPT(messageToSend);
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions accordingly
            }
        }
    }






//       public void sendMessageToChatGPT(String message) throws Exception {
//            String url = "https://api.openai.com/v1/chat/completions";  // URL de l'API ChatGPT
//
//            MediaType mediaType = MediaType.parse("application/json");
//            String requestBody = "{\"models\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(RequestBody.create(requestBody, mediaType))
//                    .addHeader("Authorization", "Bearer " + apiKey)
//                    .build();
//
//            try (Response response = client.newCall(request).execute()) {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                System.out.println(response.body().string());
//
//        }
//    }


//    @FXML
//    private void handleChatButtonClicked(ActionEvent event) {
//        String messageToSend = "Votre message ici"; // Remplacez cela par le message que vous souhaitez envoyer
//        try {
//            //sendMessageToChatGPT(messageToSend);
//        } catch (Exception e) {
//            e.printStackTrace(); // Gérez les exceptions selon vos besoins
//        }
//    }


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
