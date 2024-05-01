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

import java.util.List;

public class serachEntrainementfront {

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
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions accordingly
            }
        }
    }
}
