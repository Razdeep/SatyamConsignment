package satyamconsignment.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.entity.PaymentEntity;
import satyamconsignment.entity.PaymentItemEntity;

public class PaymentRepository {

    public void savePayment(PaymentEntity paymentEntity) throws SQLException {
        Connection connection = DatabaseHandler.getInstance().getConnection();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
        try {
            connection.setAutoCommit(false);
            String sql =
                    "INSERT INTO `Payment_Entry_Table`(`Voucher No.`,`Voucher Date`,`Supplier Name`,`Total Amount`) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, paymentEntity.getVoucherNo());
            preparedStatement.setString(2, paymentEntity.getVoucherDate());
            preparedStatement.setString(3, paymentEntity.getSupplierName());
            preparedStatement.setString(4, paymentEntity.getTotalAmount());
            preparedStatement.execute();

            sql =
                    "INSERT INTO `Payment_Entry_Extended_Table`(`Voucher No.`,`Buyer Name`,`Bill No.`,`Bill Date`,`Bill Amount`,`Due Amount`,`Amount Paid`,`Bank`,`DD No.`,`DD Date`) VALUES (?,?,?,?,?,?,?,?,?,?)";

            if (null != paymentEntity.getItems()) {
                for (PaymentItemEntity paymentItem : paymentEntity.getItems()) {

                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, paymentEntity.getVoucherNo());
                    preparedStatement.setString(2, paymentItem.getBuyerName());
                    preparedStatement.setString(3, paymentItem.getBillNo());
                    preparedStatement.setString(4, paymentItem.getBillDate());
                    preparedStatement.setString(5, paymentItem.getBillAmount());
                    preparedStatement.setString(6, paymentItem.getDueAmount());
                    preparedStatement.setString(7, paymentItem.getAmountPaid());
                    preparedStatement.setString(8, paymentItem.getBank());
                    preparedStatement.setString(9, paymentItem.getDdNo());
                    preparedStatement.setString(10, paymentItem.getDdDate());

                    preparedStatement.execute();
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            Logger.getLogger(PaymentRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
