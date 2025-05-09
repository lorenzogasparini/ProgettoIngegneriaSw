package com.progettoingegneriasw.model.Paziente;

import com.progettoingegneriasw.model.UserDAO;

/**
 * Paziente Model is the DAO (Data Access Object) class
 */
public class PazienteDAO extends UserDAO {

    private final String SQLTableName = "paziente";
    private static PazienteDAO instance;

    private PazienteDAO() {
        // Init DB connection, maybe from a config class

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

    // todo: aggiungere i metodi per i pazienti... (metti prima nell'interfaccia)

    public int getMedico(Paziente paziente){ // note: methods don't have to be static
        // todo: get via SQL id_diabetologo del paziente passato
        return 0; // todo: completare
    }




}
