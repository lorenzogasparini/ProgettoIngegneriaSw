package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.Alert;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
        handleRead();

        refreshTable();

    }

    private void refreshTable() throws SQLException {
        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        medicoDAO.getUser(ViewNavigator.getAuthenticatedUser());

        Alert[] alerts = medicoDAO.getAlertPazientiCurati();
        ObservableList<Alert> alert = FXCollections.observableArrayList(alerts);
        tableView.setItems(alert);

    }

    private void handleRead(){
        letto.setCellFactory(new Callback<TableColumn<Alert, Boolean>, TableCell<Alert, Boolean>>() {
            @Override
            public TableCell<Alert, Boolean> call(TableColumn<Alert, Boolean> param) {
                return new TableCell<>() {
                    private final Button markAsReadButton = new Button("Letto");
                    private final Label checkLabel = new Label("✓");

                    {
                        markAsReadButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                        markAsReadButton.setOnAction(event -> {
                            Alert alert = getTableView().getItems().get(getIndex());
                            if (!alert.isLetto()) {
                                UserDAO.getInstance().setAlertRead(alert, true); // Imposta come letto
                                try {
                                    refreshTable();
                                    getTableView().refresh();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        checkLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: green; -fx-cursor: hand;");
                        checkLabel.setOnMouseClicked(event -> {
                            Alert alert = getTableView().getItems().get(getIndex());

                            javafx.scene.control.Alert.AlertType type = javafx.scene.control.Alert.AlertType.CONFIRMATION;
                            javafx.scene.control.Alert confirmationDialog = new javafx.scene.control.Alert(type);
                            confirmationDialog.setTitle("Conferma Azione");
                            confirmationDialog.setHeaderText("Vuoi segnare l'alert come non letto?");

                            ButtonType okButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
                            ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
                            confirmationDialog.getButtonTypes().setAll(okButton, cancelButton);

                            confirmationDialog.showAndWait().ifPresent(response -> {
                                if (response == okButton) {
                                    UserDAO.getInstance().setAlertRead(alert, false); // Imposta come NON letto
                                    try {
                                        refreshTable();
                                        getTableView().refresh();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        });
                    }

                    @Override
                    protected void updateItem(Boolean isLetto, boolean empty) {
                        super.updateItem(isLetto, empty);
                        if (empty || isLetto == null) {
                            setGraphic(null);
                        } else if (isLetto) {
                            setGraphic(checkLabel); // Clickabile
                        } else {
                            setGraphic(markAsReadButton);
                        }
                    }
                };
            }
        });
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
