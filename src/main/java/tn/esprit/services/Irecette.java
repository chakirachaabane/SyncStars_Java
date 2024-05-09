package tn.esprit.services;

import tn.esprit.models.recette;

import java.sql.SQLException;
import java.util.List;

public interface Irecette <Recette>
{
    //CRUDs


    //1 : Ajout Recette
    boolean addRecette(Recette r);


    //2 : Update Recette
    void updateRecette(Recette r);



    //3 : Suppression Recette
    void deleteRecette(Recette r);



    //4 : Affichage Recettes
    List<Recette> getAllRecettes() throws SQLException;


    // 5 : One
    Recette getOneRecette(int id);

    //6 :getByCriteria

    List<recette> getByProbleme(String probleme);


}
