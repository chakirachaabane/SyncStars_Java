package tn.esprit.services;


import javafx.scene.control.Alert;
import tn.esprit.models.Evenement;
import tn.esprit.models.Category;
import tn.esprit.models.Format;
import tn.esprit.services.CategorieService;
import tn.esprit.services.FormatService;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import tn.esprit.utils.MyDatabase;

public class EvenementService {
    private Connection conn;

    public EvenementService() {
        conn = MyDatabase.getInstance().getConnection();
    }

    public void insert(Evenement evenement) {
        String query = "INSERT INTO evenement (categorie_id, format_id, titre, date, heure, description, image, adresse, nb_places) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, evenement.getCategorie().getId());
            pst.setInt(2, evenement.getFormat().getId());
            pst.setString(3, evenement.getTitre());
            pst.setDate(4, evenement.getDate());
            pst.setTime(5, evenement.getHeure());
            pst.setString(6, evenement.getDescription());
            pst.setString(7, evenement.getImage());
            pst.setString(8, evenement.getAdresse());
            pst.setInt(9, evenement.getNbPlaces());
            pst.executeUpdate();
            System.out.println("Event added successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(EvenementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void update(Evenement evenement) {
        String query = "UPDATE evenement SET categorie_id = ?, format_id = ?, titre = ?, date = ?, heure = ?,description = ?, image = ?, adresse = ?, nb_places = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, evenement.getCategorie().getId());
            pst.setInt(2, evenement.getFormat().getId());
            pst.setString(3, evenement.getTitre());
            pst.setDate(4, evenement.getDate());
            pst.setTime(5, evenement.getHeure());
            pst.setString(6, evenement.getDescription());
            pst.setString(7, evenement.getImage());
            pst.setString(8, evenement.getAdresse());
            pst.setInt(9, evenement.getNbPlaces());
            pst.setInt(10, evenement.getId());
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
                        new Category(rs.getInt("categorie_id")), // Assuming you have a Category constructor with an ID
                        new Format(rs.getInt("format_id"), ""),     // Assuming you have a Format constructor with an ID
                        rs.getString("titre"),
                        rs.getDate("date"),
                        rs.getTime("heure"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("adresse"),
                        rs.getInt("nb_places")
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
                        x.readById(rs.getInt("categorie_id")), // Assuming you have a Category constructor with an ID
                        y.readById(rs.getInt("format_id")),   // Assuming you have a Format constructor with an ID
                        rs.getString("titre"),
                        rs.getDate("date"),
                        rs.getTime("heure"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("adresse"),
                        rs.getInt("nb_places")
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

                e.setHeure(pst.getTime("heure"));

                // Récupérer la catégorie et le format à partir de la base de données
                int categoryId = pst.getInt("categorie_id");
                Category category = new CategoryService().readById(categoryId);

                int formatId = pst.getInt("format_id");
                Format format = new FormatService().readById(formatId);

                e.setCategorie(category);
                e.setFormat(format);
                e.setImage(pst.getString("image"));
                e.setDescription(pst.getString("description"));
                e.setNbPlaces(pst.getInt("nb_places"));

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
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

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
        String query = "SELECT YEAR(date) AS year, COUNT(*) AS count FROM evenement GROUP BY YEAR(date)";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

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
