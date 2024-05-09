package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.models.Categorie;
import tn.esprit.models.Produit;
import tn.esprit.services.CategorieService;
import tn.esprit.services.ProduitService;
import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class detailsProduit {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nomp;

    @FXML
    private Label descriptionp;

    @FXML
    private Label prix;
    @FXML
    private Label datep;
    @FXML
    private Label datepr;

    @FXML
    private Label idCategorieLabel;
    @FXML
    private Label stock;



    private ProduitService produitService;

    public detailsProduit() {
        this.produitService = new ProduitService();
    }

    @FXML
    private void initialize() {

    }

    public void afficherDetailsProduit(int id) {
        try {
            Produit produit = produitService.getByIdProduit(id);

            if (produit != null) {
                nomp.setText(produit.getNomp());
                descriptionp.setText(produit.getDescription());
                prix.setText(String.valueOf(produit.getPrix()));
                datep.setText(String.valueOf(produit.getDatePeremption()));
                stock.setText(String.valueOf(produit.getStock()));
                datepr.setText(String.valueOf(produit.getDateProduction()));

                int idCategorie = produit.getCategorie().getIdCategorie();
                String nomCategorie = produitService.getNomCategorieById(idCategorie);
                idCategorieLabel.setText(nomCategorie);

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

            } else {
                nomp.setText("Catégorie non trouvée");
                descriptionp.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (MalformedURLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("URL d'image incorrecte : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
