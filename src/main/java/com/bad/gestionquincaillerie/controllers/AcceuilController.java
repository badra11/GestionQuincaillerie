/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

import com.bad.gestionquincaillerie.dao.ClientDAO;
import com.bad.gestionquincaillerie.dao.CommandeDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.dao.CreditVenteDAO;
import com.bad.gestionquincaillerie.dao.EmployeDAO;
import com.bad.gestionquincaillerie.dao.FournisseurDAO;
import com.bad.gestionquincaillerie.dao.ProduitDAO;
import com.bad.gestionquincaillerie.dao.VenteDAO;
import com.bad.gestionquincaillerie.models.Employe;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class AcceuilController implements Initializable {

    @FXML
    private Label lblBienvenue;
    @FXML
    private Label lblTotalProduits;
    @FXML
    private Label lblAlertesStock;
    @FXML
    private Label lblVentesJour;
    @FXML
    private Label lblCreances;

    @FXML
    private Label lblValeurAchat;
    @FXML
    private Label lblValeurVente;
    @FXML
    private Label lblBenefice;

    @FXML
    private Label lblTotalEmployes;
    @FXML
    private Label lblTotalClients;
    @FXML
    private Label lblTotalFournisseurs;
    @FXML
    private Label lblCommandesAttente;
    @FXML
    private BarChart<String, Number> chartVentes7Jours;
    @FXML
    private PieChart chartStockParType;

    private final ProduitDAO produitDAO = new ProduitDAO();
    private final VenteDAO venteDAO = new VenteDAO();
    private final CreditVenteDAO creditVenteDAO = new CreditVenteDAO();
    private final EmployeDAO employeDAO = new EmployeDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private final CommandeDAO commandeDAO = new CommandeDAO();
    double valeurAchat = produitDAO.getValeurTotaleAchatStock();
    double valeurVente = produitDAO.getValeurTotaleVenteStock();
    double benefice = valeurVente - valeurAchat;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Les données sont chargées quand initEmploye() est appelé
    }

    public void initEmploye(Employe employe) {
        lblBienvenue.setText("TABLEAU DE BORD");
        chargerStatistiques();
    }

    private void chargerStatistiques() {
        lblTotalProduits.setText(String.valueOf(produitDAO.getNombreTotalProduits()));
        lblAlertesStock.setText(String.valueOf(produitDAO.getNombreProduitsEnAlerte()));

        int nbVentesJour = venteDAO.getNombreVentesDuJour();
        double montantVentesJour = venteDAO.getMontantVentesDuJour();
        lblVentesJour.setText(nbVentesJour + " (" + FormatUtilitaire.formaterMontant(montantVentesJour) + " FCFA)");

        double creanceGlobale = creditVenteDAO.getMontantCreanceGlobale();
        lblCreances.setText(FormatUtilitaire.formaterMontant(creanceGlobale) + " FCFA");

        lblTotalEmployes.setText(String.valueOf(employeDAO.getNombreEmployes()));
        lblTotalClients.setText(String.valueOf(clientDAO.getNombreClients()));
        lblTotalFournisseurs.setText(String.valueOf(fournisseurDAO.getNombreFournisseurs()));
        lblCommandesAttente.setText(String.valueOf(commandeDAO.getNombreCommandesEnAttente()));

        lblValeurAchat.setText(FormatUtilitaire.formaterMontant(valeurAchat) + " FCFA");
        lblValeurVente.setText(FormatUtilitaire.formaterMontant(valeurVente) + " FCFA à engendrer");
        lblBenefice.setText("Bénéfice potentiel : " + FormatUtilitaire.formaterMontant(benefice) + " FCFA");

        chargerGraphiqueVentes();
        chargerGraphiqueStock();
    }

    private void chargerGraphiqueVentes() {
        LocalDate finPeriode = LocalDate.now();
        LocalDate debutPeriode = finPeriode.minusDays(6); // 7 jours au total, aujourd'hui inclus

        Map<LocalDate, Double> parJour = venteDAO.getTotalVentesParJour(debutPeriode, finPeriode);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Ventes");

        DateTimeFormatter formatJour = DateTimeFormatter.ofPattern("dd/MM");
        for (LocalDate jour = debutPeriode; !jour.isAfter(finPeriode); jour = jour.plusDays(1)) {
            double montant = parJour.getOrDefault(jour, 0.0);
            serie.getData().add(new XYChart.Data<>(jour.format(formatJour), montant));
        }

        chartVentes7Jours.getData().clear();
        chartVentes7Jours.getData().add(serie);
    }

    private void chargerGraphiqueStock() {
        Map<String, Integer> repartition = produitDAO.getRepartitionStockParType();

        ObservableList<PieChart.Data> donnees = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entree : repartition.entrySet()) {
            donnees.add(new PieChart.Data(entree.getKey() + " (" + entree.getValue() + ")", entree.getValue()));
        }

        chartStockParType.setData(donnees);
    }

}
