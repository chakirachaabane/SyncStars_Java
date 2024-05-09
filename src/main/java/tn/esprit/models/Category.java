package tn.esprit.models;

public class Category {
    private int id;
    private String nom;

    public Category() {
    }

    public Category(String nom) {
        this.nom = nom;
    }

    public Category(int id) {
        this.id = id;
    }

    public Category(int categorieId, String s) {
        this.id=categorieId;
        this.nom=s;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
