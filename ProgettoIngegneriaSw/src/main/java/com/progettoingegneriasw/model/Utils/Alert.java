package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class Alert {
    private final int idPaziente;
    private final int idRilevazione;
    private final AlertType tipoAlert;
    private final Timestamp timestamp;
    private final boolean letto;

    public Alert(int idPaziente, int idRilevazione, AlertType tipoAlert, Timestamp timestamp, boolean letto){
        this.idPaziente = idPaziente;
        this.idRilevazione = idRilevazione;
        this.tipoAlert = tipoAlert;
        this.timestamp = timestamp;
        this.letto = letto;
    }
}
