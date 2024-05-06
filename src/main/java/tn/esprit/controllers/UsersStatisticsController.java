package tn.esprit.controllers;

import javafx.beans.binding.Bindings;
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
import javafx.stage.Stage;
import tn.esprit.models.User;
import tn.esprit.services.UserService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersStatisticsController implements Initializable {

    @FXML
    private PieChart pieChartGender;
    @FXML
    private PieChart pieChartAge;

    private Stage stage;
    private Scene scene;
    private Parent pt;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<User> usersList;
        usersList = UserService.displayUsers();
        int maleCount = (int) usersList.stream().filter(user -> "Male".equalsIgnoreCase(user.getGender())).count();
        int femaleCount = (int) usersList.stream().filter(user -> "Female".equalsIgnoreCase(user.getGender())).count();


        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Homme", maleCount),
                        new PieChart.Data("Femme", femaleCount));


        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), ": ", data.pieValueProperty()
                        )
                )
        );

        pieChartGender.getData().addAll(pieChartData);
        pieChartGender.setTitle("Statistique selon sexe ");

        LocalDate currentDate = LocalDate.now();

        int childCount = (int) usersList.stream()
                .filter(user -> calculateAge(user.getBirth_date(), currentDate) <= 12)
                .count();

        int youthCount = (int) usersList.stream()
                .filter(user -> calculateAge(user.getBirth_date(), currentDate) >= 13 &&
                        calculateAge(user.getBirth_date(), currentDate) <= 24)
                .count();

        int adultCount = (int) usersList.stream()
                .filter(user -> calculateAge(user.getBirth_date(), currentDate) >= 25 &&
                        calculateAge(user.getBirth_date(), currentDate) <60)
                .count();

        int oldCount = (int) usersList.stream()
                .filter(user -> calculateAge(user.getBirth_date(), currentDate) >= 60)
                .count();

//        int usersCount = maleCount + femaleCount ;
        ObservableList<PieChart.Data> pieChartDataAge =
                FXCollections.observableArrayList(
                        new PieChart.Data("Enfant", childCount),
                        new PieChart.Data("Jeune", youthCount),
                        new PieChart.Data("Adulte", adultCount),
                        new PieChart.Data("Agé", oldCount));



        pieChartDataAge.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), ": ", data.pieValueProperty()
                        )
                )
        );
        pieChartAge.getData().addAll(pieChartDataAge);
        pieChartAge.setTitle("Statistique selon tranche d'age ");
        System.out.println("Child Count: " + childCount);
        System.out.println("Youth Count: " + youthCount);
        System.out.println("Adult Count: " + adultCount);
        System.out.println("Old Count: " + oldCount);


    }

    private static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }


    @FXML
    private void returnBtnClicked(ActionEvent event) {
        try {
            pt = FXMLLoader
                    .load(getClass().getResource("/DisplayUsers-view.fxml"));

            scene = new Scene(pt);
            stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Liste des utilisateurs");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(AddUserAdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
