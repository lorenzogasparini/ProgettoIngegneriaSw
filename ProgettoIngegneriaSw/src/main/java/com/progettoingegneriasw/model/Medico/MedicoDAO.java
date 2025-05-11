package com.progettoingegneriasw.model.Medico;

import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

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

    // todo: aggiungere i metodi del medico (non vanno messi nell'interfaccia perchè non esistono interfaccie per i DAO)

    /**
     * Function that returns all the users observed by the intance of MedicoUser
     */
    public Map<Integer, User> getPazientiFromDB(String username) throws SQLException {
        Map<Integer, User> pazienti = new TreeMap<>();
        int id_diabetologo = getIdFromDB(username);
        if(id_diabetologo == -1) {
            throw new SQLException();   //  Verificare se si tratta della giusta eccezione
        }

        super.getConnection().executeQuery(
                "SELECT * FROM paziente p WHERE p.id_diabetologo = ?",
                rs -> {
                    while (rs.next()) {
                        pazienti.put(rs.getInt("id"),new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("nome"), rs.getString("cognome")));
                    }
                    return null;
                },
                id_diabetologo
        );

        return pazienti;
    }

    /**
     *
     * @param username The given username of the MedicoDAO instance
     * @return The id of the MedicoDAO instance, taken by DB query
     */
    public int getIdFromDB(String username) {   //  Funzionamento corretto
        return super.getConnection().executeQuery(
                "SELECT id FROM diabetologo d WHERE d.username = ?",
                rs -> {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                    return -1;
                },
                username
        );
    }


}
