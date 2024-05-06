package tn.esprit.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class paiementController implements Initializable {

    @FXML
    private TextField cardNumberTextField;

    @FXML
    private TextField cvcTextField;

    @FXML
    private TextField expirymTextField;

    @FXML
    private TextField expiryyTextField;

    @FXML
    private TextField payment_email;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Button payButton;
    ProduitService ps = new ProduitService();

    public void initData() {
        float totalPanier=0;
        ObservableList<Produit> produits = ps.getAllProduitsPanier();
        for (Produit produit : produits) {
            float totalProduit = produit.getPrix() * produit.getQuantite();
            produit.setTotalProduits(totalProduit);
            totalPanier+=totalProduit;
        }
        totalAmountLabel.setText(String.valueOf(totalPanier) + " DT");


    }

    @FXML
    void payer(ActionEvent event) {
        if (cardNumberTextField.getText().isEmpty() || payment_email.getText().isEmpty() || expirymTextField.getText().isEmpty() || expiryyTextField.getText().isEmpty() || cvcTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Payment Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the required fields.");
            alert.showAndWait();
        } else {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Payment");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to make this payment?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                List<Produit> panier = ps.getPanierList();
                String token = "tok_visa";
                long amount = (long) (Float.parseFloat(totalAmountLabel.getText().split(" ")[0]) * 100);
                String currency = "usd";

                Map<String, Object> chargeParams = new HashMap<>();
                chargeParams.put("amount", amount);
                chargeParams.put("currency", currency);
                chargeParams.put("source", token);

                try {
                    Charge.create(chargeParams);
                    processPayment(amount);
                    ProduitService ps = new ProduitService();
                    ps.updateStockPanier(panier);
                    ps.updateNbVentes(panier);
                    ps.viderPanier();
                    Notifications.create()
                            .title("Succès")
                            .text("Paiement fait avec succès !")
                            .hideAfter(Duration.seconds(2.5))
                            .position(Pos.BOTTOM_RIGHT)
                            .graphic(new ImageView(new Image("/images/tik1.png")))
                            .show();
                    loadPage("/historique.fxml", event);
                } catch (StripeException ex) {
                    Logger.getLogger(paiementController.class.getName()).log(Level.SEVERE, null, ex);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Payment Error");
                    alert.setHeaderText(null);
                    alert.setContentText("There was an error processing your payment. Please check your payment information and try again.");
                    alert.showAndWait();
                }
            }
        }

    }





    private void processPayment(long amount) {
        try {
            Stripe.apiKey = "sk_test_51OnjfSEEajwPxVozTTwXnT7IFHrg6S5IwrKh8Onz13nSEG9e59fsqCrgdIv4Ppt5US7terWwT16JFFk6rByc06p900eywpMfXa";
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());

        } catch (StripeException e) {
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stripe.apiKey = "sk_test_51OnjfSEEajwPxVozTTwXnT7IFHrg6S5IwrKh8Onz13nSEG9e59fsqCrgdIv4Ppt5US7terWwT16JFFk6rByc06p900eywpMfXa";

        TextFormatter<String> emailFormatter = new TextFormatter<>(change
                -> change.getControlNewText().length() <= 32 ? change : null);
        payment_email.setTextFormatter(emailFormatter);

        TextFormatter<String> cardNumberFormatter = new TextFormatter<>(change
                -> change.getControlNewText().matches("\\d{0,16}") ? change : null);
        cardNumberTextField.setTextFormatter(cardNumberFormatter);

        TextFormatter<String> expiryMFormatter = new TextFormatter<>(change
                -> change.getControlNewText().matches("\\d{0,2}") ? change : null);
        expirymTextField.setTextFormatter(expiryMFormatter);

        TextFormatter<String> expiryYFormatter = new TextFormatter<>(change
                -> change.getControlNewText().matches("\\d{0,2}") ? change : null);
        expiryyTextField.setTextFormatter(expiryYFormatter);

        TextFormatter<String> cvcFormatter = new TextFormatter<>(change
                -> change.getControlNewText().matches("\\d{0,3}") ? change : null);
        cvcTextField.setTextFormatter(cvcFormatter);

        initData();
    }
    private  void loadPage(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
