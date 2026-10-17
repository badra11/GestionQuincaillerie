/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */


import com.bad.gestionquincaillerie.models.LigneCommande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO {

    public boolean ajouter(LigneCommande ligne) {
        String sql = "INSERT INTO ligne_commandes (id_commande, id_produit, quantite, prix_unitaire, total_prix, status_commande) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ligne.getIdCommande());
            stmt.setInt(2, ligne.getIdProduit());
            stmt.setInt(3, ligne.getQuantite());
            stmt.setDouble(4, ligne.getPrixUnitaire());
            stmt.setDouble(5, ligne.getTotalPrix());
            stmt.setBoolean(6, false); // pas encore stocké
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout ligne de commande : " + e.getMessage());
            return false;
        }
    }

    public List<LigneCommande> getByCommande(int idCommande) {
        List<LigneCommande> liste = new ArrayList<>();
        String sql = "SELECT lc.id_lc, lc.id_commande, lc.id_produit, p.designation, "
                   + "lc.quantite, lc.prix_unitaire, lc.total_prix, lc.status_commande "
                   + "FROM ligne_commandes lc "
                   + "JOIN produits p ON lc.id_produit = p.id_produit "
                   + "WHERE lc.id_commande = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCommande);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LigneCommande l = new LigneCommande();
                    l.setIdLc(rs.getInt("id_lc"));
                    l.setIdCommande(rs.getInt("id_commande"));
                    l.setIdProduit(rs.getInt("id_produit"));
                    l.setDesignationProduit(rs.getString("designation"));
                    l.setQuantite(rs.getInt("quantite"));
                    l.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                    l.setTotalPrix(rs.getDouble("total_prix"));
                    l.setStatusCommande(rs.getBoolean("status_commande"));
                    liste.add(l);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération lignes de commande : " + e.getMessage());
        }
        return liste;
    }

    public boolean marquerCommeStocke(int idLc) {
        String sql = "UPDATE ligne_commandes SET status_commande = TRUE WHERE id_lc = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLc);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour statut ligne : " + e.getMessage());
            return false;
        }
    }
}
