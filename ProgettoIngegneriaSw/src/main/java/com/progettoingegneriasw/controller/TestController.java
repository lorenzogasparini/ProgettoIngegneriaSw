package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    @FXML private TableColumn<Paziente, String> email;
    @FXML private TableColumn<Paziente, Integer> idDiabetologo;
    @FXML private TableColumn<Paziente, Date> dataNascita;
    @FXML private TableColumn<Paziente, Double> peso;
    @FXML private TableColumn<Paziente, String> provinciaResidenza;
    @FXML private TableColumn<Paziente, String> comuneResidenza;
    @FXML private TableColumn<Paziente, String> notePaziente;

    /**
     * Variabile per la gestione globale della navigazione verso la view per la gestione del paziente selezionato
     */
    protected static Paziente selectedUser;

    public void initialize() throws SQLException {
        Id.setCellValueFactory(new PropertyValueFactory<Paziente, Integer>("Id"));
        Username.setCellValueFactory(new PropertyValueFactory<Paziente, String>("Username"));
        Nome.setCellValueFactory(new PropertyValueFactory<Paziente, String>("Nome"));
        Cognome.setCellValueFactory(new PropertyValueFactory<Paziente, String>("Cognome"));
        email.setCellValueFactory(new PropertyValueFactory<Paziente, String>("email"));
        idDiabetologo.setCellValueFactory(new PropertyValueFactory<Paziente, Integer>("idMedico"));
        dataNascita.setCellValueFactory(new PropertyValueFactory<Paziente, Date>("dataNascita"));
        peso.setCellValueFactory(new PropertyValueFactory<Paziente, Double>("peso"));
        provinciaResidenza.setCellValueFactory(new PropertyValueFactory<Paziente, String>("provinciaResidenza"));
        comuneResidenza.setCellValueFactory(new PropertyValueFactory<Paziente, String>("comuneResidenza"));
        notePaziente.setCellValueFactory(new PropertyValueFactory<Paziente, String>("notePaziente"));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        medicoDAO.getUser(ViewNavigator.getAuthenticatedUser());
        Paziente[] pazienti = medicoDAO.getPazientiFromDB(ViewNavigator.getAuthenticatedUser());

        ObservableList<Paziente> users = FXCollections.observableArrayList(pazienti);

        tableView.setItems(users);
    }

    @FXML
    private void clickHandling(){
        tableView.setOnMouseClicked(event -> {
            /* Codice di prova, che permette di gestire il numero di click
            if (event.getClickCount() == 1) {
                System.out.println("1 click");
            } else if (event.getClickCount() == 2) {
                System.out.println("2 click TableView");

                Object selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    System.out.println("Hai cliccato: " + selectedItem.toString());
                }
            }
            */

            // Per  la riga cliccata:
            selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                //  System.out.println("Hai cliccato: " + (pazienteSelezionato.getUsername()));
                handleUserHandling();
            }
        });

    }

    @FXML
    private void handleUserHandling(){
        ViewNavigator.navigateToUserHandling();
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