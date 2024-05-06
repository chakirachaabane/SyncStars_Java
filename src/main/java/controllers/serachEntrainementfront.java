package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import model.entrainements;
import services.EntrainementServices;
import views.EntrainementCardView;
import javafx.event.ActionEvent;

import okhttp3.*;
import java.io.IOException;
import okhttp3.OkHttpClient;


import java.util.List;

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
               String messageToSend = "Hello, world!"; // You can replace this with any message
               sendMessageToChatGPT(messageToSend);
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions accordingly
            }
        }
    }






       public void sendMessageToChatGPT(String message) throws Exception {
            String url = "https://api.openai.com/v1/chat/completions";  // URL de l'API ChatGPT

            MediaType mediaType = MediaType.parse("application/json");
            String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(requestBody, mediaType))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                System.out.println(response.body().string());

        }
    }


    @FXML
    private void handleChatButtonClicked(ActionEvent event) {
        String messageToSend = "Votre message ici"; // Remplacez cela par le message que vous souhaitez envoyer
        try {
            //sendMessageToChatGPT(messageToSend);
        } catch (Exception e) {
            e.printStackTrace(); // Gérez les exceptions selon vos besoins
        }
    }

}
