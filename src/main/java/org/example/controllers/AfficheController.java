package org.example.controllers;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import org.example.model.Category;
import org.example.model.Evenement;
import org.example.model.Format;
import org.example.services.CategoryService;
import org.example.services.EvenementService;
import org.example.services.FormatService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AfficheController {


        @FXML
        private TableView<Evenement> eventsTable;

        @FXML
        private TableColumn<Evenement, String> titretf;

        @FXML
        private TableColumn<Evenement, String> adressetf;

        @FXML
        private TableColumn<Evenement, Date> datetf;

        @FXML
        private TableColumn<Evenement, String> categorieEvetf;

        @FXML
        private TableColumn<Evenement, String> formatEvetf;

        @FXML
        private TableColumn<Evenement, String> imagetf;

        @FXML
        private TableColumn<Evenement, Time> heuretf;

        @FXML
        private TableColumn<Evenement, String> descriptiontf;

        @FXML
        private TableColumn<Evenement, Integer> nbPlacestf;

        @FXML
        private Button pieBtn;

        @FXML
        private ImageView imageView;

        @FXML
        private TextField  rechercherTF;

        private EvenementService evenementService = new EvenementService();

        FormatService formatService = new FormatService();

        CategoryService categorieService = new CategoryService();

        @FXML
        void getAddEventView(ActionEvent event) {
                try {
                        // Get the current stage
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        // Load the new FXML file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AjouterEvenement.fxml"));
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
        void DeleteAction(ActionEvent event) {
                // Get the selected item from the TableView
                Evenement selectedEvent = eventsTable.getSelectionModel().getSelectedItem();

                if (selectedEvent != null) {
                        // Create a confirmation dialog
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText("Confirm Deletion");
                        alert.setContentText("Are you sure you want to delete the selected event?");

                        // Show the confirmation dialog
                        Optional<ButtonType> result = alert.showAndWait();

                        // Check if the user clicked OK
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                                // Remove the selected item from the TableView
                                eventsTable.getItems().remove(selectedEvent);

                                // Call the service method to delete the selected event from the database
                                evenementService.delete(selectedEvent.getId());

                                // Display a success alert
                                displayAlert("Success", "Event deleted successfully.");
                        }
                } else {
                        // Display an error alert if no item is selected
                        displayAlert("Error", "Please select an event to delete.");
                }
        }

        private void displayAlert(String title, String content) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(content);
                alert.showAndWait();
        }

        public void PrintAction(ActionEvent actionEvent) {
                Document document = new Document();
                try {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Enregistrer le fichier PDF");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
                        File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());

                        if (file != null) {
                                PdfWriter.getInstance(document, new FileOutputStream(file));
                                document.open();

                                Image folderLogo = Image.getInstance("C:/Users/LENOVO/Downloads/IntegrationV/src/main/resources/images/logo.png");
                                folderLogo.scaleAbsolute(75, 75);
                                folderLogo.setAbsolutePosition(40, 780);
                                document.add(folderLogo);

                                // Ajouter un titre
                                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 36, BaseColor.RED);
                                Paragraph title = new Paragraph("Liste des événements", titleFont);
                                title.setAlignment(Element.ALIGN_CENTER);
                                document.add(title);

                                // Espacement
                                document.add(Chunk.NEWLINE);

                                // Ajouter la date système
                                Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
                                Paragraph date = new Paragraph("Date : " + LocalDate.now().toString() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), dateFont);
                                date.setAlignment(Element.ALIGN_RIGHT);
                                document.add(date);

                                // Espacement
                                document.add(Chunk.NEWLINE);

                                PdfPTable table = new PdfPTable(eventsTable.getColumns().size() + 1); // Ajouter une colonne pour les images
                                table.setWidthPercentage(113);

// Ajouter les en-têtes de colonne
                                for (TableColumn<Evenement, ?> column : eventsTable.getColumns()) {
                                        PdfPCell headerCell = new PdfPCell(new Phrase(column.getText()));
                                        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        table.addCell(headerCell);
                                }

                                // Ajouter une en-tête pour la colonne des images
                                PdfPCell imageHeaderCell = new PdfPCell(new Phrase("Image"));
                                imageHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                imageHeaderCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                table.addCell(imageHeaderCell);


                                // Ajouter les données de la table
                                for (Evenement evenement : eventsTable.getItems()) {
                                        table.addCell(evenement.getTitre());
                                        table.addCell(evenement.getAdresse());
                                        table.addCell(evenement.getDate().toString());
                                        table.addCell(evenement.getCategorie().getNom());
                                        table.addCell(evenement.getFormat().getNom());
                                        table.addCell(evenement.getDescription());
                                        table.addCell(String.valueOf(evenement.getNbPlaces()));
                                        table.addCell(evenement.getHeure().toString());

                                        try {
                                                Image image = Image.getInstance(evenement.getImage());
                                                PdfPCell imageCell = new PdfPCell(image, true);
                                                table.addCell(imageCell);
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                                table.addCell(""); // Ajouter une cellule vide en cas d'erreur
                                        }
                                }



                                document.add(table);

                                System.out.println("PDF créé avec succès.");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        if (document != null) {
                                document.close();
                        }
                }
        }


        private void editData() {

                titretf.setCellFactory(TextFieldTableCell.forTableColumn());
                titretf.setOnEditCommit(event -> {
                        Evenement evenement = event.getRowValue();
                        String newTitre = event.getNewValue();
                        if (newTitre.isEmpty()) {
                                displayAlert("Error", "Titre should not be empty.");
                                return;
                        }

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Do you want to update this event?");
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                                evenement.setTitre(newTitre);
                                evenementService.update(evenement);

                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("Success");
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("Event updated successfully.");
                                successAlert.showAndWait();
                        }
                });


                adressetf.setCellFactory(TextFieldTableCell.forTableColumn());
                adressetf.setOnEditCommit(event -> {
                        Evenement evenement = event.getRowValue();
                        String newAdresse = event.getNewValue();
                        if (newAdresse.isEmpty()) {
                                displayAlert("Error", "Adresse should not be empty.");

                                return;
                        }
                        evenement.setAdresse(newAdresse);
                        evenementService.update(evenement);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Event updated successfully.");
                        alert.showAndWait();
                });

                descriptiontf.setCellFactory(TextFieldTableCell.forTableColumn());
                descriptiontf.setOnEditCommit(event -> {
                        Evenement evenement = event.getRowValue();
                        String newDescription = event.getNewValue();
                        if (newDescription.isEmpty()) {
                                displayAlert("Error", "Description should not be empty.");

                                return;
                        }
                        evenement.setDescription(newDescription);
                        evenementService.update(evenement);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Event updated successfully.");
                        alert.showAndWait();
                });

                formatEvetf.setCellFactory(TextFieldTableCell.forTableColumn());
                formatEvetf.setOnEditCommit(event -> {
                        Evenement evenement = event.getRowValue();
                        evenement.setFormat(formatService.readByName(event.getNewValue()));
                        evenementService.update(evenement);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Event updated successfully.");
                        alert.showAndWait();
                });

                categorieEvetf.setCellFactory(TextFieldTableCell.forTableColumn());
                categorieEvetf.setOnEditCommit(event -> {
                        Evenement evenement = event.getRowValue();
                        evenement.setCategorie(categorieService.readByName(event.getNewValue()));
                        evenementService.update(evenement);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Event updated successfully.");
                        alert.showAndWait();
                });


                datetf.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
                        @Override
                        public String toString(Date object) {
                                return object != null ? object.toString() : "";
                        }

                        @Override
                        public Date fromString(String string) {
                                try {
                                        return string != null && !string.isEmpty() ? Date.valueOf(string) : null;
                                } catch (IllegalArgumentException e) {
                                        // Handle invalid date format
                                        return null;
                                }
                        }
                }));
                datetf.setOnEditCommit(event -> {
                        Evenement evenement = event.getRowValue();
                        evenement.setDate(Date.valueOf(event.getNewValue().toLocalDate()).toLocalDate());
                        evenementService.update(evenement);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Event updated successfully.");
                        alert.showAndWait();
                });

                heuretf.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Time>() {
                        @Override
                        public String toString(Time object) {
                                return object != null ? object.toString() : "";
                        }

                        @Override
                        public Time fromString(String string) {
                                return string != null && !string.isEmpty() ? Time.valueOf(string) : null;
                        }
                }));
                heuretf.setOnEditCommit(event -> {
                        Evenement evenement = event.getRowValue();
                        evenement.setHeure(Time.valueOf(event.getNewValue().toLocalTime()));
                        evenementService.update(evenement);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Event updated successfully.");
                        alert.showAndWait();
                });



                nbPlacestf.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                nbPlacestf.setOnEditCommit(event -> {
                        Evenement evenement = event.getTableView().getItems().get(event.getTablePosition().getRow());
                        Integer newNbPlaces = event.getNewValue();
                        if (newNbPlaces == null || newNbPlaces == 0) {
                                displayAlert("Error", "Number of places should not be empty or zero.");
                                return;
                        }
                        evenement.setNbPlaces(newNbPlaces);
                        evenementService.update(evenement);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Event updated successfully.");
                        alert.showAndWait();
                });
        }


        @FXML
        void getEventList(ActionEvent event) {
                // Define action to perform when Gestion Evénement button is clicked
        }



        @FXML
        public void initialize() {
                titretf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
                adressetf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
                datetf.setCellValueFactory(cellData -> {
                        Date date = cellData.getValue().getDate();
                        return new SimpleObjectProperty<>(date);
                });
                categorieEvetf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie().getNom()));
                formatEvetf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormat().getNom()));
                //imagetf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImage()));
                descriptiontf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
                nbPlacestf.setCellValueFactory(cellData -> {
                        int nbPlaces = cellData.getValue().getNbPlaces();
                        return new SimpleObjectProperty<>(nbPlaces);
                });
                heuretf.setCellValueFactory(cellData -> {
                        Time heure = cellData.getValue().getHeure();
                        return new SimpleObjectProperty<>(heure);
                });

                // Fetch data from the database and populate the TableView
                EvenementService evenementService = new EvenementService();
                List<Evenement> evenements = evenementService.getAll();
                ObservableList<Evenement> evenementsList = FXCollections.observableArrayList(evenements);
                eventsTable.setItems(evenementsList);
                editData();
        }

        @FXML
        public void showImage() {
                Evenement selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
                if (selectedEvent == null || selectedEvent.getImage() == null || selectedEvent.getImage().isEmpty()) {
                        return;
                }
                try {
                        String imageUrl = selectedEvent.getImage();
                        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                                File imageFile = new File(imageUrl);
                                if (imageFile.exists()) {
                                        imageUrl = imageFile.toURI().toURL().toString();
                                } else {
                                        imageUrl = "C:/Users/LENOVO/Documents/piDevJava/src/main/resources/img" + imageUrl;

                                        imageFile = new File(imageUrl.replace("file:///", ""));
                                        if (!imageFile.exists()) {
                                                System.err.println("Fichier d'image introuvable ");
                                                return;
                                        }
                                }
                        }

                        javafx.scene.image.Image image = new javafx.scene.image.Image(imageUrl, 367, 314, false, true);
                        imageView.setImage(image);

                } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image ");
                        e.printStackTrace();
                }
        }



        public void getCatList(ActionEvent event) {
                try {
                        // Get the current stage
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        // Load the new FXML file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AfficherCategorieEve.fxml"));
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

    public void getFormatsList(ActionEvent event) {

        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AfficherFormat.fxml"));
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

        public void getPieChartEvent(ActionEvent event) {

                try {
                        // Charger le fichier FXML de la nouvelle interface
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/PieChartEvent.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur associé au fichier FXML chargé
                        StatistiqueEventController controller = loader.getController();

                        // Remplir les données du PieChart et le configurer
                        Map<Category, Long> dataByCategory = evenementService.getDataByCategory();
                        controller.setPieChartData(dataByCategory);

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Événements par Catégorie");
                        stage.setScene(scene);

                        // Afficher la nouvelle interface
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void getBarChartEvent(ActionEvent event) {

                try {
                        // Charger le fichier FXML de la nouvelle interface
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/BarChartEvent.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur associé au fichier FXML chargé
                        StatistiqueEventController controller = loader.getController();

                        // Remplir les données du BarChart et le configurer
                        Map<Integer, Long> dataByYear = evenementService.getDataByYear();
                        controller.setBarChartData(dataByYear);

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Événements par Année");
                        stage.setScene(scene);

                        // Afficher la nouvelle interface
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void getEventDetails(ActionEvent event) {

                Evenement selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
                if (selectedEvent != null) {
                        int eventId = selectedEvent.getId();
                        try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/showDetailsEvent.fxml"));
                                Parent root = loader.load();
                                detailsEvent controller = loader.getController();
                                controller.afficherDetailsEvent(eventId);
                                Scene scene = new Scene(root);


                                Stage mainWindow = (Stage) eventsTable.getScene().getWindow();

                                Stage dialogStage = new Stage();
                                dialogStage.setScene(scene);
                                dialogStage.setTitle("Afficher Evénement");
                                dialogStage.initModality(Modality.WINDOW_MODAL);
                                dialogStage.initOwner(mainWindow);
                                dialogStage.showAndWait();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

    public void SearchEvenets(ActionEvent event) {
            String nom = rechercherTF.getText().trim(); // Trim whitespace

            List<Evenement> searchResult;
            if (nom.isEmpty()) {
                    searchResult = evenementService.getAll();
            } else {
                    searchResult = evenementService.getByName(nom);
            }

            if (searchResult.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Échec");
                    alert.setHeaderText(null);
                    alert.setContentText(" Aucun événement correspondant au terme de recherche n'a été trouvé.");
                    alert.showAndWait();
            } else {
                    eventsTable.getItems().clear();
                    eventsTable.getItems().addAll(searchResult);
            }
    }

}

