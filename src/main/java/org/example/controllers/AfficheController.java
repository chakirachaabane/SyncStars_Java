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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.example.model.Evenement;
import org.example.services.EvenementService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        private TableColumn<Evenement, String> descriptiontf;

        @FXML
        private TableColumn<Evenement, Integer> nbPlacestf;


        private EvenementService evenementService = new EvenementService();

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

                                // Créer un tableau
                                PdfPTable table = new PdfPTable(eventsTable.getColumns().size());
                                table.setWidthPercentage(100);

                                // Ajouter les en-têtes de colonne
                                for (TableColumn<Evenement, ?> column : eventsTable.getColumns()) {
                                        PdfPCell headerCell = new PdfPCell(new Phrase(column.getText()));
                                        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        table.addCell(headerCell);
                                }

                                // Ajouter les données de la table
                                for (Evenement evenement : eventsTable.getItems()) {
                                        table.addCell(evenement.getTitre());
                                        table.addCell(evenement.getAdresse());
                                        table.addCell(evenement.getDate().toString());
                                        table.addCell(evenement.getCategorie().getNom());
                                        table.addCell(evenement.getFormat().getNom());
                                        try {
                                                Image image = Image.getInstance(evenement.getImage());
                                                table.addCell(image);
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                                table.addCell(""); // Ajouter une cellule vide en cas d'erreur
                                        }
                                        table.addCell(evenement.getDescription());
                                        table.addCell(String.valueOf(evenement.getNbPlaces()));

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
                        evenement.setTitre(newTitre);
                        evenementService.update(evenement);
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
                imagetf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImage()));
                descriptiontf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
                nbPlacestf.setCellValueFactory(cellData -> {
                        int nbPlaces = cellData.getValue().getNbPlaces();
                        return new SimpleObjectProperty<>(nbPlaces);
                });

                // Fetch data from the database and populate the TableView
                EvenementService evenementService = new EvenementService();
                List<Evenement> evenements = evenementService.getAll();
                ObservableList<Evenement> evenementsList = FXCollections.observableArrayList(evenements);
                eventsTable.setItems(evenementsList);
                editData();
        }




        public void generatePieChart(ActionEvent event) {

                try {
                        // Get the current stage
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        // Load the new FXML file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Statistiques.fxml"));
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
}

