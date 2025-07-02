package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;

public class RilevazioniSintomiHandlingController {
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

    @FXML private TableView<RilevazioneSintomo> tableViewRilevazioni;
    @FXML private TableColumn<RilevazioneSintomo, Integer> id;
    @FXML private TableColumn<RilevazioneSintomo, String> sintomo;
    @FXML private TableColumn<RilevazioneSintomo, String> intensita;
    @FXML private TableColumn<RilevazioneSintomo, Timestamp> timestamp;

    public void initialize() throws SQLException {
        /*
        label1.setText(TestController.selectedUser.getNome() + ", " + TestController.selectedUser.getCognome() + ", " + TestController.selectedUser.getDataNascita());
        label2.setText(TestController.selectedUser.getEmail());
        label3.setText("Peso: " + TestController.selectedUser.getPeso() + " Kg");
        label4.setText("Note: " + TestController.selectedUser.getNotePaziente());
        */

        id.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, Integer>("id"));
        timestamp.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, Timestamp>("timestamp"));
        sintomo.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, String>("sintomo"));
        intensita.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, String>("intensita"));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        medicoDAO.getUser(ViewNavigator.getAuthenticatedUsername());
        RilevazioneSintomo[] rilevazioniSintomi = medicoDAO.getRilevazioniSintomo(TestController.selectedUser.getUsername());

        ObservableList<RilevazioneSintomo> rilSintomi = FXCollections.observableArrayList(rilevazioniSintomi);

        tableViewRilevazioni.setItems(rilSintomi);
    }

    @FXML
    private void handleLogin() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToLogin();
    }

    @FXML
    private void handleRegister() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToRegister();
    }

    @FXML
    private void handleDashboard() {
        TestController.selectedUser = null;
        ViewNavigator.navigateToDashboard();
    }
}