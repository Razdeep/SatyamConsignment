package satyamconsignment.ui.Main.InputHistory;

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
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.model.BillRecord;

public class InputHistory implements Initializable {

    String sql;
    Utils utils;
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ObservableList<BillRecord> list;
    @FXML
    private Group root2;
    @FXML
    private TableColumn<BillRecord, String> supplier_name_col;
    @FXML
    private TableColumn<BillRecord, String> buyer_name_col;
    @FXML
    private TableColumn<BillRecord, String> bill_no_col;
    @FXML
    private TableColumn<BillRecord, String> bill_date_col;
    @FXML
    private TableColumn<BillRecord, String> transport_col;
    @FXML
    private TableColumn<BillRecord, String> lr_date_col;
    @FXML
    private TableColumn<BillRecord, String> bill_amount_col;
    @FXML
    private TableView<BillRecord> tableView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        utils = new Utils();
        databaseHandler = DatabaseHandler.getInstance();
        list = FXCollections.observableArrayList();
        supplier_name_col.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        buyer_name_col.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
        bill_no_col.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        bill_date_col.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        transport_col.setCellValueFactory(new PropertyValueFactory<>("transport"));
        lr_date_col.setCellValueFactory(new PropertyValueFactory<>("lrDate"));
        bill_amount_col.setCellValueFactory(new PropertyValueFactory<>("billAmount"));

        try {
            sql = "SELECT * FROM `Bill_Entry_Table`;";
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new BillRecord(rs.getString("Supplier Name"), rs.getString("Buyer Name"),
                        rs.getString("Bill No."), rs.getString("Bill Date"),
                        rs.getString("Transport"), rs.getString("LR Date"),
                        rs.getString("Bill Amount")));
            }
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(InputHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
    }
}
