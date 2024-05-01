package controllers;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import javafx.event.ActionEvent;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.entrainements;
import services.EntrainementServices;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.exercices;
import controllers.getExercice;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import com.itextpdf.text.*;




import java.sql.SQLException;

public class getEntrainement {
    @FXML
    private TableView<exercices> tableExercices;
    @FXML
    private TextField rechercheTF;

    @FXML
    private Button pdfBTN;


    @FXML
    private TableView<entrainements> tableEntrainements;
    private final EntrainementServices entrainementServices = new EntrainementServices();

    @FXML
    public void initialize() throws SQLException {
        TableColumn<entrainements, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());


        TableColumn<entrainements, String> niveauColumn = new TableColumn<>("Niveau");
        niveauColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNiveau()));

        TableColumn<entrainements, Integer> dureeColumn = new TableColumn<>("Durée");
        dureeColumn.setCellValueFactory(cellData ->new SimpleIntegerProperty(cellData.getValue().getDuree()).asObject());

        TableColumn<entrainements, String> objectifColumn = new TableColumn<>("Objectif");
        objectifColumn.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getObjectif()));

        TableColumn<entrainements, String> nomColumn = new TableColumn<>("Nom de l'entraînement");
        nomColumn.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getNom_entrainement()));

        TableColumn<entrainements, Integer> periodeColumn = new TableColumn<>("Période");
        periodeColumn.setCellValueFactory(cellData ->new SimpleIntegerProperty(cellData.getValue().getPeriode()).asObject());

        TableColumn<entrainements, Void> actionCol = new TableColumn<>("Actions");
            actionCol.setPrefWidth(100);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button exercicesBtn = new Button("Exercices");
            private final Button deleteButton = new Button("Supprimer");


            {
                editButton.setOnAction(event -> handleModifier(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleSupprimer(getTableView().getItems().get(getIndex())));
                exercicesBtn.setOnAction(event -> handleExercices(getTableView().getItems().get(getIndex())));

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(exercicesBtn,editButton, deleteButton);
                    container.setSpacing(10);
                    setGraphic(container);
                }
            }
        });

        tableEntrainements.getColumns().addAll(idColumn, niveauColumn, dureeColumn, objectifColumn, nomColumn, periodeColumn, actionCol);
        loadEntrainements();
        recherche_avance();

    }

    private void loadEntrainements() {
        try {
            tableEntrainements.getItems().addAll(entrainementServices.getAll());
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des entraînements : " + e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }
    private void handleExercices(entrainements entrainement) {
        try {
            // Validate fields and create nouvelEntrainement...

            // Add nouvelEntrainement using entrainementServices...

            // Close current window
            Stage stage = (Stage) tableEntrainements.getScene().getWindow();
            stage.close();

            // Load getEntrainement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/getExercice.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Pass nouvelEntrainement to the loaded controller
            getExercice controller = loader.getController();
            controller.setIdEntrainemnt(entrainement.getId());
            //controller.initData(entrainementServices.getById(entrainement.getId()));

            System.out.println("Entraînement ajouté avec succès!");
        } catch (Exception e) {
            // showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Période.");
        }
    }
    private void handleModifier(entrainements entrainement) {
        try {
            // Validate fields and create nouvelEntrainement...

            // Add nouvelEntrainement using entrainementServices...

            // Close current window
            Stage stage = (Stage) tableEntrainements.getScene().getWindow();
            stage.close();

            // Load getEntrainement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierEntrainement.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Pass nouvelEntrainement to the loaded controller
            modifEntrainementController controller = loader.getController();
            controller.initData(entrainement);

            System.out.println("Entraînement ajouté avec succès!");
        } catch (Exception e) {
            // showAlert("Erreur de saisie", "Veuillez saisir des valeurs numériques valides pour Durée et Période.");


        }
    }


    private void handleSupprimer(entrainements entrainement) {
        try {
            entrainementServices.delete(entrainement.getId());
            tableEntrainements.getItems().remove(entrainement);  // Mettre à jour l'interface utilisateur
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }


    @FXML
    private void onEntrainementSelected() {
        entrainements selectedEntrainement = tableEntrainements.getSelectionModel().getSelectedItem();
        if (selectedEntrainement != null) {
            // Charger et afficher les exercices associés à l'entraînement sélectionné
            List<exercices> exercices = selectedEntrainement.getExercices();

            // Mettre à jour le TableView des exercices avec la liste d'exercices chargée
            tableExercices.getItems().setAll(exercices);
        }
    }

    @FXML
    private void ouvrirFormulaireAjoutExercice() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addExercice.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un exercice");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    void recherche_avance() throws SQLException {
        ObservableList<entrainements> data= FXCollections.observableArrayList(entrainementServices.getAll());
        FilteredList<entrainements> filteredList=new FilteredList<>(data, u->true);
        rechercheTF.textProperty().addListener(
                (observable,oldValue,newValue)->{
                    filteredList.setPredicate(u->{
                        if(newValue.isEmpty()|| newValue==null){
                            return true;
                        }
                        if(u.getNom_entrainement().toLowerCase().contains(newValue.toLowerCase())){
                            return true;
                        } else if (u.getObjectif().toLowerCase().contains(newValue.toLowerCase())) {
                            return true;
                        } else if (u.getNom_entrainement().toLowerCase().contains(newValue.toLowerCase())) {
                            return true;

                        }
                        else{
                            return false;
                        }
                    });
                    tableEntrainements.getItems().setAll(filteredList);
                }
        );
    }

    @FXML
    private void pdf(ActionEvent event) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("ListeDesEntrainements.pdf"));
            document.open();

            PdfPTable table = new PdfPTable(6); // Modifier selon le nombre de colonnes souhaité
            table.setWidthPercentage(100);

            // Ajout des titres des colonnes
            String[] headers = {"ID", "Nom", "Niveau", "Durée", "Objectif", "Période"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Récupération et ajout des données des entraînements
            List<entrainements> allEntrainements = entrainementServices.getAll();
            for (entrainements ent : allEntrainements) {
                table.addCell(String.valueOf(ent.getId()));
                table.addCell(ent.getNom_entrainement());
                table.addCell(ent.getNiveau());
                table.addCell(String.valueOf(ent.getDuree()));
                table.addCell(ent.getObjectif());
                table.addCell(String.valueOf(ent.getPeriode()));
            }

            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF créé");
            alert.setHeaderText(null);
            alert.setContentText("Le fichier PDF a été créé avec succès !");
            alert.showAndWait();
        } catch (FileNotFoundException | DocumentException | SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur de création du PDF");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Erreur lors de la création du PDF : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }




}
