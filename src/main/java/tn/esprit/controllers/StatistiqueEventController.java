package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.models.Category;
import tn.esprit.services.EvenementService;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class StatistiqueEventController {


    @FXML
    private PieChart pieChartEvent;

    @FXML
    private BarChart BarChartEvent;

    public void setPieChartData(Map<Category, Long> dataByCategory) {
        ObservableList<PieChart.Data> pieChartData = pieChartEvent.getData();
        for (Map.Entry<Category, Long> entry : dataByCategory.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey().getNom(), entry.getValue()));
        }

        pieChartEvent.setTitle("Événements par Catégorie");
        pieChartEvent.setAnimated(true);

        // Ajouter une animation au diagramme circulaire
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), pieChartEvent);
        rotateTransition.setByAngle(360);
        rotateTransition.play();
    }

    public void setBarChartData(Map<Integer, Long> dataByYear) {
        // Créer une nouvelle série de données pour le graphique en barres
        XYChart.Series<String, Long> series = new XYChart.Series<>();
        for (Map.Entry<Integer, Long> entry : dataByYear.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        BarChartEvent.getData().clear();
        BarChartEvent.getData().add(series);

        for (XYChart.Data<String, Long> data : series.getData()) {
            Node node = data.getNode();
            node.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.seconds(1), node);
            ft.setToValue(1);
            ft.play();
        }
    }
}

