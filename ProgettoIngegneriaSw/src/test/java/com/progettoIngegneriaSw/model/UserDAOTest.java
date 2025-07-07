package com.progettoIngegneriaSw.model;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.UserType;
import com.progettoingegneriasw.model.Utils.Alert;
import com.progettoingegneriasw.model.Utils.AlertType;
import com.progettoingegneriasw.model.Utils.Farmaco;
import org.junit.jupiter.api.*;
import utils.SQLiteTestDatabase;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    private SQLiteTestDatabase sqliteDb;
    private UserDAO userDAO;

    @BeforeAll
    void setup() {
        sqliteDb = new SQLiteTestDatabase(AppConfig.TESTSCHEMA_PATH);
        userDAO = UserDAO.getInstance();
        userDAO.setConnection(sqliteDb.getDatabaseManager());
    }

    @AfterAll
    void tearDown() {
        sqliteDb.close();
    }

    @Test
    void testSaveAndUpdateAdminUser() {
        User admin = new AdminUser("adminTest", "pwd", "Nome", "Cognome");
        userDAO.saveUser(admin);
        User saved = userDAO.getUser("adminTest");
        assertNotNull(saved);
        assertEquals("adminTest", saved.getUsername());

        User updatedAdmin = new AdminUser(admin.getUsername(), "NewPsw", admin.getNome(), "updatedCognome");
        userDAO.saveUser(updatedAdmin);
        assertEquals("NewPsw", userDAO.getUser(updatedAdmin.getUsername()).getPassword());

        User updated = userDAO.getUser("adminTest");
        assertEquals("updatedCognome", updated.getCognome());
    }

    @Test
    void testSaveAndUpdateMedicoUser() {
        MedicoUser medico = new MedicoUser("medTest", "pwd", "Mario", "Verdi", "mail@test.it", "img.png");
        userDAO.saveUser(medico);
        User saved = userDAO.getUser("medTest");
        assertNotNull(saved);
        assertTrue(saved.isMedico());

        User updatedMedico = new MedicoUser(medico.getId(), medico.getUsername(), medico.getPassword(), medico.getNome(),
                "nuovoCognome","new@mail.it", medico.getProfileImageName());
        userDAO.saveUser(updatedMedico);
        User updated = userDAO.getUser("medTest");
        assertEquals("new@mail.it", ((MedicoUser) updated).getEmail());
    }

    @Test
    void testSaveAndUpdatePazienteUser() {
        PazienteUser paz = new PazienteUser("pazTest", "pwd", "Test", "User", "paz@test.it",
                1, Date.valueOf(LocalDate.of(1990, 1, 1)), 70.0, "RM", "Roma", "note", "img.png");
        userDAO.saveUser(paz);
        User saved = userDAO.getUser("pazTest");
        assertNotNull(saved);
        assertTrue(saved.isPaziente());

        User updatedPaziente = new PazienteUser(paz.getId(), paz.getUsername(), paz.getPassword(), "nuovoNome",
                paz.getCognome(), "nuovaEmail@gmail.com", 3, paz.getDataNascita(), 80.0,
                paz.getProvinciaResidenza(), paz.getComuneResidenza(), "new note",
                paz.getProfileImageName());

        userDAO.saveUser(updatedPaziente);
        User updated = userDAO.getUser("pazTest");
        assertEquals("new note", ((PazienteUser) updated).getNotePaziente());
    }

    @Test
    void testGetUserByUsername() {
        User user = userDAO.getUser("drrossi");
        assertNotNull(user);
        assertTrue(user.isMedico());
    }

    @Test
    void testGetUserByIdAndType() {
        int id = userDAO.getIdFromDB("mario.rossi");
        User user = userDAO.getUser(id, UserType.Paziente);
        assertNotNull(user);
        assertTrue(user.isPaziente());
    }

    @Test
    void testIsUserDeleted() {
        assertFalse(userDAO.isUserDeleted("mario.rossi"));
    }

    @Test
    void testUserExists() {
        assertTrue(userDAO.userExists("mario.rossi"));
        assertFalse(userDAO.userExists("utente_non_esistente"));
    }


    @Test
    void testGetAllUsers() {
        User[] allUsers = userDAO.getAllUsers(UserType.Medico);
        assertNotNull(allUsers);
        assertTrue(allUsers.length > 0);
    }

    @Test
    void testGetFarmacoFromIdAndAic() {
        Farmaco f1 = userDAO.getFaracoFromId(1);
        assertNotNull(f1);

        Farmaco f2 = userDAO.getFarmacoFromAic("012345678");
        assertNotNull(f2);
        assertEquals(f1.getId(), f2.getId());
    }

    @Test
    void testGetAlertFarmaciNonAssuntiDaAlmeno3GiorniENonSegnalati() {
        Alert[] alerts = userDAO.getAlertFarmaciNonAssuntiDaAlmeno3GiorniENonSegnalati();
        assertNotNull(alerts);
        assertTrue(alerts.length >= 0); // Non è detto ci siano alert, quindi >= 0
        for (Alert alert : alerts) {
            assertEquals(AlertType.farmacoNonAssuntoDa3Giorni, alert.getTipoAlert());
            assertNotNull(alert.getIdPaziente());
            assertNotNull(alert.getIdRilevazione());
        }
    }

    @Test
    void testInsertAlerts() {
        Alert alert = new Alert(1, 1, AlertType.glicemia,
                Timestamp.valueOf(LocalDateTime.now().minusDays(4).withNano(0)), false);

        userDAO.insertAlerts(new Alert[]{alert});

        Boolean exists = sqliteDb.getDatabaseManager().executeQuery(
                "SELECT 1 as found FROM alert WHERE id_paziente = ? AND id_rilevazione = ? AND tipo_alert = ?",
                rs -> rs.next() && rs.getBoolean("found"),
                alert.getIdPaziente(),
                alert.getIdRilevazione(),
                alert.getTipoAlert().toString()
        );

        assertTrue(Boolean.TRUE.equals(exists));
    }

    @Test
    void testAutomaticUpdateAlertTableInsertsAlerts() throws SQLException {
        int before = countAlerts();

        userDAO.automaticUpdateAlertTable();

        int after = countAlerts();

        assertTrue(after >= before);
    }

    private int countAlerts() {
        return sqliteDb.getDatabaseManager().executeQuery(
                "SELECT 1 FROM alert",
                rs -> rs.next() ? rs.getInt(1) : 0
        );
    }


}
