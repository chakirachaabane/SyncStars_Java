package tn.esprit.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.models.Categorie;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategorieService implements ICategorieService<Categorie> {
    public Connection connection;

    public CategorieService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Categorie categorie) {
        try {
            String sql = "INSERT INTO `categorie` (`nomc`, `descriptionc`, `disponibilite`) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, categorie.getNomc());
            ps.setString(2, categorie.getDescription());
            ps.setInt(3, categorie.getDisponibilite());
            ps.executeUpdate();
            System.out.println("Categorie ajoutée avec succès");
            ps.close();
        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de la récupération du produit : " + e.getMessage());
        }

    }

    @Override
    public ObservableList<Categorie> getAllCategories() {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM `categorie`";
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Categorie c = new Categorie();
                c.setIdCategorie(rs.getInt("id"));
                c.setNomc(rs.getString("nomc"));
                c.setDescription(rs.getString("descriptionc"));
                c.setDisponibilite(rs.getInt("disponibilite"));
                categories.add(c);
            }
        } catch (SQLException e) {
            Logger.getLogger(CategorieService.class.getName()).log(Level.SEVERE, "error in display!!", e);
        }
        return categories;
    }

    @Override
    public void updateCategorie(Categorie categorie) {
        try {
            String sql = "UPDATE categorie SET nomc = ?, descriptionc = ?, disponibilite = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categorie.getNomc());
            preparedStatement.setString(2, categorie.getDescription());
            preparedStatement.setInt(3, categorie.getDisponibilite());
            preparedStatement.setInt(4, categorie.getIdCategorie());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(CategorieService.class.getName()).log(Level.SEVERE, "error in modify!!", e);
        }

    }

    @Override
    public void deleteCategorie(int id) {
        try {
            String sql = "delete from categorie where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(CategorieService.class.getName()).log(Level.SEVERE, "error in delete!!", e);
        }
    }

    public Categorie getByIdCategorie(int id) throws SQLException {
        String req = "SELECT * FROM `categorie` where id = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();


        Categorie categorie = new Categorie();

        while (rs.next()) {
            categorie.setIdCategorie(rs.getInt("id"));
            categorie.setNomc(rs.getString("nomc"));
            categorie.setDescription(rs.getString("descriptionc"));
            categorie.setDisponibilite(rs.getInt("disponibilite"));
        }
        ps.close();
        return categorie;
    }

    public boolean isCategorieExists(String categorieName) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) AS count FROM categorie WHERE nomc = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,categorieName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                exists = count > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(CategorieService.class.getName()).log(Level.SEVERE, "Erreur lors de la vérification de l'existence du categorie : ", e);
        }
        return exists;
    }
}

