package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tn.esprit.models.Categorie;
import tn.esprit.services.CategorieService;
import javafx.event.ActionEvent;

public class updateCategorie {
    private final CategorieService cs = new CategorieService();
    @FXML
    private TextArea descriptionCArea;

    @FXML
    private TextField disponibiliteTF;

    @FXML
    private TextField nomCTF;
    private Categorie categorieSelect;

    @FXML
    private Text DescriptioncTextError;

    @FXML
    private Text DisponibiliteTextError;

    @FXML
    private Text NomcTextError;
    public void getData(Categorie categorie) {
        categorieSelect = categorie;
        nomCTF.setText(categorie.getNomc());
        descriptionCArea.setText(categorie.getDescription());
        disponibiliteTF.setText(String.valueOf(categorie.getDisponibilite()));
    }
    @FXML
    void updateCategories(ActionEvent event) {


        boolean isValid = true;
        if (nomCTF.getText().isEmpty()) {
            NomcTextError .setText("Le nom de catégorie est vide");
            NomcTextError.setFill(Color.RED);
            isValid = false;
        } else if (!nomCTF.getText().matches("[\\p{L} -]+")) {
            NomcTextError.setText("Le nom de catégorie doit être de type string ");
            NomcTextError.setFill(Color.RED);
            isValid = false;
        }else if (nomCTF.getText().length() < 3) {
            NomcTextError.setText("Le nom du catégorie doit être d'au moins 3 caractères");
            NomcTextError.setFill(Color.RED);
            isValid = false;
        } else if (nomCTF.getText().length() > 40) {
            NomcTextError.setText("Le nom e catégorie doit être moins que  40 caractères");
            NomcTextError.setFill(Color.RED);
            isValid = false;
        }else {
            NomcTextError.setText("");
        }

        if (descriptionCArea.getText().isEmpty()) {
            DescriptioncTextError.setText("La description de catégorie est vide");
            DescriptioncTextError.setFill(Color.RED);
            isValid = false;
        }else if (descriptionCArea.getText().length() <10) {
            DescriptioncTextError.setText("La description  de catégorie doit être d'au moins 10 caractères");
            DescriptioncTextError.setFill(Color.RED);
            isValid = false;
        } else if (descriptionCArea.getText().length() > 500) {
            DescriptioncTextError.setText("La description de catégorie doit être moins que de 500 caractères");
            DescriptioncTextError.setFill(Color.RED);
            isValid = false;
        } else {
            DescriptioncTextError.setText("");
        }
        if (disponibiliteTF.getText().isEmpty()) {
            DisponibiliteTextError.setText("La disponibilité de catégorie est vide");
            DisponibiliteTextError.setFill(Color.RED);
            isValid = false;
        }

        else if (!disponibiliteTF.getText().isEmpty()) {
            try {
                int disponibilite = Integer.parseInt(disponibiliteTF.getText());
                if (disponibilite < 0) {
                    DisponibiliteTextError.setText("La disponibilité de catégorie doit être un nombre positif");
                    DisponibiliteTextError.setFill(Color.RED);
                    isValid = false;
                } else {
                    DisponibiliteTextError.setText("");
                }
            } catch (NumberFormatException e) {
                DisponibiliteTextError.setText("La disponibilité de catégorie doit être un nombre entier");
                DisponibiliteTextError.setFill(Color.RED);
                isValid = false;
            }}
        else {
            DisponibiliteTextError.setText("");
        }
        if (isValid) {

            categorieSelect.setNomc(nomCTF.getText());
            categorieSelect.setDescription(descriptionCArea.getText());
            categorieSelect.setDisponibilite(Integer.parseInt(disponibiliteTF.getText()));
            try {
                cs.updateCategorie(categorieSelect );
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                Notifications.create()
                        .title("Succès")
                        .text("Catégorie modifiée avec succès!")
                        .hideAfter(Duration.seconds(2.5))
                        .position(Pos.BOTTOM_RIGHT)
                        .graphic(new ImageView(new Image("/images/tik1.png")))
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



