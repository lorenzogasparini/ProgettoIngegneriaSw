package com.progettoingegneriasw.model.Utils;

public enum AlertType {
    farmaco,
    promemoriaFarmaco,
    farmacoNonAssuntoDa3Giorni,
    glicemia;

    public static AlertType fromString(String value) {
        for (AlertType t : AlertType.values()) {
            if (t.name().equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid AlertType: " + value);
    }
}
