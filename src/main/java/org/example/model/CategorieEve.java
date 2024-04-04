package org.example.model;

import javax.persistence.OneToMany;
import java.util.List;

public class CategorieEve {

    private int id;
    private String nom;

    @OneToMany(mappedBy = "categorieEve")
    private List<Evenement> evenements;

    public CategorieEve(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.evenements = evenements;
    }

    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }

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
}
