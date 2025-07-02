package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.UserInterface;

public interface Medico extends UserInterface {
    // todo: altri metodi esclusivi dei medici per gli attributi
    public String getEmail();
    public String getProfileImagePath();
    public void setProfileImagePath(String newProfileImagePath);
}
