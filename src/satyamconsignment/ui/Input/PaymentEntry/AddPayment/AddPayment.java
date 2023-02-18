package satyamconsignment.ui.Input.PaymentEntry.AddPayment;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;
import satyamconsignment.ui.Input.PaymentEntry.PaymentEntryController;
import satyamconsignment.ui.Input.PaymentEntry.PaymentItem;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddPayment implements Initializable {

    private DateTimeFormatter formatter;
    ObservableList<PaymentItem> paymentItems;
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
    private TableView<PaymentItem> payment_tableview;
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
    private Text last_voucher_field;

    @FXML
    private Group root2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        paymentItems = FXCollections.observableArrayList();
        supplierNameComboList = FXCollections.observableArrayList();
        billNoComboList = FXCollections.observableArrayList();

        bill_no_col.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory<>("billAmount"));
        bill_dt_col.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        buyer_col.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
        bank_col.setCellValueFactory(new PropertyValueFactory<>("bank"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory<>("ddNo"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory<>("ddDate"));
        due_col.setCellValueFactory(new PropertyValueFactory<>("due"));
        amount_paid_col.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));

        payment_tableview.setItems(paymentItems);
        fillSupplierCombo();

        updateLastVoucher();
    }

    @FXML
    private void addPayment(ActionEvent event) {
        if (bill_no_combo.getValue().isEmpty() || bill_amount_field.getText().isEmpty() || buyer_name_field.getText().isEmpty() ||
                supplier_name_combo.getValue().isEmpty() || bank_field.getText().isEmpty() || bank_field.getText().isEmpty() ||
                due_amount_field.getText().isEmpty() || amount_paid_field.getText().isEmpty() || dd_no_field.getText().isEmpty() || dd_date_field.getValue().toString().isEmpty()) {
            Rrc.showAlert("Please check whether the fields are properly filled or not.", 2);
        } else {
            paymentItems.add(new PaymentItem(bill_no_combo.getValue(), bill_amount_field.getText(), bill_date_field.getText(), buyer_name_field.getText(),
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
                paymentItems.set(payment_tableview.getSelectionModel().getSelectedIndex(),
                        new PaymentItem(bill_no_combo.getValue(), bill_amount_field.getText(), bill_date_field.getText(), buyer_name_field.getText(),
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
            paymentItems.remove(payment_tableview.getSelectionModel().getSelectedIndex());
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
        for (PaymentItem temp : paymentItems) {
            totalAmountPaid += Integer.parseInt(temp.getAmountPaid());
        }
        total_amount_paid_field.setText(Integer.toString(totalAmountPaid));
    }

    @FXML
    private void saveData(ActionEvent event) {
        if (voucher_no_field.getText().isEmpty() || voucher_date_field.getValue().toString().isEmpty()) {
            Rrc.showAlert("Check whether the Voucher No. and the Voucher Date is properly filled", 2);
            return;
        }
        Connection connection = DatabaseHandler.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO `Payment_Entry_Table`(`Voucher No.`,`Voucher Date`,`Supplier Name`,`Total Amount`) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            preparedStatement.setString(2, formatter.format(voucher_date_field.getValue()));
            preparedStatement.setString(3, supplier_name_combo.getValue());
            preparedStatement.setString(4, total_amount_paid_field.getText());
            preparedStatement.execute();

            for (PaymentItem paymentItem : paymentItems) {
                sql = "INSERT INTO `Payment_Entry_Extended_Table`(`Voucher No.`,`Buyer Name`,`Bill No.`,`Bill Date`,`Bill Amount`,`Due Amount`,`Amount Paid`,`Bank`,`DD No.`,`DD Date`) VALUES (?,?,?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, voucher_no_field.getText());
                preparedStatement.setString(2, paymentItem.getBuyerName());
                preparedStatement.setString(3, paymentItem.getBillNo());
                preparedStatement.setString(4, paymentItem.getBillDate());
                preparedStatement.setString(5, paymentItem.getBillAmount());
                preparedStatement.setString(6, paymentItem.getDue());
                preparedStatement.setString(7, paymentItem.getAmountPaid());
                preparedStatement.setString(8, paymentItem.getBank());
                preparedStatement.setString(9, paymentItem.getDdNo());
                preparedStatement.setString(10, paymentItem.getDdDate());

                preparedStatement.execute();

                //Update Bill Entry Table
                sql = "UPDATE `Bill_Entry_Table` SET `Due`=? WHERE `BILL NO.`=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, paymentItem.getDue());
                preparedStatement.setString(2, paymentItem.getBillNo());
                preparedStatement.execute();

            }
            connection.commit();
            Rrc.showAlert("Saved Successfully", 1);
            loadScreenAgain();
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PaymentEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        paymentItems.clear();

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

    private void updateLastVoucher() {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "SELECT MAX(`Voucher No.`) from `PAYMENT_ENTRY_TABLE`;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            String lastVoucherNo = resultSet.getString("Max(`Voucher No.`)");
            last_voucher_field.setText("Last Voucher No. : " + lastVoucherNo);
        } catch (SQLException ex) {
            Logger.getLogger(AddPayment.class.getName()).log(Level.SEVERE, "Failed to update Last Payment Voucher", ex);
        }
    }


    private void fillSupplierCombo() {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "select Name from `Supplier_Master_Table` order by name collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            supplierNameComboList.clear();
            while (resultSet.next()) {
                supplierNameComboList.add(resultSet.getString("Name"));
            }
            supplier_name_combo.setItems(supplierNameComboList);
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddPayment.class.getName()).log(Level.SEVERE, "Failed to populate the supplier combo", ex);
        }
    }

    @FXML
    private void fillBillNoCombo(ActionEvent event) {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "SELECT `Bill No.` FROM `Bill_Entry_Table` where NOT `Due`='0' and `Supplier Name`=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, supplier_name_combo.getSelectionModel().getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();

            billNoComboList.clear();
            while (resultSet.next()) {
                billNoComboList.add(resultSet.getString("Bill No."));
            }
            bill_no_combo.setItems(billNoComboList);


        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddPayment.class.getName()).log(Level.SEVERE, "Failed to populate bill number combo", ex);
        }
    }

    @FXML
    private void fetchData(ActionEvent event) {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "Select * from Bill_Entry_Table where `Bill No.`=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bill_no_combo.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            buyer_name_field.setText(resultSet.getString("Buyer Name"));
            bill_date_field.setText(resultSet.getString("Bill Date"));
            bill_amount_field.setText(resultSet.getString("Bill Amount"));
            previouslyDue = Integer.parseInt(resultSet.getString("Due"));
            updateDueAmount();
            supplier_name_combo.setDisable(true);
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddPayment.class.getName()).log(Level.SEVERE, "Failed to fetch data", ex);
        }
    }

    @FXML
    private void updateDueAmount() {
        String temp;
        temp = Integer.toString(previouslyDue
                - Integer.parseInt(amount_paid_field.getText()));
        due_amount_field.setText(temp);
    }
}
