/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

public class Client {

    private int idClient;
    private Integer idCredit; // Integer (pas int) car peut être null
    private String typeClient;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;

    public Client() {
    }

    public Client(int idClient, Integer idCredit, String typeClient, String nom,
                  String email, String telephone, String adresse) {
        this.idClient = idClient;
        this.idCredit = idCredit;
        this.typeClient = typeClient;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    public Integer getIdCredit() { return idCredit; }
    public void setIdCredit(Integer idCredit) { this.idCredit = idCredit; }

    public String getTypeClient() { return typeClient; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    /**
     * Utile pour l'affichage : indique si ce client a une créance en cours.
     */
    public boolean aUneCreance() {
        return idCredit != null;
    }

    @Override
    public String toString() {
        return  typeClient + nom;
    }
}
