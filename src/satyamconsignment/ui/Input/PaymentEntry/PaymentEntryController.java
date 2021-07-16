package satyamconsignment.ui.Input.PaymentEntry;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.*;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;
import satyamconsignment.ui.Input.CollectionEntry.CollectionEntryController;

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


public class PaymentEntryController implements Initializable {

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
    private TextField dd_no_field;
    @FXML
    private JFXDatePicker dd_date_field;
    @FXML
    private JFXDatePicker voucher_date_field;
    @FXML
    private TextField voucher_no_field;
    @FXML
    private Button add_payment_btn;
    @FXML
    private Button replace_payment_btn;
    @FXML
    private Button delete_payment_btn;
    @FXML
    private TableColumn<?, ?> bill_no_col;
    @FXML
    private TableColumn<?, ?> bill_amt_col;
    @FXML
    private TableColumn<?, ?> dd_no_col;
    @FXML
    private TableColumn<?, ?> dd_date_col;
    @FXML
    private TableColumn<?, ?> bill_dt_col;
    @FXML
    private TableColumn<?, ?> buyer_col;
    @FXML
    private TableColumn<?, ?> due_col;
    @FXML
    private TableColumn<?, ?> amount_paid_col;
    @FXML
    private TableView<Payment> payment_tableview;
    @FXML
    private TextField amount_paid_field;
    @FXML
    private TextField buyer_name_field;
    @FXML
    private TextField bill_date_field;
    @FXML
    private TextField bill_amount_field;

    @FXML
    private ComboBox<String> supplier_name_combo;
    @FXML
    private ComboBox<String> bill_no_combo;
    @FXML
    private TextField due_amount_field;
    @FXML
    private TableColumn<?, ?> bank_col;
    @FXML
    private TextField bank_field;
    @FXML
    private TextField total_amount_paid_field;
    @FXML
    private JFXButton save_btn;
    @FXML
    private JFXButton clear_btn;
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

        bill_no_col.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory("billAmount"));
        bill_dt_col.setCellValueFactory(new PropertyValueFactory("billDate"));
        buyer_col.setCellValueFactory(new PropertyValueFactory("buyerName"));
        bank_col.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory("ddNo"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory("ddDate"));
        due_col.setCellValueFactory(new PropertyValueFactory("due"));
        amount_paid_col.setCellValueFactory(new PropertyValueFactory("amountPaid"));

        payment_tableview.setItems(list);
        fillSupplierCombo();

        bill_no_col_2.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col_2.setCellValueFactory(new PropertyValueFactory("billAmount"));
        buyer_col_2.setCellValueFactory(new PropertyValueFactory("buyerName"));
        bill_date_col_2.setCellValueFactory(new PropertyValueFactory("billDate"));
        due_col_2.setCellValueFactory(new PropertyValueFactory("due"));
        amount_paid_col_2.setCellValueFactory(new PropertyValueFactory("amountPaid"));
        dd_no_col_2.setCellValueFactory(new PropertyValueFactory("ddNo"));
        bank_col_2.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_date_col_2.setCellValueFactory(new PropertyValueFactory("ddDate"));

        updateLastVoucher();
    }

    private void fillSupplierCombo() {
        try {
            sql = "select Name from `Supplier_Master_Table` order by name collate nocase";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            supplierNameComboList.clear();
            while (rs.next()) {
                supplierNameComboList.add(rs.getString("Name"));
            }
            supplier_name_combo.setItems(supplierNameComboList);
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void fillBillNoCombo(ActionEvent event) {
        try {

            sql = "SELECT `Bill No.` FROM `Bill_Entry_Table` where NOT `Due`='0' and `Supplier Name`=?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, supplier_name_combo.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();

            billNoComboList.clear();
            while (rs.next()) {
                billNoComboList.add(rs.getString("Bill No."));
            }
            bill_no_combo.setItems(billNoComboList);


        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void fetchData(ActionEvent event) {
        try {
            sql = "Select * from Bill_Entry_Table where `Bill No.`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, bill_no_combo.getValue());
            rs = ps.executeQuery();
            buyer_name_field.setText(rs.getString("Buyer Name"));
            bill_date_field.setText(rs.getString("Bill Date"));
            bill_amount_field.setText(rs.getString("Bill Amount"));
            previouslyDue = Integer.parseInt(rs.getString("Due"));
            updateDueAmount();
            supplier_name_combo.setDisable(true);
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void updateDueAmount() {
        String temp;
        temp = Integer.toString(previouslyDue
                - Integer.parseInt(amount_paid_field.getText()));
        due_amount_field.setText(temp);
    }

    @FXML
    private void addPayment(ActionEvent event) {
        if (bill_no_combo.getValue().isEmpty() || bill_amount_field.getText().isEmpty() || buyer_name_field.getText().isEmpty() ||
                supplier_name_combo.getValue().isEmpty() || bank_field.getText().isEmpty() || bank_field.getText().isEmpty() ||
                due_amount_field.getText().isEmpty() || amount_paid_field.getText().isEmpty() || dd_no_field.getText().isEmpty() || dd_date_field.getValue().toString().isEmpty()) {
            Rrc.showAlert("Please check whether the fields are properly filled or not.", 2);
        } else {
            list.add(new Payment(bill_no_combo.getValue(), bill_amount_field.getText(), bill_date_field.getText(), buyer_name_field.getText(),
                    due_amount_field.getText(), amount_paid_field.getText(),
                    bank_field.getText(), dd_no_field.getText(), formatter.format(dd_date_field.getValue())));
            clearRepeatingFields();
            updateTotalAmountPaid();

        }
    }

    @FXML
    private void replacePayment(ActionEvent event) {
        if (payment_tableview.getSelectionModel().getSelectedIndex() == -1) {
            Rrc.showAlert("Please select an item from the Payment Table", 2);
        } else {
            if (bill_no_combo.getValue().isEmpty() || bill_amount_field.getText().isEmpty() || buyer_name_field.getText().isEmpty() ||
                    supplier_name_combo.getValue().isEmpty() || bank_field.getText().isEmpty() || bank_field.getText().isEmpty() ||
                    due_amount_field.getText().isEmpty() || amount_paid_field.getText().isEmpty() || dd_no_field.getText().isEmpty() || dd_date_field.getValue().toString().isEmpty()) {
                Rrc.showAlert("Please check whether the fields are properly filled or not.", 2);
            } else {
                list.set(payment_tableview.getSelectionModel().getSelectedIndex(),
                        new Payment(bill_no_combo.getValue(), bill_amount_field.getText(), bill_date_field.getText(), buyer_name_field.getText(),
                                due_amount_field.getText(), amount_paid_field.getText(),
                                bank_field.getText(), dd_no_field.getText(), formatter.format(dd_date_field.getValue())));
                clearRepeatingFields();
                updateTotalAmountPaid();
            }
        }
    }

    @FXML
    private void deletePayment(ActionEvent event) {
        if (payment_tableview.getSelectionModel().getSelectedIndex() == -1) {
            Rrc.showAlert("Please select an item from the Payment Table", 2);
        } else {
            list.remove(payment_tableview.getSelectionModel().getSelectedIndex());
            updateTotalAmountPaid();
        }
    }

    private void clearRepeatingFields() {
        bill_date_field.setText("");
        buyer_name_field.setText("");
        bill_amount_field.setText("0");
        due_amount_field.setText("0");
        amount_paid_field.setText("0");
        bank_field.setText("");
        dd_no_field.setText("");
        dd_date_field.setValue(null);
    }

    private void updateTotalAmountPaid() {
        totalAmountPaid = 0;
        for (Payment temp : list) {
            totalAmountPaid += Integer.parseInt(temp.getAmountPaid());
        }
        total_amount_paid_field.setText(Integer.toString(totalAmountPaid));
    }

    @FXML
    private void saveData(ActionEvent event) {
        if (voucher_no_field.getText().isEmpty() || voucher_date_field.getValue().toString().isEmpty()) {
            Rrc.showAlert("Check whether the Voucher No. and the Voucher Date is properly filled", 2);
        } else {
            try {
                conn.setAutoCommit(false);
                sql = "INSERT INTO `Payment_Entry_Table`(`Voucher No.`,`Voucher Date`,`Supplier Name`,`Total Amount`) VALUES (?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field.getText());
                ps.setString(2, formatter.format(voucher_date_field.getValue()));
                ps.setString(3, supplier_name_combo.getValue());
                ps.setString(4, total_amount_paid_field.getText());
                ps.execute();

                for (Payment temp : list) {
                    sql = "INSERT INTO `Payment_Entry_Extended_Table`(`Voucher No.`,`Buyer Name`,`Bill No.`,`Bill Date`,`Bill Amount`,`Due Amount`,`Amount Paid`,`Bank`,`DD No.`,`DD Date`) VALUES (?,?,?,?,?,?,?,?,?,?)";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, voucher_no_field.getText());
                    ps.setString(2, temp.getBuyerName());
                    ps.setString(3, temp.getBillNo());
                    ps.setString(4, temp.getBillDate());
                    ps.setString(5, temp.getBillAmount());
                    ps.setString(6, temp.getDue());
                    ps.setString(7, temp.getAmountPaid());
                    ps.setString(8, temp.getBank());
                    ps.setString(9, temp.getDdNo());
                    ps.setString(10, temp.getDdDate());

                    ps.execute();

                    //Update Bill Entry Table
                    sql = "UPDATE `Bill_Entry_Table` SET `Due`=? WHERE `BILL NO.`=?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, temp.getDue());
                    ps.setString(2, temp.getBillNo());
                    ps.execute();

                }
                conn.commit();
                Rrc.showAlert("Saved Successfully", 1);
                loadScreenAgain();
            } catch (SQLException ex) {
                Rrc.showAlert(ex.toString());
                try {
                    conn.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
            list.clear();
        }
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
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure that you want delete " + voucher_no_field_2.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
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

                updateLastVoucher();
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

    private void updateLastVoucher() {
        try {
            String sql = "SELECT MAX(`Voucher No.`) from `PAYMENT_ENTRY_TABLE`;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            String answer = rs.getString("Max(`Voucher No.`)");
            last_voucher_field.setText("Last Voucher No. : " + answer);
        } catch (SQLException ex) {
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
