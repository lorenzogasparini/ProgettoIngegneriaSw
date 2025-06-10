package com.progettoingegneriasw.controller;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
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

public class ContattaUtenteController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, String> username;
    @FXML private TableColumn<User, String> nome;
    @FXML private TableColumn<User, String> cognome;
    @FXML private TableColumn<User, String> email;

    public void initialize() throws SQLException {
        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        nome.setCellValueFactory(new PropertyValueFactory<User, String>("nome"));
        cognome.setCellValueFactory(new PropertyValueFactory<User, String>("cognome"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        UserDAO userDAO = UserDAO.getInstance();
        userDAO.getUser(ViewNavigator.getAuthenticatedUser());
        User[] user = userDAO.getAllUsers();

        ObservableList<User> users = FXCollections.observableArrayList(user);

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
