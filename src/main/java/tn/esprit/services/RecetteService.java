package tn.esprit.services;

import tn.esprit.interfaces.Irecette;
import tn.esprit.models.recette;
import tn.esprit.util.MaConnexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RecetteService implements Irecette<recette> {
    // Attributs
    Connection cnx = MaConnexion.getInstance().getCnx();



    // Actions




    @Override
    public boolean addRecette(recette recette) {
        // Vérification des champs obligatoires et des contraintes
        if (recette.getNom_recette() == null || recette.getNom_recette().isEmpty()) {
            System.out.println("ATTENTION AJOUT RECETTE: Le champ nom_recette est obligatoire !");
            return false;
        }



        if (recette.getIngredients() == null || recette.getIngredients().isEmpty()) {
            System.out.println("ATTENTION AJOUT RECETTE: Le champ ingredients est obligatoire !");
            return false;
        }

        if (recette.getEtapes() == null || recette.getEtapes().isEmpty()) {
            System.out.println("ATTENTION AJOUT RECETTE: Le champ etapes est obligatoire !");
            return false;
        }

        if (recette.getProbleme() == null || recette.getProbleme().isEmpty()) {
            System.out.println("ATTENTION AJOUT RECETTE: Le champ probleme est obligatoire !");
            return false;
        }

        if (!validerProbleme(recette.getProbleme())) {
            System.out.println("ATTENTION AJOUT RECETTE: Le champ probleme doit être parmi  : Obésité/ Diabète/ Anorexie/ Déséquilibre alimentaire/ Cholestérol.");
            return false;
        }

        // Construction de la requête SQL pour l'ajout
        String req = "INSERT INTO recette (nom_recette, ingredients, etapes, probleme) VALUES ('" + recette.getNom_recette() + "', '" + recette.getIngredients() + "', '" + recette.getEtapes() + "', '" + recette.getProbleme() + "')";

        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Recette ajoutée avec succès !");
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la recette : " + e.getMessage());
            return false;
        }
    }


    private boolean validerNomRecette(String nomRecette) {
        return Pattern.matches("^[a-zA-Z]+$", nomRecette);
    }


    private boolean validerProbleme(String probleme) {
        List<String> problemesAutorises = Arrays.asList("Obésité", "Diabète", "Anorexie", "Déséquilibre alimentaire", "Cholestérol");
        return problemesAutorises.contains(probleme);
    }

    @Override
    public void updateRecette(recette recette) {
        // Vérification des champs obligatoires et des contraintes
        if (recette.getNom_recette() == null || recette.getNom_recette().isEmpty()) {
            System.out.println("ATTENTION UPDATE RECETTE: Le champ nom_recette est obligatoire !");
            return;
        }



        if (recette.getIngredients() == null || recette.getIngredients().isEmpty()) {
            System.out.println("ATTENTION UPDATE RECETTE: Le champ ingredients est obligatoire !");
            return;
        }

        if (recette.getEtapes() == null || recette.getEtapes().isEmpty()) {
            System.out.println("ATTENTION UPDATE RECETTE: Le champ etapes est obligatoire !");
            return;
        }

        if (recette.getProbleme() == null || recette.getProbleme().isEmpty()) {
            System.out.println("ATTENTION UPDATE RECETTE: Le champ probleme est obligatoire !");
            return;
        }

        if (!validerProbleme(recette.getProbleme())) {
            System.out.println("ATTENTION UPDATE RECETTE: Le champ probleme doit être parmi  : Obésité/ Diabète/ Anorexie/ Déséquilibre alimentaire/ Cholestérol.");
            return;
        }

        // Construction de la requête SQL pour la mise à jour
        String req = "UPDATE recette SET nom_recette = '" + recette.getNom_recette() + "', ingredients = '" + recette.getIngredients() + "', etapes = '" + recette.getEtapes() + "', probleme = '" + recette.getProbleme() + "' WHERE id = " + recette.getId();

        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteRecette(recette recette) {
        String req = "DELETE FROM recette WHERE id = " + recette.getId();
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<recette> getAllRecettes() throws SQLException {
        List<recette> recettes = new ArrayList<>();
        String req = "SELECT * FROM recette";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                recette recette = new recette();
                recette.setId(res.getInt("id"));
                recette.setNom_recette(res.getString("nom_recette"));
                recette.setIngredients(res.getString("ingredients"));
                recette.setEtapes(res.getString("etapes"));
                recette.setProbleme(res.getString("probleme"));
                recettes.add(recette);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération des recettes: " + e.getMessage());
        }
        return recettes;
    }

    @Override
    public recette getOneRecette(int id) {
        recette recetteFound = null;
        String req = "SELECT * FROM recette WHERE id = " + id;
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            if (res.next()) {
                recetteFound = new recette();
                recetteFound.setId(res.getInt("id"));
                recetteFound.setNom_recette(res.getString("nom_recette"));
                recetteFound.setIngredients(res.getString("ingredients"));
                recetteFound.setEtapes(res.getString("etapes"));
                recetteFound.setProbleme(res.getString("probleme"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recetteFound;
    }


    @Override
    public List<recette> getByProbleme(String probleme) {
        List<recette> recettes = new ArrayList<>();

        // Construire la requête SQL
        String req = "SELECT * FROM recette WHERE probleme = '" + probleme + "'";

        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                recette R1 = new recette();
                R1.setId(res.getInt("id"));
                R1.setNom_recette(res.getString("nom_recette"));
                R1.setIngredients(res.getString("ingredients"));
                R1.setEtapes(res.getString("etapes"));
                R1.setProbleme(res.getString("probleme"));

                recettes.add(R1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recettes;
    }

}
