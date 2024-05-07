package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class statistiquesProduit implements Initializable {
    @FXML
    private PieChart produitStat;
    @FXML
    private Button retour;
    ProduitService ps = new ProduitService();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statistiques();
    }

    private void statistiques() {
        List<Produit> topProduits = ps.getTopVenteProduits();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Produit produit : topProduits) {
            PieChart.Data data = new PieChart.Data(produit.getNomp(), produit.getNbventes());
            data.setName(data.getName() + " : " + (int) data.getPieValue() + " ventes");
            pieChartData.add(data);
        }

        produitStat.setData(pieChartData);
    }


    private  void loadPage(String fxmlFile, ActionEvent event) {
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
    void retourDisplay(ActionEvent event) {
        loadPage("/displayProduit.fxml", event);
    }
}
