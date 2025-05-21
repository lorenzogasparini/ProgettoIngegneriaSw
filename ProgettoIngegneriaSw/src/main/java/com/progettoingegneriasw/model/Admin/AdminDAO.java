package com.progettoingegneriasw.model.Admin;

import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.Log;

import java.sql.Date;
import java.util.ArrayList;

public class AdminDAO extends UserDAO {

    private final String SQLTableName = "amministratore";
    private static AdminDAO instance;

    private AdminDAO(){
        // Init DB connection, maybe from a config class
        super();
    }

    public static synchronized AdminDAO getInstance() {
        if (instance == null) {
            instance = new AdminDAO();
        }
        return instance;
    }

    @Override
    public String getSQLTableName(){
        return SQLTableName;
    }

    // todo: aggiungi i metodi per l'Admin (non vanno messi nell'interfaccia perchè non esistono intefaccie per i DAO)

    /*
    /**
     * Delete a user from the DB (just the Admin is allowed to do peform this operation)
     */
    public void deleteUser(String username) { // todo: da testare

        User user;

        if(userExists(username)){
            user = getUser(username);
        }else{
            return;
        }


        if (user != null && !user.isAdmin()) {
            try {
                if (user.isMedico()) {
                    MedicoDAO medicoDAO = MedicoDAO.getInstance();
                    super.getConnection().executeUpdate(
                            "DELETE FROM " + medicoDAO.getSQLTableName() + " WHERE username = ?",
                            username
                    );
                } else if (user.isPaziente()) {
                    PazienteDAO pazienteDAO = PazienteDAO.getInstance();
                    super.getConnection().executeUpdate(
                            "DELETE FROM " + pazienteDAO.getSQLTableName() + " WHERE username = ?",
                            username
                    );
                }
            } catch (Exception e) {
                System.err.println("Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Log[] getLogs(){
        ArrayList<Log> logs = new ArrayList<>();
        super.getConnection().executeQuery(
                "SELECT * FROM log",
                rs -> {
                    while (rs.next()) { // todo: capire... perché è stata fatta una mappa e non una lista di PazientiUser? (perché l'id è tenuto separato)?
                        logs.add(new Log(
                                        rs.getInt("id"),
                                        rs.getInt("id_paziente"),
                                        rs.getInt("id_diabetologo"),
                                        rs.getString("azione"),
                                        rs.getTimestamp("timestamp")
                                )
                        );
                    }
                    return null;
                }
        );
        return logs.toArray(new Log[logs.size()]);
    }
}
