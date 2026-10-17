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

public class Commande {

    private int idCommande;
    private String numCommande;
    private int idFournisseur;
    private String nomSociete; // affichage (jointure)
    private int idEmploye;
    private String modePayment;
    private double montantTotal;
    private double montantPaye;
    private double montantRestant;
    private LocalDateTime dateCommande;
    private boolean validation;
    private String statutPayment;

    public Commande() {
    }

    public int getIdCommande() { return idCommande; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }

    public String getNumCommande() { return numCommande; }
    public void setNumCommande(String numCommande) { this.numCommande = numCommande; }

    public int getIdFournisseur() { return idFournisseur; }
    public void setIdFournisseur(int idFournisseur) { this.idFournisseur = idFournisseur; }

    public String getNomSociete() { return nomSociete; }
    public void setNomSociete(String nomSociete) { this.nomSociete = nomSociete; }

    public int getIdEmploye() { return idEmploye; }
    public void setIdEmploye(int idEmploye) { this.idEmploye = idEmploye; }

    public String getModePayment() { return modePayment; }
    public void setModePayment(String modePayment) { this.modePayment = modePayment; }

    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }

    public double getMontantPaye() { return montantPaye; }
    public void setMontantPaye(double montantPaye) { this.montantPaye = montantPaye; }

    public double getMontantRestant() { return montantRestant; }
    public void setMontantRestant(double montantRestant) { this.montantRestant = montantRestant; }

    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }

    public boolean isValidation() { return validation; }
    public void setValidation(boolean validation) { this.validation = validation; }

    public String getStatutPayment() { return statutPayment; }
    public void setStatutPayment(String statutPayment) { this.statutPayment = statutPayment; }

    @Override
    public String toString() {
        return "Commande{" + "numCommande=" + numCommande + ", nomSociete=" + nomSociete + ", modePayment=" + modePayment + ", montantTotal=" + montantTotal + ", validation=" + validation + ", statutPayment=" + statutPayment + '}';
    }
    
    
}
