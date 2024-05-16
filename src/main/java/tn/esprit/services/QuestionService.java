package tn.esprit.services;

import tn.esprit.models.Question;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements Services.IServiceQ<Question> {
    private Connection connection;
    public QuestionService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Question question) throws SQLException {
        String sql = "INSERT INTO question (title, content, attachment, D_post,nb_likes, nb_dis_likes,user_id_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, question.getTitle());
        preparedStatement.setString(2, question.getContent());
        preparedStatement.setString(3, question.getAttachment());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(question.getD_Post())); // Convert LocalDateTime to Timestamp
        preparedStatement.setInt(5, question.getNbr_Likes());
        preparedStatement.setInt(6, question.getNbr_DisLikes());
        preparedStatement.setInt(7, question.getUserId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(Question question) throws SQLException {
        String sql = "UPDATE question SET Title=?, Content=?, Attachment=?, nb_likes=?, nb_dis_likes=? WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, question.getTitle());
        preparedStatement.setString(2, question.getContent());
        preparedStatement.setString(3, question.getAttachment());
        preparedStatement.setInt(4, question.getNbr_Likes());
        preparedStatement.setInt(5, question.getNbr_DisLikes());
        preparedStatement.setInt(6, question.getId());

        preparedStatement.executeUpdate();
    }


    @Override
    public void delete(int questionId) throws SQLException {
        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Delete the responses related to the question
            String deleteResponsesSql = "DELETE FROM response WHERE question_id=?";
            try (PreparedStatement deleteResponsesStatement = connection.prepareStatement(deleteResponsesSql)) {
                deleteResponsesStatement.setInt(1, questionId);
                deleteResponsesStatement.executeUpdate();
            }

            // Delete the question
            String deleteQuestionSql = "DELETE FROM question WHERE id=?";
            try (PreparedStatement deleteQuestionStatement = connection.prepareStatement(deleteQuestionSql)) {
                deleteQuestionStatement.setInt(1, questionId);
                deleteQuestionStatement.executeUpdate();
            }

            // Commit the transaction
            connection.commit();
        } catch (SQLException ex) {
            // Rollback the transaction if an error occurs
            connection.rollback();
            throw ex;
        } finally {
            // Restore auto-commit mode
            connection.setAutoCommit(true);
        }
    }



    @Override
    public List<Question> getAll() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Question question = new Question();
            question.setId(resultSet.getInt("id"));
            question.setTitle(resultSet.getString("Title"));
            question.setContent(resultSet.getString("Content"));
            question.setUserId(resultSet.getInt("user_id_id"));
            question.setAttachment(resultSet.getString("Attachment"));
            LocalDateTime dPost = resultSet.getTimestamp("D_post").toLocalDateTime();
            question.setD_Post(dPost);
            question.setNbr_Likes(resultSet.getInt("Nb_Likes"));
            question.setNbr_DisLikes(resultSet.getInt("Nb_Dis_Likes"));

            questions.add(question);
        }

        return questions;
    }

    @Override
    public Question getById(int questionId) throws SQLException {
        String sql = "SELECT * FROM question WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, questionId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Question question = new Question();
            question.setId(resultSet.getInt("id"));
            question.setTitle(resultSet.getString("Title"));
            question.setUserId(resultSet.getInt("user_id_id"));
            question.setContent(resultSet.getString("Content"));
            question.setAttachment(resultSet.getString("Attachment"));
            LocalDateTime dPost = resultSet.getTimestamp("D_post").toLocalDateTime();
            question.setD_Post(dPost);
            question.setNbr_Likes(resultSet.getInt("Nb_Likes"));
            question.setNbr_DisLikes(resultSet.getInt("Nb_Dis_Likes"));

            return question;
        }

        return null; // If no question with the given ID is found
    }

    /*public void addLike(int questionId) throws SQLException {
        String sql = "UPDATE question SET nb_likes = nb_likes + 1 WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, questionId);
        preparedStatement.executeUpdate();
    }*/

    public void addLike(int questionId, List<Question> questionList) throws SQLException {
        questionList.stream()
                .filter(question -> question.getId() == questionId)
                .forEach(question -> {
                    question.setNbr_Likes(question.getNbr_Likes() + 1);
                    try {
                        update(question);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void removeaddLike(int questionId, List<Question> questionList) throws SQLException {
        questionList.stream()
                .filter(question -> question.getId() == questionId)
                .forEach(question -> {
                    question.setNbr_Likes(question.getNbr_Likes() - 1);
                    try {
                        update(question);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void dislike(int questionId, List<Question> questionList) throws SQLException {
        questionList.stream()
                .filter(question -> question.getId() == questionId)
                .forEach(question -> {
                    question.setNbr_DisLikes(question.getNbr_DisLikes() + 1);
                    try {
                        update(question);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void removedislike(int questionId, List<Question> questionList) throws SQLException {
        questionList.stream()
                .filter(question -> question.getId() == questionId)
                .forEach(question -> {
                    question.setNbr_DisLikes(question.getNbr_DisLikes() - 1);
                    try {
                        update(question);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public List<Question> search(String searchText) throws SQLException {
        List<Question> matchingQuestions = new ArrayList<>();

        // Dummy data for demonstration
        for (Question question : getAll()) {
            if (question.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    question.getContent().toLowerCase().contains(searchText.toLowerCase())) {
                matchingQuestions.add(question);
            }
        }

        return matchingQuestions;
    }

}
