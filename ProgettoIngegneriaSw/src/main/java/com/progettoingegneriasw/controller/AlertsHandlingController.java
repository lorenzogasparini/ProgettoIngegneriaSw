package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.UserType;
import com.progettoingegneriasw.model.Utils.Alert;
import com.progettoingegneriasw.model.Utils.AlertFilter;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.SQLException;
import java.sql.Timestamp;

public class AlertsHandlingController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;
    @FXML private ComboBox<String> filtroPazientiCombo;
    @FXML private ComboBox<String> filtroLettiCombo;


    @FXML private TableView<Alert> tableView;
    @FXML private TableColumn<Alert, Integer> Id;
    @FXML private TableColumn<Alert, String> usernamePaziente;
    @FXML private TableColumn<Alert, Integer> idRilevazione;
    @FXML private TableColumn<Alert, String> tipoAlert;
    @FXML private TableColumn<Alert, Timestamp> timestamp;
    @FXML private TableColumn<Alert, Boolean> letto;

    public void initialize() throws SQLException {
        Id.setCellValueFactory(new PropertyValueFactory<Alert, Integer>("Id"));
        usernamePaziente.setCellValueFactory(cellData -> new SimpleStringProperty(
                UserDAO.getInstance().getUser(cellData.getValue().getIdPaziente(), UserType.Paziente).getUsername()
        ));
        idRilevazione.setCellValueFactory(new PropertyValueFactory<Alert, Integer>("idRilevazione"));
        tipoAlert.setCellValueFactory(new PropertyValueFactory<Alert, String>("tipoAlert"));
        timestamp.setCellValueFactory(new PropertyValueFactory<Alert, Timestamp>("timestamp"));
        letto.setCellValueFactory(new PropertyValueFactory<Alert, Boolean>("letto"));
        setTimestampFormat();
        handleRead();

        // Imposta valori predefiniti dei filtri
        filtroPazientiCombo.getSelectionModel().select("Tutti i pazienti");
        filtroLettiCombo.getSelectionModel().select("Non letti");

        refreshTable();

    }

    ///  this method set the timestamp format to 2025-05-17 08:00 instead of 2025-05-17 08:00:00.0
    private void setTimestampFormat(){
        timestamp.setCellFactory(column -> new TableCell<Alert, Timestamp>() {
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

    private void refreshTable() throws SQLException {
        boolean soloMieiPazienti = filtroPazientiCombo.getValue() == null ||
                filtroPazientiCombo.getValue().equals("Solo i miei pazienti");

        String stato = filtroLettiCombo.getValue();

        AlertFilter filtro = switch (stato) {
            case "Letti" -> AlertFilter.READ;
            case "Non letti" -> AlertFilter.UNREAD;
            default -> AlertFilter.ALL;
        };

        MedicoDAO medicoDAO = MedicoDAO.getInstance();

        Alert[] alerts = soloMieiPazienti
                ? medicoDAO.getAlertsPazientiCurati(filtro)
                : medicoDAO.getAllAlerts(filtro);

        ObservableList<Alert> alertList = FXCollections.observableArrayList(alerts);
        tableView.setItems(alertList);
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
                            setGraphic(checkLabel);
                        } else {
                            setGraphic(markAsReadButton);
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void applicaFiltri() {
        try {
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickHandling(){
        tableView.setOnMouseClicked(event -> {
        User selectedUserInAlert = UserDAO.getInstance().getUser(
                tableView.getSelectionModel().getSelectedItem().getIdPaziente(),
                UserType.Paziente);

        if(selectedUserInAlert == null)
            return;

            // Per  la riga cliccata:
            PazientiController.selectedUser = (Paziente) selectedUserInAlert;
            handleUserHandling();
        });
    }

    @FXML
    private void handleUserHandling(){
        ViewNavigator.navigateToUserHandling();
    }


}
