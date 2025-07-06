package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RilevazioniHandlingController {
    @FXML private TableView<RilevazioneFarmaco> tableViewRilevazioniFarmaci;
    @FXML private TableColumn<RilevazioneFarmaco, Timestamp> timestampRilFarmaco;
    @FXML private TableColumn<RilevazioneFarmaco, String> codiceAic;
    @FXML private TableColumn<RilevazioneFarmaco, String> nome;
    @FXML private TableColumn<RilevazioneFarmaco, Double> quantita;
    @FXML private TableColumn<RilevazioneFarmaco, String> noteRilevazione;

    @FXML private TableView<RilevazioneGlicemia> tableViewRilevazioniGlicemia;
    @FXML private TableColumn<RilevazioneGlicemia, Timestamp> timestampRilGlicemia;
    @FXML private TableColumn<RilevazioneGlicemia, Integer> valore;
    @FXML private TableColumn<RilevazioneGlicemia, Integer> gravita;
    @FXML private TableColumn<RilevazioneGlicemia, Boolean> primaPasto;

    @FXML private TableView<RilevazioneSintomo> tableViewRilevazioniSintomi;
    @FXML private TableColumn<RilevazioneSintomo, Timestamp> timestampRilSintomo;
    @FXML private TableColumn<RilevazioneSintomo, String> sintomo;
    @FXML private TableColumn<RilevazioneSintomo, Integer> intensita;

    @FXML private ComboBox comboBoxRilevazione;

    @FXML private VBox VBoxNuovaRilevazione;
    @FXML private VBox VBoxIdPaziente;
    @FXML private TextField idPazienteNuovaRilevazione;
    @FXML private VBox VBoxTimestamp;

    @FXML private VBox VBoxNuovaRilevazioneFarmaco;
    @FXML private TextField codiceAicNuovaRilevazione;
    @FXML private TextField quantitaNuovaRilevazione;
    @FXML private TextField noteNuovaRilevazione;

    @FXML private VBox VBoxNuovaRilevazioneGlicemia;
    @FXML private TextField valoreNuovaRilevazione;

    @FXML private VBox VBoxNuovaRilevazioneSintomo;
    @FXML private TextField sintomoNuovaRilevazione;

    @FXML private Button inserisciRilevazioneButton;

    @FXML private Label statusLabel;

    @FXML private ComboBox comboBoxFarmaco;

    private UserDAO userDAO = UserDAO.getInstance();
    private PazienteDAO pazienteDAO = PazienteDAO.getInstance();

    private int idFarmacoSelezionato;
    private Boolean isPrimaPasto;


    @FXML private Button nuovaRilevazioneButton;
    @FXML private Button ricaricaButton;

    @FXML private RadioButton radioPrimaPasto;
    @FXML private RadioButton radioDopoPasto;
    private ToggleGroup pastoToggleGroup;
    @FXML private DatePicker datePickerRilevazione;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private Slider intensitaSlider;

    public void initialize() throws SQLException {
        setup();

        pastoToggleGroup = new ToggleGroup();
        radioPrimaPasto.setToggleGroup(pastoToggleGroup);
        radioDopoPasto.setToggleGroup(pastoToggleGroup);
        radioPrimaPasto.setSelected(true); // default value

        // round intensità to an integer value
        intensitaSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            intensitaSlider.setValue(Math.round(newVal.doubleValue()));
        });


        comboBoxRilevazione.setOnAction(e -> handleTipoRilevazioneSelected());
        comboBoxFarmaco.setOnAction(e -> handleFarmacoSelected());

        Task<Farmaco[]> loadFarmaciTask = new Task<>() {
            @Override
            protected Farmaco[] call() throws Exception {
                return pazienteDAO.getFarmaciPaziente(ViewNavigator.getAuthenticatedUsername());
            }
        };

        loadFarmaciTask.setOnSucceeded(e -> {
            ObservableList<Farmaco> farmaci = FXCollections.observableArrayList(loadFarmaciTask.getValue());
            comboBoxFarmaco.setItems(farmaci);
        });

        new Thread(loadFarmaciTask).start();
    }


    @FXML
    private void handleNuovaRilevazione() {
        VBoxNuovaRilevazione.setVisible(true);
        VBoxNuovaRilevazione.setManaged(true);
    }

    private void setup() {
        ///  performo DB and heavy operations in background avoiding UI freezing
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() throws Exception {

                // get Data from DB
                MedicoDAO medicoDAO = MedicoDAO.getInstance();
                RilevazioneFarmaco[] rilevazioniFarmaci = medicoDAO.getRilevazioniFarmaco(ViewNavigator.getAuthenticatedUsername());
                RilevazioneGlicemia[] rilevazioniGlicemia = medicoDAO.getRilevazioniGlicemia(ViewNavigator.getAuthenticatedUsername());
                RilevazioneSintomo[] rilevazioniSintomi = medicoDAO.getRilevazioniSintomo(ViewNavigator.getAuthenticatedUsername());

                ObservableList<RilevazioneFarmaco> rilFarmaci = FXCollections.observableArrayList(rilevazioniFarmaci);
                ObservableList<RilevazioneGlicemia> rilGlicemia = FXCollections.observableArrayList(rilevazioniGlicemia);
                ObservableList<RilevazioneSintomo> rilSintomi = FXCollections.observableArrayList(rilevazioniSintomi);

                // esegue su JavaFX thread mettendo gli update dell'UI in coda
                Platform.runLater(() -> {
                    setTimestampFormat();
                    setupButtonsIcon();
                    setupTimeSpinners();

                    idPazienteNuovaRilevazione.setDisable(true);
                    idPazienteNuovaRilevazione.setEditable(false);
                    codiceAicNuovaRilevazione.setDisable(true);
                    codiceAicNuovaRilevazione.setEditable(false);

                    timestampRilFarmaco.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
                    quantita.setCellValueFactory(new PropertyValueFactory<>("quantita"));
                    noteRilevazione.setCellValueFactory(new PropertyValueFactory<>("noteRilevazione"));
                    codiceAic.setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));
                    nome.setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));

                    timestampRilGlicemia.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
                    valore.setCellValueFactory(new PropertyValueFactory<>("valore"));
                    gravita.setCellValueFactory(new PropertyValueFactory<>("gravita"));
                    primaPasto.setCellValueFactory(new PropertyValueFactory<>("primaPasto"));

                    timestampRilSintomo.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
                    sintomo.setCellValueFactory(new PropertyValueFactory<>("sintomo"));
                    intensita.setCellValueFactory(new PropertyValueFactory<>("intensita"));

                    tableViewRilevazioniFarmaci.setItems(rilFarmaci);
                    tableViewRilevazioniGlicemia.setItems(rilGlicemia);
                    tableViewRilevazioniSintomi.setItems(rilSintomi);
                });

                return null;
            }
        };

        new Thread(loadDataTask).start(); // run the background task
    }

    private void setupButtonsIcon(){
        ImageView iconViewNuovaRilevazione = new ImageView(new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/addIcon.png"));
        ImageView iconViewRicarica = new ImageView(new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/reloadIcon.png"));
        iconViewNuovaRilevazione.setFitHeight(24);
        iconViewNuovaRilevazione.setFitWidth(24);
        iconViewNuovaRilevazione.setPreserveRatio(true);
        iconViewRicarica.setFitHeight(24);
        iconViewRicarica.setFitWidth(24);
        iconViewRicarica.setPreserveRatio(true);
        nuovaRilevazioneButton.setGraphic(iconViewNuovaRilevazione);
        ricaricaButton.setGraphic(iconViewRicarica);
    }

    private void setupTimeSpinners() {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12)); // Default 12h
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0)); // Default 0m

        hourSpinner.setEditable(true);
        minuteSpinner.setEditable(true);
    }

    @FXML
    private void handleRicarica() throws SQLException {
        setup();
    }


    @FXML
    private void handleInserimentoNuovaRilevazione() throws SQLException {
        //  gestire il lancio della query di inserimento della rilevazione sulla base delle info fornite -> Implementare controlli

        if(VBoxNuovaRilevazione.isVisible() && (!idPazienteNuovaRilevazione.getText().equals(null))) {
            // inserisci rilevazione Farmaco
            if (VBoxNuovaRilevazioneFarmaco.isVisible()) {
                if ((!codiceAicNuovaRilevazione.getText().equals(null)) &&
                        (!quantitaNuovaRilevazione.getText().equals(null)) &&
                        (!noteNuovaRilevazione.getText().equals(null))) {

                    if (userDAO.getFarmacoFromAic(codiceAicNuovaRilevazione.getText()) != null) {

                        RilevazioneFarmaco rilevazioneFarmaco = new RilevazioneFarmaco(
                                Integer.parseInt(idPazienteNuovaRilevazione.getText()),
                                pazienteDAO.getFarmacoFromAic(codiceAicNuovaRilevazione.getText()),
                                Timestamp.from(Instant.now()),
                                Double.parseDouble(quantitaNuovaRilevazione.getText()),
                                noteNuovaRilevazione.getText()
                        );

                        pazienteDAO.setRilevazioneFarmaco(rilevazioneFarmaco);
                        codiceAicNuovaRilevazione.setText("");
                        //  Da capire come gestire l'azzeramento con: comboBoxFarmaco.setValue(null);
                        quantitaNuovaRilevazione.setText("");
                        noteNuovaRilevazione.setText("");
                        handleRicarica();
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

            // Inserisci Rilevazione Glicemia
            else if (VBoxNuovaRilevazioneGlicemia.isVisible()) {
                isPrimaPasto = radioPrimaPasto.isSelected();
                if((!valoreNuovaRilevazione.getText().equals(null))) {
                    RilevazioneGlicemia rilevazioneGlicemia = new RilevazioneGlicemia(
                            Integer.parseInt(idPazienteNuovaRilevazione.getText()),
                            Timestamp.from(Instant.now()),
                            Integer.parseInt(valoreNuovaRilevazione.getText()),
                            isPrimaPasto
                    );

                    pazienteDAO.setRilevazioneGlicemia(rilevazioneGlicemia);
                    valoreNuovaRilevazione.setText("");
                    handleRicarica();
                }
                else {
                    statusLabel.setText("Informazioni mancanti, completare");
                    statusLabel.setVisible(true);
                }
            }

            // Inserisci Rilevazione Sintomo
            else if (VBoxNuovaRilevazioneSintomo.isVisible()) {
                if (sintomoNuovaRilevazione.getText() != null && !sintomoNuovaRilevazione.getText().isBlank()) {
                    RilevazioneSintomo rilevazioneSintomo = new RilevazioneSintomo(
                            Integer.parseInt(idPazienteNuovaRilevazione.getText()),
                            Timestamp.from(Instant.now()),
                            sintomoNuovaRilevazione.getText(),
                            (int) intensitaSlider.getValue()
                    );
                    pazienteDAO.setRilevazioneSintomo(rilevazioneSintomo);
                    sintomoNuovaRilevazione.setText("");
                    intensitaSlider.setValue(1); // reset to default in-range
                    handleRicarica();
                } else {
                    statusLabel.setText("Informazioni mancanti, completare");
                    statusLabel.setVisible(true);
                }
            }
        }
        else{
            statusLabel.setText("Informazioni mancanti, completare");
            statusLabel.setVisible(true);
        }

        clearInputFields();
    }



    @FXML
    private void onComboBoxChanged() {
        //  gestire il comportamento del menu a tendina e dei campi della pagina di inserimento in maniera dinamica
        comboBoxRilevazione.setOnAction(e -> {
            String rilevazioneSelezionata = comboBoxRilevazione.getValue().toString();

            VBoxIdPaziente.setVisible(true);
            VBoxIdPaziente.setManaged(true);
            idPazienteNuovaRilevazione.setVisible(true);
            VBoxTimestamp.setManaged(true);
            VBoxTimestamp.setVisible(true);

            // view timestamp
            if (datePickerRilevazione != null && hourSpinner != null && minuteSpinner != null) {
                datePickerRilevazione.setVisible(true);
                hourSpinner.setVisible(true);
                minuteSpinner.setVisible(true);

                datePickerRilevazione.setManaged(true);
                hourSpinner.setManaged(true);
                minuteSpinner.setManaged(true);
            }

            inserisciRilevazioneButton.setManaged(true);
            inserisciRilevazioneButton.setVisible(true);

            MedicoDAO medicoDAO = MedicoDAO.getInstance();
            idPazienteNuovaRilevazione.setText("" + medicoDAO.getIdFromDB(ViewNavigator.getAuthenticatedUsername()));

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
                intensitaSlider.setVisible(false);
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
                intensitaSlider.setVisible(false);
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
                intensitaSlider.setVisible(true);
            }
        });
    }


    private void handleTipoRilevazioneSelected() {

        clearInputFields();

        Object selected = comboBoxRilevazione.getValue();
        if (selected == null) return;
        String rilevazioneSelezionata = selected.toString();

        VBoxIdPaziente.setVisible(true);
        VBoxIdPaziente.setManaged(true);
        idPazienteNuovaRilevazione.setVisible(true);
        idPazienteNuovaRilevazione.setEditable(false);
        VBoxTimestamp.setManaged(true);
        VBoxTimestamp.setVisible(true);
        inserisciRilevazioneButton.setManaged(true);
        inserisciRilevazioneButton.setVisible(true);

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() {
                return MedicoDAO.getInstance().getIdFromDB(ViewNavigator.getAuthenticatedUsername());
            }
        };
        task.setOnSucceeded(e -> idPazienteNuovaRilevazione.setText(String.valueOf(task.getValue())));
        new Thread(task).start();

        switch (rilevazioneSelezionata) {
            case "Rilevazione glicemia" -> hideSection(true, false, false);
            case "Rilevazione assunzione farmaco" -> hideSection(false, true, false);
            default -> hideSection(false, false, true);
        }
    }

    private void clearInputFields() {
        if (valoreNuovaRilevazione != null) valoreNuovaRilevazione.clear();
        if (sintomoNuovaRilevazione != null) sintomoNuovaRilevazione.clear();
        if (noteNuovaRilevazione != null) noteNuovaRilevazione.clear();
        if (quantitaNuovaRilevazione != null) quantitaNuovaRilevazione.clear();
        if (comboBoxFarmaco != null) comboBoxFarmaco.getSelectionModel().clearSelection();
        if (codiceAicNuovaRilevazione != null) codiceAicNuovaRilevazione.clear();

        // Reset default for radio
        if (radioPrimaPasto != null) radioPrimaPasto.setSelected(true);

        // Reset slider to valid value
        if (intensitaSlider != null) intensitaSlider.setValue(1);

        // Reset timestamp fields
        if (datePickerRilevazione != null) datePickerRilevazione.setValue(null);
        if (hourSpinner != null && hourSpinner.getValueFactory() != null) hourSpinner.getValueFactory().setValue(12);
        if (minuteSpinner != null && minuteSpinner.getValueFactory() != null) minuteSpinner.getValueFactory().setValue(0);
    }


    private void handleFarmacoSelected() {
        Object selected = comboBoxFarmaco.getValue();
        if (selected == null) return;
        String selectedText = selected.toString();

        Pattern pattern = Pattern.compile("id:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(selectedText);

        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            idFarmacoSelezionato = id;

            Task<String> task = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return pazienteDAO.getFaracoFromId(id).getCodiceAic();
                }
            };
            task.setOnSucceeded(e -> codiceAicNuovaRilevazione.setText(task.getValue()));
            new Thread(task).start();
        }
    }


    private void hideSection(boolean glicemia, boolean farmaco, boolean sintomo) {
        VBoxNuovaRilevazioneGlicemia.setVisible(glicemia);
        VBoxNuovaRilevazioneGlicemia.setManaged(glicemia);
        valoreNuovaRilevazione.setVisible(glicemia);

        VBoxNuovaRilevazioneFarmaco.setVisible(farmaco);
        VBoxNuovaRilevazioneFarmaco.setManaged(farmaco);
        quantitaNuovaRilevazione.setVisible(farmaco);
        noteNuovaRilevazione.setVisible(farmaco);
        codiceAicNuovaRilevazione.setVisible(farmaco);

        VBoxNuovaRilevazioneSintomo.setVisible(sintomo);
        VBoxNuovaRilevazioneSintomo.setManaged(sintomo);
        sintomoNuovaRilevazione.setVisible(sintomo);
        intensitaSlider.setVisible(sintomo);
        intensitaSlider.setManaged(sintomo);
    }

    ///  this method set the timestamp format to 2025-05-17 08:00 instead of 2025-05-17 08:00:00.0
    private void setTimestampFormat(){

        final java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        timestampRilGlicemia.setCellFactory(column -> new TableCell<RilevazioneGlicemia, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDateTime().format(formatter));
                }
            }
        });

        timestampRilFarmaco.setCellFactory(column -> new TableCell<RilevazioneFarmaco, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDateTime().format(formatter));
                }
            }
        });

        timestampRilSintomo.setCellFactory(column -> new TableCell<RilevazioneSintomo, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDateTime().format(formatter));
                }
            }
        });
    }


}
