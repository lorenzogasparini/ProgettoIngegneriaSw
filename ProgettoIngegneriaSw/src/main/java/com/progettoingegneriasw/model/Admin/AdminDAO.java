package com.progettoingegneriasw.model.Admin;

import com.progettoingegneriasw.model.UserDAO;

public class AdminDAO extends UserDAO {

    private final String SQLTableName = "amministratore";
    private static AdminDAO instance;

    private AdminDAO(){
        // Init DB connection, maybe from a config class
        super();
    }

    public static synchronized AdminDAO getInstance() {
        if (instance == null) {
            instance = new AdminDAO();
        }
        return instance;
    }

    @Override
    public String getSQLTableName(){
        return SQLTableName;
    }

    // todo: aggiungi i metodi per l'Admin (non vanno messi nell'interfaccia perchè non esistono intefaccie per i DAO)
}
