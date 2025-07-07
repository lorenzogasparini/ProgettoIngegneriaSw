package com.progettoingegneriasw.model;

import com.progettoingegneriasw.config.AppConfig;

import java.io.File;
import java.sql.*;

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
        //initializeDatabase(); // commented (not used)
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

    public Integer executeInsertAndReturnId(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            pstmt.executeUpdate();

            // Now retrieve the last inserted ID using a second query
            try (PreparedStatement idStmt = conn.prepareStatement("SELECT last_insert_rowid()");
                 ResultSet rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error executing insert: " + e.getMessage());
        }

        return null;
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