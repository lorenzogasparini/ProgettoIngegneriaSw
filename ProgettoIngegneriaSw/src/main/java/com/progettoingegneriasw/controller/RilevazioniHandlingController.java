package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Utils.RilevazioneFarmaco;
import com.progettoingegneriasw.model.Utils.RilevazioneGlicemia;
import com.progettoingegneriasw.model.Utils.RilevazioneSintomo;
import com.progettoingegneriasw.model.Utils.Terapia;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;

public class RilevazioniHandlingController {
    @FXML
    private TableView<RilevazioneFarmaco> tableViewRilevazioniFarmaci;
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

    @FXML private VBox VBoxNuovaRilevazione;

    @FXML private VBox VBoxDosiGiornaliere;
    @FXML private TextField dosiGiornaliereUpdate;

    @FXML private VBox VBoxQuantitaPerDose;
    @FXML private TextField quantitaPerDoseUpdate;

    @FXML private VBox VBoxNote;
    @FXML private TextField noteUpdate;

    @FXML private Button nuovaRilevazioneButton;

    public void initialize() throws SQLException {
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
        RilevazioneFarmaco[] rilevazioniFarmaci = medicoDAO.getRilevazioniFarmaco(ViewNavigator.getAuthenticatedUser());
        RilevazioneGlicemia[] rilevazioniGlicemia = medicoDAO.getRilevazioniGlicemia(ViewNavigator.getAuthenticatedUser());
        RilevazioneSintomo[] rilevazioniSintomi = medicoDAO.getRilevazioniSintomo(ViewNavigator.getAuthenticatedUser());

        ObservableList<RilevazioneFarmaco> rilFarmaci = FXCollections.observableArrayList(rilevazioniFarmaci);
        ObservableList<RilevazioneGlicemia> rilGlicemia = FXCollections.observableArrayList(rilevazioniGlicemia);
        ObservableList<RilevazioneSintomo> rilSintomi = FXCollections.observableArrayList(rilevazioniSintomi);

        tableViewRilevazioniFarmaci.setItems(rilFarmaci);
        tableViewRilevazioniGlicemia.setItems(rilGlicemia);
        tableViewRilevazioniSintomi.setItems(rilSintomi);




    }

    @FXML
    private void handleNuovaRilevazione() {
        VBoxNuovaRilevazione.setVisible(true);
        VBoxNuovaRilevazione.setManaged(true);
        /*
        VBoxDosiGiornaliere.setVisible(true);
        VBoxQuantitaPerDose.setVisible(true);
        VBoxNote.setVisible(true);
        */

        //  Da gestire
    }

    @FXML
    private void handleInserimentoNuovaRilevazione() {
        //  gestire il lancio della query di inserimento della rilevazione sulla base delle info fornite -> Implementare controlli
    }

    @FXML
    private void handleTerapie() {
        ViewNavigator.navigateToTerapieHandling();
    }

    @FXML
    private void handleRilevazioniFarmaci() {
        ViewNavigator.navigateToHandleRilevazioniFarmaci();
    }

    @FXML
    private void handleRilevazioniSintomi() {
        ViewNavigator.navigateToHandleRilevazioniSintomi();
    }

    @FXML
    private void handleRilevazioniGlicemia() {
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
