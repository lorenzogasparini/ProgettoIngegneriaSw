package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

// todo: rendi tutte le rilevazioni sottoclasse di Rilevazione (da capire se fare interfaccia o classe Astratta)
public class RilevazioneGlicemia extends Rilevazione{

    private final int valore;
    private final Integer gravita;
    private final boolean primaPasto;

    public RilevazioneGlicemia(int id_paziente, Timestamp timestamp, int valore, boolean primaPasto){
        this(null, id_paziente, timestamp, valore, primaPasto);
    }

    public RilevazioneGlicemia(Integer id, int id_paziente, Timestamp timestamp, int valore, boolean primaPasto){
        super(id, id_paziente, timestamp);
        this.valore = valore;
        this.gravita = getGravitaValoreGlicemia(this);
        this.primaPasto =primaPasto;
    }

    public int getValore() { return valore; }
    public Integer getGravita() { return  gravita; }
    public boolean getPrimaPasto() { return primaPasto; }

    public String toString(){
        return super.toString() + "; valore: " + valore + "; gravità: " + gravita + "; primaPasto: " + primaPasto;
    }

    private int getGravitaValoreGlicemia(RilevazioneGlicemia rilevazioneGlicemia) {
        int valore = rilevazioneGlicemia.getValore();
        boolean primaPasto = rilevazioneGlicemia.getPrimaPasto();

        if (primaPasto) {
            if (valore >= 80 && valore <= 130) return 0; // normale
            else if ((valore >= 131 && valore <= 160) || (valore >= 70 && valore <= 79)) return 1; // lieve
            else if ((valore >= 161 && valore <= 200) || (valore >= 60 && valore <= 69)) return 2; // moderata
            else if (valore > 200 || valore < 60) return 3; // grave
        } else {
            if (valore <= 180 && valore >= 80) return 0; // normale
            else if ((valore >= 181 && valore <= 200) || (valore >= 70 && valore <= 79)) return 1; // lieve
            else if ((valore >= 201 && valore <= 250) || (valore >= 60 && valore <= 69)) return 2; // moderata
            else if (valore > 250 || valore < 60) return 3; // grave
        }

        // Fallback in case the value is somehow invalid
        return -1;
    }

}
