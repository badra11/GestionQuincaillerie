/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

public class CreditVente {

    private int idCredit;
    private String numCredit;
    private int idClient;
    private String nomClient;
    private int idVente;
    private String numVente;
    private double montantCredit;
    private double montantPaye;
    private boolean statut;

    public CreditVente() {
    }

    public CreditVente(String numCredit, int idClient, String nomClient, int idVente, String numVente, double montantCredit, double montantPaye, boolean statut) {
        this.numCredit = numCredit;
        this.idClient = idClient;
        this.nomClient = nomClient;
        this.idVente = idVente;
        this.numVente = numVente;
        this.montantCredit = montantCredit;
        this.montantPaye = montantPaye;
        this.statut = statut;
    }
    
    

    public int getIdCredit() { return idCredit; }
    public void setIdCredit(int idCredit) { this.idCredit = idCredit; }

    public String getNumCredit() { return numCredit; }
    public void setNumCredit(String numCredit) { this.numCredit = numCredit; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    public int getIdVente() { return idVente; }
    public void setIdVente(int idVente) { this.idVente = idVente; }

    public double getMontantCredit() { return montantCredit; }
    public void setMontantCredit(double montantCredit) { this.montantCredit = montantCredit; }

    public double getMontantPaye() { return montantPaye; }
    public void setMontantPaye(double montantPaye) { this.montantPaye = montantPaye; }

    public boolean isStatut() { return statut; }
    public void setStatut(boolean statut) { this.statut = statut; }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nom) {
        this.nomClient = nom;
    }

    public String getNumVente() {
        return numVente;
    }

    public void setNumVente(String numVente) {
        this.numVente = numVente;
    }
    
    

    @Override
    public String toString() {
        return "CreditVente{" + "idCredit=" + idCredit + ", numCredit=" + numCredit + ", idClient=" + idClient + ", nom=" + nomClient + ", idVente=" + idVente + ", numVente=" + numVente + ", montantCredit=" + montantCredit + ", montantPaye=" + montantPaye + ", statut=" + statut + '}';
    }
    
    
}
