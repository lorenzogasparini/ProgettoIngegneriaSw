package com.progettoingegneriasw.model.Utils;

public class Terapia {
    private int id;
    private int dosiGiornaliere;
    private double quantitaPerDose;
    private String note;
    private Farmaco farmaco;

    public static class Farmaco {
        int id;
        String codice_aic;
        String nome;

        public Farmaco(int id, String codice_aic, String nome) {
            this.id = id;
            this.codice_aic = codice_aic;
            this.nome = nome;
        }
    }

    public Terapia(int id, int dosiGiornaliere, double quantitaPerDose, String note, Farmaco farmaco){
        this.id = id;
        this.dosiGiornaliere = dosiGiornaliere;
        this.quantitaPerDose = quantitaPerDose;
        this.note = note;
        this.farmaco = farmaco;
    }

    private int getId(){ return id; }
    private int getDosiGiornaliere() { return dosiGiornaliere; }
    private double getQuantitaPerDose() { return quantitaPerDose; }
    private String getNote() { return note; }
    private Farmaco getFarmaco() { return farmaco; }

    public String toString(){
        return "id: " + id + "; dosiGiornaliere: " + dosiGiornaliere +
                "; quantiàPerDose: " + quantitaPerDose + "; note: " + note + "; farmaco_id: " + farmaco.id + "; codice_aic" + farmaco.codice_aic + "; farmaco_nome: " + farmaco.nome;
    }

}
