package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

// todo: rendi tutte le rilevazioni sottoclasse di Rilevazione (da capire se fare interfaccia o classe Astratta)
public class RilevazioneGlicemia extends Rilevazione{

    private int valore;
    private Integer gravita;
    private boolean prima_pasto;

    public RilevazioneGlicemia(int id_paziente, Timestamp timestamp, int valore, boolean prima_pasto){
        this(null, id_paziente, timestamp, valore, prima_pasto);
    }

    public RilevazioneGlicemia(Integer id, int id_paziente, Timestamp timestamp, int valore, boolean prima_pasto){
        super(id, id_paziente, timestamp);
        this.valore = valore;
        this.gravita = getGravitaValoreGlicemia(this);
        this.prima_pasto =prima_pasto;
    }

    public int getValore() { return valore; }
    public Integer getGravita() { return  gravita; }
    public boolean getPrimaPasto() { return prima_pasto; }

    public String toString(){
        return "id: " + super.getId() + "; idPaziente: " + super.getIdPaziente() + "; timestamp: " + super.getTimestamp() +
               "; valore: " + valore + "; prima_pasto: " + prima_pasto;
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
