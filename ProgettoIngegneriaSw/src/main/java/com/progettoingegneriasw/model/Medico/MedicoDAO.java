package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;

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






    // Metodi del medico
    public Paziente[] getAllPazienti(){
        ArrayList<Paziente> pazienti = new ArrayList<>();
        super.getConnection().executeQuery(
                "SELECT * FROM paziente",
                rs -> {
                    while (rs.next()) {
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
                }
        );
        return pazienti.toArray(new Paziente[pazienti.size()]);
    }

    /**
     * @param username The String-value for the username of the intance of MedicoUser
     * @return HashMap that contains all the users observed by the intance of MedicoUser
     */
    public Paziente[] getPazientiFromDB(String username) throws SQLException { // todo: capire se è giusto far ritornare un Paziente
        ArrayList<Paziente> pazienti = new ArrayList<>();
        int id_diabetologo = getIdFromDB(username);
        if(id_diabetologo == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                "SELECT * FROM paziente p WHERE p.id_diabetologo = ?",
                rs -> {
                    while (rs.next()) {
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

        super.getConnection().executeQuery(
                "SELECT p.id AS p_id, p.nome AS p_nome, p.codice_icd AS p_codice_icd"
                + " FROM patologia p"
                + " INNER JOIN patologia_paziente pp ON p.id = pp.id_patologia"
                + " WHERE pp.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        patologie.add(
                                new Patologia(
                                        rs.getInt("p_id"),
                                        rs.getString("p_nome"),
                                        rs.getString("p_codice_icd")
                                )
                        );
                    }
                    return null;
                },
                id_paziente
        );
        return patologie.toArray(new Patologia[patologie.size()]);
    }


    public Terapia[] getTerapiePaziente(String username) throws SQLException {   //  Verificare il risultato fornito
        int id_paziente = getIdFromDB(username);
        ArrayList<Terapia> terapie = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                "SELECT t.id, t.id_farmaco, t.dosi_giornaliere, t.quantita_per_dose, t.note, f.id, f.codice_aic, f.nome"
                + " FROM terapia t, farmaco f"
                + " INNER JOIN patologia_paziente pp ON t.id = pp.id_terapia"
                + " WHERE pp.id_paziente = ?",
                rs -> {
                    while (rs.next()) {
                        terapie.add(new Terapia(rs.getInt("id"),
                                rs.getInt("dosi_giornaliere"),
                                rs.getFloat("quantita_per_dose"),
                                rs.getString("note"),
                                new Farmaco(rs.getInt("id_farmaco"),
                                        rs.getString("codice_aic"),
                                        rs.getString("nome")
                                )
                        ));
                    }
                    return null;
                },
                id_paziente
        );
        return terapie.toArray(new Terapia[terapie.size()]);
    }



    // todo: getRilevazioneGlicemia() parametrica come getRilevazioneFarmaco()

    // todo: getRilevazioneSintomo() parametrica come getRilevazioneFarmaco()



    public RilevazioneFarmaco[] getRilevazioneFarmaco() throws SQLException {
        return getRilevazioneFarmaco("");
    }

    /* query esempio
        SELECT rf.*, f.*
        FROM paziente p
        INNER JOIN rilevazione_farmaco rf ON p.id = rf.id_paziente
        INNER JOIN farmaco f ON rf.id_farmaco = f.id
        WHERE (:username IS NULL OR username = :username);
     */
    public RilevazioneFarmaco[] getRilevazioneFarmaco(String usernamePaziente) throws SQLException {
        ArrayList<RilevazioneFarmaco> rilevazioniFarmaci = new ArrayList<>();

        String query =
                "SELECT rf.* " +
                        "FROM paziente p " +
                        "INNER JOIN rilevazione_farmaco rf ON p.id = rf.id_paziente " +
                        "WHERE (? IS NULL OR p.username = ?)";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        rilevazioniFarmaci.add(new RilevazioneFarmaco(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getInt("id_farmaco"),
                                rs.getTimestamp("timestamp"),
                                rs.getFloat("quantita"),
                                rs.getString("note")
                        ));
                    }
                    return null;
                },
                usernamePaziente.isEmpty() ? null : usernamePaziente,  // 1st placeholder
                usernamePaziente.isEmpty() ? null : usernamePaziente   // 2nd placeholder
        );

        return rilevazioniFarmaci.toArray(new RilevazioneFarmaco[0]);
    }



}
