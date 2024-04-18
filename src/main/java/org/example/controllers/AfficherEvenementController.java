package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.example.model.Evenement;
import org.example.utils.MyDataBase;

import java.sql.PreparedStatement;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;


public class AfficherEvenementController {

    @FXML
    private TableView<Evenement> eventsTable;

    @FXML
    private TableColumn<Evenement, String> adressetf;

    @FXML
    private TableColumn<Evenement, String> categorieEvetf;

    @FXML
    private TableColumn<Evenement, Date> datetf;

    @FXML
    private TableColumn<Evenement, String> formatEvetf;

    @FXML
    private TableColumn<Evenement,String> imagetf;

    @FXML
    private TableColumn<Evenement, String> titretf;

    public void initialize() {

        ObservableList<Evenement> evenements = FXCollections.observableArrayList();
        String query = "SELECT * FROM event";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();
                     PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Evenement e = new Evenement(rs.getInt("id"), rs.getString("titre"), rs.getString("adresse"), rs.getDate("date"), rs.getString("categorie_id"), rs.getString("format_id"), rs.getString("image"));
                evenements.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        eventsTable.setItems(evenements);

        titretf.setCellValueFactory(new PropertyValueFactory<>("titre"));
        adressetf.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        datetf.setCellValueFactory(new PropertyValueFactory<>("date"));
        categorieEvetf.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        formatEvetf.setCellValueFactory(new PropertyValueFactory<>("format"));
        imagetf.setCellValueFactory(new PropertyValueFactory<>("image"));

        editData();
    }

    public void getAddEventView(ActionEvent actionEvent) throws IOException {

        Parent page2 = FXMLLoader.load(getClass().getResource("/Fxml/AjouterEvenement.fxml"));

        Scene scene2 = new Scene(page2);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(scene2);
        app_stage.show();
    }

    public void PrintAction(ActionEvent actionEvent) {
    }


    public void getEventList(ActionEvent actionEvent) throws IOException {

        Parent page2 = FXMLLoader.load(getClass().getResource("/Fxml/AfficherEvenement.fxml"));

        Scene scene2 = new Scene(page2);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(scene2);
        app_stage.show();
    }

    public void DeleteAction(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel<Evenement> selectionModel = eventsTable.getSelectionModel();
        if(selectionModel.isEmpty()){
            System.out.println("You need to select a data before deleting.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation de la suppression");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer l'événement sélectionné?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ObservableList<Integer> list = selectionModel.getSelectedIndices();
            Integer[] selectedIndices = new Integer[list.size()];
            selectedIndices = list.toArray(selectedIndices);
            Arrays.sort(selectedIndices);

            for(int i = selectedIndices.length - 1; i >= 0; i--){
                Evenement evenement = eventsTable.getItems().get(selectedIndices[i].intValue());
                selectionModel.clearSelection(selectedIndices[i].intValue());
                eventsTable.getItems().remove(selectedIndices[i].intValue());

                String deleteQuery = "DELETE FROM event WHERE id = ?";
                try {
                    Connection conn = MyDataBase.getInstance().getCnx();
                    PreparedStatement ps = conn.prepareStatement(deleteQuery);
                    ps.setInt(1, evenement.getId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void editData() {

        titretf.setCellFactory(TextFieldTableCell.forTableColumn());
        titretf.setOnEditCommit(event -> {
            Evenement evenement = event.getRowValue();
            String newTitre = event.getNewValue();
            if (newTitre.isEmpty()) {
                System.out.println("Titre should not be empty.");
                return;
            }
            evenement.setTitre(newTitre);
            updateDatabase(evenement);
        });


        adressetf.setCellFactory(TextFieldTableCell.forTableColumn());
        adressetf.setOnEditCommit(event -> {
            Evenement evenement = event.getRowValue();
            String newAdresse = event.getNewValue();
            if (newAdresse.isEmpty()) {
                System.out.println("Adresse should not be empty.");
                return;
            }
            evenement.setAdresse(newAdresse);
            updateDatabase(evenement);
        });

        categorieEvetf.setCellFactory(TextFieldTableCell.forTableColumn());
        categorieEvetf.setOnEditCommit(event -> {
            Evenement evenement = event.getRowValue();
            String newCategorie = event.getNewValue();
            if (newCategorie.isEmpty()) {
                System.out.println("Categorie should not be empty.");
                return;
            }
            evenement.setCategorie(newCategorie);
            updateDatabase(evenement);
        });

        formatEvetf.setCellFactory(TextFieldTableCell.forTableColumn());
        formatEvetf.setOnEditCommit(event -> {
            Evenement evenement = event.getRowValue();
            String newFormat = event.getNewValue();
            if (newFormat.isEmpty()) {
                System.out.println("Format should not be empty.");
                return;
            }
            evenement.setFormat(newFormat);
            updateDatabase(evenement);
        });

        imagetf.setCellFactory(TextFieldTableCell.forTableColumn());
        imagetf.setOnEditCommit(event -> {
            Evenement evenement = event.getRowValue();
            String newImage = event.getNewValue();
            if (newImage.isEmpty()) {
                System.out.println("Image should not be empty.");
                return;
            }
            evenement.setImage(newImage);
            updateDatabase(evenement);
        });
    }

    private void updateDatabase(Evenement evenement) {
        String updateQuery = "UPDATE event SET titre = ?, adresse = ?, categorie_id = ?, format_id = ?, image = ? WHERE id = ?";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getAdresse());
            ps.setString(3, evenement.getCategorie());
            ps.setString(4, evenement.getFormat());
            ps.setString(5, evenement.getImage());
            ps.setInt(6, evenement.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    }
