package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        setTimestampFormat();
        sintomo.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, String>("sintomo"));
        intensita.setCellValueFactory(new PropertyValueFactory<RilevazioneSintomo, String>("intensita"));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        RilevazioneSintomo[] rilevazioniSintomi = medicoDAO.getRilevazioniSintomo(PazientiController.selectedUser.getUsername());

        ObservableList<RilevazioneSintomo> rilSintomi = FXCollections.observableArrayList(rilevazioniSintomi);

        tableViewRilevazioni.setItems(rilSintomi);
    }

    ///  this method set the timestamp format to 2025-05-17 08:00 instead of 2025-05-17 08:00:00.0
    private void setTimestampFormat(){
        timestamp.setCellFactory(column -> new TableCell<RilevazioneSintomo, Timestamp>() {
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