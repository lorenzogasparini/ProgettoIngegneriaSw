package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class Alert {

    private final Integer id;
    private final Integer idPaziente;
    private final Integer idRilevazione;
    private final AlertType tipoAlert;
    private final Timestamp timestamp;
    private final Boolean letto;

    public Alert(Integer id){
        this(id, null, null, null, null, null);
    }

    public Alert(Integer idPaziente, Integer idRilevazione, AlertType tipoAlert, Timestamp timestamp, Boolean letto){
        this(null, idPaziente, idRilevazione, tipoAlert, timestamp, letto);
    }

    public Alert(Integer id, Integer idPaziente, Integer idRilevazione, AlertType tipoAlert, Timestamp timestamp, Boolean letto) {
        this.id = id;
        this.idPaziente = idPaziente;
        this.idRilevazione = idRilevazione;
        this.tipoAlert = tipoAlert;
        this.timestamp = timestamp;
        this.letto = letto;
    }

    public Integer getId() { return id; }
    public Integer getIdPaziente() { return idPaziente; }
    public Integer getIdRilevazione() { return idRilevazione; }
    public AlertType getTipoAlert() { return tipoAlert; }
    public Timestamp getTimestamp() { return timestamp; }
    public Boolean getLetto() { return letto; }

    public String toString(){
        return "id: " + id + "; idPaziente: " + idPaziente + "; idRilevazione: " + idRilevazione +
                "; tipoAlert: " + tipoAlert + "; timestamp: " + timestamp + "; letto: " + letto;
    }

    public Boolean isLetto(){
        return getLetto();
    }
}
