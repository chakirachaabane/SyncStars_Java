package tn.esprit.services;

import tn.esprit.services.Irdv;
import tn.esprit.models.rdv;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RdvService implements Irdv<rdv> {
    // Attributs
    Connection cnx = MyDatabase.getInstance().getConnection();

    // Actions
    @Override
    public boolean addRdv(rdv rdv) {
        // Convertir le format de date en 'YYYY-MM-DD'
        String formattedDate = rdv.getdate_rdv();

        // Construire la requête SQL
        String req = "INSERT INTO rdv (date_rdv, horaire_rdv, probleme) VALUES ('" + formattedDate + "', '" + rdv.gethoraire_rdv() + "', '" + rdv.getProbleme() + "')";

        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            return true; // Indiquer que l'ajout a été réussi
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du rendez-vous : " + e.getMessage());
            return false; // Indiquer que l'ajout a échoué
        }
    }

    @Override
    public void updateRdv(rdv rdv) {
        // Convertir le format de date en 'YYYY-MM-DD'
        String formattedDate = rdv.getdate_rdv();

        // Construire la requête SQL
        String req = "UPDATE rdv SET date_rdv = '" + formattedDate + "', horaire_rdv = '" + rdv.gethoraire_rdv() + "', probleme = '" + rdv.getProbleme() + "' WHERE id = " + rdv.getId();

        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du rendez-vous : " + e.getMessage());
            // Gérer l'erreur de la même manière que pour addRdv et deleteRdv
        }
    }

    @Override
    public void deleteRdv(int id) {
        String req = "DELETE FROM rdv WHERE id = " + id;
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Rendez-vous numéro " + id + " supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du rendez-vous : " + e.getMessage());
            // Gérer l'erreur de la même manière que pour addRdv et updateRdv
        }
    }

    @Override
    public List<rdv> getAll() {
        List<rdv> rdvs = new ArrayList<>();

        String req = "SELECT * FROM rdv";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                rdv rdv = new rdv();
                rdv.setId(res.getInt("id"));
                rdv.setdate_rdv(res.getString("date_rdv"));
                rdv.sethoraire_rdv(res.getString("horaire_rdv"));
                rdv.setProbleme(res.getString("probleme"));

                rdvs.add(rdv);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rendez-vous : " + e.getMessage());
            // Gérer l'erreur de la même manière que pour addRdv, updateRdv et deleteRdv
        }

        return rdvs;
    }

    @Override
    public rdv getOne(int id) {
        String req = "SELECT * FROM rdv WHERE id = " + id;
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            if (res.next()) {
                rdv rdv = new rdv();
                rdv.setId(res.getInt("id"));
                rdv.setdate_rdv(res.getString("date_rdv"));
                rdv.sethoraire_rdv(res.getString("horaire_rdv"));
                rdv.setProbleme(res.getString("probleme"));
                return rdv;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du rendez-vous : " + e.getMessage());
            // Gérer l'erreur de la même manière que pour les autres méthodes
            return null;
        }
    }

    @Override
    public List<rdv> getByProbleme(String probleme) {
        List<rdv> rdvs = new ArrayList<>();

        // Construire la requête SQL
        String req = "SELECT * FROM rdv WHERE probleme = '" + probleme + "'";

        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                rdv rdv = new rdv();
                rdv.setId(res.getInt("id"));
                rdv.setdate_rdv(res.getString("date_rdv"));
                rdv.sethoraire_rdv(res.getString("horaire_rdv"));
                rdv.setProbleme(res.getString("probleme"));

                rdvs.add(rdv);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rendez-vous par problème : " + e.getMessage());
            // Gérer l'erreur de la même manière que pour les autres méthodes
        }

        return rdvs;
    }

    // Méthode pour récupérer les rendez-vous par date
    public List<rdv> getRdvByDate(LocalDate date) {
        // Convertir la LocalDate en java.sql.Date
        Date sqlDate = Date.valueOf(date);

        // Créer une liste pour stocker les rendez-vous trouvés
        List<rdv> rdvs = new ArrayList<>();

        // Construire la requête SQL
        String req = "SELECT * FROM rdv WHERE date_rdv = '" + sqlDate + "'";

        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                rdv rdv = new rdv();
                rdv.setId(res.getInt("id"));
                rdv.setdate_rdv(res.getString("date_rdv"));
                rdv.sethoraire_rdv(res.getString("horaire_rdv"));
                rdv.setProbleme(res.getString("probleme"));

                rdvs.add(rdv);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rendez-vous par date : " + e.getMessage());
            // Gérer l'erreur de la même manière que pour les autres méthodes
        }

        return rdvs;
    }

    public List<rdv> getRdvForMonth(int monthValue, int year) {
        List<rdv> rdvs = new ArrayList<>();

        // Déterminer la première et la dernière date du mois
        LocalDate firstDayOfMonth = LocalDate.of(year, monthValue, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        // Construire la requête SQL pour récupérer les rendez-vous pour le mois donné
        String query = "SELECT * FROM rdv WHERE date_rdv BETWEEN '" + firstDayOfMonth + "' AND '" + lastDayOfMonth + "'";

        try {
            Statement statement = cnx.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Parcourir les résultats et créer des objets rdv
            while (resultSet.next()) {
                rdv rdv = new rdv();
                rdv.setId(resultSet.getInt("id"));
                rdv.setdate_rdv(String.valueOf(resultSet.getDate("date_rdv").toLocalDate()));
                // Autres attributs du rdv
                rdvs.add(rdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rdvs;
    }

    public int countRdvByDate(LocalDate date) {
        // Convertir la date en format java.sql.Date
        Date sqlDate = Date.valueOf(date);

        // Requête SQL pour compter le nombre de rendez-vous pour la date donnée
        String query = "SELECT COUNT(*) FROM rdv WHERE date_rdv = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Définir la date dans la requête SQL
            statement.setDate(1, sqlDate);

            // Exécuter la requête SQL et obtenir le résultat
            try (ResultSet resultSet = statement.executeQuery()) {
                // S'il y a un résultat, récupérer le nombre de rendez-vous
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si une erreur se produit ou s'il n'y a aucun résultat, retourner 0
        return 0;
    }

    public boolean isRdvExist(LocalDate date, String horaire) {
        // Convertir la date en format java.sql.Date
        Date sqlDate = Date.valueOf(date);

        // Requête SQL pour vérifier si un rendez-vous existe pour la date et l'horaire donnés
        String query = "SELECT COUNT(*) FROM rdv WHERE date_rdv = ? AND horaire_rdv = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Définir la date et l'horaire dans la requête SQL
            statement.setDate(1, sqlDate);
            statement.setString(2, horaire);

            // Exécuter la requête SQL et obtenir le résultat
            try (ResultSet resultSet = statement.executeQuery()) {
                // S'il y a un résultat, vérifier si le nombre de rendez-vous est supérieur à 0
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si une erreur se produit ou s'il n'y a aucun résultat, retourner false
        return false;
    }

    public int getNombreRdvParMois(int month) {
        // Déterminer la première et la dernière date du mois
        LocalDate firstDayOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        // Construire la requête SQL pour compter les rendez-vous pour le mois donné
        String query = "SELECT COUNT(*) FROM rdv WHERE date_rdv BETWEEN '" + firstDayOfMonth + "' AND '" + lastDayOfMonth + "'";

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Si une ligne est retournée, récupérer le nombre de rendez-vous
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de rendez-vous pour le mois " + month + ": " + e.getMessage());
        }

        // Retourner 0 en cas d'erreur ou si aucun rendez-vous n'est trouvé
        return 0;
    }

}
