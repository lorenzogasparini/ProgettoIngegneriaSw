package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

// todo: rendi tutte le rilevazioni sottoclasse di Rilevazione (da capire se fare interfaccia o classe Astratta)
public class RilevazioneGlicemia extends Rilevazione{

    private int valore;
    private boolean prima_pasto;

    public RilevazioneGlicemia(int id, int id_paziente, Timestamp timestamp, int valore, boolean prima_pasto){
        super(id, id_paziente, timestamp);
        this.valore = valore;
        this.prima_pasto =prima_pasto;
    }

    public int getValore() { return valore; }
    public boolean getPrimaPasto() { return prima_pasto; }

    public String toString(){
        return "id: " + super.getId() + "; idPaziente: " + super.getIdPaziente() + "; timestamp: " + super.getTimestamp() +
               "; valore: " + valore + "; prima_pasto: " + prima_pasto;
    }

}
