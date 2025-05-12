package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class RilevazioneFarmaco extends Rilevazione{

    private int id_farmaco;
    private double quantita;
    private String note;

    public RilevazioneFarmaco(int id, int idPaziente, int idFarmaco, Timestamp timestamp, double quantita, String note){
        super(id, idPaziente, timestamp);
        this.id_farmaco = idFarmaco;
        this.quantita = quantita;
        this.note = note;
    }

    public int getIdFarmaco(){ return id_farmaco; }
    public double getQuantita() { return  quantita; }
    public String getNote() { return note; }

    public String toString(){
        return "id: " + super.getId() + "; idPaziente: " + super.getIdPaziente() + "; id_farmaco: " + id_farmaco + "; timestamp: " +
                super.getTimestamp() + "; quantità: " + quantita + "; note: " + note;
    }

}
