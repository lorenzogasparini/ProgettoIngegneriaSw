package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class Log {
    private final  Integer id;
    private final int id_paziente;
    private final int id_diabetologo;
    private final String azione;
    private final Timestamp timestamp;

    public Log(int id_paziente, int id_diabetologo, String azione, Timestamp timestamp){
        this(null, id_paziente, id_diabetologo, azione, timestamp);
    }

    public Log(Integer id, int id_paziente, int id_diabetologo, String azione, Timestamp timestamp){
        this.id = id;
        this.id_paziente = id_paziente;
        this.id_diabetologo = id_diabetologo;
        this.azione = azione;
        this.timestamp = timestamp;
    }

    public int getId(){ return id; }
    public int getIdPaziente() { return id_paziente; }
    public int getIdDiabetologo() { return id_diabetologo; }
    public String getAzione() { return azione; }
    public Timestamp getTimestamp() { return timestamp; }

    public String toString(){
        return "id: " + id + "; id_paziente: "+ id_paziente + "; id_diabetologo: " + id_diabetologo +
                "; azione: " + azione + "; timestamp: " + timestamp;
    }
}
