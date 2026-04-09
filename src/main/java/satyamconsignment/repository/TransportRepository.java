package satyamconsignment.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import satyamconsignment.common.DatabaseHandler;

public class TransportRepository {

    public void saveTransport(String tranportName) throws SQLException {
        try {
            String sql = "INSERT INTO `Transport_Master_Table`(`Name`) VALUES (?);";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tranportName);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(TransportRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }

    public void deleteTransport(String transportName) throws SQLException {
        try {
            String sql = "DELETE FROM `Transport_Master_Table` WHERE name=?";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, transportName);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(TransportRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }

    public void renameTransport(String oldName, String newName) throws SQLException {
        try {
            String sql = "UPDATE `Transport_Master_Table` SET Name=? WHERE Name=?";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newName);
            ps.setString(2, oldName);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(TransportRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }

    public List<String> getAllTransports() throws SQLException {
        try {
            List<String> transportList = new ArrayList<>();
            Connection connection = DatabaseHandler.getInstance().getConnection();
            ResultSet transportResultSet = connection
                    .prepareStatement("select * from Transport_Master_Table order by name collate nocase")
                    .executeQuery();
            while (transportResultSet.next()) {
                transportList.add(transportResultSet.getString("name"));
            }
            return transportList;
        } catch (SQLException ex) {
            Logger.getLogger(TransportRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
