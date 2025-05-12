package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.RilevazioneGlicemia;
import com.progettoingegneriasw.model.Utils.Patologia;
import com.progettoingegneriasw.model.Utils.RilevazioneFarmaco;
import com.progettoingegneriasw.model.Utils.RilevazioneSintomo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public Paziente[] getPazientiFromDB(String username) throws SQLException { // todo: capire se è giusto far ritornare un Paziente
        ArrayList<Paziente> pazienti = new ArrayList<>(); // nota: metto l'interfaccia come argomento e non PazienteUser
        int id_diabetologo = getIdFromDB(username);
        if(id_diabetologo == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                "SELECT * FROM paziente p WHERE p.id_diabetologo = ?",
                rs -> {
                    while (rs.next()) { // todo: capire... perché è stata fatta una mappa e non una lista di PazientiUser? (perché l'id è tenuto separato)?
                        pazienti.add(new PazienteUser(
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
                },
                id_diabetologo
        );

        return pazienti.toArray(new Paziente[pazienti.size()]);
    }


    public RilevazioneFarmaco[] getRilevazioniFarmaci(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<RilevazioneFarmaco> rilevazioniFarmaci = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nRilevazione farmaco : " + id_paziente);

        super.getConnection().executeQuery(
                "SELECT r.id AS r_id, r.id_paziente AS r_id_paziente, r.id_farmaco AS r_id_farmaco, " +
                        "r.timestamp AS r_timestamp, r.quantita AS r_quantita, r.note AS r_note, f.id AS f_id, " +
                        "f.codice_aic AS f_codice_aic, f.nome AS f_nome " +
                        "FROM rilevazione_farmaco r " +
                        "INNER JOIN farmaco f ON r.id_farmaco = f.id " +
                        "WHERE r.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        rilevazioniFarmaci.add(
                          new RilevazioneFarmaco(
                                    rs.getInt("r_id"),
                                    rs.getInt("r_id_paziente"),
                                    rs.getInt("r_id_farmaco"),
                                    rs.getTimestamp("r_timestamp"),
                                    rs.getDouble("r_quantita"),
                                    rs.getString("r_note")
                                  )
                          );
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioniFarmaci.toArray(new RilevazioneFarmaco[rilevazioniFarmaci.size()]);
    }


    public RilevazioneGlicemia[] getRilevazioniGlicemia(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<RilevazioneGlicemia> rilevazioniGlicemia = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nRilevazione glicemia id_paziente: " + id_paziente);

        super.getConnection().executeQuery(
                "SELECT *"
                        + "FROM rilevazione_glicemia r "
                        + "WHERE r.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        rilevazioniGlicemia.add(
                                new RilevazioneGlicemia(
                                        rs.getInt("id"),
                                        rs.getInt("id_paziente"),
                                        rs.getTimestamp("timestamp"),
                                        rs.getInt("valore"),
                                        rs.getBoolean("prima_pasto")
                                )
                        );
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioniGlicemia.toArray(new RilevazioneGlicemia[rilevazioniGlicemia.size()]);
    }

    public RilevazioneSintomo[] getRilevazioniSintomi(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<RilevazioneSintomo> rilevazioniSintomo = new ArrayList<>();

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
                        rilevazioniSintomo.add(
                                    new RilevazioneSintomo(
                                            rs.getInt("id"),
                                            rs.getInt("id_paziente"),
                                            rs.getTimestamp("timestamp"),
                                            rs.getString("sintomo"),
                                            rs.getInt("intensita")
                                    )
                                );
                    }
                    return null;
                },
                id_paziente
        );
        return rilevazioniSintomo.toArray(new RilevazioneSintomo[rilevazioniSintomo.size()]);
    }


    public Patologia[] getPatologiePaziente(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<Patologia> patologie = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        System.out.println("\n\nPatologie id_paziente: " + id_paziente);

        super.getConnection().executeQuery(
                "SELECT p.id AS p_id, p.nome AS p_nome, p.codice_aic AS p_codice_aic"
                + " FROM patologia p"
                + " INNER JOIN patologia_paziente pp ON p.id = pp.id_patologia"
                + " WHERE pp.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        patologie.add(
                                new Patologia(
                                        rs.getInt("id"),
                                        rs.getString("nome"),
                                        rs.getString("codice_icd")
                                )
                        );
                    }
                    return null;
                },
                id_paziente
        );
        return patologie.toArray(new Patologia[patologie.size()]);
    }

    // todo: implementare con Terapia
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
