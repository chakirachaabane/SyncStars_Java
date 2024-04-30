package tn.esprit;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import tn.esprit.models.rdv;
import tn.esprit.models.recette;
import tn.esprit.services.RdvService;
import tn.esprit.services.RecetteService;

public class Main {
    public static void main(String[] args) {

        // ----------------------------------------PARTIE RDV------------------------------------//

      RdvService RS = new RdvService();

        //-------------ADD RDV-------//
//        LocalDate currentDate = LocalDate.now();
//
//        rdv R1 = new rdv("11/04/2024", "09:00", "Diabète");
//
//
//        if (R1.getdate_rdv().isEmpty()) {
//            System.out.println("ATTENTION FEL AJOUT : La date du rendez-vous est obligatoire !");
//            return;
//        } else if (!validerDate(R1.getdate_rdv(), currentDate)) {
//            return;
//        }
//
//
//        if (R1.gethoraire_rdv().isEmpty()) {
//            System.out.println("ATTENTION FEL AJOUT: L'horaire du rendez-vous est obligatoire !");
//            return;
//        } else if (!validerHoraire(R1.gethoraire_rdv())) {
//            return;
//        }
//
//        String probleme = R1.getProbleme();
//
//        if (probleme.isEmpty()) {
//            System.out.println("ATTENTION FEL AJOUT: Le problème du rendez-vous est obligatoire !");
//            return;
//        } else if (!validerProbleme(probleme)) {
//            System.out.println("ATTENTION FEL AJOUT : Le champ problème doit être l'une des options suivantes : Obésité, Diabète, Anorexie, Déséquilibre alimentaire, Cholestérol");
//            return;
//        }
//
//        RS.addRdv(R1);
//        System.out.println("Rendez-vous ajouté avec succès !");

//-------------UPDATE RDV-------//
//
//        rdv rdvToUpdate = new rdv(1, "12/04/2024", "10:00", "Diabète");
//
//        if (rdvToUpdate.getdate_rdv().isEmpty()) {
//            System.out.println("PAS D'UPDATE : La date du rendez-vous est obligatoire !");
//            return;
//        } else if (!validerDate(rdvToUpdate.getdate_rdv(), LocalDate.now())) {
//            return;
//        }
//
//        if (rdvToUpdate.gethoraire_rdv().isEmpty()) {
//            System.out.println("PAS D'UPDATE : L'horaire du rendez-vous est obligatoire !");
//            return;
//        } else if (!validerHoraire(rdvToUpdate.gethoraire_rdv())) {
//            return;
//        }
//
//        if (rdvToUpdate.getProbleme().isEmpty()) {
//            System.out.println("PAS D'UPDATE : Le problème du rendez-vous est obligatoire !");
//            return;
//        } else if (!validerProbleme(rdvToUpdate.getProbleme())) {
//            System.out.println("PAS D'UPDATE : Le problème du rendez-vous doit être l'une des options suivantes : Obésité, Diabète, Anorexie, Déséquilibre alimentaire, Cholestérol");
//            return;
//        }
//
//        RS.updateRdv(rdvToUpdate);
//        System.out.println("UPDATE RDV effectuée avec succès !");
        //-------------DELETE RDV-------//

//        int rdvIdToDelete = 2;
//        RS.deleteRdv(rdvIdToDelete);

        //-------------LIST RDV-------//

//        List<rdv> tousLesRdvs = RS.getAll();
//        System.out.println("Liste des rendez-vous :");
//        for (rdv r : tousLesRdvs) {
//            System.out.println(r);
//        }

        //-------------GETONE RDV-------//

//        int rdvId = 55; // Remplacez 1 par l'ID du rendez-vous que vous souhaitez récupérer
//
//        // Appel de la méthode getOne avec l'ID spécifié
//        rdv rdvFound = RS.getOne(rdvId);
//
//        // Vérification si le rendez-vous a été trouvé
//        if (rdvFound != null) {
//            System.out.println("Rendez-vous trouvé avec l'ID : " + rdvId);
//            System.out.println(rdvFound); // Affichage du rendez-vous trouvé
//        } else {
//            System.out.println("Aucun rendez-vous trouvé avec l'ID : " + rdvId);
//        }

        //-------------GetByProblem RDV-------//

//        List<rdv> rdvsByProbleme = RS.getByProbleme("Diabète");
//
//        // Affichage des rendez-vous ayant le problème "Diabète"
//        System.out.println("Rendez-vous ayant le problème 'Diabète':");
//        for (rdv r : rdvsByProbleme) {
//            System.out.println(r);
//        }

        // ----------------------------------------PARTIE RECETTE------------------------------------//
        RecetteService recetteService = new RecetteService();


        //-------------ADD RECETTE-------//

        recette recette1 = new recette("Recette", "Ingredient 1", "Etape 1", "Obésité");
        recetteService.addRecette(recette1);

        //-------------LIST RECETTE-------//
//        try {
//            List<recette> recettes = recetteService.getAllRecettes();
//            for (recette recette : recettes) {
//                System.out.println(recette);
//            }
//        } catch (SQLException e) {
//            System.err.println("Erreur lors de la récupération des recettes: " + e.getMessage());
//        }

        //-------------UPDATE RECETTE-------//

        int idRecetteToUpdate = 7;
        recette recetteToUpdate = recetteService.getOneRecette(idRecetteToUpdate);
        if (recetteToUpdate != null) {

            recetteToUpdate.setNom_recette("PAINfarcie");
            recetteToUpdate.setIngredients("ingrédient1");
            recetteToUpdate.setEtapes("etapeàmanger");
            recetteToUpdate.setProbleme("Diabète");

            recetteService.updateRecette(recetteToUpdate);
            System.out.println("Recette mise à jour avec succès !");
        } else {
            System.out.println("Recette non trouvée avec l'ID : " + idRecetteToUpdate);
        }
        //-------------DELETE RECETTE-------//
        int idRecetteASupprimer = 10; // Remplacez ceci par l'ID de la recette que vous souhaitez supprimer
        recette recetteASupprimer = recetteService.getOneRecette(idRecetteASupprimer);
        if (recetteASupprimer != null) {
            recetteService.deleteRecette(recetteASupprimer);
            System.out.println("Recette supprimée avec succès !");
        } else {
            System.out.println("Aucune recette trouvée avec l'ID : " + idRecetteASupprimer);
        }
        //-------------GetONE RECETTE-------//
        // Récupérer une recette à partir de son ID
        int idRecette = 9; // ID de la recette à récupérer
        recette recetteFound = recetteService.getOneRecette(idRecette);
        if (recetteFound != null) {
            // Afficher les détails de la recette
            System.out.println("Recette trouvée : " + recetteFound);
        } else {
            System.out.println("Aucune recette trouvée avec l'ID : " + idRecette);
        }

        //-------------GetByProblem RECETTE-------//
        String probleme = "Diabète"; // Remplacez ceci par le problème recherché
        List<recette> recettesPourProbleme = recetteService.getByProbleme(probleme);

        if (!recettesPourProbleme.isEmpty()) {
            System.out.println("Recettes pour le problème '" + probleme + "' :");
            for (recette recette : recettesPourProbleme) {
                System.out.println(recette);
            }
        } else {
            System.out.println("Aucune recette trouvée pour le problème '" + probleme + "'");
        }


    }

    // ----------------------------------------PARTIE FONCTIONS RDV------------------------------------//


    private static boolean validerDate(String dateSaisie, LocalDate currentDate) {
        try {
            LocalDate rdvDate = LocalDate.parse(dateSaisie, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (rdvDate.isBefore(currentDate)) {
                System.out.println("ATTENTION : Vous ne pouvez pas saisir une date antérieure à la date actuelle !");
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("ATTENTION : Format de date invalide !");
            return false;
        }
    }

    // Méthode pour valider l'horaire du RDV
    private static boolean validerHoraire(String horaireSaisi)
    {
        try {
            LocalTime horaire = LocalTime.parse(horaireSaisi);
            boolean isHoraireValide = (horaire.isAfter(LocalTime.of(7, 59)) && horaire.isBefore(LocalTime.of(11, 0))) ||
                    (horaire.isAfter(LocalTime.of(14, 29)) && horaire.isBefore(LocalTime.of(17, 1)));
            if (!isHoraireValide) {
                System.out.println("ATTENTION : L'horaire doit être entre 08:00 et 11:00 ou entre 14:30 et 17:00 !");
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("ATTENTION : Format d'horaire invalide !");
            return false;
        }
    }

    private static boolean validerProbleme(String probleme) {
        List<String> options = Arrays.asList("Obésité", "Diabète", "Anorexie", "Déséquilibre alimentaire", "Cholestérol");
        return options.contains(probleme);
    }


    // ----------------------------------------PARTIE FONCTIONS RECETTE------------------------------------//

}
