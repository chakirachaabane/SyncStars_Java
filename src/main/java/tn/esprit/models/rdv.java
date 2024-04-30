package tn.esprit.models;

public class rdv {

    //Attributs rdv
    int id;
    private String date_rdv;
    private String horaire_rdv;
    private String probleme;

    //constructeurs rdv

    public rdv() {

    }

    public rdv(int id, String date_rdv, String horaire_rdv, String probleme) {
       this.id=id;
        this.date_rdv = date_rdv;
        this.horaire_rdv = horaire_rdv;
        this.probleme = probleme;
    }

    public rdv(String date_rdv, String horaire_rdv, String probleme) {
        this.date_rdv = date_rdv;
        this.horaire_rdv = horaire_rdv;
        this.probleme = probleme;
    }

    //Getters wel setters mta3 rdv


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getdate_rdv() {
        return date_rdv;
    }

    public void setdate_rdv(String date_rdv) {
        this.date_rdv = date_rdv;
    }

    public String gethoraire_rdv() {
        return horaire_rdv;
    }

    public void sethoraire_rdv(String horaire_rdv) {
        this.horaire_rdv = horaire_rdv;
    }

    public String getProbleme() {
        return probleme;
    }

    public void setProbleme(String probleme) {
        this.probleme = probleme;
    }

    //Display rdv


    @Override
    public String toString() {
        return "rdv{" +
                "id=" + id +
                ", date_rdv='" + date_rdv + '\'' +
                ", horaire_rdv='" + horaire_rdv + '\'' +
                ", Probleme='" + probleme + '\'' +
                '}';
    }
}
