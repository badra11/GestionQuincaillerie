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

public class ReçusVente {

    private int idRecus;
    private int idVente;
    private String numVente;
    private String nomClient;
    private String numRecus;
    private double montant;
    private double montantRecuVente;
    private double montantRestantVente;
    private LocalDateTime dateRecus;

    public ReçusVente() {
    }

   

    public String getNumVente() {
        return numVente;
    }

    public void setNumVente(String numVente) {
        this.numVente = numVente;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public int getIdRecus() {
        return idRecus;
    }

    public void setIdRecus(int idRecus) {
        this.idRecus = idRecus;
    }

    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public String getNumRecus() {
        return numRecus;
    }

    public void setNumRecus(String numRecus) {
        this.numRecus = numRecus;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }


    public double getMontantRecuVente() {
        return montantRecuVente;
    }

    public void setMontantRecuVente(double montantRecuVente) {
        this.montantRecuVente = montantRecuVente;
    }

    public double getMontantRestantVente() {
        return montantRestantVente;
    }

    public void setMontantRestantVente(double montantRestantVente) {
        this.montantRestantVente = montantRestantVente;
    } 
    
    public LocalDateTime getDateRecus() {
        return dateRecus;
    }

    public void setDateRecus(LocalDateTime dateRecus) {
        this.dateRecus = dateRecus;
    }

    @Override
    public String toString() {
        return "Re\u00e7usVente{" + "idRecus=" + idRecus + ", idVente=" + idVente + ", numRecus=" + numRecus + ", Montant" + montant + ", dateRecus=" + dateRecus + '}';
    }

}
