package satyamconsignment.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import satyamconsignment.common.DatabaseHandler;

public class BuyerRepository {

    public List<String> getAllBuyers() throws SQLException {
        try {
            List<String> buyerList = new ArrayList<>();
            Connection connection = DatabaseHandler.getInstance().getConnection();
            ResultSet buyerResultSet = connection
                    .prepareStatement("select * from Buyer_Master_Table order by name collate nocase")
                    .executeQuery();
            while (buyerResultSet.next()) {
                buyerList.add(buyerResultSet.getString("name"));
            }
            return buyerList;
        } catch (SQLException ex) {
            Logger.getLogger(BuyerRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
