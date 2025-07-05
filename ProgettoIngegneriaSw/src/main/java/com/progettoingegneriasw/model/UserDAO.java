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
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
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

        if(userExists(user.getUsername())){
            updateUser(user);
            return;
        }

        try {
            if (user.isAdmin()) {
                AdminDAO adminDAO = AdminDAO.getInstance();
                Admin admin = (Admin) user;

                dbManager.executeUpdate(
                        "INSERT INTO " + adminDAO.getSQLTableName() + " (username, password, nome, cognome) VALUES (?, ?, ?, ?)",
                        admin.getUsername(),
                        admin.getPassword(),
                        admin.getNome(),
                        admin.getCognome()
                );
            } else if (user.isMedico()) {
                MedicoDAO medicoDAO = MedicoDAO.getInstance();
                Medico medico = (Medico) user;

                Integer medicoId = dbManager.executeInsertAndReturnId(
                        "INSERT INTO " + medicoDAO.getSQLTableName() + " (username, password, nome, cognome, email," +
                                "profile_image_name) VALUES (?, ?, ?, ?, ?, ?)",
                        medico.getUsername(),
                        medico.getPassword(),
                        medico.getEmail(),
                        medico.getNome(),
                        medico.getCognome(),
                        medico.getProfileImageName()
                );
                setLog(new Log(null, medicoId, LogAction.SaveUser, null));
            } else if (user.isPaziente()) {
                PazienteDAO pazienteDAO = PazienteDAO.getInstance();
                Paziente paziente = (Paziente) user;

                Integer pazienteId = dbManager.executeInsertAndReturnId(
                        "INSERT INTO " + pazienteDAO.getSQLTableName() +
                                " (username, password, nome, cognome, email, id_diabetologo, data_nascita, peso," +
                                " provincia_residenza, comune_residenza, note_paziente, profile_image_name) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
                        paziente.getNotePaziente(),
                        paziente.getProfileImageName()
                );
                setLog(new Log(pazienteId, null, LogAction.SaveUser, null));
            }


        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }


    }

    private void updateUser(User user){
        /*UPDATE paziente SET note_paziente = 'dieta poco sana', peso = 74.5 WHERE username = 'giulia.bianchi'*/

        try {
            if (user.isAdmin()) {
                AdminDAO adminDAO = AdminDAO.getInstance();
                Admin admin = (Admin) user;

                dbManager.executeUpdate(
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

                dbManager.executeUpdate(
                        "UPDATE " + medicoDAO.getSQLTableName() + " SET username = ?, password = ?, nome = ?, " +
                                "cognome = ?, email = ?, profile_image_name = ?" +
                                "WHERE username = ?",
                        medico.getUsername(),
                        medico.getPassword(),
                        medico.getNome(),
                        medico.getCognome(),
                        medico.getEmail(),
                        medico.getProfileImageName(),
                        medico.getUsername()
                );
                setLog(new Log(null, getIdFromDB(medico.getUsername()), LogAction.UpdateUser, null));

            } else if (user.isPaziente()) {
                PazienteDAO pazienteDAO = PazienteDAO.getInstance();
                Paziente paziente = (Paziente) user;

                dbManager.executeUpdate(
                        "UPDATE " + pazienteDAO.getSQLTableName() + " SET username = ?, password = ?, nome = ?," +
                                " cognome = ?, email = ?, id_diabetologo = ?, data_nascita = ?, peso = ?, " +
                                "provincia_residenza = ?, comune_residenza = ?, note_paziente = ?, profile_image_name = ?" +
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
                        paziente.getProfileImageName(),
                        paziente.getUsername()
                );
                setLog(new Log(getIdFromDB(paziente.getUsername()), null, LogAction.UpdateUser, null));

            }
        } catch (Exception e) {
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
                "SELECT * FROM diabetologo WHERE username = ?",
                rs -> {
                    if (rs.next()) {
                        return new MedicoUser(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email"),
                                rs.getString("profile_image_name")
                        );
                    }
                    return null;
                },
                username
        );

        if (user != null) return user;

        // Try to get Paziente
        user = dbManager.executeQuery(
                "SELECT * FROM paziente WHERE username = ?",
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
                                rs.getString("note_paziente"),
                                rs.getString("profile_image_name")
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

    public boolean isUserDeleted(String username) {
        String tableName = getUser(username).getSQLTableName();
        String query = "SELECT 1 FROM " + tableName + " WHERE username = ? AND deleted = true";

        Boolean result = getConnection().executeQuery(
                query,
                rs -> rs.next(),
                username
        );

        return result != null && result;
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

        String query = "SELECT id FROM " + getUser(username).getSQLTableName() + " WHERE username = ?";
        return getConnection().executeQuery(
                query,
                rs -> {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                    return -1;
                },
                username
        );
    }

    /// NB: la tabella di log è sporcata dai test del main
    public void setLog(Log log){
        dbManager.executeUpdate(
                "INSERT INTO log (id_paziente, id_diabetologo, azione) VALUES (?, ?, ?)", // non viene passato appositamente timestamp
                log.getIdPaziente(),
                log.getIdDiabetologo(),
                log.getAzione()
        );
    }


    /**
     *
     * @return Tutti gli utenti del sistema: medici e pazienti
     */
    public User[] getAllUsers(){
        ArrayList<User> users = new ArrayList<>();

        if(getUser(ViewNavigator.getAuthenticatedUsername()).isMedico()){
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
                                            rs.getString("note_paziente"),
                                            rs.getString("profile_image_name")
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
                                        rs.getString("email"),
                                        rs.getString("profile_image_name")
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
                "SELECT * FROM farmaco WHERE id = ?",
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

    public Farmaco getFarmacoFromAic(String codiceAic){
        return getConnection().executeQuery(
                "SELECT f.id, f.codice_aic, f.nome " +
                    "FROM farmaco f " +
                    "WHERE f.codice_aic = ?",
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
                codiceAic
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

    // todo: questo metodo andrebbe reso astratto e ridefinito solo in PazienteDAO e MedicoDAO
    public int countAlerts() throws SQLException { // todo: da ridefinire in PazienteDAO perché deve prendere solo gli alert con
        return 0;
    }

    // todo: verificare se ci sono pazienti che non assumono i farmaci che dovrebbero da più di 3 giorni
    /// in un sistema CLIENT-SERVER questo metodo verrebbe chiamato ripetutamente dal server (qui per maggiore efficienza
    /// lo facciamo chiamare da un solo client tramite un file .lck
    public void automaticUpdateAlertTable() throws SQLException {

        Alert[] alerts = getAlertFarmaciNonAssuntiDaAlmeno3GiorniENonSegnalati();

        /*
        System.out.println("--- Alerts to insert ---");
        for(Alert a: alerts){
            System.out.println("Alert: " + a);
        }
        System.out.println("\n");
         */

        insertAlerts(alerts);

    }

    public Alert[] getAlertFarmaciNonAssuntiDaAlmeno3GiorniENonSegnalati(){

        ArrayList<Alert> alerts = new ArrayList<>();

        getConnection().executeQuery(
                "SELECT " +
                        "   p.id AS id_paziente, " +
                        "   rf_last.id AS id_rilevazione_farmaco, " +
                        "   f.id AS id_farmaco, " +
                        "   f.codice_aic AS codice_aic, " +
                        "   f.nome AS nome_farmaco " +
                        " FROM paziente p " +
                        " INNER JOIN patologia_paziente pp ON p.id = pp.id_paziente " +
                        " INNER JOIN terapia t ON pp.id_terapia = t.id " +
                        " INNER JOIN farmaco f ON t.id_farmaco = f.id " +
                        " INNER JOIN ( " +
                        "   SELECT rf1.* " +
                        "   FROM rilevazione_farmaco rf1 " +
                        "   WHERE rf1.timestamp = ( " +
                        "       SELECT MAX(rf2.timestamp) " +
                        "       FROM rilevazione_farmaco rf2 " +
                        "       WHERE rf2.id_paziente = rf1.id_paziente " +
                        "           AND rf2.id_farmaco = rf1.id_farmaco " +
                        "   ) " +
                        " ) rf_last ON rf_last.id_paziente = p.id AND rf_last.id_farmaco = f.id " +


                        " WHERE NOT EXISTS ( " +
                        "   SELECT 1 " +
                        "   FROM rilevazione_farmaco rf " +
                        "   WHERE rf.id_paziente = p.id " +
                        "       AND rf.id_farmaco = f.id " +
                        "       AND DATE(rf.timestamp) >= DATE('now', '-2 days') " +
                        " ) " +

                        " AND NOT EXISTS ( " +
                        "   SELECT 1 " +
                        "   FROM alert a " +
                        "   WHERE a.id_paziente = p.id " +
                        "       AND a.tipo_alert = 'farmacoNonAssuntoDa3Giorni' " +
                        "       AND a.id_rilevazione = rf_last.id " +
                        " )",
                rs -> {
                    while (rs.next()) {
                        alerts.add(
                                new Alert(
                                        rs.getInt("id_paziente"),
                                        rs.getInt("id_rilevazione_farmaco"),
                                        AlertType.farmacoNonAssuntoDa3Giorni,
                                        Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                                        false
                                )
                        );
                    }
                    return null;
                }

        );

        return alerts.toArray(new Alert[alerts.size()]);
    }

    public void insertAlerts(Alert[] alerts){
        for(Alert alert: alerts){
            dbManager.executeUpdate(
                    "INSERT INTO alert (id_paziente, id_rilevazione, tipo_alert, data_alert, letto) VALUES (?, ?, ?, ?, ?)",
                    alert.getIdPaziente(),
                    alert.getIdRilevazione(),
                    alert.getTipoAlert(),
                    Timestamp.valueOf(alert.getTimestamp().toLocalDateTime().withNano(0)).toString(),
                    alert.getLetto()
            );
            setLog(new Log(alert.getIdPaziente(), null, LogAction.InsertAlert, null));
        }
    }

    public void setAlertRead(Alert alert, boolean read){
            dbManager.executeUpdate(
                    "UPDATE alert SET letto = ?" +
                            " WHERE id = ?",
                    read,
                    alert.getId()
            );
    }



}