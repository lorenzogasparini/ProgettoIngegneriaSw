package com.progettoingegneriasw.model.Admin;

import com.progettoingegneriasw.model.User;

public class AdminUser extends User implements Admin{


    public AdminUser(String username){
        super(username);
    }

    /**
     * Constructor for creating a user with username and password
     *
     * @param username The user's username
     * @param password The user's password (stored in plain text for educational purposes)
     */
    public AdminUser(String username, String password, String nome, String cognome) {
        this(null, username, password, nome, cognome);
    }


    public AdminUser(Integer id, String username, String password, String nome, String cognome) {
        super(id, username, password, nome, cognome);
    }

    public String toString(){
        return super.toString();
    }

    /// NON HA METODI AdminUser perché non ha attributi in più di User
}
