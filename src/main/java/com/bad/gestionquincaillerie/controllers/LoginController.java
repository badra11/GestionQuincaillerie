/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

/**
 *
 * @author Badra Aliou Dembele
 */


import com.bad.gestionquincaillerie.dao.EmployeDAO;
import com.bad.gestionquincaillerie.models.Employe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.bad.gestionquincaillerie.controllers.DashboardController;
import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUtilisateur;

    @FXML
    private PasswordField txtMotDePasse;

    @FXML
    private Button btnConnexion;

    @FXML
    private Label lblMessage;

    private final EmployeDAO employeDAO = new EmployeDAO();

    @FXML
    private void handleConnexion() {
        String username = txtUtilisateur.getText().trim();
        String password = txtMotDePasse.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Veuillez remplir tous les champs.");
            return;
        }

        Employe employe = employeDAO.authentifier(username, password);

        if (employe != null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/Dashboard.fxml")
                );
                Parent dashboardRoot = loader.load();

                DashboardController dashboardController = loader.getController();
                dashboardController.initData(employe);

                Stage stage = (Stage) btnConnexion.getScene().getWindow();
                Scene dashboardScene = new Scene(dashboardRoot, 1000, 650);
                stage.setScene(dashboardScene);
                stage.setTitle("Gestion Quincaillerie - Tableau de bord");
                new JournalActiviteDAO().enregistrer(employe.getId(), "Connexion à l'application");

            } catch (IOException e) {
                e.printStackTrace();
                lblMessage.setText("Erreur lors du chargement du tableau de bord.");
            }

        } else {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Identifiant ou mot de passe incorrect.");
        }
    }
}
