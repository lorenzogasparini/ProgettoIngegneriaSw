package com.progettoingegneriasw.model.Admin;

import com.progettoingegneriasw.model.User;

public class AdminUser extends User implements Admin{


    /**
     * Constructor for creating a user with username and password
     *
     * @param username The user's username
     * @param password The user's password (stored in plain text for educational purposes)
     */
    public AdminUser(String username, String password) {
        super(username, password);
    }
}
