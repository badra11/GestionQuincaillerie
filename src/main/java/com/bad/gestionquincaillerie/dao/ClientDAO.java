/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.dao;

/**
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public boolean ajouter(Client client) {
        String sql = "INSERT INTO clients (type_client, nom, email, telephone, adresse) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getTypeClient());
            stmt.setString(2, client.getNom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getTelephone());
            stmt.setString(5, client.getAdresse());
            // id_credit reste NULL par défaut (colonne non renseignée à l'insertion)
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout client : " + e.getMessage());
            return false;
        }
    }

    public boolean modifier(Client client) {
        String sql = "UPDATE clients SET type_client = ?, nom = ?, email = ?, telephone = ?, adresse = ? WHERE id_client = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getTypeClient());
            stmt.setString(2, client.getNom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getTelephone());
            stmt.setString(5, client.getAdresse());
            stmt.setInt(6, client.getIdClient());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification client : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int idClient) {
        String sql = "DELETE FROM clients WHERE id_client = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression client : " + e.getMessage());
            return false;
        }
    }

    public List<Client> getAll() {
        List<Client> liste = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY id_client DESC";

        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client c = new Client();
                c.setIdClient(rs.getInt("id_client"));

                int idCredit = rs.getInt("id_credit");
                c.setIdCredit(rs.wasNull() ? null : idCredit);

                c.setTypeClient(rs.getString("type_client"));
                c.setNom(rs.getString("nom"));
                c.setEmail(rs.getString("email"));
                c.setTelephone(rs.getString("telephone"));
                c.setAdresse(rs.getString("adresse"));
                liste.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération clients : " + e.getMessage());
        }
        return liste;
    }

    public boolean lierCredit(int idClient, int idCredit) {
        String sql = "UPDATE clients SET id_credit = ? WHERE id_client = ?";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCredit);
            stmt.setInt(2, idClient);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur liaison crédit-client : " + e.getMessage());
            return false;
        }
    }

    public int getNombreClients() {
        String sql = "SELECT COUNT(*) AS total FROM clients";
        try (Connection conn = ConnexionBD.getConnexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage clients : " + e.getMessage());
        }
        return 0;
    }
}
