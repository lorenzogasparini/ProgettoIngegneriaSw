package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;

import java.sql.SQLException;
import java.sql.Timestamp;
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
    private PazienteDAO() {
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


    /// funzione per ottenere tutti i farmaci che un utente deve assumere
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
    public Map<Terapia, Boolean> getTerapieEAssunzioniPaziente(String username) throws SQLException {
        int id_paziente = getIdFromDB(username);
        Map<Terapia, Boolean> terapieEAssunzioni = new HashMap<>();
        if(id_paziente == -1) {
            throw new SQLException();   // todo: verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                " SELECT " +
                        "     f.id AS id_farmaco, f.codice_aic, f.nome, t.*, " +
                        "     d.id AS id_diabetologo, d.nome AS nome_diabetologo, d.cognome AS cognome_diabetologo, "+
                        "     d.username AS username_diabetologo, d.password AS password_diabetologo, " +
                        "     d.email AS email_diabetologo, d.profile_image_name AS profile_image_name_diabetologo, " +
                        "     CASE " +
                        "         WHEN EXISTS ( " +
                        "              SELECT 1 " +
                        "              FROM rilevazione_farmaco rf " +
                        "              WHERE rf.id_paziente = p.id " +
                        "                 AND rf.id_farmaco = f.id " +
                        "                 AND DATE(rf.timestamp) = DATE('now') " +
                        "         ) " +
                        "         THEN 1 " +
                        "         ELSE 0 " +
                        "     END AS has_taken_today " +
                        " FROM paziente p " +
                        " INNER JOIN patologia_paziente pp ON p.id = pp.id_paziente " +
                        " INNER JOIN terapia t ON pp.id_terapia = t.id " +
                        " INNER JOIN diabetologo d ON d.id = t.id_diabetologo " +
                        " INNER JOIN farmaco f ON t.id_farmaco = f.id " +
                        " WHERE p.id = ? ",
                rs -> {
                    while (rs.next()) {
                        terapieEAssunzioni.put( // todo: sistemare
                                new Terapia(
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
                                ),
                                rs.getBoolean("has_taken_today")
                        );
                    }
                    return null;
                },
                id_paziente
        );
        return terapieEAssunzioni;
    }

    /*
    public boolean checkFarmacoAssuntoOggi(Farmaco farmaco) throws SQLException {
        int id_paziente = getIdFromDB(ViewNavigator.getAuthenticatedUsername());

        Map<Terapia, Boolean> farmaciEAssunzioni = getTerapieEAssunzioni(ViewNavigator.getAuthenticatedUsername());

        return farmaciEAssunzioni.get(farmaco);
    }
     */

    public void setRilevazioneSintomo(RilevazioneSintomo rilevazioneSintomo){
        super.getConnection().executeUpdate(
                "INSERT INTO rilevazione_sintomo (id_paziente, timestamp, sintomo, intensita) VALUES (?, ?, ?, ?)",
                rilevazioneSintomo.getIdPaziente(),
                Timestamp.valueOf(rilevazioneSintomo.getTimestamp().toLocalDateTime().withNano(0)).toString(),
                rilevazioneSintomo.getSintomo(),
                rilevazioneSintomo.getIntensita()
        );
        setLog(new Log(rilevazioneSintomo.getIdPaziente(), null, LogAction.SetRilevazioneSintomo, null));
    }

    public void setRilevazioneGlicemia(RilevazioneGlicemia rilevazioneGlicemia){
        Integer rilevazioneGlicemiaId = super.getConnection().executeInsertAndReturnId(
                "INSERT INTO rilevazione_glicemia (id_paziente, timestamp, valore, gravita, prima_pasto) " +
                        "VALUES (?, ?, ?, ?, ?)",
                rilevazioneGlicemia.getIdPaziente(),
                Timestamp.valueOf(rilevazioneGlicemia.getTimestamp().toLocalDateTime().withNano(0)).toString(),
                rilevazioneGlicemia.getValore(),
                rilevazioneGlicemia.getGravita(),
                rilevazioneGlicemia.getPrimaPasto()
        );

        if(rilevazioneGlicemia.getGravita() !=  0){
            insertAlerts(new Alert[]{
                    new Alert(rilevazioneGlicemia.getIdPaziente(),
                            rilevazioneGlicemiaId,
                            AlertType.glicemia,
                            rilevazioneGlicemia.getTimestamp(),
                            false)
            });
        }
        setLog(new Log(rilevazioneGlicemia.getIdPaziente(), null, LogAction.SetRilevazioneGlicemia, null));
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
                Timestamp.valueOf(rilevazioneFarmaco.getTimestamp().toLocalDateTime().withNano(0)).toString(),
                rilevazioneFarmaco.getQuantita(),
                rilevazioneFarmaco.getNoteRilevazione()
        );
        setLog(new Log(rilevazioneFarmaco.getIdPaziente(), null, LogAction.SetRilevazioneFarmaco, null));
    }

    public MedicoUser getMedicoRiferimento(String username){

        User user = getUser(username);
        if(!user.isPaziente())
            throw new IllegalArgumentException("getMedicoRiferimento() può essere chiamato solo da pazienti!");

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
                                rs.getString("email"),
                                rs.getString("profile_image_name")
                        );
                    }
                    return null;
                },
                id_paziente
        );
    }

    public int countAlerts() throws SQLException {
        return this.countAlerts(ViewNavigator.getAuthenticatedUsername());
    }


    public int countAlerts(String username) throws SQLException {
        Map<Terapia, Boolean> terapieEAssunzioni = getTerapieEAssunzioniPaziente(username);
        int count = 0;

        for (Terapia t: terapieEAssunzioni.keySet()){
            if (!terapieEAssunzioni.get(t))
                count++;
        }

        return count;
    }

}
