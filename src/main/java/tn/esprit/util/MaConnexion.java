package tn.esprit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MaConnexion {

    //DB
    final String URL="jdbc:mysql://localhost:3306/projetpi";
    final String  USR = "root"; // les permissions

    final String MDP = ""; // le MDP

    //Attributs
     Connection cnx; //c'est la variable ou on va stocker l'établissement de la cnx de la base de données //

    //Singleton step 2 : Création d'une variable de même type que la classe
    private static MaConnexion instance;
    //Constructeurs de MaConnexion pour initialiser ses attributs

    //Singleton step 1 :Privatisation du constructeur
    private MaConnexion()  {
        try {
            cnx = DriverManager.getConnection(URL, USR, MDP);
            System.out.println("Connexion établie avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    //Singleton step 3 : check if instance est nulle
    public static MaConnexion getInstance() {
        if (instance == null)
            instance = new MaConnexion();

        return instance;
    }
}
