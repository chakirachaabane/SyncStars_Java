package services;

import model.exercices;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.MyDatabase;

public class ExercicesServices implements IService<exercices> {
    private Connection connection;

    public ExercicesServices() { // Mise à jour du constructeur pour correspondre au nom de la classe
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(exercices exercices) throws SQLException {

    }

    @Override
    public void addExercice(exercices exercise, int idEntrainement) throws SQLException {
        String sql = "INSERT INTO exercices (nom_exercice, type, duree, nombres_de_fois) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, exercise.getNom_exercice());
            preparedStatement.setString(2, exercise.getType());
            preparedStatement.setInt(3, exercise.getDuree());
            preparedStatement.setInt(4, exercise.getNombres_de_fois());

            // Execute the INSERT statement
            preparedStatement.executeUpdate();

            // Retrieve the generated keys (in this case, the generated ID)
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    String sql2 = "INSERT INTO entrainements_exercices (entrainements_id, exercices_id) VALUES (?, ?)";
                    try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                        preparedStatement2.setInt(2,generatedKeys.getInt(1));
                        preparedStatement2.setInt(1, idEntrainement);
                        System.out.println(generatedKeys.getInt(1));// Return the generated ID
                        System.out.println(idEntrainement);// Return the generated ID
                        // Execute the INSERT statement
                        preparedStatement2.executeUpdate();}
                    //entrainements_exercices
                    System.out.println(generatedKeys.getInt(1));// Return the generated ID
                } else {
                    throw new SQLException("Failed to retrieve generated ID for exercise");
                }
            }
        }

    }

    @Override
    public void update(exercices exercise) throws SQLException {
        String sql = "UPDATE exercices SET nom_exercice = ?, type = ?, duree = ?, nombres_de_fois = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, exercise.getNom_exercice());
            preparedStatement.setString(2, exercise.getType());
            preparedStatement.setInt(3, exercise.getDuree());
            preparedStatement.setInt(4, exercise.getNombres_de_fois());
            preparedStatement.setInt(5, exercise.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM exercices WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<exercices> getAll() throws SQLException {
        List<exercices> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercices";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                exercices exercise = new exercices(
                        rs.getInt("id"),
                        rs.getString("nom_exercice"),
                        rs.getString("type"),
                        rs.getInt("duree"),
                        rs.getInt("nombres_de_fois")
                );
                exercises.add(exercise);
            }
        }
        return exercises;
    }

    @Override
    public exercices getById(int id) throws SQLException {
        String sql = "SELECT * FROM exercices WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new exercices(
                        rs.getInt("id"),
                        rs.getString("nom_exercice"),
                        rs.getString("type"),
                        rs.getInt("duree"),
                        rs.getInt("nombres_de_fois")
                );
            }
        }
        return null;
    }
    @Override
    public List<exercices> getByIdEntrainemnt(int id) throws SQLException {
        System.out.println("xxxx"+id);
        List<exercices> exercises = new ArrayList<>();
        String sql = "SELECT e.* FROM exercices e INNER JOIN entrainements_exercices ee ON e.id = ee.exercices_id WHERE ee.entrainements_id =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(rs.toString());
            while (rs.next()) {
                exercices exercise = new exercices(
                        rs.getInt("id"),
                        rs.getString("nom_exercice"),
                        rs.getString("type"),
                        rs.getInt("duree"),
                        rs.getInt("nombres_de_fois")
                );
                exercises.add(exercise);
            }
        }
        return exercises;
    }

    @Override
    public List<exercices> getByTypeEntrainemnt(String id) throws SQLException {
        return null;
    }

}
