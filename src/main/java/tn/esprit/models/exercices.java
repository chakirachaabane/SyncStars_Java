package tn.esprit.models;

import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

public class exercices {


    private int id;
    private String nom_exercice;

    private String type;
    private int duree;
    private int nombres_de_fois;

    public exercices() {
    }
    @ManyToMany(mappedBy = "exercices")
    private Set<entrainements> entrainements = new HashSet<>();

    // Standard getters and setters
    public Set<entrainements> getEntrainements() {
        return entrainements;
    }

    public void setEntrainements(Set<entrainements> entrainements) {
        this.entrainements = entrainements;
    }

    public exercices(int id, String nom_exercice, String type, int duree, int nombres_de_fois) {
        this.id = id;
        this.nom_exercice = nom_exercice;
        this.type = type;
        this.duree = duree;
        this.nombres_de_fois = nombres_de_fois;
    }

    public exercices(int id, String nom_exercice, String type, int duree, int nombres_de_fois, Set<tn.esprit.models.entrainements> entrainements) {
        this.id = id;
        this.nom_exercice = nom_exercice;
        this.type = type;
        this.duree = duree;
        this.nombres_de_fois = nombres_de_fois;
        this.entrainements = entrainements;
    }

    public exercices(String nom_exercice, String type, int duree, int nombres_de_fois) {

        this.nom_exercice = nom_exercice;
        this.type = type;
        this.duree = duree;
        this.nombres_de_fois = nombres_de_fois;
    }

    public int getId() {
        return id;
    }

    public String getNom_exercice() {
        return nom_exercice;
    }

    public String getType() {
        return type;
    }

    public int getDuree() {
        return duree;
    }

    public int getNombres_de_fois() {
        return nombres_de_fois;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setNom_exercice(String nom_exercice) {
        this.nom_exercice = nom_exercice;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setNombres_de_fois(int nombres_de_fois) {
        this.nombres_de_fois = nombres_de_fois;
    }

    @Override
    public String toString() {
        return "exercices{" +
                "id=" + id +
                ", nom_exercice='" + nom_exercice + '\'' +
                ", type='" + type + '\'' +
                ", duree=" + duree +
                ", nombres_de_fois=" + nombres_de_fois +
                '}';
    }
}
