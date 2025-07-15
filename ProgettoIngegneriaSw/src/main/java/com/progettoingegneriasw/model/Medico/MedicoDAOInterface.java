package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Utils.*;

import java.sql.Date;
import java.sql.SQLException;

public interface MedicoDAOInterface {
    public Paziente[] getAllPazienti();
    public Paziente[] getPazientiAssegnati(String medicoUsername) throws SQLException;
    public Patologia[] getPatologiePaziente(String username) throws SQLException;
    public void setPatologiaPaziente(Patologia patologia, String username, Terapia terapia, Date dataDiagnosi, String notePatologia);
    public Patologia getPatologiaFromId(int idPatologia) throws SQLException;
    public Patologia[] getPatologie() throws SQLException;
    public Patologia[] getPatologie(String nomePatologia) throws SQLException;
    public void addPatologia(Patologia patologia);
    public Terapia getTerapiaFromId(Integer idTerapia);
    public Terapia[] getTerapiePaziente(String username) throws SQLException;
    public void setTerapiaPaziente(Terapia terapia, String usernamePaziente, Patologia patologia, String notePatologiaPaziente);
    public void updateTerapiaPaziente(Terapia terapia);
    public void deleteTerapia(Terapia terapia) throws SQLException;
    public Paziente getPazienteFromTerapia(Terapia terapia);
    public Medico getDiabetologoFromTerapia(Terapia terapia);
    public RilevazioneSintomo[] getRilevazioniSintomo();
    public RilevazioneSintomo[] getRilevazioniSintomo(String usernamePaziente);
    public RilevazioneGlicemia[] getRilevazioniGlicemia();
    public RilevazioneGlicemia[] getRilevazioniGlicemia(String usernamePaziente);
    public RilevazioneFarmaco[] getRilevazioniFarmaco() throws SQLException;
    public RilevazioneFarmaco[] getRilevazioniFarmaco(String usernamePaziente) throws SQLException;
    public Alert[] getAlert() throws SQLException;
    public Farmaco[] getFarmaci() throws SQLException;
    public Alert[] getAllAlerts(AlertFilter filter) throws SQLException;
    public Alert[] getAlertsPazientiCurati(AlertFilter filter) throws SQLException;
    public Alert[] getAlertPaziente(String usernamePaziente) throws SQLException;
    public Rilevazione getRilevazioneFormAlert(Alert alert);
}
