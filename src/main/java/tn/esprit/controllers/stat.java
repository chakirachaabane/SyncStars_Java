package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class stat {

    @FXML
    private LineChart<Integer, Integer> lineChart;

    public void initialize() {
        // Connexion à la base de données
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nom_de_votre_base_de_donnees", "utilisateur", "mot_de_passe")) {
            // Remplacer "nom_de_votre_base_de_donnees", "utilisateur" et "mot_de_passe" par les détails de connexion appropriés

            // Préparation de la requête SQL pour récupérer les statistiques des rendez-vous par mois
            String query = "SELECT MONTH(date_rdv) AS mois, COUNT(*) AS nb_rdv FROM rdv WHERE YEAR(date_rdv) = ? GROUP BY MONTH(date_rdv)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, LocalDate.now().getYear());

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            // Création de la liste pour stocker les statistiques des rendez-vous par mois
            List<Integer> moisList = new ArrayList<>();
            List<Integer> nbRdvList = new ArrayList<>();

            // Extraction des données de la base de données
            while (resultSet.next()) {
                int mois = resultSet.getInt("mois");
                int nbRdv = resultSet.getInt("nb_rdv");
                moisList.add(mois);
                nbRdvList.add(nbRdv);
            }

            // Création de la série de données pour le graphique
            XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
            for (int i = 0; i < moisList.size(); i++) {
                series.getData().add(new XYChart.Data<>(moisList.get(i), nbRdvList.get(i)));
            }

            // Ajout de la série de données au graphique
            lineChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
