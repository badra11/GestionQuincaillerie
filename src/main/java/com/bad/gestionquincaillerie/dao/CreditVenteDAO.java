/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreditVenteDAO {

    /**
     * Crée un enregistrement de crédit et retourne son id généré, ou -1 en cas
     * d'échec.
     */
    public int ajouter(CreditVente credit) {
        String numCredit = genererNumCredit();
        String sql = "INSERT INTO credit_ventes (num_credit, id_client, id_vente, montant_credit, montant_payée, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, numCredit);
            stmt.setInt(2, credit.getIdClient());
            stmt.setInt(3, credit.getIdVente());
            stmt.setDouble(4, credit.getMontantCredit());
            stmt.setDouble(5, credit.getMontantPaye());
            stmt.setBoolean(6, false);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("Erreur création crédit vente : " + e.getMessage());
            return -1;
        }
    }

    private String genererNumCredit() {
        int annee = LocalDateTime.now().getYear();
        String sql = "SELECT COUNT(*) AS total FROM credit_ventes";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            int compteur = 1;
            if (rs.next()) {
                compteur = rs.getInt("total") + 1;
            }
            return String.format("CRE-%d-%03d", annee, compteur);

        } catch (SQLException e) {
            System.err.println("Erreur génération numéro crédit : " + e.getMessage());
            return "CRE-" + annee + "-000";
        }
    }

    public List<CreditVente> getAll() {
        List<CreditVente> liste = new ArrayList<>();
        String sql = "SELECT cv.id_credit, cv.num_credit, cv.id_client, c.nom, cv.id_vente, v.num_vente, "
                + "cv.montant_credit, cv.montant_payée, cv.statut "
                + "FROM credit_ventes cv "
                + "JOIN clients c ON cv.id_client = c.id_client "
                + "JOIN ventes v ON cv.id_vente = v.id_vente "
                + "ORDER BY cv.id_credit DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CreditVente cv = new CreditVente();
                cv.setIdCredit(rs.getInt("id_credit"));
                cv.setNumCredit(rs.getString("num_credit"));
                cv.setIdClient(rs.getInt("id_client"));
                cv.setNomClient(rs.getString("nom"));
                cv.setIdVente(rs.getInt("id_vente"));
                cv.setNumVente(rs.getString("num_vente"));
                cv.setMontantCredit(rs.getDouble("montant_credit"));
                cv.setMontantPaye(rs.getDouble("montant_payée"));
                cv.setStatut(rs.getBoolean("statut"));
                liste.add(cv);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération crédits : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Enregistre un règlement partiel ou total. Si le montant payé cumulé
     * atteint le montant du crédit, le statut passe à "soldé" et le client est
     * délié du crédit.
     */
    public boolean reglerCredit(int idCredit, double montantVerse) {
        String sqlSelect = "SELECT montant_credit, montant_payée, id_client FROM credit_ventes WHERE id_credit = ?";
        String sqlUpdate = "UPDATE credit_ventes SET montant_payée = ?, statut = ? WHERE id_credit = ?";

        try (Connection conn = ConnexionBD.getConnexion()) {

            double montantCredit;
            double montantDejaPaye;
            int idClient;

            try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
                stmt.setInt(1, idCredit);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                    montantCredit = rs.getDouble("montant_credit");
                    montantDejaPaye = rs.getDouble("montant_payée");
                    idClient = rs.getInt("id_client");
                }
            }

            double nouveauMontantPaye = montantDejaPaye + montantVerse;
            boolean solde = nouveauMontantPaye >= montantCredit;

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                stmt.setDouble(1, nouveauMontantPaye);
                stmt.setBoolean(2, solde);
                stmt.setInt(3, idCredit);
                stmt.executeUpdate();
            }

            // Si le crédit est totalement soldé, on délie le client (id_credit redevient NULL)
            if (solde) {
                String sqlClient = "UPDATE clients SET id_credit = NULL WHERE id_client = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlClient)) {
                    stmt.setInt(1, idClient);
                    stmt.executeUpdate();
                }
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Erreur règlement crédit : " + e.getMessage());
            return false;
        }
    }

    public CreditVente getCreditActifDuClient(int idClient) {
        String sql = "SELECT * FROM credit_ventes WHERE id_client = ? AND statut = FALSE";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CreditVente c = new CreditVente();
                    c.setIdCredit(rs.getInt("id_credit"));
                    c.setNumCredit(rs.getString("num_credit"));
                    c.setIdClient(rs.getInt("id_client"));
                    c.setIdVente(rs.getInt("id_vente"));
                    c.setMontantCredit(rs.getDouble("montant_credit"));
                    c.setMontantPaye(rs.getDouble("montant_payée"));
                    c.setStatut(rs.getBoolean("statut"));
                    return c;
                }
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erreur récupération crédit actif : " + e.getMessage());
            return null;
        }
    }

    /**
     * Retourne la somme des montants restants (montant_credit - montant_payé)
     * pour tous les crédits NON soldés d'un client donné.
     */
    public double getMontantCreanceTotaleParClient(int idClient) {
        String sql = "SELECT SUM(montant_credit - `montant_payée`) AS total_restant "
                + "FROM credit_ventes WHERE id_client = ? AND statut = FALSE";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_restant"); // renvoie 0 si aucun crédit (SUM sur rien = NULL -> 0 via getDouble)
                }
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Erreur calcul créance totale : " + e.getMessage());
            return 0;
        }
    }

    /**
     * Indique si le client a au moins un crédit non soldé.
     */
    public boolean aDesCreditsActifs(int idClient) {
        String sql = "SELECT COUNT(*) AS total FROM credit_ventes WHERE id_client = ? AND statut = FALSE";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erreur vérification crédits actifs : " + e.getMessage());
            return false;
        }
    }

    // ======= les methodes pour l'affichage de l'acceuil===
    public double getMontantCreanceGlobale() {
        String sql = "SELECT COALESCE(SUM(montant_credit - `montant_payée`), 0) AS total "
                + "FROM credit_ventes WHERE statut = FALSE";
        try (Connection conn = ConnexionBD.getConnexion(); 
            PreparedStatement stmt = conn.prepareStatement(sql); 
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur calcul créance globale : " + e.getMessage());
        }
        return 0;
    }

    public List<CreditVente> getCreditsActifs() {
        List<CreditVente> liste = new ArrayList<>();
        String sql = "SELECT cv.id_credit, cv.num_credit, cv.id_client, c.nom, cv.id_vente, v.num_vente, "
                + "cv.montant_credit, cv.`montant_payée`, cv.statut "
                + "FROM credit_ventes cv "
                + "JOIN clients c ON cv.id_client = c.id_client "
                + "JOIN ventes v ON cv.id_vente = v.id_vente "
                + "WHERE cv.statut = FALSE "
                + "ORDER BY (cv.montant_credit - cv.`montant_payée`) DESC";

        try (Connection conn = ConnexionBD.getConnexion(); 
            PreparedStatement stmt = conn.prepareStatement(sql); 
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CreditVente cv = new CreditVente();
                cv.setIdCredit(rs.getInt("id_credit"));
                cv.setNumCredit(rs.getString("num_credit"));
                cv.setIdClient(rs.getInt("id_client"));
                cv.setNomClient(rs.getString("nom"));
                cv.setIdVente(rs.getInt("id_vente"));
                cv.setNumVente(rs.getString("num_vente"));
                cv.setMontantCredit(rs.getDouble("montant_credit"));
                cv.setMontantPaye(rs.getDouble("montant_payée"));
                cv.setStatut(rs.getBoolean("statut"));
                liste.add(cv);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération crédits actifs : " + e.getMessage());
        }
        return liste;
    }

}
