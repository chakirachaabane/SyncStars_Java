package tn.esprit.views;
import tn.esprit.models.exercices;
import tn.esprit.models.entrainements;
import javafx.scene.layout.VBox;
import tn.esprit.models.exercices;
import java.util.List;

public class ExerciceListView extends VBox {

    public ExerciceListView(List<exercices> exercicesList,int poids) {
        setSpacing(20);

        for (exercices exercice : exercicesList) {
            tn.esprit.views.ExerciceCardView cardView = new tn.esprit.views.ExerciceCardView(exercice,poids);
            getChildren().add(cardView);
        }
    }
}
