package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class RilevazioneSintomo {
    private int id;
    private int idPaziente;
    private Timestamp timestamp;
    private String sintomo;
    private int intensita;

    public RilevazioneSintomo(int id, int idPaziente, Timestamp timestamp, String sintomo, int intensita){
        this.id = id;
        this.idPaziente = idPaziente;
        this.timestamp = timestamp;
        this.sintomo = sintomo;
        this.intensita = intensita;
    }

    public int getId(){ return id; }
    public int getIdPaziente() { return idPaziente; }
    public Timestamp getTimestamp() { return timestamp; }
    public String getSintomo() { return sintomo; }
    public int getIntensita() { return intensita; }

}
