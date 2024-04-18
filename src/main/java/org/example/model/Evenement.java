package org.example.model;


public class Evenement {

    private int id;

    private String titre;

    private String format_id;

    private String categorie_id;

    private String adresse;

    private String image;

    private java.sql.Date date;


    public Evenement(int id, String titre, String adresse ,java.sql.Date date, String categorie_id , String format_id, String image) {
        this.id = id;
        this.titre = titre;
        this.adresse = adresse;
        this.date = date;
        this.categorie_id = categorie_id;
        this.format_id = format_id;
        this.image = image;
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

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public String getFormat() {
        return format_id;
    }

    public String getCategorie() {
        return categorie_id;
    }

    public void setFormat(String format) {
        this.format_id = format;
    }

    public void setCategorie(String categorie) {
        this.categorie_id = categorie;
    }
}
