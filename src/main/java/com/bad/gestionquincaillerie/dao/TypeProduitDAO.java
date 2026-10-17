/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */

import com.bad.gestionquincaillerie.models.TypeProduit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeProduitDAO {
    public boolean ajouter(TypeProduit type) {
        String sql = "INSERT INTO types (id_cat, nom_type, description) VALUES (?, ?, ?)";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, type.getIdCat());
            stmt.setString(2, type.getNomType());
            stmt.setString(3, type.getDescription());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout type : " + e.getMessage());
            return false;
        }
    }

    public boolean modifier(TypeProduit type) {
        String sql = "UPDATE types SET id_cat = ?, nom_type = ?, description = ? WHERE id_type = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, type.getIdCat());
            stmt.setString(2, type.getNomType());
            stmt.setString(3, type.getDescription());
            stmt.setInt(4, type.getIdType());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification type : " + e.getMessage());
            return false;
        }
    }
    
    public boolean supprimer(int idType) {
        String sql = "DELETE FROM types WHERE id_type = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idType);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression type : " + e.getMessage());
            return false;
        }
    }
    
    // Jointure avec categorie pour afficher le nom de la catégorie dans la table
    public List<TypeProduit> getAll() {
        List<TypeProduit> liste = new ArrayList<>();
        String sql = "SELECT t.id_type, t.id_cat, c.nom_cat, t.nom_type, t.description "
                   + "FROM types t "
                   + "JOIN categories c ON t.id_cat = c.id_cat "
                   + "ORDER BY t.id_type DESC";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TypeProduit t = new TypeProduit(
                    rs.getInt("id_type"),
                    rs.getInt("id_cat"),
                    rs.getString("nom_cat"),
                    rs.getString("nom_type"),
                    rs.getString("description")
                );
                liste.add(t);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération types : " + e.getMessage());
        }
        return liste;
    }
    
    public List<TypeProduit> getByCategorie(int idCat) {
    List<TypeProduit> liste = new ArrayList<>();
    String sql = "SELECT t.id_type, t.id_cat, c.nom_cat, t.nom_type, t.description "
               + "FROM types t "
               + "JOIN categories c ON t.id_cat = c.id_cat "
               + "WHERE t.id_cat = ? "
               + "ORDER BY t.nom_type";

    try (Connection conn = ConnexionBD.getConnexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idCat);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                TypeProduit t = new TypeProduit(
                    rs.getInt("id_type"),
                    rs.getInt("id_cat"),
                    rs.getString("nom_cat"),
                    rs.getString("nom_type"),
                    rs.getString("description")
                );
                liste.add(t);
            }
        }

        } catch (SQLException e) {
            System.err.println("Erreur récupération types par catégorie : " + e.getMessage());
        }
        return liste;
    }
    
}

