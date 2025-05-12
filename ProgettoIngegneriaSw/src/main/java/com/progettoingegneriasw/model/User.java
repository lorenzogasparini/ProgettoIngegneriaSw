package com.progettoingegneriasw.model;

import com.progettoingegneriasw.model.Admin.Admin;
import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;

public class User implements UserInterface{ // todo: rendere questa classe astratta finiti i controller
    private Integer id;
    private String username;
    private String password;
    private String nome;
    private String cognome;

    public User(String username){
        this(null, username, null, null, null);
    }

    public User(String username, String password){ // todo: probabilmente sarà da cancellare questo costruttore e tenere solo gli altri 2
        this(null, username, password, null, null);
    }

    public User(String username, String password, String nome, String cognome){
        this(null, username, password, nome, cognome);
    }

    public User(Integer id, String username, String password, String nome, String cognome){
        this.id = id;
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }


    // todo: capire se utilizzare al posto di questo create saveUser() contenuto in UserDAO
//    /**
//     * Factory method to create a new user
//     *
//     * @param username The user's username
//     * @param password The user's password
//     * @return A new User object
//     */
//    public static User create(String username, String password, String nome, String cognome) {
//        User user =  new User(username, password, nome, cognome);
//        UserDAO userDAO = UserDAO.getInstance();
//
//        return user;
//    }
    
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
    public int getId() {
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

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getCognome() {
        return cognome;
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

    public String toString(){
        return "id: " + id + "; username: " + username + "; password: " + password + "; nome: " + nome +
                "; cognome: " + cognome;
    }

    // todo: rimuovere quando si render User stratta
    @Override
    public String getSQLTableName() {
        return "";
    }

}