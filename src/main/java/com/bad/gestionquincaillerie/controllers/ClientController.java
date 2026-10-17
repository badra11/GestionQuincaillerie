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
import com.bad.gestionquincaillerie.dao.ClientDAO;
import com.bad.gestionquincaillerie.dao.CreditVenteDAO;
import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import com.bad.gestionquincaillerie.models.Client;
import com.bad.gestionquincaillerie.models.CreditVente;
import com.bad.gestionquincaillerie.models.Employe;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import com.bad.gestionquincaillerie.dao.VenteDAO;
import com.bad.gestionquincaillerie.models.Vente;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import java.io.IOException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.time.format.DateTimeFormatter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientController implements Initializable {

    @FXML
    private ComboBox<String> cbTypeClient;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelephone;
    @FXML
    private Label lblCreance;
    @FXML
    private TextField txtAdresse;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnAnnuler;
    @FXML
    private Button btnSupprimer;

    @FXML
    private TableView<Client> tableClients;
    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colTypeClient;
    @FXML
    private TableColumn<Client, String> colNom;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private TableColumn<Client, String> colTelephone;
    @FXML
    private TableColumn<Client, String> colAdresse;
    @FXML
    private TableColumn<Client, Boolean> colCreance;
    @FXML
    private Button btnHistorique;

    
    private final VenteDAO venteDAO = new VenteDAO();
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final ClientDAO clientDAO = new ClientDAO();
    private final CreditVenteDAO creditVenteDAO = new CreditVenteDAO();
    private Client clientEnEdition = null;
    private Employe employeConnecte;

    private static final String[] TYPES_CLIENT = {"Particulier", "Entreprise", "Détaillant", "Revendeur", "Grossiste"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTypeClient.setItems(FXCollections.observableArrayList(TYPES_CLIENT));

        colId.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        colTypeClient.setCellValueFactory(new PropertyValueFactory<>("typeClient"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setCellFactory(col -> new TableCell<Client, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colAdresse.setCellFactory(col -> new TableCell<Client, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colCreance.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(c.getValue().aUneCreance()));
        colCreance.setCellFactory(col -> new TableCell<Client, Boolean>() {
            @Override
            protected void updateItem(Boolean creance, boolean empty) {
                super.updateItem(creance, empty);
                if (empty || creance == null) {
                    setText(null);
                } else {
                    setText(creance ? "Oui" : "Non");
                    setStyle(creance ? "-fx-text-fill: #e74c3c; -fx-font-weight: bold;" : "-fx-text-fill: #27ae60;");
                }
            }
        });

        chargerClients();
 
        tableClients.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                clientEnEdition = selection;
                cbTypeClient.setValue(selection.getTypeClient());
                txtNom.setText(selection.getNom());
                txtEmail.setText(selection.getEmail());
                txtTelephone.setText(selection.getTelephone());
                txtAdresse.setText(selection.getAdresse());

                // ===== Affichage de la créance totale (somme de tous les crédits actifs) =====
                if (creditVenteDAO.aDesCreditsActifs(selection.getIdClient())) {
                    double totalCreance = creditVenteDAO.getMontantCreanceTotaleParClient(selection.getIdClient());
                    lblCreance.setText("Créance totale en cours : " + FormatUtilitaire.formaterMontant(totalCreance) + " FCFA");
                    lblCreance.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                } else {
                    lblCreance.setText("Aucune créance");
                    lblCreance.setStyle("-fx-text-fill: #27ae60;");
                }

              
                btnAjouter.setText("Modifier le client");
                btnAnnuler.setVisible(true);
                btnAnnuler.setManaged(true);
                btnSupprimer.setVisible(true);
                btnSupprimer.setManaged(true);
                btnHistorique.setVisible(true);
                btnHistorique.setManaged(true);
            }
        });
    }

    private void chargerClients() {
        tableClients.setItems(FXCollections.observableArrayList(clientDAO.getAll()));
    }

    @FXML
    private void ajouterClient() {
        String type = cbTypeClient.getValue();
        String nom = txtNom.getText().trim();
        String email = txtEmail.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();

        if (type == null || nom.isEmpty() || telephone.isEmpty() || adresse.isEmpty()) {
            afficherMessage("Tous les champs sont obligatoires.", false);
            return;
        }

        boolean succes;

        if (clientEnEdition != null) {
            clientEnEdition.setTypeClient(type);
            clientEnEdition.setNom(nom);
            clientEnEdition.setEmail(email);
            clientEnEdition.setTelephone(telephone);
            clientEnEdition.setAdresse(adresse);
            succes = clientDAO.modifier(clientEnEdition);
            if (succes) {
                afficherMessage("Client modifié avec succès.", true);
            }
        } else {
            Client client = new Client();
            client.setTypeClient(type);
            client.setNom(nom);
            client.setEmail(email);
            client.setTelephone(telephone);
            client.setAdresse(adresse);
            succes = clientDAO.ajouter(client);
            if (succes) {
                afficherMessage("Client ajouté avec succès.", true);
            }
        }

        if (succes) {
            annulerClient();
            chargerClients();
            new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Ajout de client : " + nom.toUpperCase());
        } else {
            afficherMessage("Erreur lors de l'opération.", false);
        }
    }

    @FXML
    private void supprimerClient() {
        if (clientEnEdition == null) {
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer \"" + clientEnEdition.getNom() + "\" ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean succes = clientDAO.supprimer(clientEnEdition.getIdClient());
                if (succes) {
                    afficherMessage("Client supprimé avec succès.", true);
                    annulerClient();
                    chargerClients();
                    new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Suppression du client : " + clientEnEdition.getNom());
                } else {
                    afficherMessage("Impossible de supprimer : ce client a peut-être des ventes ou créances liées.", false);
                }
            }
        });
    }

    @FXML
    private void annulerClient() {
        clientEnEdition = null;
        cbTypeClient.setValue(null);
        txtNom.clear();
        txtEmail.clear();
        txtTelephone.clear();
        txtAdresse.clear();
        lblCreance.setText("");
        btnAjouter.setText("Ajouter le client");
        btnAnnuler.setVisible(false);
        btnAnnuler.setManaged(false);
        btnSupprimer.setVisible(false);
        btnSupprimer.setManaged(false);
        btnHistorique.setVisible(false);
        btnHistorique.setManaged(false);
        tableClients.getSelectionModel().clearSelection();
    }

    @FXML
    private void voirHistorique() {
        if (clientEnEdition == null) {
            return;
        }

        List<Vente> ventes = venteDAO.getVentesParClient(clientEnEdition.getIdClient());
        double creanceTotale = creditVenteDAO.getMontantCreanceTotaleParClient(clientEnEdition.getIdClient());

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bad/gestionquincaillerie/views/HistoriqueClient.fxml")
            );
            Parent root = loader.load();

            HistoriqueClientController controller = loader.getController();
            controller.initData(clientEnEdition, ventes, creanceTotale);

            Stage stage = new Stage();
            stage.setTitle("Historique de " + clientEnEdition.getNom());
            stage.setScene(new Scene(root, 650, 500));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void afficherMessage(String texte, boolean succes) {
        lblMessage.setText(texte);
        lblMessage.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
