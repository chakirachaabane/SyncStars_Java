package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class panierController {

    @FXML
    private Label prixTotalLabel;

    @FXML
    private Label produitLabel;
    @FXML
    private Label prixLabel;

    @FXML
    private Label quantiteLabel;

    @FXML
    private Label totalPanierLabel;

    @FXML
    private Button decrementButton;

    @FXML
    private Button incrementButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button PanierButton;
    private final ProduitService ps = new ProduitService();
    private Produit currentProduit;

    public void initData(Produit produit) {
        this.currentProduit = produit;
       ps.addToPanier(produit);
        produitLabel.setText(produit.getNomp());
        prixLabel.setText(String.valueOf(produit.getPrix()) + " DT");
        try {
            String imageUrl = produit.getImage();
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    imageUrl = imageFile.toURI().toURL().toString();
                } else {
                    imageUrl = "file:///C:/Users/LENOVO/OneDrive - ESPRIT/Images/integration chakira +nawres+feten+azza+aziz/integration chakira +nawres+feten+azza - Copie2/integration chakira +nawres+feten/SecondProject1/public/FrontOffice/img/" + imageUrl;

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
            }

            Image image = new Image(imageUrl, 367, 314, false, true);
            imageView.setImage(image);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du chargement de l'image : " + e.getMessage());
            alert.showAndWait();
        }

        refreshLabels();
    }


    private void refreshLabels() {
        Produit panierProduit = ps.getPanierList()
                .stream()
                .filter(p -> p.getId() == currentProduit.getId())
                .findFirst()
                .orElse(null);
        if (panierProduit != null) {
            quantiteLabel.setText(String.valueOf(panierProduit.getQuantite()));
            prixTotalLabel.setText(String.valueOf(ps.TotalProduit(panierProduit)) + " DT");
            decrementButton.setDisable(panierProduit.getQuantite() <= 1);
        } else {
            decrementButton.setDisable(true);
        }
    }

    @FXML
    public void incrementQuantityPanier(ActionEvent event) {
        if (currentProduit != null) {
            ps.incrementQuantity(currentProduit.getId());
            refreshLabels();
        }
    }

    @FXML
    void decrementQuantityPanier(ActionEvent event) {
        if (currentProduit != null) {
            ps.decrementQuantity(currentProduit.getId());
            refreshLabels();
        }
    }

    @FXML
    void AjouterPanier(ActionEvent event) {
        Produit panierProduit = ps.getPanierList()
                .stream()
                .filter(p -> p.getId() == currentProduit.getId())
                .findFirst()
                .orElse(null);
        if (panierProduit != null && panierProduit.getQuantite() > panierProduit.getStock()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Stock insuffisant");
            alert.setHeaderText(null);
            alert.setContentText("La quantité demandée est supérieure au stock disponible pour " + panierProduit.getNomp());
            alert.showAndWait();
        } else {
            try {
                FXMLLoader historiqueLoader = new FXMLLoader(getClass().getResource("/historique.fxml"));
                Parent root = historiqueLoader.load();
                Scene currentScene = PanierButton.getScene();
                Stage currentStage = (Stage) currentScene.getWindow();
                currentStage.close();

                HistoriquePanierController historiqueController = historiqueLoader.getController();
                historiqueController.refreshTableView();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Notifications.create()
                    .title("Succès")
                    .text("Produit ajouté à votre panier !")
                    .hideAfter(Duration.seconds(2.5))
                    .position(Pos.BOTTOM_RIGHT)
                    .graphic(new ImageView(new Image("/images/tik1.png")))
                    .show();
        }
    }
    }


