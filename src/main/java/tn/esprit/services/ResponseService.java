package tn.esprit.services;

import tn.esprit.models.Question;
import tn.esprit.models.Response;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponseService implements Services.IServiceQ<Response> {
    private Connection connection;
    public ResponseService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Response response) throws SQLException {
        String sql = "INSERT INTO response (content,user_id,question_id) VALUES (?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, response.getContent());
        preparedStatement.setInt(2, response.getUserId());
        preparedStatement.setInt(3, response.getQuestionId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(Response response) throws SQLException {
        String sql = "UPDATE response SET Content=?,user_id=?,question_id=? WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, response.getContent());
        preparedStatement.setInt(2, response.getUserId());
        preparedStatement.setInt(3, response.getQuestionId());
        preparedStatement.setInt(4, response.getId());

        preparedStatement.executeUpdate();
    }


    @Override
    public void delete(int responseId) throws SQLException {
        String sql = "DELETE FROM response WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, responseId);

        preparedStatement.executeUpdate();
    }


    @Override
    public List<Response> getAll() throws SQLException {
        List<Response> questions = new ArrayList<>();
        String sql = "SELECT * FROM response";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Response response = new Response();
            response.setId(resultSet.getInt("id"));
            response.setContent(resultSet.getString("Content"));
            response.setQuestionId(resultSet.getInt("Question_Id"));
            response.setUserId(resultSet.getInt("User_Id"));

            questions.add(response);
        }

        return questions;
    }

    @Override
    public Response getById(int responseId) throws SQLException {
        String sql = "SELECT * FROM response WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, responseId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Response response = new Response();
            response.setId(resultSet.getInt("id"));
            response.setContent(resultSet.getString("Content"));
            response.setQuestionId(resultSet.getInt("Question_Id"));
            response.setUserId(resultSet.getInt("User_Id"));

            return response;
        }

        return null; // If no response with the given ID is found
    }

    // Method to fetch replies for a given question
    public List<Response> getRepliesForQuestion(Question question) throws SQLException {
        List<Response> replies = new ArrayList<>();
        String query = "SELECT * FROM response WHERE question_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, question.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Response response = new Response(
                            resultSet.getInt("id"),
                            resultSet.getString("content"),
                            resultSet.getInt("User_id"),
                            resultSet.getInt("question_id")
                    );
                    replies.add(response);
                }
            }
        }
        return replies;
    }



}
