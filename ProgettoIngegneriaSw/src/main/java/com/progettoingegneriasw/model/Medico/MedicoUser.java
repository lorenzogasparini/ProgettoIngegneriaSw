package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.User;

public class MedicoUser extends User implements Medico{
    /**
     * Constructor for creating a user with username and password
     *
     * @param username The user's username
     * @param password The user's password (stored in plain text for educational purposes)
     */
    public MedicoUser(String username, String password) {
        super(username, password);
    }

    @Override
    public String getEmail() {
        return "";
    }
}
