package org.example.model;
import javax.persistence.*;
import java.util.List;

public class FormatEve {

    private int id;
    private String nom;

    @OneToMany(mappedBy = "formatEve")
    private List<Evenement> evenements;

    public FormatEve(int id, String nom) {
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
