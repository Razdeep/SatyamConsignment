package satyamconsignment.misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class DatabaseHandler {

    private static DatabaseHandler databaseHandler = null;
    private Connection conn = null;

    private DatabaseHandler() {
        Rrc rrc = new Rrc();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException | SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static DatabaseHandler getInstance() {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler();
        }
        return databaseHandler;
    }

    public Connection getConnection() {
        return conn;
    }

}
