package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class RilevazioneFarmaco extends Rilevazione{

    private final Farmaco farmaco;
    private final double quantita;
    private final String noteRilevazione;

    public RilevazioneFarmaco(int idPaziente, Farmaco farmaco, Timestamp timestamp, double quantita, String noteRilevazione){
        this(null, idPaziente, farmaco, timestamp, quantita, noteRilevazione);
    }

    public RilevazioneFarmaco(Integer id, int idPaziente, Farmaco farmaco, Timestamp timestamp, double quantita, String noteRilevazione){
        super(id, idPaziente, timestamp);
        this.farmaco = farmaco;
        this.quantita = quantita;
        this.noteRilevazione = noteRilevazione;
    }

    public Farmaco getFarmaco(){ return farmaco; }
    public double getQuantita() { return  quantita; }
    public String getNoteRilevazione() { return noteRilevazione; }

    public String toString(){
        return super.toString() + "; id_farmaco: " + farmaco.toString() + "; quantità: " + quantita + "; noteRilevazione: " + noteRilevazione;
    }

}
