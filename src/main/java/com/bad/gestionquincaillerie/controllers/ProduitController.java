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
import com.bad.gestionquincaillerie.dao.EmplacementDAO;
import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import com.bad.gestionquincaillerie.dao.ProduitDAO;
import com.bad.gestionquincaillerie.dao.TypeProduitDAO;
import com.bad.gestionquincaillerie.models.Emplacement;
import com.bad.gestionquincaillerie.models.Employe;
import com.bad.gestionquincaillerie.models.Produit;
import com.bad.gestionquincaillerie.models.TypeProduit;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.collections.transformation.FilteredList;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProduitController implements Initializable {

    @FXML
    private TextField txtDesignation;
    @FXML
    private TextField txtMarque;
    @FXML
    private ComboBox<TypeProduit> cbType;
    @FXML
    private ComboBox<Emplacement> cbEmplacement;
    @FXML
    private ComboBox<String> cbUnite;
    @FXML
    private TextField txtPrixAchat;
    @FXML
    private TextField txtPrixVente;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnAnnuler;
    @FXML
    private Button btnSupprimer;

    @FXML private ComboBox<TypeProduit> cbFiltreType;
    @FXML private ComboBox<String> cbFiltreUnite;
    @FXML private TextField txtRecherche;
    @FXML private Button btnReinitialiserFiltres;
    @FXML
    private TableView<Produit> tableProduits;
    @FXML
    private TableColumn<Produit, Integer> colId;
    @FXML
    private TableColumn<Produit, String> colReference;
    @FXML
    private TableColumn<Produit, String> colDesignation;
    @FXML
    private TableColumn<Produit, String> colMarque;
    @FXML
    private TableColumn<Produit, String> colType;
    @FXML
    private TableColumn<Produit, String> colEmplacement;
    @FXML
    private TableColumn<Produit, Double> colPrixAchat;
    @FXML
    private TableColumn<Produit, Double> colPrixVente;
    @FXML
    private TableColumn<Produit, Integer> colQuantite;
    @FXML
    private TableColumn<Produit, Boolean> colAlerte;
    @FXML
    private TableColumn<Produit, String> colUnite;

    private final ProduitDAO produitDAO = new ProduitDAO();
    private final TypeProduitDAO typeDAO = new TypeProduitDAO();
    private final EmplacementDAO emplacementDAO = new EmplacementDAO();
    private Employe employeConnecte;
    private FilteredList<Produit> produitsFiltres;

    private Produit produitEnEdition = null;

    
    private static final String[] UNITES = {"Pièce", "Kg", "Litre", "Mètre", "Carton", "Boîte", "Sac"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbFiltreType.valueProperty().addListener((obs, ancien, nouveau) -> appliquerFiltres());
        cbFiltreType.setConverter(new javafx.util.StringConverter<TypeProduit>() {
            @Override 
            public String toString(TypeProduit t) {
                return t != null ? t.getNomType() : ""; 
            }
            @Override
            public TypeProduit fromString(String s) {
                return null;
            }
        });
        cbFiltreUnite.valueProperty().addListener((obs, ancien, nouveau) -> appliquerFiltres());
        txtRecherche.textProperty().addListener((obs, ancien, nouveau) -> appliquerFiltres());
        
        colId.setCellValueFactory(new PropertyValueFactory<>("idProduit"));
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colDesignation.setCellFactory(col -> new TableCell<Produit, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colMarque.setCellFactory(col -> new TableCell<Produit, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colType.setCellValueFactory(new PropertyValueFactory<>("nomType"));
        colEmplacement.setCellValueFactory(new PropertyValueFactory<>("designationEmplacement"));
        colPrixAchat.setCellValueFactory(new PropertyValueFactory<>("prixAchat"));
        colPrixAchat.setCellFactory(col -> new TableCell<Produit, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colPrixVente.setCellValueFactory(new PropertyValueFactory<>("prixVente"));
        colPrixVente.setCellFactory(col -> new TableCell<Produit, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colUnite.setCellValueFactory(new PropertyValueFactory<>("unite"));
        colUnite.setCellFactory(col -> new TableCell<Produit, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });

        // Colonne alerte : affiche "Oui"/"Non" au lieu de true/false
        colAlerte.setCellValueFactory(new PropertyValueFactory<>("alerteSeuil"));
        colAlerte.setCellFactory(col -> new TableCell<Produit, Boolean>() {
            @Override
            protected void updateItem(Boolean alerte, boolean empty) {
                super.updateItem(alerte, empty);
                if (empty || alerte == null) {
                    setText(null);
                } else {
                    setText(alerte ? "Oui" : "Non");
                    setStyle(alerte ? "-fx-text-fill: #e74c3c; -fx-font-weight: bold;" : "-fx-text-fill: #27ae60;");
                }
            }
        });

        cbUnite.setItems(FXCollections.observableArrayList(UNITES));

        chargerTypes();
        chargerEmplacements();
        chargerProduits();

        tableProduits.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                produitEnEdition = selection;
                txtDesignation.setText(selection.getDesignation());
                txtMarque.setText(selection.getMarque());
                txtPrixAchat.setText(String.valueOf(selection.getPrixAchat()));
                txtPrixVente.setText(String.valueOf(selection.getPrixVente()));
                cbUnite.setValue(selection.getUnite());

                for (TypeProduit t : cbType.getItems()) {
                    if (t.getIdType() == selection.getIdType()) {
                        cbType.setValue(t);
                        break;
                    }
                }
                for (Emplacement e : cbEmplacement.getItems()) {
                    if (e.getIdEmplacement() == selection.getIdEmplacement()) {
                        cbEmplacement.setValue(e);
                        break;
                    }
                }

                btnAjouter.setText("Modifier le produit");
                btnAnnuler.setVisible(true);
                btnAnnuler.setManaged(true);
                btnSupprimer.setVisible(true);
                btnSupprimer.setManaged(true);
            }
        });
    }

    private void chargerTypes() {
        cbType.setItems(FXCollections.observableArrayList(typeDAO.getAll()));
    }

    private void chargerEmplacements() {
        cbEmplacement.setItems(FXCollections.observableArrayList(emplacementDAO.getAll()));
    }
    
    private void chargerProduits() {
        ObservableList<Produit> liste = FXCollections.observableArrayList(produitDAO.getAll());
        produitsFiltres = new FilteredList<>(liste, p -> true);
        tableProduits.setItems(produitsFiltres);

        // Remplit les combobox de filtre avec les valeurs disponibles
        cbFiltreType.setItems(FXCollections.observableArrayList(typeDAO.getAll()));

        ObservableList<String> unitesDisponibles = FXCollections.observableArrayList(
            liste.stream().map(Produit::getUnite).distinct().sorted().collect(Collectors.toList())
        );
        cbFiltreUnite.setItems(unitesDisponibles);
    }
    
    private void appliquerFiltres() {
        if (produitsFiltres == null) return;

        TypeProduit typeSelectionne = cbFiltreType.getValue();
        String uniteSelectionnee = cbFiltreUnite.getValue();
        String recherche = txtRecherche.getText() != null ? txtRecherche.getText().trim().toLowerCase() : "";

        produitsFiltres.setPredicate(produit -> {
            boolean correspondType = (typeSelectionne == null) || produit.getIdType() == typeSelectionne.getIdType();
            boolean correspondUnite = (uniteSelectionnee == null) || uniteSelectionnee.equals(produit.getUnite());
            boolean correspondRecherche = recherche.isEmpty()
                    || produit.getDesignation().toLowerCase().contains(recherche)
                    || produit.getMarque().toLowerCase().contains(recherche)
                    || produit.getReference().toLowerCase().contains(recherche);

            return correspondType && correspondUnite && correspondRecherche;
        });
    }

    @FXML
    private void ajouterProduit() {
        String designation = txtDesignation.getText().trim();
        String marque = txtMarque.getText().trim();
        TypeProduit type = cbType.getValue();
        Emplacement emplacement = cbEmplacement.getValue();
        String unite = cbUnite.getValue();
        String prixAchatTexte = txtPrixAchat.getText().trim();
        String prixVenteTexte = txtPrixVente.getText().trim();

        if (designation.isEmpty() || marque.isEmpty() || type == null || emplacement == null
                || unite == null || prixAchatTexte.isEmpty() || prixVenteTexte.isEmpty()) {
            afficherMessage("Tous les champs sont obligatoires.", false);
            return;
        }

        double prixAchat, prixVente;
        try {
            prixAchat = Double.parseDouble(prixAchatTexte);
            prixVente = Double.parseDouble(prixVenteTexte);
        } catch (NumberFormatException e) {
            afficherMessage("Les prix doivent être des nombres valides.", false);
            return;
        }

        if (produitEnEdition != null) {
            // ===== Mode modification (reference/code_barre/quantite ne changent pas) =====
            produitEnEdition.setDesignation(designation);
            produitEnEdition.setMarque(marque);
            produitEnEdition.setIdType(type.getIdType());
            produitEnEdition.setIdEmplacement(emplacement.getIdEmplacement());
            produitEnEdition.setUnite(unite);
            produitEnEdition.setPrixAchat(prixAchat);
            produitEnEdition.setPrixVente(prixVente);

            boolean succes = produitDAO.modifier(produitEnEdition);
            if (succes) {
                afficherMessage("Produit modifié avec succès.", true);
                annulerProduit();
                chargerProduits();
            } else {
                afficherMessage("Erreur lors de la modification.", false);
            }

        } else {
            // ===== Mode ajout =====
            Produit produit = new Produit();
            produit.setDesignation(designation);
            produit.setMarque(marque);
            produit.setIdType(type.getIdType());
            produit.setIdEmplacement(emplacement.getIdEmplacement());
            produit.setUnite(unite);
            produit.setPrixAchat(prixAchat);
            produit.setPrixVente(prixVente);

            Produit resultat = produitDAO.ajouter(produit);
            if (resultat != null) {
                afficherMessage("Produit ajouté avec succès. Référence générée : " + resultat.getReference(), true);
                annulerProduit();
                chargerProduits();
                new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Ajout du produit : " + resultat.getDesignation());
            } else {
                afficherMessage("Erreur lors de l'ajout du produit.", false);
            }
        }
    }

    @FXML
    private void supprimerProduit() {
        if (produitEnEdition == null) {
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer le produit \"" + produitEnEdition.getDesignation() + "\" ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean succes = produitDAO.supprimer(produitEnEdition.getIdProduit());
                if (succes) {
                    afficherMessage("Produit supprimé avec succès.", true);
                    annulerProduit();
                    chargerProduits();
                    new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Suppression du produit : " + produitEnEdition.getDesignation());
                } else {
                    afficherMessage("Impossible de supprimer ce produit.", false);
                }
            }
        });
    }

    @FXML
    private void reinitialiserFiltres() {
        cbFiltreType.setValue(null);
        cbFiltreUnite.setValue(null);
        txtRecherche.clear();
    }
    
    @FXML
    private void annulerProduit() {
        produitEnEdition = null;
        txtDesignation.clear();
        txtMarque.clear();
        txtPrixAchat.clear();
        txtPrixVente.clear();
        cbType.setValue(null);
        cbEmplacement.setValue(null);
        cbUnite.setValue(null);
        btnAjouter.setText("Ajouter le produit");
        btnAnnuler.setVisible(false);
        btnAnnuler.setManaged(false);
        btnSupprimer.setVisible(false);
        btnSupprimer.setManaged(false);
        tableProduits.getSelectionModel().clearSelection();
    }

    private void afficherMessage(String texte, boolean succes) {
        lblMessage.setText(texte);
        lblMessage.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
