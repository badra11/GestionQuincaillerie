/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.bad.gestionquincaillerie.controllers;

import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Badra Aliou Dembele
 */
import com.bad.gestionquincaillerie.models.LigneVente;
import com.bad.gestionquincaillerie.models.ReçusVente;
import com.bad.gestionquincaillerie.models.Vente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;

public class ReçusVenteController {

    @FXML
    private Label lblNumRecu;
    @FXML
    private Label lblDateRecu;
    @FXML
    private Label lblNumVente;
    @FXML
    private Label lblClient;
    @FXML
    private TableView<LigneVente> tableLignes;
    @FXML
    private TableColumn<LigneVente, String> colProduit;
    @FXML
    private TableColumn<LigneVente, Integer> colQuantite;
    @FXML
    private TableColumn<LigneVente, Double> colPrixUnitaire;
    @FXML
    private TableColumn<LigneVente, Double> colTotal;
    @FXML
    private Label lblMontantTotal;
    @FXML
    private Label lblMontantRecu;
    @FXML
    private Label lblMontantRestant;

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private ReçusVente recuActuel;
    private Vente venteActuelle;
    private List<LigneVente> lignesActuelles;

    public void initData(ReçusVente recu, Vente vente, List<LigneVente> lignes) {
        this.recuActuel = recu;
        this.venteActuelle = vente;
        this.lignesActuelles = lignes;

        lblNumRecu.setText("Reçu N° : " + recu.getNumRecus());
        lblDateRecu.setText("Date : " + (recu.getDateRecus() != null ? recu.getDateRecus().format(FORMAT_DATE) : ""));
        lblNumVente.setText("Vente N° : " + vente.getNumVente());
        lblClient.setText("Client : " + vente.getNomClient());

        colProduit.setCellValueFactory(new PropertyValueFactory<>("designationProduit"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colPrixUnitaire.setCellFactory(col -> new TableCell<LigneVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrix"));
        colTotal.setCellFactory(col -> new TableCell<LigneVente, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });
        tableLignes.setItems(FXCollections.observableArrayList(lignes));

        lblMontantTotal.setText("Montant total : " + FormatUtilitaire.formaterMontant(vente.getMontantTotal()) + " FCFA");
        lblMontantRecu.setText("Montant reçu : " + vente.getMontantRecu() + " FCFA");

        if (vente.getMontantRestant() >= 0) {
            lblMontantRestant.setText("Monnaie rendue : " + FormatUtilitaire.formaterMontant(vente.getMontantTotal()) + " FCFA");
            lblMontantRestant.setStyle("-fx-text-fill: #27ae60;");
        } else {
            lblMontantRestant.setText("Reste à payer (créance) : " + FormatUtilitaire.formaterMontant(Math.abs(vente.getMontantRestant())) + " FCFA");
            lblMontantRestant.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void enregistrerPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le reçu en PDF");
        fileChooser.setInitialFileName("Reçu_" + recuActuel.getNumRecus() + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));

        File fichier = fileChooser.showSaveDialog(lblNumRecu.getScene().getWindow());
        if (fichier == null) {
            return; // l'utilisateur a annulé
        }
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A5);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float margeGauche = 40;
                float y = page.getMediaBox().getHeight() - 50;
                float interligne = 18;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(margeGauche, y);
                content.showText("QUINCAILLERIE");
                content.endText();
                y -= interligne + 5;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 11);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Reçu de vente");
                content.endText();
                y -= interligne + 10;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Reçu N° : " + recuActuel.getNumRecus());
                content.endText();
                y -= interligne;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Vente N° : " + venteActuelle.getNumVente());
                content.endText();
                y -= interligne;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Client : " + venteActuelle.getNomClient());
                content.endText();
                y -= interligne + 10;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 10);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Produit                Qté      P.Unit         Total");
                content.endText();
                y -= interligne;

                content.setFont(PDType1Font.HELVETICA, 9);
                for (LigneVente ligne : lignesActuelles) {
                    content.beginText();
                    content.newLineAtOffset(margeGauche, y);
                    String ligneTexte = String.format("%-22s %-8d %-10.0f %-10.0f",
                            ligne.getDesignationProduit(), ligne.getQuantite(),
                            ligne.getPrixUnitaire(), ligne.getTotalPrix());
                    content.showText(ligneTexte);
                    content.endText();
                    y -= interligne;
                }

                y -= 10;
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 11);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Montant total : " + venteActuelle.getMontantTotal() + " FCFA");
                content.endText();
                y -= interligne;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Montant reçu : " + venteActuelle.getMontantRecu() + " FCFA");
                content.endText();
                y -= interligne;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margeGauche, y);
                if (venteActuelle.getMontantRestant() >= 0) {
                    content.showText("Monnaie rendue : " + venteActuelle.getMontantRestant() + " FCFA");
                } else {
                    content.showText("Reste à payer (créance) : " + Math.abs(venteActuelle.getMontantRestant()) + " FCFA");
                }
                content.endText();
                y -= interligne + 15;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
                content.newLineAtOffset(margeGauche, y);
                content.showText("Merci pour votre confiance !");
                content.endText();
            }

            document.save(fichier);

            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Export réussi");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Le reçu a été enregistré avec succès :\n" + fichier.getAbsolutePath());
            confirmation.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert erreur = new Alert(Alert.AlertType.ERROR);
            erreur.setTitle("Erreur");
            erreur.setHeaderText(null);
            erreur.setContentText("Erreur lors de la génération du PDF : " + e.getMessage());
            erreur.showAndWait();
        }
    }

    @FXML
    private void fermer() {
        ((Stage) lblNumRecu.getScene().getWindow()).close();
    }
}
