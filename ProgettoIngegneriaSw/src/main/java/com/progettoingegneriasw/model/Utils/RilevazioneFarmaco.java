package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class RilevazioneFarmaco {
    private int id;
    private int idPaziente;
    private int idFarmaco;
    private Timestamp timestamp;
    private double quantita;
    private String note;

    public RilevazioneFarmaco(int id, int idPaziente, int idFarmaco, Timestamp timestamp, double quantita, String note){
        this.id = id;
        this.idPaziente = idPaziente;
        this.idFarmaco = idFarmaco;
        this.timestamp = timestamp;
        this.quantita = quantita;
        this.note = note;
    }

    public int getId(){ return id; }
    public int getIdPaziente(){ return idPaziente; }
    public int getIdFarmaco(){ return idFarmaco; }
    public Timestamp getTimestamp(){ return timestamp; }
    public double getQuantita() { return  quantita; }
    public String getNote() { return note; }

    public String toString(){
        return "id: " + id + "; idPaziente: " + idPaziente + "; idFarmaco: " + idFarmaco + "; timestamp: " +
                timestamp + "; quantità: " + quantita + "; note: " + note;
    }

}
