package com.progettoingegneriasw.model.Utils;

public enum AlertType {
    farmaco,
    glicemia,
    sintomo;

    public static AlertType fromString(String value) {
        for (AlertType t : AlertType.values()) {
            if (t.name().equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid AlertType: " + value);
    }
}
