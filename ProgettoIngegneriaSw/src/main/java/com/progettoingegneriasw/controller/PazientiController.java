package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.SQLException;

public class PazientiController {

    @FXML private ComboBox<String> filtroPazientiCombo;
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



        // Default filter value
        filtroPazientiCombo.getSelectionModel().select("Solo i miei pazienti");

        // Listener for automatic refresh
        filtroPazientiCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            try {
                updateTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Initial load
        updateTableView();
    }

    /// condivide il proprio utente alle altre pagine tramite la variabile condivisa selectedUser a cui accedono
    /// UserHandlingController e le pagine derivate da essa
    @FXML
    private void clickHandling(){
        tableView.setOnMouseClicked(event -> {

            // Per  la riga cliccata:
            selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
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

    // called everytime value filter value has changed
    private void updateTableView() throws SQLException {
        Paziente[] pazienti;
        String selectedFilter = filtroPazientiCombo.getValue();

        if ("Tutti i pazienti".equals(selectedFilter)) {
            pazienti = MedicoDAO.getInstance().getAllPazienti();
        } else {
            pazienti = MedicoDAO.getInstance().getPazientiAssegnati(ViewNavigator.getAuthenticatedUsername());
        }

        ObservableList<Paziente> users = FXCollections.observableArrayList(pazienti);
        tableView.setItems(users);
    }

}