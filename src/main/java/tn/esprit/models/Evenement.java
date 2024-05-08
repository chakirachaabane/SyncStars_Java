package tn.esprit.models;


import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class Evenement {

    private int id;

    private String titre;

    private Format format_id;

    private Category categorie_id;

    private String adresse;

    private String image;

    private java.sql.Date date;

    private Time heure;

    private int nb_places;

    private String description;


    public Evenement(int id, Category categorie_id, Format format_id, String titre, Date date,Time heure, String description, String image, String adresse, int nb_places) {
        this.id = id;
        this.categorie_id = categorie_id;
        this.format_id = format_id;
        this.titre = titre;
        this.date = date;
        this.heure=heure;
        this.description = description;
        this.image = image;
        this.adresse = adresse;
        this.nb_places = nb_places;
    }


    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", format='" + format_id + '\'' +
                ", categorie='" + categorie_id + '\'' +
                ", adresse='" + adresse + '\'' +
                ", image='" + image + '\'' +
                ", date=" + date +
                ", heure=" + heure +
                ", places=" + nb_places +
                '}';
    }

    public Evenement() {
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(LocalDate localDate) {
        this.date = java.sql.Date.valueOf(localDate);
    }

    public Time getHeure() {
        return heure;
    }

    public void setHeure(Time heure) {
        this.heure = heure;
    }

    public Format getFormat() {
        return format_id;
    }

    public Category getCategorie() {
        return categorie_id;
    }

    public void setFormat(Format format) {
        this.format_id = format;
    }

    public void setCategorie(Category categorie) {
        this.categorie_id = categorie;
    }

    public int getNbPlaces() {
        return nb_places;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nb_places = nbPlaces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
