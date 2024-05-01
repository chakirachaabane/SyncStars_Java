package org.example.model;

public class Format {
    private int id;
    private String nom;

    public Format() {
    }

    public Format(String nom) {
        this.nom = nom;
    }

    public Format(int formatId, String s) {
        this.id=formatId;
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
