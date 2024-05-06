package tn.esprit.models;

public class Categorie {
    private int idCategorie;
    private String nomc;
    private String description;
    private int disponibilite;

    public Categorie() {
    }

    public Categorie(String nomc, String description, int disponibilite) {
        this.nomc = nomc;
        this.description = description;
        this.disponibilite = disponibilite;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNomc() {
        return nomc;
    }

    public void setNomc(String nomc) {
        this.nomc = nomc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(int disponibilite) {
        this.disponibilite = disponibilite;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "idCategorie=" + idCategorie +
                ", nomc='" + nomc + '\'' +
                ", description='" + description + '\'' +
                ", disponibilite=" + disponibilite +
                '}';
    }

}
