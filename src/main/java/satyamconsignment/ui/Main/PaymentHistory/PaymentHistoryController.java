/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satyamconsignment.ui.Main.PaymentHistory;

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

public class PaymentHistoryController implements Initializable {

	String sql;
	Utils utils;
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
	private TableColumn<Record, String> supplier_name_col;
	@FXML
	private TableColumn<Record, String> total_amount_col;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		utils = new Utils();
		databaseHandler = DatabaseHandler.getInstance();
		list = FXCollections.observableArrayList();
		voucher_no_col.setCellValueFactory(new PropertyValueFactory<>("voucherNo"));
		voucher_date_col.setCellValueFactory(new PropertyValueFactory<>("voucherDate"));
		supplier_name_col.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
		total_amount_col.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
		try {
			sql = "SELECT * FROM `Payment_Entry_Table`;";
			conn = databaseHandler.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Record(rs.getString("Voucher No."), rs.getString("Voucher Date"),
						rs.getString("Supplier Name"), rs.getString("Total Amount")));
			}
		} catch (SQLException ex) {
			Utils.showAlert(ex.toString());
			Logger.getLogger(PaymentHistoryController.class.getName()).log(Level.SEVERE, null, ex);
		}
		tableView.setItems(list);
	}

	public static class Record {
		private final String voucherNo;
		private final String voucherDate;
		private final String supplierName;
		private final String totalAmount;

		public Record(String voucherNo, String voucherDate, String supplierName, String totalAmount) {
			this.voucherNo = voucherNo;
			this.voucherDate = voucherDate;
			this.supplierName = supplierName;
			this.totalAmount = totalAmount;
		}

		public String getVoucherNo() {
			return voucherNo;
		}

		public String getVoucherDate() {
			return voucherDate;
		}

		public String getSupplierName() {
			return supplierName;
		}

		public String getTotalAmount() {
			return totalAmount;
		}
	}
}
