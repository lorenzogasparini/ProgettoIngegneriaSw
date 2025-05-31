package com.progettoingegneriasw.controller;


import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.text.View;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserHandlingController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;

    @FXML private TableView<Terapia> tableViewTerapie;
    @FXML private TableColumn<Terapia, Integer> dosiGiornaliere;
    @FXML private TableColumn<Terapia, Double> quantitaPerDose;
    @FXML private TableColumn<Terapia, String> note;
    @FXML private TableColumn<Terapia, String> Nome_farmaco;
    @FXML private TableColumn<Terapia, String> Codice_aic;

    @FXML private TableView<RilevazioneFarmaco> tableViewRilevazioniFarmaci;
    @FXML private TableColumn<RilevazioneFarmaco, Timestamp> timestamp;
    @FXML private TableColumn<RilevazioneFarmaco, String> codiceAic;
    @FXML private TableColumn<RilevazioneFarmaco, String> nome;
    @FXML private TableColumn<RilevazioneFarmaco, Double> quantita;
    @FXML private TableColumn<RilevazioneFarmaco, String> noteRilevazione;

    @FXML private TableView<RilevazioneGlicemia> tableViewRilevazioniGlicemia;
    //  @FXML private TableColumn<RilevazioneGlicemia, Timestamp> timestamp;
    @FXML private TableColumn<RilevazioneGlicemia, Integer> valore;
    @FXML private TableColumn<RilevazioneGlicemia, Integer> gravita;
    @FXML private TableColumn<RilevazioneGlicemia, Boolean> primaPasto;

    @FXML private TableView<RilevazioneSintomo> tableViewRilevazioniSintomi;
    //  @FXML private TableColumn<RilevazioneSintomo, Timestamp> timestamp;
    @FXML private TableColumn<RilevazioneSintomo, String> sintomo;
    @FXML private TableColumn<RilevazioneSintomo, Integer> intensita;

    /*
    @FXML private TableView<Paziente> tableViewRilevazioni;
    attributi tabella
    */

    public void initialize() throws SQLException {
        label1.setText(TestController.selectedUser.getNome() + ", " + TestController.selectedUser.getCognome() + ", " + TestController.selectedUser.getDataNascita());
        label2.setText(TestController.selectedUser.getEmail());
        label3.setText("Peso: " + TestController.selectedUser.getPeso() + " Kg");
        label4.setText("Note: " + TestController.selectedUser.getNotePaziente());

        dosiGiornaliere.setCellValueFactory(new PropertyValueFactory<Terapia, Integer>("dosiGiornaliere"));
        quantitaPerDose.setCellValueFactory(new PropertyValueFactory<Terapia, Double>("quantitaPerDose"));
        note.setCellValueFactory(new PropertyValueFactory<Terapia, String>("note"));
        Nome_farmaco.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));
        Codice_aic.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));

        timestamp.setCellValueFactory(new PropertyValueFactory<RilevazioneFarmaco, Timestamp>("timestamp"));
        quantita.setCellValueFactory(new PropertyValueFactory<RilevazioneFarmaco, Double>("quantita"));
        noteRilevazione.setCellValueFactory(new PropertyValueFactory<RilevazioneFarmaco, String>("noteRilevazione"));
        codiceAic.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));
        nome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));

        //  timestamp.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Timestamp>("timestamp"));
        valore.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Integer>("valore"));
        gravita.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Integer>("gravita"));
        primaPasto.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Boolean>("primaPasto"));

        //  timestamp.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, Timestamp>("timestamp"));
        sintomo.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, String>("sintomo"));
        intensita.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, Integer>("intensita"));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        medicoDAO.getUser(ViewNavigator.getAuthenticatedUser());
        Terapia[] terapie = medicoDAO.getTerapiePaziente(TestController.selectedUser.getUsername());
        RilevazioneFarmaco[] rilevazioniFarmaci = medicoDAO.getRilevazioniFarmaco(TestController.selectedUser.getUsername());
        RilevazioneGlicemia[] rilevazioniGlicemia = medicoDAO.getRilevazioniGlicemia(TestController.selectedUser.getUsername());
        RilevazioneSintomo[] rilevazioniSintomi = medicoDAO.getRilevazioniSintomo(TestController.selectedUser.getUsername());

        ObservableList<Terapia> ter = FXCollections.observableArrayList(terapie);
        ObservableList<RilevazioneFarmaco> rilFarmaci = FXCollections.observableArrayList(rilevazioniFarmaci);
        ObservableList<RilevazioneGlicemia> rilGlicemia = FXCollections.observableArrayList(rilevazioniGlicemia);
        ObservableList<RilevazioneSintomo> rilSintomi = FXCollections.observableArrayList(rilevazioniSintomi);

        tableViewTerapie.setItems(ter);
        tableViewRilevazioniFarmaci.setItems(rilFarmaci);
        tableViewRilevazioniGlicemia.setItems(rilGlicemia);
        tableViewRilevazioniSintomi.setItems(rilSintomi);
    }

    @FXML
    private void handleTerapia() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToDashboard();
    }

    @FXML
    private void handleRilevazioniFarmaci() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToHandleRilevazioniFarmaci();
    }

    @FXML
    private void handleRilevazioniSintomi() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToHandleRilevazioniSintomi();
    }

    @FXML
    private void handleRilevazioniGlicemia() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToHandleRilevazioniGlicemia();
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