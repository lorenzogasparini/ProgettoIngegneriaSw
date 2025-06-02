package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Utils.Alert;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AlertsHandlingController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML private TableView<Alert> tableView;
    @FXML private TableColumn<Alert, Integer> Id;
    @FXML private TableColumn<Alert, Integer> idPaziente;
    @FXML private TableColumn<Alert, Integer> idRilevazione;
    @FXML private TableColumn<Alert, String> tipoAlert;
    @FXML private TableColumn<Alert, Timestamp> timestamp;
    @FXML private TableColumn<Alert, Boolean> letto;

    public void initialize() throws SQLException {
        Id.setCellValueFactory(new PropertyValueFactory<Alert, Integer>("Id"));
        idPaziente.setCellValueFactory(new PropertyValueFactory<Alert, Integer>("idPaziente"));
        idRilevazione.setCellValueFactory(new PropertyValueFactory<Alert, Integer>("idRilevazione"));
        tipoAlert.setCellValueFactory(new PropertyValueFactory<Alert, String>("tipoAlert"));
        timestamp.setCellValueFactory(new PropertyValueFactory<Alert, Timestamp>("timestamp"));
        letto.setCellValueFactory(new PropertyValueFactory<Alert, Boolean>("letto"));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        medicoDAO.getUser(ViewNavigator.getAuthenticatedUser());
        Alert[] alerts = medicoDAO.getAlertPazientiCurati();

        ObservableList<Alert> alert = FXCollections.observableArrayList(alerts);

        tableView.setItems(alert);
    }

    @FXML
    private void handleLogin() {
        ViewNavigator.navigateToLogin();
    }

    @FXML
    private void handleRegister() {
        ViewNavigator.navigateToRegister();
    }

    @FXML
    private void handleDashboard() {
        ViewNavigator.navigateToDashboard();
    }
}
