package tn.esprit.models;

import java.time.LocalDate;

public class Produit {
    private int id;
    private String image;
    private String nomp;
    private String description;
    private float prix;
    private int stock;
    private LocalDate dateProduction;
    private LocalDate datePeremption;
    private int idCategorie;
    private int likes;
    private int dislikes;
    private int nbventes;

    public Produit() {

    }

    public Produit(String image, String nomp, String description, float prix, int stock, LocalDate dateProduction, LocalDate datePeremption,int idCategorie) {
        this.image = image;
        this.nomp = nomp;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.dateProduction = dateProduction;
        this.datePeremption = datePeremption;
        this.idCategorie = idCategorie;
//        this.likes = likes;
//        this.dislikes = dislikes;
//        this.nbventes = nbventes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNomp() {
        return nomp;
    }

    public void setNomp(String nomp) {
        this.nomp = nomp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDate getDateProduction() {
        return dateProduction;
    }

    public void setDateProduction(LocalDate dateProduction) {
        this.dateProduction = dateProduction;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getNbventes() {
        return nbventes;
    }

    public void setNbventes(int nbventes) {
        this.nbventes = nbventes;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", nomp='" + nomp + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                ", dateProduction=" + dateProduction +
                ", datePeremption=" + datePeremption +
                ", idCategorie=" + idCategorie +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", nbventes=" + nbventes +
                '}';
    }
}
