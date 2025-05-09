package com.progettoingegneriasw.model;

import com.progettoingegneriasw.config.AppConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final String dbUrl;
    
    public DatabaseManager() {
        this(AppConfig.DATABASE_PATH);
    }
    
    public DatabaseManager(String dbPath) {
        // Create parent directories if they don't exist
        File dbFile = new File(dbPath);
        if (dbFile.getParentFile() != null && !dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }
        
        this.dbUrl = "jdbc:sqlite:" + dbPath;
        initializeDatabase(); // commented (not used)
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create amministratore table
            stmt.executeUpdate("""
            CREATE TABLE amministratore (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                        	nome VARCHAR(50) NOT NULL,
                        	cognome VARCHAR(50) NOT NULL,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL
                        );
        """);

            // Create diabetologo table
            stmt.executeUpdate("""
            id INTEGER PRIMARY KEY AUTOINCREMENT,
                        	nome VARCHAR(50) NOT NULL,
                        	cognome VARCHAR(50) NOT NULL,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            email VARCHAR(100) NOT NULL
        """);

            // Create paziente table
            stmt.executeUpdate("""
            CREATE TABLE paziente (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                        	nome VARCHAR(50) NOT NULL,
                        	cognome VARCHAR(50) NOT NULL,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            email VARCHAR(100),
                            id_diabetologo INTEGER NOT NULL,
                            data_nascita DATE,
                            peso REAL,
                            provincia_residenza VARCHAR(2),
                            comune_residenza VARCHAR(100),
                            note_paziente TEXT,
                            FOREIGN KEY(id_diabetologo) REFERENCES diabetologo(id)
                        );
        """);

            System.out.println("Database tables initialized.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }


    /**
     * Funzione che permette di eseguire query SQL direttamente da file fornito in input
     * @param path : parametro "percorso" in cui si trova il file SQL da eseguire
     */
    public void executeFile(String path) {
        try (FileReader reader = new FileReader(path);
             // Wrap the FileReader in a BufferedReader for efficient reading.
             BufferedReader bufferedReader = new BufferedReader(reader);

             // Stabilisce una connessione con il database, mediante funzione getConnection() per sqlite definita di seguito
             Connection connection = getConnection();

             // Crea un oggetto statement per eseguire l'oggetto SQL.
             Statement statement = connection.createStatement();) {

            StringBuilder builder = new StringBuilder();

            String line;
            int lineNumber = 0;
            int count = 0;

            // Leggiamo tutte le righe del file SQL fornito in input
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber += 1;
                line = line.trim();

                // Salta righe vuote e -
                if (line.isEmpty() || line.startsWith("--"))
                    continue;

                builder.append(line);

                // Se la righa termina con una ; allora ciò indica di terminare il comando SQL che si sta scorrendo
                if (line.endsWith(";"))
                    try {
                        // Esegui il comando SQL
                        statement.execute(builder.toString());
                        // Print a success message along with the first 15 characters of the executed command.
                        System.out.println(++count + " Command successfully executed : " + builder.substring(0, Math.min(builder.length(), 15)) + "...");
                        builder.setLength(0);
                    }
                    catch (SQLException e) {
                        System.err.println("At line " + lineNumber + " : " + e.getMessage() + "\n");
                        return;
                    }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get a database connection
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
    
    /**
     * Execute an update query (INSERT, UPDATE, DELETE)
     */
    public boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            // Execute the query
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Execute a query and process the results with a ResultSetProcessor
     */
    public <T> T executeQuery(String sql, ResultSetProcessor<T> processor, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            // Execute the query and process results
            try (ResultSet rs = pstmt.executeQuery()) {
                return processor.process(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Interface for processing a ResultSet into a specific type
     */
    public interface ResultSetProcessor<T> {
        T process(ResultSet rs) throws SQLException;
    }
}