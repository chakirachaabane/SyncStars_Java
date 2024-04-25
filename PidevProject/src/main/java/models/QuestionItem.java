package models;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class QuestionItem extends HBox {
    private final Question question;

    public QuestionItem(Question question) {
        this.question = question;
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(10); // Set spacing between elements in the HBox
        setAlignment(Pos.CENTER_LEFT); // Align items to the left
        setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 1px;");

        // Create VBox for title and date
        VBox titleDateBox = new VBox();
        titleDateBox.setSpacing(5);
        titleDateBox.setAlignment(Pos.CENTER_LEFT);

        // Create labels for title and date
        Label titleLabel = createLabel("Title: " + question.getTitle());
        Label dateLabel = createLabel("Date: " + question.getD_Post());

        // Add labels to titleDateBox
        titleDateBox.getChildren().addAll(titleLabel, dateLabel);

        // Create VBox for content, attachment, likes, dislikes
        VBox contentBox = new VBox();
        contentBox.setSpacing(5);
        contentBox.setAlignment(Pos.CENTER_LEFT);

        // Create labels for content, attachment, likes, dislikes
        Label contentLabel = createLabel("Content: " + question.getContent());
        Label attachmentLabel = createLabel("Attachment: " + question.getAttachment());
        Label likesLabel = createLabel("Likes: " + question.getNbr_Likes());
        Label dislikesLabel = createLabel("Dislikes: " + question.getNbr_DisLikes());

        // Add labels to contentBox
        contentBox.getChildren().addAll(contentLabel, attachmentLabel, likesLabel, dislikesLabel);

        // Add titleDateBox and contentBox to VBox
        getChildren().addAll(titleDateBox, contentBox);

        // Add event handler for mouse hover effect
        setOnMouseEntered(e -> setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 1px;"));
        setOnMouseExited(e -> setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 1px;"));

        // Add event handler for mouse click
        setOnMouseClicked(e -> {
            // Handle mouse click event, e.g., open question details, navigate to question page, etc.
        });
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 14)); // Set font size
        return label;
    }
}
