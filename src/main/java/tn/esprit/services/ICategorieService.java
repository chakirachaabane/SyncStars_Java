package tn.esprit.services;

import tn.esprit.models.Categorie;

import java.sql.SQLException;
import java.util.List;

public interface ICategorieService<Categorie> {
    void add(Categorie categorie) ;
    List<Categorie> getAllCategories();
    void updateCategorie(Categorie categorie);
    void deleteCategorie(int id);
    Categorie getByIdCategorie(int id) throws SQLException;
}
