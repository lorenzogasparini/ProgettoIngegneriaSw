package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.UserType;
import com.progettoingegneriasw.model.Utils.Log;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;

public class VisualizzazioneLogController {

    @FXML
    private TableView<Log> logTable;
    @FXML private TableColumn<Log, Integer> idCol;
    //@FXML private TableColumn<Log, Integer> pazienteIdCol;
    @FXML private TableColumn<Log, String> pazienteUsernameCol;
    //@FXML private TableColumn<Log, Integer> medicoIdCol;
    @FXML private TableColumn<Log, String> medicoUsernameCol;
    @FXML private TableColumn<Log, String> azioneCol;
    @FXML private TableColumn<Log, Timestamp> timestampCol;

    @FXML private Button avantiButton;
    @FXML private Button indietroButton;
    @FXML private Label paginaCorrenteLabel;

    private final AdminDAO adminDAO = AdminDAO.getInstance();
    private int paginaCorrente = 0;
    private final int PAGINA_SIZE = 100;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        pazienteUsernameCol.setCellValueFactory(cellData -> {
            ///  se il paziente non è presente sulla tabella log è presente il valore id_paziente = 00
            Integer idPaziente = cellData.getValue().getIdPaziente();
            if (idPaziente != null && idPaziente != 0) {
                User user = UserDAO.getInstance().getUser(idPaziente, UserType.Paziente);
                return new SimpleStringProperty(user != null ? user.getUsername() : "");
            } else {
                return new SimpleStringProperty("");
            }
        });

        medicoUsernameCol.setCellValueFactory(cellData -> {
            ///  se il medico non è presente sulla tabella log è presente il valore id_diabetologo = 0
            Integer idMedico = cellData.getValue().getIdDiabetologo();
            if (idMedico != null && idMedico != 0) {
                User user = UserDAO.getInstance().getUser(idMedico, UserType.Medico);
                return new SimpleStringProperty(user != null ? user.getUsername() : "");
            } else {
                return new SimpleStringProperty("");
            }
        });

        azioneCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAzione().toString()));
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        caricaPagina();
    }

    @FXML
    private void handleAvanti() {
        paginaCorrente++;
        caricaPagina();
    }

    @FXML
    private void handleIndietro() {
        if (paginaCorrente > 0) {
            paginaCorrente--;
            caricaPagina();
        }
    }

    private void caricaPagina() {
        Log[] logs = adminDAO.getLogsPaged(paginaCorrente * PAGINA_SIZE, PAGINA_SIZE);
        logTable.setItems(FXCollections.observableArrayList(logs));
        paginaCorrenteLabel.setText("Pagina: " + (paginaCorrente + 1));
        indietroButton.setDisable(paginaCorrente == 0);
        avantiButton.setDisable(logs.length < PAGINA_SIZE);
    }
}

