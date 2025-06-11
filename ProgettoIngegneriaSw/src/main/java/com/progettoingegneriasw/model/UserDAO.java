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
import com.progettoingegneriasw.model.Utils.Farmaco;
import com.progettoingegneriasw.model.Utils.Log;
import com.progettoingegneriasw.view.ViewNavigator;

import java.sql.Date;
import java.util.ArrayList;

public class UserDAO { // todo: è corretto rendere questa classe abstract???
    private final DatabaseManager dbManager;
    private static UserDAO instance;
    public static User loggedUser; // this contains the current logged user; //todo: capire dove metterlo

    protected UserDAO() {
        this.dbManager = new DatabaseManager();
        //refreshUserCache();
    }

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    // todo: capire quando chiamare questo metodo (probabilmente dopo che l'utente si logga) e metterlo private finiti i test
    //  forse non è necessario
    public void refreshLoggedUser(User currUser){
        loggedUser = currUser;
    }

    public static boolean userIsLogged(){
        return loggedUser != null;
    }

    public DatabaseManager getConnection(){
        return dbManager;
    }

    /**
     * Save a user to the right table
     */
    public void saveUser(User user) { // Funzionante!
        boolean success = false;

        if(userExists(user.getUsername())){
            updateUser(user);
            return;
        }

        try {
            if (user.isAdmin()) {
                AdminDAO adminDAO = AdminDAO.getInstance();
                Admin admin = (Admin) user;

                success = dbManager.executeUpdate(
                        "INSERT INTO " + adminDAO.getSQLTableName() + " (username, password, nome, cognome) VALUES (?, ?, ?, ?)",
                        admin.getUsername(),
                        admin.getPassword(),
                        admin.getNome(),
                        admin.getCognome()
                );
            } else if (user.isMedico()) {
                MedicoDAO medicoDAO = MedicoDAO.getInstance();
                Medico medico = (Medico) user;

                success = dbManager.executeUpdate(
                        "INSERT INTO " + medicoDAO.getSQLTableName() + " (username, password, nome, cognome, email) VALUES (?, ?, ?, ?, ?)",
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
                        "INSERT INTO " + pazienteDAO.getSQLTableName() +
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

    private void updateUser(User user){
        // todo: implementa l'UPDATE se l'utente è già esistente
        boolean success = false;

        /*UPDATE paziente SET note_paziente = 'dieta poco sana', peso = 74.5 WHERE username = 'giulia.bianchi'*/

        try {
            if (user.isAdmin()) {
                AdminDAO adminDAO = AdminDAO.getInstance();
                Admin admin = (Admin) user;

                success = dbManager.executeUpdate(
                        "UPDATE " + adminDAO.getSQLTableName() + " SET username = ?, password = ?, nome = ?," +
                                " cognome = ?" +
                                " WHERE username = ?",
                        admin.getUsername(),
                        admin.getPassword(),
                        admin.getNome(),
                        admin.getCognome(),
                        admin.getUsername()
                );

            } else if (user.isMedico()) {
                MedicoDAO medicoDAO = MedicoDAO.getInstance();
                Medico medico = (Medico) user;

                success = dbManager.executeUpdate(
                        "UPDATE " + medicoDAO.getSQLTableName() + " SET username = ?, password = ?, nome = ?, " +
                                "cognome = ?, email = ?" +
                                "WHERE username = ?",
                        medico.getUsername(),
                        medico.getPassword(),
                        medico.getNome(),
                        medico.getCognome(),
                        medico.getEmail(),
                        medico.getUsername()
                );

            } else if (user.isPaziente()) {
                PazienteDAO pazienteDAO = PazienteDAO.getInstance();
                Paziente paziente = (Paziente) user;

                success = dbManager.executeUpdate(
                        "UPDATE " + pazienteDAO.getSQLTableName() + " SET username = ?, password = ?, nome = ?," +
                                " cognome = ?, email = ?, id_diabetologo = ?, data_nascita = ?, peso = ?, " +
                                "provincia_residenza = ?, comune_residenza = ?, note_paziente = ?" +
                                " WHERE username = ?",
                        paziente.getUsername(),
                        paziente.getPassword(),
                        paziente.getNome(),
                        paziente.getCognome(),
                        paziente.getEmail(),
                        paziente.getIdMedico(),
                        paziente.getDataNascita().toString(),
                        paziente.getPeso(),
                        paziente.getProvinciaResidenza(),
                        paziente.getComuneResidenza(),
                        paziente.getNotePaziente(),
                        paziente.getUsername()
                );

            }
        } catch (Exception e) {
            success = false;
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }




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

        if (!userIsLogged())
            return new UserDAO();

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

    // todo: chiamare questa funzione in ogni altra funzione e probabilmente creare un metodo intermediario
    // todo: per generare l'oggetto Log a seconda del contesto che poi chiami questa funzione
    public void setLog(Log log){
        dbManager.executeUpdate(
                "INSERT INTO log (id_paziente, id_diabetologo, azione, timestamp) VALUES (?, ?, ?, ?)",
                log.getIdPaziente(),
                log.getIdDiabetologo(),
                log.getAzione(),
                log.getTimestamp().toString()
        );
    }

    /**
     *
     * @return Tutti gli utenti del sistema: medici e pazienti
     */
    public User[] getAllUsers(){
        ArrayList<User> users = new ArrayList<>();

        if(getUser(ViewNavigator.getAuthenticatedUser()).isMedico()){
            dbManager.executeQuery(
                    "SELECT * FROM paziente",
                    rs -> {
                        while (rs.next()) {
                            users.add(new PazienteUser(
                                            rs.getInt("id"),
                                            rs.getString("username"),
                                            rs.getString("password"),
                                            rs.getString("nome"),
                                            rs.getString("cognome"),
                                            rs.getString("email"),
                                            rs.getInt("id_diabetologo"),
                                            Date.valueOf(rs.getString("data_nascita")),
                                            rs.getDouble("peso"),
                                            rs.getString("provincia_residenza"),
                                            rs.getString("comune_residenza"),
                                            rs.getString("note_paziente")
                                    )
                            );
                        }
                        return null;
                    }
            );
        }
        dbManager.executeQuery(
                "SELECT * FROM diabetologo",
                rs -> {
                    while (rs.next()) {
                        users.add(new MedicoUser(
                                        rs.getInt("id"),
                                        rs.getString("username"),
                                        rs.getString("password"),
                                        rs.getString("nome"),
                                        rs.getString("cognome"),
                                        rs.getString("email")
                                )
                        );
                    }
                    return null;
                }
        );
        return users.toArray(new User[users.size()]);
    }

    public Farmaco getFaracoFromId(Integer idFarmaco){
        return getConnection().executeQuery(
                "SELECT id FROM farmaco WHERE id = ?",
                rs -> {
                    if (rs.next()) {
                        return new Farmaco(
                                rs.getInt("id"),
                                rs.getString("codice_aic"),
                                rs.getString("nome")
                        );
                    }
                    return null;
                },
                idFarmaco
        );
    }

    private static String uriEncode(String s) {
        return s.replace(" ", "%20").replace("\n", "%0A").replace("\r", "%0D");
    }

    /**
     * Funzione per gestire l'apertura dell'app di mailing di default del dispositivo, utilizzata nelle riespettive
     * view di contatto utente di paziente e medico. per impostarla su Linux:
     * Impostazioni > Applicazioni > App. predefinite > Email e selezionare dal menu a tendina (consigliata Tunderbird)
     */
    public void contattaDiabetologo(String destinatario, String oggetto, String corpo) {
        String mailto = String.format(
                "mailto:%s?subject=%s&body=%s",
                uriEncode(destinatario),
                uriEncode(oggetto),
                uriEncode(corpo)
        );

        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + mailto);
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + mailto);
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("xdg-open " + mailto);
            } else {
                throw new UnsupportedOperationException("Sistema operativo non supportato.");
            }
        } catch (Exception e) {
            System.err.println("Errore apertura email: " + e.getMessage());
        }
    }

    // todo: method to get alerts
    //public ArrayList<Alert> getAllAlerts() // todo: da ridefinire in PazienteDAO perché deve prendere solo gli alert con
}