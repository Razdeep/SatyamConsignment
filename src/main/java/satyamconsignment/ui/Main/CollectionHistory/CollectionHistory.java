package satyamconsignment.ui.Main.CollectionHistory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;

public class CollectionHistory implements Initializable {

    @FXML
    private Group root;
    @FXML
    private TableView<Record> tableView;
    @FXML
    private TableColumn<Record, String> voucher_no_col;
    @FXML
    private TableColumn<Record, String> voucher_date_col;
    @FXML
    private TableColumn<Record, String> buyer_name_col;
    @FXML
    private TableColumn<Record, String> total_amount_col;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        ObservableList<Record> list = FXCollections.observableArrayList();
        voucher_no_col.setCellValueFactory(new PropertyValueFactory<>("voucherNo"));
        voucher_date_col.setCellValueFactory(new PropertyValueFactory<>("voucherDate"));
        buyer_name_col.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
        total_amount_col.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        try {
            String sql = "SELECT * FROM `Collection_Entry_Table`;";
            Connection conn = databaseHandler.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Record(rs.getString("Voucher No."), rs.getString("Voucher Date"),
                        rs.getString("Buyer Name"), rs.getString("Total Amount")));
            }
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(CollectionHistory.class.getName()).log(Level.SEVERE, ex.toString(),
                    ex);
        }
        tableView.setItems(list);
    }

    @AllArgsConstructor
    @Getter
    public static class Record {
        private final String voucherNo;
        private final String voucherDate;
        private final String buyerName;
        private final String totalAmount;
    }
}
