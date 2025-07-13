package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Utils.RilevazioneFarmaco;
import com.progettoingegneriasw.model.Utils.Terapia;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class TerapieController {
    @FXML private TableView<Terapia> tableViewTerapie;
    @FXML private TableColumn<Terapia, Integer> dosiGiornaliere;
    @FXML private TableColumn<Terapia, Double> quantitaPerDose;
    @FXML private TableColumn<Terapia, String> note;
    @FXML private TableColumn<Terapia, String> Nome_farmaco;
    @FXML private TableColumn<Terapia, String> Codice_aic;
    @FXML private TableColumn<Terapia, Boolean> assunto;


    public void initialize() throws SQLException {
        dosiGiornaliere.setCellValueFactory(new PropertyValueFactory<Terapia, Integer>("dosiGiornaliere"));
        quantitaPerDose.setCellValueFactory(new PropertyValueFactory<Terapia, Double>("quantitaPerDose"));
        note.setCellValueFactory(new PropertyValueFactory<Terapia, String>("note"));
        Nome_farmaco.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getNome()));
        Codice_aic.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFarmaco().getCodiceAic()));

        refreshAssunzioni();
        handleAssunzioni();

    }

    private void handleAssunzioni(){
        assunto.setCellFactory(column -> new TableCell<Terapia, Boolean>() {
            private final Button confirmButton = new Button("Assumi");
            private final Label checkLabel = new Label("✓");

            {
                confirmButton.setOnAction(event -> {
                    Terapia terapia = getTableView().getItems().get(getIndex());

                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Conferma Assunzione");
                    confirmation.setHeaderText("Vuoi confermare l'assunzione del farmaco?");
                    confirmation.setContentText("Farmaco: " + terapia.getFarmaco().getNome());

                    ButtonType okButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
                    confirmation.getButtonTypes().setAll(okButton, cancelButton);

                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == okButton) {
                            Task<Void> task = new Task<>() {
                                @Override
                                protected Void call() throws Exception {
                                    RilevazioneFarmaco rilevazioneFarmaco = new RilevazioneFarmaco(
                                            MedicoDAO.getInstance().getPazienteFromTerapia(terapia).getId(),
                                            terapia.getFarmaco(),
                                            Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                                            terapia.getQuantitaPerDose(),
                                            ""
                                    );
                                    PazienteDAO.getInstance().setRilevazioneFarmaco(rilevazioneFarmaco);
                                    return null;
                                }
                            };

                            task.setOnSucceeded(e -> {
                                refreshAssunzioni();
                            });

                            task.setOnFailed(e -> {
                                new Alert(Alert.AlertType.ERROR, "Errore durante l'inserimento!").showAndWait();
                                task.getException().printStackTrace();
                            });

                            new Thread(task).start();
                        }
                    });
                });


                checkLabel.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item ? checkLabel : confirmButton);
                }
            }
        });
    }

    private void refreshAssunzioni() {
        Task<Map<Terapia, Boolean>> task = new Task<>() {
            @Override
            protected Map<Terapia, Boolean> call() throws Exception {
                return PazienteDAO.getInstance()
                        .getTerapieEAssunzioniPaziente(ViewNavigator.getAuthenticatedUsername());
            }
        };

        task.setOnSucceeded(e -> {
            Map<Terapia, Boolean> data = task.getValue();

            assunto.setCellValueFactory(cellData -> {
                Terapia terapia = cellData.getValue();
                boolean value = data.getOrDefault(terapia, false);
                return new SimpleBooleanProperty(value);
            });

            ObservableList<Terapia> lista = FXCollections.observableArrayList(data.keySet());
            tableViewTerapie.setItems(lista);
            tableViewTerapie.refresh();
        });

        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }
}
