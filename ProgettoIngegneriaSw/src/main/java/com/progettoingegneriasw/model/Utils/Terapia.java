package com.progettoingegneriasw.model.Utils;

public class Terapia {
    private int id;
    private int idFarmaco;
    private int dosiGiornaliere;
    private double quantitaPerDose;
    private String note;

    public Terapia(int id, int idFarmaco, int dosiGiornaliere, double quantitaPerDose, String note){
        this.id = id;
        this.idFarmaco = idFarmaco;
        this.dosiGiornaliere = dosiGiornaliere;
        this.quantitaPerDose = quantitaPerDose;
        this.note = note;
    }

    private int getId(){ return id; }
    private int getIdFarmaco() { return idFarmaco; }
    private int getDosiGiornaliere() { return dosiGiornaliere; }
    private double getQuantitaPerDose() { return quantitaPerDose; }
    private String getNote() { return note; }

    public String toString(){
        return "id: " + id + "; idFarmaco: " + idFarmaco + "; dosiGiornaliere: " + dosiGiornaliere +
                "; quantiàPerDose: " + quantitaPerDose + "; note: " + note;
    }

}
