package satyamconsignment.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import satyamconsignment.common.DatabaseHandler;

public class SupplierRepository {

    public List<String> getAllSuppliers() throws SQLException {
        try {
            List<String> supplierList = new ArrayList<>();
            Connection connection = DatabaseHandler.getInstance().getConnection();
            ResultSet supplierResultSet = connection
                    .prepareStatement("select * from Supplier_Master_Table order by name collate nocase")
                    .executeQuery();
            while (supplierResultSet.next()) {
                supplierList.add(supplierResultSet.getString("name"));
            }
            return supplierList;
        } catch (SQLException ex) {
            Logger.getLogger(SupplierRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
