/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.CreditVente;
import com.bad.gestionquincaillerie.models.ReçusVente;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReçusVenteDAO {

    /**
     * Retourne le reçu existant pour cette vente, ou null s'il n'y en a pas
     * encore.
     */
    public ReçusVente getByVente(int idVente) {
        String sql = "SELECT * FROM reçus_ventes WHERE id_vente = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapper(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erreur récupération reçu : " + e.getMessage());
            return null;
        }
    }

    /**
     * Crée un nouveau reçu pour cette vente et retourne l'objet complet (avec
     * id, num_reçus, date générés).
     */
    public ReçusVente ajouter(int idVente, double montant) {
        String numRecus = genererNumRecus();

        String sql = "INSERT INTO reçus_ventes (id_vente, num_reçus, montant) VALUES (?, ?, ?)";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idVente);
            stmt.setString(2, numRecus);
            stmt.setDouble(3, montant);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ReçusVente recu = new ReçusVente();
                    recu.setIdRecus(generatedKeys.getInt(1));
                    recu.setIdVente(idVente);
                    recu.setNumRecus(numRecus);
                    recu.setMontant(montant);
                    recu.setDateRecus(LocalDateTime.now());
                    return recu;
                }
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erreur création reçu : " + e.getMessage());
            return null;
        }
    }

  public List<ReçusVente> getAll() {
    List<ReçusVente> liste = new ArrayList<>();
    String sql = "SELECT r.`id_reçus`, r.id_vente, r.`num_reçus`, r.montant, r.`date_reçus`, "
               + "v.num_vente, c.nom, v.`montant_reçus`, v.montant_restant "
               + "FROM `reçus_ventes` r "
               + "JOIN ventes v ON r.id_vente = v.id_vente "
               + "LEFT JOIN clients c ON v.id_client = c.id_client "
               + "ORDER BY r.`id_reçus` DESC";

    try (Connection conn = ConnexionBD.getConnexion();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            ReçusVente r = mapper(rs);
            r.setNumVente(rs.getString("num_vente"));
            r.setNomClient(rs.getString("nom") != null ? rs.getString("nom") : "Client anonyme");
            r.setMontantRecuVente(rs.getDouble("montant_reçus"));
            r.setMontantRestantVente(rs.getDouble("montant_restant"));
            liste.add(r);
        }

    } catch (SQLException e) {
        System.err.println("Erreur récupération reçus : " + e.getMessage());
    }
    return liste;
}

    private String genererNumRecus() {
        int annee = LocalDateTime.now().getYear();
        String sql = "SELECT COUNT(*) AS total FROM reçus_ventes";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            int compteur = 1;
            if (rs.next()) {
                compteur = rs.getInt("total") + 1;
            }
            return String.format("REC-%d-%03d", annee, compteur);

        } catch (SQLException e) {
            System.err.println("Erreur génération numéro reçu : " + e.getMessage());
            return "REC-" + annee + "-000";
        }
    }

    private ReçusVente mapper(ResultSet rs) throws SQLException {
        ReçusVente recu = new ReçusVente();
        recu.setIdRecus(rs.getInt("id_reçus"));
        recu.setIdVente(rs.getInt("id_vente"));
        recu.setNumRecus(rs.getString("num_reçus"));
        recu.setMontant(rs.getDouble("montant"));
        Timestamp ts = rs.getTimestamp("date_reçus");
        if (ts != null) {
            recu.setDateRecus(ts.toLocalDateTime());
        }
        return recu;
    }
}
