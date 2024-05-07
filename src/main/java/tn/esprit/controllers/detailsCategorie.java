package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import tn.esprit.models.Categorie;
import tn.esprit.services.CategorieService;

import java.sql.SQLException;

public class detailsCategorie
{
    @FXML
    private Label nomLabel;

    @FXML
    private Label descriptionLabel;


    @FXML
    private Label disponibiliteLabel;

    private CategorieService categorieService;

    public detailsCategorie() {
        this.categorieService = new CategorieService();
    }

    @FXML
    private void initialize() {

    }



    public void afficherDetailsCategorie(int id) {
        try {
            Categorie categorie = categorieService.getByIdCategorie(id);

            if (categorie != null) {
                nomLabel.setText(categorie.getNomc());
                descriptionLabel.setText(categorie.getDescription());
                disponibiliteLabel.setText(String.valueOf(categorie.getDisponibilite()));
            } else {
                nomLabel.setText("Catégorie non trouvée");
                descriptionLabel.setText("");
                disponibiliteLabel.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}
