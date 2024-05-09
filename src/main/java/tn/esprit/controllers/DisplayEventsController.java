package tn.esprit.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.twilio.Twilio;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.twilio.rest.api.v2010.account.Message;

import com.twilio.type.PhoneNumber;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import tn.esprit.models.Category;
import tn.esprit.models.Data;
import tn.esprit.models.Evenement;
import tn.esprit.models.Format;
import tn.esprit.services.CategoryService;
import tn.esprit.services.EvenementService;
import tn.esprit.services.FormatService;
import tn.esprit.services.UserService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DisplayEventsController implements Initializable {

    @FXML
    private ListView<Evenement> listEvents;

    @FXML
    private TextField SearchBar;

    @FXML
    private ComboBox categoryComboBox;

    @FXML
    private ComboBox formatComboBox;


    @FXML
    private Button likeButton;

    @FXML
    private Button dislikeButton;

    private final int MAX_CLICK_DURATION = 250; // Maximum duration between clicks in milliseconds
    private final int MAX_CLICKS = 2; // Maximum number of clicks to consider as a double click

    private long lastClickTime = 0;
    private int clickCount = 0;

    private int likeCount = 0;
    private int dislikeCount = 0;

    private boolean hasLiked = false;
    private boolean hasDisliked = false;

    EvenementService se = new EvenementService();
    CategoryService sc = new CategoryService();

    UserService us= new UserService();

    FormatService sf = new FormatService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Category> categories = sc.readAll();
        List<Format> formats = sf.readAll();
        categoryComboBox.getItems().addAll(categories);
        formatComboBox.getItems().addAll(formats);
        formatComboBox.getItems().add("Aucun");
        categoryComboBox.getItems().add("Aucun");

        listEvents.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(Evenement e, boolean empty) {
                super.updateItem(e, empty);

                if (empty || e == null) {
                    setText(null);
                    setGraphic(null);
                } else {


                    GridPane container = new GridPane();

                    TextFlow textFlow = new TextFlow();


                    // Style des labels
                    String labelStyl = "-fx-font-size: 14px; -fx-text-fill: #333333;";

// Style des champs de texte
                    String textFieldStyle = "-fx-font-size: 14px; -fx-prompt-text-fill: #999999;";

                    /*Text likeCountText = new Text(String.valueOf(e.getLikeCount()));
                    likeCountText.setStyle("-fx-fill: #1f9f1f;");
                    Text dislikeCountText = new Text(String.valueOf(e.getDislikeCount()));
                    dislikeCountText.setStyle("-fx-fill: #ff0101;");

                    Button likeButton = new Button("J'aime");
                    likeButton.setStyle("-fx-background-color: #1f9f1f; -fx-text-fill: white; -fx-font-weight: bold;");
                    Button dislikeButton = new Button("Je n'aime pas");
                    dislikeButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-weight: bold;");


                    likeButton.setOnAction(event -> {
                        if (!e.isHasLiked()) {
                            e.incrementLikeCount();
                            e.setHasLiked(true);
                            if (e.isHasDisliked()) {
                                e.decrementDislikeCount();
                                e.setHasDisliked(false);
                            }
                            likeCountText.setText(String.valueOf(e.getLikeCount()));
                            dislikeCountText.setText(String.valueOf(e.getDislikeCount()));
                            se.update(e);
                        }
                    });

                    dislikeButton.setOnAction(event -> {
                        if (!e.isHasDisliked()) {
                            e.incrementDislikeCount();
                            e.setHasDisliked(true);
                            if (e.isHasLiked()) {
                                e.decrementLikeCount();
                                e.setHasLiked(false);
                            }
                            likeCountText.setText(String.valueOf(e.getLikeCount()));
                            dislikeCountText.setText(String.valueOf(e.getDislikeCount()));
                            se.update(e);
                        }
                    }); */

                    // Remplacez la création du champ de texte nbTicketsField par un HBox contenant un champ de texte et des boutons
                    HBox nbTicketsBox = new HBox();
                    Label nbTicketsLabel = new Label("tickets:");
                    nbTicketsLabel.setStyle(labelStyl);
                    TextField nbTicketsField = new TextField();
                    nbTicketsField.setStyle(textFieldStyle);
                    nbTicketsField.setPrefWidth(50); // Adjust width as needed

                    Button incrementButton = new Button("+");
                    incrementButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-min-width: 30px; -fx-min-height: 30px; -fx-background-color: #d9aa55;");
                    incrementButton.setOnAction(event -> {
                        String text = nbTicketsField.getText();
                        int current = text.isEmpty() ? 0 : Integer.parseInt(text);
                        nbTicketsField.setText(String.valueOf(current + 1));
                    });

                    Button decrementButton = new Button("-");
                    decrementButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-min-width: 30px; -fx-min-height: 30px; -fx-background-color: #d9aa55;");
                    decrementButton.setOnAction(event -> {
                        String text = nbTicketsField.getText();
                        int current = text.isEmpty() ? 0 : Integer.parseInt(text);
                        if (current > 0) {
                            nbTicketsField.setText(String.valueOf(current - 1));
                        }
                    });

                    /* likeButton.setOnAction(event -> {
                        if (e.isHasLiked()) {
                            e.decrementLikeCount();
                            e.setHasLiked(false);
                            likeCountText.setText(String.valueOf(e.getLikeCount()));
                            dislikeCountText.setText(String.valueOf(e.getDislikeCount()));
                        } else {
                            e.incrementLikeCount();
                            e.setHasLiked(true);
                            if (e.isHasDisliked()) {
                                e.decrementDislikeCount();
                                e.setHasDisliked(false);
                                dislikeCountText.setText(String.valueOf(e.getDislikeCount()));
                            }
                            likeCountText.setText(String.valueOf(e.getLikeCount()));
                        }
                        se.update(e);
                    });

                    dislikeButton.setOnAction(event -> {
                        if (e.isHasDisliked()) {
                            e.decrementDislikeCount();
                            e.setHasDisliked(false);
                            dislikeCountText.setText(String.valueOf(e.getDislikeCount()));
                        } else {
                            e.incrementDislikeCount();
                            e.setHasDisliked(true);
                            if (e.isHasLiked()) {
                                e.decrementLikeCount();
                                e.setHasLiked(false);
                                likeCountText.setText(String.valueOf(e.getLikeCount()));
                            }
                            dislikeCountText.setText(String.valueOf(e.getDislikeCount()));
                        }
                        se.update(e);
                    }); */


                    Button participateButton = new Button("Participer");
                    participateButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-min-width: 80px; -fx-min-height: 30px; -fx-background-color: #d9aa55;");
                    participateButton.setPrefWidth(153);
                    participateButton.setOnAction(event -> {
                        String nbTicketsText = nbTicketsField.getText();
                        if (!nbTicketsText.isEmpty()) {
                            int nbTickets = Integer.parseInt(nbTicketsText);
                            if (nbTickets > 0) {
                                sendSMS();
                                se.decrementNbPlaces(e, nbTickets);
                                updateItem(e, empty);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Confirmation de participation");
                                alert.setHeaderText("Bienvenue!");
                                alert.setContentText("Vous avez bien participé à l'événement. Nombre de places : " + nbTickets);
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText(null);
                                alert.setContentText("Veuillez entrer un nombre de tickets valide.");
                                alert.showAndWait();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erreur");
                            alert.setHeaderText(null);
                            alert.setContentText("Veuillez entrer un nombre de tickets.");
                            alert.showAndWait();
                        }
                    });

                    nbTicketsBox.getChildren().addAll(nbTicketsLabel, incrementButton, nbTicketsField, decrementButton);
                    container.add(nbTicketsBox, 2, 1);
                    container.add(participateButton, 2, 2);


                    String nameStyle = "-fx-fill: #18593b;  -fx-font-size: 25;";
                    String labelStyle = "-fx-fill: #69bfa7; -fx-font-size: 14; -fx-font-weight: bold;";
                    String dataStyle = "-fx-fill: black; -fx-font-size: 14;";
                    String dateStyle = "-fx-fill: black; -fx-font-size: 13; -fx-font-weight: bold;";


                    Text nameData = new Text(e.getTitre() + "\n\n");
                    nameData.setStyle(nameStyle);

                    Text descriptionText = new Text("Description: ");
                    descriptionText.setStyle(labelStyle);
                    Text descriptionData = new Text(e.getDescription() + "\n");
                    descriptionData.setStyle(dataStyle);

                    Text dateText = new Text("Date: ");
                    dateText.setStyle(labelStyle);
                    Text dateData = new Text(e.getDate() + "\n");
                    dateData.setStyle(dateStyle);

                    // Ajouter un Text et un Label pour afficher l'heure
                    Text heureText = new Text("Heure: ");
                    heureText.setStyle(labelStyle);
                    Text heureData = new Text(e.getHeure().toString() + "\n");
                    heureData.setStyle(dateStyle);

                    Text adresseText = new Text("Adresse: ");
                    adresseText.setStyle(labelStyle);
                    Text adresseData = new Text(e.getAdresse() + "\n");
                    adresseData.setStyle(dateStyle);

                    Text formatText = new Text("Format: ");
                    formatText.setStyle(labelStyle);
                    Text formatData = new Text(e.getFormat() + "\n");
                    formatData.setStyle(dataStyle);

                    Text categoryText = new Text("categorie: ");
                    categoryText.setStyle(labelStyle);
                    Text categoryData = new Text(e.getCategorie() + "\n");
                    categoryData.setStyle(dataStyle);

                    Text nbPlaceText = new Text("Nombre places: ");
                    nbPlaceText.setStyle(labelStyle);
                    Text nbPlaceData = new Text(e.getNbPlaces() + "\n");
                    categoryData.setStyle(dataStyle);

                    String imagePath = e.getImage();
                    Image productImage = new Image(new File(imagePath).toURI().toString());
                    ImageView imageView = new ImageView(productImage);

                    BufferedImage qrCodeImage = createQRImage(
                            nameData.getText() + " : \n" +
                                    descriptionText.getText() + " \n" +
                                    descriptionData.getText() + " \n" +
                                    dateText.getText() + " \n" +
                                    dateData.getText() + " \n" +
                                    heureData.getText() + " \n" +
                                    nbPlaceText.getText() + " \n" +
                                    nbPlaceData.getText()
                            , 120);
                    Image qrCodeImageFX = SwingFXUtils.toFXImage(qrCodeImage, null);
                    ImageView Qr = new ImageView(qrCodeImageFX);


                    imageView.setFitHeight(200);
                    imageView.setFitWidth(200);
                    nameData.setWrappingWidth(200);
                    descriptionData.setWrappingWidth(200);
                    descriptionText.setWrappingWidth(200);
                    dateText.setWrappingWidth(200);
                    dateData.setWrappingWidth(200);
                    heureText.setWrappingWidth(200);
                    heureData.setWrappingWidth(200);
                    formatText.setWrappingWidth(200);
                    formatData.setWrappingWidth(200);
                    categoryText.setWrappingWidth(200);
                    categoryData.setWrappingWidth(200);
                    adresseText.setWrappingWidth(200);
                    adresseData.setWrappingWidth(200);
                    nbPlaceText.setWrappingWidth(200);
                    nbPlaceData.setWrappingWidth(200);
                    ColumnConstraints col1 = new ColumnConstraints(200);
                    ColumnConstraints col2 = new ColumnConstraints(450);
                    ColumnConstraints col3 = new ColumnConstraints(200);
                    container.getColumnConstraints().addAll(col1, col2, col3);

                    container.add(Qr, 2, 0);


                    textFlow.getChildren().addAll(nameData, descriptionText, descriptionData, dateText, dateData,heureText,heureData, adresseText, adresseData, nbPlaceText, nbPlaceData, formatText, formatData, categoryText, categoryData);
                    container.add(textFlow, 1, 0);
                    container.add(imageView, 0, 0);

                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS);
                    container.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);

                    container.setHgap(30);

                    setGraphic(container);


                    listEvents.setOnMouseClicked(event -> {
                        long now = System.currentTimeMillis();
                        if (clickCount == 0 || now - lastClickTime <= MAX_CLICK_DURATION) {
                            clickCount++;
                            if (clickCount == MAX_CLICKS) {
                                Evenement selectedEvent = listEvents.getSelectionModel().getSelectedItem();
                                if (selectedEvent != null) {
                                    // Redirection vers la page de description de l'événement sélectionné
                                    redirectToDescriptionPage(selectedEvent);
                                }
                            }
                        } else {
                            clickCount = 1;
                        }
                        lastClickTime = now;
                    });

                }
            }
        });

        listEvents.getItems().addAll(se.getAll());


    }

    private void redirectToDescriptionPage(Evenement selectedEvent) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventItem.fxml"));
        Parent root;
        try {
            root = loader.load();
            EventItemController controller = loader.getController();
            controller.initData(selectedEvent); // Passer les données de l'événement à la page de description
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void search(ActionEvent event) throws SQLException {
        String searchTerm = SearchBar.getText().trim(); // Trim whitespace

        List<Evenement> searchResult;
        if (searchTerm.isEmpty()) {
            searchResult = se.getAll();
        } else {
            searchResult = se.getByName(searchTerm);
        }

        if (searchResult.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Échec");
            alert.setHeaderText(null);
            alert.setContentText(" Aucun événement correspondant au terme de recherche n'a été trouvé.");
            alert.showAndWait();
        } else {
            listEvents.getItems().clear();
            listEvents.getItems().addAll(searchResult);
        }

    }


    public BufferedImage createQRImage(String qrCodeText, int size) {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = null;
        try {
            byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
    }


    public void handleFilterAction(ActionEvent actionEvent) {

        Object selectedCategoryObj = categoryComboBox.getValue();
        Object selectedFormatObj = formatComboBox.getValue();

        Category selectedCategory = null;
        Format selectedFormat = null;

        if (selectedCategoryObj instanceof Category) {
            selectedCategory = (Category) selectedCategoryObj;
        }
        if (selectedFormatObj instanceof Format) {
            selectedFormat = (Format) selectedFormatObj;
        }

        System.out.println("Selected Category: " + selectedCategory);
        System.out.println("Selected Format: " + selectedFormat);

        List<Evenement> filteredEvents = se.getByCategoryAndFormat(selectedCategory, selectedFormat);

        listEvents.getItems().clear();
        listEvents.getItems().addAll(filteredEvents);

        if (filteredEvents.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Échec");
            alert.setHeaderText(null);
            alert.setContentText("Aucun événement trouvé.");
            alert.showAndWait();
        } else {
            listEvents.getItems().clear();
            listEvents.getItems().addAll(filteredEvents);
        }
    }

    public void clearFilterAction(ActionEvent actionEvent) {
        // Effacer le filtrage en réinitialisant la liste des événements
        listEvents.getItems().clear();
        listEvents.getItems().addAll(se.getAll());
        // Effacer la sélection des ComboBox
        categoryComboBox.getSelectionModel().clearSelection();
        formatComboBox.getSelectionModel().clearSelection();
    }

    private void sendSMS() {
        // Initialize Twilio with your Account SID and Auth Token
        Twilio.init("ACc87ded6bedeb009e570c8f50f10e90f4", "60990c89561369f066f05a8737c3a1bb");
        // Remplacer les valeurs suivantes par votre numéro Twilio et le numéro de téléphone de destination
        String twilioNumber = "+17205230423";
// Votre numéro Twilio
        String recipientNumber = "+21627873721";
        // Numéro de téléphone du destinataire
        String messageBody = "Participation confirmée !";
        Message message = Message.creator( new PhoneNumber(recipientNumber), new PhoneNumber(twilioNumber), messageBody ).create();
        System.out.println("Message SID: " + message.getSid());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }






    public void refresh(ActionEvent event) {

        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
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


