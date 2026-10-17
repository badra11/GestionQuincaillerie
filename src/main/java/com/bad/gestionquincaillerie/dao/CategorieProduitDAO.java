/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.CategorieProduit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategorieProduitDAO {

    public boolean ajouter(CategorieProduit categorie) {
        String sql = "INSERT INTO categories (nom_cat, description) VALUES (?, ?)";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categorie.getNomCat());
            stmt.setString(2, categorie.getDescription());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout catégorie : " + e.getMessage());
            return false;
        }
    }
    
    public boolean modifier(CategorieProduit categorie) {
        String sql = "UPDATE categories SET nom_cat = ?, description = ? WHERE id_cat = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categorie.getNomCat());
            stmt.setString(2, categorie.getDescription());
            stmt.setInt(3, categorie.getIdCat());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification catégorie : " + e.getMessage());
            return false;
        }
    }
    
    public boolean supprimer(int idCat) {
        String sql = "DELETE FROM categories WHERE id_cat = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCat);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression catégorie : " + e.getMessage());
            return false;
        }
    }

    public List<CategorieProduit> getAll() {
        List<CategorieProduit> liste = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY id_cat DESC";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategorieProduit c = new CategorieProduit(
                    rs.getInt("id_cat"),
                    rs.getString("nom_cat"),
                    rs.getString("description")
                );
                liste.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération catégories : " + e.getMessage());
        }
        return liste;
    }
}

