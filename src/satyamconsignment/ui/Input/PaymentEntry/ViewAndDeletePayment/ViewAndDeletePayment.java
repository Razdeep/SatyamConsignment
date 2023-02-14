package satyamconsignment.ui.Input.PaymentEntry.ViewAndDeletePayment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.*;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;
import satyamconsignment.ui.Input.CollectionEntry.CollectionEntryController;
import satyamconsignment.ui.Input.PaymentEntry.Payment;
import satyamconsignment.ui.Input.PaymentEntry.PaymentEntryController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewAndDeletePayment implements Initializable {
    //My variables
    Rrc rrc;
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    String sql;
    DateTimeFormatter formatter;
    ObservableList<Payment> list;
    ObservableList<String> supplierNameComboList;
    ObservableList<String> billNoComboList;
    int previouslyDue;
    int totalAmountPaid;
    @FXML
    private TextField voucher_no_field_2;
    private TableView<?> collection_tableview_2;
    @FXML
    private TableColumn<?, ?> bill_no_col_2;
    @FXML
    private TableColumn<?, ?> bill_amt_col_2;
    @FXML
    private TableColumn<?, ?> bill_date_col_2;
    @FXML
    private TableColumn<?, ?> buyer_col_2;
    private TableColumn<?, ?> gr_col_2;
    private TableColumn<?, ?> rd_col_2;
    private TableColumn<?, ?> cd_col_2;
    private TableColumn<?, ?> bc_col_2;
    @FXML
    private TableColumn<?, ?> amount_paid_col_2;
    @FXML
    private TableColumn<?, ?> bank_col_2;
    @FXML
    private TableColumn<?, ?> dd_no_col_2;
    @FXML
    private TableColumn<?, ?> dd_date_col_2;
    @FXML
    private Label display_board_label_2;
    @FXML
    private TextField total_amount_field_2;
    @FXML
    private Button get_details_btn;
    @FXML
    private Button delete_entry_btn;
    @FXML
    private TextField voucher_date_2;
    @FXML
    private TextField supplier_name_2;
    @FXML
    private TableColumn<?, ?> due_col_2;
    @FXML
    private TableView<Payment> payment_tableview_2;
    @FXML
    private Group root2;
    @FXML
    private Button print_payment_btn;
    @FXML
    private Text last_voucher_field;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rrc = new Rrc();
        databaseHandler = DatabaseHandler.getInstance();
        conn = databaseHandler.getConnection();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        list = FXCollections.observableArrayList();
        supplierNameComboList = FXCollections.observableArrayList();
        billNoComboList = FXCollections.observableArrayList();


        bill_no_col_2.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col_2.setCellValueFactory(new PropertyValueFactory("billAmount"));
        buyer_col_2.setCellValueFactory(new PropertyValueFactory("buyerName"));
        bill_date_col_2.setCellValueFactory(new PropertyValueFactory("billDate"));
        due_col_2.setCellValueFactory(new PropertyValueFactory("due"));
        amount_paid_col_2.setCellValueFactory(new PropertyValueFactory("amountPaid"));
        dd_no_col_2.setCellValueFactory(new PropertyValueFactory("ddNo"));
        bank_col_2.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_date_col_2.setCellValueFactory(new PropertyValueFactory("ddDate"));
    }

    @FXML
    private void getDetails(ActionEvent event) {
        //Copy Paste Edit

        if (voucher_no_field_2.getText().isEmpty()) {
            Rrc.showAlert("Voucher No. Field is kept empty. Please fill the voucher no.");
        } else {
            try {
                sql = "select * from `Payment_Entry_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs = ps.executeQuery();

                voucher_date_2.setText(rs.getString("Voucher Date"));
                supplier_name_2.setText(rs.getString("Supplier Name"));
                total_amount_field_2.setText(rs.getString("Total Amount"));
                display_board_label_2.setText(rs.getString("Supplier Name"));

                list.clear();
                sql = "select * from `Payment_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs = ps.executeQuery();
                if (rs.isClosed()) {
                    Rrc.showAlert("No Results found", 1);
                } else {
                    while (rs.next()) {
                        Payment payment = Payment.builder()
                                .billNo(rs.getString("Bill No."))
                                .billAmount(rs.getString("Bill Amount"))
                                .billDate(rs.getString("Bill Date"))
                                .buyerName(rs.getString("Buyer Name"))
                                .due(rs.getString("due amount"))
                                .amountPaid(rs.getString("amount paid"))
                                .bank(rs.getString("bank"))
                                .ddNo(rs.getString("DD No."))
                                .ddDate(rs.getString("DD Date"))
                                .build();

                        list.add(payment);
                    }
                    payment_tableview_2.setItems(list);
                    delete_entry_btn.setDisable(false);
                }
            } catch (SQLException ex) {
                Rrc.showAlert(ex.toString());
                Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want delete " + voucher_no_field_2.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                conn.setAutoCommit(false);
                sql = "SELECT `Bill No.`,`Amount Paid` FROM `Payment_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    sql = "UPDATE `Bill_Entry_Table` SET `Due`=`Due`+? WHERE `BILL NO.`=?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, rs.getString("Amount Paid"));
                    ps.setString(2, rs.getString("Bill No."));
                    ps.execute();
                }
                sql = "DELETE FROM `Payment_Entry_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                ps.execute();

                sql = "DELETE FROM `Payment_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                ps.execute();
                list.clear();

                voucher_date_2.setText("");
                supplier_name_2.setText("");
                display_board_label_2.setText("");
                total_amount_field_2.setText("");
                conn.commit();
                Rrc.showAlert(voucher_no_field_2.getText().toUpperCase() + " Entry was successfully deleted.", 1);
            } catch (SQLException ex) {
                Rrc.showAlert(ex.toString());
                try {
                    conn.rollback();
                } catch (SQLException ex1) {
                    Rrc.showAlert(ex1.toString());
                    Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadScreenAgain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Input/PaymentEntry/PaymentEntry.fxml"));
            root2.getChildren().setAll(parent);
        } catch (IOException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void printPayment(ActionEvent event) {
        try {
            String pdfFileName = "Report.pdf";
            String jrxmlFileName = "Payment.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFileName));
            Map map = new HashMap();
            map.put("voucherNo", voucher_no_field_2.getText());
            map.put("voucherDate", voucher_date_2.getText());
            map.put("supplierName", supplier_name_2.getText());
            map.put("billAmount", total_amount_field_2.getText());
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
            Rrc.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
