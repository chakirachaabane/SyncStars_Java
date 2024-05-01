package services;

import model.entrainements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.exercices;
import utils.MyDatabase;

public class EntrainementServices implements IService<entrainements> {
    private Connection connection;

    public EntrainementServices() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public List<entrainements> getByIdEntrainemnt(int id) throws SQLException {return null ;}

    @Override
    public List<entrainements> getByTypeEntrainemnt(String id) throws SQLException {
        String sql = "SELECT * FROM entrainements WHERE objectif = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        List<entrainements> entrainements = new ArrayList<>();
        while (rs.next()) {
            model.entrainements entrainement = new entrainements();
            entrainement.setId(rs.getInt("id"));
            entrainement.setNiveau(rs.getString("niveau"));
            entrainement.setObjectif(rs.getString("objectif"));
            entrainement.setDuree(rs.getInt("duree"));
            entrainement.setNom_entrainement(rs.getString("nom_entrainement"));
            entrainement.setPeriode(rs.getInt("periode"));

            entrainements.add(entrainement);
        }
        return entrainements;
    }

    @Override
    public void add(entrainements entrainements) throws SQLException {
        String sql = "INSERT INTO entrainements (niveau, objectif, duree, nom_entrainement, periode)" +
                " VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, entrainements.getNiveau());
        preparedStatement.setString(2, entrainements.getObjectif());
        preparedStatement.setInt(3, entrainements.getDuree());
        preparedStatement.setString(4, entrainements.getNom_entrainement());
        preparedStatement.setInt(5, entrainements.getPeriode());

        preparedStatement.executeUpdate();
    }

    @Override
    public void addExercice(entrainements entrainements, int id) throws SQLException {

    }

    @Override
    public void update(entrainements entrainements) throws SQLException {
        String sql = "UPDATE entrainements SET niveau = ?, objectif = ?, duree = ?, nom_entrainement = ?, periode = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, entrainements.getNiveau());
        preparedStatement.setString(2, entrainements.getObjectif());
        preparedStatement.setInt(3, entrainements.getDuree());
        preparedStatement.setString(4, entrainements.getNom_entrainement());
        preparedStatement.setInt(5, entrainements.getPeriode());
        preparedStatement.setInt(6, entrainements.getId());

        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM entrainements WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<entrainements> getAll() throws SQLException {
        String sql = "SELECT * FROM entrainements";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<entrainements> entrainements = new ArrayList<>();
        while (rs.next()) {
            model.entrainements entrainement = new entrainements();
            entrainement.setId(rs.getInt("id"));
            entrainement.setNiveau(rs.getString("niveau"));
            entrainement.setObjectif(rs.getString("objectif"));
            entrainement.setDuree(rs.getInt("duree"));
            entrainement.setNom_entrainement(rs.getString("nom_entrainement"));
            entrainement.setPeriode(rs.getInt("periode"));

            entrainements.add(entrainement);
        }
        return entrainements;
    }

    @Override
    public entrainements getById(int id) throws SQLException {
        String sql = "SELECT * FROM entrainements WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            entrainements entrainements = new entrainements();
            entrainements.setId(rs.getInt("id"));
            entrainements.setNiveau(rs.getString("niveau"));
            entrainements.setObjectif(rs.getString("objectif"));
            entrainements.setDuree(rs.getInt("duree"));
            entrainements.setNom_entrainement(rs.getString("nom_entrainement"));
            entrainements.setPeriode(rs.getInt("periode"));
            return entrainements;
        }
        return null;
    }
}
