package tn.esprit.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tn.esprit.models.Categorie;
import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.time.LocalDateTime;




import java.awt.*;
import java.io.File;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;


public class displayProduit implements Initializable {
    private final ProduitService ps = new ProduitService();


    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;
    @FXML
    private ImageView imageView;
    @FXML
    private TableColumn<Produit, LocalDate> colDatep;

    @FXML
    private TableColumn<Produit,LocalDate> colDatepr;

    @FXML
    private TableColumn<Produit,String> colDescription;
    @FXML
    private TableColumn<Produit,String> colNom;

    @FXML
    private TableColumn<Produit, Integer> colPrix;

    @FXML
    private TableColumn<Produit,Integer> colStock;


    @FXML
    private TableColumn<Produit,String> colCategorie;
    @FXML
    private Button btnExport;
    @FXML
    private Button btnDetails;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private Button sidebarCategorie;

    @FXML
    private Button sidebarProduit;


    @FXML
    private TableView<Produit> tableP;
    @FXML
    private Button rechercher;

    @FXML
    private TextField rechercherTF;

    @FXML
    private ComboBox<String> nomCategorie;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<String> categoryNames = this.ps.getAllCategories();
        this.nomCategorie.getItems().addAll(categoryNames);
        showProduits();
        btnSupprimer.setDisable(true);
        btnDetails.setDisable(true);
        btnModifier.setDisable(true);
        tableP.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                btnSupprimer.setDisable(true);
                btnModifier.setDisable(true);
                btnDetails.setDisable(true);
            } else {
                btnSupprimer.setDisable(false);
                btnModifier.setDisable(false);
                btnDetails.setDisable(false);
            }
        });
    }
    @FXML
    void openUpdateProduitDialog() {
        Alert alert;
        Produit produitSelect = tableP.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateProduit.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier produit");
                updateProduit controller = loader.getController();
                controller.getData(produitSelect);
                stage.setOnHiding(event -> {
                    showProduits();
                });
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    public void showProduits() {

        ObservableList<Produit> produitList = ps.getAllProduits();
        tableP.setItems(produitList);
        //colid.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("id"));
//        colImage.setCellValueFactory(new PropertyValueFactory<Produit, String>("image"));
        colNom.setCellValueFactory(new PropertyValueFactory<Produit, String>("nomp"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Produit, String>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("prix"));
        colDatep.setCellValueFactory(new PropertyValueFactory<Produit,LocalDate>("datePeremption"));
        colStock.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("stock"));
        colDatepr.setCellValueFactory(new PropertyValueFactory<Produit,LocalDate>("dateProduction"));
        colCategorie.setCellValueFactory(cellData -> {
            Produit produit = cellData.getValue();
            int idCategorie = produit.getCategorie().getIdCategorie();
            String nomCategorie = ps.getNomCategorieById(idCategorie);
            return new SimpleStringProperty(nomCategorie);
        });
    }
    @FXML
    public void showImage() {
        Produit selectedProduit = tableP.getSelectionModel().getSelectedItem();
        if (selectedProduit == null || selectedProduit.getImage() == null || selectedProduit.getImage().isEmpty()) {
            return;
        }
        try {
            String imageUrl = selectedProduit.getImage();
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    imageUrl = imageFile.toURI().toURL().toString();
                } else {
                    imageUrl = "file:///C:/Users/Nawres/SecondProject/public/FrontOffice/img/" + imageUrl;

                    imageFile = new File(imageUrl.replace("file:///", ""));
                    if (!imageFile.exists()) {
                        System.err.println("Fichier d'image introuvable ");
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Erreur");
//                        alert.setHeaderText(null);
//                        alert.setContentText("Fichier d'image introuvable : " + imageUrl);
//                        alert.showAndWait();
                        return;
                    }
                }
            }

            Image image = new Image(imageUrl, 367, 314, false, true);
            imageView.setImage(image);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image ");
            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erreur");
//            alert.setHeaderText(null);
//            alert.setContentText("Erreur lors du chargement de l'image : " + e.getMessage());
//            alert.showAndWait();
        }
    }
    @FXML
    void deleteProduit() {
        Alert alert;
        Produit produit = tableP.getSelectionModel().getSelectedItem();

        try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Veuillez d'abord sélectionner un produit");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit : " + produit.getNomp() + " ?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.isPresent() && option.get() == ButtonType.OK) {
                    ps.deleteProduit(produit.getId());
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Produit supprimé avec succès !");
                    alert.showAndWait();
                    showProduits();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherDetailsProduits(ActionEvent event) {
        Produit selectedProduit = tableP.getSelectionModel().getSelectedItem();
        if (selectedProduit != null) {
            int productId = selectedProduit.getId();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/showDetailsProduit.fxml"));
                Parent root = loader.load();
                detailsProduit controller = loader.getController();
                controller.afficherDetailsProduit(productId);
                Scene scene = new Scene(root);


                Stage mainWindow = (Stage) tableP.getScene().getWindow();

                Stage dialogStage = new Stage();
                dialogStage.setScene(scene);
                dialogStage.setTitle("Afficher produit");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(mainWindow);
                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private  void loadPage(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Modifier le titre de la scène
            if (fxmlFile.equals("/statistiquesProduit.fxml")) {
                stage.setTitle("Statistiques des produits");
            }  if (fxmlFile.equals("/displayUsers-view.fxml")) {
                stage.setTitle("Liste des Utilisateurs");
            }  if (fxmlFile.equals("/addUserAdmin-view.fxml")) {
                stage.setTitle("Ajouter un Administrateur");
            }
            if (fxmlFile.equals("/usersStatistics-view.fxml")) {
                stage.setTitle("Statistiques des utilisateurs");
            }
            if (fxmlFile.equals("/displayCategorie")) {
                stage.setTitle("Liste des catégories");
            }

            if (fxmlFile.equals("/addCategorie.fxml")) {
                stage.setTitle("Ajouter une catégorie");
            }
            if (fxmlFile.equals("/addProduit.fxml")) {
                stage.setTitle("Ajouter un produit");
            }
//            if (fxmlFile.equals("/displayProduit.fxml")) {
//                stage.setTitle("Listes des produits");
//            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @FXML
//    private void ProduitsButtonAction(ActionEvent event) {
//        loadPage("/displayProduit.fxml", event);
//    }

    @FXML
    private void CategoriesButtonAction(ActionEvent event) {
        loadPage("/displayCategorie.fxml", event);
    }

    @FXML
    void AddProduitSidebar(ActionEvent event) {
        loadPage("/addProduit.fxml", event);
    }

    @FXML
    void AddCategorieSidebar(ActionEvent event) {
        loadPage("/addCategorie.fxml", event);
    }
    @FXML
    void SearchProduits(ActionEvent event) {
        String nom = rechercherTF.getText();
        ObservableList<Produit> produitList = ps.searchProduitsByName(nom);
        tableP.setItems(produitList);

    }

    @FXML
    void exportPDF(ActionEvent event) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        String selectedCategory = this.nomCategorie.getValue();
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Aucune catégorie selectionnée du ComboBox");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.setInitialFileName(selectedCategory + "_Produits.pdf");
        File selectedFile = fileChooser.showSaveDialog(btnExport.getScene().getWindow());

        if (selectedFile == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Aucun fichier selectioné");
            alert.setContentText("Le PDF n'est pas sauvegardé");
            alert.showAndWait();
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDFont font = PDType1Font.HELVETICA_BOLD;
            float fontSize = 10;
            int tableWidth = 750;
            int yOffset = 700;

            contentStream.setNonStrokingColor(Color.orange);
            contentStream.fillRect(0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

            String headerText = "Listes des produits de " + selectedCategory + "  -" + formattedDateTime;
            float titleWidth = font.getStringWidth(headerText) / 1000 * (fontSize + 2);
            float titleX = (PDRectangle.A4.getWidth() - titleWidth) / 2;
            float titleY = yOffset;


            float imageY = titleY - 10;
            PDImageXObject logoImage = PDImageXObject.createFromFile("C:/Users/Nawres/SecondProject/public/FrontOffice/img/logo.png", document);
            contentStream.drawImage(logoImage, 50, imageY, logoImage.getWidth() / 4, logoImage.getHeight() / 4);
            contentStream.beginText();
            contentStream.setFont(font, fontSize + 2);
            contentStream.setStrokingColor(java.awt.Color.BLACK);
            contentStream.setNonStrokingColor(java.awt.Color.BLACK);
            contentStream.newLineAtOffset(titleX, yOffset);
            contentStream.showText(headerText);
            contentStream.endText();
            yOffset -= 40;


            contentStream.drawLine(50, yOffset, 50 + tableWidth, yOffset);

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(50, yOffset - 20);
            contentStream.showText("Nom du produit");
            contentStream.newLineAtOffset(120, 0);
            contentStream.showText("Date de production");
            contentStream.newLineAtOffset(120, 0);
            contentStream.showText("Date de péremption");
            contentStream.newLineAtOffset(120, 0);
            contentStream.showText("Prix");
            contentStream.newLineAtOffset(120, 0);
            contentStream.showText("Stock");
            contentStream.endText();

            yOffset -= 30;

            contentStream.drawLine(50, yOffset, 50 + tableWidth, yOffset);

            List<Produit> produits = ps.getProduitsByCategory(selectedCategory);
            for (Produit produit : produits) {
                contentStream.beginText();

                contentStream.setFont(font, fontSize);
                contentStream.newLineAtOffset(50, yOffset - 15);
                contentStream.showText(produit.getNomp());
                contentStream.newLineAtOffset(120, 0);
                contentStream.showText(produit.getDateProduction().toString());
                contentStream.newLineAtOffset(120, 0);
                contentStream.showText(produit.getDatePeremption().toString());
                contentStream.newLineAtOffset(120, 0);
                contentStream.showText(String.valueOf(produit.getPrix()));
                contentStream.newLineAtOffset(120, 0);
                contentStream.showText(String.valueOf(produit.getStock()));
                contentStream.endText();

                yOffset -= 25;

                contentStream.drawLine(50, yOffset, 50 + tableWidth, yOffset);

                if (yOffset < 50) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yOffset = 700;
                }
            }

            contentStream.close();
            document.save(selectedFile);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("PDF enregistré");
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setText("Le fichier est enregistré :\n" + selectedFile.getAbsolutePath());
            textArea.setPrefWidth(400);
            textArea.setPrefHeight(60);
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("PDF generation or saving error");
            alert.setContentText("An error occurred while generating or saving the PDF: " + ex.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void StatistiquesButton(ActionEvent event) {
        loadPage("/statistiquesProduit.fxml", event);
    }
    @FXML
    void listesUserSwitch(ActionEvent event) {
        loadPage("/displayUsers-view.fxml", event);
    }
    @FXML
    void addAdmin(ActionEvent event) {
        loadPage("/addUserAdmin-view.fxml",event);
    }
    @FXML
    void StatistiquesUser(ActionEvent event) {
        loadPage("/usersStatistics-view.fxml",event);
    }
}



