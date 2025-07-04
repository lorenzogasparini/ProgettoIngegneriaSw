package com.progettoingegneriasw.model.Utils;

import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoUser;

import java.util.Objects;

public class Terapia {
    private final Integer id;
    private final Medico medico;
    private final Farmaco farmaco;
    private final Integer dosiGiornaliere;
    private final Double quantitaPerDose;
    private final String note;

    public Terapia(Integer id){
        this(id, null, null, null, null, null);
    }

    public Terapia(Medico medico, Farmaco farmaco, Integer dosiGiornaliere, Double quantitaPerDose, String note){
        this(null, medico, farmaco, dosiGiornaliere, quantitaPerDose, note);
    }

    public Terapia(Integer id, Medico medico, Farmaco farmaco, Integer dosiGiornaliere, Double quantitaPerDose, String note){
        this.id = id;
        this.medico = medico;
        this.farmaco = farmaco;
        this.dosiGiornaliere = dosiGiornaliere;
        this.quantitaPerDose = quantitaPerDose;
        this.note = note;
    }

    public Integer getId(){ return id; }
    public Medico getMedico(){ return medico; }
    public Farmaco getFarmaco() { return farmaco; }
    public Integer getDosiGiornaliere() { return dosiGiornaliere; }
    public Double getQuantitaPerDose() { return quantitaPerDose; }
    public String getNote() { return note; }


    public String toString(){
        return "id: " + id + "; medicoId: " + medico.getId() + "; medicoUsername: " + medico.getUsername() +
                "; medicoNome: " + medico.getNome() + "; medicoCognome: " + medico.getCognome() +
                "; medicoEmail: " + medico.getEmail() + "; medicoProfileImageName: " + medico.getProfileImageName() +
                "; codice_aic" + farmaco.getCodiceAic() + "; farmaco_nome: " + farmaco.getNome() +
                "; dosiGiornaliere: " + dosiGiornaliere + "; quantitaPerDose: " + quantitaPerDose +
                "; note: " + note + "; farmaco_id: " + farmaco.getId();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terapia terapia = (Terapia) o;
        return Objects.equals(id, terapia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
