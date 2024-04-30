package tn.esprit.models;

public class recette {


    //Attributs recette
    int id;
    private String nom_recette;
    private String ingredients;
    private String etapes;
    private String probleme;

    //constructeurs recette

    public recette() {

    }

    public recette(int id, String nom_recette, String ingredients,String etapes, String probleme) {
        this.id=id;
        this.nom_recette = nom_recette;
        this.ingredients = ingredients;
        this.etapes = etapes;
        this.probleme = probleme;
    }

    public recette(String nom_recette, String ingredients,String etapes, String probleme) {
        this.nom_recette = nom_recette;
        this.ingredients = ingredients;
        this.etapes= etapes ;
        this.probleme = probleme;
    }

    //Getters wel setters mta3 recette


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_recette() {
        return nom_recette;
    }

    public void setNom_recette(String nom_recette) {
        this.nom_recette = nom_recette;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getEtapes() {
        return etapes;
    }

    public void setEtapes(String etapes) {
        this.etapes = etapes;
    }

    public String getProbleme() {
        return probleme;
    }

    public void setProbleme(String probleme) {
        this.probleme = probleme;
    }

    //Display recette


    @Override
    public String toString() {
        return "recette{" +
                "id=" + id +
                ", nom_recette='" + nom_recette + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", etapes='" + etapes + '\'' +
                ", Probleme='" + probleme + '\'' +
                '}';
    }

}
