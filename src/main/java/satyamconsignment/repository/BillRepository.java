package satyamconsignment.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.entity.BillEntity;
import satyamconsignment.entity.LREntity;

public class BillRepository {

    public void save(BillEntity billEntity) throws SQLException {
        Connection connection = DatabaseHandler.getInstance().getConnection();
        try {
            /* Code for saving data into Bill_Entry_Table */

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            String sql =
                    "INSERT INTO `Bill_Entry_Table`(`Supplier Name`,`Buyer Name`,`Bill No.`,`Bill Date`,`Transport`,`LR Date`,`Bill Amount`,`Collection Due`,`Due`) VALUES (?,?,?,?,?,?,?,?,?);";

            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, billEntity.getSupplierName());
            preparedStatement.setString(2, billEntity.getBuyerName());
            preparedStatement.setString(3, billEntity.getBillNo());
            //            preparedStatement.setString(4, formatter.format(date_field.getValue()));
            preparedStatement.setString(4, billEntity.getBillDate());
            preparedStatement.setString(5, billEntity.getTransport());
            //            preparedStatement.setString(6, formatter.format(lr_date.getValue()));
            preparedStatement.setString(6, billEntity.getLrDate());
            preparedStatement.setString(7, billEntity.getBillAmount());
            preparedStatement.setString(8, billEntity.getBillAmount());
            preparedStatement.setString(9, billEntity.getBillAmount());
            preparedStatement.execute();

            /* Code for Entering Data into LR_Table */
            if (!billEntity.getLrEntities().isEmpty()) {
                for (LREntity lr : billEntity.getLrEntities()) {
                    sql = "INSERT INTO `LR_Table`(`Bill No.`,`LR No.`,`PM`) VALUES (?,?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, billEntity.getBillNo());
                    preparedStatement.setString(2, lr.getLrNo());
                    preparedStatement.setString(3, lr.getPm());
                    preparedStatement.execute();
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            Logger.getLogger(BillRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
