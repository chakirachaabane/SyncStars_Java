package org.example.test;

import org.example.model.Evenement;
import org.example.services.ServicesEvenement;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {
    public static void main(String[] args) throws ParseException {


        ServicesEvenement sp = new ServicesEvenement();

        // Création du format de date souhaité
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        // Création de la date en format String
        String dateString = "05-04-2024";
        // Conversion du String en Date
        Date date = sdf.parse(dateString);
        // Convertir la date en java.sql.Date
        java.sql.Date EveDate = new java.sql.Date(date.getTime());

        // ajouter les EVENEMENTS
            Evenement eb = new Evenement(1,"event national ","bizerte", EveDate ,"sante","Festivale","image1");
            Evenement es = new Evenement(2,"sport et vie ","sousse", EveDate ,"sport","Conference","image2");

            // Modifier les EVENEMENTS
            /*eb.setTitre("Event national modified");
            eb.setAdresse("Bizerte modified");
            eb.setDate(EveDate);
            eb.setCategorie("health");
            eb.setFormat("festivale");
            eb.setImage("image32"); */


            // ajouter les EVENEMENTS
            //sp.ajouter(eb);

            // Modifier les EVENEMENTS
            //sp.modifier(eb);

            //supprimer EVENEMENT
            //sp.supprimer(es);
            //sp.supprimerParId(1);


            // AFFICHER EVENEMENT
            try {
                System.out.println(sp.afficher());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }}





}
