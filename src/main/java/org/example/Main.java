package org.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.entrainements;
import services.EntrainementServices;

import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {
    public static void main(String[] args) {

    //    MyDatabase dbInstance = MyDatabase.getInstance();
        EntrainementServices ps = new EntrainementServices();

        System.out.println("hello ");
        entrainements e1 = new entrainements(8686886,"e",22,"55e","xxx",22);



        try {
            ps.add(e1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }




    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Chargement du fichier FXML...");
       // if ( typeuser.eq)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addEntrainement.fxml"));
        Parent root = loader.load();
        System.out.println("Fichier FXML chargé avec succès.");

        Scene scene = new Scene(root);
        primaryStage.setTitle("SyncStars");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}