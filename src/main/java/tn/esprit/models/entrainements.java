package tn.esprit.models;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.List;
@Table(name = "entrainements")
public class entrainements {

    private int id ;
    private String niveau;
    private int duree;
    private String objectif;
    private String nom_entrainement;
    private int periode;

   @ManyToMany
    @JoinTable(
            name = "entrainement_exercices",
            joinColumns = @JoinColumn(name = "entrainement_id"),
            inverseJoinColumns = @JoinColumn(name = "exercice_id")
    )
    private List<exercices> exercices;
    public entrainements(int id, String niveau, int duree, String objectif, String nom_entrainement, int periode) {
        this.id = id;
        this.niveau = niveau;
        this.duree = duree;
        this.objectif = objectif;
        this.nom_entrainement = nom_entrainement;
        this.periode = periode;
    }

    public entrainements() {
    }

    public int getId() {
        return id;
    }

    public String getNiveau() {
        return niveau;
    }

    public int getDuree() {
        return duree;
    }

    public String getObjectif() {
        return objectif;
    }

    public String getNom_entrainement() {
        return nom_entrainement;
    }

    public int getPeriode() {
        return periode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public void setNom_entrainement(String nom_entrainement) {
        this.nom_entrainement = nom_entrainement;
    }

    public void setPeriode(int periode) {
        this.periode = periode;
    }

    @Override
    public String toString() {
        return "Entrainement{" +
                "id=" + id +
                ", niveau='" + niveau + '\'' +
                ", duree=" + duree +
                ", objectif='" + objectif + '\'' +
                ", nom_entrainement='" + nom_entrainement + '\'' +
                ", periode=" + periode +
                '}';
    }

    public List<tn.esprit.models.exercices> getExercices() {  return exercices;
    }
}


