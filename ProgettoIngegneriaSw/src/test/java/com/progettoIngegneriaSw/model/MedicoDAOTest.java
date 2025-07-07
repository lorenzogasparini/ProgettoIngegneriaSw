package com.progettoIngegneriaSw.model;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.*;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Utils.*;
import com.progettoingegneriasw.view.ViewNavigator;
import org.junit.jupiter.api.*;
import utils.SQLiteTestDatabase;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicoDAOTest {

    private SQLiteTestDatabase sqliteDb;
    private MedicoDAO medicoDAO;

    @BeforeAll
    void setup() {
        sqliteDb = new SQLiteTestDatabase(AppConfig.TESTSCHEMA_PATH);
        medicoDAO = MedicoDAO.getInstance();
        medicoDAO.setConnection(sqliteDb.getDatabaseManager());
        UserDAO.getInstance().setConnection(sqliteDb.getDatabaseManager());
    }

    @AfterAll
    void tearDown() {
        sqliteDb.close();
    }

    @Test
    void testGetAllPazienti() {
        Paziente[] pazienti = medicoDAO.getAllPazienti();
        assertNotNull(pazienti);
        assertTrue(pazienti.length > 0);
    }

    @Test
    void testGetPazientiAssegnati() throws SQLException {
        Paziente[] pazienti = medicoDAO.getPazientiAssegnati("drrossi");
        assertNotNull(pazienti);
        assertTrue(pazienti.length > 0);
    }

    @Test
    void testGetPatologiePaziente() throws SQLException {
        Patologia[] patologie = medicoDAO.getPatologiePaziente("mario.rossi");
        assertNotNull(patologie);
        assertTrue(patologie.length > 0);
    }

    // todo: testSetPatologiePaziente()
    @Test
    void testSetPatologiaPaziente() throws SQLException{
        Patologia newPatologia = medicoDAO.getPatologiaFromId(4);
        Terapia newTerapia = medicoDAO.getTerapiaFromId(4);
        medicoDAO.setPatologiaPaziente(newPatologia, "mario.rossi", newTerapia,
                Date.valueOf(LocalDate.now()), "test note patologia");

        Boolean found = sqliteDb.getDatabaseManager().executeQuery(
                "SELECT 1 as found FROM patologia_paziente pp" +
                        " WHERE id_paziente = ? AND id_terapia = ? AND id_patologia = ?",
                rs -> rs.next() && rs.getBoolean("found"),
                medicoDAO.getUser("mario.rossi").getId(),
                newTerapia.getId(),
                newPatologia.getId()
        );
        assertTrue(found);
    }

    @Test
    void testGetAllPatologie() throws SQLException {
        Patologia[] patologie = medicoDAO.getPatologie(); // return all patologie
        assertNotNull(patologie);
        assertTrue(patologie.length > 0);
    }

    @Test
    void testGetPatologiaFiltered() throws SQLException {
        Patologia[] patologie = medicoDAO.getPatologie("Ipertensione"); // get specifico patologia
        assertNotNull(patologie);
        assertEquals(2, patologie[0].getId());
    }


    @Test
    void testGetPatologiaFromId() throws SQLException {
        Patologia[] patologie = medicoDAO.getPatologie("Diabete mellito tipo 2");
        assertNotNull(patologie);
        assertEquals(1, patologie[0].getId());
    }

    @Test
    void testGetNonExistingPatologiaFromId() throws SQLException {
        Patologia[] patologie = medicoDAO.getPatologie("Patologia non esistente");
        assertNotNull(patologie);
        assertEquals(0, patologie.length);
    }

    @Test
    void testAddPatologia() throws SQLException {
        Patologia nuova = new Patologia("TestPatologia", "T999");
        medicoDAO.addPatologia(nuova);

        Patologia[] found = medicoDAO.getPatologie("TestPatologia");
        assertTrue(found.length > 0);
    }

    @Test
    void testGetTerapiaFromId() throws SQLException {
        Terapia terapia = medicoDAO.getTerapiaFromId(2);
        assertNotNull(terapia);
        assertEquals(2, terapia.getId());
    }

    @Test
    void testNonExistingTerapiaFromId() throws SQLException {
        Terapia terapia = medicoDAO.getTerapiaFromId(1);
        assertNull(terapia);
    }

    @Test
    void testGetTerapiaFromIdAndHelpers() throws SQLException {
        Terapia[] terapie = medicoDAO.getTerapiePaziente("mario.rossi");
        assertTrue(terapie.length > 0);

        Terapia t = medicoDAO.getTerapiaFromId(terapie[0].getId());
        assertNotNull(t);

        Paziente p = medicoDAO.getPazienteFromTerapia(t);
        assertNotNull(p);

        Medico m = medicoDAO.getDiabetologoFromTerapia(t);
        assertNotNull(m);
    }

    @Test
    void testGetTerapiePaziente() throws SQLException {
        Terapia[] terapie = medicoDAO.getTerapiePaziente("mario.rossi");
        assertNotNull(terapie);
        assertTrue(terapie.length > 0);
    }

    @Test
    void testSetTerapiaPazienteInsertAndUpdate() throws SQLException {
        Farmaco farmaco = new Farmaco(1, "012345678", "Metformina");
        MedicoUser medico = (MedicoUser) medicoDAO.getUser("drdestri");
        Patologia patologia = medicoDAO.getPatologie("Diabete")[0];

        assertNotNull(farmaco);
        assertNotNull(medico);
        assertNotNull(patologia);

        Terapia terapia = new Terapia(medico, farmaco, 2, 10.0, "test");
        medicoDAO.setTerapiaPaziente(terapia, "mario.rossi", patologia, "test");

        Terapia[] terapie = medicoDAO.getTerapiePaziente("mario.rossi");
        boolean exists = Arrays.stream(terapie).anyMatch(t -> t.getNote().equals("test"));
        assertTrue(exists);

        Terapia newTerapia = new Terapia(terapie[0].getId(), terapie[0].getMedico(), terapie[0].getFarmaco(),
                4, 50.0, "da assumere ogni 4 ore");
        medicoDAO.updateTerapiaPaziente(newTerapia);

        Terapia[] afterUpdate = medicoDAO.getTerapiePaziente("mario.rossi");
        boolean updatedExists = Arrays.stream(afterUpdate)
                .anyMatch(t -> t.getNote().equals("da assumere ogni 4 ore"));
        assertTrue(updatedExists);
    }

    @Test
    void testDeleteTerapia() throws SQLException {
        Farmaco farmaco = new Farmaco(2, "987654321", "Lisinopril");
        Medico medico = (MedicoUser) medicoDAO.getUser("drrossi");
        Terapia terapia = new Terapia(medico, farmaco, 1, 5.0, "Terapia da cancellare");
        Patologia patologia = medicoDAO.getPatologie("Ipertensione")[0];

        medicoDAO.setTerapiaPaziente(terapia, "lucia.verdi", patologia, "Note temporanee");

        Terapia toDelete = Arrays.stream(medicoDAO.getTerapiePaziente("lucia.verdi"))
                .filter(t -> t.getNote().equals("Terapia da cancellare"))
                .findFirst().orElse(null);

        assertNotNull(toDelete);
        medicoDAO.deleteTerapia(toDelete);

        Terapia[] afterDelete = medicoDAO.getTerapiePaziente("lucia.verdi");
        boolean stillExists = Arrays.stream(afterDelete).anyMatch(t -> t.getNote().equals("Terapia da cancellare"));
        assertFalse(stillExists);
    }

    @Test
    void testGetPazienteFromTerapia() throws SQLException {
        Terapia[] terapie = medicoDAO.getTerapiePaziente("mario.rossi");
        assertTrue(terapie.length > 0);
        Terapia t = terapie[0];
        assertNotNull(t);

        Paziente p = medicoDAO.getPazienteFromTerapia(t);
        assertEquals("mario.rossi", p.getUsername());
    }

    @Test
    void testGetDiabetologoFromTerapia() throws  SQLException {
        Terapia[] terapie = medicoDAO.getTerapiePaziente("mario.rossi");
        assertTrue(terapie.length > 0);
        Terapia t = terapie[0];
        assertNotNull(t);

        Medico m = medicoDAO.getDiabetologoFromTerapia(t);
        assertEquals("drrossi", m.getUsername());
    }

    @Test
    void testGetRilevazioniGlicemiaWithoutPaziente() {
        RilevazioneGlicemia[] rilevazioni = medicoDAO.getRilevazioniGlicemia();
        assertNotNull(rilevazioni);
        assertTrue(rilevazioni.length > 0);
    }

    @Test
    void testGetRilevazioniGlicemiaWithPaziente() {
        RilevazioneGlicemia[] rilevazioni = medicoDAO.getRilevazioniGlicemia("mario.rossi");
        assertNotNull(rilevazioni);
        assertTrue(rilevazioni.length > 0);
    }

    @Test
    void testGetRilevazioniSintomoWithoutPaziente() {
        RilevazioneSintomo[] sintomi = medicoDAO.getRilevazioniSintomo();
        assertNotNull(sintomi);
        assertTrue(sintomi.length > 0);
    }

    @Test
    void testGetRilevazioniSintomoWithPaziente() {
        RilevazioneSintomo[] sintomi = medicoDAO.getRilevazioniSintomo("mario.rossi");
        assertNotNull(sintomi);
        assertTrue(sintomi.length > 0);
    }

    @Test
    void testGetRilevazioniFarmacoWithoutPaziente() throws SQLException {
        RilevazioneFarmaco[] farmaci = medicoDAO.getRilevazioniFarmaco();
        assertNotNull(farmaci);
        assertTrue(farmaci.length > 0);
    }

    @Test
    void testGetRilevazioniFarmacoWithPaziente() throws SQLException {
        RilevazioneFarmaco[] farmaci = medicoDAO.getRilevazioniFarmaco("mario.rossi");
        assertNotNull(farmaci);
        assertTrue(farmaci.length > 0);
    }

    @Test
    void testGetFarmaci() throws SQLException {
        Farmaco[] farmaci = medicoDAO.getFarmaci();
        assertNotNull(farmaci);
        assertTrue(farmaci.length > 0);
    }

    @Test
    void testGetAllAlertsVariants() throws SQLException {
        Alert[] all = medicoDAO.getAllAlerts(AlertFilter.ALL);
        Alert[] read = medicoDAO.getAllAlerts(AlertFilter.READ);
        Alert[] unread = medicoDAO.getAllAlerts(AlertFilter.UNREAD);

        assertNotNull(all);
        assertNotNull(read);
        assertNotNull(unread);
    }

    @Test
    void testGetAlertPaziente() throws SQLException {
        Alert[] alerts = medicoDAO.getAlertPaziente("mario.rossi");
        assertNotNull(alerts);
        assertTrue(alerts.length > 0);
    }

    @Test
    void testCountAlerts() throws SQLException {
        int count = medicoDAO.countAlerts();
        assertTrue(count >= 0);
    }


}
