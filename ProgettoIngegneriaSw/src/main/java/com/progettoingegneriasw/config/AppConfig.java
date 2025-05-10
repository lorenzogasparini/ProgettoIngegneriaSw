package com.progettoingegneriasw.config;

import java.io.File;

public class AppConfig {
    // Application settings
    public static final String APP_TITLE = "Gestione diabete";
    
    // Data storage settings
    public static final String DATA_DIR = System.getProperty("user.dir") + "/src/main/resources/com/progettoingegneriasw/data";
    public static final String DATABASE_PATH = DATA_DIR + "/DiabeteDB.db";

//    public static final String DATA_DIR = "src/resources/data";
//    public static final String DATABASE_PATH = DATA_DIR + "/Diabete.db";
//    public static final String DATABASE_POPULATION_SQL = DATA_DIR + "/populateDiabeteDB.sql";
//    public static final String DATABASE_CREATION_SQL = DATA_DIR + "/createDiabeteDB.sql";


    // Create data directory if it doesn't exist
    static {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}