package tn.esprit.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;

import java.io.File;
import java.io.IOException;


public class HistoriquePanierController {

    @FXML
    private TableColumn<Produit,Integer> QuantiteProduit;

    @FXML
    private TableView<Produit> historiqueTableView;

    @FXML
    private TableColumn<Produit,String> nomProduit;
    @FXML
    private TableColumn<Produit,Float> total;
    @FXML
    private TableColumn<Produit,String> imageProduit;
    @FXML
    private Button btnSupprimer;

    @FXML
    private Label totalLabel;

    @FXML
    private TableColumn<Produit,Float> totalProduit;
    private final ProduitService ps = new ProduitService();
    @FXML
    public void initialize() {
      btnSupprimer.setDisable(true);
        historiqueTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                btnSupprimer.setDisable(true);
            } else {
                btnSupprimer.setDisable(false);
            }
        });
        refreshTableView();

        float totalPanier=0;
        ObservableList<Produit> produits = ps.getAllProduitsPanier();
        for (Produit produit : produits) {
            float totalProduit = produit.getPrix() * produit.getQuantite();
            produit.setTotalProduits(totalProduit);
            totalPanier+=totalProduit;
        }
        totalLabel.setText(String.valueOf(totalPanier) + " DT");
        imageProduit.setCellFactory(column -> {
            return new TableCell<Produit, String>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    setGraphic(imageView);
                }

                @Override
                protected void updateItem(String imageUrl, boolean empty) {
                    super.updateItem(imageUrl, empty);
                    if (empty || imageUrl == null) {
                        imageView.setImage(null);
                        setGraphic(null);
                    } else {
                        try {
                            File imageFile = new File(imageUrl);
                            if (imageFile.exists()) {
                                imageUrl = imageFile.toURI().toURL().toString();
                            } else {
                                imageUrl = "file:///C:/Users/Nawres/SecondProject/public/FrontOffice/img/" + imageUrl;
                                imageFile = new File(imageUrl.replace("file:///", ""));
                                if (!imageFile.exists()) {
                                    System.err.println("Fichier d'image introuvable : " + imageUrl);
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Erreur");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Fichier d'image introuvable : " + imageUrl);
                                    alert.showAndWait();
                                    return;
                                }
                            }
                            Image image = new Image(imageUrl, 50, 50, true, true);
                            imageView.setImage(image);
                            setGraphic(imageView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        });

    }

    public void refreshTableView() {
        ObservableList<Produit> produits = ps.getAllProduitsPanier();
        for (Produit produit : produits) {
            float totalProduit = produit.getPrix() * produit.getQuantite();
            produit.setTotalProduits(totalProduit);
        }
        historiqueTableView.setItems(produits);
    }
    private  void loadPage(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            if (fxmlFile.equals("/paiement.fxml")) {
                stage.setTitle("Paiement");
            }
            if (fxmlFile.equals("/displayFrontProduit.fxml")) {
                stage.setTitle("Produits");
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void payerPanier(ActionEvent event) {
        loadPage("/paiement.fxml", event);
    }

    @FXML
    void retourPanier(ActionEvent event) {
        loadPage("/displayFrontProduit.fxml", event);
    }
    @FXML
    void supprimerPanier(ActionEvent event) {
        Produit produit = historiqueTableView.getSelectionModel().getSelectedItem();
        if (produit != null) {
            int produitId = produit.getId();
            ps.removeFromPanier(produitId);
            refreshTableView();
            updateTotalLabel();
        }
    }
    private void updateTotalLabel() {
        float totalPanier = 0;
        ObservableList<Produit> produits = ps.getAllProduitsPanier();
        for (Produit produit : produits) {
            float totalProduit = produit.getPrix() * produit.getQuantite();
            produit.setTotalProduits(totalProduit);
            totalPanier += totalProduit;
        }
        totalLabel.setText(String.valueOf(totalPanier) + " DT");
    }
}
