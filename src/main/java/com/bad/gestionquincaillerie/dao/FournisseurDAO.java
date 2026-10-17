/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.Fournisseur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAO {

    public boolean ajouter(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseurs (nom_societe, email, telephone, adresse) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fournisseur.getNomSociete());
            stmt.setString(2, fournisseur.getEmail());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getAdresse());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout fournisseur : " + e.getMessage());
            return false;
        }
    }

    public boolean modifier(Fournisseur fournisseur) {
        String sql = "UPDATE fournisseurs SET nom_societe = ?, email = ?, telephone = ?, adresse = ? WHERE id_fournisseur = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fournisseur.getNomSociete());
            stmt.setString(2, fournisseur.getEmail());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getAdresse());
            stmt.setInt(5, fournisseur.getIdFournisseur());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification fournisseur : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int idFournisseur) {
        String sql = "DELETE FROM fournisseurs WHERE id_fournisseur = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFournisseur);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression fournisseur : " + e.getMessage());
            return false;
        }
    }

    public List<Fournisseur> getAll() {
        List<Fournisseur> liste = new ArrayList<>();
        String sql = "SELECT * FROM fournisseurs ORDER BY id_fournisseur DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fournisseur f = new Fournisseur(
                        rs.getInt("id_fournisseur"),
                        rs.getString("nom_societe"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getString("adresse")
                );
                liste.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération fournisseurs : " + e.getMessage());
        }
        return liste;
    }

    public int getNombreFournisseurs() {
        String sql = "SELECT COUNT(*) AS total FROM fournisseurs";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage fournisseurs : " + e.getMessage());
        }
        return 0;
    }
}
