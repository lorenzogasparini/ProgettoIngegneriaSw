package com.progettoingegneriasw.model;

public interface UserInterface {
    public boolean checkPassword(String password);
    public Integer getId();
    public String getUsername();
    public String getPassword();
    public String getNome();
    public String getCognome();
    public Boolean isAdmin();

    public String toString();
    public String getSQLTableName();
}
