package com.progettoingegneriasw.model.Admin;

import com.progettoingegneriasw.model.User;

public class AdminUser extends User implements Admin{


    public AdminUser(String username){
        this(null, username, null, null, null);
    }


    public AdminUser(AdminUser adminUser, String newPassword){
        this(adminUser.getId(), adminUser.getUsername(), newPassword,
                adminUser.getNome(), adminUser.getCognome());
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

    @Override
    public String getSQLTableName() {
        return AdminDAO.getInstance().getSQLTableName();
    }

    /// NON HA METODI AdminUser perché non ha attributi in più di User
}
