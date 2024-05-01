package org.example.services;

import org.example.model.Category;
import org.example.utils.MyConnections;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
    private Connection conn;

    public CategoryService() {
        conn = MyConnections.getInstance().getCnx();
    }

    public void insert(Category category) {
        String query = "INSERT INTO categorie (nom) VALUES (?)";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, category.getNom());
            pst.executeUpdate();
            System.out.println("Category inserted successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(Category category) {
        String query = "UPDATE categorie SET nom=? WHERE id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, category.getNom());
            pst.setInt(2, category.getId());
            pst.executeUpdate();
            System.out.println("Category updated successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM categorie WHERE id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Category deleted successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Category readById(int id) {
        String query = "SELECT * FROM categorie WHERE id=?";
        Category category = null;
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                category = new Category(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return category;
    }

    public Category readByName(String name) {
        String query = "SELECT * FROM categorie WHERE nom=?";
        Category category = null;
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                category = new Category(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return category;
    }

    public ArrayList<Category> readAll() {
        ArrayList<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
                categories.add(category);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categories;
    }


}