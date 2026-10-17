/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

import java.time.LocalDateTime;

public class Vente {

    private int idVente;
    private String numVente;
    private int idClient;
    private String nomClient; // affichage (jointure)
    private int idEmploye;
    private String modePayment;
    private double montantTotal;
    private double montantRecu;
    private double montantRestant;
    private LocalDateTime dateVente;
    private boolean validation;
    private String adresseLivraison;
    private String vehiculeLivraison;
    private String statutPayment;

    public Vente() {
    }

    public int getIdVente() { return idVente; }
    public void setIdVente(int idVente) { this.idVente = idVente; }

    public String getNumVente() { return numVente; }
    public void setNumVente(String numVente) { this.numVente = numVente; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public int getIdEmploye() { return idEmploye; }
    public void setIdEmploye(int idEmploye) { this.idEmploye = idEmploye; }

    public String getModePayment() { return modePayment; }
    public void setModePayment(String modePayment) { this.modePayment = modePayment; }

    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }

    public double getMontantRecu() { return montantRecu; }
    public void setMontantRecu(double montantRecu) { this.montantRecu = montantRecu; }

    public double getMontantRestant() { return montantRestant; }
    public void setMontantRestant(double montantRestant) { this.montantRestant = montantRestant; }

    public LocalDateTime getDateVente() { return dateVente; }
    public void setDateVente(LocalDateTime dateVente) { this.dateVente = dateVente; }

    public boolean isValidation() { return validation; }
    public void setValidation(boolean validation) { this.validation = validation; }

    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }

    public String getVehiculeLivraison() { return vehiculeLivraison; }
    public void setVehiculeLivraison(String vehiculeLivraison) { this.vehiculeLivraison = vehiculeLivraison; }

    public String getStatutPayment() { return statutPayment; }
    public void setStatutPayment(String statutPayment) { this.statutPayment = statutPayment; }
}