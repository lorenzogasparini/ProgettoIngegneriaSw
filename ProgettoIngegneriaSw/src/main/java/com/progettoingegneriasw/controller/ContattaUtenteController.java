package com.progettoingegneriasw.controller;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.Alert;
import com.progettoingegneriasw.model.Utils.Terapia;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
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

    @FXML private VBox VBoxContatta;

    @FXML private VBox VBoxDestinatario;
    @FXML private TextField emailDestinatario;

    @FXML private VBox VBoxOggetto;
    @FXML private TextField emailOggetto;

    @FXML private VBox VBoxCorpo;
    @FXML private TextArea emailCorpo;

    @FXML private Button contactButton;

    private UserDAO userDAO = UserDAO.getInstance();

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
    private void setupComunication() {
        VBoxContatta.setVisible(true);
        VBoxContatta.setManaged(true);

        VBoxOggetto.setVisible(true);
        emailOggetto.setVisible(true);

        VBoxCorpo.setVisible(true);
        emailCorpo.setVisible(true);

        tableView.setOnMouseClicked(event -> {
            // Per  la riga cliccata:
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                if(selectedUser.isMedico()) {
                    MedicoUser medico = (MedicoUser) userDAO.getUser(selectedUser.getUsername());
                    emailDestinatario.setText(medico.getEmail());
                }
                else if(selectedUser.isPaziente()){
                    PazienteUser paziente = (PazienteUser) userDAO.getUser(selectedUser.getUsername());
                    emailDestinatario.setText(paziente.getEmail());
                }
                else{
                    System.out.println("Errore");
                }
            }
        });

        VBoxDestinatario.setVisible(true);
        emailDestinatario.setVisible(true);
    }

    @FXML
    private void handleComunication() {
        userDAO.contattaDiabetologo(emailDestinatario.getText(), emailOggetto.getText(), emailCorpo.getText());
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
