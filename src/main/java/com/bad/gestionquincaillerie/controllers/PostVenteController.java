/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

/**
 *
 * @author Badra Aliou Dembele
 */

import com.bad.gestionquincaillerie.dao.ReçusVenteDAO;
import com.bad.gestionquincaillerie.dao.VenteDAO;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import com.bad.gestionquincaillerie.models.LigneVente;
import com.bad.gestionquincaillerie.models.ReçusVente;
import com.bad.gestionquincaillerie.models.Vente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PostVenteController {

    @FXML private Label lblTitre;
    @FXML private Label lblStatutCreance;
    @FXML private TableView<LigneVente> tableLignesVente;
    @FXML private TableColumn<LigneVente, String> colProduit;
    @FXML private TableColumn<LigneVente, Integer> colQuantite;
    @FXML private TableColumn<LigneVente, Double> colPrixUnitaire;
    @FXML private TableColumn<LigneVente, Double> colTotal;
    @FXML private TextField txtAdresseLivraison;
    @FXML private TextField txtVehiculeLivraison;
    @FXML private Label lblMessage;

    private final VenteDAO venteDAO = new VenteDAO();
    private final ReçusVenteDAO recuVenteDAO = new ReçusVenteDAO();
    private Vente vente;
    private List<LigneVente> lignesVente;

    public void initData(Vente vente, List<LigneVente> lignes) {
        this.vente = vente;
        this.lignesVente = lignes;

        lblTitre.setText("Vente " + vente.getNumVente() + " enregistrée avec succès");

        double montantRestant = vente.getMontantRestant();
        if (montantRestant >= 0) {
            lblStatutCreance.setText("Aucune créance — montant restant : " + FormatUtilitaire.formaterMontant(montantRestant) + " FCFA");
            lblStatutCreance.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 14px;");
        } else {
            lblStatutCreance.setText("Créance envers le client : " + FormatUtilitaire.formaterMontant(Math.abs(montantRestant))+ " FCFA");
            lblStatutCreance.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 14px;");
        }

        colProduit.setCellValueFactory(new PropertyValueFactory<>("designationProduit"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrix"));
        tableLignesVente.setItems(FXCollections.observableArrayList(lignes));
    }

    @FXML
    private void finaliserVente() {
        String adresse = txtAdresseLivraison.getText().trim();
        String vehicule = txtVehiculeLivraison.getText().trim();

        // Enregistre la livraison seulement si les deux champs sont remplis (facultatif)
        if (!adresse.isEmpty() && !vehicule.isEmpty()) {
            venteDAO.mettreAJourLivraison(vente.getIdVente(), adresse, vehicule);
        }

        // Génère (ou récupère) le reçu et l'affiche
        ReçusVente recu = recuVenteDAO.getByVente(vente.getIdVente());
        if (recu == null) {
            recu = recuVenteDAO.ajouter(vente.getIdVente(), vente.getMontantTotal());
        }

        if (recu == null) {
            lblMessage.setText("Erreur lors de la génération du reçu.");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/bad/gestionquincaillerie/views/ReçusVente.fxml")
            );
            Parent root = loader.load();

            ReçusVenteController controller = loader.getController();
            controller.initData(recu, vente, lignesVente);

            Stage stageRecu = new Stage();
            stageRecu.setTitle("Reçus: " + recu.getNumRecus());
            stageRecu.setScene(new Scene(root, 500, 650));
            stageRecu.show();

            // Ferme le panneau post-vente une fois le reçu affiché
            ((Stage) lblTitre.getScene().getWindow()).close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}