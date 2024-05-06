package tn.esprit.test;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import tn.esprit.models.Categorie;
import tn.esprit.models.Produit;
import tn.esprit.services.CategorieService;
import tn.esprit.services.ProduitService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

public class Main {
    public static void main(String[] args) {
//       ProduitService ps = new ProduitService();
//        Produit pr1 = new Produit("eee","pr1","eeeeeeeeeeee",120,100, LocalDate.of(2023, Month.FEBRUARY, 15),LocalDate.of(2025, Month.APRIL, 20,));
//        CategorieService cs =new CategorieService();
//        Categorie c1=new Categorie("Anti-Stress","eee",130);
//        cs.add(c1);
             //tester Stripe fonctionnel ou non
        Stripe.apiKey = "sk_test_51OnhcHH2lBROA2EuaFGzNx5OeRSsg1kRQhIFi6W7Xev6amtJA64nxyVcEkHjGKUkeWYvKEl1qUkp9fMHiy7JJW9M00fiyqbsIy";

        try {

            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}