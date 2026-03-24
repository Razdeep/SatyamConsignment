package satyamconsignment.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import satyamconsignment.common.DatabaseHandler;

public class TransportRepository {

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
