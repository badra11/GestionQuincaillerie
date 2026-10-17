/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import com.bad.gestionquincaillerie.models.Employe;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Badra Aliou Dembele
 */

import com.bad.gestionquincaillerie.models.SauvegardeUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.time.LocalDateTime;

public class ParametresController {

    @FXML private Label lblMessage;

    // Adaptez ce chemin selon votre installation trouvée précédemment
    private static final String CHEMIN_MYSQLDUMP = "C:\\xampp\\mysql\\bin\\mysqldump.exe";
    
    private Employe employeConnecte;
    

    @FXML
    private void lancerSauvegarde() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir le dossier de sauvegarde");

        File dossier = chooser.showDialog(lblMessage.getScene().getWindow());
        if (dossier == null) return;

        boolean succes = SauvegardeUtil.sauvegarder(CHEMIN_MYSQLDUMP, dossier.getAbsolutePath(), "");
        LocalDateTime dateTime = LocalDateTime.now();

        if (succes) {
            lblMessage.setText("Sauvegarde réussie dans : " + dossier.getAbsolutePath());
            lblMessage.setStyle("-fx-text-fill: green;");
            new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Sauvegarde etat: " + String.valueOf(dateTime));
        } else {
            lblMessage.setText("Erreur lors de la sauvegarde. Vérifiez le chemin de mysqldump.");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }
}
