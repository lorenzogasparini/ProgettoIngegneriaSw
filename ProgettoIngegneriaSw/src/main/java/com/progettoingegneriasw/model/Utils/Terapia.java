package com.progettoingegneriasw.model.Utils;

public class Terapia {
    private int id;
    private int dosiGiornaliere;
    private double quantitaPerDose;
    private String note;
    private Farmaco farmaco;

    public Terapia(int id, int dosiGiornaliere, double quantitaPerDose, String note, Farmaco farmaco){
        this.id = id;
        this.dosiGiornaliere = dosiGiornaliere;
        this.quantitaPerDose = quantitaPerDose;
        this.note = note;
        this.farmaco = farmaco;
    }

    public int getId(){ return id; }
    public int getDosiGiornaliere() { return dosiGiornaliere; }
    public double getQuantitaPerDose() { return quantitaPerDose; }
    public String getNote() { return note; }
    public Farmaco getFarmaco() { return farmaco; }

    public String toString(){
        return "id: " + id + "; dosiGiornaliere: " + dosiGiornaliere +
                "; quantiàPerDose: " + quantitaPerDose + "; note: " + note + "; farmaco_id: " + farmaco.getId() + "; codice_aic" + farmaco.getCodice_aic() + "; farmaco_nome: " + farmaco.getNome();
    }

}
