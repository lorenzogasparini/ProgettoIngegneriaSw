package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @FXML private Label statusLabel;

    @FXML private VBox VBoxFarmaco;
    @FXML private ComboBox comboBoxFarmaco;
    @FXML private int idFarmacoSelezionato;

    Terapia selectedTerapia = null;

    private MedicoDAO medicoDAO = MedicoDAO.getInstance();

    public void initialize() throws SQLException {
        setup();

        Farmaco[] farmaciAssegnati = medicoDAO.getFarmaci();

        ObservableList<Farmaco> farmaci = FXCollections.observableArrayList(comboBoxFarmaco.getItems());
        farmaci.addAll(farmaciAssegnati);

        comboBoxFarmaco.setItems(farmaci);
    }

    @FXML
    private void handleTerapiaUpdate() {
        tableViewTerapie.setOnMouseClicked(event -> {
            selectedTerapia = tableViewTerapie.getSelectionModel().getSelectedItem();
            if (selectedTerapia != null) {
                VBoxUpdate.setVisible(true);
                VBoxUpdate.setManaged(true);
                VBoxDosiGiornaliere.setVisible(true);
                VBoxQuantitaPerDose.setVisible(true);
                VBoxNote.setVisible(true);
                VBoxFarmaco.setVisible(true);
                VBoxFarmaco.setManaged(true);

                System.out.println("ID: " + selectedTerapia.getId());

                dosiGiornaliereUpdate.setText(selectedTerapia.getDosiGiornaliere().toString());
                quantitaPerDoseUpdate.setText(selectedTerapia.getQuantitaPerDose().toString());
                noteUpdate.setText(selectedTerapia.getNote());
            }
        });
    }

    private void setup() throws SQLException {
        dosiGiornaliere.setCellValueFactory(new PropertyValueFactory<Terapia, Integer>("dosiGiornaliere"));
        quantitaPerDose.setCellValueFactory(new PropertyValueFactory<Terapia, Double>("quantitaPerDose"));
        note.setCellValueFactory(new PropertyValueFactory<Terapia, String>("note"));
        Nome_farmaco.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));
        Codice_aic.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));

        medicoDAO.getUser(ViewNavigator.getAuthenticatedUser());
        Terapia[] terapie = medicoDAO.getTerapiePaziente(TestController.selectedUser.getUsername());

        ObservableList<Terapia> ter = FXCollections.observableArrayList(terapie);

        tableViewTerapie.setItems(ter);
    }

    @FXML
    private void handleUpdate() throws SQLException {
        //  gestire il lancio della query di update della terapia sulla base delle info fornite -> Implementare controlli
        MedicoDAO medicoDAO = MedicoDAO.getInstance();

        if(VBoxUpdate.isVisible()) {
            if((!dosiGiornaliereUpdate.getText().isEmpty()) && (!quantitaPerDoseUpdate.getText().isEmpty()) && (!noteUpdate.getText().isEmpty())) {
                medicoDAO.updateTerapiaPaziente(selectedTerapia, new Terapia(selectedTerapia.getId(), medicoDAO.getFaracoFromId(idFarmacoSelezionato), Integer.parseInt(dosiGiornaliereUpdate.getText()), Double.parseDouble(quantitaPerDoseUpdate.getText()), noteUpdate.getText()));
            }
            else {
                statusLabel.setVisible(true);
            }
        }
        setup();
    }

    @FXML
    private void onComboBoxFarmaco() throws SQLException {
        UserDAO userDao = UserDAO.getInstance();
        comboBoxFarmaco.setOnAction(e -> {
            String selezione = comboBoxFarmaco.getValue().toString();

            Pattern pattern = Pattern.compile("id:\\s*(\\d+)");
            Matcher matcher = pattern.matcher(selezione);

            if (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                idFarmacoSelezionato = id;
            }
        });
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