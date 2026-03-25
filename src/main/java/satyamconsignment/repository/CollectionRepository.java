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

public class CollectionRepository {

    public List<String> fetchPendingBillsForBuyer(String buyerName) throws SQLException {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            // language=sql
            String sql =
                    """
                        with cte_collected as (
                        select
                            `Bill No.`,
                            sum(`Amount Collected`) as amount_collected
                        from
                            Collection_Entry_Extended_Table
                        group by
                            `Bill No.`
                        ),
                        cte_bill as (
                        select
                            *
                        from
                            Bill_Entry_Table
                        WHERE
                            `Buyer Name` = ?
                        ),
                        cte_bill_collected as (
                        select
                            `Bill No.`,
                            `Bill Amount`,
                            coalesce(`amount_collected`, 0) as amount_collected
                        from
                            cte_bill
                        left outer join cte_collected
                                using(`Bill No.`)
                        ),
                        cte_pending as (
                        select
                            `Bill No.`,
                            `Bill Amount` - amount_collected as pending_amount
                        from
                            cte_bill_collected
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
            preparedStatement.setString(1, buyerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> res = new ArrayList<>();
            while (resultSet.next()) {
                res.add(resultSet.getString("Bill No."));
            }
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(CollectionRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }

    public int fetchPendingAmountForBillNo(String billNo) throws SQLException {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            // language=sql
            String sql =
                    """
                        with cte_collected as (
                        select
                            `Bill No.`,
                            sum(`Amount Collected`) as amount_collected
                        from
                            Collection_Entry_Extended_Table
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
                        cte_bill_collected as (
                        select
                            `Bill No.`,
                            `Bill Amount`,
                            coalesce(`amount_collected`, 0) as amount_collected
                        from
                            cte_bill
                        left outer join cte_collected
                                using(`Bill No.`)
                        ),
                        cte_pending as (
                        select
                            `Bill No.`,
                            `Bill Amount` - amount_collected as pending_amount
                        from
                            cte_bill_collected
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
            Logger.getLogger(CollectionRepository.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
