package satyamconsignment.ui.Input.PaymentEntry.ViewAndDeletePayment;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.ui.Input.CollectionEntry.CollectionEntryController;
import satyamconsignment.ui.Input.PaymentEntry.PaymentEntryController;
import satyamconsignment.model.PaymentItem;

public class ViewAndDeletePayment implements Initializable {

    @FXML
    private TextField voucher_no_field;
    @FXML
    private TableColumn<?, ?> bill_no_col;
    @FXML
    private TableColumn<?, ?> bill_amt_col;
    @FXML
    private TableColumn<?, ?> bill_date_col;
    @FXML
    private TableColumn<?, ?> buyer_col;
    @FXML
    private TableColumn<?, ?> amount_paid_col;
    @FXML
    private TableColumn<?, ?> bank_col;
    @FXML
    private TableColumn<?, ?> dd_no_col;
    @FXML
    private TableColumn<?, ?> dd_date_col;
    @FXML
    private Label display_board_label;
    @FXML
    private TextField total_amount_field;
    @FXML
    private Button get_details_btn;
    @FXML
    private Button delete_entry_btn;
    @FXML
    private TextField voucher_date;
    @FXML
    private TextField supplier_name;
    @FXML
    private TableColumn<?, ?> due_col;
    @FXML
    private TableView<PaymentItem> payment_tableview;
    @FXML
    private Button print_payment_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bill_no_col.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory<>("billAmount"));
        buyer_col.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
        bill_date_col.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        due_col.setCellValueFactory(new PropertyValueFactory<>("due"));
        amount_paid_col.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory<>("ddNo"));
        bank_col.setCellValueFactory(new PropertyValueFactory<>("bank"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory<>("ddDate"));
    }

    @FXML
    private void getDetails(ActionEvent event) {

        if (voucher_no_field.getText().isEmpty()) {
            Utils.showAlert("Voucher No. Field is kept empty. Please fill the voucher no.");
            return;
        }
        try {
            String sql = "select * from `Payment_Entry_Table` where `Voucher No.`=? collate nocase";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field.getText());
            ResultSet rs = ps.executeQuery();

            voucher_date.setText(rs.getString("Voucher Date"));
            supplier_name.setText(rs.getString("Supplier Name"));
            total_amount_field.setText(rs.getString("Total Amount"));
            display_board_label.setText(rs.getString("Supplier Name"));

            List<PaymentItem> list = new ArrayList<>();
            sql = "select * from `Payment_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
            ps = conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field.getText());
            rs = ps.executeQuery();
            if (rs.isClosed()) {
                Utils.showAlert("No Results found", 1);
            } else {
                while (rs.next()) {
                    PaymentItem paymentItem = PaymentItem.builder().billNo(rs.getString("Bill No."))
                            .billAmount(rs.getString("Bill Amount"))
                            .billDate(rs.getString("Bill Date"))
                            .buyerName(rs.getString("Buyer Name")).due(rs.getString("due amount"))
                            .amountPaid(rs.getString("amount paid")).bank(rs.getString("bank"))
                            .ddNo(rs.getString("DD No.")).ddDate(rs.getString("DD Date")).build();

                    list.add(paymentItem);
                }
                payment_tableview.setItems(FXCollections.observableArrayList(list));
                delete_entry_btn.setDisable(false);
            }
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure that you want delete " + voucher_no_field.getText() + " ?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() != ButtonType.YES) {
            return;
        }

        Connection conn = DatabaseHandler.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);
            String sql = "SELECT `Bill No.`,`Amount Paid` FROM `Payment_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field.getText());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sql = "UPDATE `Bill_Entry_Table` SET `Due`=`Due`+? WHERE `BILL NO.`=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, rs.getString("Amount Paid"));
                ps.setString(2, rs.getString("Bill No."));
                ps.execute();
            }
            sql = "DELETE FROM `Payment_Entry_Table` where `Voucher No.`=? collate nocase";
            ps = conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field.getText());
            ps.execute();

            sql = "DELETE FROM `Payment_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
            ps = conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field.getText());
            ps.execute();

            payment_tableview.setItems(FXCollections.observableArrayList());

            voucher_date.setText("");
            supplier_name.setText("");
            display_board_label.setText("");
            total_amount_field.setText("");
            conn.commit();
            Utils.showAlert(
                    voucher_no_field.getText().toUpperCase() + " Entry was successfully deleted.",
                    1);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Utils.showAlert(ex1.toString());
                Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE,
                        ex1.toString(), ex1);
            }
            Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
        }

    }

    @FXML
    private void printPayment(ActionEvent event) {
        try {
            Connection conn = DatabaseHandler.getInstance().getConnection();
            String jrxmlFileName = "Payment.jrxml";
            JasperReport jasperReport = JasperCompileManager
                    .compileReport(getClass().getResourceAsStream(jrxmlFileName));
            Map<String, Object> map = new HashMap<>();
            map.put("voucherNo", voucher_no_field.getText());
            map.put("voucherDate", voucher_date.getText());
            map.put("supplierName", supplier_name.getText());
            map.put("billAmount", total_amount_field.getText());
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jprint, Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
        }
    }
}
