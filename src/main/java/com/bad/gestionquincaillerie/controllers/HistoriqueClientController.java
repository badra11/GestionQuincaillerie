/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Badra Aliou Dembele
 */

import com.bad.gestionquincaillerie.models.Client;
import com.bad.gestionquincaillerie.models.Vente;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoriqueClientController {

    @FXML private Label lblTitre;
    @FXML private Label lblCreanceInfo;
    @FXML private TableView<Vente> tableHistorique;
    @FXML private TableColumn<Vente, String> colNumVente;
    @FXML private TableColumn<Vente, String> colDate;
    @FXML private TableColumn<Vente, Double> colMontant;
    @FXML private TableColumn<Vente, String> colStatut;

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void initData(Client client, List<Vente> ventes, double creanceTotale) {
        lblTitre.setText("Historique des achats de " + client.getNom().toUpperCase());

        if (creanceTotale > 0) {
            lblCreanceInfo.setText("Créance actuelle : " + FormatUtilitaire.formaterMontant(creanceTotale) + " FCFA");
            lblCreanceInfo.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else {
            lblCreanceInfo.setText("Aucune créance en cours");
            lblCreanceInfo.setStyle("-fx-text-fill: #27ae60;");
        }

        colNumVente.setCellValueFactory(new PropertyValueFactory<>("numVente"));
        colDate.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
            c.getValue().getDateVente() != null ? c.getValue().getDateVente().format(FORMAT_DATE) : ""
        ));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colMontant.setCellFactory(col -> new TableCell<Vente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });

        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutPayment"));
        colStatut.setCellFactory(col -> new TableCell<Vente, String>() {
            @Override
            protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(statut);
                    setStyle("Payé".equals(statut)
                        ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                        : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });

        tableHistorique.setItems(FXCollections.observableArrayList(ventes));
    }
}
