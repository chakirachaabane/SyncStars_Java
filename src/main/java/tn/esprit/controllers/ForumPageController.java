package tn.esprit.controllers;

import javafx.scene.Node;
import javafx.scene.image.Image;
import tn.esprit.models.Data;
import tn.esprit.models.Question;
import tn.esprit.models.Response;
import tn.esprit.services.ResponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tn.esprit.services.QuestionService;
import tn.esprit.utils.MyDatabase;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ForumPageController {

    @FXML
    private ListView<Question> questionListView; // Assuming this is the ListView where questions will be displayed

    @FXML
    private Button Ajouter_question;

    @FXML
    private MenuButton Filter_button;

    @FXML
    private HBox _root;

    @FXML
    private TextField search_bar;







    private QuestionService questionService;
    private ResponseService responseService;
    private Connection connection;
    private WelcomeController WC;

    public ForumPageController() {
        this.questionService = new QuestionService(); // Initialize your QuestionService
        this.responseService = new ResponseService(); // Initialize your QuestionService
        connection = MyDatabase.getInstance().getConnection();
    }

    @FXML
    public void initialize() {

        questionListView.setCellFactory(new Callback<ListView<Question>, ListCell<Question>>() {
            @Override
            public ListCell<Question> call(ListView<Question> listView) {
                return new QuestionListCell(ForumPageController.this);
            }
        });
        search_bar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filter the questions based on the search text
            try {
                filterQuestions(newValue);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        loadQuestions(); // Call loadQuestions() when the controller is initialized
    }



    public void loadQuestions() {
        try {
            // Fetch existing questions from the database
            List<Question> questions = questionService.getAll();

            // Add questions to the ListView
            questionListView.getItems().addAll(questions);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception (e.g., show error message)
        }
    }

    //------------------------------------ Filtre Place --------------------------------------------------

    private void filterQuestions(String searchText) throws SQLException {
        // If the search text is empty, show all questions
        if (searchText == null || searchText.isEmpty()) {
            questionListView.setItems(FXCollections.observableArrayList(questionService.getAll()));
            return;
        }

        // Filter the questions based on the search text
        List<Question> filteredQuestions = questionService.search(searchText); // Implement the search method in your QuestionService

        // Update the ListView with the filtered questions
        questionListView.setItems(FXCollections.observableArrayList(filteredQuestions));
    }

    @FXML
    private void handleFilterMenuItemHR() throws SQLException {
        List<Question> questions = questionService.getAll();
            ObservableList<Question> questionsWithResponses = FXCollections.observableArrayList();
            try {
                for (Question question : questions) {
                    if (questionHasResponses(question.getId())) {
                        questionsWithResponses.add(question);
                    }
                }
                // Update the ListView to display questions with responses
                questionListView.setItems(questionsWithResponses);
                System.out.println("Questions that have responses : " + questionsWithResponses.size());
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL exception
            }
        // Add more conditions for other filter options if needed
    }

    @FXML
    private void AllQuestions() throws SQLException {
        List<Question> questions = questionService.getAll();
            ObservableList<Question> questionsWithResponses = FXCollections.observableArrayList();
        for (Question question : questions) {
                questionsWithResponses.addAll(question);
        }
        System.out.println("All Questions : " + questionsWithResponses.size());
        // Update the ListView to display questions with responses
        questionListView.setItems(FXCollections.observableArrayList(questionsWithResponses));
        // Add more conditions for other filter options if needed
    }

    @FXML
    private void handleFilterMenuItemNR() throws SQLException {
        List<Question> questions = questionService.getAll();
            ObservableList<Question> questionsWithNoResponses = FXCollections.observableArrayList();
            try {
                for (Question question : questions) {
                    if (!questionHasResponses(question.getId())) {
                        questionsWithNoResponses.add(question);
                    }
                }
                // Update the ListView to display questions with responses
                questionListView.setItems(questionsWithNoResponses);
                System.out.println("Questions That dont have responses : " + questionsWithNoResponses.size());
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL exception
            }
        // Add more conditions for other filter options if needed
    }

    private boolean questionHasResponses(int questionId) throws SQLException {
        // Execute SQL query to check if the question has responses in the response table
        // Replace this with your SQL query to check if responses exist for the given questionId
        String sql = "SELECT COUNT(*) FROM response WHERE question_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, questionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    //------------------------------------ Other Question Methods  --------------------------------------------------


    public void deleteQuestion(Question questionToDelete) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this question?");

            // Show the confirmation dialog and wait for the user's response
            Optional<ButtonType> result = alert.showAndWait();

            // Check if the user clicked the OK button
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the question from the database
                questionService.delete(questionToDelete.getId());
                System.out.println("Question Delete Successfully "+ questionToDelete.getId());

                // Remove the question from the ListView
                questionListView.getItems().remove(questionToDelete);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception (e.g., show error message)
        }
    }

    // Method to delete a response by its ID
    public void deleteResponse(int responseId) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this response?");

            // Show the confirmation dialog and wait for the user's response
            Optional<ButtonType> result = alert.showAndWait();

            // Check if the user clicked the OK button
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the question from the database
                responseService.delete(responseId);
                System.out.println("response supprimé avec succées : " +responseId);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception (e.g., show error message)
        }
    }

    public void addResponse(int user_id,int questionId, String content) throws SQLException {
        // Create a new Response object
        Response newResponse = new Response(content,user_id, questionId);

        // Call the addResponse method in the ResponseService to insert the response into the database
        responseService.add(newResponse);
    }

    public void UpdateResponse(int id,int user_id,int questionId, String content) throws SQLException {
        // Create a new Response object
        Response UpdatedResponse = new Response(id,content,user_id, questionId);

        // Call the addResponse method in the ResponseService to insert the response into the database
        responseService.update(UpdatedResponse);
    }

    //------------------------------------ Other tabs  --------------------------------------------------

    @FXML
    private void handleShowUpdateForm(ActionEvent event) {
        // Get the selected question from the list view
        Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            // Call the method to open the update form and pass the selected question
            NewTabUpdate(selectedQuestion);
        } else {
            // Handle case when no question is selected
            System.out.println("Please select a question to update.");
        }
    }

    @FXML
    void NewTabUpdate(Question question) {
        try {
            /*Parent root = FXMLLoader.load(getClass().getResource("/updateQuestionForm.fxml"));
            // Get the controller for the update form
            UpdateQuestionFormController updateController = new UpdateQuestionFormController();*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateQuestionForm.fxml"));
            Parent root = loader.load();
            UpdateQuestionFormController controller = loader.getController();


            // Pass the selected question data to the update form controller
            controller.setQuestion(question);

            Stage stage = new Stage();
            stage.setTitle("Update A Question");
            stage.setScene(new Scene(root, 450, 300));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    ----------------------------------------Navigation----------------------------------------
*/

    @FXML
    void RDVPath(ActionEvent event) {
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

    @FXML
    private void showProductsSwitch(ActionEvent event) {
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
    void NewTabAdd() {
        try {
            /*Parent root = FXMLLoader.load(getClass().getResource("/updateQuestionForm.fxml"));
            // Get the controller for the update form
            UpdateQuestionFormController updateController = new UpdateQuestionFormController();*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Add_Question_Page.fxml"));
            Parent root = loader.load();
            AddQuestionController controller = loader.getController();


            Stage stage = new Stage();
            stage.setTitle("Ajouter nouveaux Question !");
            stage.setScene(new Scene(root, 450, 300));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void welcomeSwitch(ActionEvent event) {
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
    void ReponseUpdateTab(Response response) {
        try {
            /*Parent root = FXMLLoader.load(getClass().getResource("/updateQuestionForm.fxml"));
            // Get the controller for the update form
            UpdateQuestionFormController updateController = new UpdateQuestionFormController();*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResponseUpdatePage.fxml"));
            Parent root = loader.load();

            UpdateResponseController controller = loader.getController();

            controller.setResponse(response);

            Stage stage = new Stage();
            stage.setTitle("Update A Response");
            stage.setScene(new Scene(root, 304, 122));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Response> getRepliesForQuestion(Question question) {

        try {
            // Call a method in your reply service to fetch replies for the given question
            return responseService.getRepliesForQuestion(question);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions, such as database errors
            return null; // Or throw the exception to be handled elsewhere
        }
    }


    private static class QuestionListCell extends ListCell<Question> {
        private ForumPageController controller;
        public QuestionListCell(ForumPageController controller) {
            this.controller = controller;
        }
        @Override
        protected void updateItem(Question question, boolean empty) {
            super.updateItem(question, empty);

            if (question != null) {
                // Create a VBox to hold the elements
                VBox vbox = new VBox();
                HBox hbox = new HBox();
                HBox hbox1 = new HBox();
                HBox hbox2 = new HBox();

                // Create labels for title and date
                Label titleLabel = new Label(question.getTitle());
                // Inside the updateItem method of QuestionListCell class
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                Label dateLabel = new Label(question.getD_Post().format(dateFormatter));

                hbox.getChildren().addAll(titleLabel, dateLabel);
                hbox.getStyleClass().add("hbox");

                // Create labels for other fields
                Label contentLabel = new Label(question.getContent());

                String attachmentFileName = ""; // Initialize an empty string to hold the file name
                String attachmentPath = question.getAttachment(); // Get the attachment path

// Extract the file name from the attachment path
                if (attachmentPath != null && !attachmentPath.isEmpty()) {
                    int lastSeparatorIndex = attachmentPath.lastIndexOf("\\");
                    if (lastSeparatorIndex != -1) {
                        // Extract the file name from the attachment path
                        attachmentFileName = attachmentPath.substring(lastSeparatorIndex + 1);
                    } else {
                        // If there is no separator, set the entire attachment path as the file name
                        attachmentFileName = attachmentPath;
                    }
                }

// Create the label with the file name
                Label attachmentLabel = new Label(attachmentFileName);



                Button likesButton = new Button("Likes: " + question.getNbr_Likes());
                likesButton.getStyleClass().add("button-like");
                likesButton.setOnAction(e -> {
                    try {
                        List<Question> questions = controller.questionService.getAll();

                        controller.questionService.addLike(question.getId(),questions);
                        // Update the button text to reflect the new number of likes
                        likesButton.setText("Likes : " + (question.getNbr_Likes()+1));
                        System.out.println("Like Added to : Question Id : "+ question.getId());
                        controller.AllQuestions();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Handle SQL exception (e.g., show error message)
                    }
                });

                Button dislikesButton = new Button("Dislikes: " + question.getNbr_DisLikes());
                dislikesButton.getStyleClass().add("button-dislike");
                dislikesButton.setOnAction(e -> {
                    try {
                        List<Question> questions = controller.questionService.getAll();
                        controller.questionService.dislike(question.getId(),questions);
                        // Update the button text to reflect the new number of dislikes
                        dislikesButton.setText("Dislikes: " + (question.getNbr_DisLikes()+1));
                        System.out.println("DisLike Added to : Question Id : "+ question.getId());
                        controller.AllQuestions();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Handle SQL exception (e.g., show error message)
                    }
                });

                hbox1.getChildren().addAll(likesButton, dislikesButton);



                // Add title and date labels to the VBox
                vbox.getChildren().add(hbox);

                // Create VBox for displaying replies
                VBox repliesVBox = new VBox();
                // Fetch and display replies related to this question
                List<Response> replies = controller.getRepliesForQuestion(question);
                if(!replies.isEmpty()) {
                    for (Response reply : replies) {
                        Label replyLabel = new Label(reply.getContent());
                        if(Data.user.getId()==reply.getUserId()){

                        // Create ContextMenu with update and delete options
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem updateItemresponse = new MenuItem("Modifier");
                        updateItemresponse.getStyleClass().add("menu-item");
                        MenuItem deleteItemresponse = new MenuItem("Supprimer");
                        updateItemresponse.getStyleClass().add("menu-item");


                        // Set actions for the menu items
                        updateItemresponse.setOnAction(e -> {
                            // Handle update action
                            controller.ReponseUpdateTab(reply);
                        });
                        deleteItemresponse.setOnAction(e -> {
                            // Handle delete action
                            controller.deleteResponse(reply.getId());
                            try {
                                controller.AllQuestions();
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                        // Add menu items to the context menu
                        contextMenu.getItems().addAll(updateItemresponse, deleteItemresponse);

                        // Attach context menu to label
                        replyLabel.setContextMenu(contextMenu);
                        }

                        repliesVBox.getChildren().add(replyLabel);
                        replyLabel.getStyleClass().add("reply-label");
                    }
                }else{
                    Label replyLabel = new Label("Non Repondu ... ");
                    repliesVBox.getChildren().add(replyLabel);
                    replyLabel.getStyleClass().add("reply-label");
                }


                if(Data.user.getId()==question.getUserId()){

                // Create a context menu for each question
                ContextMenu contextMenu = new ContextMenu();

// Create menu items for update and delete actions
                MenuItem updateMenuItem = new MenuItem("Modifier");
                updateMenuItem.getStyleClass().add("menu-item-question1");
                MenuItem deleteMenuItem = new MenuItem("Supprimer");
                deleteMenuItem.getStyleClass().add("menu-item-question1");

// Set action event handlers for menu items
                updateMenuItem.setOnAction(event -> {
                    controller.handleShowUpdateForm(event);
                });

                deleteMenuItem.setOnAction(event -> {
                    controller.deleteQuestion(question);
                });

// Add menu items to the context menu
                contextMenu.getItems().addAll(updateMenuItem, deleteMenuItem);

// Attach the context menu to the question label or any appropriate node
                setContextMenu(contextMenu);
                }

                // Create a TextField for adding a new response
                TextField responseTextField = new TextField();
                responseTextField.setPromptText("Ajouter Une Reponse ...");
                responseTextField.getStyleClass().add("text-field_update");

// Create a Button to add the response
                Button addResponseButton = new Button("Ajouter");
                addResponseButton.getStyleClass().add("file-chooser-button");
                addResponseButton.setOnAction(e -> {
                    String responseText = responseTextField.getText();
                    if (!responseText.isEmpty()) {
                        try {
                            // Add the new response to the database
                            controller.addResponse(Data.user.getId(),question.getId(),responseText);

                            // Clear the text field after adding the response
                            responseTextField.clear();
                            System.out.println("Reponse add successfully Question : "+question.getId()+" User : "+Data.user.getId());
                            controller.AllQuestions();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            // Handle SQL exception (e.g., show error message)
                        }
                    }
                });

                hbox2.getChildren().addAll(responseTextField, addResponseButton);

                vbox.getChildren().addAll(contentLabel, attachmentLabel, hbox1);

                // Add reply VBox to the main VBox
                vbox.getChildren().add(repliesVBox);

                vbox.getChildren().add(hbox2);

                // Set the VBox as the cell's graphic
                titleLabel.getStyleClass().add("question-title");
                dateLabel.getStyleClass().add("question-date");
                contentLabel.getStyleClass().add("question-content");
                attachmentLabel.getStyleClass().add("question-attachment");
/*                likesLabel.getStyleClass().add("question-likes-dislikes");
                dislikesLabel.getStyleClass().add("question-likes-dislikes");*/
                vbox.getStyleClass().add("question-details");
                vbox.getStyleClass().add("vbox");
                repliesVBox.getStyleClass().add("replies-vbox");
                setGraphic(vbox);

                // Set text to null as we are using graphic
                setText(null);
            } else {
                // If the item is empty, clear the graphic and text
                setGraphic(null);
                setText(null);
            }
        }
    }

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        // Get the current stage
        Stage stage = (Stage) _root.getScene().getWindow();

        // Close the current stage
        stage.close();

        // Open the stage again
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main_Forum_Page.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            ForumPageController controller = loader.getController();

            // Set up the stage
            stage.setTitle("Forum Page");
            stage.setScene(new Scene(root, 926, 536));
            stage.show();
            System.out.println("Page Refreshed");

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    //-------------------------------------------------- PDF ------------------------------------------

    @FXML
    private void handleQuestionSelection() {
        Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            // Generate PDF for the selected question
            generatePDF(selectedQuestion);
        }
    }


    // Method to generate PDF from question information
    private void generatePDF(Question question) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            /*// Add logo to the top left corner
            PDImageXObject logo = PDImageXObject.createFromFile("../../main/resources/images/logo.png", document);
            float logoWidth = logo.getWidth();
            float logoHeight = logo.getHeight();
            float logoPositionX = 50; // Adjust position as needed
            float logoPositionY = 700 - logoHeight; // Adjust position as needed
            contentStream.drawImage(logo, logoPositionX, logoPositionY, logoWidth, logoHeight);*/
            // Set font for text
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Add today's date and time to the top of the page
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750); // Adjust position as needed
            contentStream.showText("Date & Time: " + formattedDateTime);
            contentStream.endText();

            // Draw table with question information
            drawTable(contentStream, question);

            // End the content stream
            contentStream.close();

            // Save the document
            String fileName = "questionDetails.pdf";
            File file = new File(fileName);
            document.save(file);

            // Open the file using the default PDF viewer
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

            // Show success message to the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Downloaded");
            alert.setHeaderText(null);
            alert.setContentText("PDF downloaded successfully.");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while generating PDF.");
            alert.showAndWait();
        }
    }

    // Method to draw a cell in the table
    private void drawCell(PDPageContentStream contentStream, float x, float y, float width, float height, String text) throws IOException {
        float textWidth = PDType1Font.HELVETICA.getStringWidth(text) / 1000 * 12; // Adjust font size as needed
        float textHeight = PDType1Font.HELVETICA.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 12; // Adjust font size as needed
        float startX = x + (width - textWidth) / 2;
        float startY = y + (height - textHeight) / 2;
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12); // Adjust font size as needed
        contentStream.newLineAtOffset(startX, startY);
        contentStream.showText(text);
        contentStream.endText();
    }

    // Method to draw table with question information
    private void drawTable(PDPageContentStream contentStream, Question question) throws IOException {
        float tableWidth = 500;
        float yStart = 700;
        float margin = 50;
        float cellMargin = 10;

        // Draw headers
        drawCell(contentStream, margin, yStart, tableWidth / 4, 20, "Attribute");
        drawCell(contentStream, margin + tableWidth / 4, yStart, tableWidth / 4, 20, "Value");

        // Draw question ID row
        float yPosition = yStart - 20 - cellMargin;
        /*drawCell(contentStream, margin, yPosition, tableWidth / 4, 20, "Question ID :");
        drawCell(contentStream, margin + tableWidth / 4, yPosition, tableWidth / 4, 20, String.valueOf(question.getId()));*/

        // Draw user ID row
        drawCell(contentStream, margin, yPosition, tableWidth / 4, 20, "By User :");
        drawCell(contentStream, margin + tableWidth / 4, yPosition, tableWidth / 4, 20, Data.user.getFirstname()+" "+Data.user.getLastname());

        // Draw content row
        yPosition -= 20 + cellMargin;
        drawCell(contentStream, margin, yPosition, tableWidth / 4, 20, "Titre :");
        drawCell(contentStream, margin + tableWidth / 4, yPosition, tableWidth / 4, 20, question.getTitle());// Draw content row

        yPosition -= 20 + cellMargin;
        drawCell(contentStream, margin, yPosition, tableWidth / 4, 20, "Content :");
        drawCell(contentStream, margin + tableWidth / 4, yPosition, tableWidth / 4, 20, question.getContent());

        if(!question.getAttachment().isEmpty()){

        // Draw date row
        yPosition -= 20 + cellMargin;
        drawCell(contentStream, margin, yPosition, tableWidth / 4, 20, "Attachment :");
        drawCell(contentStream, margin+ 20 + tableWidth / 4, yPosition, tableWidth / 4, 20, question.getAttachment());
        }

        // Draw date row
        yPosition -= 20 + cellMargin;
        drawCell(contentStream, margin, yPosition, tableWidth / 4, 20, "The Date :");
        drawCell(contentStream, margin + tableWidth / 4, yPosition, tableWidth / 4, 20, String.valueOf(question.getD_Post()));
    }


    public void sendNotificationEmail(Question question) {
        try {
            // SMTP server properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "25");

            /*// Sender's email credentials
            String username = "zoghlami.dhirar.10@gmail.com";
            String password = "badt mwvs cgpd bueg";  // Add your password/ key  here*/
            // Sender's email credentials
            String username = "Zouaghi.mohamedaziz@esprit.tn";
            String password = "vgoe xate vqvv nvqm";  // Add your password/ key  here

            javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Receiver's email
            String receiverEmail = "aziz.boss2001@gmail.com";

            if (receiverEmail != null) {
                String message = "Bonjour,\n\n"
                        + "Une nouvelle question a été ajoutée au forum:\n\n"
                        + "Titre: " + question.getTitle() + "\n"
                        + "Contenue: " + question.getContent() + "\n"
                        + "peut-être devriez-vous nous rendre visite et vous pourrez peut-être y répondre. \n\n"
                        + "Nous vous remercions de votre contribution!\n\n"
                        + "Cordialement,\n"
                        + "Align Vibes Team";
                sendEmail(session, username, receiverEmail, message);
                System.out.println("Notification email sent successfully.");
            } else {
                System.out.println("Receiver email not found.");
            }

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(javax.mail.Session session, String from, String to, String content) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("New Question Notification");
        message.setText(content);
        Transport.send(message);
    }

}
