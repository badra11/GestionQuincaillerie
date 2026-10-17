/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

import java.time.LocalDate;


/**
 *
 * @author Badra Aliou Dembele
 */
public class Employe {

    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String telephone;
    private String role;
    private LocalDate  date_embauche;
    private double salaire;
    private String username;
    private String password;

    public Employe() {
    }

    public Employe(int id, String matricule, String nom, String prenom, String telephone,
            String role, LocalDate  date_embauche, double salaire, String username, String password) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.role = role;
        this.date_embauche = date_embauche;
        this.salaire = salaire;
        this.username = username;
        this.password = password;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate  getDateEmbauche() {
        return date_embauche;
    }

    public void setDateEmbauche(LocalDate  date) {
        this.date_embauche = date;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Méthode utile : vérifier si cet employé est un utilisateur autorisé de l'app
    public boolean estUtilisateurApp() {
        return "admin".equalsIgnoreCase(role) || "user".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + role + ")";
    }
}
