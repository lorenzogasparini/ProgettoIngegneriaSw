package com.progettoIngegneriaSw.model.Admin.AdminDAOTest;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.User;
import org.junit.jupiter.api.*;
import utils.SQLiteTestDatabase;

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
    }

    @AfterAll
    void tearDown() {
        sqliteDb.close();
    }

    @Test
    void testUserExists() {
        assertTrue(pazienteDAO.userExists("mario.rossi"));
        assertFalse(pazienteDAO.userExists("unknown.user"));
    }

    @Test
    void testGetUser() {
        User user = pazienteDAO.getUser("mario.rossi");
        assertNotNull(user);
        assertTrue(user instanceof Paziente);
        assertEquals("mario.rossi", user.getUsername());
    }

    @Test
    void testGetMedicoRiferimento() {
        User medico = pazienteDAO.getMedicoRiferimento("mario.rossi");
        assertNotNull(medico);
        assertEquals("drrossi", medico.getUsername());
    }
}

