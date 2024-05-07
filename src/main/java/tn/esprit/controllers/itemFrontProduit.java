package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Produit;
import tn.esprit.models.QrCodeGenerator;
import tn.esprit.services.ProduitService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class itemFrontProduit {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prixLabel;

    @FXML
    private Label descriptionLabel;
    @FXML
    private Button PanierButton;


    private final ProduitService ps = new ProduitService();
    private Produit selectedProduit;


    public void setData(Produit produit) {
        this.selectedProduit = produit;
        nomLabel.setText(produit.getNomp());
        prixLabel.setText(String.valueOf(produit.getPrix()) + " DT");
        descriptionLabel.setText(produit.getDescription());

        try {
            String imageUrl = produit.getImage();
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
            System.err.println("Erreur lors du chargement de l'image : " );
            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erreur");
//            alert.setHeaderText(null);
//            alert.setContentText("Erreur lors du chargement de l'image : " + e.getMessage());
//            alert.showAndWait();
        }
        PanierButton.setOnAction(event -> addToCart(event, produit));
    }

    @FXML
    void addToCart(ActionEvent event, Produit produit) {
        if (produit != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/panier.fxml"));
                Parent root = loader.load();
                panierController panierController = loader.getController();
                panierController.initData(produit);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Confirmation panier");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun produit sélectionné");
        }
    }
    @FXML
    void generateQrcode(ActionEvent event) {
        if (selectedProduit != null) {
            String qrText = "Produit: " + selectedProduit.getNomp() + "\n"
                    + "Description du produit: " + selectedProduit.getDescription() + "\n"
                    + "Stock: " + selectedProduit.getStock() + "\n"
                    + "Date de production: " + selectedProduit.getDateProduction() + "\n"
                    + "Date d'expiration: " + selectedProduit.getDatePeremption();

            try {
                byte[] qrCodeImage = QrCodeGenerator.generateQrCode(qrText);

                ImageView qrCodeImageView = new ImageView(new Image(new ByteArrayInputStream(qrCodeImage)));
                qrCodeImageView.setFitWidth(200);
                qrCodeImageView.setFitHeight(200);

                VBox vbox = new VBox(qrCodeImageView);
                vbox.setAlignment(Pos.CENTER);
                vbox.setPadding(new Insets(20, 0, 0, 0));

                VBox.setMargin(qrCodeImageView, new Insets(0, 0, 0, -50));

                double alertWidth = qrCodeImageView.getFitWidth() + 120;
                double alertHeight = qrCodeImageView.getFitHeight() + 70;

                Alert qrCodeAlert = new Alert(Alert.AlertType.INFORMATION);
                qrCodeAlert.setTitle("QR Code");
                qrCodeAlert.setHeaderText(null);
                qrCodeAlert.getDialogPane().setContent(vbox);
                qrCodeAlert.getDialogPane().setPrefWidth(alertWidth);
                qrCodeAlert.getDialogPane().setPrefHeight(alertHeight);

                qrCodeAlert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun produit sélectionné");
        }
    }

}