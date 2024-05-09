package tn.esprit.services;

import tn.esprit.models.rdv;

import java.sql.SQLException;
import java.util.List;

public interface Irdv <R> {
    //CRUDs


     //1 : Ajout RDV
     boolean addRdv(R r);


     //2 : Update RDV
     void updateRdv(R r);



     //3 : Suppression RDV
     void deleteRdv(int r);



    //4 : Affichage RDVs
List<R> getAll() throws SQLException;


    // 5 : One
    R getOne(int id);

    //6 :getByCriteria

    List<rdv> getByProbleme(String probleme);



}
