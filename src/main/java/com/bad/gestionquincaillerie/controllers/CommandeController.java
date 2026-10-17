/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

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
import javafx.util.StringConverter;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CommandeController implements Initializable {

    // ---- Nouvelle commande : en-tête ----
    @FXML
    private ComboBox<Fournisseur> cbFournisseur;
    @FXML
    private ComboBox<String> cbModePayment;
    @FXML
    private TextField txtMontantPaye;
    @FXML
    private Label lblNumCommande;
    @FXML
    private Label lblMontantTotal;

    // ---- Nouvelle commande : ligne produit ----
    // Nouveaux champs pour le filtrage en cascade
    @FXML
    private ComboBox<TypeProduit> cbType;
    @FXML
    private ComboBox<Produit> cbProduit;
    @FXML
    private Label lblUnite;
    @FXML
    private TextField txtQuantiteLigne;
    @FXML
    private Label lblPrixUnitaire;
    @FXML
    private Button btnAjouterLigne;
    @FXML
    private TableView<LigneCommande> tableLignesTemp;
    @FXML
    private TableColumn<LigneCommande, String> colLigneTempProduit;
    @FXML
    private TableColumn<LigneCommande, Integer> colLigneTempQuantite;
    @FXML
    private TableColumn<LigneCommande, Double> colLigneTempPrixUnitaire;
    @FXML
    private TableColumn<LigneCommande, Double> colLigneTempTotal;
    @FXML
    private TableColumn<LigneCommande, Void> colLigneTempAction;
    @FXML
    private Button btnEnregistrerCommande;
    @FXML
    private Button btnAnnulerCommande;
    @FXML
    private Label lblMessage;

    // ---- Historique ----
    @FXML
    private TableView<Commande> tableCommandes;
    @FXML
    private TableColumn<Commande, String> colNumCommande;
    @FXML
    private TableColumn<Commande, String> colFournisseur;
    @FXML
    private TableColumn<Commande, String> colDateCommande;
    @FXML
    private TableColumn<Commande, Double> colMontantTotalCol;
    @FXML
    private TableColumn<Commande, Double> colMontantRestantCol;
    @FXML
    private TableColumn<Commande, String> colStatutPaymentCol;
    @FXML
    private TableColumn<Commande, Boolean> colValidationCol;

    @FXML
    private TableView<LigneCommande> tableDetailsLignes;
    @FXML
    private TableColumn<LigneCommande, String> colDetailProduit;
    @FXML
    private TableColumn<LigneCommande, Integer> colDetailQuantite;
    @FXML
    private TableColumn<LigneCommande, Double> colDetailPrixUnitaire;
    @FXML
    private TableColumn<LigneCommande, Double> colDetailTotal;
    @FXML
    private TableColumn<LigneCommande, Boolean> colDetailStatut;
    @FXML
    private TableColumn<LigneCommande, Void> colDetailAction;

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAO();
    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();
    private final TypeProduitDAO typeDAO = new TypeProduitDAO();
    private String numCommandeActuel;

    private final ObservableList<LigneCommande> lignesTemporaires = FXCollections.observableArrayList();
    private ObservableList<Produit> produitsDuTypeActuel = FXCollections.observableArrayList();
    private ObservableList<TypeProduit> typesComplets = FXCollections.observableArrayList();
    private Employe employeConnecte;

    private static final String[] MODES_PAYMENT = {"Espèces", "Chèque", "Virement", "Mobile Money"};
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private LigneCommande ligneEnEditionTemp = null;

    /**
     * Appelée par DashboardController juste après le chargement de cette vue,
     * pour transmettre l'employé connecté (nécessaire pour id_employe).
     */
    public void initEmploye(Employe employe) {
        this.employeConnecte = employe;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbModePayment.setItems(FXCollections.observableArrayList(MODES_PAYMENT));

        cbFournisseur.setItems(FXCollections.observableArrayList(fournisseurDAO.getAll()));
        typesComplets = FXCollections.observableArrayList(typeDAO.getAll());
        cbType.setItems(typesComplets);

        // ---- Cascade Type -> Produit ----
        cbType.valueProperty().addListener((obs, ancien, typeSelectionne) -> {
            cbProduit.setValue(null);
            lblUnite.setText("—");
            if (typeSelectionne != null) {
                produitsDuTypeActuel = FXCollections.observableArrayList(
                        produitDAO.getByType(typeSelectionne.getIdType())
                );
                cbProduit.setItems(produitsDuTypeActuel);
            } else {
                produitsDuTypeActuel.clear();
                cbProduit.getItems().clear();
            }
        });
        // Rendre cbType éditable avec filtrage
        cbType.setEditable(true);

        cbType.setConverter(new StringConverter<TypeProduit>() {
            @Override
            public String toString(TypeProduit type) {
                return type != null ? type.getNomType() : "";
            }

            @Override
            public TypeProduit fromString(String string) {
                return cbType.getItems().stream()
                        .filter(t -> t.getNomType().equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        cbType.getEditor().textProperty().addListener((obs, ancienTexte, nouveauTexte) -> {
            if (cbType.getValue() != null && cbType.getValue().getNomType().equals(nouveauTexte)) {
                return;
            }

            if (nouveauTexte == null || nouveauTexte.isEmpty()) {
                cbType.setItems(typesComplets);
                return;
            }

            ObservableList<TypeProduit> filtres = typesComplets.stream()
                    .filter(t -> t.getNomType().toLowerCase().contains(nouveauTexte.toLowerCase()))
                    .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

            cbType.setItems(filtres);
            if (!cbType.isShowing()) {
                cbType.show();
            }
        });

        // Rendre cbProduit éditable avec filtrage
        cbProduit.setEditable(true);

        cbProduit.setConverter(new StringConverter<Produit>() {
            @Override
            public String toString(Produit produit) {
                return produit != null ? produit.getDesignation() : "";
            }

            @Override
            public Produit fromString(String string) {
                return cbProduit.getItems().stream()
                        .filter(p -> p.getDesignation().equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        cbProduit.getEditor().textProperty().addListener((obs, ancienTexte, nouveauTexte) -> {
            if (cbProduit.getValue() != null && cbProduit.getValue().getDesignation().equals(nouveauTexte)) {
                return; // évite de refiltrer juste après une sélection
            }

            if (nouveauTexte == null || nouveauTexte.isEmpty()) {
                cbProduit.setItems(produitsDuTypeActuel);
                return;
            }

            ObservableList<Produit> filtres = produitsDuTypeActuel.stream()
                    .filter(p -> p.getDesignation().toLowerCase().contains(nouveauTexte.toLowerCase()))
                    .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

            cbProduit.setItems(filtres);
            if (!cbProduit.isShowing()) {
                cbProduit.show();
            }
        });
        cbProduit.valueProperty().addListener((obs, ancien, produitSelectionne) -> {
            if (produitSelectionne != null) {
                lblUnite.setText(produitSelectionne.getUnite());
                lblPrixUnitaire.setText(FormatUtilitaire.formaterMontant(produitSelectionne.getPrixAchat()));

            } else {
                lblUnite.setText("—");
                lblPrixUnitaire.setText("—");
            }
        });

        // ---- Affichage de l'unité du produit sélectionné ----
        cbProduit.valueProperty().addListener((obs, ancien, produitSelectionne) -> {
            lblUnite.setText(produitSelectionne != null ? produitSelectionne.getUnite() : "—");
        });

        // ---- Colonnes table lignes temporaires ----
        colLigneTempProduit.setCellValueFactory(new PropertyValueFactory<>("designationProduit"));
        colLigneTempProduit.setCellFactory(col -> new TableCell<LigneCommande, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colLigneTempQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colLigneTempPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colLigneTempPrixUnitaire.setCellFactory(col -> new TableCell<LigneCommande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colLigneTempTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrix"));
        colLigneTempTotal.setCellFactory(col -> new TableCell<LigneCommande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });

        colLigneTempAction.setCellFactory(col -> new TableCell<LigneCommande, Void>() {
            private final Button btnModifier = new Button("Modifier");

            {
                btnModifier.getStyleClass().add("btn-stocker");
                btnModifier.setOnAction(event -> {
                    LigneCommande ligne = getTableView().getItems().get(getIndex());
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

        // ---- Colonnes table historique commandes ----
        colNumCommande.setCellValueFactory(new PropertyValueFactory<>("numCommande"));
        colFournisseur.setCellValueFactory(new PropertyValueFactory<>("nomSociete"));
        colFournisseur.setCellFactory(col -> new TableCell<Commande, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colDateCommande.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
                c.getValue().getDateCommande() != null ? c.getValue().getDateCommande().format(FORMAT_DATE) : ""
        ));
        colMontantTotalCol.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colMontantTotalCol.setCellFactory(col -> new TableCell<Commande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colMontantRestantCol.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));
        colMontantRestantCol.setCellFactory(col -> new TableCell<Commande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colStatutPaymentCol.setCellValueFactory(new PropertyValueFactory<>("statutPayment"));
        colStatutPaymentCol.setCellFactory(col -> new TableCell<Commande, String>() {
            @Override
            protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(statut);
                    if ("Payé".equals(statut)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });
        colValidationCol.setCellValueFactory(new PropertyValueFactory<>("validation"));
        colValidationCol.setCellFactory(col -> new TableCell<Commande, Boolean>() {
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

        // ---- Colonnes table détails lignes (historique) ----
        colDetailProduit.setCellValueFactory(new PropertyValueFactory<>("designationProduit"));
        colDetailProduit.setCellFactory(col -> new TableCell<LigneCommande, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colDetailQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colDetailPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colDetailPrixUnitaire.setCellFactory(col -> new TableCell<LigneCommande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colDetailTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrix"));
        colDetailTotal.setCellFactory(col -> new TableCell<LigneCommande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colDetailStatut.setCellValueFactory(new PropertyValueFactory<>("statusCommande"));
        colDetailStatut.setCellFactory(col -> new TableCell<LigneCommande, Boolean>() {
            @Override
            protected void updateItem(Boolean stocke, boolean empty) {
                super.updateItem(stocke, empty);
                if (empty || stocke == null) {
                    setText(null);
                } else {
                    setText(stocke ? "Stocké" : "En attente");
                    setStyle(stocke ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;" : "-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                }
            }
        });

        // ---- Bouton "Stocker" dans la colonne Action ----
        colDetailAction.setCellFactory(col -> new TableCell<LigneCommande, Void>() {
            private final Button btnStocker = new Button("Stocker");

            {
                btnStocker.getStyleClass().add("btn-stocker");
                btnStocker.setOnAction(event -> {
                    LigneCommande ligne = getTableView().getItems().get(getIndex());
                    stockerLigne(ligne);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    LigneCommande ligne = getTableView().getItems().get(getIndex());
                    setGraphic(ligne.isStatusCommande() ? null : btnStocker);
                }
            }
        });

        chargerCommandes();
        genererNouveauNumeroCommande();

        tableCommandes.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                ObservableList<LigneCommande> lignes = FXCollections.observableArrayList(
                        ligneCommandeDAO.getByCommande(selection.getIdCommande())
                );
                tableDetailsLignes.setItems(lignes);
            }
        });
    }

    private void chargerLignePourEdition(LigneCommande ligne) {
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

    private void genererNouveauNumeroCommande() {
        numCommandeActuel = commandeDAO.genererProchainNumCommande();
        lblNumCommande.setText("Commande N° : " + numCommandeActuel);
        mettreAJourMontantTotal();
    }

    private void mettreAJourMontantTotal() {
        double total = lignesTemporaires.stream().mapToDouble(LigneCommande::getTotalPrix).sum();
        

        lblMontantTotal.setText("Montant total de la commande : " + FormatUtilitaire.formaterMontant(total) + " FCFA");
    }

    // ================= NOUVELLE COMMANDE =================
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
            afficherMessage("Quantité doit être des nombres valides.", false);
            return;
        }

        if (quantite <= 0) {
            afficherMessage("La quantité doit être supérieurs à 0.", false);
            return;
        }

        double prixUnitaire = produit.getPrixAchat();

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
            LigneCommande ligne = new LigneCommande();
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
    private void enregistrerCommande() {
        Fournisseur fournisseur = cbFournisseur.getValue();
        String modePayment = cbModePayment.getValue();
        String montantPayeTexte = txtMontantPaye.getText().trim();

        if (fournisseur == null || modePayment == null || montantPayeTexte.isEmpty()) {
            afficherMessage("Veuillez remplir toutes les informations de la commande.", false);
            return;
        }

        if (lignesTemporaires.isEmpty()) {
            afficherMessage("Ajoutez au moins un produit à la commande.", false);
            return;
        }

        double montantPaye;
        try {
            montantPaye = Double.parseDouble(montantPayeTexte);
        } catch (NumberFormatException e) {
            afficherMessage("Le montant donné doit être un nombre valide.", false);
            return;
        }

        double montantTotal = lignesTemporaires.stream().mapToDouble(LigneCommande::getTotalPrix).sum();

        // ===== Calcul automatique du statut et du montant restant =====
        double montantRestant = montantTotal - montantPaye;
        String statutPayment = (montantPaye >= montantTotal) ? "Payé" : "Partiel";

        Commande commande = new Commande();
        commande.setIdFournisseur(fournisseur.getIdFournisseur());
        commande.setIdEmploye(employeConnecte.getId());
        commande.setModePayment(modePayment);
        commande.setMontantTotal(montantTotal);
        commande.setMontantPaye(montantPaye);
        commande.setMontantRestant(montantRestant);
        commande.setStatutPayment(statutPayment);

        Commande resultat = commandeDAO.ajouter(commande, numCommandeActuel);

        if (resultat != null) {
            for (LigneCommande ligne : lignesTemporaires) {
                ligne.setIdCommande(resultat.getIdCommande());
                ligneCommandeDAO.ajouter(ligne);
            }

            afficherMessage("Commande " + resultat.getNumCommande() + " enregistrée avec succès.", true);
            annulerNouvelleCommande();
            chargerCommandes();
            new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "faire commande : " + resultat.getNumCommande());
        } else {
            afficherMessage("Erreur lors de l'enregistrement de la commande.", false);
        }
    }

    @FXML
    private void annulerNouvelleCommande() {
        cbFournisseur.setValue(null);
        cbModePayment.setValue(null);
        txtMontantPaye.clear();
        cbType.setValue(null);
        cbType.getEditor().clear();
        cbProduit.getItems().clear();
        cbProduit.getEditor().clear();
        lblUnite.setText("—");
        lblPrixUnitaire.setText("—");
        txtQuantiteLigne.clear();
        lignesTemporaires.clear();
        genererNouveauNumeroCommande();
    }

    // ================= HISTORIQUE =================
    private void chargerCommandes() {
        tableCommandes.setItems(FXCollections.observableArrayList(commandeDAO.getAll()));
    }

    private void stockerLigne(LigneCommande ligne) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de réception");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Confirmer la réception de " + ligne.getQuantite()
                + " x " + ligne.getDesignationProduit() + " ? La quantité en stock sera mise à jour.");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean stockMisAJour = produitDAO.incrementerStock(ligne.getIdProduit(), ligne.getQuantite());
                if (stockMisAJour) {
                    ligneCommandeDAO.marquerCommeStocke(ligne.getIdLc());

                    Commande selection = tableCommandes.getSelectionModel().getSelectedItem();
                    if (selection != null) {
                        // Valide la commande dès qu'au moins une ligne est stockée
                        if (!selection.isValidation()) {
                            boolean valideOk = commandeDAO.valider(selection.getIdCommande());
                            if (valideOk) {
                                selection.setValidation(true);
                                tableCommandes.refresh();
                            }
                        }

                        tableDetailsLignes.setItems(
                                FXCollections.observableArrayList(ligneCommandeDAO.getByCommande(selection.getIdCommande()))
                        );
                    }

                    cbProduit.setItems(FXCollections.observableArrayList(
                            cbType.getValue() != null ? produitDAO.getByType(cbType.getValue().getIdType()) : produitDAO.getAll()
                    ));
                }
            }
        });
    }

    private void afficherMessage(String texte, boolean succes) {
        lblMessage.setText(texte);
        lblMessage.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
