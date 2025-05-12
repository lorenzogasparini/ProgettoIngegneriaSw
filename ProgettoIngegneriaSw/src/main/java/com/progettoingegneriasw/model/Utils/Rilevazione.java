package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public abstract class Rilevazione {

    /**
     * @implNote rilevazione common parameters
     */
    private int id;
    private int id_paziente;
    private Timestamp timestamp;

    public Rilevazione(int id, int id_paziente, Timestamp timestamp){
        this.id = id;
        this.id_paziente = id_paziente;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getIdPaziente() {
        return id_paziente;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
