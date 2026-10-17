/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.LigneVente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneVenteDAO {

    public boolean ajouter(LigneVente ligne) {
        String sql = "INSERT INTO ligne_ventes (id_vente, id_produit, quantite, prix_unitaire, total_prix, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ligne.getIdVente());
            stmt.setInt(2, ligne.getIdProduit());
            stmt.setInt(3, ligne.getQuantite());
            stmt.setDouble(4, ligne.getPrixUnitaire());
            stmt.setDouble(5, ligne.getTotalPrix());
            stmt.setBoolean(6, true); // vente immédiate, statut validé directement
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout ligne de vente : " + e.getMessage());
            return false;
        }
    }

    public List<LigneVente> getByVente(int idVente) {
        List<LigneVente> liste = new ArrayList<>();
        String sql = "SELECT lv.id_lv, lv.id_vente, lv.id_produit, p.designation, "
                + "lv.quantite, lv.prix_unitaire, lv.total_prix, lv.statut "
                + "FROM ligne_ventes lv "
                + "JOIN produits p ON lv.id_produit = p.id_produit "
                + "WHERE lv.id_vente = ?";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LigneVente l = new LigneVente();
                    l.setIdLv(rs.getInt("id_lv"));
                    l.setIdVente(rs.getInt("id_vente"));
                    l.setIdProduit(rs.getInt("id_produit"));
                    l.setDesignationProduit(rs.getString("designation"));
                    l.setQuantite(rs.getInt("quantite"));
                    l.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                    l.setTotalPrix(rs.getDouble("total_prix"));
                    l.setStatut(rs.getBoolean("statut"));
                    liste.add(l);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération lignes de vente : " + e.getMessage());
        }
        return liste;
    }

    public boolean annulerLigne(int idLv) {
        String sql = "UPDATE ligne_ventes SET statut = FALSE WHERE id_lv = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLv);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur annulation ligne de vente : " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie s'il reste au moins une ligne active (statut = 1) pour cette
     * vente.
     */
    public boolean auMoinsUneLigneActive(int idVente) {
        String sql = "SELECT COUNT(*) AS total FROM ligne_ventes WHERE id_vente = ? AND statut = TRUE";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erreur vérification lignes actives : " + e.getMessage());
            return false;
        }
    }

}
