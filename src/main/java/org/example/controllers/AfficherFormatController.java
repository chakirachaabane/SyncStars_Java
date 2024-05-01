package org.example.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.Category;
import org.example.model.Format;
import org.example.services.CategoryService;
import org.example.services.FormatService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AfficherFormatController {


    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Format> formatsTable;



    @FXML
    private TableColumn<Format, String> nomtf;



    private FormatService fs = new FormatService();

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
                Paragraph title = new Paragraph("Liste des formats", titleFont);
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
                PdfPTable table = new PdfPTable(formatsTable.getColumns().size());
                table.setWidthPercentage(100);

                // Ajouter les en-têtes de colonne
                for (TableColumn<Format, ?> column : formatsTable.getColumns()) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(column.getText()));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(headerCell);
                }

                // Ajouter les données de la table
                for (Format f : formatsTable.getItems()) {
                    table.addCell(f.getNom());
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

    public void DeleteAction(ActionEvent actionEvent) {
        // Get the selected item from the TableView
        Format selectedEvent = formatsTable.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Remove the selected item from the TableView
            formatsTable.getItems().remove(selectedEvent);

            // Call the service method to delete the selected event from the database
            fs.delete(selectedEvent.getId());

            // Display a success alert
            displayAlert("Success", "Format deleted successfully.");
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

    public void getEventList(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AfficherEvenement.fxml"));
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

    public void Refresh(ActionEvent actionEvent) {
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



    private void editData() {

        nomtf.setCellFactory(TextFieldTableCell.forTableColumn());
        nomtf.setOnEditCommit(event -> {
            Format F = event.getRowValue();
            String newTitre = event.getNewValue();
            if (newTitre.isEmpty()) {

                displayAlert("Error", "Titre should not be empty.");
                return;
            }
            F.setNom(newTitre);
            fs.update(F);
        });

    }

    @FXML
    public void initialize() {
        nomtf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        // Fetch data from the database and populate the TableView
        FormatService fs = new FormatService();
        List<Format> formats = fs.readAll();
        ObservableList<Format> formatsList = FXCollections.observableArrayList(formats);
        formatsTable.setItems(formatsList);
        editData();
    }

    public void getcatList(ActionEvent event) {

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

    public void getAddFormat(ActionEvent event) {

        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AjouterFormat.fxml"));
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
