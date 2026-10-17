/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitialisationBD {

    private static final String URL_SERVEUR = "jdbc:mysql://localhost:3306/";
    private static final String NOM_BASE = "gestion_quincaillerie";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    /**
     * Vérifie si la base existe. Si non, la crée et importe le schéma automatiquement.
     * Retourne true si tout est prêt (base existante ou nouvellement créée avec succès).
     */
    public static boolean initialiserSiNecessaire() {
        if (baseExiste()) {
            return true;
        }

        System.out.println("Base de données introuvable. Création en cours...");
        return creerBaseEtImporterSchema();
    }

    private static boolean baseExiste() {
        try (Connection conn = DriverManager.getConnection(
                URL_SERVEUR + NOM_BASE, UTILISATEUR, MOT_DE_PASSE)) {
            return true;
        } catch (SQLException e) {
            return false; // la base n'existe probablement pas encore
        }
    }

    private static boolean creerBaseEtImporterSchema() {
        try (Connection conn = DriverManager.getConnection(URL_SERVEUR, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + NOM_BASE);
            stmt.execute("USE " + NOM_BASE);

            // Lecture du fichier schema.sql embarqué dans l'application
            InputStream is = InitialisationBD.class.getResourceAsStream("/com/bad/gestionquincaillerie/schema.sql");
            if (is == null) {
                System.err.println("Fichier schema.sql introuvable dans les ressources.");
                return false;
            }

            StringBuilder contenuSql = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    if (ligne.trim().startsWith("--") || ligne.trim().isEmpty()) {
                        continue; // ignore les commentaires et lignes vides
                    }
                    contenuSql.append(ligne).append("\n");
                }
            }

            // Exécute chaque instruction SQL séparément (séparées par ;)
            try (Connection connBase = DriverManager.getConnection(URL_SERVEUR + NOM_BASE, UTILISATEUR, MOT_DE_PASSE);
                 Statement stmtBase = connBase.createStatement()) {

                String[] instructions = contenuSql.toString().split(";");
                for (String instruction : instructions) {
                    String propre = instruction.trim();
                    if (!propre.isEmpty()) {
                        stmtBase.executeUpdate(propre);
                    }
                }
            }

            System.out.println("Base de données créée et initialisée avec succès.");
            return true;

        } catch (IOException | SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base : " + e.getMessage());
            return false;
        }
    }
}
