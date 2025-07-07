package utils;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.DatabaseManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteTestDatabase {

    private final Connection connection;

    public SQLiteTestDatabase(String schemaSqlPath) {
        try {

            File dbFile = new File(AppConfig.TESTDATABASE_PATH);
            if (dbFile.exists()) dbFile.delete();

            connection = DriverManager.getConnection("jdbc:sqlite:" + AppConfig.TESTDATABASE_PATH);
            loadSchema(schemaSqlPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SQLite test DB", e);
        }
    }

    private void loadSchema(String schemaSqlPath) throws Exception {
        String sql = Files.readString(Path.of(schemaSqlPath));
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("PRAGMA foreign_keys = ON;");
        for (String s : sql.split(";")) {
            String query = s.trim();
            if (!query.isEmpty()) {
                stmt.execute(query + ";");
            }
        }
    }

    public DatabaseManager getDatabaseManager() {
        return new DatabaseManager(AppConfig.TESTDATABASE_PATH);
    }

    public void close() {
        try {
            connection.close();
            // delete test.db after test is finished
            new File(AppConfig.TESTDATABASE_PATH).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


