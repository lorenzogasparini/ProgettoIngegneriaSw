package com.progettoIngegneriaSw.model;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.Utils.Log;
import org.junit.jupiter.api.*;
import utils.SQLiteTestDatabase;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminDAOTest {

    private SQLiteTestDatabase sqliteDb;
    private AdminDAO adminDAO;

    @BeforeAll
    void setup() {
        sqliteDb = new SQLiteTestDatabase(AppConfig.TESTSCHEMA_PATH);
        adminDAO = AdminDAO.getInstance();
        adminDAO.setConnection(sqliteDb.getDatabaseManager());
    }

    @AfterAll
    void tearDown() {
        sqliteDb.close();
    }

    @Test
    void testGetAllMedici() {
        Medico[] medici = adminDAO.getAllMedici();
        assertNotNull(medici);
        assertTrue(medici.length >= 1);
    }

    @Test
    void testDeleteUser_Medico() {
        User user = adminDAO.getUser("drrossi");
        assertNotNull(user);
        assertTrue(user.isMedico());

        adminDAO.deleteUser("drrossi");

        Boolean deleted = sqliteDb.getDatabaseManager().executeQuery(
                "SELECT deleted FROM diabetologo WHERE username = ?",
                rs -> rs.next() && rs.getBoolean("deleted"),
                "drrossi"
        );
        assertTrue(deleted);
    }

    @Test
    void testDeleteUser_Paziente() {
        User user = adminDAO.getUser("mario.rossi");
        assertNotNull(user);
        assertTrue(user.isPaziente());

        adminDAO.deleteUser("mario.rossi");

        Boolean deleted = sqliteDb.getDatabaseManager().executeQuery(
                "SELECT deleted FROM paziente WHERE username = ?",
                rs -> rs.next() && rs.getBoolean("deleted"),
                "mario.rossi"
        );
        assertTrue(deleted);
    }

    @Test
    void testGetLogsPaged() {
        Log[] logs = adminDAO.getLogsPaged(0, 5);
        assertNotNull(logs);
        assertTrue(logs.length > 0);
    }

    @Test
    void testGetAllMedici_FullFields() {
        Medico[] medici = adminDAO.getAllMedici();
        assertNotNull(medici);
        assertTrue(medici.length >= 1);

        Medico medico = medici[0];
        assertNotNull(medico.getUsername());
        assertNotNull(medico.getNome());
        assertNotNull(medico.getCognome());
        assertNotNull(medico.getEmail());
        assertNotNull(medico.getProfileImageName());
    }

}
