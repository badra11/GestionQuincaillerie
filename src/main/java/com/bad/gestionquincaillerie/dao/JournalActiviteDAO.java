/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */

import com.bad.gestionquincaillerie.models.JournalActivite;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JournalActiviteDAO {

    public void enregistrer(int idEmploye, String action) {
        String sql = "INSERT INTO journal_activite (id_employe, action) VALUES (?, ?)";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmploye);
            stmt.setString(2, action);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur journal activité : " + e.getMessage());
        }
    }

    public List<JournalActivite> getAll() {
        List<JournalActivite> liste = new ArrayList<>();
        String sql = "SELECT j.date_action, e.prenom, e.nom, j.action "
                   + "FROM journal_activite j JOIN employes e ON j.id_employe = e.id_employe "
                   + "ORDER BY j.date_action DESC LIMIT 500";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JournalActivite j = new JournalActivite();
                Timestamp ts = rs.getTimestamp("date_action");
                if (ts != null) j.setDateAction(ts.toLocalDateTime());
                j.setNomEmploye(rs.getString("prenom") + " " + rs.getString("nom"));
                j.setAction(rs.getString("action"));
                liste.add(j);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération journal : " + e.getMessage());
        }
        return liste;
    }
}
