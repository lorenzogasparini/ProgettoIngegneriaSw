package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Paziente Model is the DAO (Data Access Object) class
 */
public class PazienteDAO extends UserDAO {

    private final String SQLTableName = "paziente";
    private static PazienteDAO instance;

    // todo: capire in che modo renderlo privato per implementare il singleton
    public PazienteDAO() {
        // Init DB connection, maybe from a config class
        super();
    }

    public static synchronized PazienteDAO getInstance() { // synchronized to avoid more thread accessing at the same time
        if (instance == null) {
            instance = new PazienteDAO();
        }
        return instance;
    }

    @Override
    public String getSQLTableName(){
        return SQLTableName;
    }

    public int getMedico(Paziente paziente){ // note: methods don't have to be static
        // todo: get via SQL id_diabetologo del paziente passato
        return 0; // todo: completare
    }


    // todo: aggiungere i metodi per i pazienti (non vanno messi nell'interfaccia perchè non esistono interfaccie per i DAO)
    // todo: aggiungi metodi di inserimento rilevazioni_farmaco, glicemia e sintomo

    // funzione per ottenere tutti i farmaci che un utente deve assumere
    /* query esempio
            SELECT f.*
            FROM paziente p
            INNER JOIN patologia_paziente pp ON p.id = pp.id_paziente
            INNER JOIN terapia t ON pp.id_terapia = t.id
            INNER JOIN farmaco f ON t.id_farmaco = f.id
            WHERE p.username = "giulia.bianchi"
         */
    public Farmaco[] getFarmaciPaziente(String username) throws SQLException {

        int id_paziente = getIdFromDB(username);
        ArrayList<Farmaco> farmaci = new ArrayList<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                "SELECT f.*" +
                        " FROM paziente p" +
                        " INNER JOIN patologia_paziente pp ON p.id = pp.id_paziente" +
                        " INNER JOIN terapia t ON pp.id_terapia = t.id" +
                        " INNER JOIN farmaco f ON t.id_farmaco = f.id" +
                        " WHERE p.id = ?",
                rs -> {
                    while (rs.next()) {
                        farmaci.add(new Farmaco(rs.getInt("id"),
                                rs.getString("codice_aic"),
                                rs.getString("nome")
                        ));
                    }
                    return null;
                },
                id_paziente
        );
        return farmaci.toArray(new Farmaco[farmaci.size()]);

    }

    // query alternativa per sapere tutti i farmaci che deve assumere e quali ha già assunto
     /*
        -- query per prendersi tutti i farmaci di un paziente e per verificare quali ha già assunto
        SELECT
            f.*,
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM rilevazione_farmaco rf
                    WHERE rf.id_paziente = p.id
                      AND rf.id_farmaco = f.id
                      AND DATE(rf.timestamp) = DATE('now')
                )
                THEN 1
                ELSE 0
            END AS has_taken_today
        FROM paziente p
        INNER JOIN patologia_paziente pp ON p.id = pp.id_paziente
        INNER JOIN terapia t ON pp.id_terapia = t.id
        INNER JOIN farmaco f ON t.id_farmaco = f.id
        WHERE p.username = 'mario.rossi';
         */
    public Map<Farmaco, Boolean> getFarmaciPazienteEAssunzioni(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        Map<Farmaco, Boolean> farmaciEAssunzioni = new HashMap<>();
        if(id_paziente == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                "SELECT" +
                        "     f.*," +
                        "     CASE" +
                        "         WHEN EXISTS (" +
                        "              SELECT 1" +
                        "              FROM rilevazione_farmaco rf" +
                        "              WHERE rf.id_paziente = p.id" +
                        "                 AND rf.id_farmaco = f.id" +
                        "                 AND DATE(rf.timestamp) = DATE('now')" +
                        "         )" +
                        "         THEN 1" +
                        "         ELSE 0" +
                        "     END AS has_taken_today" +
                        " FROM paziente p" +
                        " INNER JOIN patologia_paziente pp ON p.id = pp.id_paziente" +
                        " INNER JOIN terapia t ON pp.id_terapia = t.id" +
                        " INNER JOIN farmaco f ON t.id_farmaco = f.id" +
                        " WHERE p.id = ?",
                rs -> {
                    while (rs.next()) {
                        farmaciEAssunzioni.put(
                                new Farmaco(rs.getInt("id"),
                                        rs.getString("codice_aic"),
                                        rs.getString("nome")
                                ),
                                rs.getBoolean("has_taken_today")
                        );
                    }
                    return null;
                },
                id_paziente
        );
        return farmaciEAssunzioni;
    }



    public void setRilevazioneSintomo(RilevazioneSintomo rilevazioneSintomo){
        super.getConnection().executeUpdate(
                "INSERT INTO rilevazione_sintomo (id_paziente, timestamp, sintomo, intensita) VALUES (?, ?, ?, ?)",
                rilevazioneSintomo.getIdPaziente(),
                rilevazioneSintomo.getTimestamp().toString(),
                rilevazioneSintomo.getSintomo(),
                rilevazioneSintomo.getIntensita()
        );
    }

    public void setRilevazioneGlicemia(RilevazioneGlicemia rilevazioneGlicemia){
        Integer rilevazioneGlicemiaId = super.getConnection().executeInsertAndReturnId(
                "INSERT INTO rilevazione_glicemia (id_paziente, timestamp, valore, gravita, prima_pasto) " +
                        "VALUES (?, ?, ?, ?, ?)",
                rilevazioneGlicemia.getIdPaziente(),
                rilevazioneGlicemia.getTimestamp().toString(),
                rilevazioneGlicemia.getValore(),
                rilevazioneGlicemia.getGravita(),
                rilevazioneGlicemia.getPrimaPasto()
        );

        if(rilevazioneGlicemia.getGravita() !=  0){
            insertAlertGlicemia(new Alert(rilevazioneGlicemia.getIdPaziente(), rilevazioneGlicemiaId,
                    AlertType.glicemia, rilevazioneGlicemia.getTimestamp(), false));
        }

    }


    /*
        -- query di inserimento farmaco:
        INSERT INTO rilevazione_farmaco(id_paziente, id_farmaco, quantita, note)
        VALUES(1, 2, 1, "farmaco assunto senza problemi")
         */
    public void setRilevazioneFarmaco(RilevazioneFarmaco rilevazioneFarmaco){
        super.getConnection().executeUpdate(
                "INSERT INTO rilevazione_farmaco (id_paziente, id_farmaco, timestamp, quantita, note) " +
                        "VALUES (?, ?, ?, ?, ?)",
                rilevazioneFarmaco.getIdPaziente(),
                rilevazioneFarmaco.getFarmaco().getId(),
                rilevazioneFarmaco.getTimestamp().toString(),
                rilevazioneFarmaco.getQuantita(),
                rilevazioneFarmaco.getNote()
        );
    }

    // funzione getMedicoRiferimento() --> ok!
    public MedicoUser getMedicoRiferimento(String username){
        int id_paziente = getIdFromDB(username);

        return getConnection().executeQuery(
                "SELECT d.* " +
                        " FROM paziente p " +
                        " JOIN diabetologo d ON p.id_diabetologo = d.id" +
                        " WHERE p.id = ?",
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
                id_paziente
        );
    }


    /// funzione per l'inserimento in Alert se ci sono dei dati sballati in rilevazione_glicemia --> ok!
    public void insertAlertGlicemia(Alert alert){
        final String alertType = "glicemia";

        super.getConnection().executeUpdate(
                "INSERT INTO alert (id_paziente, id_rilevazione, tipo_alert, data_alert, letto) " +
                        "VALUES (?, ?, ?, ?, ?)",
                alert.getIdPaziente(),
                alert.getIdRilevazione(),
                alert.getTipoAlert(),
                alert.getTimestamp().toString(),
                alert.getLetto()
        );
    }

    /// funzione contattaDiabetologo() per aprire l'app di email di default del dispostivo --> ok!
    /// per impostarla su Linux: Impostazioni > Applicazioni > App. predefinite > Email e selezionare dal menu a tendina
    /// (consigliata Tunderbird)
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

    private static String uriEncode(String s) {
        return s.replace(" ", "%20").replace("\n", "%0A").replace("\r", "%0D");
    }



}
