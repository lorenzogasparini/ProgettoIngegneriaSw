package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class RilevazioneFarmaco extends Rilevazione{

    private final Farmaco farmaco;
    private final double quantita;
    private final String note;

    /*
    int id;
    String codiceAic;
    String nome;
     */

    public RilevazioneFarmaco(int idPaziente, Farmaco farmaco, Timestamp timestamp, double quantita, String note){
        this(null, idPaziente, farmaco, timestamp, quantita, note);
    }

    public RilevazioneFarmaco(Integer id, int idPaziente, Farmaco farmaco, Timestamp timestamp, double quantita, String note){
        super(id, idPaziente, timestamp);
        this.farmaco = farmaco;
        this.quantita = quantita;
        this.note = note;
    }

    public Farmaco getFarmaco(){ return farmaco; }
    public double getQuantita() { return  quantita; }
    public String getNote() { return note; }

    public String toString(){
        return super.toString() + "; id_farmaco: " + farmaco.toString() + "; quantità: " + quantita + "; note: " + note;
    }

}
