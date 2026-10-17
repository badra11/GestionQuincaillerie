/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.Produit;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProduitDAO {

    /**
     * Ajoute un produit en 2 étapes : 1. INSERT avec une référence temporaire
     * (placeholder), pour obtenir l'id auto-généré 2. Calcul de la vraie
     * référence (3 lettres designation + 3 lettres marque + id_produit-id_type)
     * 3. UPDATE pour enregistrer la référence et le code-barre définitifs
     *
     * Retourne le produit complet (avec id, reference, codeBarre) si succès,
     * sinon null.
     */
    public Produit ajouter(Produit produit) {
        String sqlInsert = "INSERT INTO produits (id_type, id_emplacement, reference, designation, code_barre, "
                + "prix_vente, prix_achat, quantite, alerte_seuil, unite, marque) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, produit.getIdType());
            stmt.setInt(2, produit.getIdEmplacement());
            stmt.setString(3, "TEMP");   // référence provisoire (colonne NOT NULL)
            stmt.setString(4, produit.getDesignation());
            stmt.setString(5, "TEMP");   // code-barre provisoire (colonne NOT NULL)
            stmt.setDouble(6, produit.getPrixVente());
            stmt.setDouble(7, produit.getPrixAchat());
            stmt.setInt(8, 0);           // quantité initiale toujours à 0
            stmt.setBoolean(9, true);   // alerte_seuil toujours "oui" au départ (quantite = 0)
            stmt.setString(10, produit.getUnite());
            stmt.setString(11, produit.getMarque());

            stmt.executeUpdate();

            int idGenere;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idGenere = generatedKeys.getInt(1);
                } else {
                    return null;
                }
            }

            // ===== Génération de la référence définitive =====
            String reference = genererReference(produit.getDesignation(), produit.getMarque(), idGenere, produit.getIdType());
            String codeBarre = reference; // convenu : le code-barre = la référence

            String sqlUpdate = "UPDATE produits SET reference = ?, code_barre = ? WHERE id_produit = ?";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, reference);
                stmtUpdate.setString(2, codeBarre);
                stmtUpdate.setInt(3, idGenere);
                stmtUpdate.executeUpdate();
            }

            produit.setIdProduit(idGenere);
            produit.setReference(reference);
            produit.setCodeBarre(codeBarre);
            produit.setQuantite(0);
            produit.setAlerteSeuil(true);

            return produit;

        } catch (SQLException e) {
            System.err.println("Erreur ajout produit : " + e.getMessage());
            return null;
        }
    }

    /**
     * Construit la référence au format : 3 lettres designation + 3 lettres
     * marque + id_produit-id_type Exemple : "Marteau" + "Stanley" + id=15 +
     * id_type=2 -> "MARSTA15-2"
     */
    private String genererReference(String designation, String marque, int idProduit, int idType) {
        String prefixeDesignation = extraireTroisLettres(designation);
        String prefixeMarque = extraireTroisLettres(marque);
        return (prefixeDesignation + prefixeMarque + idProduit + "-" + idType).toUpperCase();
    }

    private String extraireTroisLettres(String texte) {
        if (texte == null) {
            return "XXX";
        }
        String nettoye = texte.trim().replaceAll("[^A-Za-z]", ""); // enlève espaces/chiffres/accents
        if (nettoye.length() >= 3) {
            return nettoye.substring(0, 3);
        }
        // Si le mot fait moins de 3 lettres, complète avec des "X"
        return (nettoye + "XXX").substring(0, 3);
    }

    public boolean incrementerStock(int idProduit, int quantiteAjoutee) {
        String sqlSelect = "SELECT quantite FROM produits WHERE id_produit = ?";
        String sqlUpdate = "UPDATE produits SET quantite = ?, alerte_seuil = ? WHERE id_produit = ?";

        try (Connection conn = ConnexionBD.getConnexion()) {

            int quantiteActuelle;
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                stmtSelect.setInt(1, idProduit);
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    if (rs.next()) {
                        quantiteActuelle = rs.getInt("quantite");
                    } else {
                        return false;
                    }
                }
            }

            int nouvelleQuantite = quantiteActuelle + quantiteAjoutee;
            boolean alerteSeuil = nouvelleQuantite < 10;

            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setInt(1, nouvelleQuantite);
                stmtUpdate.setBoolean(2, alerteSeuil);
                stmtUpdate.setInt(3, idProduit);
                stmtUpdate.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour stock : " + e.getMessage());
            return false;
        }
    }

    public boolean decrementerStock(int idProduit, int quantiteVendue) {
        String sqlSelect = "SELECT quantite FROM produits WHERE id_produit = ?";
        String sqlUpdate = "UPDATE produits SET quantite = ?, alerte_seuil = ? WHERE id_produit = ?";

        try (Connection conn = ConnexionBD.getConnexion()) {

            int quantiteActuelle;
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                stmtSelect.setInt(1, idProduit);
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    if (rs.next()) {
                        quantiteActuelle = rs.getInt("quantite");
                    } else {
                        return false;
                    }
                }
            }

            if (quantiteActuelle < quantiteVendue) {
                System.err.println("Stock insuffisant pour ce produit.");
                return false;
            }

            int nouvelleQuantite = quantiteActuelle - quantiteVendue;
            boolean alerteSeuil = nouvelleQuantite < 10;

            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setInt(1, nouvelleQuantite);
                stmtUpdate.setBoolean(2, alerteSeuil);
                stmtUpdate.setInt(3, idProduit);
                stmtUpdate.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour stock (vente) : " + e.getMessage());
            return false;
        }
    }

    public boolean modifier(Produit produit) {
        String sql = "UPDATE produits SET id_type = ?, id_emplacement = ?, designation = ?, "
                + "prix_vente = ?, prix_achat = ?, unite = ?, marque = ? WHERE id_produit = ?";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produit.getIdType());
            stmt.setInt(2, produit.getIdEmplacement());
            stmt.setString(3, produit.getDesignation());
            stmt.setDouble(4, produit.getPrixVente());
            stmt.setDouble(5, produit.getPrixAchat());
            stmt.setString(6, produit.getUnite());
            stmt.setString(7, produit.getMarque());
            stmt.setInt(8, produit.getIdProduit());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification produit : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int idProduit) {
        String sql = "DELETE FROM produits WHERE id_produit = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduit);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression produit : " + e.getMessage());
            return false;
        }
    }

    public List<Produit> getAll() {
        List<Produit> liste = new ArrayList<>();
        String sql = "SELECT p.id_produit, p.id_type, t.nom_type, p.id_emplacement, e.designation_emplacement, "
                + "p.reference, p.designation, p.code_barre, p.prix_vente, p.prix_achat, "
                + "p.quantite, p.alerte_seuil, p.unite, p.marque "
                + "FROM produits p "
                + "JOIN types t ON p.id_type = t.id_type "
                + "JOIN emplacements e ON p.id_emplacement = e.id_emplacement "
                + "ORDER BY p.id_produit DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produit p = new Produit();
                p.setIdProduit(rs.getInt("id_produit"));
                p.setIdType(rs.getInt("id_type"));
                p.setNomType(rs.getString("nom_type"));
                p.setIdEmplacement(rs.getInt("id_emplacement"));
                p.setDesignationEmplacement(rs.getString("designation_emplacement"));
                p.setReference(rs.getString("reference"));
                p.setDesignation(rs.getString("designation"));
                p.setCodeBarre(rs.getString("code_barre"));
                p.setPrixVente(rs.getDouble("prix_vente"));
                p.setPrixAchat(rs.getDouble("prix_achat"));
                p.setQuantite(rs.getInt("quantite"));
                p.setAlerteSeuil(rs.getBoolean("alerte_seuil"));
                p.setUnite(rs.getString("unite"));
                p.setMarque(rs.getString("marque"));
                liste.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération produits : " + e.getMessage());
        }
        return liste;
    }

    public List<Produit> getByType(int idType) {
        List<Produit> liste = new ArrayList<>();
        String sql = "SELECT p.id_produit, p.id_type, t.nom_type, p.id_emplacement, e.designation_emplacement, "
                + "p.reference, p.designation, p.code_barre, p.prix_vente, p.prix_achat, "
                + "p.quantite, p.alerte_seuil, p.unite, p.marque "
                + "FROM produits p "
                + "JOIN types t ON p.id_type = t.id_type "
                + "JOIN emplacements e ON p.id_emplacement = e.id_emplacement "
                + "WHERE p.id_type = ? "
                + "ORDER BY p.designation";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produit p = new Produit();
                    p.setIdProduit(rs.getInt("id_produit"));
                    p.setIdType(rs.getInt("id_type"));
                    p.setNomType(rs.getString("nom_type"));
                    p.setIdEmplacement(rs.getInt("id_emplacement"));
                    p.setDesignationEmplacement(rs.getString("designation_emplacement"));
                    p.setReference(rs.getString("reference"));
                    p.setDesignation(rs.getString("designation"));
                    p.setCodeBarre(rs.getString("code_barre"));
                    p.setPrixVente(rs.getDouble("prix_vente"));
                    p.setPrixAchat(rs.getDouble("prix_achat"));
                    p.setQuantite(rs.getInt("quantite"));
                    p.setAlerteSeuil(rs.getBoolean("alerte_seuil"));
                    p.setUnite(rs.getString("unite"));
                    p.setMarque(rs.getString("marque"));
                    liste.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération produits par type : " + e.getMessage());
        }
        return liste;
    }

    // ======= les Methodes pour l'affichage dans l'acceuil =========
    public int getNombreTotalProduits() {
        String sql = "SELECT COUNT(*) AS total FROM produits";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage produits : " + e.getMessage());
        }
        return 0;
    }

    public int getNombreProduitsEnAlerte() {
        // alerte_seuil = FALSE signifie quantite <= 10 -> besoin de réapprovisionner
        String sql = "SELECT COUNT(*) AS total FROM produits WHERE alerte_seuil = TRUE";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage alertes stock : " + e.getMessage());
        }
        return 0;
    }

    public List<Produit> getProduitsEnAlerte() {
        List<Produit> liste = new ArrayList<>();
        String sql = "SELECT p.id_produit, p.id_type, t.nom_type, p.id_emplacement, e.designation_emplacement, "
                + "p.reference, p.designation, p.code_barre, p.prix_vente, p.prix_achat, "
                + "p.quantite, p.alerte_seuil, p.unite, p.marque "
                + "FROM produits p "
                + "JOIN types t ON p.id_type = t.id_type "
                + "JOIN emplacements e ON p.id_emplacement = e.id_emplacement "
                + "WHERE p.alerte_seuil = FALSE "
                + "ORDER BY p.quantite ASC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produit p = new Produit();
                p.setIdProduit(rs.getInt("id_produit"));
                p.setIdType(rs.getInt("id_type"));
                p.setNomType(rs.getString("nom_type"));
                p.setIdEmplacement(rs.getInt("id_emplacement"));
                p.setDesignationEmplacement(rs.getString("designation_emplacement"));
                p.setReference(rs.getString("reference"));
                p.setDesignation(rs.getString("designation"));
                p.setCodeBarre(rs.getString("code_barre"));
                p.setPrixVente(rs.getDouble("prix_vente"));
                p.setPrixAchat(rs.getDouble("prix_achat"));
                p.setQuantite(rs.getInt("quantite"));
                p.setAlerteSeuil(rs.getBoolean("alerte_seuil"));
                p.setUnite(rs.getString("unite"));
                p.setMarque(rs.getString("marque"));
                liste.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération produits en alerte : " + e.getMessage());
        }
        return liste;
    }

    public Map<String, Integer> getRepartitionStockParType() {
        Map<String, Integer> resultat = new LinkedHashMap<>();
        String sql = "SELECT t.nom_type, SUM(p.quantite) AS total_quantite "
                + "FROM produits p JOIN types t ON p.id_type = t.id_type "
                + "GROUP BY t.nom_type ORDER BY total_quantite DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultat.put(rs.getString("nom_type"), rs.getInt("total_quantite"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur répartition stock par type : " + e.getMessage());
        }
        return resultat;
    }

    // ======== montant d'achat total des produits en stoks==============
    public double getValeurTotaleAchatStock() {
        String sql = "SELECT COALESCE(SUM(prix_achat * quantite), 0) AS total FROM produits";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur calcul valeur achat stock : " + e.getMessage());
        }
        return 0;
    }

    // ======== montant de vente total à engendrer des produits en stoks==============
    public double getValeurTotaleVenteStock() {
        String sql = "SELECT COALESCE(SUM(prix_vente * quantite), 0) AS total FROM produits";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur calcul valeur vente stock : " + e.getMessage());
        }
        return 0;
    }

}
