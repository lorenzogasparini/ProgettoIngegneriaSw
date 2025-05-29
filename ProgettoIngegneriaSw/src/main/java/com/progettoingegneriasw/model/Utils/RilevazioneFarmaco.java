package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class RilevazioneFarmaco extends Rilevazione{

    private final  int id_farmaco;
    private final double quantita;
    private final String note;

    public RilevazioneFarmaco(int idPaziente, int idFarmaco, Timestamp timestamp, double quantita, String note){
        this(null, idPaziente, idFarmaco, timestamp, quantita, note);
    }

    public RilevazioneFarmaco(Integer id, int idPaziente, int idFarmaco, Timestamp timestamp, double quantita, String note){
        super(id, idPaziente, timestamp);
        this.id_farmaco = idFarmaco;
        this.quantita = quantita;
        this.note = note;
    }

    public int getIdFarmaco(){ return id_farmaco; }
    public double getQuantita() { return  quantita; }
    public String getNote() { return note; }

    public String toString(){
        return super.toString() + "; id_farmaco: " + id_farmaco + "; quantità: " + quantita + "; note: " + note;
    }

}
