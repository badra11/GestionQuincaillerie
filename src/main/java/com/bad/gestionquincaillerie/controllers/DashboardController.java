/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

import com.bad.gestionquincaillerie.dao.CreditVenteDAO;
import com.bad.gestionquincaillerie.dao.ProduitDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import com.bad.gestionquincaillerie.models.Employe;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Badra Aliou Dembele
 */
public class DashboardController {

    @FXML
    private Button btnAccueil;
    @FXML
    private Button btnProduits;
    @FXML
    private Button btnEmployes;
    @FXML
    private Button btnVentes;
    @FXML
    private Button btnClients;
    @FXML
    private Button btnFournisseurs;
    @FXML
    private Button btnStatistiques;
    @FXML
    private Button btnCommandes;
    @FXML
    private Button btnCreditVente;
    @FXML
    private Button btnStructureProduit;
    @FXML
    private Button btnParametre;
    @FXML
    private Button btnJournal;

    private List<Button> boutonsSidebar;

    @FXML
    private VBox contenuCentral;

    @FXML
    private Label lblBienvenue;
    @FXML
    private Label lblInitiales;
    @FXML
    private Label lblUsername;

    @FXML
    private Label lblRole;

    private Employe employeConnecte;

    /**
     * Appelée juste après le chargement du Dashboard pour transmettre l'employé
     * actuellement connecté (voir modification du LoginController).
     */
    public void initData(Employe employe) {
        this.employeConnecte = employe;
        lblBienvenue.setText("Bienvenue, " + employe.getPrenom() + " " + employe.getNom().toUpperCase());
        lblRole.setText(employe.getRole().toUpperCase());

        // ===== Avatar avec initiales =====
        String initiale1 = employe.getPrenom() != null && !employe.getPrenom().isEmpty()
                ? String.valueOf(employe.getPrenom().charAt(0)).toUpperCase() : "";
        String initiale2 = employe.getNom() != null && !employe.getNom().isEmpty()
                ? String.valueOf(employe.getNom().charAt(0)).toUpperCase() : "";
        lblInitiales.setText(initiale1 + initiale2);

        // ===== Nom d'utilisateur =====
        lblUsername.setText(employe.getUsername() != null ? "@" + employe.getUsername() : "");

        appliquerRestrictionsRole();
        initialiserListeBoutons();
        showAccueil();
        verifierAlertes();

    }

    private void initialiserListeBoutons() {
        boutonsSidebar = java.util.List.of(
                btnAccueil, btnProduits, btnEmployes, btnVentes, btnClients,
                btnFournisseurs, btnStatistiques, btnCommandes, btnCreditVente, btnStructureProduit, btnParametre, btnJournal
        );
    }

    private void setBoutonActif(Button bouton) {
        for (Button b : boutonsSidebar) {
            b.getStyleClass().remove("sidebar-btn-active");
        }
        bouton.getStyleClass().add("sidebar-btn-active");
    }

    private void appliquerRestrictionsRole() {
        if ("user".equalsIgnoreCase(employeConnecte.getRole())) {
            masquerBouton(btnEmployes);
            masquerBouton(btnProduits);
            masquerBouton(btnCommandes);
            masquerBouton(btnStructureProduit);
            masquerBouton(btnParametre);
            masquerBouton(btnJournal);
        }
    }

    private void masquerBouton(Button bouton) {
        bouton.setVisible(false);
        bouton.setManaged(false);
    }

    private void verifierAlertes() {
        ProduitDAO produitDAO = new ProduitDAO();
        CreditVenteDAO creditDAO = new CreditVenteDAO();

        int nbAlertesStock = produitDAO.getNombreProduitsEnAlerte();
        double creanceGlobale = creditDAO.getMontantCreanceGlobale();

        if (nbAlertesStock > 0 || creanceGlobale > 0) {
            StringBuilder message = new StringBuilder();
            if (nbAlertesStock > 0) {
                message.append("⚠ ").append(nbAlertesStock).append(" produit(s) en alerte de stock.\n");
            }
            if (creanceGlobale > 0) {
                message.append("💰 Créances en cours : ").append(FormatUtilitaire.formaterMontant(creanceGlobale)).append(" FCFA.");
            }

            Alert alerte = new Alert(Alert.AlertType.WARNING);
            alerte.setTitle("Alertes du jour");
            alerte.setHeaderText("Points d'attention");
            alerte.setContentText(message.toString());
            alerte.showAndWait();
        }
    }

    @FXML
    private void showAccueil() {
        setBoutonActif(btnAccueil);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Acceuil.fxml")
            );
            Parent vue = loader.load();

            AcceuilController controller = loader.getController();
            controller.initEmploye(employeConnecte);

            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showProduits() {
        setBoutonActif(btnProduits);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Produit.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showEmployes() {
        setBoutonActif(btnEmployes);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Employe.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showVentes() {
        setBoutonActif(btnVentes);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Vente.fxml")
            );
            Parent vue = loader.load();

            VenteController controller = loader.getController();
            controller.initEmploye(employeConnecte);

            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showClients() {
        setBoutonActif(btnClients);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Client.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showFournisseurs() {
        setBoutonActif(btnFournisseurs);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Fournisseur.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showStatistiques() {
        setBoutonActif(btnStatistiques);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Statistiques.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeconnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Login.fxml")
            );
            Parent loginRoot = loader.load();

            Stage stage = (Stage) contenuCentral.getScene().getWindow();
            Scene loginScene = new Scene(loginRoot, 500, 600);
            stage.setScene(loginScene);
            stage.setTitle("Gestion Quincaillerie - Connexion");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showCommandes() {
        setBoutonActif(btnCommandes);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Commande.fxml")
            );
            Parent vue = loader.load();

            CommandeController controller = loader.getController();
            controller.initEmploye(employeConnecte);

            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showCreditVente() {
        setBoutonActif(btnCreditVente);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/CreditVente.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showStructureProduit() {
        setBoutonActif(btnStructureProduit);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/StructureProduit.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showParametre() {
        setBoutonActif(btnParametre);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Parametre.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showJournal() {
        setBoutonActif(btnJournal);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/JournalActivite.fxml")
            );
            Parent vue = loader.load();
            contenuCentral.getChildren().clear();
            contenuCentral.getChildren().add(vue);
            VBox.setVgrow(vue, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche temporairement un texte dans la zone centrale (à remplacer plus
     * tard par le chargement d'un vrai FXML par section)
     */
    private void afficherSection(String nomSection) {
        contenuCentral.getChildren().clear();
        Label label = new Label("Section : " + nomSection);
        label.getStyleClass().add("contenu-placeholder");
        contenuCentral.getChildren().add(label);
    }
}
