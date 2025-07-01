package com.progettoingegneriasw.controller;


import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;

public class UserHandlingController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML private VBox datiViewBox;
    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;
    @FXML private Label label4;

    @FXML private VBox datiEditBox;
    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField emailField;
    @FXML private TextField pesoField;
    @FXML private TextArea noteArea;



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

    public void initialize() throws SQLException {
        label1.setText(TestController.selectedUser.getNome() + ", " + TestController.selectedUser.getCognome() + ", " + TestController.selectedUser.getDataNascita());
        label2.setText(TestController.selectedUser.getEmail());
        label3.setText("Peso: " + TestController.selectedUser.getPeso() + " Kg");
        label4.setText("Note: " + TestController.selectedUser.getNotePaziente());

        // Popola i campi modificabili con i dati del paziente selezionato
        Paziente paziente = TestController.selectedUser;

        nomeField.setText(paziente.getNome());
        cognomeField.setText(paziente.getCognome());
        emailField.setText(paziente.getEmail());
        pesoField.setText(String.valueOf(paziente.getPeso()));
        noteArea.setText(paziente.getNotePaziente());


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


    @FXML
    private void showDatiEditBox() {
        datiViewBox.setVisible(false);
        datiViewBox.setManaged(false);

        datiEditBox.setVisible(true);
        datiEditBox.setManaged(true);

        // Popola i campi editabili con i dati attuali
        Paziente user = TestController.selectedUser;
        nomeField.setText(user.getNome());
        cognomeField.setText(user.getCognome());
        emailField.setText(user.getEmail());
        pesoField.setText(String.valueOf(user.getPeso()));
        noteArea.setText(user.getNotePaziente());
    }

    @FXML
    private void showDatiViewBox(Paziente paziente){
        // Aggiorna la visualizzazione
        label1.setText(paziente.getNome() + ", " + paziente.getCognome() + ", " + paziente.getDataNascita());
        label2.setText(paziente.getEmail());
        label3.setText("Peso: " + paziente.getPeso() + " Kg");
        label4.setText("Note: " + paziente.getNotePaziente());

        // Torna alla vista iniziale
        datiEditBox.setVisible(false);
        datiEditBox.setManaged(false);
        datiViewBox.setVisible(true);
        datiViewBox.setManaged(true);
    }

    @FXML
    private void handleSalvaDatiPaziente() {
        try {
            Paziente paziente = TestController.selectedUser;

            //todo: consentire la modifica di altri campi?
            Paziente pazienteUpdated = new PazienteUser(
                    TestController.selectedUser.getId(),
                    TestController.selectedUser.getUsername(),
                    TestController.selectedUser.getPassword(),
                    nomeField.getText(),
                    cognomeField.getText(),
                    emailField.getText(),
                    TestController.selectedUser.getIdMedico(),
                    TestController.selectedUser.getDataNascita(),
                    Double.parseDouble(pesoField.getText()),
                    TestController.selectedUser.getProvinciaResidenza(),
                    TestController.selectedUser.getComuneResidenza(),
                    noteArea.getText()
            );

            UserDAO.getInstance().saveUser((User) pazienteUpdated);

            showDatiViewBox(pazienteUpdated);


        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Errore");
            errorAlert.setHeaderText("Errore durante il salvataggio");
            errorAlert.setContentText("Verifica che i campi siano corretti");
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }





}