package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.User;

public class MedicoUser extends User implements Medico{

    private final String email;

    /**
     * Constructor for creating a user with username and password
     *
     * @param username The user's username
     */
    public MedicoUser(String username) {
        this(null, username, null, null, null, null);
    }

    public MedicoUser(MedicoUser medicoUser, String newPassword){
        this(medicoUser.getId(), medicoUser.getUsername(), newPassword, medicoUser.getNome(),
                medicoUser.getCognome(), medicoUser.email);
    }

    public MedicoUser(String username, String password) {
        this(null, username, password, null, null, null);
    }

    public MedicoUser(String username, String password, String nome, String cognome, String email){
        this(null, username, password, nome, cognome, email);
    }

    public MedicoUser(Integer id, String username, String password, String nome, String cognome, String email){
        super(id, username, password, nome, cognome);
        this.email = email;
    }

    public String toString(){
        return super.toString() + "; email: " + email;
    }

    @Override
    public String getSQLTableName() {
        return MedicoDAO.getInstance().getSQLTableName();
    }

    /// Metodi per gli attributi del medico
    @Override
    public String getEmail() {
        return email;
    }
}
