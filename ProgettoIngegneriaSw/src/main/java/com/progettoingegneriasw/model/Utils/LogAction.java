package com.progettoingegneriasw.model.Utils;

public enum LogAction {
    SaveUser, UpdateUser, InsertAlert,                                                                     // UserDAO,
    DeleteUser,                                                                                            // AdminDAO
    SetPatologiaPaziente, AddPatologia, SetTerapiaPaziente, UpdateTerapiaPaziente, DeleteTerapia,          // MedicoDAO
    SetRilevazioneSintomo, SetRilevazioneGlicemia, SetRilevazioneFarmaco;                                  // PazienteDAO

    /**
     *
     * @param logActionString: string corresponding to a value in the enum LogAction
     * @return the corresponding LogAction value which has the same value of the given string
     */
    public static LogAction fromString(String logActionString) {
        for (LogAction logAction: LogAction.values()) {
            if (logAction.name().equalsIgnoreCase(logActionString)) {
                return logAction;
            }
        }
        throw new IllegalArgumentException("Invalid LogAction: " + logActionString);
    }
}
