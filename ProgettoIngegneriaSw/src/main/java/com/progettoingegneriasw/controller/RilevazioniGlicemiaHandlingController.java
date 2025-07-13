package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;

public class RilevazioniGlicemiaHandlingController {

    @FXML private Label labelRisultato;

    @FXML private TableView<RilevazioneGlicemia> tableViewRilevazioni;
    @FXML private TableColumn<RilevazioneGlicemia, Integer> id;
    @FXML private TableColumn<RilevazioneGlicemia, Integer> valore;
    @FXML private TableColumn<RilevazioneGlicemia, Integer> gravita;
    @FXML private TableColumn<RilevazioneGlicemia, Boolean> primaPasto;
    @FXML private TableColumn<RilevazioneGlicemia, Timestamp> timestamp;

    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private LineChart<String, Number> lineChart;

    @FXML private VBox pannelloVisualizzaRisultato;

    private ObservableList<RilevazioneGlicemia> rilglicemia;

    public void initialize() throws SQLException {
        id.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Integer>("id"));
        timestamp.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Timestamp>("timestamp"));
        setTimestampFormat();
        valore.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Integer>("valore"));
        gravita.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Integer>("gravita"));
        primaPasto.setCellValueFactory(new PropertyValueFactory<RilevazioneGlicemia, Boolean>("primaPasto"));

        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        RilevazioneGlicemia[] rilevazioniGlicemia = medicoDAO.getRilevazioniGlicemia(PazientiController.selectedUser.getUsername());
        rilglicemia = FXCollections.observableArrayList(rilevazioniGlicemia);
        tableViewRilevazioni.setItems(rilglicemia);
        visualizzaGrafico();
    }

    private void visualizzaGrafico(){
        lineChart.setTitle("Grafico rilevazioni del livello di glicemia nel sangue");
        yAxis.setLabel("Valore (mg/l)");
        xAxis.setLabel("Data");

        XYChart.Series series = new XYChart.Series();

        for(RilevazioneGlicemia ril : rilglicemia){
            series.getData().add(new XYChart.Data<>(ril.getTimestamp().toString(), ril.getValore()));
        }

        lineChart.getData().add(series);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        Platform.runLater(() -> {
            for (XYChart.Series<String, Number> seriesr : lineChart.getData()) {
                for (XYChart.Data<String, Number> data : seriesr.getData()) {
                    Node node = data.getNode();
                    if (node != null) {
                        node.setOnMouseClicked(event -> {
                            pannelloVisualizzaRisultato.setVisible(true);
                            labelRisultato.setText("Rilevazione glicemia nel sangue: " + PazientiController.selectedUser.getNome() + ", " + PazientiController.selectedUser.getCognome() + "   ->   " + data.getYValue() + ", " + data.getXValue());
                        });
                    }
                }
            }
        });
    }

    ///  this method set the timestamp format to 2025-05-17 08:00 instead of 2025-05-17 08:00:00.0
    private void setTimestampFormat(){
        timestamp.setCellFactory(column -> new TableCell<RilevazioneGlicemia, Timestamp>() {
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

}
