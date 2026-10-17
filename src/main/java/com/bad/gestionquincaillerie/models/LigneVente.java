/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */


public class LigneVente {

    private int idLv;
    private int idVente;
    private int idProduit;
    private String designationProduit;
    private int quantite;
    private double prixUnitaire;
    private double totalPrix;
    private boolean statut;

    public LigneVente() {
    }

    public int getIdLv() { return idLv; }
    public void setIdLv(int idLv) { this.idLv = idLv; }

    public int getIdVente() { return idVente; }
    public void setIdVente(int idVente) { this.idVente = idVente; }

    public int getIdProduit() { return idProduit; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    public String getDesignationProduit() { return designationProduit; }
    public void setDesignationProduit(String designationProduit) { this.designationProduit = designationProduit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public double getTotalPrix() { return totalPrix; }
    public void setTotalPrix(double totalPrix) { this.totalPrix = totalPrix; }

    public boolean isStatut() { return statut; }
    public void setStatut(boolean statut) { this.statut = statut; }
}
