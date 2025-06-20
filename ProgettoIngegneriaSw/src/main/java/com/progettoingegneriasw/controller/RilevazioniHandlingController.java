package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.User;
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

public class RilevazioniHandlingController {
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

    @FXML private ComboBox comboBoxRilevazione;

    @FXML private VBox VBoxNuovaRilevazione;
    @FXML private VBox VBoxIdPaziente;
    @FXML private TextField idPazienteNuovaRilevazione;
    @FXML private VBox VBoxTimestamp;
    @FXML private TextField timestampNuovaRilevazione;

    @FXML private VBox VBoxNuovaRilevazioneFarmaco;
    @FXML private TextField codiceAicNuovaRilevazione;
    @FXML private TextField quantitaNuovaRilevazione;
    @FXML private TextField noteNuovaRilevazione;

    @FXML private VBox VBoxNuovaRilevazioneGlicemia;
    @FXML private TextField valoreNuovaRilevazione;

    @FXML private VBox VBoxNuovaRilevazioneSintomo;
    @FXML private TextField sintomoNuovaRilevazione;
    @FXML private TextField intensitaNuovaRilevazione;

    @FXML private Button inserisciRilevazioneButton;

    @FXML private Label statusLabel;

    @FXML private ComboBox comboBoxFarmaco;

    private UserDAO userDAO = UserDAO.getInstance();
    private PazienteDAO pazienteDAO = PazienteDAO.getInstance();

    private int idFarmacoSelezionato;
    private String primaPastoSelezionato;

    @FXML private ComboBox comboBoxPrimaPasto;

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

        PazienteDAO pazienteDao = PazienteDAO.getInstance();

        Farmaco[] farmaciAssegnati = pazienteDao.getFarmaciPaziente(ViewNavigator.getAuthenticatedUser());

        ObservableList<Farmaco> farmaci = FXCollections.observableArrayList(comboBoxFarmaco.getItems());
        farmaci.addAll(farmaciAssegnati);

        comboBoxFarmaco.setItems(farmaci);
    }

    @FXML
    private void onComboBoxChanged() {
        //  gestire il comportamento del menu a tendina e dei campi della pagina di inserimento in maniera dinamica
        comboBoxRilevazione.setOnAction(e -> {
            String rilevazioneSelezionata = comboBoxRilevazione.getValue().toString();

            VBoxIdPaziente.setVisible(true);
            VBoxIdPaziente.setManaged(true);
            idPazienteNuovaRilevazione.setVisible(true);
            idPazienteNuovaRilevazione.setEditable(false);
            VBoxTimestamp.setManaged(true);
            VBoxTimestamp.setVisible(true);
            timestampNuovaRilevazione.setVisible(true);
            inserisciRilevazioneButton.setManaged(true);
            inserisciRilevazioneButton.setVisible(true);

            MedicoDAO medicoDAO = MedicoDAO.getInstance();
            idPazienteNuovaRilevazione.setText("" + medicoDAO.getIdFromDB(ViewNavigator.getAuthenticatedUser()));

            if(rilevazioneSelezionata.equals("Rilevazione glicemia")){
                VBoxNuovaRilevazioneFarmaco.setManaged(false);
                VBoxNuovaRilevazioneFarmaco.setVisible(false);
                codiceAicNuovaRilevazione.setVisible(false);
                quantitaNuovaRilevazione.setVisible(false);
                noteNuovaRilevazione.setVisible(false);

                VBoxNuovaRilevazioneGlicemia.setManaged(true);
                VBoxNuovaRilevazioneGlicemia.setVisible(true);
                valoreNuovaRilevazione.setVisible(true);

                VBoxNuovaRilevazioneSintomo.setManaged(false);
                VBoxNuovaRilevazioneSintomo.setVisible(false);
                sintomoNuovaRilevazione.setVisible(false);
                intensitaNuovaRilevazione.setVisible(false);
            }
            else if(rilevazioneSelezionata.equals("Rilevazione assunzione farmaco")){
                VBoxNuovaRilevazioneFarmaco.setManaged(true);
                VBoxNuovaRilevazioneFarmaco.setVisible(true);
                quantitaNuovaRilevazione.setVisible(true);
                noteNuovaRilevazione.setVisible(true);
                codiceAicNuovaRilevazione.setVisible(true);

                VBoxNuovaRilevazioneGlicemia.setManaged(false);
                VBoxNuovaRilevazioneGlicemia.setVisible(false);
                valoreNuovaRilevazione.setVisible(false);

                VBoxNuovaRilevazioneSintomo.setManaged(false);
                VBoxNuovaRilevazioneSintomo.setVisible(false);
                sintomoNuovaRilevazione.setVisible(false);
                intensitaNuovaRilevazione.setVisible(false);
            }
            else{
                VBoxNuovaRilevazioneFarmaco.setManaged(false);
                VBoxNuovaRilevazioneFarmaco.setVisible(false);
                quantitaNuovaRilevazione.setVisible(false);
                noteNuovaRilevazione.setVisible(false);
                codiceAicNuovaRilevazione.setVisible(true);

                VBoxNuovaRilevazioneGlicemia.setManaged(false);
                VBoxNuovaRilevazioneGlicemia.setVisible(false);
                valoreNuovaRilevazione.setVisible(false);

                VBoxNuovaRilevazioneSintomo.setManaged(true);
                VBoxNuovaRilevazioneSintomo.setVisible(true);
                sintomoNuovaRilevazione.setVisible(true);
                intensitaNuovaRilevazione.setVisible(true);
            }
        });
    }

    @FXML
    private void handleNuovaRilevazione() {
        VBoxNuovaRilevazione.setVisible(true);
        VBoxNuovaRilevazione.setManaged(true);
    }

    @FXML
    private void handleInserimentoNuovaRilevazione() {
        //  gestire il lancio della query di inserimento della rilevazione sulla base delle info fornite -> Implementare controlli

        if(VBoxNuovaRilevazione.isVisible() && (!idPazienteNuovaRilevazione.getText().equals(null))) {
            if (VBoxNuovaRilevazioneFarmaco.isVisible()) {
                if ((!codiceAicNuovaRilevazione.getText().equals(null)) && (!quantitaNuovaRilevazione.getText().equals(null)) && (!noteNuovaRilevazione.getText().equals(null))) {
                    if (userDAO.getFarmacoFromAic(codiceAicNuovaRilevazione.getText()) != null) {
                        RilevazioneFarmaco rilevazioneFarmaco = new RilevazioneFarmaco(Integer.parseInt(idPazienteNuovaRilevazione.getText()), pazienteDAO.getFarmacoFromAic(codiceAicNuovaRilevazione.getText()), Timestamp.from(Instant.now()), Double.parseDouble(quantitaNuovaRilevazione.getText()), noteNuovaRilevazione.getText());
                        pazienteDAO.setRilevazioneFarmaco(rilevazioneFarmaco);
                        codiceAicNuovaRilevazione.setText("");
                        //  Da capire come gestire l'azzeramento con: comboBoxFarmaco.setValue(null);
                        quantitaNuovaRilevazione.setText("");
                        noteNuovaRilevazione.setText("");
                    }
                    else {
                        statusLabel.setText("Inserisci un codice AIC valido.");
                        statusLabel.setVisible(true);
                    }
                }
                else {
                    statusLabel.setText("Informazioni mancanti, completare");
                    statusLabel.setVisible(true);
                }
            }

            else if (VBoxNuovaRilevazioneGlicemia.isVisible()) {
                if((!valoreNuovaRilevazione.getText().equals(null))) {
                    RilevazioneGlicemia rilevazioneGlicemia = new RilevazioneGlicemia(Integer.parseInt(idPazienteNuovaRilevazione.getText()), Timestamp.from(Instant.now()), Integer.parseInt(valoreNuovaRilevazione.getText()), primaPastoSelezionato.equals("Vero"));
                    pazienteDAO.setRilevazioneGlicemia(rilevazioneGlicemia);
                    valoreNuovaRilevazione.setText("");
                    primaPastoSelezionato = "";
                }
                else {
                    statusLabel.setText("Informazioni mancanti, completare");
                    statusLabel.setVisible(true);
                }
            }

            else if (VBoxNuovaRilevazioneSintomo.isVisible()) {
                if((!sintomoNuovaRilevazione.getText().equals(null)) && (!intensitaNuovaRilevazione.getText().equals(null))) {
                    RilevazioneSintomo rilevazioneSintomo = new RilevazioneSintomo(Integer.parseInt(idPazienteNuovaRilevazione.getText()), Timestamp.from(Instant.now()), sintomoNuovaRilevazione.getText(), Integer.parseInt(intensitaNuovaRilevazione.getText()));
                    pazienteDAO.setRilevazioneSintomo(rilevazioneSintomo);
                    sintomoNuovaRilevazione.setText("");
                    intensitaNuovaRilevazione.setText("");
                }
                else {
                    statusLabel.setText("Informazioni mancanti, completare");
                    statusLabel.setVisible(true);
                }
            }
        }
        else{
            statusLabel.setText("Informazioni mancanti, completare");
            statusLabel.setVisible(true);
        }
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
                codiceAicNuovaRilevazione.setText(pazienteDAO.getFaracoFromId(idFarmacoSelezionato).getCodiceAic());
            }
        });
    }

    @FXML
    private void onComboBoxPrimaPasto() {
        comboBoxPrimaPasto.setOnAction(e -> {
            String selezione = comboBoxPrimaPasto.getValue().toString();

            primaPastoSelezionato = selezione;
        });
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
