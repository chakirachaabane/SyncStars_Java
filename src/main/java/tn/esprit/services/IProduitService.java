package tn.esprit.services;

import javafx.collections.ObservableList;
import tn.esprit.models.Produit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IProduitService<Produit>{
    void addProduits(Produit produit);
    void deleteProduit(int id);
    List<Produit> getAllProduits();
    void updateProduit(Produit produit);

  Produit getByIdProduit(int id) throws SQLException;

  ObservableList<Produit> searchProduitsByName(String nom);
 List<Produit> getProduitsByCategory(String categoryName);
    float TotalProduit(Produit produit);
   void addToPanier(Produit produit);
    void incrementQuantity(int productId);
    void decrementQuantity(int productId);
     List<Produit> getPanierList();
     void updateNbVentes(List<Produit> panier);
    int getNbVentesForProduit(int productId) throws SQLException;
    List<Produit> getTopVenteProduits();
    void removeFromPanier(int produitId);
}
