package satyamconsignment.ui.Input.CollectionEntry.AddCollection;

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
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;
import satyamconsignment.ui.Input.CollectionEntry.Collection;

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

public class AddCollection implements Initializable {
    int totalAmount;
    ObservableList<Collection> list;
    ObservableList<String> buyerNameComboList;
    ObservableList<String> billNoComboList;
    String buyerName, supplierName, billDate, billAmount;
    DateTimeFormatter formatter;
    int previouslyDue;
    @FXML
    private TextField dd_no_field;
    @FXML
    private JFXDatePicker dd_date_field;
    @FXML
    private JFXButton save_btn;
    @FXML
    private JFXButton clear_btn;
    @FXML
    private TextField voucher_no_field;
    @FXML
    private JFXDatePicker voucher_date_field;
    @FXML
    private Button add_collection_btn;
    @FXML
    private Button replace_collection_btn;
    @FXML
    private Button delete_collection_btn;
    @FXML
    private TableView<Collection> collection_tableview;
    @FXML
    private TableColumn<Collection, String> bill_no_col;
    @FXML
    private TableColumn<Collection, String> bill_amt_col;
    @FXML
    private TableColumn<Collection, String> supplier_col;
    @FXML
    private TableColumn<Collection, String> amount_collection_col;
    @FXML
    private TableColumn<Collection, String> bank_col;
    @FXML
    private TableColumn<Collection, String> dd_no_col;
    @FXML
    private TableColumn<Collection, String> dd_date_col;
    @FXML
    private TextField bill_date_view;
    @FXML
    private TextField bill_amount_view;
    @FXML
    private ComboBox<String> buyer_name_view;
    @FXML
    private TextField supplier_name_view;
    @FXML
    private ComboBox<String> bill_no_combo;
    @FXML
    private TextField bank_field;

    @FXML
    private Label display_board_label;
    @FXML
    private TextField total_amount_field;

    @FXML
    private TextField amount_collected_field;
    @FXML
    private TextField collection_due_field;
    @FXML
    private Group root2;
    @FXML
    private Button print_btn;
    @FXML
    private Label last_voucher_field;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        totalAmount = 0;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Connection connection = databaseHandler.getConnection();

        list = FXCollections.observableArrayList();
        buyerNameComboList = FXCollections.observableArrayList();
        billNoComboList = FXCollections.observableArrayList();
        bill_no_col.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory("billAmount"));
        supplier_col.setCellValueFactory(new PropertyValueFactory("supplierName"));
        amount_collection_col.setCellValueFactory(new PropertyValueFactory("amountCollected"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory("ddNo"));
        bank_col.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory("ddDate"));
        collection_tableview.setItems(list);
        fillBuyerNameCombo();

        updateLastVoucher();
    }

    private void fillBuyerNameCombo() {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "select Name from `Buyer_Master_Table` order by name collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet buyerMasterResultSet = preparedStatement.executeQuery();

            buyerNameComboList.clear();
            while (buyerMasterResultSet.next()) {
                buyerNameComboList.add(buyerMasterResultSet.getString("Name"));
            }

            buyer_name_view.setItems(buyerNameComboList);

        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void fillBillNoCombo() {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "select `Bill No.` from `Bill_Entry_Table` where `Buyer Name`=? and not `Collection Due`='0' order by `Bill No.` collate nocase";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, buyer_name_view.getValue());
            ResultSet pendingBillNoResultSet = preparedStatement.executeQuery();

            billNoComboList.clear();
            while (pendingBillNoResultSet.next()) {
                billNoComboList.add(pendingBillNoResultSet.getString("Bill No."));
            }
            bill_no_combo.setItems(billNoComboList);
            display_board_label.setText(buyer_name_view.getValue());

        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void fetchData(ActionEvent event) {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "Select * from Bill_Entry_Table where `Bill No.`=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bill_no_combo.getValue());
            ResultSet billResultSet = preparedStatement.executeQuery();
            buyerName = billResultSet.getString("Buyer Name");
            supplierName = billResultSet.getString("Supplier Name");
            billDate = billResultSet.getString("Bill Date");
            billAmount = billResultSet.getString("Bill Amount");
            previouslyDue = Integer.parseInt(billResultSet.getString("Collection Due"));
            supplier_name_view.setText(supplierName);
            bill_date_view.setText(billDate);
            bill_amount_view.setText(billAmount);
            updateCollectionDue();
            buyer_name_view.setDisable(true);

        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void addCollection(ActionEvent event) {
        if (bill_no_combo.getValue().isEmpty() || billAmount.isEmpty() || buyerName.isEmpty() || supplierName.isEmpty() || bank_field.getText().isEmpty() ||
                collection_due_field.getText().isEmpty() || amount_collected_field.getText().isEmpty() || dd_no_field.getText().isEmpty() || dd_date_field.getValue().toString().isEmpty()) {
            Rrc.showAlert("Please check whether the fields are properly filled or not.", 2);
        } else {
            list.add(new Collection(bill_no_combo.getValue(), bill_date_view.getText(), billAmount, supplierName,
                    collection_due_field.getText(), amount_collected_field.getText(),
                    bank_field.getText(), dd_no_field.getText(), formatter.format(dd_date_field.getValue())));
            updateTotalAmount();
            clearRepeatingFields();

        }
    }

    @FXML
    private void updateCollectionDue() {
        collection_due_field.setText(Integer.toString(previouslyDue
                - Integer.parseInt(amount_collected_field.getText())));
    }

    private void updateTotalAmount() {
        totalAmount = 0;
        for (Collection temp : list) {
            totalAmount += Integer.parseInt(temp.getAmountCollected());
        }
        total_amount_field.setText(Integer.toString(totalAmount));
    }

    private void clearRepeatingFields() {
        //bill_no_combo.setValue("");
        buyerName = null;
        billDate = null;
        supplierName = null;
        billAmount = null;
        //buyer_name_view.setText("");
        bill_date_view.setText("");
        supplier_name_view.setText("");
        bill_amount_view.setText("");
        collection_due_field.setText("0");
        amount_collected_field.setText("0");
        bank_field.setText("");
        dd_no_field.setText("");

        dd_date_field.setValue(null);
    }

    @FXML
    private void replaceCollection(ActionEvent event) {
        if (collection_tableview.getSelectionModel().getSelectedIndex() == -1) {
            Rrc.showAlert("Please select an item from the Collection Table", 2);
        } else {
            if (bill_no_combo.getValue().isEmpty() || billAmount.isEmpty() || buyerName.isEmpty() || supplierName.isEmpty() || bank_field.getText().isEmpty() ||
                    collection_due_field.getText().isEmpty() || amount_collected_field.getText().isEmpty() || dd_no_field.getText().isEmpty() || dd_date_field.getValue().toString().isEmpty()) {
                Rrc.showAlert("Please check whether the fields are properly filled or not.", 2);
            } else {
                list.set(collection_tableview.getSelectionModel().getSelectedIndex(),
                        new Collection(bill_no_combo.getValue(), bill_date_view.getText(), billAmount, supplierName,
                                collection_due_field.getText(),
                                amount_collected_field.getText(), bank_field.getText(), dd_no_field.getText(), dd_date_field.getValue().toString()));
                updateTotalAmount();
            }
        }
    }


    @FXML
    private void deleteCollection(ActionEvent event) {
        if (collection_tableview.getSelectionModel().getSelectedIndex() == -1) {
            Rrc.showAlert("Please select an item from the LR Table", 2);
        } else {
            list.remove(collection_tableview.getSelectionModel().getSelectedIndex());
            updateTotalAmount();
        }
    }


    private void clearAllFields() {
        clearRepeatingFields();
        //error
        voucher_no_field.setText("");
        voucher_date_field.setValue(null);
        buyer_name_view.setDisable(false);
        //buyer_name_view.setValue(null);
        billNoComboList.clear();
        //bill_no_combo.setValue(null);

    }

    @FXML
    private void saveData(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want save " + voucher_no_field.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {
            return;
        }
        if (voucher_no_field.getText().isEmpty() || voucher_date_field.getValue().toString().isEmpty()) {
            Rrc.showAlert("Check whether the Voucher No. and the Voucher Date is properly filled", 2);
            return;
        }
        Connection connection = DatabaseHandler.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO `Collection_Entry_Table`(`Voucher No.`,`Voucher Date`,`Buyer Name`,`Total Amount`) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            preparedStatement.setString(2, formatter.format(voucher_date_field.getValue()));
            preparedStatement.setString(3, buyer_name_view.getValue());
            preparedStatement.setString(4, total_amount_field.getText());
            preparedStatement.execute();

            for (Collection temp : list) {
                sql = "INSERT INTO `Collection_Entry_Extended_Table`(`Voucher No.`,`Supplier Name`,`Bill No.`,`Bill Date`,`Bill Amount`,`Collection Due`,`Amount Collected`,`Bank`,`DD No.`,`DD Date`) VALUES (?,?,?,?,?,?,?,?,?,?);";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, voucher_no_field.getText());
                preparedStatement.setString(2, temp.getSupplierName());
                preparedStatement.setString(3, temp.getBillNo());
                preparedStatement.setString(4, temp.getBillDate());
                preparedStatement.setString(5, temp.getBillAmount());
                preparedStatement.setString(6, temp.getDue());
                preparedStatement.setString(7, temp.getAmountCollected());
                preparedStatement.setString(8, temp.getBank());
                preparedStatement.setString(9, temp.getDdNo());
                preparedStatement.setString(10, temp.getDdDate());

                preparedStatement.execute();

                //Update Bill Entry Table
                sql = "UPDATE `Bill_Entry_Table` SET `Collection Due`=? WHERE `BILL NO.`=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, temp.getDue());
                preparedStatement.setString(2, temp.getBillNo());
                preparedStatement.execute();
            }
            list.clear();
            connection.commit();
            Rrc.showAlert("Saved Successfully", 1);
            //clearAllFields();
            loadScreenAgain();
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Rrc.showAlert(ex.toString());
                Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex1.toString(), ex1);
            }
        }

        //@TODO clear the variables
        list.clear();
    }


    private void loadScreenAgain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Input/CollectionEntry/CollectionEntry.fxml"));
            root2.getChildren().setAll(parent);
        } catch (IOException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    private void updateLastVoucher() {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "SELECT MAX(`Voucher No.`) from `COLLECTION_ENTRY_TABLE`;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            String answer = resultSet.getString("Max(`Voucher No.`)");
            last_voucher_field.setText("Last Voucher No. : " + answer);
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
