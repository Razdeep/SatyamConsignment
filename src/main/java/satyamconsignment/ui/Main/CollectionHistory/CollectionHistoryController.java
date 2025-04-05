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
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;

public class CollectionHistoryController implements Initializable {

	String sql;
	Rrc rrc;
	DatabaseHandler databaseHandler;
	Connection conn;
	PreparedStatement ps;
	ResultSet rs;
	ObservableList<Record> list;
	@FXML
	private Group root2;
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
		rrc = new Rrc();
		databaseHandler = DatabaseHandler.getInstance();
		list = FXCollections.observableArrayList();
		voucher_no_col.setCellValueFactory(new PropertyValueFactory<>("voucherNo"));
		voucher_date_col.setCellValueFactory(new PropertyValueFactory<>("voucherDate"));
		buyer_name_col.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
		total_amount_col.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
		try {
			sql = "SELECT * FROM `Collection_Entry_Table`;";
			conn = databaseHandler.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Record(rs.getString("Voucher No."), rs.getString("Voucher Date"),
						rs.getString("Buyer Name"), rs.getString("Total Amount")));
			}
		} catch (SQLException ex) {
			Rrc.showAlert(ex.toString());
			Logger.getLogger(CollectionHistoryController.class.getName()).log(Level.SEVERE, null, ex);
		}
		tableView.setItems(list);
	}

	public static class Record {
		private final String voucherNo;
		private final String voucherDate;
		private final String buyerName;
		private final String totalAmount;

		public Record(String voucherNo, String voucherDate, String buyerName, String totalAmount) {
			this.voucherNo = voucherNo;
			this.voucherDate = voucherDate;
			this.buyerName = buyerName;
			this.totalAmount = totalAmount;
		}

		public String getVoucherNo() {
			return voucherNo;
		}

		public String getVoucherDate() {
			return voucherDate;
		}

		public String getBuyerName() {
			return buyerName;
		}

		public String getTotalAmount() {
			return totalAmount;
		}
	}
}
