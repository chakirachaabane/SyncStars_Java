package org.example.model;


import javafx.scene.image.Image;

import java.sql.Date;
import java.time.LocalDate;

public class Evenement {

    private int id;

    private String titre;

    private Format format_id;

    private Category categorie_id;

    private String adresse;

    private String image;

    private java.sql.Date date;

    private int nbPlaces;

    private String description;


    public Evenement(int id, String titre, String adresse ,java.sql.Date date, Category categorie_id , Format format_id, String image,String description, int nbPlaces) {
        this.id = id;
        this.titre = titre;
        this.adresse = adresse;
        this.date = date;
        this.categorie_id = categorie_id;
        this.format_id = format_id;
        this.image = image;
        this.nbPlaces=nbPlaces;
        this.description=description;
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
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
