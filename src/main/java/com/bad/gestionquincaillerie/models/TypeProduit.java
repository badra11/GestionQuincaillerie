/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */
public class TypeProduit {
    private int idType;
    private int idCat;
    private String nomCat; // pour affichage dans la table (jointure)
    private String nomType;
    private String description;

    public TypeProduit() {
    }

    public TypeProduit(int idCat, String nomCat, String nomType, String description) {
        this.idCat = idCat;
        this.nomCat = nomCat;
        this.nomType = nomType;
        this.description = description;
    }
    
    public TypeProduit(int idType, int idCat, String nomCat, String nomType, String description) {
        this.idType = idType;
        this.idCat = idCat;
        this.nomCat = nomCat;
        this.nomType = nomType;
        this.description = description;
    }

    public int getIdType() { return idType; }
    public void setIdType(int idType) { this.idType = idType; }

    public int getIdCat() { return idCat; }
    public void setIdCat(int idCat) { this.idCat = idCat; }

    public String getNomCat() { return nomCat; }
    public void setNomCat(String nomCat) { this.nomCat = nomCat; }

    public String getNomType() { return nomType; }
    public void setNomType(String nomType) { this.nomType = nomType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return nomType;
    }
}

