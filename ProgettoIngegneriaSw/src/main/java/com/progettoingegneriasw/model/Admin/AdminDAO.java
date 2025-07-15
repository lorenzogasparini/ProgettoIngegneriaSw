package com.progettoingegneriasw.model.Admin;

import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.Log;
import com.progettoingegneriasw.model.Utils.LogAction;
import com.progettoingegneriasw.view.ViewNavigator;

import java.sql.Date;
import java.util.ArrayList;

public class AdminDAO extends UserDAO implements AdminDAOInterface{

    private final String SQLTableName = "amministratore";
    private static AdminDAO instance = null;

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

    /*
    /**
     * Delete a user from the DB (just the Admin is allowed to do perform this operation)
     */
    public void deleteUser(String username) {

        User user;

        if(userExists(username)){
            user = getUser(username);
        }else{
            return;
        }


        if (user != null && !user.isAdmin()) {
            try {
                if (user.isMedico()) {
                    super.getConnection().executeUpdate(
                            "UPDATE diabetologo SET deleted = ? WHERE username = ?",
                            true,
                            username
                    );
                    setLog(new Log(null, UserDAO.getInstance().getIdFromDB(username), LogAction.DeleteUser, null));
                } else if (user.isPaziente()) {
                    super.getConnection().executeUpdate(
                            "UPDATE paziente SET deleted = ? WHERE username = ?",
                            true,
                            username
                    );
                    setLog(new Log(UserDAO.getInstance().getIdFromDB(username), null, LogAction.DeleteUser, null));
                }
            } catch (Exception e) {
                System.err.println("Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Log[] getLogsPaged(int offset, int limit) {
        ArrayList<Log> logs = new ArrayList<>();
        super.getConnection().executeQuery(
                "SELECT * FROM log ORDER BY timestamp DESC LIMIT ? OFFSET ?",
                rs -> {
                    while (rs.next()) {
                        logs.add(new Log(
                                rs.getInt("id"),
                                rs.getInt("id_paziente"),
                                rs.getInt("id_diabetologo"),
                                LogAction.fromString(rs.getString("azione")),
                                rs.getTimestamp("timestamp")
                        ));
                    }
                    return null;
                },
                limit, offset
        );
        return logs.toArray(new Log[0]);
    }

    public Medico[] getAllMedici(){
        ArrayList<User> medici = new ArrayList<>();

        super.getConnection().executeQuery(
                "SELECT * FROM diabetologo",
                rs -> {
                    while (rs.next()) {
                        medici.add(new MedicoUser(
                                        rs.getInt("id"),
                                        rs.getString("username"),
                                        rs.getString("password"),
                                        rs.getString("nome"),
                                        rs.getString("cognome"),
                                        rs.getString("email"),
                                        rs.getString("profile_image_name")
                                )
                        );
                    }
                    return null;
                }
        );
        return medici.toArray(new Medico[medici.size()]);
    }

}
