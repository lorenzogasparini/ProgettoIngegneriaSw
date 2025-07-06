package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;

public class RilevazioniFarmaciHandlingController {
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

    @FXML private TableView<RilevazioneFarmaco> tableViewRilevazioni;
    @FXML private TableColumn<RilevazioneFarmaco, Integer> id;
    @FXML private TableColumn<RilevazioneFarmaco, String> codiceAic;
    @FXML private TableColumn<RilevazioneFarmaco, String> nome;
    @FXML private TableColumn<RilevazioneFarmaco, Timestamp> timestamp;
    @FXML private TableColumn<RilevazioneFarmaco, Double> quantita;
    @FXML private TableColumn<RilevazioneFarmaco, String> noteRilevazione;

    public void initialize() throws SQLException {
        /*
        label1.setText(TestController.selectedUser.getNome() + ", " + TestController.selectedUser.getCognome() + ", " + TestController.selectedUser.getDataNascita());
        label2.setText(TestController.selectedUser.getEmail());
        label3.setText("Peso: " + TestController.selectedUser.getPeso() + " Kg");
        label4.setText("Note: " + TestController.selectedUser.getNotePaziente());
        */

        id.setCellValueFactory(new PropertyValueFactory<RilevazioneFarmaco, Integer>("id"));
        timestamp.setCellValueFactory(new PropertyValueFactory<RilevazioneFarmaco, Timestamp>("timestamp"));
        setTimestampFormat();
        quantita.setCellValueFactory(new PropertyValueFactory<RilevazioneFarmaco, Double>("quantita"));
        noteRilevazione.setCellValueFactory(new PropertyValueFactory<RilevazioneFarmaco, String>("noteRilevazione"));
        codiceAic.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));
        nome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();

        RilevazioneFarmaco[] rilevazioniFarmaci = medicoDAO.getRilevazioniFarmaco(PazientiController.selectedUser.getUsername());

        ObservableList<RilevazioneFarmaco> rilFarmaci = FXCollections.observableArrayList(rilevazioniFarmaci);

        tableViewRilevazioni.setItems(rilFarmaci);
    }

    ///  this method set the timestamp format to 2025-05-17 08:00 instead of 2025-05-17 08:00:00.0
    private void setTimestampFormat(){
        timestamp.setCellFactory(column -> new TableCell<RilevazioneFarmaco, Timestamp>() {
            private final java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
}
