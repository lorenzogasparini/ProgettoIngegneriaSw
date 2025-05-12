package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MedicoDAO extends UserDAO {

    private final String SQLTableName = "diabetologo";
    private static MedicoDAO instance;

    private MedicoDAO() {
        // Init DB connection, maybe from a config class
        super();
    }

    public static synchronized MedicoDAO getInstance() {
        if (instance == null) {
            instance = new MedicoDAO();
        }
        return instance;
    }


    @Override
    public String getSQLTableName(){
        return SQLTableName;
    }

    // todo: aggiungere i metodi del medico (non vanno messi nell'interfaccia perchè non esistono interfaccie per i DAO)

    /**
     * @param username The String-value for the username of the intance of MedicoUser
     * @return HashMap that contains all the users observed by the intance of MedicoUser
     */
    public Map<Integer, User> getPazientiFromDB(String username) throws SQLException {
        Map<Integer, User> pazienti = new TreeMap<>();
        int id_diabetologo = getIdFromDB(username);
        if(id_diabetologo == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                "SELECT * FROM paziente p WHERE p.id_diabetologo = ?",
                rs -> {
                    while (rs.next()) { // todo: capire... perché è stata fatta una mappa e non una lista di PazientiUser? (perché l'id è tenuto separato)?
                        pazienti.put(rs.getInt("id"),new PazienteUser(
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
                                rs.getString("note_paziente")));
                    }
                    return null;
                },
                id_diabetologo
        );

        return pazienti;
    }

    // todo: capire... non facciamo una classe per rilevazioni e quindi ritorniamo un array di Rilevazione?
    public String[] getRilevazioniFarmaci(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<String> rilevazioni = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nRilevazione farmaco : " + id_paziente);

        super.getConnection().executeQuery(
                "SELECT r.id, r.id_paziente, r.timestamp, r.quantita, r.note, f.id, f.codice_aic, f.nome " +
                        "FROM rilevazione_farmaco r " +
                        "INNER JOIN farmaco f ON r.id_farmaco = f.id " +
                        "WHERE r.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        rilevazioni.add(rs.getInt("id") + ", " + rs.getString("id_paziente") + ", " + rs.getTimestamp("timestamp") + ", " + rs.getString("quantita") + ", " + rs.getString("note") + ", " + rs.getString("note") + ", " + rs.getInt("id") + ", " + rs.getString("codice_aic"));
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioni.toArray(new String[rilevazioni.size()]);
    }

    // todo: niente classe Rilevazione?
    public String[] getRilevazioniGlicemia(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<String> rilevazioni = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nRilevazione glicemia id_paziente: " + id_paziente);

        super.getConnection().executeQuery(
                "SELECT * "
                        + "FROM rilevazione_glicemia r "
                        + "WHERE r.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        rilevazioni.add(rs.getInt("id") + ", " + rs.getInt("id_paziente") + ", " + rs.getTimestamp("timestamp") + ", " + rs.getInt("valore") + ", " + rs.getBoolean("prima_pasto"));
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioni.toArray(new String[rilevazioni.size()]);
    }

    public String[] getRilevazioniSintomi(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<String> rilevazioni = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nRilevazione sintomi id_paziente: " + id_paziente);

        super.getConnection().executeQuery(
                    "SELECT * "
                        + " FROM rilevazione_sintomo r"
                        + " WHERE r.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        rilevazioni.add(rs.getInt("id") + ", " + rs.getInt("id_paziente") + ", " + rs.getTimestamp("timestamp") + ", " + rs.getString("sintomo") + ", " + rs.getInt("intensita"));
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioni.toArray(new String[rilevazioni.size()]);
    }

    // todo: non facciamo una classe per Patologia?
    public String[] getPatologiePaziente(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<String> rilevazioni = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nPatologie id_paziente: " + id_paziente);

        super.getConnection().executeQuery(
                "SELECT *"
                + " FROM patologia p"
                + " INNER JOIN patologia_paziente pp ON p.id = pp.id_patologia"
                + " WHERE pp.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        rilevazioni.add(rs.getInt("id") + ", " + rs.getString("nome") + ", " + rs.getString("codice_icd"));
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioni.toArray(new String[rilevazioni.size()]);
    }

    // todo: capire... non facciamo una classe Terapia?
    public String[] getTerapiePaziente(String username) throws SQLException {   //  Verificare il risultato fornito
        int id_paziente = getIdFromDB(username);
        ArrayList<String> rilevazioni = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nPatologie id_paziente: " + id_paziente);

        super.getConnection().executeQuery(
                "SELECT t.id, t.id_farmaco, t.dosi_giornaliere, t.quantita_per_dose, t.note, f.id, f.codice_aic, f.nome"
                + " FROM terapia t, farmaco f"
                + " INNER JOIN patologia_paziente pp ON t.id = pp.id_terapia"
                + " WHERE pp.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        rilevazioni.add(rs.getInt("id") + ", " + rs.getInt("id_farmaco") + ", " + rs.getInt("dosi_giornaliere") + ", " + rs.getFloat("quantita_per_dose") + ", " + rs.getString("note") + ", " + rs.getInt("id") + ", " + rs.getString("codice_aic") + ", " + rs.getString("nome"));
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioni.toArray(new String[rilevazioni.size()]);
    }
}
