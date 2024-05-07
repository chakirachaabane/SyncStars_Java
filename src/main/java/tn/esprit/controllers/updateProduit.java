package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tn.esprit.models.Categorie;

import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;
import javafx.scene.text.Text;

import javax.management.Notification;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class updateProduit implements Initializable {
    private final ProduitService ps = new ProduitService();

    @FXML
    private Button btnModifier;

    @FXML
    private DatePicker datePeremption;

    @FXML
    private DatePicker dateProduction;

    @FXML
    private TextArea descriptionArea;


    @FXML
    private ComboBox<String> nomCategorie;

    @FXML
    private TextField nomTF;
    @FXML
    private TextField imageTF;

    @FXML
    private TextField prixTF;

    @FXML
    private TextField stockTF;

    @FXML
    private Button uploadButton;
    private Produit produitSelect;
    @FXML
    private Text datePeremptionError;
    @FXML
    private Text dateProductionError;


    @FXML
    private Text DescriptiontextError;
    @FXML
    private Text imagetextError;
    @FXML
    private Text nomtextError;
    @FXML
    private Text prixTextError;
    @FXML
    private Text stocktextError;
    @FXML
    private Text CategorieComboBoxError;

    @FXML
    void updateProduits(ActionEvent event) {

        boolean isValid = true;
        LocalDate today = LocalDate.now();
        if (imageTF.getText().isEmpty()) {
            imagetextError.setText("L'image du produit est vide");
            imagetextError.setFill(Color.RED);
            isValid = false;
        } else if (!imageTF.getText().matches(".*\\.(gif|jpe?g|png)$")) {
            imagetextError.setText("L'URL de l'image doit se terminer par .gif, .jpeg, .jpg ou .png");
            imagetextError.setFill(Color.RED);
            isValid = false;
        } else {
            imagetextError.setText("");
        }
        if (nomTF.getText().isEmpty()) {
            nomtextError.setText("Le nom du produit est vide");
            nomtextError.setFill(Color.RED);
            isValid = false;
        } else if (!nomTF.getText().matches("[\\p{L} ]+")) {

            nomtextError.setText("Le nom doit être de type string");
            nomtextError.setFill(Color.RED);
            isValid = false;
        }else if (nomTF.getText().length() < 3) {
            nomtextError.setText("Le nom du produit doit être d'au moins 3 caractères");
            nomtextError.setFill(Color.RED);
            isValid = false;
        } else if (nomTF.getText().length() > 40) {
            nomtextError.setText("Le nom du produit doit être moins que  40 caractères");
            nomtextError.setFill(Color.RED);
            isValid = false;
        }else {
            nomtextError.setText("");
        }

        if (descriptionArea.getText().isEmpty()) {
            DescriptiontextError.setText("La description du produit est vide");
            DescriptiontextError.setFill(Color.RED);
            isValid = false;
        }else if (descriptionArea.getText().length() <10) {
            DescriptiontextError.setText("La description  du produit doit être d'au moins 10 caractères");
            DescriptiontextError.setFill(Color.RED);
            isValid = false;
        } else if (descriptionArea.getText().length() > 500) {
            DescriptiontextError.setText("La description du produit doit être moins que de 500 caractères");
            DescriptiontextError.setFill(Color.RED);
            isValid = false;
        } else {
            DescriptiontextError.setText("");
        }

        if (prixTF.getText().isEmpty()) {
            prixTextError.setText("Le prix du produit est vide");
            prixTextError.setFill(Color.RED);
            isValid = false;
        }
        else if (!prixTF.getText().isEmpty()) {
            try {
                float prix = Float.parseFloat(prixTF.getText());
                if (prix <= 0) {
                    prixTextError.setText("le prix doit etre positif");
                    prixTextError.setFill(Color.RED);
                    isValid = false;
                } else {
                    prixTextError.setText("");
                }
            } catch (NumberFormatException e) {
                prixTextError.setText("type non valide");
                prixTextError.setFill(Color.RED);
                isValid = false;
            }}else {
            prixTextError.setText("");
        }

        if (stockTF.getText().isEmpty()) {
            stocktextError.setText("Le stock du produit est vide");
            stocktextError.setFill(Color.RED);
            isValid = false;
        }

        else if (!stockTF.getText().isEmpty()) {
            try {
                int stock = Integer.parseInt(stockTF.getText());
                if (stock < 0) {
                    stocktextError.setText("Le stock du produit doit être un nombre positif");
                    stocktextError.setFill(Color.RED);
                    isValid = false;
                } else {
                    stocktextError.setText("");
                }
            } catch (NumberFormatException e) {
                stocktextError.setText("Le stock du produit doit être un nombre entier");
                stocktextError.setFill(Color.RED);
                isValid = false;
            }}
        else {
            stocktextError.setText("");
        }

        if (datePeremption.getValue() == null) {

            datePeremptionError.setText("La date de péremption du produit n'est pas sélectionnée");
            datePeremptionError.setFill(Color.RED);

            isValid = false;
        }
        else if (datePeremption.getValue() .isBefore(dateProduction.getValue())) {
            datePeremptionError.setText("La date de péremption doit être superieur à la date de production !");
            datePeremptionError.setFill(Color.RED);

        }
        else {
            datePeremptionError.setText("");
        }

        if (dateProduction.getValue() == null) {
            dateProductionError.setText("La date de production du produit n'est pas sélectionnée");
            dateProductionError.setFill(Color.RED);

            isValid = false;
        }
        else if (dateProduction.getValue() .isAfter(today)) {
            dateProductionError.setText("La date de production doit être inferieur ou egale à la date actuelle !");
            dateProductionError.setFill(Color.RED);

        }else {
            dateProductionError.setText("");
        }
        if (nomCategorie.getValue() == null) {
            CategorieComboBoxError.setText("La catégorie du produit n'est pas sélectionnée");
            CategorieComboBoxError.setFill(Color.RED);
        } else{
            CategorieComboBoxError.setText("");
        } if (isValid) {
            produitSelect.setImage(imageTF.getText());
            produitSelect.setNomp(nomTF.getText());
            produitSelect.setDescription(descriptionArea.getText());
            produitSelect.setPrix(Float.parseFloat(prixTF.getText()));
            produitSelect.setDatePeremption(datePeremption.getValue());
            produitSelect.setStock(Integer.parseInt(stockTF.getText()));
            produitSelect.setDateProduction(dateProduction.getValue());
            String nomCategorieValue = nomCategorie.getValue();
            int idCategorie = ps.getIdCategorieByName(nomCategorieValue);
            produitSelect.getCategorie().setIdCategorie(idCategorie);

            try {
                ps.updateProduit(produitSelect);
                refreshImage();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                Notifications.create()
                        .title("Succès")
                        .text("Produit modifié avec succès!")
                        .hideAfter(Duration.seconds(2.5))
                        .position(Pos.BOTTOM_RIGHT)
                        .graphic(new ImageView(new Image("/images/tik1.png")))
                        .show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }}
    private void refreshImage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayProduit.fxml"));
        try {
            Parent root = loader.load();
            displayProduit controller = loader.getController();
            controller.showProduits();
            controller.showImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getData(Produit produit) {
        produitSelect = produit;
        imageTF.setText(produit.getImage());
        nomTF.setText(produit.getNomp());
        descriptionArea.setText(produit.getDescription());
        prixTF.setText(String.valueOf(produit.getPrix()));
        datePeremption.setValue(produit.getDatePeremption());
        stockTF.setText(String.valueOf(produit.getStock()));
        dateProduction.setValue(produit.getDateProduction());
        int idCategorie = produit.getCategorie().getIdCategorie();
        nomCategorie.setValue(ps.getNomCategorieById(idCategorie));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> categoryNames = ps.getAllCategories();
        nomCategorie.getItems().addAll(categoryNames);
        uploadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Télécharger une image");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            if (selectedFile != null) {
                imageTF.setText(selectedFile.getAbsolutePath());

            }
        });
    }
}

