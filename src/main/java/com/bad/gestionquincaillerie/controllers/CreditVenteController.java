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

import com.bad.gestionquincaillerie.dao.CreditVenteDAO;
import com.bad.gestionquincaillerie.models.CreditVente;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import com.bad.gestionquincaillerie.models.LigneCommande;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreditVenteController implements Initializable {

    @FXML private TableView<CreditVente> tableCredits;
    @FXML private TableColumn<CreditVente, String> colNumCredit;
    @FXML private TableColumn<CreditVente, String> colClient;
    @FXML private TableColumn<CreditVente, String> colNumVente;
    @FXML private TableColumn<CreditVente, Double> colMontantCredit;
    @FXML private TableColumn<CreditVente, Double> colMontantPaye;
    @FXML private TableColumn<CreditVente, Double> colMontantReste;
    @FXML private TableColumn<CreditVente, Boolean> colStatut;
    @FXML private TableColumn<CreditVente, Void> colAction;

    private final CreditVenteDAO creditVenteDAO = new CreditVenteDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNumCredit.setCellValueFactory(new PropertyValueFactory<>("numCredit"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colClient.setCellFactory(col -> new TableCell<CreditVente, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colNumVente.setCellValueFactory(new PropertyValueFactory<>("numVente"));
        colMontantCredit.setCellValueFactory(new PropertyValueFactory<>("montantCredit"));
        colMontantCredit.setCellFactory(col -> new TableCell<CreditVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant) + " Fcfa");
            }
        });
        colMontantPaye.setCellValueFactory(new PropertyValueFactory<>("montantPaye"));
        colMontantPaye.setCellFactory(col -> new TableCell<CreditVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant) + " Fcfa");
            }
        });
        colMontantReste.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(
            c.getValue().getMontantCredit() - c.getValue().getMontantPaye()
        ).asObject());
        colMontantReste.setCellFactory(col -> new TableCell<CreditVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant) + " Fcfa");
            }
        });

        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setCellFactory(col -> new TableCell<CreditVente, Boolean>() {
            @Override
            protected void updateItem(Boolean solde, boolean empty) {
                super.updateItem(solde, empty);
                if (empty || solde == null) {
                    setText(null);
                } else {
                    setText(solde ? "Soldé" : "En cours");
                    setStyle(solde ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;" : "-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                }
            }
        });

        colAction.setCellFactory(col -> new TableCell<CreditVente, Void>() {
            private final Button btnRegler = new Button("Régler");
            {
                btnRegler.getStyleClass().add("btn-stocker");
                btnRegler.setOnAction(event -> {
                    CreditVente credit = getTableView().getItems().get(getIndex());
                    ouvrirFenetreReglement(credit);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CreditVente credit = getTableView().getItems().get(getIndex());
                    setGraphic(credit.isStatut() ? null : btnRegler);
                }
            }
        });

        chargerCredits();
    }

    private void chargerCredits() {
        tableCredits.setItems(FXCollections.observableArrayList(creditVenteDAO.getAll()));
    }

    private void ouvrirFenetreReglement(CreditVente credit) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/bad/gestionquincaillerie/views/ReglerCredit.fxml")
            );
            Parent root = loader.load();

            ReglerCreditController controller = loader.getController();
            controller.initData(credit, this::chargerCredits); // rafraîchit la table après règlement

            Stage stage = new Stage();
            stage.setTitle("Règlement du crédit " + credit.getNumCredit());
            stage.setScene(new Scene(root, 450, 300));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
