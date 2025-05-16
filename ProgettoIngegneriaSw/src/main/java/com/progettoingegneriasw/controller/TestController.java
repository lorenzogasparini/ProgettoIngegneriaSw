package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
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

public class TestController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML private TableView<Paziente> tableView;
    @FXML private TableColumn<Paziente, Integer> Id;
    @FXML private TableColumn<Paziente, String> Username;
    @FXML private TableColumn<Paziente, String> Nome;
    @FXML private TableColumn<Paziente, String> Cognome;
    @FXML private TableColumn<Paziente, String> Password;
    @FXML private TableColumn<Paziente, String> Email;
    @FXML private TableColumn<Paziente, Double> Peso;
    @FXML private TableColumn<Paziente, String> Provincia_residenza;

    public void initialize() throws SQLException {
        // If we're already authenticated, hide login/register buttons
        if (ViewNavigator.isAuthenticated()) {
            loginButton.setVisible(false);
            registerButton.setVisible(false);
        }

        Id.setCellValueFactory(new PropertyValueFactory<Paziente, Integer>("Id"));
        Username.setCellValueFactory(new PropertyValueFactory<Paziente, String>("Username"));
        Nome.setCellValueFactory(new PropertyValueFactory<Paziente, String>("Nome"));
        Cognome.setCellValueFactory(new PropertyValueFactory<Paziente, String>("Cognome"));
        Password.setCellValueFactory(new PropertyValueFactory<Paziente, String>("Password"));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        medicoDAO.getUser("drrossi");
        Paziente[] pazienti = medicoDAO.getPazientiFromDB("drrossi");

        ObservableList<Paziente> users = FXCollections.observableArrayList(pazienti);

        tableView.setItems(users);
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