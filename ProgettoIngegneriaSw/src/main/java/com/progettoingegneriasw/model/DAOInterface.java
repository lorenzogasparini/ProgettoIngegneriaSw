package com.progettoingegneriasw.model;

public interface DAOInterface {
    public void setConnection(DatabaseManager dbManager);
    public DatabaseManager getConnection();
}
