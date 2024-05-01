package org.example.controllers;

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
import org.example.model.Category;
import org.example.services.EvenementService;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class StatistiqueController {


    @FXML
    private PieChart pieChartEvent;


    @FXML
    private BarChart<String, Number> barChart;


    @FXML
    void generatePieChart(ActionEvent event) {


        /*Map<Integer, Long> dataByYear = serviceEve.getDataByYear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        int startYear = Collections.min(dataByYear.keySet());
        int endYear = Collections.max(dataByYear.keySet());

        for (int year = startYear; year <= endYear; year++) {
            Long count = dataByYear.getOrDefault(year, 0L);
            series.getData().add(new XYChart.Data<>(String.valueOf(year), count));
        }

        // Mettre à jour les données du BarChart
        barChart.getData().clear(); // Effacer les anciennes données
        barChart.getData().add(series);
        barChart.setTitle("Nombre d'événements par année");*/

    }


    public void getEventList(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AfficherEvenement.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Close the current stage
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPieChartEvent(ActionEvent actionEvent) {

        EvenementService serviceEve = new EvenementService();

        Map<Category, Long> dataByCategory = serviceEve.getDataByCategory();

        ObservableList<PieChart.Data> pieChartData = pieChartEvent.getData();

        for (Map.Entry<Category, Long> entry : dataByCategory.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey().getNom(), entry.getValue()));
        }

        pieChartEvent.setData(pieChartData);
        pieChartEvent.setTitle("Événements par Catégorie");
        pieChartEvent.setAnimated(true);

        // Ajoutez une animation au diagramme circulaire
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), pieChartEvent);
        rotateTransition.setByAngle(360);
        rotateTransition.play();

    }
}

