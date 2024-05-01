package org.example.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.example.model.Category;
import org.example.model.Evenement;
import org.example.model.Format;
import org.example.services.CategoryService;
import org.example.services.EvenementService;
import org.example.services.FormatService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class DisplayEventsController implements Initializable {

    @FXML
    private ListView<Evenement> listEvents;

    @FXML
    private TextField SearchBar;

    @FXML
    private ComboBox categoryComboBox;

    @FXML
    private ComboBox formatComboBox;

    private final int MAX_CLICK_DURATION = 250; // Maximum duration between clicks in milliseconds
    private final int MAX_CLICKS = 2; // Maximum number of clicks to consider as a double click

    private long lastClickTime = 0;
    private int clickCount = 0;

    EvenementService se = new EvenementService();
    CategoryService sc = new CategoryService();

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

                    Label nbTicketsLabel = new Label("Nombre de tickets:");
                    nbTicketsLabel.setStyle(labelStyl);
                    TextField nbTicketsField = new TextField();
                    nbTicketsField.setPromptText("Entrez le nombre de tickets");
                    nbTicketsField.setStyle(textFieldStyle);
                    nbTicketsField.setPrefWidth(50); // Adjust width as needed
                    container.add(nbTicketsLabel, 0, 1);
                    container.add(nbTicketsField, 1, 1);

                    Button participateButton = new Button("Participer");
                    participateButton.setStyle("-fx-background-color: #d9aa55; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");
                    participateButton.setOnAction(event -> {
                        String nbTicketsText = nbTicketsField.getText();
                        if (!nbTicketsText.isEmpty()) {
                        int nbTickets = Integer.parseInt(nbTicketsField.getText());
                        se.decrementNbPlaces(e, nbTickets); // Decrement the number of places of the event
                        // Refresh the display to reflect the new value of nbPlaces
                        updateItem(e, empty);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation de participation");
                        alert.setHeaderText("Bienvenue!");
                        alert.setContentText("Vous avez bien participé à l'événement. Nombre de places : " + nbTickets);
                        alert.showAndWait();

                    } else {
                        // Handle the case where the text field is empty
                        // For example, show an alert to the user
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a valid number of tickets.");
                        alert.showAndWait();
                    }
                    });

                    container.add(participateButton, 2,1); // Ajoute le bouton à la deuxième ligne de la GridPane




                    String nameStyle = "-fx-fill: #18593b;  -fx-font-size: 25;";
                    String labelStyle = "-fx-fill: #69bfa7; -fx-font-size: 14; -fx-font-weight: bold;";
                    String dataStyle = "-fx-fill: black; -fx-font-size: 14;";
                    String dateStyle = "-fx-fill: black; -fx-font-size: 13; -fx-font-weight: bold;";


                    Text nameData = new Text(e.getTitre() + "\n");
                    nameData.setStyle(nameStyle);

                    Text descriptionText = new Text("Description: ");
                    descriptionText.setStyle(labelStyle);
                    Text descriptionData = new Text(e.getDescription() + "\n");
                    descriptionData.setStyle(dataStyle);

                    Text dateText = new Text("Date: ");
                    dateText.setStyle(labelStyle);
                    Text dateData = new Text(e.getDate() + "\n");
                    dateData.setStyle(dateStyle);

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
                                    categoryText.getText() + " \n" +
                                    formatText.getText() + " \n"+
                                    dateText.getText()+ " \n"+
                                    nbPlaceData.getText()+ " \n"+
                                    categoryText.getText()+ " \n"+
                                    categoryData.getText()
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
                    formatText.setWrappingWidth(200);
                    formatData.setWrappingWidth(200);
                    categoryText.setWrappingWidth(200);
                    categoryData.setWrappingWidth(200);
                    nbPlaceText.setWrappingWidth(200);
                    nbPlaceData.setWrappingWidth(200);
                    ColumnConstraints col1 = new ColumnConstraints(200);
                    ColumnConstraints col2 = new ColumnConstraints(450);
                    ColumnConstraints col3 = new ColumnConstraints(200);
                    container.getColumnConstraints().addAll(col1, col2, col3);

                    container.add(Qr, 2, 0);


                    textFlow.getChildren().addAll(nameData, descriptionText, descriptionData, dateText,dateData,nbPlaceText,nbPlaceData, formatText, formatData, categoryText, categoryData);
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/EventItem.fxml"));
        Parent root;
        try {
            root = loader.load();
            EventItemController controller= loader.getController();
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


    public void handleFilterAction(ActionEvent actionEvent){

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
        System.out.println("Filtered Events: " + filteredEvents.size());

        listEvents.getItems().clear();
        listEvents.getItems().addAll(filteredEvents);
}

    public void clearFilterAction(ActionEvent actionEvent) {
        // Effacer le filtrage en réinitialisant la liste des événements
        listEvents.getItems().clear();
        listEvents.getItems().addAll(se.getAll());
        // Effacer la sélection des ComboBox
        categoryComboBox.getSelectionModel().clearSelection();
        formatComboBox.getSelectionModel().clearSelection();
    }
}
