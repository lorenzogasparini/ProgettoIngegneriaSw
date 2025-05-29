package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.User;
import java.sql.Date;

public class PazienteUser extends User implements Paziente{

    // todo: ALLA FINE, per limitare i problemi capire se è possibile lasciare i campi final
    private final String email;
    private final Integer idDiabetologo;
    private final Date dataNascita;
    private final Double peso;
    private final String provinciaResidenza;
    private final String comuneResidenza;
    private final String notePaziente;


    /**
     * Constructor for creating a user with username
     *
     * @param username The user's username
     */
    public PazienteUser(String username) {
        this(null, username, null, null, null, null, null,
                null, null, null, null, null);
    }

    public PazienteUser(String username, String password, String nome, String cognome, String email,
                        Integer idDiabetologo, Date dataNascita, Double peso, String provinciaResidenza,
                        String comuneResidenza, String notePaziente){
        this(null, username, password, nome, cognome, email, idDiabetologo, dataNascita, peso, provinciaResidenza,
                comuneResidenza, notePaziente);
    }

    public PazienteUser(Integer id, String username, String password, String nome, String cognome, String email,
                        Integer idDiabetologo, Date dataNascita, Double peso, String provinciaResidenza,
                        String comuneResidenza, String notePaziente){
        super(id, username, password, nome, cognome);
        this.email = email;
        this.idDiabetologo = idDiabetologo;
        this.dataNascita = dataNascita;
        this.peso = peso;
        this.provinciaResidenza = provinciaResidenza;
        this.comuneResidenza = comuneResidenza;
        this.notePaziente = notePaziente;
    }

    public String toString(){
        return super.toString() + "; email: " + email + "; idDiabetologo: " + idDiabetologo + "; dataNascita: " +
                dataNascita + "; peso: " + peso + "; provinciaResidenza: " + provinciaResidenza +
                "; comuneResidenza: " + comuneResidenza + "; notePaziente: " + notePaziente;
    }

    @Override
    public String getSQLTableName() {
        return PazienteDAO.getInstance().getSQLTableName();
    }


    /// Metodi per gli attributi del paziente
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public int getIdMedico() {
        return idDiabetologo;
    }

    @Override
    public Date getDataNascita() {
        return dataNascita;
    }

    @Override
    public double getPeso() {
        return peso;
    }

    @Override
    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    @Override
    public String getComuneResidenza() {
        return comuneResidenza;
    }

    @Override
    public String getNotePaziente() {
        return notePaziente;
    }
}
