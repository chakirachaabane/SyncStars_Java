package tn.esprit.controllers;


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
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.TimeStringConverter;
import tn.esprit.models.*;
import tn.esprit.services.EvenementService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        private ImageView imageViewUser;
        @FXML
        private Label userNameTf;
        @FXML
        private ComboBox comboBoxUser;

        @FXML
        private TableColumn<Evenement, String> imagetf;

        @FXML
        private TableColumn<Evenement, String> descriptiontf;

        @FXML
        private TableColumn<Evenement, Integer> nbPlacestf;

        @FXML
        private TableColumn<Evenement, Time> heuretf;

        private Stage stage;
        private Scene scene;
        private Parent pt;

        @FXML
        private Button pieBtn;

        @FXML
        private ImageView imageView;

        @FXML
        private TextField  rechercherTF;

        private EvenementService evenementService = new EvenementService();

        @FXML
        void getAddEventView(ActionEvent event) {
                try {
                        // Get the current stage
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        // Load the new FXML file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenement.fxml"));
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
                                                String imagePath = evenement.getImage();
                                                if (imagePath != null && !imagePath.isEmpty()) {
                                                        if (!imagePath.startsWith("http://") && !imagePath.startsWith("https://")) {
                                                                File imageFile = new File(imagePath);
                                                                if (!imageFile.exists()) {
                                                                        imagePath = "file:///C:/Users/LENOVO/OneDrive - ESPRIT/Images/integration chakira +nawres+feten+azza+aziz/integration chakira +nawres+feten+azza - Copie2/integration chakira +nawres+feten/SecondProject1/public/FrontOffice/img/" + imagePath;

                                                                        imageFile = new File(imagePath.replace("file:///", ""));
                                                                        if (!imageFile.exists()) {
                                                                                System.err.println("Fichier d'image introuvable: " + imagePath);
                                                                                table.addCell("Image non trouvée");
                                                                                continue;
                                                                        }
                                                                }
                                                        }

                                                        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imagePath);
                                                        image.scaleToFit(100, 100); // Redimensionner l'image pour s'adapter à la cellule
                                                        PdfPCell imageCell = new PdfPCell(image, true);
                                                        imageCell.setFixedHeight(100); // Définir la hauteur de la cellule
                                                        imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                        imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                        table.addCell(imageCell);
                                                } else {
                                                        table.addCell("Pas d'image");
                                                }
                                        } catch (Exception e) {
                                                e.printStackTrace();
                                                table.addCell("Erreur d'image");
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


        private void showUserAdminSwitch() {
                try {
                        pt = FXMLLoader.load(getClass().getResource("/showUserAdmin-view.fxml"));
                        stage = new Stage();
                        scene = new Scene(pt);
                        // Close the current stage
                        Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
                        currentStage.close();
                        stage.setTitle("User infos");
                        stage.setScene(scene);
                        stage.show();

                } catch (IOException ex) {
                        Logger.getLogger(ShowUserAdminController.class
                                .getName()).log(Level.SEVERE, null, ex);
                }
        }
        @FXML
        private void addAdminSwitch() {
                try {
                        pt = FXMLLoader.load(getClass().getResource("/addUserAdmin-view.fxml"));
                        stage = new Stage();
                        scene = new Scene(pt);
                        // Close the current stage
                        Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
                        currentStage.close();
                        stage.setTitle("Ajouter un admin");
                        stage.setScene(scene);
                        stage.show();

                } catch (IOException ex) {
                        Logger.getLogger(AddUserAdminController.class
                                .getName()).log(Level.SEVERE, null, ex);
                }
        }

        private void clearEmailFileContent() {
                try {
                        // Get the path to the email.txt file in the resources directory
                        Path filePath = Paths.get(getClass().getResource("/email.txt").toURI());

                        // Write an empty string to the file to clear its content
                        Files.write(filePath, "".getBytes(StandardCharsets.UTF_8));

                        System.out.println("File content cleared successfully.");
                } catch (IOException e) {
                        System.err.println("Error clearing email file content: " + e.getMessage());
                } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                }
        }

        private void loginSwitch() {
                try {
                        pt = FXMLLoader.load(getClass().getResource("/signIn-view.fxml"));
                        stage = new Stage();
                        scene = new Scene(pt);
                        // Close the current stage
                        Stage currentStage = (Stage) comboBoxUser.getScene().getWindow(); // Assuming comboBox is part of your current scene
                        currentStage.close();
                        stage.setScene(scene);
                        stage.show();

                } catch (IOException ex) {
                        Logger.getLogger(SignUpController.class
                                .getName()).log(Level.SEVERE, null, ex);
                }

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

                String imagePath = "file:\\C:\\Users\\LENOVO\\OneDrive - ESPRIT\\Images\\integration chakira +nawres+feten+azza+aziz\\integration chakira +nawres+feten+azza - Copie2\\integration chakira +nawres+feten\\SecondProject1\\public\\FrontOffice\\img\\"+ Data.user.getImage();
                // Load the image
                javafx.scene.image.Image image = new javafx.scene.image.Image(imagePath);
                // Set the image to the ImageView
                imageViewUser.setImage(image);

                // Ajout d'éléments à la liste déroulante
                comboBoxUser.getItems().addAll( "Gérer mon compte","Se déconnecter");

                comboBoxUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && newValue.equals("Gérer mon compte")) {
                                showUserAdminSwitch();
                                System.out.println("Modifier profile is selected!");
                        }

                        if (newValue != null && newValue.equals("Se déconnecter")) {
                                System.out.println("Se deconnecter is selected!");
                                Data.user=null;
                                //Session
                                clearEmailFileContent();
                                Data.currentUserMail="";

                                loginSwitch();
                        }
                });

                userNameTf.setText(Data.user.getFirstname()+" "+Data.user.getLastname());

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
                                        imageUrl = "file:///C:/Users/LENOVO/OneDrive - ESPRIT/Images/integration chakira +nawres+feten+azza+aziz/integration chakira +nawres+feten+azza - Copie2/integration chakira +nawres+feten/SecondProject1/public/FrontOffice/img/" + imageUrl;

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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategorieEve.fxml"));
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
        public void ConsulterRDVButtonAction(ActionEvent actionEvent) {
                try {
                        // Charger rdvListBack.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvListBack.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);

                        // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                        // Afficher la nouvelle scène dans la même fenêtre
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace(); // Gérer l'exception en conséquence
                }
        }

        @FXML
        public void handleConsulterListeRecettesButtonAction(ActionEvent actionEvent) {
                try {
                        // Charger le fichier FXML de la nouvelle vue
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteList.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène avec la vue chargée
                        Scene scene = new Scene(root);

                        // Obtenir la fenêtre principale (stage) à partir de l'événement
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                        // Mettre la nouvelle scène dans la fenêtre principale
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        @FXML
        public void handleAjouterRecettesButtonAction(ActionEvent actionEvent) {
                try {
                        // Charger le fichier FXML de la nouvelle vue
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/recetteAdd.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène avec la vue chargée
                        Scene scene = new Scene(root);

                        // Obtenir la fenêtre principale (stage) à partir de l'événement
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                        // Mettre la nouvelle scène dans la fenêtre principale
                        stage.setScene(scene);
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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormat.fxml"));
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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PieChartEvent.fxml"));
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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BarChartEvent.fxml"));
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
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/showDetailsEvent.fxml"));
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


        public void handleListeExerciceButtonAction(ActionEvent event) {

                try {
                        // Get the current stage
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        // Load the new FXML file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/getExercice.fxml"));
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
        public void allerVersUser(ActionEvent actionEvent) {
                try {
                        // Charger recetteList.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayUsers-view.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);

                        // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                        // Afficher la nouvelle scène dans la même fenêtre
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace(); // Gérer l'exception en conséquence
                }
        }

        @FXML
        public void allerAjouterUser(ActionEvent actionEvent) {
                try {
                        // Charger recetteList.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addUserAdmin-view.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);

                        // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                        // Afficher la nouvelle scène dans la même fenêtre
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace(); // Gérer l'exception en conséquence
                }
        }


        @FXML
        public void allerVersProduits(ActionEvent actionEvent) {
                try {
                        // Charger recetteList.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayProduit.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);

                        // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                        // Afficher la nouvelle scène dans la même fenêtre
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace(); // Gérer l'exception en conséquence
                }
        }


        @FXML
        public void allerAjouterProduits(ActionEvent actionEvent) {
                try {
                        // Charger recetteList.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addProduit.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);

                        // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                        // Afficher la nouvelle scène dans la même fenêtre
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace(); // Gérer l'exception en conséquence
                }
        }

        @FXML
        public void allerListCat(ActionEvent actionEvent) {
                try {
                        // Charger recetteList.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayCategorie.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);

                        // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                        // Afficher la nouvelle scène dans la même fenêtre
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace(); // Gérer l'exception en conséquence
                }
        }

        @FXML
        public void allerAjouterCat(ActionEvent actionEvent) {
                try {
                        // Charger recetteList.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCategorie.fxml"));
                        Parent root = loader.load();

                        // Créer une nouvelle scène
                        Scene scene = new Scene(root);

                        // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                        // Afficher la nouvelle scène dans la même fenêtre
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace(); // Gérer l'exception en conséquence
                }
        }


        public void EntrainementPath(ActionEvent event) {
                try {
                        // Get the current stage
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        // Load the new FXML file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEntrainement.fxml"));
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

