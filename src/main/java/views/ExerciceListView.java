package views;

import javafx.scene.layout.VBox;
import model.exercices;
import java.util.List;

public class ExerciceListView extends VBox {

    public ExerciceListView(List<exercices> exercicesList,int poids) {
        setSpacing(20);

        for (exercices exercice : exercicesList) {
            ExerciceCardView cardView = new ExerciceCardView(exercice,poids);
            getChildren().add(cardView);
        }
    }
}
