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
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import java.io.File;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.stage.FileChooser;

public class StatistiquesController implements Initializable {

    // ---- Ventes ----
    @FXML
    private DatePicker dateDebutVentes;
    @FXML
    private DatePicker dateFinVentes;
    @FXML
    private BarChart<String, Number> chartVentes;
    @FXML
    private TableView<Vente> tableVentesRapport;
    @FXML
    private TableColumn<Vente, String> colVenteNum;
    @FXML
    private TableColumn<Vente, String> colVenteClient;
    @FXML
    private TableColumn<Vente, String> colVenteDate;
    @FXML
    private TableColumn<Vente, Double> colVenteMontant;
    @FXML
    private TableColumn<Vente, String> colVenteStatut;

    // ---- Stock ----
    @FXML
    private TableView<Produit> tableStock;
    @FXML
    private TableColumn<Produit, String> colStockReference;
    @FXML
    private TableColumn<Produit, String> colStockDesignation;
    @FXML
    private TableColumn<Produit, String> colStockType;
    @FXML
    private TableColumn<Produit, Integer> colStockQuantite;
    @FXML
    private TableColumn<Produit, String> colStockUnite;

    // ---- Créances ----
    @FXML
    private Label lblCreanceTotale;
    @FXML
    private TableView<CreditVente> tableCreances;
    @FXML
    private TableColumn<CreditVente, String> colCreanceNum;
    @FXML
    private TableColumn<CreditVente, String> colCreanceClient;
    @FXML
    private TableColumn<CreditVente, String> colCreanceVente;
    @FXML
    private TableColumn<CreditVente, Double> colCreanceMontant;
    @FXML
    private TableColumn<CreditVente, Double> colCreanceReste;

    // ---- Commandes ----
    @FXML
    private DatePicker dateDebutCommandes;
    @FXML
    private DatePicker dateFinCommandes;
    @FXML
    private BarChart<String, Number> chartCommandes;
    @FXML
    private TableView<Commande> tableCommandesRapport;
    @FXML
    private TableColumn<Commande, String> colCmdNum;
    @FXML
    private TableColumn<Commande, String> colCmdFournisseur;
    @FXML
    private TableColumn<Commande, String> colCmdDate;
    @FXML
    private TableColumn<Commande, Double> colCmdMontant;
    @FXML
    private TableColumn<Commande, String> colCmdStatut;

    private final VenteDAO venteDAO = new VenteDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();
    private final CreditVenteDAO creditVenteDAO = new CreditVenteDAO();
    private final CommandeDAO commandeDAO = new CommandeDAO();

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMAT_JOUR = DateTimeFormatter.ofPattern("dd/MM");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ----- Colonnes Ventes -----
        colVenteNum.setCellValueFactory(new PropertyValueFactory<>("numVente"));
        colVenteClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colVenteDate.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
                c.getValue().getDateVente() != null ? c.getValue().getDateVente().format(FORMAT_DATE) : ""
        ));
        colVenteMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colVenteMontant.setCellFactory(col -> creerCelluleMontant());
        colVenteStatut.setCellValueFactory(new PropertyValueFactory<>("statutPayment"));

        // ----- Colonnes Stock -----
        colStockReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colStockDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colStockType.setCellValueFactory(new PropertyValueFactory<>("nomType"));
        colStockQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colStockUnite.setCellValueFactory(new PropertyValueFactory<>("unite"));

        // ----- Colonnes Créances -----
        colCreanceNum.setCellValueFactory(new PropertyValueFactory<>("numCredit"));
        colCreanceClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colCreanceVente.setCellValueFactory(new PropertyValueFactory<>("numVente"));
        colCreanceMontant.setCellValueFactory(new PropertyValueFactory<>("montantCredit"));
        colCreanceMontant.setCellFactory(col -> creerCelluleMontant());
        colCreanceReste.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
                c.getValue().getMontantCredit() - c.getValue().getMontantPaye()
        ));       // 
        colCreanceReste.setCellFactory(col -> creerCelluleMontant());

        // ----- Colonnes Commandes -----
        colCmdNum.setCellValueFactory(new PropertyValueFactory<>("numCommande"));
        colCmdFournisseur.setCellValueFactory(new PropertyValueFactory<>("nomSociete"));
        colCmdDate.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
                c.getValue().getDateCommande() != null ? c.getValue().getDateCommande().format(FORMAT_DATE) : ""
        ));
        colCmdMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colCmdMontant.setCellFactory(col -> creerCelluleMontant());
        colCmdStatut.setCellValueFactory(new PropertyValueFactory<>("statutPayment"));

        // Valeurs par défaut : 7 derniers jours
        dateDebutVentes.setValue(LocalDate.now().minusDays(7));
        dateFinVentes.setValue(LocalDate.now());
        dateDebutCommandes.setValue(LocalDate.now().minusDays(7));
        dateFinCommandes.setValue(LocalDate.now());

        chargerStock();
        chargerCreances();
        genererRapportVentes();
        genererRapportCommandes();
    }

    private static <S> TableCell<S, Double> creerCelluleMontant() {
        return new TableCell<S, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        };
    }

    // ================= VENTES =================
    @FXML
    private void genererRapportVentes() {
        LocalDate debut = dateDebutVentes.getValue();
        LocalDate fin = dateFinVentes.getValue();
        if (debut == null || fin == null) {
            return;
        }

        tableVentesRapport.setItems(FXCollections.observableArrayList(venteDAO.getVentesParPeriode(debut, fin)));

        Map<LocalDate, Double> parJour = venteDAO.getTotalVentesParJour(debut, fin);
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Ventes");
        for (Map.Entry<LocalDate, Double> entree : parJour.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entree.getKey().format(FORMAT_JOUR), entree.getValue()));
        }
        chartVentes.getData().clear();
        chartVentes.getData().add(serie);
    }

    // ================= STOCK =================
    private void chargerStock() {
        tableStock.setItems(FXCollections.observableArrayList(produitDAO.getProduitsEnAlerte()));
    }

    // ================= CRÉANCES =================
    private void chargerCreances() {
        double total = creditVenteDAO.getMontantCreanceGlobale();
        lblCreanceTotale.setText("Créance totale en cours : " + FormatUtilitaire.formaterMontant(total) + " FCFA");
        lblCreanceTotale.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: 800; -fx-font-size: 16px;");

        tableCreances.setItems(FXCollections.observableArrayList(creditVenteDAO.getCreditsActifs()));
    }

    // ================= COMMANDES =================
    @FXML
    private void genererRapportCommandes() {
        LocalDate debut = dateDebutCommandes.getValue();
        LocalDate fin = dateFinCommandes.getValue();
        if (debut == null || fin == null) {
            return;
        }

        tableCommandesRapport.setItems(FXCollections.observableArrayList(commandeDAO.getCommandesParPeriode(debut, fin)));

        Map<LocalDate, Double> parJour = commandeDAO.getTotalCommandesParJour(debut, fin);
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Commandes");
        for (Map.Entry<LocalDate, Double> entree : parJour.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entree.getKey().format(FORMAT_JOUR), entree.getValue()));
        }
        chartCommandes.getData().clear();
        chartCommandes.getData().add(serie);
    }

    @FXML
    private void exporterVentesExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("rapport_ventes.xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        File fichier = fileChooser.showSaveDialog(tableVentesRapport.getScene().getWindow());
        if (fichier == null) {
            return;
        }

        String[] entetes = {"N° Vente", "Client", "Date", "Montant", "Statut"};
        List<Object[]> lignes = new ArrayList<>();
        for (Vente v : tableVentesRapport.getItems()) {
            lignes.add(new Object[]{v.getNumVente(), v.getNomClient(),
                v.getDateVente().format(FORMAT_DATE), v.getMontantTotal(), v.getStatutPayment()});
        }

        boolean succes = ExcelExportUtil.exporter(fichier.getAbsolutePath(), entetes, lignes);
        // afficher une Alert de confirmation ici
    }
}
