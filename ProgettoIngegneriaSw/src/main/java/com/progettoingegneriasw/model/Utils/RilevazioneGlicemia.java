package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

// todo: rendi tutte le rilevazioni sottoclasse di Rilevazione (da capire se fare interfaccia o classe Astratta)
public class RilevazioneGlicemia {
    private int id;
    private int id_paziente;
    private Timestamp timestamp;
    private int valore;
    private boolean prima_pasto;

    public RilevazioneGlicemia(int id, int id_paziente, Timestamp timestamp, int valore, boolean prima_pasto){
        this.id = id;
        this.id_paziente = id_paziente;
        this.timestamp = timestamp;
        this.valore = valore;
        this.prima_pasto =prima_pasto;
    }

    public int getId(){ return id; }
    public int getIdPaziente(){ return id_paziente; }
    public Timestamp getTimestamp() { return timestamp; }
    public int getValore() { return valore; }
    public boolean getPrimaPasto() { return prima_pasto; }

}
