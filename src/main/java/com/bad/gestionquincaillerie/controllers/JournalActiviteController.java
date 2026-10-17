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

import com.bad.gestionquincaillerie.dao.JournalActiviteDAO;
import com.bad.gestionquincaillerie.models.JournalActivite;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class JournalActiviteController implements Initializable {

    @FXML private TableView<JournalActivite> tableJournal;
    @FXML private TableColumn<JournalActivite, String> colDate;
    @FXML private TableColumn<JournalActivite, String> colEmploye;
    @FXML private TableColumn<JournalActivite, String> colAction;
    @FXML private Button btnActualiser;

    private final JournalActiviteDAO journalDAO = new JournalActiviteDAO();
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colDate.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(
            c.getValue().getDateAction() != null ? c.getValue().getDateAction().format(FORMAT_DATE) : ""
        ));
        colEmploye.setCellValueFactory(new PropertyValueFactory<>("nomEmploye"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        chargerJournal();
    }

    @FXML
    private void actualiser() {
        chargerJournal();
    }

    private void chargerJournal() {
        tableJournal.setItems(FXCollections.observableArrayList(journalDAO.getAll()));
    }
}
