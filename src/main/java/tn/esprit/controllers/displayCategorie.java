package tn.esprit.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.services.CategorieService;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.Categorie;
import tn.esprit.services.CategorieService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import tn.esprit.controllers.displayProduit;


public class displayCategorie implements Initializable {

    private final CategorieService cs = new CategorieService();
    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private AnchorPane sidebar;
    @FXML
    private Button sidebarCategorie;

    @FXML
    private Button sidebarProduit;
    @FXML
    private TableView<Categorie> tableC;

    @FXML
    private TableColumn<Categorie,String> colDescription;

    @FXML
    private TableColumn<Categorie,Integer> colDisponibilite;

    @FXML
    private TableColumn<Categorie,String> colNom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCategorie();


    }


//    void openAddCategorieDialog() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCategorie.fxml"));
//            Parent root = loader.load();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.setTitle("AlignVibe");
//            stage.setOnHidden(event -> {
//                addCategorie controller = (addCategorie) loader.getController();
//                showCategorie();
//            });
//            stage.showAndWait();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        }

    @FXML
    void AddCategorieSidebar(ActionEvent event) {
        loadPage("/addCategorie.fxml", event);

    }
    public void showCategorie() {

        ObservableList<Categorie> categorieList =cs.getAllCategories();
        tableC.setItems(categorieList);
        //colid.setCellValueFactory(new PropertyValueFactory<Categorie, Integer>("idCategorie"));
        colNom.setCellValueFactory(new PropertyValueFactory<Categorie, String>("nomc"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Categorie, String>("description"));
        colDisponibilite.setCellValueFactory(new PropertyValueFactory<Categorie, Integer>("disponibilite"));
    }

    @FXML
    void openUpdateCategorieDialog() {
        Categorie categorieSelect = tableC.getSelectionModel().getSelectedItem();
        Alert alert;
        if (categorieSelect != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateCategorie.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("AlignVibe");

                updateCategorie controller = loader.getController();
                controller.getData(categorieSelect);
                stage.setOnHiding(event -> {
                    showCategorie();
                });
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            System.out.println("veuillez choisir une catégorie !");
            if (categorieSelect == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une catégorie !");
                alert.showAndWait();
        }
        }}


    @FXML
    void deleteCategorie() {
        Alert alert;
        Categorie categorie = tableC.getSelectionModel().getSelectedItem();

        try {
            if (categorie == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une catégorie");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Veuillez d'abord sélectionner une catégorie");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer cette catégorie : " + categorie.getNomc() + " ?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.isPresent() && option.get() == ButtonType.OK) {
                    cs.deleteCategorie(categorie.getIdCategorie());
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Catégorie supprimée avec succès !");
                    alert.showAndWait();
                    showCategorie();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherDetailsCategories(ActionEvent event) {
        Categorie selectedCategorie = tableC.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            int categoryId = selectedCategorie.getIdCategorie();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/showDetailsC.fxml"));
                Parent root = loader.load();
                detailsCategorie controller = loader.getController();
                controller.afficherDetailsCategorie(categoryId);
                Scene scene = new Scene(root);

                Stage mainWindow = (Stage) tableC.getScene().getWindow();

                Stage dialogStage = new Stage();
                dialogStage.setScene(scene);
                dialogStage.setTitle("AlignVibe");

                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(mainWindow);
                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void ProduitsButtonAction(ActionEvent event) {
        loadPage("/displayProduit.fxml", event);
    }

    @FXML
    private void CategoriesButtonAction(ActionEvent event) {
        loadPage("/displayCategorie.fxml", event);
    }
    private void loadPage(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        @FXML
        void AddProduitSidebar(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayProduit.fxml"));
                Parent root = loader.load();
                displayProduit controller = loader.getController();
                controller.AddProduitSidebar(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    @FXML
    void StatistiquesButton(ActionEvent event) {
        loadPage("/statistiquesProduit.fxml", event);
    }

    }



