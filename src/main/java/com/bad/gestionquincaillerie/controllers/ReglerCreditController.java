/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Badra Aliou Dembele
 */

import com.bad.gestionquincaillerie.dao.CreditVenteDAO;
import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import com.bad.gestionquincaillerie.models.CreditVente;
import com.bad.gestionquincaillerie.models.Employe;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReglerCreditController {

    @FXML private Label lblTitre;
    @FXML private Label lblInfoCredit;
    @FXML private TextField txtMontantVerse;
    @FXML private Button btnConfirmer;
    @FXML private Label lblMessage;

    private final CreditVenteDAO creditVenteDAO = new CreditVenteDAO();
    private CreditVente credit;
    private Runnable surSucces; // callback pour rafraîchir la table parente
    private Employe employeConnecte;

    public void initData(CreditVente credit, Runnable surSucces) {
        this.credit = credit;
        this.surSucces = surSucces;

        double reste = credit.getMontantCredit() - credit.getMontantPaye();
        lblTitre.setText("Régler le crédit " + credit.getNumCredit());
        lblInfoCredit.setText("Client : " + credit.getNomClient() + " | Reste à payer : " + reste + " FCFA");
    }

    @FXML
    private void confirmerReglement() {
        String texte = txtMontantVerse.getText().trim();

        if (texte.isEmpty()) {
            afficherMessage("Veuillez saisir un montant.", false);
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(texte);
        } catch (NumberFormatException e) {
            afficherMessage("Le montant doit être un nombre valide.", false);
            return;
        }

        if (montant <= 0) {
            afficherMessage("Le montant doit être supérieur à 0.", false);
            return;
        }

        boolean succes = creditVenteDAO.reglerCredit(credit.getIdCredit(), montant);

        if (succes) {
            afficherMessage("Règlement enregistré avec succès.", true);
            new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Règlement de credit : " + credit.getIdCredit() + "Montant paye : " + FormatUtilitaire.formaterMontant(montant));

            if (surSucces != null) surSucces.run();

            // Ferme la fenêtre après un court instant pour laisser voir le message
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
            pause.setOnFinished(e -> ((Stage) lblTitre.getScene().getWindow()).close());
            pause.play();
        } else {
            afficherMessage("Erreur lors de l'enregistrement du règlement.", false);
        }
    }

    private void afficherMessage(String texte, boolean succes) {
        lblMessage.setText(texte);
        lblMessage.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
