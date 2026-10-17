/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.Commande;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandeDAO {

    /**
     * Ajoute une commande. Le num_commande est généré automatiquement (ex:
     * CMD-2026-001). Retourne la commande complète (avec id et numCommande) si
     * succès, sinon null.
     */
    public Commande ajouter(Commande commande, String numCommande) {
        LocalDateTime maintenant = LocalDateTime.now();

        String sql = "INSERT INTO commandes (num_commande, id_fournisseur, id_employe, mode_payment, "
                + "montant_total, montant_paye, montant_restant, date_commande, validation, statut_payment) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, numCommande);
            stmt.setInt(2, commande.getIdFournisseur());
            stmt.setInt(3, commande.getIdEmploye());
            stmt.setString(4, commande.getModePayment());
            stmt.setDouble(5, commande.getMontantTotal());
            stmt.setDouble(6, commande.getMontantPaye());
            stmt.setDouble(7, commande.getMontantRestant());
            stmt.setTimestamp(8, Timestamp.valueOf(maintenant));
            stmt.setBoolean(9, false); // validation = false par défaut
            stmt.setString(10, commande.getStatutPayment());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    commande.setIdCommande(generatedKeys.getInt(1));
                    commande.setNumCommande(numCommande);
                    commande.setDateCommande(maintenant);
                    commande.setValidation(false);
                    return commande;
                }
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erreur ajout commande : " + e.getMessage());
            return null;
        }
    }

    /**
     * Calcule le prochain numéro de commande (ex: CMD-2026-004) sans rien
     * insérer, pour pouvoir l'afficher à l'écran avant l'enregistrement
     * définitif.
     */
    public String genererProchainNumCommande() {
        int annee = LocalDateTime.now().getYear();
        String sql = "SELECT COUNT(*) AS total FROM commandes WHERE YEAR(date_commande) = ?";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, annee);
            try (ResultSet rs = stmt.executeQuery()) {
                int compteur = 1;
                if (rs.next()) {
                    compteur = rs.getInt("total") + 1;
                }
                return String.format("CMD-%d-%03d", annee, compteur);
            }

        } catch (SQLException e) {
            System.err.println("Erreur génération numéro commande : " + e.getMessage());
            return "CMD-" + annee + "-000";
        }

    }

    public boolean valider(int idCommande) {
        String sql = "UPDATE commandes SET validation = TRUE WHERE id_commande = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCommande);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur validation commande : " + e.getMessage());
            return false;
        }
    }

    public List<Commande> getAll() {
        List<Commande> liste = new ArrayList<>();
        String sql = "SELECT c.id_commande, c.num_commande, c.id_fournisseur, f.nom_societe, "
                + "c.id_employe,c.mode_payment, c.montant_total, c.montant_paye, c.montant_restant, "
                + "c.date_commande, c.validation, c.statut_payment "
                + "FROM commandes c "
                + "JOIN fournisseurs f ON c.id_fournisseur = f.id_fournisseur "
                + "ORDER BY c.id_commande DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Commande c = new Commande();
                c.setIdCommande(rs.getInt("id_commande"));
                c.setNumCommande(rs.getString("num_commande"));
                c.setIdFournisseur(rs.getInt("id_fournisseur"));
                c.setNomSociete(rs.getString("nom_societe"));
                c.setIdEmploye(rs.getInt("id_employe"));
                c.setModePayment(rs.getString("mode_payment"));
                c.setMontantTotal(rs.getDouble("montant_total"));
                c.setMontantPaye(rs.getDouble("montant_paye"));
                c.setMontantRestant(rs.getDouble("montant_restant"));
                Timestamp ts = rs.getTimestamp("date_commande");
                if (ts != null) {
                    c.setDateCommande(ts.toLocalDateTime());
                }
                c.setValidation(rs.getBoolean("validation"));
                c.setStatutPayment(rs.getString("statut_payment"));
                liste.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération commandes : " + e.getMessage());
        }
        return liste;
    }

    // ===== les methodes pour l'affichage de l'acceuil=====
    public Map<LocalDate, Double> getTotalCommandesParJour(LocalDate debut, LocalDate fin) {
        Map<LocalDate, Double> resultat = new LinkedHashMap<>();
        String sql = "SELECT DATE(date_commande) AS jour, SUM(montant_total) AS total "
                + "FROM commandes WHERE DATE(date_commande) BETWEEN ? AND ? "
                + "GROUP BY DATE(date_commande) ORDER BY jour";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(debut));
            stmt.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultat.put(rs.getDate("jour").toLocalDate(), rs.getDouble("total"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur agrégation commandes par jour : " + e.getMessage());
        }
        return resultat;
    }

    public List<Commande> getCommandesParPeriode(LocalDate debut, LocalDate fin) {
        List<Commande> liste = new ArrayList<>();
        String sql = "SELECT c.id_commande, c.num_commande, c.id_fournisseur, f.nom_societe, "
                + "c.id_employe, c.mode_payment, c.montant_total, c.montant_paye, c.montant_restant, "
                + "c.date_commande, c.validation, c.statut_payment "
                + "FROM commandes c JOIN fournisseurs f ON c.id_fournisseur = f.id_fournisseur "
                + "WHERE DATE(c.date_commande) BETWEEN ? AND ? "
                + "ORDER BY c.date_commande DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(debut));
            stmt.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Commande c = new Commande();
                    c.setIdCommande(rs.getInt("id_commande"));
                    c.setNumCommande(rs.getString("num_commande"));
                    c.setIdFournisseur(rs.getInt("id_fournisseur"));
                    c.setNomSociete(rs.getString("nom_societe"));
                    c.setIdEmploye(rs.getInt("id_employe"));
                    c.setModePayment(rs.getString("mode_payment"));
                    c.setMontantTotal(rs.getDouble("montant_total"));
                    c.setMontantPaye(rs.getDouble("montant_paye"));
                    c.setMontantRestant(rs.getDouble("montant_restant"));
                    Timestamp ts = rs.getTimestamp("date_commande");
                    if (ts != null) {
                        c.setDateCommande(ts.toLocalDateTime());
                    }
                    c.setValidation(rs.getBoolean("validation"));
                    c.setStatutPayment(rs.getString("statut_payment"));
                    liste.add(c);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération commandes par période : " + e.getMessage());
        }
        return liste;
    }

    public int getNombreCommandesEnAttente() {
        String sql = "SELECT COUNT(*) AS total FROM commandes WHERE validation = FALSE";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage commandes en attente : " + e.getMessage());
        }
        return 0;
    }
}
