package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.UserInterface;

import java.sql.Date;

public interface Paziente extends UserInterface {
    // todo: altri metodi esclusivi dei pazienti
    public String getEmail();
    public Integer getIdMedico();
    public Date getDataNascita();
    public Double getPeso();
    public String getProvinciaResidenza();
    public String getComuneResidenza();
    public String getNotePaziente();
    public String getProfileImageName();
    public void setProfileImageName(String newProfileImageName);
}
