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

import com.bad.gestionquincaillerie.dao.FournisseurDAO;
import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import com.bad.gestionquincaillerie.models.Employe;
import com.bad.gestionquincaillerie.models.Fournisseur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class FournisseurController implements Initializable {

    @FXML private TextField txtNomSociete;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtAdresse;
    @FXML private Label lblMessage;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;
    @FXML private Button btnSupprimer;

    @FXML private TableView<Fournisseur> tableFournisseurs;
    @FXML private TableColumn<Fournisseur, Integer> colId;
    @FXML private TableColumn<Fournisseur, String> colNomSociete;
    @FXML private TableColumn<Fournisseur, String> colEmail;
    @FXML private TableColumn<Fournisseur, String> colTelephone;
    @FXML private TableColumn<Fournisseur, String> colAdresse;

    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private Employe employeConnecte;
    private Fournisseur fournisseurEnEdition = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFournisseur"));
        colNomSociete.setCellValueFactory(new PropertyValueFactory<>("nomSociete"));
        colNomSociete.setCellFactory(col -> new TableCell<Fournisseur, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colAdresse.setCellFactory(col -> new TableCell<Fournisseur, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        
        chargerFournisseurs();

        tableFournisseurs.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                fournisseurEnEdition = selection;
                txtNomSociete.setText(selection.getNomSociete());
                txtEmail.setText(selection.getEmail());
                txtTelephone.setText(selection.getTelephone());
                txtAdresse.setText(selection.getAdresse());

                btnAjouter.setText("Modifier le fournisseur");
                btnAnnuler.setVisible(true);
                btnAnnuler.setManaged(true);
                btnSupprimer.setVisible(true);
                btnSupprimer.setManaged(true);
            }
        });
    }
    private void afficherMessage(String texte, boolean succes) {
        lblMessage.setText(texte);
        lblMessage.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    private void chargerFournisseurs() {
        ObservableList<Fournisseur> liste = FXCollections.observableArrayList(fournisseurDAO.getAll());
        tableFournisseurs.setItems(liste);
    }

    @FXML
    private void ajouterFournisseur() {
        String nomSociete = txtNomSociete.getText().trim();
        String email = txtEmail.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();

        if (nomSociete.isEmpty() ||telephone.isEmpty() || adresse.isEmpty()) {
            afficherMessage("Tous les champs sont obligatoires.", false);
            return;
        }

        boolean succes;

        if (fournisseurEnEdition != null) {
            fournisseurEnEdition.setNomSociete(nomSociete);
            fournisseurEnEdition.setEmail(email);
            fournisseurEnEdition.setTelephone(telephone);
            fournisseurEnEdition.setAdresse(adresse);
            succes = fournisseurDAO.modifier(fournisseurEnEdition);
            if (succes) afficherMessage("Fournisseur modifié avec succès.", true);
        } else {
            Fournisseur fournisseur = new Fournisseur();
            fournisseur.setNomSociete(nomSociete);
            fournisseur.setEmail(email);
            fournisseur.setTelephone(telephone);
            fournisseur.setAdresse(adresse);
            succes = fournisseurDAO.ajouter(fournisseur);
            if (succes) afficherMessage("Fournisseur"+ fournisseur.getNomSociete().toUpperCase() +" ajouté avec succès.", true);
        }

        if (succes) {
            annulerFournisseur();
            chargerFournisseurs();
            new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Ajout de Fournisseur : " + nomSociete.toUpperCase());
        } else {
            afficherMessage("Erreur lors de l'opération.", false);
        }
    }

    @FXML
    private void supprimerFournisseur() {
        if (fournisseurEnEdition == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer \"" + fournisseurEnEdition.getNomSociete() + "\" ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean succes = fournisseurDAO.supprimer(fournisseurEnEdition.getIdFournisseur());
                if (succes) {
                    afficherMessage("Fournisseur supprimé avec succès.", true);
                    annulerFournisseur();
                    chargerFournisseurs();
                    new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Suppression du Fournisseur : " + fournisseurEnEdition.getNomSociete());
                } else {
                    afficherMessage("Impossible de supprimer : ce fournisseur est peut-être lié à une commande existante.", false);
                }
            }
        });
    }

    @FXML
    private void annulerFournisseur() {
        fournisseurEnEdition = null;
        txtNomSociete.clear();
        txtEmail.clear();
        txtTelephone.clear();
        txtAdresse.clear();
        btnAjouter.setText("Ajouter le fournisseur");
        btnAnnuler.setVisible(false);
        btnAnnuler.setManaged(false);
        btnSupprimer.setVisible(false);
        btnSupprimer.setManaged(false);
        tableFournisseurs.getSelectionModel().clearSelection();
    }

}
