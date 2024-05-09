package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.models.rdv;
import tn.esprit.services.RdvService;
import tn.esprit.controllers.rdvUpdate;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class rdvList {

    @FXML
    private TableView<rdv> rdvTableView;


    // Déclarez la référence à l'étiquette dans votre classe de contrôleur
    @FXML
    private Label moisAnneeLabel;
    @FXML
    private Label label;

    @FXML
    private Button AllerVersFormulaire; // Ajout du champ pour le bouton

    private RdvService rdvService = new RdvService();

    @FXML
    private GridPane calendrierGridPane; // Ajout du champ pour le calendrier

    @FXML
    private Label moisLabel;

    @FXML
    private Label anneeLabel;

    private int currentYear;
    private int currentMonth;

    @FXML
    private Button moisPrecedentButton;
    @FXML
    private Button moisSuivantButton;

    @FXML
    private Button anneePrecedenteButton;
    @FXML
    private Button anneeSuivanteButton;




    // Mettez à jour l'étiquette du mois avec le mois sélectionné
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

        // Définir l'action lorsque le bouton "Prendre un RDV" est cliqué
        AllerVersFormulaire.setOnAction(event -> {
            redirectToRdvAdd(); // Redirige vers rdvAdd.fxml lorsque le bouton est cliqué
        });
        updateMonthLabel(currentMonth);
        updateYearLabel(currentYear);
    }

    private void configureTableView() {
        // Colonnes existantes
        TableColumn<rdv, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

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
                        setTextFill(Color.BLACK); // Couleur du texte en noir
                    }
                }
            };
        });

        TableColumn<rdv, String> horaireColumn = new TableColumn<>("Horaire");
        horaireColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().gethoraire_rdv()));

        TableColumn<rdv, String> problemeColumn = new TableColumn<>("Problème");
        problemeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProbleme()));

        // Ajouter les colonnes existantes à la TableView
        rdvTableView.getColumns().addAll(idColumn, dateColumn, horaireColumn, problemeColumn);

        // Colonnes pour les boutons Modifier et Supprimer
        TableColumn<rdv, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(cellFactoryModifier);

        TableColumn<rdv, Void> supprimerColumn = new TableColumn<>("Annuler RDV");
        supprimerColumn.setCellFactory(cellFactorySupprimer);

        rdvTableView.getColumns().add(modifierColumn);
        rdvTableView.getColumns().add(supprimerColumn);
    }

    // Factory pour créer les cellules de boutons Modifier
    private Callback<TableColumn<rdv, Void>, TableCell<rdv, Void>> cellFactoryModifier = new Callback<>() {
        @Override
        public TableCell<rdv, Void> call(final TableColumn<rdv, Void> param) {
            final TableCell<rdv, Void> cell = new TableCell<>() {
                private final Button modifierButton = new Button("Modifier");

                {
                    modifierButton.setStyle("-fx-background-color: #69BFA7; -fx-text-fill: #F2F2F2;");
                    modifierButton.setOnAction(event -> {
                        rdv rdv = getTableView().getItems().get(getIndex());
                        openUpdateView(rdv);
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(modifierButton);
                    }
                }
            };
            return cell;
        }
    };

    // Factory pour créer les cellules de boutons Supprimer
    private Callback<TableColumn<rdv, Void>, TableCell<rdv, Void>> cellFactorySupprimer = new Callback<>() {
        @Override
        public TableCell<rdv, Void> call(final TableColumn<rdv, Void> param) {
            final TableCell<rdv, Void> cell = new TableCell<>() {
                private final Button supprimerButton = new Button("Annuler");

                {
                    supprimerButton.setStyle("-fx-background-color: #69BFA7; -fx-text-fill: #F2F2F2;");
                    supprimerButton.setOnAction(event -> {
                        rdv rdv = getTableView().getItems().get(getIndex());
                        rdvService.deleteRdv(rdv.getId());
                        afficherRendezVous();
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(supprimerButton);
                    }
                }
            };
            return cell;
        }
    };

    private void afficherRendezVous() {
        // Vider la liste des éléments de la TableView
        rdvTableView.getItems().clear();

        // Récupérer la nouvelle liste des rendez-vous
        List<rdv> rdvs = rdvService.getAll();

        // Ajouter les nouveaux éléments à la TableView
        rdvTableView.getItems().addAll(rdvs);

    }



    @FXML
    private void EventPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void RDVPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAcc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void EntrainementPath(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateView(rdv rdv) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvUpdate.fxml"));
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

    private void redirectToRdvAdd() {
        try {
            // Charger le fichier FXML de rdvAdd.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAdd.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale et la modifier pour afficher la nouvelle scène
            Stage primaryStage = (Stage) AllerVersFormulaire.getScene().getWindow();
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
                btnJour.setStyle("-fx-background-color: linear-gradient(#69BFA7, #18593B); -fx-text-fill: #F2F2F2; -fx-font-size: 14px;");
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
    void getEventList(ActionEvent event) {
        try {
            // Charger le fichier FXML de rdvAdd.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale et la modifier pour afficher la nouvelle scène
            Stage primaryStage = (Stage) AllerVersFormulaire.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }   }


    public void getCatList(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategorieEve.fxml"));
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






    @FXML
    public void showProductsSwitch(ActionEvent event) {
        try {
            Parent parent2 = FXMLLoader
                    .load(getClass().getResource("/displayFrontProduit.fxml"));

            Scene scene = new Scene(parent2);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Produits");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showAccueilSwitch(ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent pt;
        PreparedStatement pst;
        try {
            pt = FXMLLoader.load(getClass().getResource("/welcome-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pt);
            stage.setScene(scene);
            stage.setTitle("Bienvenue!");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignInController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }


    @FXML
    private void displayEventSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void displayRdvSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rdvAcc.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void displayEntSwitch1(ActionEvent event) {
        try {
            // Charger le fichier FXML rdvList.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchEntrainementfront.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la référence de la fenêtre (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Modifier la scène de la fenêtre
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowForumSwitch1(ActionEvent event) {
        try {
            /*Parent root = FXMLLoader.load(getClass().getResource("/updateQuestionForm.fxml"));
            // Get the controller for the update form
            UpdateQuestionFormController updateController = new UpdateQuestionFormController();*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main_Forum_Page.fxml"));
            Parent root = loader.load();
            ForumPageController controller = loader.getController();


            Stage stage = new Stage();
            stage.setTitle("Forum Page");
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
