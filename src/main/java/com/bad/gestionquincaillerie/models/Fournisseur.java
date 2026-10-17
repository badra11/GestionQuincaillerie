/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

public class Fournisseur {

    private int idFournisseur;
    private String nomSociete;
    private String email;
    private String telephone;
    private String adresse;

    public Fournisseur() {
    }

    public Fournisseur(int idFournisseur, String nomSociete, String email, String telephone, String adresse) {
        this.idFournisseur = idFournisseur;
        this.nomSociete = nomSociete;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public int getIdFournisseur() { return idFournisseur; }
    public void setIdFournisseur(int idFournisseur) { this.idFournisseur = idFournisseur; }

    public String getNomSociete() { return nomSociete; }
    public void setNomSociete(String nomSociete) { this.nomSociete = nomSociete; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public String toString() {
        return nomSociete;
    }
}
