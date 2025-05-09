package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.UserInterface;

import java.sql.Date;
import java.sql.Timestamp;

public interface Paziente extends UserInterface {
    // todo: altri metodi esclusivi dei pazienti
    public String getEmail();
    public int getIdMedico();
    public Date getDataNascita();
    public double getPeso();
    public String getProvinciaResidenza();
    public String getComuneResidenza();
    public String getNotePaziente();

}
