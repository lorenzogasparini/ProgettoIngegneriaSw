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

import java.util.HashMap;
import java.util.Map;

public class UserDAO { // todo: rendere questa classe abstract
    private final DatabaseManager dbManager;
    // private Map<String, User> userCache = new HashMap<>(); // this contains all the users --> NOT USED
    private User currentUser; // this contains the current logged user;

    public UserDAO() {
        this.dbManager = new DatabaseManager();
        //refreshUserCache();
    }

    // todo: capire quando chiamare questo metodo (probabilmente dopo che l'utente si logga)
    private void refreshCurrentUser(User currUser){
        currentUser = currUser;
    }

    // NOT USED!
    /**
     * Refresh the user cache from the database
     */
//    private void refreshUserCache() {
//        userCache.clear();
//
//        dbManager.executeQuery(
//            "SELECT username, password FROM users",
//            rs -> {
//                while (rs.next()) {
//                    String username = rs.getString("username");
//                    String password = rs.getString("password");
//                    //boolean isAdmin = rs.getInt("is_admin") == 1;
//
//                    User user = new User(username, password);
//                    userCache.put(username, user);
//                }
//                return null;
//            }
//        );
//    }

    /**
     * Save a user to the right table
     */
    public void saveUser(User user) {
        boolean success = false;

        try {
            if (user.isAdmin()) {
                AdminDAO adminDAO = AdminDAO.getInstance();
                Admin admin = (Admin) user;

                success = dbManager.executeUpdate(
                        "INSERT OR REPLACE INTO " + adminDAO.getSQLTableName() + " (username, password) VALUES (?, ?)",
                        admin.getUsername(),
                        admin.getPassword()
                );
            } else if (user.isMedico()) {
                MedicoDAO medicoDAO = MedicoDAO.getInstance();
                Medico medico = (Medico) user;

                success = dbManager.executeUpdate(
                        "INSERT OR REPLACE INTO " + medicoDAO.getSQLTableName() + " (username, password, email) VALUES (?, ?, ?)",
                        medico.getUsername(),
                        medico.getPassword(),
                        medico.getEmail()
                );
            } else if (user.isPaziente()) {
                PazienteDAO pazienteDAO = PazienteDAO.getInstance();
                Paziente paziente = (Paziente) user;

                success = dbManager.executeUpdate(
                        "INSERT OR REPLACE INTO " + pazienteDAO.getSQLTableName() +
                                " (username, password, email, id_diabetologo, data_nascita, peso, provincia_residenza, comune_residenza, note_paziente) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        paziente.getUsername(),
                        paziente.getPassword(),
                        paziente.getEmail(),
                        pazienteDAO.getMedico(paziente),
                        paziente.getDataNascita(),
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

        // No user cache logic needed
    }

    // todo: togli commento
    /*
    /**
     * Delete a user from the repository
     */
    /*
    public void deleteUser(String username) {
        User user = getUser(username);
        boolean success = false;

        if (user != null && !user.isAdmin()) {
            try {
                if (user instanceof Medico) {
                    MedicoDAO medicoDAO = MedicoDAO.getInstance();
                    success = dbManager.executeUpdate(
                            "DELETE FROM " + medicoDAO.getSQLTableName() + " WHERE username = ?",
                            username
                    );
                } else if (user instanceof Paziente) {
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
    */

    /**
     * Get a user by username
     */
    /*
    public User getUser(String username) {
        // Try to get Admin
        User user = dbManager.executeQuery(
                "SELECT username, password FROM amministratore WHERE username = ?",
                rs -> {
                    if (rs.next()) {
                        return new AdminUser(rs.getString("username"), rs.getString("password"));
                    }
                    return null;
                },
                username
        );

        if (user != null) return user;

        // Try to get Medico
        user = dbManager.executeQuery(
                "SELECT username, password, email FROM diabetologo WHERE username = ?",
                rs -> {
                    if (rs.next()) {
                        return new MedicoUser(
                                rs.getString("username"),
                                rs.getString("password"),
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
                "SELECT username, password, email, id_diabetologo, data_nascita, peso, provincia_residenza, comune_residenza, note_paziente " +
                        "FROM paziente WHERE username = ?",
                rs -> {
                    if (rs.next()) {
                        return new PazienteUser(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getInt("id_diabetologo"),
                                rs.getDate("data_nascita"),
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

        return user;
    }
*/

    // todo: togli commento
    /*
    /**
     * Check if a username exists
     /*
    public boolean usernameExists(String username) {
        //return userCache.containsKey(username);
        //return getUser(username) != null;
    }
    */

    // non utilizzato perché la cache locale non è più utilizzata
//    /**
//     * Get all users
//     */
//    public Map<String, User> getAllUsers() {
//        return new HashMap<>(userCache);
//    }

    //todo: quando UserModel sarà astratto togliere il codice da qui e definire questa funzione solo nei figli
    public String getSQLTableName(){
        return "";
    }
}