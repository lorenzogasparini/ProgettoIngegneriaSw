package com.progettoIngegneriaSw.model;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.*;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import org.junit.jupiter.api.*;
import utils.SQLiteTestDatabase;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PazienteDAOTest {

    private SQLiteTestDatabase sqliteDb;
    private PazienteDAO pazienteDAO;

    @BeforeAll
    void setup() {
        sqliteDb = new SQLiteTestDatabase(AppConfig.TESTSCHEMA_PATH);
        pazienteDAO = PazienteDAO.getInstance();
        pazienteDAO.setConnection(sqliteDb.getDatabaseManager());
        UserDAO.getInstance().setConnection(sqliteDb.getDatabaseManager()); // ensure UserDAO also uses test db
    }

    @AfterAll
    void tearDown() {
        sqliteDb.close();
    }

    @Test
    void testGetFarmaciPaziente() throws SQLException {
        Farmaco[] farmaci = pazienteDAO.getFarmaciPaziente("mario.rossi");
        assertNotNull(farmaci);
        assertTrue(farmaci.length > 0);
    }

    @Test
    void testGetTerapieEAssunzioniPaziente() throws SQLException {
        Map<Terapia, Boolean> terapie = pazienteDAO.getTerapieEAssunzioniPaziente("mario.rossi");
        assertNotNull(terapie);
        assertFalse(terapie.isEmpty());

        for (Map.Entry<Terapia, Boolean> entry : terapie.entrySet()) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        }
    }

    @Test
    void testSetRilevazioneSintomo() {
        RilevazioneSintomo sintomo = new RilevazioneSintomo(
                pazienteDAO.getIdFromDB("mario.rossi"),
                Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                "Nausea improvvisa",
                6
        );
        pazienteDAO.setRilevazioneSintomo(sintomo);

        Boolean found = sqliteDb.getDatabaseManager().executeQuery(
                "SELECT 1 as found FROM rilevazione_sintomo WHERE sintomo = ?",
                rs -> rs.next() && rs.getBoolean("found"),
                "Nausea improvvisa"
        );
        assertTrue(found);
    }

    @Test
    void testSetRilevazioneGlicemia() {
        RilevazioneGlicemia glicemia = new RilevazioneGlicemia(
                pazienteDAO.getUser("mario.rossi").getId(),
                Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                250,
                true
        );
        pazienteDAO.setRilevazioneGlicemia(glicemia);

        Boolean found = sqliteDb.getDatabaseManager().executeQuery(
                "SELECT 1 as found FROM rilevazione_glicemia WHERE valore = ?",
                rs -> rs.next() && rs.getBoolean("found"),
                250
        );
        assertTrue(found);
    }

    @Test
    void testSetRilevazioneFarmaco() {
        Farmaco farmaco = new Farmaco(1, "012345678", "Metformina");
        RilevazioneFarmaco rilevazione = new RilevazioneFarmaco(
                pazienteDAO.getUser("mario.rossi").getId(),
                farmaco,
                Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                20.0,
                "Test set rilevazione farmaco"
        );
        pazienteDAO.setRilevazioneFarmaco(rilevazione);

        Boolean found = sqliteDb.getDatabaseManager().executeQuery(
                "SELECT 1 as found FROM rilevazione_farmaco WHERE note = ?",
                rs -> rs.next() && rs.getBoolean("found"),
                "Test set rilevazione farmaco"
        );
        assertTrue(found);
    }

    @Test
    void testGetMedicoRiferimento() {
        MedicoUser medico = pazienteDAO.getMedicoRiferimento("mario.rossi");
        assertNotNull(medico);
        assertEquals("drrossi", medico.getUsername());
    }

    @Test
    void testCountAlerts() throws SQLException {

        int alerts = pazienteDAO.countAlerts("mario.rossi");
        assertTrue(alerts >= 0);
    }
}
