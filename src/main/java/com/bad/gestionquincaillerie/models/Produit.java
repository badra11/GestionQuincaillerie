/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

public class Produit {

    private int idProduit;
    private int idType;
    private String nomType;        // pour affichage (jointure)
    private int idEmplacement;
    private String designationEmplacement; // pour affichage (jointure)
    private String reference;
    private String designation;
    private String codeBarre;
    private double prixVente;
    private double prixAchat;
    private int quantite;
    private boolean alerteSeuil;
    private String unite;
    private String marque;

    public Produit() {
    }

    // Constructeur utilisé à la création (avant que reference/code_barre soient générés)
    public Produit(int idType, int idEmplacement, String designation, double prixVente,
                   double prixAchat, String unite, String marque) {
        this.idType = idType;
        this.idEmplacement = idEmplacement;
        this.designation = designation;
        this.prixVente = prixVente;
        this.prixAchat = prixAchat;
        this.unite = unite;
        this.marque = marque;
        this.quantite = 0;
        this.alerteSeuil = true;
    }

    public int getIdProduit() { return idProduit; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    public int getIdType() { return idType; }
    public void setIdType(int idType) { this.idType = idType; }

    public String getNomType() { return nomType; }
    public void setNomType(String nomType) { this.nomType = nomType; }

    public int getIdEmplacement() { return idEmplacement; }
    public void setIdEmplacement(int idEmplacement) { this.idEmplacement = idEmplacement; }

    public String getDesignationEmplacement() { return designationEmplacement; }
    public void setDesignationEmplacement(String designationEmplacement) { this.designationEmplacement = designationEmplacement; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getCodeBarre() { return codeBarre; }
    public void setCodeBarre(String codeBarre) { this.codeBarre = codeBarre; }

    public double getPrixVente() { return prixVente; }
    public void setPrixVente(double prixVente) { this.prixVente = prixVente; }

    public double getPrixAchat() { return prixAchat; }
    public void setPrixAchat(double prixAchat) { this.prixAchat = prixAchat; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public boolean isAlerteSeuil() { return alerteSeuil; }
    public void setAlerteSeuil(boolean alerteSeuil) { this.alerteSeuil = alerteSeuil; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    @Override
    public String toString() {
        return "Produit{" + "idProduit=" + idProduit + ", idType=" + idType + ", nomType=" + nomType + ", idEmplacement=" + idEmplacement + ", designationEmplacement=" + designationEmplacement + ", reference=" + reference + ", designation=" + designation + ", codeBarre=" + codeBarre + ", prixVente=" + prixVente + ", prixAchat=" + prixAchat + ", quantite=" + quantite + ", alerteSeuil=" + alerteSeuil + ", unite=" + unite + ", marque=" + marque + '}';
    }
    
}
