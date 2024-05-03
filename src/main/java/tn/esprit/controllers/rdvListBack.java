package tn.esprit.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import tn.esprit.models.rdv;
import tn.esprit.services.RdvService;
import javafx.scene.control.TableRow;
import javafx.scene.control.Button;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class rdvListBack {
    @FXML
    private TableView<rdv> rdvTableView;

    @FXML
    private Label moisLabel;

    @FXML
    private Label anneeLabel;

    @FXML
    private GridPane calendrierGridPane;

    private RdvService rdvService = new RdvService();

    @FXML
    private int currentYear;
    @FXML
    private int currentMonth;

    @FXML
    private Button RetourButton;

    private boolean isTriAscendant = true;





    private void updateMonthLabel(int month) {
        String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        moisLabel.setText(months[month - 1]);
    }

    // Mettez à jour l'étiquette de l'année avec l'année sélectionnée
    private void updateYearLabel(int year) {
        anneeLabel.setText(Integer.toString(year));
    }



    @FXML
    private void initialize() {
        configureTableView();
        afficherRendezVous();
        LocalDate currentDate = LocalDate.now();
        currentYear = currentDate.getYear();
        currentMonth = currentDate.getMonthValue();
        afficherCalendrier(currentYear, currentMonth);

        updateMonthLabel(currentMonth);
        updateYearLabel(currentYear);

       }

    private void configureTableView() {
        // Colonnes existantes

        TableColumn<rdv, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getdate_rdv()));
        dateColumn.setCellFactory(column -> {
            return new TableCell<rdv, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        setAlignment(Pos.CENTER); // Aligner au centre
                        setTextFill(Color.BLACK); // Couleur du texte en noir
                    }
                }
            };
        });
        dateColumn.setPrefWidth(100); // Définissez la largeur de la colonne ici
        dateColumn.setStyle("-fx-alignment: CENTER;"); // Centrer les données

        TableColumn<rdv, String> horaireColumn = new TableColumn<>("Horaire");
        horaireColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().gethoraire_rdv()));
        horaireColumn.setPrefWidth(100); // Définissez la largeur de la colonne ici
        horaireColumn.setStyle("-fx-alignment: CENTER;"); // Centrer les données

        TableColumn<rdv, String> problemeColumn = new TableColumn<>("Problème");
        problemeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProbleme()));
        problemeColumn.setPrefWidth(100); // Définissez la largeur de la colonne ici
        problemeColumn.setStyle("-fx-alignment: CENTER;"); // Centrer les données

        // Appliquer un style personnalisé aux lignes de la TableView directement à partir du code Java
        rdvTableView.setRowFactory(tv -> {
            TableRow<rdv> row = new TableRow<>();
            row.setStyle("-fx-background-color: linear-gradient(to bottom, #FFFFFF, #E0E0E0); -fx-text-fill: #000000;");
            return row;
        });

        // Ajouter les colonnes existantes à la TableView
        rdvTableView.getColumns().addAll( dateColumn, horaireColumn, problemeColumn);
    }




    private void afficherRendezVous() {
        // Vider la liste des éléments de la TableView
        rdvTableView.getItems().clear();

        // Récupérer la nouvelle liste des rendez-vous
        List<rdv> rdvs = rdvService.getAll();

        // Ajouter les nouveaux éléments à la TableView
        rdvTableView.getItems().addAll(rdvs);

    }

    private void openUpdateView(rdv rdv) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/rdvUpdate.fxml"));
            Parent root = loader.load();

            rdvUpdate controller = loader.getController();
            controller.initData(rdv);

            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) rdvTableView.getScene().getWindow();

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void afficherCalendrier(int year, int month) {
        calendrierGridPane.getChildren().clear();
        YearMonth yearMonth = YearMonth.of(year, month);
        int joursDansMois = yearMonth.lengthOfMonth();
        int jourDebutMois = yearMonth.atDay(1).getDayOfWeek().getValue(); // Jour de la semaine du premier jour du mois

        String[] joursSemaine = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (int i = 0; i < 7; i++) {
            Label labelJourSemaine = new Label(joursSemaine[i]);
            labelJourSemaine.setStyle("-fx-font-weight: bold");
            calendrierGridPane.add(labelJourSemaine, i, 0);
        }

        int row = 1;
        int jourCourant = 1;

        while (jourCourant <= joursDansMois) {
            for (int col = 0; col < 7; col++) {
                Button btnJour = new Button(Integer.toString(jourCourant));
                btnJour.setStyle("-fx-background-color: linear-gradient(#69bfa7, #00a480); -fx-text-fill: #F2F2F2; -fx-font-size: 14px;");
                btnJour.setPrefWidth(40);
                btnJour.setPrefHeight(40);

                if (hasRendezVous(year, month, jourCourant)) {
                    Circle circle = new Circle(3, Color.RED); // Ajustez le rayon ici (par exemple, 2.5)
                    StackPane stack = new StackPane(btnJour, circle);
                    StackPane.setAlignment(circle, Pos.TOP_RIGHT);
                    calendrierGridPane.add(stack, col, row);
                } else {
                    calendrierGridPane.add(btnJour, col, row);
                }

                final int jourSelectionne = jourCourant;
                btnJour.setOnAction(event -> {
                    afficherRendezVousDuJour(year, month, jourSelectionne);
                });

                jourCourant++;

                if (jourCourant > joursDansMois) {
                    break;
                }
            }
            row++;
        }
    }

    private void afficherRendezVousDuJour(int annee, int mois, int jour) {
        // Construire la date sélectionnée
        LocalDate dateSelectionnee = LocalDate.of(annee, mois, jour);

        // Récupérer les rendez-vous pour la date sélectionnée
        List<rdv> rdvs = rdvService.getRdvByDate(dateSelectionnee);

        // Effacer la TableView actuelle
        rdvTableView.getItems().clear();

        // Afficher un message en fonction de la présence de rendez-vous
        if (rdvs.isEmpty()) {
            // Afficher une boîte de dialogue avec le message approprié
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aucun rendez-vous");
            alert.setHeaderText(null);
            alert.setContentText("Aucun rendez-vous n'est pris pour cette date.");
            alert.showAndWait();
        } else {
            // Ajouter les rendez-vous à la TableView
            rdvTableView.getItems().addAll(rdvs);
        }
    }

    @FXML
    private void moisPrecedent() {
        currentMonth--;
        if (currentMonth == 0) {
            currentMonth = 12;
            currentYear--;
        }
        afficherCalendrier(currentYear, currentMonth);
        updateMonthLabel(currentMonth);
        afficherRendezVous();
    }

    @FXML
    private void moisSuivant() {
        currentMonth++;
        if (currentMonth == 13) {
            currentMonth = 1;
            currentYear++;
        }
        afficherCalendrier(currentYear, currentMonth);
        updateMonthLabel(currentMonth);
        afficherRendezVous();
    }

    @FXML
    private void anneePrecedente() {
        currentYear--;
        afficherCalendrier(currentYear, currentMonth);
        updateYearLabel(currentYear);
        afficherRendezVous();
    }

    @FXML
    private void anneeSuivante() {
        currentYear++;
        afficherCalendrier(currentYear, currentMonth);
        updateYearLabel(currentYear);
        afficherRendezVous();
    }

    private boolean hasRendezVous(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        List<rdv> rdvs = rdvService.getRdvByDate(date);
        return !rdvs.isEmpty();
    }








    @FXML
    public void trierParDate(ActionEvent actionEvent) {
        // Récupérer l'état du tri
        boolean ascendant = isTriAscendant;

        // Récupérer la liste des rendez-vous
        List<rdv> rdvs = rdvService.getAll();

        // Trier les rendez-vous par date
        if (ascendant) {
            rdvs.sort(Comparator.comparing(rdv -> LocalDate.parse(rdv.getdate_rdv())));
        } else {
            rdvs.sort((rdv1, rdv2) -> {
                LocalDate date1 = LocalDate.parse(rdv1.getdate_rdv());
                LocalDate date2 = LocalDate.parse(rdv2.getdate_rdv());
                return date2.compareTo(date1);
            });
        }

        // Effacer la TableView actuelle
        rdvTableView.getItems().clear();

        // Ajouter les rendez-vous triés à la TableView
        rdvTableView.getItems().addAll(rdvs);

        // Inverser l'état du tri
        isTriAscendant = !isTriAscendant;
    }

    @FXML
    private void calculerEtAfficherStatistiques() {
        List<rdv> rdvs = rdvService.getAll();
        int totalRDVs = rdvs.size();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Parcourir les rendez-vous et compter le nombre de rendez-vous pour chaque problème
        Map<String, Integer> problemCountMap = new HashMap<>();
        for (rdv rdv : rdvs) {
            String problem = rdv.getProbleme();
            problemCountMap.put(problem, problemCountMap.getOrDefault(problem, 0) + 1);
        }

        // Ajouter les données au graphique camembert
        problemCountMap.forEach((problem, count) -> {
            double pourcentage = (double) count / totalRDVs * 100;
            String dataLabel = String.format("%s (%d)", problem, count);
            pieChartData.add(new PieChart.Data(dataLabel, count));
        });

        // Afficher les statistiques dans le graphique camembert
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Statistiques des rendez-vous par problème");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);

        // Créer une nouvelle fenêtre pour afficher le graphique camembert
        Stage stage = new Stage();
        Scene scene = new Scene(pieChart);
        stage.setScene(scene);
        stage.setTitle("Statistiques des rendez-vous par problème");
        stage.show();
    }

    @FXML
    public void ConsulterRDVButtonAction(ActionEvent actionEvent) {
        try {
            // Charger rdvListBack.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/rdvListBack.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton et la mettre dans une fenêtre (Stage)
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la même fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en conséquence
        }
    }

    @FXML
    public void handleConsulterListeRecettesButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteList.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAjouterRecettesButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/recetteAdd.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Mettre la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

