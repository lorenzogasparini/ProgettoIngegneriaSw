package com.progettoingegneriasw.model.Admin;

import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Utils.Log;

public interface AdminDAOInterface {
    public void deleteUser(String username);
    public Log[] getLogsPaged(int offset, int limit);
    public Medico[] getAllMedici();
}
