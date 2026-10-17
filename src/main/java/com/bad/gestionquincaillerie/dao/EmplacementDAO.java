/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */

import com.bad.gestionquincaillerie.models.Emplacement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmplacementDAO {
    public boolean ajouter(Emplacement emplacement) {
        String sql = "INSERT INTO emplacements (type_emplacement, designation_emplacement) VALUES (?, ?)";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emplacement.getTypeEmplacement());
            stmt.setString(2, emplacement.getDesignationEmplacement());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout emplacement : " + e.getMessage());
            return false;
        }
    }
    
    public boolean modifier(Emplacement emplacement) {
        String sql = "UPDATE emplacements SET type_emplacement = ?, designation_emplacement = ? WHERE id_emplacement = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emplacement.getTypeEmplacement());
            stmt.setString(2, emplacement.getDesignationEmplacement());
            stmt.setInt(3, emplacement.getIdEmplacement());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification emplacement : " + e.getMessage());
            return false;
        }
    }
    public boolean supprimer(int idEmplacement) {
        String sql = "DELETE FROM emplacements WHERE id_emplacement = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmplacement);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression emplacement : " + e.getMessage());
            return false;
        }
    }

    public List<Emplacement> getAll() {
        List<Emplacement> liste = new ArrayList<>();
        String sql = "SELECT * FROM emplacements ORDER BY id_emplacement DESC";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emplacement e = new Emplacement(
                    rs.getInt("id_emplacement"),
                    rs.getString("type_emplacement"),
                    rs.getString("designation_emplacement")
                );
                liste.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération emplacements : " + e.getMessage());
        }
        return liste;
    }
}

