package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class MedicoDAO extends UserDAO {

    private final String SQLTableName = "diabetologo";
    private static MedicoDAO instance = null;

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
                                        rs.getString("note_paziente"),
                                        rs.getString("profile_image_name")
                                )
                        );
                    }
                    return null;
                }
        );
        return pazienti.toArray(new Paziente[pazienti.size()]);
    }


    /**
     * @param medicoUsername The String-value for the medicoUsername of the intance of MedicoUser
     * @return HashMap that contains all the users observed by the intance of MedicoUser
     */
    public Paziente[] getPazientiAssegnati(String medicoUsername) throws SQLException { // todo: capire se è giusto far ritornare un Paziente
        ArrayList<Paziente> pazienti = new ArrayList<>();
        int id_diabetologo = getIdFromDB(medicoUsername);
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
                                    rs.getString("note_paziente"),
                                    rs.getString("profile_image_name")
                                )
                        );
                    }
                    return null;
                },
                id_diabetologo
        );

        return pazienti.toArray(new Paziente[pazienti.size()]);
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

    // todo: non è chiamata da nessuno, è corretto? In teoria viene fatto tutto dalla terapia?
    public void setPatologiaPaziente(Patologia patologia, String username, Terapia terapia, Date dataDiagnosi, String notePatologia){
        int id_paziente = getIdFromDB(username);
        super.getConnection().executeUpdate(
                "INSERT INTO patologia_paziente (id_paziente, id_patologia, id_terapia, data_diagnosi, note_patologia) " +
                        "VALUES (?, ?, ?, ?, ?)",
                id_paziente,
                patologia.getId(),
                (terapia != null) ? terapia.getId() : null,
                dataDiagnosi.toString(),
                notePatologia
        );
        setLog(new Log(id_paziente, null, LogAction.SetPatologiaPaziente, null));
    }



    public Patologia getPatologiaFromId(int idPatologia) throws SQLException {
        AtomicReference<Patologia> patologiaRef = new AtomicReference<>();

        String query =  "SELECT p.* " +
                        "FROM patologia p " +
                        "WHERE p.id = ?";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        patologiaRef.set(new Patologia(
                                rs.getInt("id"),
                                rs.getString("nome"),
                                rs.getString("codice_icd")
                        ));
                    }
                    return null;
                },
                idPatologia
        );
        return patologiaRef.get();
    }

    /// getPatologie() ritorna tutte le patologie
    public Patologia[] getPatologie() throws SQLException {
        return getPatologie("");
    }

    // ritorna la patologia con uno specifico nome
    public Patologia[] getPatologie(String nomePatologia) throws SQLException {
        ArrayList<Patologia> patologie = new ArrayList<>();

        String query =
                "SELECT p.* " +
                        "FROM patologia p " +
                        "WHERE (? IS NULL OR p.nome LIKE ?)";

        String likePattern = nomePatologia == null || nomePatologia.isEmpty() ? null : "%" + nomePatologia + "%";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        patologie.add(new Patologia(
                                rs.getInt("id"),
                                rs.getString("nome"),
                                rs.getString("codice_icd")
                        ));
                    }
                    return null;
                },
                likePattern,  // for the IS NULL check
                likePattern   // for the LIKE operator
        );

        return patologie.toArray(new Patologia[0]);
    }


    /// aggiunge le patologie non ancora presenti nella lista
    public void addPatologia(Patologia patologia){
        super.getConnection().executeUpdate(
                "INSERT INTO patologia (nome, codice_icd) " +
                        "VALUES (?, ?)",
                patologia.getNome(),
                patologia.getCodiceIcd()
        );
        setLog(new Log(null, null, LogAction.AddPatologia, null));
    }

    public Terapia getTerapiaFromId(Integer idTerapia){
        AtomicReference<Terapia> terapiaRef = new AtomicReference<>();

        super.getConnection().executeQuery(
                " SELECT t.id, t.id_farmaco, t.dosi_giornaliere, t.quantita_per_dose, t.note, f.id, f.codice_aic, f.nome," +
                        " d.id AS id_diabetologo, d.nome AS nome_diabetologo, d.cognome AS cognome_diabetologo, " +
                        " d.username AS username_diabetologo, d.password AS password_diabetologo, " +
                        " d.email AS email_diabetologo, d.profile_image_name AS profile_image_name_diabetologo " +
                        " FROM terapia t, farmaco f " +
                        " INNER JOIN patologia_paziente pp ON t.id = pp.id_terapia " +
                        " INNER JOIN diabetologo d ON d.id = t.id_diabetologo " +
                        " WHERE t.id = ? ",
                rs -> {
                    while (rs.next()) {
                        terapiaRef.set(new Terapia(
                                rs.getInt("id"),
                                new MedicoUser(
                                        rs.getInt("id_diabetologo"),
                                        rs.getString("nome_diabetologo"),
                                        rs.getString("cognome_diabetologo"),
                                        rs.getString("username_diabetologo"),
                                        rs.getString("password_diabetologo"),
                                        rs.getString("email_diabetologo"),
                                        rs.getString("profile_image_name_diabetologo")
                                ),
                                new Farmaco(
                                        rs.getInt("id_farmaco"),
                                        rs.getString("codice_aic"),
                                        rs.getString("nome")
                                ),
                                rs.getInt("dosi_giornaliere"),
                                rs.getDouble("quantita_per_dose"),
                                rs.getString("note")
                        ));

                    }
                    return null;
                },
                idTerapia
        );

        return terapiaRef.get();
    }


    public Terapia[] getTerapiePaziente(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        ArrayList<Terapia> terapie = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();
        }

        super.getConnection().executeQuery(
                " SELECT t.id, t.id_farmaco, t.dosi_giornaliere, t.quantita_per_dose, t.note, f.id, f.codice_aic, f.nome," +
                        "d.id AS id_diabetologo, d.nome AS nome_diabetologo, d.cognome AS cognome_diabetologo, " +
                        "d.username AS username_diabetologo, d.password AS password_diabetologo, " +
                        "d.email AS email_diabetologo, d.profile_image_name AS profile_image_name_diabetologo " +
                    " FROM terapia t, farmaco f " +
                    " INNER JOIN patologia_paziente pp ON t.id = pp.id_terapia " +
                    " INNER JOIN diabetologo d ON d.id = t.id_diabetologo " +
                    " WHERE pp.id_paziente = ? AND t.id_farmaco = f.id",
                rs -> {
                    while (rs.next()) {
                        terapie.add(new Terapia(
                                rs.getInt("id"),
                                new MedicoUser(
                                        rs.getInt("id_diabetologo"),
                                        rs.getString("nome_diabetologo"),
                                        rs.getString("cognome_diabetologo"),
                                        rs.getString("username_diabetologo"),
                                        rs.getString("password_diabetologo"),
                                        rs.getString("email_diabetologo"),
                                        rs.getString("profile_image_name_diabetologo")
                                ),
                                new Farmaco(
                                        rs.getInt("id_farmaco"),
                                        rs.getString("codice_aic"),
                                        rs.getString("nome")
                                ),
                                rs.getInt("dosi_giornaliere"),
                                rs.getDouble("quantita_per_dose"),
                                rs.getString("note")
                        ));
                    }
                    return null;
                },
                id_paziente
        );
        return terapie.toArray(new Terapia[terapie.size()]);
    }

    /// consente sia di inserire una terapia sia di modificarla nel caso sia già esistente.
    /// Se la patologia non è stata ancora stata assegnata al paziente gli viene assegnata (creazione riga in
    /// patologia_paziente + creazione terapia)
    public void setTerapiaPaziente(Terapia terapia, String usernamePaziente, Patologia patologia, String notePatologiaPaziente) {
        int id_paziente = getIdFromDB(usernamePaziente);
        final Integer[] tmp = new Integer[2]; // to be assigned into a lambda it must be a final int[]
        int terapiaId, patologiaPazienteId;

        // Check if there's already a terapia in patologia_paziente record
        super.getConnection().executeQuery(
                "SELECT pp.id, pp.id_terapia " +
                        " FROM patologia_paziente pp " +
                        " WHERE pp.id_paziente = ? AND pp.id_patologia = ?",
                rs -> {
                    while (rs.next()) {
                        tmp[0] = rs.getInt("id");
                        tmp[1] = rs.getInt("id_terapia");
                    }
                    return null;
                },
                id_paziente,
                patologia.getId()
        );

        if(tmp[0] != null)
            patologiaPazienteId = tmp[0];
        else{
            // assegna la patologia al paziente
            patologiaPazienteId = super.getConnection().executeInsertAndReturnId(
                    "INSERT INTO patologia_paziente (id_paziente, id_patologia, id_terapia, " +
                            "data_diagnosi, note_patologia) VALUES (?, ?, ?, ?, ?)",
                    id_paziente,
                    patologia.getId(),
                    terapia.getId(),
                    Timestamp.valueOf(LocalDateTime.now().withNano(0)).toString(),
                    notePatologiaPaziente
            );
        }

        if(tmp[1] != null)
            terapiaId = tmp[1]; // la patologia del paziente ha già una terapia associata
        else
            terapiaId = -1; // la patologia di quel paziente non ha ancora una terapia associata


        if (terapiaId <= 0) {
            // Insert a new terapia
            terapiaId = super.getConnection().executeInsertAndReturnId(
                    "INSERT INTO terapia (id_diabetologo, id_farmaco, dosi_giornaliere," +
                            " quantita_per_dose, note) VALUES (?, ?, ?, ?, ?)",
                    terapia.getMedico().getId(),
                    terapia.getFarmaco().getId(),
                    terapia.getDosiGiornaliere(),
                    terapia.getQuantitaPerDose(),
                    terapia.getNote()
            );


            // update patologia paziente fk id_terapia with the one inserted
            if (terapiaId >= 0) {
                // Update
                super.getConnection().executeUpdate(
                        "UPDATE patologia_paziente SET id_terapia = ? WHERE id = ?",
                        terapiaId,
                        patologiaPazienteId
                );
            }

        } else {
            // Update existing terapia
            super.getConnection().executeUpdate(
                    "UPDATE terapia SET id_diabetologo = ?, id_farmaco = ?, dosi_giornaliere = ?, " +
                            "quantita_per_dose = ?, note = ? WHERE id = ?",
                    terapia.getMedico().getId(),
                    terapia.getFarmaco().getId(),
                    terapia.getDosiGiornaliere(),
                    terapia.getQuantitaPerDose(),
                    terapia.getNote(),
                    terapiaId
            );
        }
        setLog(new Log(id_paziente, getDiabetologoFromTerapia(getTerapiaFromId(terapiaId)).getId(), LogAction.SetTerapiaPaziente, null));
    }

    /**
     * Consente solamente di effettuare la modifica di una terapia esistente sulla base della sola
     * terapia nota, a differenza della funzione setTerapiaPaziente() che effettua l'inserimento o la modifica
     * sulla base della presenza o meno della terapia nella tabella patologia_paziente. Di fatto, questa funzione
     * viene utilizzata nel caso in cui sia necessario modificare una terapia di cui è nota la presenza nella tabella
     * terapia, sulla base delle sole informazioni note riguardanti terapia e modifiche da attuare.
     * @param terapia terapia esistente da modificare in tabella terapie
     */
    public void updateTerapiaPaziente(Terapia terapia) {
        super.getConnection().executeUpdate(
                "UPDATE terapia SET id_diabetologo = ?, id_farmaco = ?, dosi_giornaliere = ?, " +
                        "quantita_per_dose = ?, note = ? WHERE id = ?",
                terapia.getMedico().getId(),
                terapia.getFarmaco().getId(),
                terapia.getDosiGiornaliere(),
                terapia.getQuantitaPerDose(),
                terapia.getNote(),
                terapia.getId()
        );
        setLog(new Log(getPazienteFromTerapia(terapia).getId(), getDiabetologoFromTerapia(terapia).getId(),
                LogAction.UpdateTerapiaPaziente, null));
    }

    public void deleteTerapia(Terapia terapia) throws SQLException {
        try (Connection conn = super.getConnection().getConnection()) {
            // Enable foreign key constraints for this connection
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON"); // to apply ON DELETE CASCADE of terapia in patologia_paziente
            }

            Integer pazienteId = getPazienteFromTerapia(terapia).getId();

            // Now run the DELETE statement
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM terapia WHERE id = ?")) {
                pstmt.setInt(1, terapia.getId());
                pstmt.executeUpdate();
            }

            setLog(new Log(pazienteId, terapia.getMedico().getId(), LogAction.DeleteTerapia, null));
        }
    }


    public Paziente getPazienteFromTerapia(Terapia terapia) {
        AtomicReference<Paziente> pazienteRef = new AtomicReference<>();

        String query =
                "SELECT p.* " +
                        "FROM paziente p " +
                        "INNER JOIN patologia_paziente pp ON p.id = pp.id_paziente " +
                        "INNER JOIN terapia t ON pp.id_terapia = t.id " +
                        "WHERE t.id = ?";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    if (rs.next()) {
                        pazienteRef.set(new PazienteUser(
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
                        ));
                    }
                    return null;
                },
                terapia.getId()
        );

        return pazienteRef.get();
    }

    public Medico getDiabetologoFromTerapia(Terapia terapia) {
        AtomicReference<Medico> medicoRef = new AtomicReference<>();

        String query =
                "SELECT d.* " +
                        "FROM diabetologo d " +
                        "INNER JOIN terapia t ON t.id_diabetologo = d.id " +
                        "WHERE t.id = ?";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    if (rs.next()) {
                        medicoRef.set(new MedicoUser(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email"),
                                rs.getString("profile_image_name")
                        ));
                    }
                    return null;
                },
                terapia.getId()
        );

        return medicoRef.get();
    }



    public RilevazioneSintomo[] getRilevazioniSintomo(){
        return getRilevazioniSintomo("");
    }

    public RilevazioneSintomo[] getRilevazioniSintomo(String usernamePaziente){
        ArrayList<RilevazioneSintomo> rilevazioniSintomo = new ArrayList<>();

        String query =
                "SELECT rs.* " +
                        "FROM paziente p " +
                        "INNER JOIN rilevazione_sintomo rs ON p.id = rs.id_paziente " +
                        "WHERE (? IS NULL OR p.username = ?)";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        rilevazioniSintomo.add(new RilevazioneSintomo(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getTimestamp("timestamp"),
                                rs.getString("sintomo"),
                                rs.getInt("intensita")
                        ));
                    }
                    return null;
                },
                usernamePaziente.isEmpty() ? null : usernamePaziente,  // 1st placeholder
                usernamePaziente.isEmpty() ? null : usernamePaziente   // 2nd placeholder
        );

        return rilevazioniSintomo.toArray(new RilevazioneSintomo[0]);
    }



    public RilevazioneGlicemia[] getRilevazioniGlicemia(){
        return getRilevazioniGlicemia("");
    }

    public RilevazioneGlicemia[] getRilevazioniGlicemia(String usernamePaziente){
        ArrayList<RilevazioneGlicemia> rilevazioniGliecemia = new ArrayList<>();

        String query =
                "SELECT rg.* " +
                        "FROM paziente p " +
                        "INNER JOIN rilevazione_glicemia rg ON p.id = rg.id_paziente " +
                        "WHERE (? IS NULL OR p.username = ?)";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        rilevazioniGliecemia.add(new RilevazioneGlicemia(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getTimestamp("timestamp"),
                                rs.getInt("valore"),
                                rs.getBoolean("prima_pasto")
                        ));
                    }
                    return null;
                },
                usernamePaziente.isEmpty() ? null : usernamePaziente,  // 1st placeholder
                usernamePaziente.isEmpty() ? null : usernamePaziente   // 2nd placeholder
        );

        return rilevazioniGliecemia.toArray(new RilevazioneGlicemia[0]);
    }



    public RilevazioneFarmaco[] getRilevazioniFarmaco() throws SQLException {
        return getRilevazioniFarmaco("");
    }

    /* query esempio
        SELECT rf.*, f.*
        FROM paziente p
        INNER JOIN rilevazione_farmaco rf ON p.id = rf.id_paziente
        INNER JOIN farmaco f ON rf.id_farmaco = f.id
        WHERE (:username IS NULL OR username = :username);
     */
    public RilevazioneFarmaco[] getRilevazioniFarmaco(String usernamePaziente) throws SQLException {
        ArrayList<RilevazioneFarmaco> rilevazioniFarmaci = new ArrayList<>();

        String query =
        "SELECT r.id, r.id_paziente, r.timestamp, r.quantita, r.note, f.id AS id_farmaco, f.codice_aic, f.nome " +
        "FROM rilevazione_farmaco r " +
        "INNER JOIN farmaco f ON r.id_farmaco = f.id " +
        "WHERE (? IS NULL OR r.id_paziente = ?)";

    super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        rilevazioniFarmaci.add(new RilevazioneFarmaco(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                new Farmaco(rs.getInt("id_farmaco"), rs.getString("codice_aic"), rs.getString("nome")),
                                rs.getTimestamp("timestamp"),
                                rs.getFloat("quantita"),
                                rs.getString("note")
                        ));
                    }
                    return null;
                },
                usernamePaziente.isEmpty() ? null : getIdFromDB(usernamePaziente),
                usernamePaziente.isEmpty() ? null : getIdFromDB(usernamePaziente)
        );

        return rilevazioniFarmaci.toArray(new RilevazioneFarmaco[0]);
    }

    //  IMPORTANTE:
    // todo: gestire il dato id_paziente correttamente nelle query per gli alert, magari usando il metodo apposito per paziente
    // todo: a cosa serve?
    public Alert[] getAlert() throws SQLException {
        ArrayList<Alert> alerts = new ArrayList<>();

        String query =  "SELECT a.id, a.id_paziente, a.id_rilevazione, a.tipo_alert, a.data_alert, a.letto " +
                        "FROM alert a ";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        alerts.add(new Alert(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getInt("id_rilevazione"),
                                AlertType.fromString(rs.getString("tipo_alert")),
                                rs.getTimestamp("data_alert"),
                                rs.getBoolean("letto")
                        ));
                    }
                    return null;
                });

        return alerts.toArray(new Alert[0]);
    }

    public Farmaco[] getFarmaci() throws SQLException {
        ArrayList<Farmaco> farmaci = new ArrayList<>();

        String query =  "SELECT f.id, f.codice_aic, f.nome " +
                        "FROM farmaco f";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        farmaci.add(new Farmaco(
                                rs.getInt("id"),
                                rs.getString("codice_aic"),
                                rs.getString("nome")));
                    }
                    return null;
                });

        return farmaci.toArray(new Farmaco[0]);
    }


    ///  prende tutti gli alerts presenti nella tabella e quindi solamente quelli per i medici (gli alert degli utenti non vanno nella tabella alert)
    public Alert[] getAllAlerts(AlertFilter filter) throws SQLException {
        ArrayList<Alert> alerts = new ArrayList<>();

        StringBuilder query = new StringBuilder(
                "SELECT a.id, a.id_paziente, a.id_rilevazione, a.tipo_alert, a.data_alert, a.letto FROM alert a"
        );

        switch (filter) {
            case READ -> query.append(" WHERE a.letto = 1");
            case UNREAD -> query.append(" WHERE a.letto = 0");
            case ALL -> {
                // nessuna condizione WHERE
            }
        }

        super.getConnection().executeQuery(
                query.toString(),
                rs -> {
                    while (rs.next()) {
                        alerts.add(new Alert(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getInt("id_rilevazione"),
                                AlertType.fromString(rs.getString("tipo_alert")),
                                rs.getTimestamp("data_alert"),
                                rs.getBoolean("letto")
                        ));
                    }
                    return null;
                }
                //getIdFromDB(ViewNavigator.getAuthenticatedUser())
        );

        return alerts.toArray(new Alert[0]);
    }

    public Alert[] getAlertsPazientiCurati(AlertFilter filter) throws SQLException {
        ArrayList<Alert> alerts = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT a.id, a.id_paziente, a.id_rilevazione, a.tipo_alert, a.data_alert, a.letto " +
                        "FROM alert a " +
                        "INNER JOIN paziente p ON p.id = a.id_paziente " +
                        "WHERE p.id_diabetologo = ?"
        );

        switch (filter) {
            case READ -> query.append(" WHERE a.letto = 1");
            case UNREAD -> query.append(" WHERE a.letto = 0");
            case ALL -> {
                // nessuna condizione WHERE
            }
        }

        super.getConnection().executeQuery(
                query.toString(),
                rs -> {
                    while (rs.next()) {
                        alerts.add(new Alert(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getInt("id_rilevazione"),
                                AlertType.fromString(rs.getString("tipo_alert")),
                                rs.getTimestamp("data_alert"),
                                rs.getBoolean("letto")
                        ));
                    }
                    return null;
                },
                getIdFromDB(ViewNavigator.getAuthenticatedUsername())
        );

        return alerts.toArray(new Alert[0]);
    }

    public Alert[] getAlertPaziente(String usernamePaziente) throws SQLException{
        ArrayList<Alert> alerts = new ArrayList<>();

        String query =  "SELECT * " +
                        "FROM alert a " +
                        "WHERE a.id_paziente = ?";

        super.getConnection().executeQuery(
                query,
                rs -> {
                    while (rs.next()) {
                        alerts.add(new Alert(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getInt("id_rilevazione"),
                                AlertType.fromString(rs.getString("tipo_alert")),
                                rs.getTimestamp("data_alert"),
                                rs.getBoolean("letto")
                        ));
                    }
                    return null;
                },
                usernamePaziente.isEmpty() ? null : getIdFromDB(usernamePaziente)
        );

        return alerts.toArray(new Alert[0]);
    }

    @Override
    public int countAlerts() throws SQLException {
        return getAllAlerts(AlertFilter.UNREAD).length;
    }

}
