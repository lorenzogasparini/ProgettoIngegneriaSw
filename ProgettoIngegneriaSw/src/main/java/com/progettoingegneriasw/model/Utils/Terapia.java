package com.progettoingegneriasw.model.Utils;

public class Terapia {
    private final Integer id;
    private final Farmaco farmaco;
    private final Integer dosiGiornaliere;
    private final Double quantitaPerDose;
    private final String note;

    public Terapia(Integer id){
        this(id, null, null, null, null);
    }

    public Terapia(Farmaco farmaco, Integer dosiGiornaliere, Double quantitaPerDose, String note){
        this(null, farmaco, dosiGiornaliere, quantitaPerDose, note);
    }

    public Terapia(Integer id, Farmaco farmaco, Integer dosiGiornaliere, Double quantitaPerDose, String note){
        this.id = id;
        this.dosiGiornaliere = dosiGiornaliere;
        this.quantitaPerDose = quantitaPerDose;
        this.note = note;
        this.farmaco = farmaco;
    }

    public Integer getId(){ return id; }
    public Farmaco getFarmaco() { return farmaco; }
    public Integer getDosiGiornaliere() { return dosiGiornaliere; }
    public Double getQuantitaPerDose() { return quantitaPerDose; }
    public String getNote() { return note; }


    public String toString(){
        return "id: " + id + "; codice_aic" + farmaco.getCodiceAic() + "; farmaco_nome: " + farmaco.getNome() +
                "; dosiGiornaliere: " + dosiGiornaliere + "; quantitaPerDose: " + quantitaPerDose +
                "; note: " + note + "; farmaco_id: " + farmaco.getId();

    }

}
