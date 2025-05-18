package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class Alert {
    private int idPaziente;
    private int idRilevazione;
    private AlertType tipoAlert;
    private Timestamp timestamp;
    private boolean letto;

    public Alert(int idPaziente, int idRilevazione, AlertType tipoAlert, Timestamp timestamp, boolean letto){
        this.idPaziente = idPaziente;
        this.idRilevazione = idRilevazione;
        this.tipoAlert = tipoAlert;
        this.timestamp = timestamp;
        this.letto = letto;
    }
}
