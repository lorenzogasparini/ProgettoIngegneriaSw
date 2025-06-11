package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Utils.Terapia;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class TerapieHandlingController {
    @FXML private TableView<Terapia> tableViewTerapie;
    @FXML private TableColumn<Terapia, Integer> dosiGiornaliere;
    @FXML private TableColumn<Terapia, Double> quantitaPerDose;
    @FXML private TableColumn<Terapia, String> note;
    @FXML private TableColumn<Terapia, String> Nome_farmaco;
    @FXML private TableColumn<Terapia, String> Codice_aic;

    @FXML private VBox VBoxUpdate;

    @FXML private VBox VBoxDosiGiornaliere;
    @FXML private TextField dosiGiornaliereUpdate;

    @FXML private VBox VBoxQuantitaPerDose;
    @FXML private TextField quantitaPerDoseUpdate;

    @FXML private VBox VBoxNote;
    @FXML private TextField noteUpdate;

    public void initialize() throws SQLException {
        dosiGiornaliere.setCellValueFactory(new PropertyValueFactory<Terapia, Integer>("dosiGiornaliere"));
        quantitaPerDose.setCellValueFactory(new PropertyValueFactory<Terapia, Double>("quantitaPerDose"));
        note.setCellValueFactory(new PropertyValueFactory<Terapia, String>("note"));
        Nome_farmaco.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));
        Codice_aic.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        medicoDAO.getUser(ViewNavigator.getAuthenticatedUser());
        Terapia[] terapie = medicoDAO.getTerapiePaziente(TestController.selectedUser.getUsername());

        ObservableList<Terapia> ter = FXCollections.observableArrayList(terapie);

        tableViewTerapie.setItems(ter);
    }

    @FXML
    private void handleTerapiaUpdate() {
        tableViewTerapie.setOnMouseClicked(event -> {
            // Per  la riga cliccata:
            Terapia selectedTerapia = tableViewTerapie.getSelectionModel().getSelectedItem();
            if (selectedTerapia != null) {
                VBoxUpdate.setVisible(true);
                VBoxUpdate.setManaged(true);
                VBoxDosiGiornaliere.setVisible(true);
                VBoxQuantitaPerDose.setVisible(true);
                VBoxNote.setVisible(true);

                dosiGiornaliereUpdate.setText(selectedTerapia.getDosiGiornaliere().toString());
                quantitaPerDoseUpdate.setText(selectedTerapia.getQuantitaPerDose().toString());
                noteUpdate.setText(selectedTerapia.getNote());
            }
        });
    }

    @FXML
    private void handleUpdate() {
        //  gestire il lancio della query di update della terapia sulla base delle info fornite -> Implementare controlli
    }

    @FXML
    private void handleLogin() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToLogin();
    }

    @FXML
    private void handleRegister() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToRegister();
    }

    @FXML
    private void handleDashboard() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToDashboard();
    }
}