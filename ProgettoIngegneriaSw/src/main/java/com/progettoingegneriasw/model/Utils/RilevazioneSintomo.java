package com.progettoingegneriasw.model.Utils;

import java.sql.Timestamp;

public class RilevazioneSintomo extends Rilevazione{

    private String sintomo;
    private int intensita;

    public RilevazioneSintomo(int idPaziente, Timestamp timestamp, String sintomo, int intensita){
        this(null, idPaziente, timestamp, sintomo, intensita);
    }

    public RilevazioneSintomo(Integer id, int idPaziente, Timestamp timestamp, String sintomo, int intensita){
        super(id, idPaziente, timestamp);
        this.sintomo = sintomo;
        this.intensita = intensita;
    }

    public String getSintomo() { return sintomo; }
    public int getIntensita() { return intensita; }

    public String toString(){
        return "id: " + super.getId() + "; idPaziente: " + super.getIdPaziente() + "; timestamp: " + super.getTimestamp() +
                "; sintomo: " + sintomo + "; intensita: " + intensita;
    }

}
