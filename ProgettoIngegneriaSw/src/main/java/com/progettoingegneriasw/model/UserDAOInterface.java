package com.progettoingegneriasw.model;

import com.progettoingegneriasw.model.Utils.Alert;
import com.progettoingegneriasw.model.Utils.Farmaco;
import com.progettoingegneriasw.model.Utils.Log;

import java.sql.SQLException;

public interface UserDAOInterface {
    public void saveUser(User user);
    public User getUser(String username);
    public User getUser(Integer idUtente, UserType userType);
    public boolean userExists(String username);
    public boolean isUserDeleted(String username);
    public int getIdFromDB(String username);
    public void setLog(Log log);
    public User[] getAllUsers();
    public User[] getAllUsers(UserType userType);
    public Farmaco getFarmacoFromId(Integer idFarmaco);
    public Farmaco getFarmacoFromAic(String codiceAic);
    public void contattaDiabetologo(String destinatario, String oggetto, String corpo);
    public int countAlerts() throws SQLException;
    public void automaticUpdateAlertTable() throws SQLException;
    public Alert[] getAlertFarmaciNonAssuntiDaAlmeno3GiorniENonSegnalati();
    public void insertAlerts(Alert[] alerts);
    public void setAlertRead(Alert alert, boolean read);
}
