package org.example.services;

import org.example.model.Evenement;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesEvenement implements IService<Evenement> {
    private Connection cnx ;
    public ServicesEvenement(){
        cnx =MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Evenement evenement)  {

        if (evenement.getTitre() == null || evenement.getTitre().isEmpty() ||
                evenement.getAdresse() == null || evenement.getAdresse().isEmpty() ||
                evenement.getCategorie() == null || evenement.getCategorie().isEmpty() ||
                evenement.getFormat() == null || evenement.getFormat().isEmpty() ||
                evenement.getDate() == null) {
            System.out.println("Saisie invalide. Veuillez remplir tous les champs.");
            return;
        }

        /*java.util.Date dateActuelle = new java.util.Date();
        if (evenement.getDate().compareTo(dateActuelle) <= 0) {
            System.out.println("La date de l'événement doit être postérieure à la date actuelle.");
            return;
        }*/

        String req = "INSERT INTO event (id, titre, adresse, categorie_id, format_id,date,image) " +
                "VALUES (?, ?, ?, ?, ?,?,?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, evenement.getId());
            stm.setString(2, evenement.getTitre());
            stm.setString(3, evenement.getAdresse());
            stm.setString(4, evenement.getCategorie());
            stm.setString(5, evenement.getFormat());
            stm.setDate(6, new java.sql.Date(evenement.getDate().getTime()));
            stm.setString(7, evenement.getImage());
            stm.executeUpdate();

            System.out.println("Evénement ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void modifier(Evenement evenement)  {

        if (evenement.getTitre() == null || evenement.getTitre().isEmpty() ||
                evenement.getAdresse() == null || evenement.getAdresse().isEmpty() ||
                evenement.getCategorie() == null || evenement.getCategorie().isEmpty() ||
                evenement.getFormat() == null || evenement.getFormat().isEmpty() ||
                evenement.getDate() == null) {
            System.out.println("Saisie invalide. Veuillez remplir tous les champs.");
            return;
        }

        java.util.Date dateActuelle = new java.util.Date();
        if (evenement.getDate().compareTo(dateActuelle) <= 0) {
            System.out.println("La date de l'événement doit être postérieure à la date actuelle.");
            return;
        }

        String req = "UPDATE event SET titre=?, adresse=?, categorie_id=?, format_id=? , date =? , image=? WHERE id=?";
        try (PreparedStatement pre = cnx.prepareStatement(req)) {
            pre.setString(1, evenement.getTitre());
            pre.setString(2, evenement.getAdresse());
            pre.setString(3, evenement.getCategorie());
            pre.setString(4, evenement.getFormat());
            pre.setDate(5, new java.sql.Date(evenement.getDate().getTime()));
            pre.setString(6, evenement.getImage());
            pre.setInt(7, evenement.getId());
            pre.executeUpdate();

            System.out.println("Evénement modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void supprimer(Evenement evenement) {

        try {
            String req = " delete from event where id=?";
            PreparedStatement pre = cnx.prepareStatement(req);
            pre.setInt(1, evenement.getId());
            pre.executeUpdate();
            System.out.println("Evenement est supprimé");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void supprimerParId(int Id) {

        try {
            // Préparer la requête de suppression
            String req = "delete from event where id=?";
            PreparedStatement pre = cnx.prepareStatement(req);

            pre.setInt(1, Id);
            pre.executeUpdate();
            System.out.println("Evénement supprimé");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'evenement' : " + e.getMessage());
        }
    }

    public List<Evenement> afficher() throws SQLException {

        String req = "select * from event";
        Statement ste = cnx.createStatement();
        ResultSet res = ste.executeQuery(req);
        List<Evenement> list = new ArrayList<>();
        while (res.next()) {
            Evenement e = new Evenement();
            e.setId(res.getInt(1));
            e.setTitre(res.getString(2));
            e.setAdresse(res.getString(3));
            e.setDate(res.getDate(4));
            e.setFormat(res.getString(5));
            e.setCategorie(res.getString(6));
            e.setImage(res.getString(7));



            list.add(e);

        }
        return list;
    }



}