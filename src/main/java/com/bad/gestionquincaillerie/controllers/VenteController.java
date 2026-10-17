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
import com.bad.gestionquincaillerie.dao.*;
import com.bad.gestionquincaillerie.models.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VenteController implements Initializable {

    @FXML
    private ComboBox<Client> cbClient;
    @FXML
    private ComboBox<String> cbModePayment;
    @FXML
    private TextField txtMontantRecu;
    @FXML
    private Label lblNumVente;
    @FXML
    private Label lblMontantTotal;

    @FXML
    private ComboBox<TypeProduit> cbType;
    @FXML
    private ComboBox<Produit> cbProduit;
    @FXML
    private Label lblUnite;
    @FXML
    private Label lblPrixUnitaire;
    @FXML
    private Label lblStockDisponible;
    @FXML
    private TextField txtQuantiteLigne;
    @FXML
    private Button btnAjouterLigne;
    @FXML
    private TableView<LigneVente> tableLignesTemp;
    @FXML
    private TableColumn<LigneVente, String> colLigneTempProduit;
    @FXML
    private TableColumn<LigneVente, Integer> colLigneTempQuantite;
    @FXML
    private TableColumn<LigneVente, Double> colLigneTempPrixUnitaire;
    @FXML
    private TableColumn<LigneVente, Double> colLigneTempTotal;
    @FXML
    private TableColumn<LigneVente, Void> colLigneTempAction;
    @FXML
    private Button btnEnregistrerVente;
    @FXML
    private Button btnAnnulerVente;
    @FXML
    private Label lblMessage;

    @FXML
    private ComboBox<String> cbMoisHistorique;
    @FXML
    private Label lblMontantMois;

    @FXML
    private TableView<Vente> tableVentes;
    @FXML
    private TableColumn<Vente, String> colNumVente;
    @FXML
    private TableColumn<Vente, String> colClient;
    @FXML
    private TableColumn<Vente, String> colDateVente;
    @FXML
    private TableColumn<Vente, Double> colMontantTotalCol;
    @FXML
    private TableColumn<Vente, Double> colMontantRestantCol;
    @FXML
    private TableColumn<Vente, String> colStatutPaymentCol;
    @FXML
    private TableColumn<Vente, Boolean> colValidationCol;

    @FXML
    private TableView<LigneVente> tableDetailsLignes;
    @FXML
    private TableColumn<LigneVente, String> colDetailProduit;
    @FXML
    private TableColumn<LigneVente, Integer> colDetailQuantite;
    @FXML
    private TableColumn<LigneVente, Double> colDetailPrixUnitaire;
    @FXML
    private TableColumn<LigneVente, Double> colDetailTotal;
    @FXML
    private TableColumn<LigneVente, Boolean> colDetailStatut;
    @FXML
    private TableColumn<LigneVente, Void> colDetailAction;

    @FXML
    private TableView<ReçusVente> tableRecus;
    @FXML
    private TableColumn<ReçusVente, String> colRecuNum;
    @FXML
    private TableColumn<ReçusVente, String> colRecuVente;
    @FXML
    private TableColumn<ReçusVente, String> colRecuClient;
    @FXML
    private TableColumn<ReçusVente, Double> colRecuMontant;
    @FXML
    private TableColumn<ReçusVente, Double> colRecuRest;
    @FXML
    private TableColumn<ReçusVente, Double> colRecuReçu;
    @FXML
    private TableColumn<ReçusVente, String> colRecuDate;

    private final VenteDAO venteDAO = new VenteDAO();
    private final LigneVenteDAO ligneVenteDAO = new LigneVenteDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();
    private final TypeProduitDAO typeDAO = new TypeProduitDAO();
    private final CreditVenteDAO creditVenteDAO = new CreditVenteDAO();
    private final ReçusVenteDAO recuVenteDAO = new ReçusVenteDAO();

    private final ObservableList<LigneVente> lignesTemporaires = FXCollections.observableArrayList();
    private ObservableList<TypeProduit> typesComplets = FXCollections.observableArrayList();
    private ObservableList<Produit> produitsDuTypeActuel = FXCollections.observableArrayList();
    private Employe employeConnecte;
    private String numVenteActuel;
    List<String> moisDisponibles = venteDAO.getMoisDisponibles();

    private static final String[] MODES_PAYMENT = {"Espèces", "Chèque", "Virement", "Mobile Money"};
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private LigneVente ligneEnEditionTemp = null;

    public void initEmploye(Employe employe) {
        this.employeConnecte = employe;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbModePayment.setItems(FXCollections.observableArrayList(MODES_PAYMENT));
        cbClient.setItems(FXCollections.observableArrayList(clientDAO.getAll()));

        typesComplets = FXCollections.observableArrayList(typeDAO.getAll());
        cbType.setItems(typesComplets);

        // ---- Convertisseurs d'affichage ----
        cbClient.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client c) {
                return c != null ? c.getNom() : "";
            }

            @Override
            public Client fromString(String s) {
                return null;
            }
        });

        cbType.setEditable(true);
        cbType.setConverter(new StringConverter<TypeProduit>() {
            @Override
            public String toString(TypeProduit t) {
                return t != null ? t.getNomType() : "";
            }

            @Override
            public TypeProduit fromString(String s) {
                return cbType.getItems().stream().filter(t -> t.getNomType().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        });
        cbType.getEditor().textProperty().addListener((obs, ancien, texte) -> {
            if (cbType.getValue() != null && cbType.getValue().getNomType().equals(texte)) {
                return;
            }
            if (texte == null || texte.isEmpty()) {
                cbType.setItems(typesComplets);
                return;
            }
            ObservableList<TypeProduit> filtres = typesComplets.stream()
                    .filter(t -> t.getNomType().toLowerCase().contains(texte.toLowerCase()))
                    .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
            cbType.setItems(filtres);
            if (!cbType.isShowing()) {
                cbType.show();
            }
        });

        cbProduit.setEditable(true);
        cbProduit.setConverter(new StringConverter<Produit>() {
            @Override
            public String toString(Produit p) {
                return p != null ? p.getDesignation() : "";
            }

            @Override
            public Produit fromString(String s) {
                return cbProduit.getItems().stream().filter(p -> p.getDesignation().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        });
        cbProduit.getEditor().textProperty().addListener((obs, ancien, texte) -> {
            if (cbProduit.getValue() != null && cbProduit.getValue().getDesignation().equals(texte)) {
                return;
            }
            if (texte == null || texte.isEmpty()) {
                cbProduit.setItems(produitsDuTypeActuel);
                return;
            }
            ObservableList<Produit> filtres = produitsDuTypeActuel.stream()
                    .filter(p -> p.getDesignation().toLowerCase().contains(texte.toLowerCase()))
                    .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
            cbProduit.setItems(filtres);
            if (!cbProduit.isShowing()) {
                cbProduit.show();
            }
        });

        // ---- Cascade Type -> Produit ----
        cbType.valueProperty().addListener((obs, ancien, typeSelectionne) -> {
            cbProduit.setValue(null);
            lblUnite.setText("—");
            lblPrixUnitaire.setText("—");
            lblStockDisponible.setText("—");
            if (typeSelectionne != null) {
                produitsDuTypeActuel = FXCollections.observableArrayList(produitDAO.getByType(typeSelectionne.getIdType()));
                cbProduit.setItems(produitsDuTypeActuel);
            } else {
                produitsDuTypeActuel.clear();
                cbProduit.getItems().clear();
            }
        });

        // ---- Affichage unité / prix / stock du produit sélectionné ----
        cbProduit.valueProperty().addListener((obs, ancien, produit) -> {
            if (produit != null) {
                lblUnite.setText(produit.getUnite());
                // lblPrixUnitaire.setText(String.valueOf(produit.getPrixVente()));
                // lblStockDisponible.setText(String.valueOf(produit.getQuantite()));  
                if (cbType.getValue().toString().equals("ciment")) {
                    int qt = produit.getQuantite() / 20;
                    lblPrixUnitaire.setText(FormatUtilitaire.formaterMontant(produit.getPrixVente()));
                    lblStockDisponible.setText(FormatUtilitaire.formaterMontant(produit.getQuantite()) + "Sacs = " + String.valueOf(qt) + " Tonnes");
                } else {
                    lblStockDisponible.setText(FormatUtilitaire.formaterMontant(produit.getQuantite()));
                    lblPrixUnitaire.setText(FormatUtilitaire.formaterMontant(produit.getPrixVente()));
                }
            } else {
                lblUnite.setText("—");
                lblPrixUnitaire.setText("—");
                lblStockDisponible.setText("—");
            }
        });

        colLigneTempProduit.setCellValueFactory(new PropertyValueFactory<>("designationProduit"));
        colLigneTempProduit.setCellFactory(col -> new TableCell<LigneVente, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colLigneTempQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colLigneTempPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colLigneTempPrixUnitaire.setCellFactory(col -> new TableCell<LigneVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colLigneTempTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrix"));
        colLigneTempTotal.setCellFactory(col -> new TableCell<LigneVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colLigneTempAction.setCellFactory(col -> new TableCell<LigneVente, Void>() {
            private final Button btnModifier = new Button("Modifier");

            {
                btnModifier.getStyleClass().add("btn-stocker");
                btnModifier.setOnAction(event -> {
                    LigneVente ligne = getTableView().getItems().get(getIndex());
                    chargerLignePourEdition(ligne);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnModifier);
            }
        });

        tableLignesTemp.setItems(lignesTemporaires);

        colNumVente.setCellValueFactory(new PropertyValueFactory<>("numVente"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colClient.setCellFactory(col -> new TableCell<Vente, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colDateVente.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
                c.getValue().getDateVente() != null ? c.getValue().getDateVente().format(FORMAT_DATE) : ""
        ));
        colMontantTotalCol.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colMontantTotalCol.setCellFactory(col -> new TableCell<Vente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colMontantRestantCol.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));
        colMontantRestantCol.setCellFactory(col -> new TableCell<Vente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colStatutPaymentCol.setCellValueFactory(new PropertyValueFactory<>("statutPayment"));
        colStatutPaymentCol.setCellFactory(col -> new TableCell<Vente, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());

            }
        });
        colValidationCol.setCellValueFactory(new PropertyValueFactory<>("validation"));
        colValidationCol.setCellFactory(col -> new TableCell<Vente, Boolean>() {
            @Override
            protected void updateItem(Boolean valide, boolean empty) {
                super.updateItem(valide, empty);
                if (empty || valide == null) {
                    setText(null);
                } else {
                    setText(valide ? "Oui" : "Non");
                    setStyle(valide ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;" : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });

        colDetailProduit.setCellValueFactory(new PropertyValueFactory<>("designationProduit"));
        colDetailProduit.setCellFactory(col -> new TableCell<LigneVente, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());

            }
        });
        colDetailQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colDetailPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colDetailPrixUnitaire.setCellFactory(col -> new TableCell<LigneVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colDetailTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrix"));
        colDetailTotal.setCellFactory(col -> new TableCell<LigneVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colDetailStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDetailStatut.setCellFactory(col -> new TableCell<LigneVente, Boolean>() {
            @Override
            protected void updateItem(Boolean actif, boolean empty) {
                super.updateItem(actif, empty);
                if (empty || actif == null) {
                    setText(null);
                } else {
                    setText(actif ? "Valide" : "Annulé");
                    setStyle(actif ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;" : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });
        colDetailAction.setCellFactory(col -> new TableCell<LigneVente, Void>() {
            private final Button btnAnnuler = new Button("Annuler");

            {
                btnAnnuler.getStyleClass().add("btn-stocker"); // réutilise le style vert->on l'adapte en rouge ci-dessous
                btnAnnuler.setOnAction(event -> {
                    LigneVente ligne = getTableView().getItems().get(getIndex());
                    annulerLigneVente(ligne);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    LigneVente ligne = getTableView().getItems().get(getIndex());
                    setGraphic(ligne.isStatut() ? btnAnnuler : null);
                }
            }
        });

        colRecuNum.setCellValueFactory(new PropertyValueFactory<>("numRecus"));
        colRecuVente.setCellValueFactory(new PropertyValueFactory<>("numVente"));
        colRecuClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colRecuClient.setCellFactory(col -> new TableCell<ReçusVente, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        // Montant Restant (négatif = créance en rouge, positif = monnaie rendue en vert)
        colRecuRest.setCellValueFactory(new PropertyValueFactory<>("montantRestantVente"));
        colRecuRest.setCellFactory(col -> new TableCell<ReçusVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                if (empty || montant == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(FormatUtilitaire.formaterMontant(montant));
                    setStyle(montant >= 0
                            ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                            : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });
        // Montant Reçu (ce que le client a effectivement donné)
        colRecuReçu.setCellValueFactory(new PropertyValueFactory<>("montantRecuVente"));
        colRecuReçu.setCellFactory(col -> new TableCell<ReçusVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        // Montant Total (= r.montant, le montant du reçus qui représente maintenant le total)
        colRecuMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colRecuMontant.setCellFactory(col -> new TableCell<ReçusVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colRecuDate.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
                c.getValue().getDateRecus() != null ? c.getValue().getDateRecus().format(FORMAT_DATE) : ""
        ));

        tableRecus.setItems(FXCollections.observableArrayList(recuVenteDAO.getAll()));

        chargerVentes();

        List<String> moisDisponibles = venteDAO.getMoisDisponibles();
        cbMoisHistorique.setItems(FXCollections.observableArrayList(moisDisponibles));

        if (!moisDisponibles.isEmpty()) {
            cbMoisHistorique.setValue(moisDisponibles.get(0)); // mois le plus récent par défaut
            afficherMontantDuMois(moisDisponibles.get(0));
        }

        cbMoisHistorique.valueProperty().addListener((obs, ancien, moisSelectionne) -> {
            if (moisSelectionne != null) {
                afficherMontantDuMois(moisSelectionne);
            }
        });

        genererNouveauNumeroVente();

        tableVentes.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                tableDetailsLignes.setItems(FXCollections.observableArrayList(ligneVenteDAO.getByVente(selection.getIdVente())));
            }
        });
    }

    private void chargerLignePourEdition(LigneVente ligne) {
        ligneEnEditionTemp = ligne;

        // Retrouve et sélectionne le type/produit correspondant
        for (TypeProduit t : typesComplets) {
            if (produitsDuTypeActuel.stream().anyMatch(p -> p.getIdProduit() == ligne.getIdProduit())) {
                break;
            }
        }
        // Simplification : on recharge directement par le produit connu
        Produit produitTrouve = produitDAO.getAll().stream()
                .filter(p -> p.getIdProduit() == ligne.getIdProduit())
                .findFirst().orElse(null);

        if (produitTrouve != null) {
            TypeProduit typeDuProduit = typesComplets.stream()
                    .filter(t -> t.getIdType() == produitTrouve.getIdType())
                    .findFirst().orElse(null);

            cbType.setValue(typeDuProduit);
            cbProduit.setValue(produitTrouve);
        }

        txtQuantiteLigne.setText(String.valueOf(ligne.getQuantite()));
        btnAjouterLigne.setText("Mettre à jour la ligne");
    }

    private void annulerLigneVente(LigneVente ligne) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation d'annulation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Annuler la vente de " + ligne.getQuantite() + " x "
                + ligne.getDesignationProduit() + " ? La quantité sera remise en stock.");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean ligneAnnulee = ligneVenteDAO.annulerLigne(ligne.getIdLv());
                if (ligneAnnulee) {
                    produitDAO.incrementerStock(ligne.getIdProduit(), ligne.getQuantite());

                    Vente selection = tableVentes.getSelectionModel().getSelectedItem();
                    if (selection != null) {
                        boolean encoreActive = ligneVenteDAO.auMoinsUneLigneActive(selection.getIdVente());
                        venteDAO.mettreAJourValidation(selection.getIdVente(), encoreActive);
                        selection.setValidation(encoreActive);
                        tableVentes.refresh();

                        tableDetailsLignes.setItems(FXCollections.observableArrayList(
                                ligneVenteDAO.getByVente(selection.getIdVente())
                        ));
                    }
                }
            }
        });
    }

    private void afficherMontantDuMois(String moisAnnee) {
        // Format attendu : "2026-07"
        String[] parties = moisAnnee.split("-");
        int annee = Integer.parseInt(parties[0]);
        int mois = Integer.parseInt(parties[1]);

        double montant = venteDAO.getMontantVentesDuMois(mois, annee);
        lblMontantMois.setText("Total du mois : " + FormatUtilitaire.formaterMontant(montant) + " FCFA");
    }

    private void genererNouveauNumeroVente() {
        numVenteActuel = venteDAO.genererProchainNumVente();
        lblNumVente.setText("Vente N° : " + numVenteActuel);
        mettreAJourMontantTotal();
    }

    private void mettreAJourMontantTotal() {
        double total = lignesTemporaires.stream().mapToDouble(LigneVente::getTotalPrix).sum();
        lblMontantTotal.setText("Montant total de la vente : " + FormatUtilitaire.formaterMontant(total) + " FCFA");
    }

    @FXML
    private void ajouterLigneTemporaire() {
        TypeProduit type = cbType.getValue();
        Produit produit = cbProduit.getValue();
        String quantiteTexte = txtQuantiteLigne.getText().trim();

        if (type == null || produit == null) {
            afficherMessage("Veuillez sélectionner un type et un produit existants dans les listes.", false);
            return;
        }
        if (quantiteTexte.isEmpty()) {
            afficherMessage("Veuillez saisir la quantité.", false);
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(quantiteTexte);
        } catch (NumberFormatException e) {
            afficherMessage("La quantité doit être un nombre valide.", false);
            return;
        }

        if (quantite <= 0) {
            afficherMessage("La quantité doit être supérieure à 0.", false);
            return;
        }

        if (quantite > produit.getQuantite()) {
            afficherMessage("Stock insuffisant. Disponible : " + produit.getQuantite() + ".", false);
            return;
        }

        double prixUnitaire = produit.getPrixVente();

        if (ligneEnEditionTemp != null) {
            // ===== Mode mise à jour d'une ligne existante =====
            ligneEnEditionTemp.setIdProduit(produit.getIdProduit());
            ligneEnEditionTemp.setDesignationProduit(produit.getDesignation());
            ligneEnEditionTemp.setQuantite(quantite);
            ligneEnEditionTemp.setPrixUnitaire(prixUnitaire);
            ligneEnEditionTemp.setTotalPrix(quantite * prixUnitaire);
            tableLignesTemp.refresh();
            ligneEnEditionTemp = null;
            btnAjouterLigne.setText("Ajouter cette ligne");
        } else {
            // ===== Mode ajout normal =====
            LigneVente ligne = new LigneVente();
            ligne.setIdProduit(produit.getIdProduit());
            ligne.setDesignationProduit(produit.getDesignation());
            ligne.setQuantite(quantite);
            ligne.setPrixUnitaire(prixUnitaire);
            ligne.setTotalPrix(quantite * prixUnitaire);
            lignesTemporaires.add(ligne);
        }
        cbType.setValue(null);
        cbType.getEditor().clear();
        txtQuantiteLigne.clear();

        mettreAJourMontantTotal();
    }

    @FXML
    private void enregistrerVente() {
        Client client = cbClient.getValue();
        String modePayment = cbModePayment.getValue();
        String montantRecuTexte = txtMontantRecu.getText().trim();

        if (modePayment == null || montantRecuTexte.isEmpty()) {
            afficherMessage("Veuillez remplir toutes les informations de la vente.", false);
            return;
        }

        if (lignesTemporaires.isEmpty()) {
            afficherMessage("Ajoutez au moins un produit à la vente.", false);
            return;
        }

        double montantRecu;
        try {
            montantRecu = Double.parseDouble(montantRecuTexte);
        } catch (NumberFormatException e) {
            afficherMessage("Le montant reçu doit être un nombre valide.", false);
            return;
        }

        double montantTotal = lignesTemporaires.stream().mapToDouble(LigneVente::getTotalPrix).sum();

        // ===== Règle métier : sans client, le paiement doit être total =====
        if (client == null && montantRecu < montantTotal) {
            afficherMessage("Une vente sans client enregistré doit être payée intégralement (pas de crédit possible).", false);
            return;
        }

        // ===== Calcul automatique du statut et du montant restant =====
        double montantRestant = montantRecu - montantTotal;
        String statutPayment = (montantTotal <= montantRecu) ? "Payé" : "Partiel";

        Vente vente = new Vente();
        vente.setIdClient(client != null ? client.getIdClient() : 0);
        vente.setIdEmploye(employeConnecte.getId());
        vente.setModePayment(modePayment);
        vente.setMontantTotal(montantTotal);
        vente.setMontantRecu(montantRecu);
        vente.setMontantRestant(montantRestant);
        vente.setStatutPayment(statutPayment);
        vente.setAdresseLivraison(null);
        vente.setVehiculeLivraison(null);

        Vente resultat = venteDAO.ajouter(vente, numVenteActuel);

        if (resultat != null) {
            resultat.setNomClient(client != null ? client.getNom() : "Client anonyme");
            List<LigneVente> lignesEnregistrees = new ArrayList<>();

            for (LigneVente ligne : lignesTemporaires) {
                ligne.setIdVente(resultat.getIdVente());
                ligneVenteDAO.ajouter(ligne);
                produitDAO.decrementerStock(ligne.getIdProduit(), ligne.getQuantite());
                lignesEnregistrees.add(ligne);
            }

            venteDAO.mettreAJourValidation(resultat.getIdVente(), true);
            resultat.setValidation(true);

            // ===== Création automatique du crédit si créance (montant_restant négatif) =====
            if (client != null && montantRestant < 0) {
                CreditVente credit = new CreditVente();
                credit.setIdClient(client.getIdClient());
                credit.setIdVente(resultat.getIdVente());
                credit.setMontantCredit(Math.abs(montantRestant));
                credit.setMontantPaye(0);

                int idCreditGenere = creditVenteDAO.ajouter(credit);
                if (idCreditGenere != -1) {
                    clientDAO.lierCredit(client.getIdClient(), idCreditGenere);
                }
            }

            afficherMessage("Vente " + resultat.getNumVente() + " enregistrée avec succès.", true);
            ouvrirPanneauPostVente(resultat, lignesEnregistrees);
            annulerNouvelleVente();
            chargerVentes();
            new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "realiser Vente : " + resultat.getNumVente());
        } else {
            afficherMessage("Erreur lors de l'enregistrement de la vente.", false);
        }
    }

    private void ouvrirPanneauPostVente(Vente vente, List<LigneVente> lignes) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/PostVente.fxml")
            );
            Parent root = loader.load();

            PostVenteController controller = loader.getController();
            controller.initData(vente, lignes);

            Stage stage = new Stage();
            stage.setTitle("Détails de la vente " + vente.getNumVente());
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void annulerNouvelleVente() {
        cbClient.setValue(null);
        cbModePayment.setValue(null);
        txtMontantRecu.clear();
        cbType.setValue(null);
        cbType.getEditor().clear();
        cbProduit.getItems().clear();
        cbProduit.getEditor().clear();
        lblUnite.setText("—");
        lblPrixUnitaire.setText("—");
        lblStockDisponible.setText("—");
        txtQuantiteLigne.clear();
        lignesTemporaires.clear();
        genererNouveauNumeroVente();
    }

    private void chargerVentes() {
        tableVentes.setItems(FXCollections.observableArrayList(venteDAO.getAll()));
    }

    private void afficherMessage(String texte, boolean succes) {
        lblMessage.setText(texte);
        lblMessage.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
