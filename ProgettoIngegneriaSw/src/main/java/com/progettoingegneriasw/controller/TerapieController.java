package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Utils.Terapia;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class TerapieController {
    @FXML
    private TableView<Terapia> tableViewTerapie;
    @FXML private TableColumn<Terapia, Integer> dosiGiornaliere;
    @FXML private TableColumn<Terapia, Double> quantitaPerDose;
    @FXML private TableColumn<Terapia, String> note;
    @FXML private TableColumn<Terapia, String> Nome_farmaco;
    @FXML private TableColumn<Terapia, String> Codice_aic;


    public void initialize() throws SQLException {
        dosiGiornaliere.setCellValueFactory(new PropertyValueFactory<Terapia, Integer>("dosiGiornaliere"));
        quantitaPerDose.setCellValueFactory(new PropertyValueFactory<Terapia, Double>("quantitaPerDose"));
        note.setCellValueFactory(new PropertyValueFactory<Terapia, String>("note"));
        Nome_farmaco.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));
        Codice_aic.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        Terapia[] terapie = medicoDAO.getTerapiePaziente(ViewNavigator.getAuthenticatedUser());

        ObservableList<Terapia> ter = FXCollections.observableArrayList(terapie);

        tableViewTerapie.setItems(ter);

        //  setupTable();
    }

    /**
     * Da terminare: si deve fare attenzione al fatto che un farmaco può essere stato assunto ma
     * essendo potenzialmente presente in più terapie si deve valutare anche la quantità assunta
     * al fine di stabilire se la terapia è stata assunta e quindi debba essre indicata in grigio nella tabella
     */
    @FXML
    private void setupTable() {
        PazienteDAO pazienteDAO = PazienteDAO.getInstance();
        tableViewTerapie.setRowFactory(tv -> new TableRow<Terapia>() {
            @Override
            protected void updateItem(Terapia terapia, boolean empty) {
                super.updateItem(terapia, empty);
                try {
                    if (pazienteDAO.getFarmacoAssuntoOggi(terapia.getFarmaco())) {
                        setStyle("-fx-background-color: #dddddd; -fx-text-fill: #666666;");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
