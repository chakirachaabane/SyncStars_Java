package tn.esprit.services;


import tn.esprit.models.Format;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormatService {
    private Connection conn;

    public FormatService() {
        conn = MyDatabase.getInstance().getConnection();
    }

    public void insert(Format format) {
        String query = "INSERT INTO format (nom) VALUES (?)";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, format.getNom());
            pst.executeUpdate();
            System.out.println("Format inserted successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(FormatService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Format readByName(String name) {
        String query = "SELECT * FROM format WHERE nom=?";
        Format format = null;
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                format = new Format(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return format;
    }

    public void update(Format format) {
        String query = "UPDATE format SET nom=? WHERE id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, format.getNom());
            pst.setInt(2, format.getId());
            pst.executeUpdate();
            System.out.println("Format updated successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(FormatService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM format WHERE id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Format deleted successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(FormatService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Format readById(int id) {
        String query = "SELECT * FROM format WHERE id=?";
        Format format = null;
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                format = new Format(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return format;
    }

    public ArrayList<Format> readAll() {
        ArrayList<Format> formats = new ArrayList<>();
        String query = "SELECT * FROM format";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Format format = new Format(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
                formats.add(format);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formats;
    }


}