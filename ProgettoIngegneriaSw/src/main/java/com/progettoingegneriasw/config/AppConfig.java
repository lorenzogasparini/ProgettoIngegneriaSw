package com.progettoingegneriasw.config;

import java.io.File;

public class AppConfig {
    // Application settings
    public static final String APP_TITLE = "Gestione diabete";
    
    // Data storage settings
    public static final String DATA_DIR = System.getProperty("user.dir") + "/src/main/resources/com/progettoingegneriasw/data/";
    public static final String DATABASE_PATH = DATA_DIR + "DiabeteDB.db";
    public static final String TESTDATA_DIR = System.getProperty("user.dir") + "/src/test/resources/data/";
    public static final String TESTSCHEMA_PATH = TESTDATA_DIR + "schema.sql";
    public static final String TESTDATABASE_PATH = TESTDATA_DIR + "test.db";
    public static final String IMAGE_DIR = System.getProperty("user.dir") + "/src/main/resources/images/";
    public static final String ICON_DIR = System.getProperty("user.dir") + "/src/main/resources/icons/";
    public static final String DEFAULT_IMAGE = "default.png";
    public static final String APPICON_IMAGE = "/icons/app_icon.png";

//    public static final String DATA_DIR = "src/resources/data";
//    public static final String DATABASE_PATH = DATA_DIR + "/Diabete.db";
//    public static final String DATABASE_POPULATION_SQL = DATA_DIR + "/populateDiabeteDB.sql";
//    public static final String DATABASE_CREATION_SQL = DATA_DIR + "/createDiabeteDB_old.sql";


    // Create data directory if it doesn't exist
    static {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}