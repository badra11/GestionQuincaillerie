/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */
public class CategorieProduit {
    private int idCat;
    private String nomCat;
    private String description;

    public CategorieProduit() {
    }
    
    public CategorieProduit(String nomCat, String description) {
        this.nomCat = nomCat;
        this.description = description;
    }
    
    public CategorieProduit(int idCat, String nomCat, String description) {
        this.idCat = idCat;
        this.nomCat = nomCat;
        this.description = description;
    }

    public int getIdCat() { 
        return idCat; 
    }
    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public String getNomCat() {
        return nomCat; 
    }
    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

    public String getDescription() { 
        return description;
    }
    public void setDescription(String description) { 
        this.description = description; 
    }

    @Override
    public String toString() {
        return nomCat; // utile pour l'affichage dans un ComboBox
    }
}

