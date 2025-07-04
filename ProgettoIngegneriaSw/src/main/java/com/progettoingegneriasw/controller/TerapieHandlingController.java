package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerapieHandlingController {
    @FXML private TableView<Terapia> tableViewTerapie;
    @FXML private TableColumn<Terapia, String> usernameMedico;
    @FXML private TableColumn<Terapia, Integer> dosiGiornaliere;
    @FXML private TableColumn<Terapia, Double> quantitaPerDose;
    @FXML private TableColumn<Terapia, String> note;
    @FXML private TableColumn<Terapia, String> Nome_farmaco;
    @FXML private TableColumn<Terapia, String> Codice_aic;

    @FXML private VBox VBoxUpdate;
    @FXML private Button deleteButton;
    @FXML private Terapia selectedTerapia;

    @FXML private VBox VBoxMedicoUpdate;
    @FXML private TextField medicoTextFieldUpdate;

    @FXML private VBox VBoxDosiGiornaliere;
    @FXML private TextField dosiGiornaliereUpdate;

    @FXML private VBox VBoxQuantitaPerDose;
    @FXML private TextField quantitaPerDoseUpdate;

    @FXML private VBox VBoxNote;
    @FXML private TextField noteUpdate;

    @FXML private Label statusLabel;

    @FXML private VBox VBoxFarmaco;
    @FXML private ComboBox comboBoxFarmacoUpdate;
    @FXML private int idFarmacoSelezionato;


    Patologia selectedPatologia = null;

    private MedicoDAO medicoDAO = MedicoDAO.getInstance();

    @FXML private VBox VBoxInserimento;
    @FXML private Label statusLabel2;
    @FXML private VBox VBoxPatologia;
    @FXML private ComboBox comboBoxPatologiaInsert;
    @FXML private VBox VBoxFarmaci;
    @FXML private ComboBox comboBoxFarmacoInsert;
    @FXML private VBox VBoxDoseGiornaliera;
    @FXML private TextField doseGiornalieraUpdate;
    @FXML private VBox VBoxQuantita;
    @FXML private TextField quantitaPerDose2;
    @FXML private VBox VBoxNote2;
    @FXML private TextField note2;

    public void initialize() throws SQLException {
        setup();
        setupDeleteButton();
        setupCombobox();

    }


    private void setup() throws SQLException {
        usernameMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNome())); // todo: capire perchè ritorna lo username (anche se è ciò che voglio) e non il nome con .getNome()
        medicoTextFieldUpdate.setEditable(false);
        dosiGiornaliere.setCellValueFactory(new PropertyValueFactory<Terapia, Integer>("dosiGiornaliere"));
        quantitaPerDose.setCellValueFactory(new PropertyValueFactory<Terapia, Double>("quantitaPerDose"));
        note.setCellValueFactory(new PropertyValueFactory<Terapia, String>("note"));
        Nome_farmaco.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));
        Codice_aic.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));

        refreshTable();
    }

    private void refreshTable() throws SQLException {
        medicoDAO.getUser(ViewNavigator.getAuthenticatedUsername());
        Terapia[] terapie = medicoDAO.getTerapiePaziente(TestController.selectedUser.getUsername());

        ObservableList<Terapia> ter = FXCollections.observableArrayList(terapie);

        tableViewTerapie.setItems(ter);
        tableViewTerapie.refresh();
    }

    private void setupDeleteButton(){
        Image image = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/deleteIcon.png");
        ImageView icon = new ImageView(image);
        icon.setFitWidth(16);
        icon.setFitHeight(16);
        deleteButton.setGraphic(icon);
        deleteButton.setContentDisplay(ContentDisplay.LEFT);
    }

    private void setupCombobox() throws SQLException {
        // get farmaci for comboBoxFarmaco
        Farmaco[] farmaciAssegnati = medicoDAO.getFarmaci();
        ObservableList<Farmaco> farmaci = FXCollections.observableArrayList(comboBoxFarmacoUpdate.getItems());
        farmaci.addAll(farmaciAssegnati);
        comboBoxFarmacoUpdate.setItems(farmaci);
        comboBoxFarmacoInsert.setItems(farmaci);

        // get patologie for comboBoxPatologia
        Patologia[] patologie = medicoDAO.getPatologie();
        ObservableList<Patologia> pat = FXCollections.observableArrayList(comboBoxPatologiaInsert.getItems());
        pat.addAll(patologie);
        comboBoxPatologiaInsert.setItems(pat);

        comboBoxFarmacoUpdate.setOnAction(e -> parseSelectedFarmaco(comboBoxFarmacoUpdate.getValue()));
        comboBoxPatologiaInsert.setOnAction(e -> parseSelectedPatologia(comboBoxPatologiaInsert.getValue()));
        comboBoxFarmacoInsert.setOnAction(e -> parseSelectedFarmaco(comboBoxFarmacoInsert.getValue()));

        // Setup comboBoxes
        if (!pat.isEmpty() && !farmaci.isEmpty()) {
            cleanComboBoxes();
        }

    }

    private void cleanComboBoxes(){
        comboBoxPatologiaInsert.getSelectionModel().selectFirst();
        parseSelectedPatologia(comboBoxPatologiaInsert.getValue());
        comboBoxFarmacoUpdate.getSelectionModel().selectFirst();
        parseSelectedFarmaco(comboBoxFarmacoUpdate.getValue());
        comboBoxFarmacoInsert.getSelectionModel().selectFirst();
        parseSelectedFarmaco(comboBoxFarmacoInsert.getValue());
    }


    @FXML
    private void handleNuovaTerapia() throws SQLException {

        // clean fields
        cleanComboBoxes();
        doseGiornalieraUpdate.clear();
        quantitaPerDose2.clear();
        note2.clear();
        VBoxInserimento.setVisible(false);
        VBoxInserimento.setManaged(false);

        VBoxUpdate.setVisible(false);
        VBoxUpdate.setManaged(false);
        VBoxInserimento.setVisible(true);
        VBoxInserimento.setManaged(true);
        statusLabel2.setVisible(false);
        statusLabel2.setManaged(false);
        VBoxMedicoUpdate.setVisible(true);
        VBoxMedicoUpdate.setManaged(true);
        VBoxPatologia.setVisible(true);
        VBoxPatologia.setManaged(true);
        comboBoxPatologiaInsert.setVisible(true);
        comboBoxPatologiaInsert.setManaged(true);
        VBoxFarmaci.setVisible(true);
        VBoxFarmaci.setManaged(true);
        comboBoxFarmacoInsert.setVisible(true);
        comboBoxFarmacoInsert.setManaged(true);
        VBoxDoseGiornaliera.setVisible(true);
        VBoxDoseGiornaliera.setManaged(true);
        doseGiornalieraUpdate.setVisible(true);
        doseGiornalieraUpdate.setManaged(true);
        VBoxQuantita.setVisible(true);
        VBoxQuantita.setManaged(true);
        quantitaPerDose2.setVisible(true);
        quantitaPerDose2.setManaged(true);
        VBoxNote2.setVisible(true);
        VBoxNote2.setManaged(true);
        note2.setVisible(true);
        note2.setManaged(true);
    }

    @FXML
    private void handleTerapiaUpdate() {
        tableViewTerapie.setOnMouseClicked(event -> {
            selectedTerapia = tableViewTerapie.getSelectionModel().getSelectedItem();
            VBoxInserimento.setVisible(false);
            VBoxInserimento.setManaged(false);
            if (selectedTerapia != null && (!VBoxInserimento.isVisible())) {
                VBoxUpdate.setVisible(true);
                VBoxUpdate.setManaged(true);
                VBoxDosiGiornaliere.setVisible(true);
                VBoxQuantitaPerDose.setVisible(true);
                VBoxNote.setVisible(true);
                VBoxFarmaco.setVisible(true);
                VBoxFarmaco.setManaged(true);

                //  System.out.println("ID: " + selectedTerapia.getId());

                medicoTextFieldUpdate.setText(selectedTerapia.getMedico().getNome());
                comboBoxFarmacoUpdate.getSelectionModel().select(selectedTerapia.getFarmaco());
                dosiGiornaliereUpdate.setText(selectedTerapia.getDosiGiornaliere().toString());
                quantitaPerDoseUpdate.setText(selectedTerapia.getQuantitaPerDose().toString());
                noteUpdate.setText(selectedTerapia.getNote());
            }
            else {
                doseGiornalieraUpdate.setText(selectedTerapia.getDosiGiornaliere().toString());
                quantitaPerDose2.setText(selectedTerapia.getQuantitaPerDose().toString());
                note2.setText(selectedTerapia.getNote());
            }
        });
    }

    private void parseSelectedPatologia(Object value) {
        if (value == null) return;
        String selezione = value.toString();

        Pattern pattern = Pattern.compile("id:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(selezione);

        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            try {
                selectedPatologia = MedicoDAO.getInstance().getPatologiaFromId(id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void parseSelectedFarmaco(Object value) {
        if (value == null) return;
        String selezione = value.toString();

        Pattern pattern = Pattern.compile("id:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(selezione);

        if (matcher.find()) {
            idFarmacoSelezionato = Integer.parseInt(matcher.group(1));
        }
    }


    @FXML
    private void handleInserimento() throws SQLException {
        Medico loggedMedico = (Medico) UserDAO.getInstance().getUser(ViewNavigator.getAuthenticatedUsername());

        Terapia ter = new Terapia(
                loggedMedico,
                medicoDAO.getFaracoFromId(idFarmacoSelezionato),
                Integer.parseInt(doseGiornalieraUpdate.getText()),
                Double.parseDouble(quantitaPerDose2.getText()),
                note2.getText()
        );
        Patologia pat = selectedPatologia;

        medicoDAO.setTerapiaPaziente(ter, TestController.selectedUser.getUsername(), pat, "");

        setup();
    }

    @FXML
    private void onComboBoxFarmaco() throws SQLException {
        UserDAO userDao = UserDAO.getInstance();
        comboBoxFarmacoUpdate.setOnAction(e -> {
            String selezione = comboBoxFarmacoUpdate.getValue().toString();

            Pattern pattern = Pattern.compile("id:\\s*(\\d+)");
            Matcher matcher = pattern.matcher(selezione);

            if (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                idFarmacoSelezionato = id;
            }
        });
    }

    @FXML
    private void handleUpdate() throws SQLException {
        //  gestire il lancio della query di update della terapia sulla base delle info fornite -> Implementare controlli
        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        Medico loggedMedico = (Medico) UserDAO.getInstance().getUser(ViewNavigator.getAuthenticatedUsername());
        medicoTextFieldUpdate.setText(loggedMedico.getUsername());

        if(VBoxUpdate.isVisible()) {
            if((!dosiGiornaliereUpdate.getText().isEmpty()) && (!quantitaPerDoseUpdate.getText().isEmpty()) && (!noteUpdate.getText().isEmpty())) {
                medicoDAO.updateTerapiaPaziente(
                        new Terapia(selectedTerapia.getId(),
                                loggedMedico, // si prende il medico loggato che sta eseguendo la modifica
                                medicoDAO.getFaracoFromId(idFarmacoSelezionato),
                                Integer.parseInt(dosiGiornaliereUpdate.getText()),
                                Double.parseDouble(quantitaPerDoseUpdate.getText()),
                                noteUpdate.getText()
                        )
                );
            }
            else {
                statusLabel.setVisible(true);
            }
        }
        setup();
    }


    @FXML
    private void handleDeleteTerapia() {
        if (selectedTerapia == null) {
            statusLabel.setText("Nessuna terapia selezionata.");
            statusLabel.setVisible(true);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Conferma eliminazione");
        confirm.setHeaderText("Vuoi davvero eliminare questa terapia?");

        ButtonType okButton = new ButtonType("Elimina", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(okButton, cancelButton);

        confirm.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                try {
                    MedicoDAO.getInstance().deleteTerapia(selectedTerapia);

                    statusLabel.setText("Terapia eliminata con successo.");
                    statusLabel.setStyle("-fx-text-fill: green;");
                    statusLabel.setVisible(true);

                    VBoxUpdate.setVisible(false);
                    VBoxUpdate.setManaged(false);
                    statusLabel.setVisible(false);

                    refreshTable();

                } catch (SQLException e) {
                    statusLabel.setText("Errore durante l'eliminazione.");
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setVisible(true);
                    e.printStackTrace();
                }
            }
        });
    }

}