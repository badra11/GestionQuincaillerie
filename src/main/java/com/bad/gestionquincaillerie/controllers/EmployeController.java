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

import com.bad.gestionquincaillerie.dao.EmployeDAO;
import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import com.bad.gestionquincaillerie.models.Employe;
import com.bad.gestionquincaillerie.models.FormatUtilitaire;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

public class EmployeController implements Initializable {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtTelephone;
    @FXML private ComboBox<String> cbRole;
    @FXML private DatePicker dateEmbauche;
    @FXML private TextField txtSalaire;

    @FXML private VBox cardUtilisateur;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;
    @FXML private Button btnSupprimer;
    @FXML private Label lblMessage;

    @FXML private TableView<Employe> tableEmployes;
    @FXML private TableColumn<Employe, String> colMatricule;
    @FXML private TableColumn<Employe, String> colNom;
    @FXML private TableColumn<Employe, String> colPrenom;
    @FXML private TableColumn<Employe, String> colTelephone;
    @FXML private TableColumn<Employe, String> colRole;
    @FXML private TableColumn<Employe, String> colDateEmbauche;
    @FXML private TableColumn<Employe, Double> colSalaire;
    @FXML private TableColumn<Employe, String> colUsername;

    private final EmployeDAO employeDAO = new EmployeDAO();
    private Employe employeEnEdition = null;
    private Employe employeConnecte ;

    private static final String[] ROLES = {"Admin", "User", "Vendeur", "Caissier", "Magasinier", "Gérant"};
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbRole.setItems(FXCollections.observableArrayList(ROLES));

        // Affiche/masque la section utilisateur selon le rôle sélectionné
        cbRole.valueProperty().addListener((obs, ancien, role) -> {
            boolean estUtilisateur = "Admin".equalsIgnoreCase(role) || "User".equalsIgnoreCase(role);
            cardUtilisateur.setVisible(estUtilisateur);
            cardUtilisateur.setManaged(estUtilisateur);
            if (!estUtilisateur) {
                txtUsername.clear();
                txtPassword.clear();
            }
        });

        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colMatricule.setCellFactory(col -> new TableCell<Employe, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setCellFactory(col -> new TableCell<Employe, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
         
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colPrenom.setCellFactory(col -> new TableCell<Employe, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
         
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colRole.setCellFactory(col -> new TableCell<Employe, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });

        colDateEmbauche.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getDateEmbauche() != null ? c.getValue().getDateEmbauche().format(FORMAT_DATE) : ""
        ));

        colSalaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        colSalaire.setCellFactory(col -> new TableCell<Employe, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                setText(empty || montant == null ? null : FormatUtilitaire.formaterMontant(montant));
            }
        });

        colUsername.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getUsername() != null ? c.getValue().getUsername() : "—"
        ));
        colUsername.setCellFactory(col -> new TableCell<Employe, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });

        chargerEmployes();

        tableEmployes.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                employeEnEdition = selection;
                txtNom.setText(selection.getNom());
                txtPrenom.setText(selection.getPrenom());
                txtTelephone.setText(selection.getTelephone());
                cbRole.setValue(selection.getRole());
                dateEmbauche.setValue(selection.getDateEmbauche());
                txtSalaire.setText(String.valueOf(selection.getSalaire()));
                txtUsername.setText(selection.getUsername() != null ? selection.getUsername() : "");
                txtPassword.setText(selection.getPassword() != null ? selection.getPassword() : "");

                btnAjouter.setText("Modifier l'employé");
                btnAnnuler.setVisible(true);
                btnAnnuler.setManaged(true);
                btnSupprimer.setVisible(true);
                btnSupprimer.setManaged(true);
            }
        });
    }

    private void chargerEmployes() {
        tableEmployes.setItems(FXCollections.observableArrayList(employeDAO.getAll()));
    }

    @FXML
    private void ajouterEmploye() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String role = cbRole.getValue();
        LocalDate date = dateEmbauche.getValue();
        String salaireTexte = txtSalaire.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || role == null || date == null || salaireTexte.isEmpty()) {
            afficherMessage("Tous les champs employé sont obligatoires.", false);
            return;
        }

        double salaire;
        try {
            salaire = Double.parseDouble(salaireTexte);
        } catch (NumberFormatException e) {
            afficherMessage("Le salaire doit être un nombre valide.", false);
            return;
        }

        boolean estUtilisateur = "Admin".equalsIgnoreCase(role) || "User".equalsIgnoreCase(role);
        String username = null;
        String password = null;

        if (estUtilisateur) {
            username = txtUsername.getText().trim();
            password = txtPassword.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                afficherMessage("Nom d'utilisateur et mot de passe obligatoires pour ce rôle.", false);
                return;
            }
        }

        if (employeEnEdition != null) {
            employeEnEdition.setNom(nom);
            employeEnEdition.setPrenom(prenom);
            employeEnEdition.setTelephone(telephone);
            employeEnEdition.setRole(role);
            employeEnEdition.setDateEmbauche(date);
            employeEnEdition.setSalaire(salaire);
            employeEnEdition.setUsername(username);
            employeEnEdition.setPassword(password);

            boolean succes = employeDAO.modifier(employeEnEdition);
            if (succes) {
                afficherMessage("Employé modifié avec succès.", true);
                annulerEmploye();
                chargerEmployes();
            } else {
                afficherMessage("Erreur lors de la modification.", false);
            }
        } else {
            Employe employe = new Employe();
            employe.setNom(nom);
            employe.setPrenom(prenom);
            employe.setTelephone(telephone);
            employe.setRole(role);
            employe.setDateEmbauche(date);
            employe.setSalaire(salaire);
            employe.setUsername(username);
            employe.setPassword(password);

            Employe resultat = employeDAO.ajouter(employe);
            if (resultat != null) {
                afficherMessage("Employé ajouté avec succès. Matricule : " + resultat.getMatricule(), true);
                annulerEmploye();
                chargerEmployes();
                new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Ajout d'employe N°: " + resultat.getMatricule() + " "+ resultat.getPrenom() + " "+ resultat.getNom());
            } else {
                afficherMessage("Erreur lors de l'ajout.", false);
            }
        }
    }

    @FXML
    private void supprimerEmploye() {
        if (employeEnEdition == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer " + employeEnEdition.getPrenom() + " " + employeEnEdition.getNom() + " ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean succes = employeDAO.supprimer(employeEnEdition.getId());
                if (succes) {
                    afficherMessage("Employé supprimé avec succès.", true);
                    annulerEmploye();
                    chargerEmployes();
                    new JournalActiviteDAO().enregistrer(employeConnecte.getId(), "Suppression de l'employe :" + employeEnEdition.getMatricule()+ " "+ employeEnEdition.getPrenom() + " "+ employeEnEdition.getNom());
                } else {
                    afficherMessage("Impossible de supprimer cet employé.", false);
                }
            }
        });
    }

    @FXML
    private void annulerEmploye() {
        employeEnEdition = null;
        txtNom.clear();
        txtPrenom.clear();
        txtTelephone.clear();
        cbRole.setValue(null);
        dateEmbauche.setValue(null);
        txtSalaire.clear();
        txtUsername.clear();
        txtPassword.clear();
        cardUtilisateur.setVisible(false);
        cardUtilisateur.setManaged(false);
        btnAjouter.setText("Ajouter l'employé");
        btnAnnuler.setVisible(false);
        btnAnnuler.setManaged(false);
        btnSupprimer.setVisible(false);
        btnSupprimer.setManaged(false);
        tableEmployes.getSelectionModel().clearSelection();
    }

    private void afficherMessage(String texte, boolean succes) {
        lblMessage.setText(texte);
        lblMessage.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
