package com.progettoingegneriasw.model;

public interface UserInterface {
    public boolean checkPassword(String password);
    public int getId();
    public String getUsername();
    public String getPassword();
    public String getNome();
    public String getCognome();
    public boolean isAdmin();

    public String toString();
}
