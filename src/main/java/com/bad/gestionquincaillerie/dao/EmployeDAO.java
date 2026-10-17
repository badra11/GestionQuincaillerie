/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.Employe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//import org.mindrot.jbcrypt.BCrypt;

public class EmployeDAO {

    /**
     * Vérifie les identifiants et retourne l'employé si l'authentification
     * réussit. Retourne null si les identifiants sont invalides ou si l'employé
     * n'est pas autorisé à utiliser l'application (role différent de
     * admin/user).
     */
    /**
     * Authenticates a user based on provided credentials.
     *
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return A boolean indicating whether the authentication was successful.
     * @throws IllegalArgumentException if the username or password is null or
     * empty.
     */
    public Employe authentifier(String username, String password) {
        String sql = "SELECT * FROM employes WHERE username = ? AND password = ? ";
        //+ "AND (role = 'admin' OR role = 'user')";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employe employe = new Employe();
                    employe.setId(rs.getInt("id_employe"));
                    employe.setMatricule(rs.getString("matricule"));
                    employe.setNom(rs.getString("nom"));
                    employe.setPrenom(rs.getString("prenom"));
                    employe.setTelephone(rs.getString("telephone"));
                    employe.setRole(rs.getString("role"));
                    employe.setDateEmbauche(rs.getDate("date_embauche").toLocalDate());
                    employe.setSalaire(rs.getDouble("salaire"));
                    employe.setUsername(rs.getString("username"));
                    employe.setPassword(rs.getString("password"));
                    return employe;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Aucun employé trouvé ou non autorisé
    }

    public Employe ajouter(Employe employe) {
        String sqlInsert = "INSERT INTO employes (matricule, nom, prenom, telephone, role, date_embauche, "
                + "salaire, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "TEMP"); // matricule provisoire
            stmt.setString(2, employe.getNom());
            stmt.setString(3, employe.getPrenom());
            stmt.setString(4, employe.getTelephone());
            stmt.setString(5, employe.getRole());
            stmt.setDate(6, java.sql.Date.valueOf(employe.getDateEmbauche()));
            stmt.setDouble(7, employe.getSalaire());

            if (employe.getUsername() != null && !employe.getUsername().isEmpty()) {
                stmt.setString(8, employe.getUsername());
                stmt.setString(9, employe.getPassword());
            } else {
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
            }

            stmt.executeUpdate();

            int idGenere;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idGenere = generatedKeys.getInt(1);
                } else {
                    return null;
                }
            }

            String matricule = genererMatricule(idGenere, employe.getNom(), employe.getPrenom());

            String sqlUpdate = "UPDATE employes SET matricule = ? WHERE id_employe = ?";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, matricule);
                stmtUpdate.setInt(2, idGenere);
                stmtUpdate.executeUpdate();
            }

            employe.setId(idGenere);
            employe.setMatricule(matricule);
            return employe;

        } catch (SQLException e) {
            System.err.println("Erreur ajout employé : " + e.getMessage());
            return null;
        }
    }

    private String genererMatricule(int idEmploye, String nom, String prenom) {
        int annee = java.time.LocalDate.now().getYear();
        String numero = String.format("%03d", idEmploye);
        String prefixeNom = extraireDeuxLettres(nom);
        String prefixePrenom = extraireDeuxLettres(prenom);
        return ("EMP-" + numero + "-" + annee + "-" + prefixeNom + prefixePrenom).toUpperCase();
    }

    private String extraireDeuxLettres(String texte) {
        if (texte == null) {
            return "XX";
        }
        String nettoye = texte.trim().replaceAll("[^A-Za-z]", "");
        if (nettoye.length() >= 2) {
            return nettoye.substring(0, 2);
        }
        return (nettoye + "XX").substring(0, 2);
    }

    public boolean modifier(Employe employe) {
        String sql = "UPDATE employes SET nom = ?, prenom = ?, telephone = ?, role = ?, date_embauche = ?, "
                + "salaire = ?, username = ?, password = ? WHERE id_employe = ?";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employe.getNom());
            stmt.setString(2, employe.getPrenom());
            stmt.setString(3, employe.getTelephone());
            stmt.setString(4, employe.getRole());
            stmt.setDate(5, java.sql.Date.valueOf(employe.getDateEmbauche()));
            stmt.setDouble(6, employe.getSalaire());

            if (employe.getUsername() != null && !employe.getUsername().isEmpty()) {
                stmt.setString(7, employe.getUsername());
                stmt.setString(8, employe.getPassword());
            } else {
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
            }

            stmt.setInt(9, employe.getId());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification employé : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int idEmploye) {
        String sql = "DELETE FROM employes WHERE id_employe = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmploye);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression employé : " + e.getMessage());
            return false;
        }
    }

    public List<Employe> getAll() {
        List<Employe> liste = new ArrayList<>();
        String sql = "SELECT * FROM employes ORDER BY id_employe DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employe e = new Employe();
                e.setId(rs.getInt("id_employe"));
                e.setMatricule(rs.getString("matricule"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setTelephone(rs.getString("telephone"));
                e.setRole(rs.getString("role"));
                java.sql.Date date = rs.getDate("date_embauche");
                if (date != null) {
                    e.setDateEmbauche(date.toLocalDate());
                }
                e.setSalaire(rs.getDouble("salaire"));
                e.setUsername(rs.getString("username"));
                e.setPassword(rs.getString("password"));
                liste.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération employés : " + e.getMessage());
        }
        return liste;
    }

    public int getNombreEmployes() {
        String sql = "SELECT COUNT(*) AS total FROM employes";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage employés : " + e.getMessage());
        }
        return 0;
    }
}
