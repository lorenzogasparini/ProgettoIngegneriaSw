package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.User;

import java.sql.Timestamp;

public class PazienteUser extends User implements Paziente{

    // todo: attributi privati pazienti
    private Timestamp dataNascita;



    /**
     * Constructor for creating a user with username and password
     *
     * @param username The user's username
     * @param password The user's password (stored in plain text for educational purposes)
     */
    public PazienteUser(String username, String password) {
        super(username, password);
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public int getIdMedico() {
        return 0;
    }

    @Override
    public Timestamp getDataNascita() {
        return dataNascita;
    }

    @Override
    public double getPeso() {
        return 0;
    }

    @Override
    public String getProvinciaResidenza() {
        return "";
    }

    @Override
    public String getComuneResidenza() {
        return "";
    }

    @Override
    public String getNotePaziente() {
        return "";
    }
}
