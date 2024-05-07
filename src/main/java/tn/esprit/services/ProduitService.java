package tn.esprit.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.models.Categorie;
import tn.esprit.models.Produit;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProduitService implements IProduitService<Produit> {

    static Connection connection = MyDatabase.getInstance().getConnection();

    private static List<Produit> panier = new ArrayList<>();

    public ProduitService() {
    }

    public void addProduits(Produit produit) {
        try {
            String sql = "INSERT INTO produit(image, nomp, description, prix, dateperemption, stock, dateproduction,idcategorie_id) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, produit.getImage());
            ps.setString(2, produit.getNomp());
            ps.setString(3, produit.getDescription());
            ps.setFloat(4, produit.getPrix());
            ps.setDate(5, Date.valueOf(produit.getDatePeremption()));
            ps.setInt(6, produit.getStock());
            ps.setDate(7, Date.valueOf(produit.getDateProduction()));
            ps.setInt(8, produit.getCategorie().getIdCategorie());
            System.out.println("Produit ajouté avec succès");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de la récupération du produit : " + e.getMessage());
        }
    }

    //combobox
    public List<String> getAllCategories() {
        List<String> categoryNames = new ArrayList<>();

        try {
            String req = "SELECT nomc FROM categorie";
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String categoryName = rs.getString("nomc");
                categoryNames.add(categoryName);
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de la récupération des catégories : " + e.getMessage());
        }

        return categoryNames;
    }

    public int getIdCategorieByName(String categoryName) {
        int categoryId = -1;

        try {
            String req = "SELECT id FROM categorie WHERE nomc = ?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, categoryName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                categoryId = rs.getInt("id");
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de la récupération de l'ID de la catégorie : " + e.getMessage());
        }

        return categoryId;
    }

    @Override
    public ObservableList<Produit> getAllProduits() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM `produit`";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Produit p = new Produit();
                p.setId(rs.getInt("id"));
                p.setImage(rs.getString("image"));
                p.setNomp(rs.getString("nomp"));
                p.setDescription(rs.getString("description"));
                p.setPrix(rs.getFloat("prix"));
                Date datePeremption = rs.getDate("dateperemption");
                p.setDatePeremption(datePeremption.toLocalDate());
                p.setStock(rs.getInt("stock"));
                Date dateProduction = rs.getDate("dateproduction");
                p.setDateProduction(dateProduction.toLocalDate());
//                p.setIdCategorie(rs.getInt("idcategorie_id"));
//                p.getCategorie().setIdCategorie(rs.getInt("idcategorie_id"));
                if (p.getCategorie() != null) {
                    p.getCategorie().setIdCategorie(rs.getInt("idcategorie_id"));
                }


                produits.add(p);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "error in display!!", e);
        }
        return produits;
    }

    public String getNomCategorieById(int idCategorie) {
        String nomCategorie = "";
        try {
            String req = "SELECT nomc FROM categorie WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, idCategorie);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nomCategorie = rs.getString("nomc");
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de la récupération du nom de la catégorie : " + e.getMessage());
        }
        return nomCategorie;
    }

    @Override
    public void updateProduit(Produit produit) {
        try {
            String sql = "UPDATE produit SET image = ?, nomp = ?, description = ?, prix = ?, dateperemption = ?,stock= ?,dateproduction= ? , idcategorie_id = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(9, produit.getId());
            preparedStatement.setString(1, produit.getImage());
            preparedStatement.setString(2, produit.getNomp());
            preparedStatement.setString(3, produit.getDescription());
            preparedStatement.setFloat(4, produit.getPrix());
            preparedStatement.setDate(5, java.sql.Date.valueOf(produit.getDatePeremption()));
            preparedStatement.setInt(6, produit.getStock());
            preparedStatement.setDate(7, java.sql.Date.valueOf(produit.getDateProduction()));
            preparedStatement.setInt(8, produit.getCategorie().getIdCategorie());

            preparedStatement.executeUpdate();
            System.out.println("Produit modifié avec succès");
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "error in modify!!", e);
        }

    }

    @Override
    public void deleteProduit(int id) {
        try {
            String sql = "delete from produit where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(CategorieService.class.getName()).log(Level.SEVERE, "error in delete!!", e);
        }
    }

    @Override
    public Produit getByIdProduit(int id) throws SQLException {
        String req = "SELECT * FROM `produit` where id = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();


        Produit produit = new Produit();

        while (rs.next()) {
            produit.setId(rs.getInt("id"));
            produit.setImage(rs.getString("image"));
            produit.setNomp(rs.getString("nomp"));
            produit.setDescription(rs.getString("description"));
            produit.setPrix(rs.getFloat("prix"));
            produit.setDatePeremption(rs.getDate("dateperemption").toLocalDate());
            produit.setStock(rs.getInt("stock"));
            produit.setDateProduction(rs.getDate("dateproduction").toLocalDate());
//           produit.setIdCategorie(rs.getInt("idcategorie_id"));
            produit.getCategorie().setIdCategorie(rs.getInt("idcategorie_id"));
            ;
        }
        ps.close();
        return produit;
    }

    @Override
    public List<Produit> getProduitsByCategory(String categoryName) {
        List<Produit> produits = new ArrayList<>();
        try {
            String sql = "SELECT * FROM produit WHERE idcategorie_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getIdCategorieByName(categoryName));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Produit p = new Produit();
                p.setId(rs.getInt("id"));
                p.setImage(rs.getString("image"));
                p.setNomp(rs.getString("nomp"));
                p.setDescription(rs.getString("description"));
                p.setPrix(rs.getFloat("prix"));
                p.setDatePeremption(rs.getDate("dateperemption").toLocalDate());
                p.setStock(rs.getInt("stock"));
                p.setDateProduction(rs.getDate("dateproduction").toLocalDate());
                p.getCategorie().setIdCategorie(rs.getInt("idcategorie_id"));
//                p.setIdCategorie(rs.getInt("idcategorie_id"));
                produits.add(p);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "Error while getting products by category: ", e);
        }
        return produits;
    }

    @Override
    public ObservableList<Produit> searchProduitsByName(String nom) {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM `produit` WHERE nomp LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + nom + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Produit p = new Produit();
                p.setId(rs.getInt("id"));
                p.setImage(rs.getString("image"));
                p.setNomp(rs.getString("nomp"));
                p.setDescription(rs.getString("description"));
                p.setPrix(rs.getFloat("prix"));
                Date datePeremption = rs.getDate("dateperemption");
                p.setDatePeremption(datePeremption.toLocalDate());
                p.setStock(rs.getInt("stock"));
                Date dateProduction = rs.getDate("dateproduction");
                p.setDateProduction(dateProduction.toLocalDate());
                p.getCategorie().setIdCategorie(rs.getInt("idcategorie_id"));
//                p.setIdCategorie(rs.getInt("idcategorie_id"));
                produits.add(p);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "Error while searching products by name: ", e);
        }
        return produits;
    }

    @Override
    public float TotalProduit(Produit produit) {
        return produit.getPrix() * produit.getQuantite();
    }


    @Override
    public void addToPanier(Produit produit) {
        Produit existingProduit = findProduitInPanier(produit.getId());
        if (existingProduit != null) {
            existingProduit.incrementQuantity();

//            System.out.println("Quantité augmentée pour le produit : " + existingProduit.getNomp());
        } else {
            produit.setQuantite(1);
            panier.add(produit);
//            System.out.println("Produit ajouté au panier : " + produit.getNomp());
        }
    }

    private Produit findProduitInPanier(int productId) {
        for (Produit produit : panier) {
            if (produit.getId() == productId) {
                return produit;
            }
        }
        return null;
    }

    @Override
    public void incrementQuantity(int productId) {
        Produit product = findProduitInPanier(productId);
        if (product != null) {
            product.incrementQuantity();
        }
    }

    @Override
    public void decrementQuantity(int productId) {
        Produit product = findProduitInPanier(productId);
        if (product != null && product.getQuantite() > 1) {
            product.decrementQuantity();
        }
    }

    @Override
    public List<Produit> getPanierList() {
        return panier;
    }

    public ObservableList<Produit> getAllProduitsPanier() {
        return FXCollections.observableArrayList(panier);
    }


    @Override
    public void updateNbVentes(List<Produit> panier) {
        try {
            for (Produit produit : panier) {

                int nbVentesBefore = getNbVentesForProduit(produit.getId());

                String sql = "UPDATE produit SET nbventes = nbventes + ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, produit.getQuantite());
                preparedStatement.setInt(2, produit.getId());
                preparedStatement.executeUpdate();
                int nbVentesAfter = getNbVentesForProduit(produit.getId());

                System.out.println("Statistiques de vente avant mise à jour pour le produit " + produit.getNomp() + " : " + nbVentesBefore);
                System.out.println("Statistiques de vente après mise à jour pour le produit " + produit.getNomp() + " : " + nbVentesAfter);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "Erreur lors de la mise à jour des statistiques de vente après paiement : ", e);
        }
    }

    @Override
    public int getNbVentesForProduit(int productId) throws SQLException {
        int nbVentes = 0;
        String sql = "SELECT nbventes FROM produit WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, productId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            nbVentes = resultSet.getInt("nbventes");
        }

        return nbVentes;
    }

    @Override
    public List<Produit> getTopVenteProduits() {
        List<Produit> top3Produits = new ArrayList<>();
        try {
            String sql = "SELECT * FROM produit ORDER BY nbventes DESC LIMIT 3";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Produit p = new Produit();
                p.setId(rs.getInt("id"));
                p.setImage(rs.getString("image"));
                p.setNomp(rs.getString("nomp"));
                p.setDescription(rs.getString("description"));
                p.setPrix(rs.getFloat("prix"));
                p.setDatePeremption(rs.getDate("dateperemption").toLocalDate());
                p.setStock(rs.getInt("stock"));
                p.setDateProduction(rs.getDate("dateproduction").toLocalDate());
                p.setNbventes(rs.getInt("nbventes"));
                p.getCategorie().setIdCategorie(rs.getInt("idcategorie_id"));
                top3Produits.add(p);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "Error while getting top 3 sold products: ", e);
        }
        return top3Produits;
    }

    @Override
    public void removeFromPanier(int produitId) {
        Produit produitToRemove = findProduitInPanier(produitId);
        if (produitToRemove != null) {
            panier.remove(produitToRemove);
//            System.out.println("Produit supprimé du panier : " + produitToRemove.getNomp());
        } else {
            System.out.println("Produit non trouvé dans le panier");
        }
    }

    public boolean isProductExists(String productName) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) AS count FROM produit WHERE nomp = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, productName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                exists = count > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "Erreur lors de la vérification de l'existence du produit : ", e);
        }
        return exists;
    }
    //modifier stock aprés paiement de chaque produit dans le panier
    public void updateStockPanier(List<Produit> panier) {
        try {
            for (Produit produit : panier) {
                String sql = "UPDATE produit SET stock = stock - ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, produit.getQuantite());
                preparedStatement.setInt(2, produit.getId());
                preparedStatement.executeUpdate();

                System.out.println("Stock mis à jour pour le produit " + produit.getNomp());
            }
        } catch (SQLException e) {
            Logger.getLogger(ProduitService.class.getName()).log(Level.SEVERE, "Erreur lors de la mise à jour du stock après paiement : ", e);
        }
    }

    public void viderPanier() {
        panier.clear();
    }

}

