package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public abstract class Rilevazione {

    /**
     * @implNote rilevazione common parameters
     */
    private final Integer id;
    private final int id_paziente;
    private final Timestamp timestamp;

    public Rilevazione(Integer id, int id_paziente, Timestamp timestamp){
        this.id = id;
        this.id_paziente = id_paziente;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public int getIdPaziente() {
        return id_paziente;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
