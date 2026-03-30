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
import satyamconsignment.entity.PaymentEntity;
import satyamconsignment.entity.PaymentItemEntity;

public class PaymentRepository {

    public void savePayment(PaymentEntity paymentEntity) throws SQLException {
        Connection connection = DatabaseHandler.getInstance().getConnection();
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

    public List<String> fetchPendingBillsForSupplier(String supplierName) throws SQLException {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            // language=sql
            String sql =
                    """
                        with cte_paid as (
                        select
                            `Bill No.`,
                            sum(`Amount Paid`) as amount_paid
                        from
                            Payment_Entry_Extended_Table
                        group by
                            `Bill No.`
                        ),
                        cte_bill as (
                        select
                            *
                        from
                            Bill_Entry_Table
                        WHERE
                            `Supplier Name` = ?
                        ),
                        cte_bill_paid as (
                        select
                            `Bill No.`,
                            `Bill Amount`,
                            coalesce(`amount_paid`, 0) as amount_paid
                        from
                            cte_bill
                        left join cte_paid
                            using(`Bill No.`)
                        ),
                        cte_pending as (
                        select
                            `Bill No.`,
                            `Bill Amount` - amount_paid as pending_amount
                        from
                            cte_bill_paid
                        ),
                        cte_final as (
                        select
                            `Bill No.`
                        from
                            cte_pending
                        where
                            pending_amount > 0
                        )
                        select
                            *
                        from
                            cte_final;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, supplierName);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> res = new ArrayList<>();
            while (resultSet.next()) {
                res.add(resultSet.getString("Bill No."));
            }
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(PaymentRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }

    public int fetchPendingAmountForBillNo(String billNo) throws SQLException {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            // language=sql
            String sql =
                    """
                        with cte_paid as (
                        select
                            `Bill No.`,
                            sum(`Amount Paid`) as amount_paid
                        from
                            Payment_Entry_Extended_Table
                        group by
                            `Bill No.`
                        ),
                        cte_bill as (
                        select
                            *
                        from
                            Bill_Entry_Table
                        WHERE
                            `Bill No.` = ?
                        ),
                        cte_bill_paid as (
                        select
                            `Bill No.`,
                            `Bill Amount`,
                            coalesce(`amount_paid`, 0) as amount_paid
                        from
                            cte_bill
                        left join cte_paid
                            using(`Bill No.`)
                        ),
                        cte_pending as (
                        select
                            `Bill No.`,
                            `Bill Amount` - amount_paid as pending_amount
                        from
                            cte_bill_paid
                        ),
                        cte_final as (
                        select
                            pending_amount
                        from
                            cte_pending
                        )
                        select
                            *
                        from
                            cte_final;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, billNo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isClosed()) {
                return 0;
            }

            return resultSet.getInt("pending_amount");
        } catch (SQLException ex) {
            Logger.getLogger(PaymentRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
