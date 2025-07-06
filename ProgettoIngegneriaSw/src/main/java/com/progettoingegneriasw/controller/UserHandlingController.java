package com.progettoingegneriasw.controller;


import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
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
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserHandlingController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML private VBox datiViewBox;
    @FXML private ImageView profileImage;
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

    public void initialize() {
        // UI Setup – fast operations
        loadProfileImage(PazientiController.selectedUser.getProfileImageName());
        Paziente paziente = PazientiController.selectedUser;

        label1.setText(paziente.getNome() + ", " + paziente.getCognome() + ", " + paziente.getDataNascita());
        label2.setText(paziente.getEmail());
        label3.setText("Peso: " + paziente.getPeso() + " Kg");
        label4.setText("Note: " + paziente.getNotePaziente());

        nomeField.setText(paziente.getNome());
        cognomeField.setText(paziente.getCognome());
        emailField.setText(paziente.getEmail());
        pesoField.setText(String.valueOf(paziente.getPeso()));
        noteArea.setText(paziente.getNotePaziente());

        // Table columns setup
        dosiGiornaliere.setCellValueFactory(new PropertyValueFactory<>("dosiGiornaliere"));
        quantitaPerDose.setCellValueFactory(new PropertyValueFactory<>("quantitaPerDose"));
        note.setCellValueFactory(new PropertyValueFactory<>("note"));
        Nome_farmaco.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));
        Codice_aic.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));

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

        setTimestampFormat();

        // Load data asynchronously
        Task<Void> loadTask = new Task<>() {
            ObservableList<Terapia> ter;
            ObservableList<RilevazioneFarmaco> rilFarmaci;
            ObservableList<RilevazioneGlicemia> rilGlicemia;
            ObservableList<RilevazioneSintomo> rilSintomi;

            @Override
            protected Void call() throws Exception {
                MedicoDAO medicoDAO = MedicoDAO.getInstance();

                Terapia[] terapie = medicoDAO.getTerapiePaziente(paziente.getUsername());
                RilevazioneFarmaco[] farmaci = medicoDAO.getRilevazioniFarmaco(paziente.getUsername());
                RilevazioneGlicemia[] glicemia = medicoDAO.getRilevazioniGlicemia(paziente.getUsername());
                RilevazioneSintomo[] sintomi = medicoDAO.getRilevazioniSintomo(paziente.getUsername());

                ter = FXCollections.observableArrayList(terapie);
                rilFarmaci = FXCollections.observableArrayList(farmaci);
                rilGlicemia = FXCollections.observableArrayList(glicemia);
                rilSintomi = FXCollections.observableArrayList(sintomi);

                return null;
            }

            // populate tables
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    tableViewTerapie.setItems(ter);
                    tableViewRilevazioniFarmaci.setItems(rilFarmaci);
                    tableViewRilevazioniGlicemia.setItems(rilGlicemia);
                    tableViewRilevazioniSintomi.setItems(rilSintomi);
                });
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                // Optionally show error in UI
            }
        };

        new Thread(loadTask).start();
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
        PazientiController.selectedUser = null;
        ViewNavigator.navigateToLogin();
    }

    @FXML
    private void handleRegister() {
        PazientiController.selectedUser = null;
        ViewNavigator.navigateToRegister();
    }

    @FXML
    private void handleDashboard() {
        PazientiController.selectedUser = null;
        ViewNavigator.navigateToDashboard();
    }


    @FXML
    private void showDatiEditBox() {
        datiViewBox.setVisible(false);
        datiViewBox.setManaged(false);

        datiEditBox.setVisible(true);
        datiEditBox.setManaged(true);

        // Popola i campi editabili con i dati attuali
        Paziente user = PazientiController.selectedUser;
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

            //todo: consentire la modifica di altri campi?
            Paziente pazienteUpdated = new PazienteUser(
                    PazientiController.selectedUser.getId(),
                    PazientiController.selectedUser.getUsername(),
                    PazientiController.selectedUser.getPassword(),
                    nomeField.getText(),
                    cognomeField.getText(),
                    emailField.getText(),
                    PazientiController.selectedUser.getIdMedico(),
                    PazientiController.selectedUser.getDataNascita(),
                    Double.parseDouble(pesoField.getText()),
                    PazientiController.selectedUser.getProvinciaResidenza(),
                    PazientiController.selectedUser.getComuneResidenza(),
                    noteArea.getText(),
                    PazientiController.selectedUser.getProfileImageName()
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

    private void loadProfileImage(String profileImageName){
        String imagePath = AppConfig.IMAGE_DIR + profileImageName;

        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                profileImage.setImage(new Image(file.toURI().toString()));
            }
        }
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