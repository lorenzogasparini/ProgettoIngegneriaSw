package com.progettoingegneriasw.model;

import com.progettoingegneriasw.model.Admin.Admin;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Paziente.Paziente;

public class User implements UserInterface{ // todo: rendere questa classe astratta
    private String id;
    private String username;
    private String password;
    
    /**
     * Constructor for creating a user with username and password
     * 
     * @param username The user's username
     * @param password The user's password (stored in plain text for educational purposes)
     */
    public User(String username, String password) {
        this.id = username; // Using username as ID for simplicity
        this.username = username;
        this.password = password;
    }
    
    /**
     * Factory method to create a new user
     *
     * @param username The user's username
     * @param password The user's password
     * @return A new User object
     */
    public static User create(String username, String password) {
        return new User(username, password);
    }
    
    /**
     * Verify if the provided password matches the stored password
     * 
     * @param password The password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    /**
     * Get the user's ID
     * 
     * @return The user's ID (same as username in this implementation)
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the user's username
     * 
     * @return The user's username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Get the user's password
     * 
     * @return The user's password
     */
    public String getPassword() {
        return password;
    }

    public UserTypes getUserType(){
        if (this instanceof Admin)
            return UserTypes.Admin;
        else if(this instanceof Medico)
            return UserTypes.Medico;
        else if(this instanceof Paziente)
            return UserTypes.Paziente;
        else
            throw new UserTypeNotFoundException("This user type is not found");
    }

    public boolean isAdmin() {
        return this.getUserType().equals(UserTypes.Admin);
    }

    public boolean isMedico() {
        return this.getUserType().equals(UserTypes.Medico);
    }

    public boolean isPaziente() {
        return this.getUserType().equals(UserTypes.Paziente);
    }
}