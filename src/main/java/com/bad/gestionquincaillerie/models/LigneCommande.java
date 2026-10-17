/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

public class LigneCommande {

    private int idLc;
    private int idCommande;
    private int idProduit;
    private String designationProduit; // affichage (jointure)
    private int quantite;
    private double prixUnitaire;
    private double totalPrix;
    private boolean statusCommande; // false = pas encore stocké, true = déjà stocké

    public LigneCommande() {
    }

    public LigneCommande(int idCommande, int idProduit, String designationProduit, int quantite, double prixUnitaire, double totalPrix, boolean statusCommande) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.designationProduit = designationProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.totalPrix = totalPrix;
        this.statusCommande = statusCommande;
    }

    
    
    public int getIdLc() { return idLc; }
    public void setIdLc(int idLc) { this.idLc = idLc; }

    public int getIdCommande() { return idCommande; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }

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

    public boolean isStatusCommande() { return statusCommande; }
    public void setStatusCommande(boolean statusCommande) { this.statusCommande = statusCommande; }

    @Override
    public String toString() {
        return "LigneCommande{" + "idCommande=" + idCommande + ", designationProduit=" + designationProduit + ", quantite=" + quantite + ", prixUnitaire=" + prixUnitaire + ", totalPrix=" + totalPrix + ", statusCommande=" + statusCommande + '}';
    }
    
    
}
