package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public abstract class Rilevazione {

    /**
     * @implNote rilevazione common parameters
     */
    private final Integer id;
    private final int idPaziente;
    private final Timestamp timestamp;

    public Rilevazione(Integer id, int idPaziente, Timestamp timestamp){
        this.id = id;
        this.idPaziente = idPaziente;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public int getIdPaziente() {
        return idPaziente;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String toString(){
        return "id: " + id + "; idPaziente: " + idPaziente + "; timestamp: " + timestamp;
    }
}
