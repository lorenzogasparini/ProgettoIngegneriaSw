package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class Log {
    private final  Integer id;
    private final Integer id_paziente;
    private final Integer id_diabetologo;
    private final LogAction azione;
    private final Timestamp timestamp;

    public Log(Integer id_paziente, Integer id_diabetologo, LogAction azione, Timestamp timestamp){
        this(null, id_paziente, id_diabetologo, azione, timestamp);
    }

    public Log(Integer id, Integer id_paziente, Integer id_diabetologo, LogAction azione, Timestamp timestamp){
        this.id = id;
        this.id_paziente = id_paziente;
        this.id_diabetologo = id_diabetologo;
        this.azione = azione;
        this.timestamp = timestamp;
    }

    public Integer getId(){ return id; }
    public Integer getIdPaziente() { return id_paziente; }
    public Integer getIdDiabetologo() { return id_diabetologo; }
    public LogAction getAzione() { return azione; }
    public Timestamp getTimestamp() { return timestamp; }

    public String toString(){
        return "id: " + id + "; id_paziente: "+ id_paziente + "; id_diabetologo: " + id_diabetologo +
                "; azione: " + azione + "; timestamp: " + timestamp;
    }
}
