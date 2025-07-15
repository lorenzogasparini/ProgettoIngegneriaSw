package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Utils.*;

import java.sql.SQLException;
import java.util.Map;

public interface PazienteDAOInterface {
    public Farmaco[] getFarmaciPaziente(String username) throws SQLException;
    public Map<Terapia, Boolean> getTerapieEAssunzioniPaziente(String username) throws SQLException;
    public void setRilevazioneSintomo(RilevazioneSintomo rilevazioneSintomo);
    public void setRilevazioneGlicemia(RilevazioneGlicemia rilevazioneGlicemia);
    public void setRilevazioneFarmaco(RilevazioneFarmaco rilevazioneFarmaco);
    public MedicoUser getMedicoRiferimento(String username);
    public int countAlerts(String username) throws SQLException;

}
