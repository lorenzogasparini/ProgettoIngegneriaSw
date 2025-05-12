package com.progettoingegneriasw.model;

import com.progettoingegneriasw.model.Admin.Admin;
import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Paziente.PazienteUser;

import java.sql.Date;

public class UserDAO { // todo: è corretto rendere questa classe abstract???
    private final DatabaseManager dbManager;
    // private Map<String, User> userCache = new HashMap<>(); // this contains all the users --> NOT USED
    public static User loggedUser; // this contains the current logged user; //todo: capire dove metterlo

    public UserDAO() {
        this.dbManager = new DatabaseManager();
        //refreshUserCache();
    }

    // todo: capire quando chiamare questo metodo (probabilmente dopo che l'utente si logga) e metterlo private finiti i test
    public void refreshLoggedUser(User currUser){
        loggedUser = currUser;
    }

    public DatabaseManager getConnection(){
        return dbManager;
    }

    /**
     * Save a user to the right table
     */
    public void saveUser(User user) { // Funzionante!
        boolean success = false;

        try {
            if (user.isAdmin()) {
                AdminDAO adminDAO = AdminDAO.getInstance();
                Admin admin = (Admin) user;

                success = dbManager.executeUpdate(
                        "INSERT OR REPLACE INTO " + adminDAO.getSQLTableName() + " (username, password, nome, cognome) VALUES (?, ?, ?, ?)",
                        admin.getUsername(),
                        admin.getPassword(),
                        admin.getNome(),
                        admin.getCognome()
                );
            } else if (user.isMedico()) {
                MedicoDAO medicoDAO = MedicoDAO.getInstance();
                Medico medico = (Medico) user;

                success = dbManager.executeUpdate(
                        "INSERT OR REPLACE INTO " + medicoDAO.getSQLTableName() + " (username, password, nome, cognome, email) VALUES (?, ?, ?, ?, ?)",
                        medico.getUsername(),
                        medico.getPassword(),
                        medico.getEmail(),
                        medico.getNome(),
                        medico.getCognome()
                );
            } else if (user.isPaziente()) {
                PazienteDAO pazienteDAO = PazienteDAO.getInstance();
                Paziente paziente = (Paziente) user;

                success = dbManager.executeUpdate(
                        "INSERT OR REPLACE INTO " + pazienteDAO.getSQLTableName() +
                                " (username, password, nome, cognome, email, id_diabetologo, " +
                                "data_nascita, peso, provincia_residenza, comune_residenza, note_paziente) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        paziente.getUsername(),
                        paziente.getPassword(),
                        paziente.getNome(),
                        paziente.getCognome(),
                        paziente.getEmail(),
                        paziente.getIdMedico(),
                        paziente.getDataNascita().toString(), // using .toString() insert in format: 'aaaa-mm-dd'
                        paziente.getPeso(),
                        paziente.getProvinciaResidenza(),
                        paziente.getComuneResidenza(),
                        paziente.getNotePaziente()
                );
            }
        } catch (Exception e) {
            success = false;
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /*
    /**
     * Delete a user from the repository
     */
    public void deleteUser(String username) { // todo: da testare

        User user;

        if(userExists(username)){
            user = getUser(username);
        }else{
            return;
        }

        boolean success = false;

        if (user != null && !user.isAdmin()) {
            try {
                if (user.isMedico()) {
                    MedicoDAO medicoDAO = MedicoDAO.getInstance();
                    success = dbManager.executeUpdate(
                            "DELETE FROM " + medicoDAO.getSQLTableName() + " WHERE username = ?",
                            username
                    );
                } else if (user.isPaziente()) {
                    PazienteDAO pazienteDAO = PazienteDAO.getInstance();
                    success = dbManager.executeUpdate(
                            "DELETE FROM " + pazienteDAO.getSQLTableName() + " WHERE username = ?",
                            username
                    );
                }
            } catch (Exception e) {
                System.err.println("Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //todo: rimuovere se non necessaria (era una funzione per testare il db)
//        public void printAllPazientiDB(){
//        dbManager.executeQuery(
//                "SELECT username, password FROM paziente",
//                rs -> {
//                    while (rs.next()) {
//                        String username = rs.getString("username");
//                        String password = rs.getString("password");
//
//                        System.out.println("username: " + username + "; password: " + password);
//                    }
//                    return null;
//                }
//        );
//    }


    /**
     * Get a user by username
     * Try first searching in table 'amministratore' after in table 'diabetologo' and last but not least on 'paziente'
     */
    public User getUser(String username) { // Funziona!
        // Try to get Admin
        User user = dbManager.executeQuery(
                "SELECT id, username, password, nome, cognome FROM amministratore WHERE username = ?",
                rs -> {
                    if (rs.next()) {
                        return new AdminUser(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("nome"),
                                rs.getString("cognome")
                        );
                    }
                    return null;
                },
                username
        );

        if (user != null) return user;

        // Try to get Medico
        user = dbManager.executeQuery(
                "SELECT id, username, password, nome, cognome, email FROM diabetologo WHERE username = ?",
                rs -> {
                    if (rs.next()) {
                        return new MedicoUser(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email")
                        );
                    }
                    return null;
                },
                username
        );

        if (user != null) return user;

        // Try to get Paziente
        user = dbManager.executeQuery(
                "SELECT id, username, password, nome, cognome, email, id_diabetologo, data_nascita, peso," +
                        " provincia_residenza, comune_residenza, note_paziente FROM paziente WHERE username = ?",
                rs -> {
                    if (rs.next()) {
                        return new PazienteUser(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email"),
                                rs.getInt("id_diabetologo"),
                                java.sql.Date.valueOf(rs.getString("data_nascita")),
                                rs.getDouble("peso"),
                                rs.getString("provincia_residenza"),
                                rs.getString("comune_residenza"),
                                rs.getString("note_paziente")
                        );
                    }
                    return null;
                },
                username
        );

        if (user == null)
            System.out.println(username + " not found!");
        return user; // potrebbe essere null (verificarlo quando si chiama il metodo)
    }


    /**
     * Check if a username exists
    */
    public boolean userExists(String username) { // Funziona!
        return getUser(username) != null;
    }


    //todo: quando UserDAO sarà astratto togliere il codice da qui e definire questa funzione solo nei figli
    public String getSQLTableName(){
        return "";
    }


    /**
     *
     * @return the right DAO for the user (AdminDAO, MedicoDAO or PazienteDAO)
     */
    public static UserDAO getLoggedUserDAO(){

        if(loggedUser.isAdmin())
            return AdminDAO.getInstance();
        else if(loggedUser.isMedico())
            return MedicoDAO.getInstance();
        else if(loggedUser.isPaziente())
            return PazienteDAO.getInstance();
        else
            throw new UserTypeNotFoundException("Cannot return correct DAO because User is not among acceptable types");
    }

    /**
     *
     * @param username The given username of the MedicoDAO instance
     * @return The id of the MedicoDAO instance, taken by DB query
     */
    public int getIdFromDB(String username) {   //  Funzionamento corretto

        // todo: capire se questa parte aggiunta è corretta

        String query = "SELECT id FROM " + getUser(username).getSQLTableName() + " WHERE username = ";
        return getConnection().executeQuery(
                query + " ?",
                rs -> {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                    return -1;
                },
                username
        );
    }
}