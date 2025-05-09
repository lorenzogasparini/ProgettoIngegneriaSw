package com.progettoingegneriasw.model;

public interface UserInterface {
    public boolean checkPassword(String password);
    public String getId();
    public String getUsername();
    public String getPassword();
    public boolean isAdmin();
}
