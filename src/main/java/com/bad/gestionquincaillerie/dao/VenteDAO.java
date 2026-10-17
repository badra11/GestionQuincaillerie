/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.Vente;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VenteDAO {

    public Vente ajouter(Vente vente, String numVente) {
        LocalDateTime maintenant = LocalDateTime.now();

        String sql = "INSERT INTO ventes (num_vente, id_client, id_employe, mode_payment, montant_total, "
                + "montant_reçus, montant_restant, date_vente, validation, adresse_livraison, "
                + "vehicule_livraison, statut_payment) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, numVente);
            // Dans ajouter(), remplacer la ligne id_client :
            if (vente.getIdClient() > 0) {
                stmt.setInt(2, vente.getIdClient());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, vente.getIdEmploye());
            stmt.setString(4, vente.getModePayment());
            stmt.setDouble(5, vente.getMontantTotal());
            stmt.setDouble(6, vente.getMontantRecu());
            stmt.setDouble(7, vente.getMontantRestant());
            stmt.setTimestamp(8, Timestamp.valueOf(maintenant));
            stmt.setBoolean(9, true); // validation = true, vente finalisée immédiatement
            stmt.setString(10, vente.getAdresseLivraison());
            stmt.setString(11, vente.getVehiculeLivraison());
            stmt.setString(12, vente.getStatutPayment());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vente.setIdVente(generatedKeys.getInt(1));
                    vente.setNumVente(numVente);
                    vente.setDateVente(maintenant);
                    vente.setValidation(true);
                    return vente;
                }
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erreur ajout vente : " + e.getMessage());
            return null;
        }
    }

    public String genererProchainNumVente() {
        int annee = LocalDateTime.now().getYear();
        String sql = "SELECT COUNT(*) AS total FROM ventes WHERE YEAR(date_vente) = ?";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, annee);
            try (ResultSet rs = stmt.executeQuery()) {
                int compteur = 1;
                if (rs.next()) {
                    compteur = rs.getInt("total") + 1;
                }
                return String.format("VTE-%d-%03d", annee, compteur);
            }

        } catch (SQLException e) {
            System.err.println("Erreur génération numéro vente : " + e.getMessage());
            return "VTE-" + annee + "-000";
        }
    }

    public List<Vente> getAll() {
        List<Vente> liste = new ArrayList<>();
        String sql = "SELECT v.id_vente, v.num_vente, v.id_client, c.nom, v.id_employe, v.mode_payment, "
                + "v.montant_total, v.montant_reçus, v.montant_restant, v.date_vente, v.validation, "
                + "v.adresse_livraison, v.vehicule_livraison, v.statut_payment "
                + "FROM ventes v "
                + "LEFT JOIN clients c ON v.id_client = c.id_client "
                + "ORDER BY v.id_vente DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vente v = new Vente();
                v.setIdVente(rs.getInt("id_vente"));
                v.setNumVente(rs.getString("num_vente"));
                //v.setIdClient(rs.getInt("id_client"));
                //v.setNomClient(rs.getString("nom"));
                int idClient = rs.getInt("id_client");
                v.setIdClient(rs.wasNull() ? 0 : idClient);
                v.setNomClient(rs.getString("nom") != null ? rs.getString("nom") : "Client anonyme");
                v.setIdEmploye(rs.getInt("id_employe"));
                v.setModePayment(rs.getString("mode_payment"));
                v.setMontantTotal(rs.getDouble("montant_total"));
                v.setMontantRecu(rs.getDouble("montant_reçus"));
                v.setMontantRestant(rs.getDouble("montant_restant"));
                Timestamp ts = rs.getTimestamp("date_vente");
                if (ts != null) {
                    v.setDateVente(ts.toLocalDateTime());
                }
                v.setValidation(rs.getBoolean("validation"));
                v.setAdresseLivraison(rs.getString("adresse_livraison"));
                v.setVehiculeLivraison(rs.getString("vehicule_livraison"));
                v.setStatutPayment(rs.getString("statut_payment"));
                liste.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération ventes : " + e.getMessage());
        }
        return liste;
    }

    public boolean mettreAJourValidation(int idVente, boolean validation) {
        String sql = "UPDATE ventes SET validation = ? WHERE id_vente = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, validation);
            stmt.setInt(2, idVente);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour validation vente : " + e.getMessage());
            return false;
        }
    }

    public boolean mettreAJourLivraison(int idVente, String adresseLivraison, String vehiculeLivraison) {
        String sql = "UPDATE ventes SET adresse_livraison = ?, vehicule_livraison = ? WHERE id_vente = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adresseLivraison);
            stmt.setString(2, vehiculeLivraison);
            stmt.setInt(3, idVente);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour livraison : " + e.getMessage());
            return false;
        }
    }

    // ======= les methodes pour les affichages de l'acceuil===========
    public int getNombreVentesDuJour() {
        String sql = "SELECT COUNT(*) AS total FROM ventes WHERE DATE(date_vente) = CURDATE()";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage ventes du jour : " + e.getMessage());
        }
        return 0;
    }

    public double getMontantVentesDuJour() {
        String sql = "SELECT COALESCE(SUM(montant_total), 0) AS total FROM ventes WHERE DATE(date_vente) = CURDATE()";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur calcul montant ventes du jour : " + e.getMessage());
        }
        return 0;
    }

    /**
     * Retourne le total des ventes groupé par jour, entre deux dates
     * (incluses).
     */
    public Map<LocalDate, Double> getTotalVentesParJour(LocalDate debut, LocalDate fin) {
        Map<LocalDate, Double> resultat = new LinkedHashMap<>();
        String sql = "SELECT DATE(date_vente) AS jour, SUM(montant_total) AS total "
                + "FROM ventes WHERE DATE(date_vente) BETWEEN ? AND ? "
                + "GROUP BY DATE(date_vente) ORDER BY jour";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(debut));
            stmt.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultat.put(rs.getDate("jour").toLocalDate(), rs.getDouble("total"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur agrégation ventes par jour : " + e.getMessage());
        }
        return resultat;
    }

    public List<Vente> getVentesParPeriode(LocalDate debut, LocalDate fin) {
        List<Vente> liste = new ArrayList<>();
        String sql = "SELECT v.id_vente, v.num_vente, v.id_client, c.nom, v.id_employe, v.mode_payment, "
                + "v.montant_total, v.montant_reçus, v.montant_restant, v.date_vente, v.validation, "
                + "v.adresse_livraison, v.vehicule_livraison, v.statut_payment "
                + "FROM ventes v LEFT JOIN clients c ON v.id_client = c.id_client "
                + "WHERE DATE(v.date_vente) BETWEEN ? AND ? "
                + "ORDER BY v.date_vente DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(debut));
            stmt.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vente v = new Vente();
                    v.setIdVente(rs.getInt("id_vente"));
                    v.setNumVente(rs.getString("num_vente"));
                    v.setIdClient(rs.getInt("id_client"));
                    v.setNomClient(rs.getString("nom") != null ? rs.getString("nom") : "Client anonyme");
                    v.setIdEmploye(rs.getInt("id_employe"));
                    v.setModePayment(rs.getString("mode_payment"));
                    v.setMontantTotal(rs.getDouble("montant_total"));
                    v.setMontantRecu(rs.getDouble("montant_reçus"));
                    v.setMontantRestant(rs.getDouble("montant_restant"));
                    Timestamp ts = rs.getTimestamp("date_vente");
                    if (ts != null) {
                        v.setDateVente(ts.toLocalDateTime());
                    }
                    v.setValidation(rs.getBoolean("validation"));
                    v.setStatutPayment(rs.getString("statut_payment"));
                    liste.add(v);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération ventes par période : " + e.getMessage());
        }
        return liste;
    }

    public List<Vente> getVentesParClient(int idClient) {
        List<Vente> liste = new ArrayList<>();
        String sql = "SELECT * FROM ventes WHERE id_client = ? ORDER BY date_vente DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vente v = new Vente();
                    v.setIdVente(rs.getInt("id_vente"));
                    v.setNumVente(rs.getString("num_vente"));
                    v.setMontantTotal(rs.getDouble("montant_total"));
                    Timestamp ts = rs.getTimestamp("date_vente");
                    if (ts != null) {
                        v.setDateVente(ts.toLocalDateTime());
                    }
                    v.setStatutPayment(rs.getString("statut_payment"));
                    liste.add(v);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération ventes par client : " + e.getMessage());
        }
        return liste;
    }

    public double getMontantVentesDuMois(int mois, int annee) {
        String sql = "SELECT COALESCE(SUM(montant_total), 0) AS total FROM ventes "
                + "WHERE MONTH(date_vente) = ? AND YEAR(date_vente) = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mois);
            stmt.setInt(2, annee);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur calcul montant ventes du mois : " + e.getMessage());
        }
        return 0;
    }

    public List<String> getMoisDisponibles() {
        List<String> liste = new ArrayList<>();
        String sql = "SELECT DISTINCT DATE_FORMAT(date_vente, '%Y-%m') AS mois "
                + "FROM ventes ORDER BY mois DESC";
        try (Connection conn = ConnexionBD.getConnexion(); 
            PreparedStatement stmt = conn.prepareStatement(sql); 
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(rs.getString("mois"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération mois disponibles : " + e.getMessage());
        }
        return liste;
    }

}
