
package satyamconsignment.misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class DatabaseHandler {
    
    static DatabaseHandler databaseHandler=null;
    static Connection conn=null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    
    public static DatabaseHandler getInstance()
    {
        if(databaseHandler==null)
        {
            databaseHandler=new DatabaseHandler();
        }
        return databaseHandler;
    }
    
    private DatabaseHandler()
    {
        Rrc rrc=new Rrc();
        try {
            
            Class.forName("org.sqlite.JDBC");
            conn=DriverManager.getConnection("jdbc:sqlite:database.db");
            
        } catch (ClassNotFoundException | SQLException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public Connection getConnection()
    {
        return conn;
    }
    
            
    
}
