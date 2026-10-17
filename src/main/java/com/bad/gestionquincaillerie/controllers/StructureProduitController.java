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

import com.bad.gestionquincaillerie.dao.CategorieProduitDAO;
import com.bad.gestionquincaillerie.dao.EmplacementDAO;
import com.bad.gestionquincaillerie.dao.TypeProduitDAO;
import com.bad.gestionquincaillerie.models.CategorieProduit;
import com.bad.gestionquincaillerie.models.Emplacement;
import com.bad.gestionquincaillerie.models.TypeProduit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class StructureProduitController implements Initializable {

    // ---- Catégorie ----
    @FXML private TextField txtNomCat;
    @FXML private TextArea txtDescCat;
    @FXML private Label lblMessageCat;
    @FXML private Button btnAjouterCat;
    @FXML private Button btnAnnulerCat;
    @FXML private Button btnSupprimerCat;
    @FXML private TableView<CategorieProduit> tableCategories;
    @FXML private TableColumn<CategorieProduit, Integer> colIdCat;
    @FXML private TableColumn<CategorieProduit, String> colNomCat;
    @FXML private TableColumn<CategorieProduit, String> colDescCat;

    // ---- Type ----
    @FXML private ComboBox<CategorieProduit> cbCategoriePourType;
    @FXML private TextField txtNomType;
    @FXML private TextArea txtDescType;
    @FXML private Label lblMessageType;
    @FXML private Button btnAjouterType;
    @FXML private Button btnAnnulerType;
    @FXML private Button btnSupprimerType;
    @FXML private TableView<TypeProduit> tableTypes;
    @FXML private TableColumn<TypeProduit, Integer> colIdType;
    @FXML private TableColumn<TypeProduit, String> colCategorieType;
    @FXML private TableColumn<TypeProduit, String> colNomType;
    @FXML private TableColumn<TypeProduit, String> colDescType;

    // ---- Emplacement ----
    @FXML private TextField txtTypeEmplacement;
    @FXML private TextField txtDesignationEmplacement;
    @FXML private Label lblMessageEmplacement;
    @FXML private Button btnAjouterEmplacement;
    @FXML private Button btnAnnulerEmplacement;
    @FXML private Button btnSupprimerEmplacement;
    @FXML private TableView<Emplacement> tableEmplacements;
    @FXML private TableColumn<Emplacement, Integer> colIdEmplacement;
    @FXML private TableColumn<Emplacement, String> colTypeEmplacement;
    @FXML private TableColumn<Emplacement, String> colDesignationEmplacement;

    private final CategorieProduitDAO categorieDAO = new CategorieProduitDAO();
    private final TypeProduitDAO typeDAO = new TypeProduitDAO();
    private final EmplacementDAO emplacementDAO = new EmplacementDAO();
    
     // Suivi du mode édition pour chaque section (null = mode ajout)
    private CategorieProduit categorieEnEdition = null;
    private TypeProduit typeEnEdition = null;
    private Emplacement emplacementEnEdition = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbCategoriePourType.setEditable(true);
        cbCategoriePourType.setConverter(new javafx.util.StringConverter<CategorieProduit>() {
            @Override
            public String toString(CategorieProduit c) {
                return c != null ? c.getNomCat() : "";
            }

            @Override
            public CategorieProduit fromString(String s) {
                return cbCategoriePourType.getItems().stream()
                    .filter(c -> c.getNomCat().equalsIgnoreCase(s))
                    .findFirst()
                    .orElse(null);
            }
        });
        cbCategoriePourType.getEditor().textProperty().addListener((obs, ancien, texte) -> {
            if (cbCategoriePourType.getValue() != null && cbCategoriePourType.getValue().getNomCat().equals(texte)) return;

            ObservableList<CategorieProduit> toutesLesCategories = FXCollections.observableArrayList(categorieDAO.getAll());

            if (texte == null || texte.isEmpty()) {
                cbCategoriePourType.setItems(toutesLesCategories);
                return;
            }

            ObservableList<CategorieProduit> filtres = toutesLesCategories.stream()
                .filter(c -> c.getNomCat().toLowerCase().contains(texte.toLowerCase()))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
            cbCategoriePourType.setItems(filtres);
            if (!cbCategoriePourType.isShowing()) cbCategoriePourType.show();
        });
        
        
        
        // Configuration des colonnes - Catégorie
        colIdCat.setCellValueFactory(new PropertyValueFactory<>("idCat"));
        colNomCat.setCellValueFactory(new PropertyValueFactory<>("nomCat"));
        colNomCat.setCellFactory(col -> new TableCell<CategorieProduit, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colDescCat.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Configuration des colonnes - Type
        colIdType.setCellValueFactory(new PropertyValueFactory<>("idType"));
        colCategorieType.setCellValueFactory(new PropertyValueFactory<>("nomCat"));
        colCategorieType.setCellFactory(col -> new TableCell<TypeProduit, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colNomType.setCellValueFactory(new PropertyValueFactory<>("nomType"));
        colNomType.setCellFactory(col -> new TableCell<TypeProduit, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colDescType.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Configuration des colonnes - Emplacement
        colIdEmplacement.setCellValueFactory(new PropertyValueFactory<>("idEmplacement"));
        colTypeEmplacement.setCellValueFactory(new PropertyValueFactory<>("typeEmplacement"));
        colTypeEmplacement.setCellFactory(col -> new TableCell<Emplacement, String>() {
            @Override
            protected void updateItem(String valeur, boolean empty) {
                super.updateItem(valeur, empty);
                setText(empty || valeur == null ? null : valeur.toUpperCase());
            }
        });
        colDesignationEmplacement.setCellValueFactory(new PropertyValueFactory<>("designationEmplacement"));

        chargerCategories();
        chargerTypes();
        chargerEmplacements();
        
        // ===== Clic sur une ligne : remplir le formulaire pour modification =====
        tableCategories.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                categorieEnEdition = selection;
                txtNomCat.setText(selection.getNomCat());
                txtDescCat.setText(selection.getDescription());
                btnAjouterCat.setText("Modifier la catégorie");
                btnAnnulerCat.setVisible(true);
                btnAnnulerCat.setManaged(true);
                btnSupprimerCat.setVisible(true);
                btnSupprimerCat.setManaged(true);
            }
        });
        tableTypes.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                typeEnEdition = selection;
                txtNomType.setText(selection.getNomType());
                txtDescType.setText(selection.getDescription());
                // Sélectionne la bonne catégorie dans le ComboBox
                for (CategorieProduit c : cbCategoriePourType.getItems()) {
                    if (c.getIdCat() == selection.getIdCat()) {
                        cbCategoriePourType.setValue(c);
                        break;
                    }
                }
                btnAjouterType.setText("Modifier le type");
                btnAnnulerType.setVisible(true);
                btnAnnulerType.setManaged(true);
                btnSupprimerType.setVisible(true);
                btnSupprimerType.setManaged(true);
            }
        });
          tableEmplacements.getSelectionModel().selectedItemProperty().addListener((obs, ancien, selection) -> {
            if (selection != null) {
                emplacementEnEdition = selection;
                txtTypeEmplacement.setText(selection.getTypeEmplacement());
                txtDesignationEmplacement.setText(selection.getDesignationEmplacement());
                btnAjouterEmplacement.setText("Modifier l'emplacement");
                btnAnnulerEmplacement.setVisible(true);
                btnAnnulerEmplacement.setManaged(true);
                btnSupprimerEmplacement.setVisible(true);
                btnSupprimerEmplacement.setManaged(true);
                
            }
        });
    }
    
    //==================== le message =================================
    private void afficherMessage(Label label, String texte, boolean succes) {
        label.setText(texte);
        label.setStyle(succes ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    // ================= CATEGORIE =================

    private void chargerCategories() {
        ObservableList<CategorieProduit> liste = FXCollections.observableArrayList(categorieDAO.getAll());
        tableCategories.setItems(liste);
        cbCategoriePourType.setItems(liste); // alimente aussi le ComboBox du formulaire Type
    }

    @FXML
    private void ajouterCategorie() {
        String nom = txtNomCat.getText().trim();
        String description = txtDescCat.getText().trim();

        if (nom.isEmpty()) {
            afficherMessage(lblMessageCat, "Le nom de la catégorie est obligatoire.", false);
            return;
        }
        
        boolean succes;
        
         if (categorieEnEdition != null) {
            // ===== Mode modification =====
            categorieEnEdition.setNomCat(nom);
            categorieEnEdition.setDescription(description);
            succes = categorieDAO.modifier(categorieEnEdition);
            if (succes) afficherMessage(lblMessageCat, "Catégorie modifiée avec succès.", true);
        } else {
            // ===== Mode ajout =====
            CategorieProduit categorie = new CategorieProduit();
            categorie.setNomCat(nom);
            categorie.setDescription(description);
            succes = categorieDAO.ajouter(categorie);
            if (succes) afficherMessage(lblMessageCat, "Catégorie ajoutée avec succès.", true);
        }

        if (succes) {
            annulerCategorie(); // réinitialise le formulaire et le mode
            chargerCategories();
        } else {
            afficherMessage(lblMessageCat, "Erreur lors de l'opération.", false);
        }
    }
    
    @FXML
    private void annulerCategorie() {
        categorieEnEdition = null;
        txtNomCat.clear();
        txtDescCat.clear();
        btnAjouterCat.setText("Ajouter la catégorie");
        btnAnnulerCat.setVisible(false);
        btnAnnulerCat.setManaged(false);
        btnSupprimerCat.setVisible(false);
        btnSupprimerCat.setManaged(false);
        tableCategories.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void supprimerCategorie() {
        if (categorieEnEdition == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer la catégorie \"" + categorieEnEdition.getNomCat() + "\" ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean succes = categorieDAO.supprimer(categorieEnEdition.getIdCat());
                if (succes) {
                    afficherMessage(lblMessageCat, "Catégorie supprimée avec succès.", true);
                    annulerCategorie();
                    chargerCategories();
                } else {
                    afficherMessage(lblMessageCat, "Impossible de supprimer : cette catégorie est peut-être utilisée par un type existant.", false);
                }
            }
        });
    }

    // ================= TYPE =================

    private void chargerTypes() {
        ObservableList<TypeProduit> liste = FXCollections.observableArrayList(typeDAO.getAll());
        tableTypes.setItems(liste);
    }

    @FXML
    private void ajouterType() {
        CategorieProduit categorieSelectionnee = cbCategoriePourType.getValue();
        String nomType = txtNomType.getText().trim();
        String description = txtDescType.getText().trim();

        if (categorieSelectionnee == null) {
            afficherMessage(lblMessageType, "Veuillez sélectionner une catégorie.", false);
            return;
        }
        boolean succes;

        if (typeEnEdition != null) {
            typeEnEdition.setIdCat(categorieSelectionnee.getIdCat());
            typeEnEdition.setNomType(nomType);
            typeEnEdition.setDescription(description);
            succes = typeDAO.modifier(typeEnEdition);
            if (succes) afficherMessage(lblMessageType, "Type modifié avec succès.", true);
        } else {
            TypeProduit type = new TypeProduit();
            type.setIdCat(categorieSelectionnee.getIdCat());
            type.setNomType(nomType);
            type.setDescription(description);
            succes = typeDAO.ajouter(type);
            if (succes) afficherMessage(lblMessageType, "Type ajouté avec succès.", true);
        }

        if (succes) {
            annulerType();
            chargerTypes();
        } else {
            afficherMessage(lblMessageType, "Erreur lors de l'opération.", false);
        }
    }
    @FXML
    private void annulerType() {
        typeEnEdition = null;
        txtNomType.clear();
        txtDescType.clear();
        cbCategoriePourType.setValue(null);
        btnAjouterType.setText("Ajouter le type");
        btnAnnulerType.setVisible(false);
        btnAnnulerType.setManaged(false);
        btnSupprimerType.setVisible(false);
        btnSupprimerType.setManaged(false);
        tableTypes.getSelectionModel().clearSelection();
    }
    @FXML
    private void supprimerType() {
        if (typeEnEdition == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer le type \"" + typeEnEdition.getNomType() + "\" ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean succes = typeDAO.supprimer(typeEnEdition.getIdType());
                if (succes) {
                    afficherMessage(lblMessageType, "Type supprimé avec succès.", true);
                    annulerType();
                    chargerTypes();
                } else {
                    afficherMessage(lblMessageType, "Impossible de supprimer : ce type est peut-être utilisé par un produit existant.", false);
                }
            }
        });
    }
    

    // ================= EMPLACEMENT =================

    private void chargerEmplacements() {
        ObservableList<Emplacement> liste = FXCollections.observableArrayList(emplacementDAO.getAll());
        tableEmplacements.setItems(liste);
    }

    @FXML
    private void ajouterEmplacement() {
        String type = txtTypeEmplacement.getText().trim();
        String designation = txtDesignationEmplacement.getText().trim();

        if (type.isEmpty() || designation.isEmpty()) {
            afficherMessage(lblMessageEmplacement, "Tous les champs sont obligatoires.", false);
            return;
        }
  boolean succes;

        if (emplacementEnEdition != null) {
            emplacementEnEdition.setTypeEmplacement(type);
            emplacementEnEdition.setDesignationEmplacement(designation);
            succes = emplacementDAO.modifier(emplacementEnEdition);
            if (succes) afficherMessage(lblMessageEmplacement, "Emplacement modifié avec succès.", true);
        } else {
            Emplacement emplacement = new Emplacement();
            emplacement.setTypeEmplacement(type);
            emplacement.setDesignationEmplacement(designation);
            succes = emplacementDAO.ajouter(emplacement);
            if (succes) afficherMessage(lblMessageEmplacement, "Emplacement ajouté avec succès.", true);
        }

        if (succes) {
            annulerEmplacement();
            chargerEmplacements();
        } else {
            afficherMessage(lblMessageEmplacement, "Erreur lors de l'opération.", false);
        }
    }
    @FXML
    private void annulerEmplacement() {
        emplacementEnEdition = null;
        txtTypeEmplacement.clear();
        txtDesignationEmplacement.clear();
        btnAjouterEmplacement.setText("Ajouter l'emplacement");
        btnAnnulerEmplacement.setVisible(false);
        btnAnnulerEmplacement.setManaged(false);
        btnSupprimerEmplacement.setVisible(false);
        btnSupprimerEmplacement.setVisible(false);
        tableEmplacements.getSelectionModel().clearSelection();
    }
    @FXML
    private void supprimerEmplacement() {
        if (emplacementEnEdition == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer l'emplacement \"" + emplacementEnEdition.getDesignationEmplacement() + "\" ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                boolean succes = emplacementDAO.supprimer(emplacementEnEdition.getIdEmplacement());
                if (succes) {
                    afficherMessage(lblMessageEmplacement, "Emplacement supprimé avec succès.", true);
                    annulerEmplacement();
                    chargerEmplacements();
                } else {
                    afficherMessage(lblMessageEmplacement, "Impossible de supprimer : cet emplacement est peut-être utilisé par un produit existant.", false);
                }
            }
        });
    }
    
}
