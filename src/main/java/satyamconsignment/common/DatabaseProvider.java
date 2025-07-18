package satyamconsignment.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseProvider {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private final Connection connection;

    public DatabaseProvider() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to SQLite", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}