package utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ImageViewExample extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger l'image depuis un fichier local
        Image image = new Image("file:///chemin/vers/votre/image/example.png");

        // Créer une ImageView pour afficher l'image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200); // Définir la largeur de l'ImageView
        imageView.setFitHeight(150); // Définir la hauteur de l'ImageView

        // Créer un conteneur (par exemple, AnchorPane) et ajouter ImageView
        AnchorPane root = new AnchorPane();
        root.getChildren().add(imageView);

        // Créer une scène et définir le conteneur comme racine
        Scene scene = new Scene(root, 400, 300);

        // Configurer la scène et afficher la fenêtre
        primaryStage.setScene(scene);
        primaryStage.setTitle("Affichage d'une Image avec JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
