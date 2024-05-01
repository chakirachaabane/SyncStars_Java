package org.example.services;


import javafx.scene.control.Alert;
import org.example.model.Category;
import org.example.model.Evenement;
import org.example.model.Format;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.utils.MyConnections;

public class EvenementService {
    private Connection conn;

    public EvenementService() {
        conn = MyConnections.getInstance().getCnx();
    }

    public void insert(Evenement evenement) {
        String query = "INSERT INTO evenement (titre,adresse,date,categorie_id,format_id,image,description,nbPlaces) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, evenement.getTitre());
            pst.setString(2, evenement.getAdresse());
            pst.setDate(3, evenement.getDate());
            pst.setInt(4, evenement.getCategorie().getId());
            pst.setInt(5, evenement.getFormat().getId());
            pst.setString(6, evenement.getImage());
            pst.setString(7, evenement.getDescription());
            pst.setInt(8, evenement.getNbPlaces());
            pst.executeUpdate();
            System.out.println("Event added successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void update(Evenement evenement) {
        String query = "UPDATE evenement SET titre = ?, format_id = ?, categorie_id = ?, adresse = ?, image = ?, date = ?, nbPlaces = ?, description = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, evenement.getTitre());
            pst.setInt(2, evenement.getFormat().getId());
            pst.setInt(3, evenement.getCategorie().getId());
            pst.setString(4, evenement.getAdresse());
            pst.setString(5, evenement.getImage());
            pst.setDate(6, evenement.getDate());
            pst.setInt(7, evenement.getNbPlaces());
            pst.setString(8, evenement.getDescription());
            pst.setInt(9, evenement.getId());
            pst.executeUpdate();
            System.out.println("Event updated successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM evenement WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Event deleted successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Evenement getById(int id) {
        String query = "SELECT * FROM evenement WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("adresse"),
                        rs.getDate("date"),
                        new Category(rs.getInt("categorie_id")), // Assuming you have a Category constructor with an ID
                        new Format(rs.getInt("format_id"), ""),     // Assuming you have a Format constructor with an ID
                        rs.getString("image"),
                        rs.getString("description"),
                        rs.getInt("nbPlaces")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Evenement> getAll() {
        CategoryService x = new CategoryService();
        FormatService y = new FormatService();
        String query = "SELECT * FROM evenement";
        ArrayList<Evenement> events = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Evenement event = new Evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("adresse"),
                        rs.getDate("date"),
                        x.readById(rs.getInt("categorie_id")), // Assuming you have a Category constructor with an ID
                        y.readById(rs.getInt("format_id")),   // Assuming you have a Format constructor with an ID
                        rs.getString("image"),
                        rs.getString("description"),
                        rs.getInt("nbPlaces")
                );
                events.add(event);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return events;
    }

    public List<Evenement> getByName(String titre) {
        List<Evenement> eventList = new ArrayList<>();
        try {

            String req = "SELECT * FROM evenement WHERE titre LIKE ?";
            PreparedStatement st = conn.prepareStatement(req);
            st.setString(1, "%" + titre + "%");
            ResultSet pst = st.executeQuery();
            while (pst.next()) {
                Evenement e = new Evenement();
                e.setId(pst.getInt("id"));
                e.setTitre(pst.getString("titre"));
                e.setAdresse(pst.getString("adresse"));

                // Convertir java.sql.Date en LocalDate
                Date date = pst.getDate("date");
                e.setDate(date.toLocalDate());

                // Récupérer la catégorie et le format à partir de la base de données
                int categoryId = pst.getInt("categorie_id");
                Category category = new CategoryService().readById(categoryId);

                int formatId = pst.getInt("format_id");
                Format format = new FormatService().readById(formatId);

                e.setCategorie(category);
                e.setFormat(format);
                e.setImage(pst.getString("image"));
                e.setDescription(pst.getString("description"));
                e.setNbPlaces(pst.getInt("nbPlaces"));

                eventList.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (eventList.isEmpty()) {
            System.out.println("Événement introuvable");
        }
        return eventList;
    }

    public void decrementNbPlaces(Evenement evenement, int nbTickets) {
        int nbPlaces = evenement.getNbPlaces();
        if (nbTickets > 0 && nbPlaces >= nbTickets) {
            evenement.setNbPlaces(nbPlaces - nbTickets);
            update(evenement); // Mettre à jour l'événement dans la base de données
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Échec");
            alert.setHeaderText(null);
            alert.setContentText("Le nombre de places demandé est invalide ou insuffisant.");
            alert.showAndWait();
        }
    }

    public Map<Category, Long> getDataByCategory() {
        Map<Category, Long> dataByCategory = new HashMap<>();
        String query = "SELECT categorie_id, COUNT(*) AS count FROM evenement GROUP BY categorie_id";
        try (Connection conn = MyConnections.getInstance().getCnx();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            CategoryService categoryService = new CategoryService();

            while (rs.next()) {
                int categoryId = rs.getInt("categorie_id");
                Category category = categoryService.readById(categoryId); // Utiliser readById pour obtenir la catégorie par ID
                if (category != null) {
                    long count = rs.getLong("count");
                    dataByCategory.put(category, count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataByCategory;
    }

    public Map<Integer, Long> getDataByYear() {
        Map<Integer, Long> dataByYear = new HashMap<>();
        String query = "SELECT YEAR(date) AS year, COUNT(*) AS count FROM event GROUP BY YEAR(date)";
        try (Connection conn = MyConnections.getInstance().getCnx();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int year = rs.getInt("year");
                long count = rs.getLong("count");
                dataByYear.put(year, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataByYear;
    }

    public List<Evenement> getByCategoryAndFormat(Category category, Format format) {
        List<Evenement> filteredEvents = new ArrayList<>();
        List<Evenement> allEvents = getAll(); // Supposons que getAll() retourne tous les événements

        for (Evenement event : allEvents) {
            boolean categoryMatch = category == null || event.getCategorie().getId() == category.getId();
            boolean formatMatch = format == null || event.getFormat().getId() == format.getId();

            if (categoryMatch && formatMatch) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;}
}
