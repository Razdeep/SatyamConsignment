package satyamconsignment.ui.Input.CollectionEntry.AddCollection;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.model.CollectionItem;

public class AddCollection implements Initializable {
    private int totalAmount;
    private ObservableList<CollectionItem> collectionItemObservableList;
    private ObservableList<String> buyerNameComboList;
    private ObservableList<String> billNoComboList;
    private DateTimeFormatter formatter;
    int previouslyDue;
    @FXML
    private TextField dd_no_field;
    @FXML
    private DatePicker dd_date_field;
    @FXML
    private TextField voucher_no_field;
    @FXML
    private DatePicker voucher_date_field;
    @FXML
    private TableView<CollectionItem> collection_tableview;
    @FXML
    private TableColumn<CollectionItem, String> bill_no_col;
    @FXML
    private TableColumn<CollectionItem, String> bill_amt_col;
    @FXML
    private TableColumn<CollectionItem, String> supplier_col;
    @FXML
    private TableColumn<CollectionItem, String> amount_collection_col;
    @FXML
    private TableColumn<CollectionItem, String> bank_col;
    @FXML
    private TableColumn<CollectionItem, String> dd_no_col;
    @FXML
    private TableColumn<CollectionItem, String> dd_date_col;
    @FXML
    private TextField bill_date;
    @FXML
    private TextField bill_amount;
    @FXML
    private ComboBox<String> buyer_name;
    @FXML
    private TextField supplier_name;
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
    private Label last_voucher_field;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        totalAmount = 0;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        collectionItemObservableList = FXCollections.observableArrayList();
        buyerNameComboList = FXCollections.observableArrayList();
        billNoComboList = FXCollections.observableArrayList();
        bill_no_col.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory<>("billAmount"));
        supplier_col.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        amount_collection_col.setCellValueFactory(new PropertyValueFactory<>("amountCollected"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory<>("ddNo"));
        bank_col.setCellValueFactory(new PropertyValueFactory<>("bank"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory<>("ddDate"));
        collection_tableview.setItems(collectionItemObservableList);
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

            buyer_name.setItems(buyerNameComboList);

        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void fillBillNoCombo() {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();

            String getCollectionAmountsForEveryoneSql = "select `Bill No.`, sum(`Amount Collected`) as `Amount Collected` "
                    + "from `Collection_Entry_Extended_Table` group by `Bill No.`";
            Map<String, Double> collectionAmountMap = new HashMap<>();
            PreparedStatement preparedStatement = connection
                    .prepareStatement(getCollectionAmountsForEveryoneSql);
            ResultSet collectionAmountsResultSet = preparedStatement.executeQuery();
            while (collectionAmountsResultSet.next()) {
                String amountCollectedStr = collectionAmountsResultSet
                        .getString("Amount Collected");
                double amountCollected = 0;
                try {
                    amountCollected = Double.valueOf(amountCollectedStr);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                collectionAmountMap.put(collectionAmountsResultSet.getString("Bill No."),
                        amountCollected);
            }

            String getBillAmountsForBuyerSql = "select `Bill No.`, `Bill Amount` from `Bill_Entry_Table` "
                    + "where `Buyer Name`=? order by `Bill No.` collate nocase";

            preparedStatement = connection.prepareStatement(getBillAmountsForBuyerSql);
            preparedStatement.setString(1, buyer_name.getValue());
            ResultSet billNoResultSet = preparedStatement.executeQuery();

            billNoComboList.clear();

            while (billNoResultSet.next()) {
                String billNo = billNoResultSet.getString("Bill No.");
                String billAmountStr = billNoResultSet.getString("Bill Amount");
                Double billAmount = 1.0;
                try {
                    billAmount = Double.valueOf(billAmountStr);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                if (collectionAmountMap.containsKey(billNo)) {
                    if (billAmount - collectionAmountMap.get(billNo) > 0) {
                        billNoComboList.add(billNo);
                    }
                } else {
                    billNoComboList.add(billNo);
                }
            }

            bill_no_combo.setItems(billNoComboList);
            display_board_label.setText(buyer_name.getValue());

        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void fetchData() {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "Select * from Bill_Entry_Table where `Bill No.`=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bill_no_combo.getValue());
            ResultSet billResultSet = preparedStatement.executeQuery();
            String supplierName = billResultSet.getString("Supplier Name");
            String billDate = billResultSet.getString("Bill Date");
            String billAmount = billResultSet.getString("Bill Amount");
            previouslyDue = Integer.parseInt(billResultSet.getString("Collection Due"));
            supplier_name.setText(supplierName);
            bill_date.setText(billDate);
            bill_amount.setText(billAmount);
            updateCollectionDue();
//            buyer_name.setDisable(true);

        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void addCollection(ActionEvent event) {
        if (bill_no_combo.getValue().isEmpty() || bill_amount.getText().isEmpty()
                || buyer_name.getValue().isEmpty() || supplier_name.getText().isEmpty()
                || bank_field.getText().isEmpty() || collection_due_field.getText().isEmpty()
                || amount_collected_field.getText().isEmpty() || dd_no_field.getText().isEmpty()
                || dd_date_field.getValue().toString().isEmpty()) {
            Utils.showAlert("Please check whether the fields are properly filled or not.", 2);
            return;
        }

        collectionItemObservableList.add(new CollectionItem(bill_no_combo.getValue(),
                bill_date.getText(), bill_amount.getText(), supplier_name.getText(),
                collection_due_field.getText(), amount_collected_field.getText(),
                bank_field.getText(), dd_no_field.getText(),
                formatter.format(dd_date_field.getValue())));
        updateTotalAmount();
        clearRepeatingFields();
    }

    @FXML
    private void updateCollectionDue() {
        collection_due_field.setText(Integer
                .toString(previouslyDue - Integer.parseInt(amount_collected_field.getText())));
    }

    private void updateTotalAmount() {
        totalAmount = 0;
        for (CollectionItem temp : collectionItemObservableList) {
            totalAmount += Integer.parseInt(temp.getAmountCollected());
        }
        total_amount_field.setText(Integer.toString(totalAmount));
    }

    private void clearRepeatingFields() {
        bill_date.setText("");
        supplier_name.setText("");
        bill_amount.setText("");
        collection_due_field.setText("0");
        amount_collected_field.setText("0");
        bank_field.setText("");
        dd_no_field.setText("");

        dd_date_field.setValue(null);
    }

    @FXML
    private void replaceCollection(ActionEvent event) {
        if (collection_tableview.getSelectionModel().getSelectedIndex() == -1) {
            Utils.showAlert("Please select an item from the Collection Table", 2);
            return;
        }
        if (bill_no_combo.getValue().isEmpty() || bill_amount.getText().isEmpty()
                || buyer_name.getValue().isEmpty() || supplier_name.getText().isEmpty()
                || bank_field.getText().isEmpty() || collection_due_field.getText().isEmpty()
                || amount_collected_field.getText().isEmpty() || dd_no_field.getText().isEmpty()
                || dd_date_field.getValue().toString().isEmpty()) {
            Utils.showAlert("Please check whether the fields are properly filled or not.", 2);
            return;
        }
        collectionItemObservableList.set(
                collection_tableview.getSelectionModel().getSelectedIndex(),
                CollectionItem.builder().billNo(bill_no_combo.getValue())
                        .billDate(bill_date.getText()).billAmount(bill_amount.getText())
                        .supplierName(supplier_name.getText()).due(collection_due_field.getText())
                        .amountCollected(amount_collected_field.getText())
                        .bank(bank_field.getText()).ddNo(dd_no_field.getText())
                        .ddDate(dd_date_field.getValue().toString()).build());

        updateTotalAmount();
    }

    @FXML
    private void deleteCollection(ActionEvent event) {
        if (collection_tableview.getSelectionModel().getSelectedIndex() == -1) {
            Utils.showAlert("Please select an item from the LR Table", 2);
            return;
        }
        collectionItemObservableList
                .remove(collection_tableview.getSelectionModel().getSelectedIndex());
        updateTotalAmount();
    }

    @FXML
    private void clearAllFields() {
        clearRepeatingFields();
        voucher_no_field.setText("");
        voucher_date_field.setValue(null);
        buyer_name.setDisable(false);
        billNoComboList.clear();
    }

    @FXML
    private void saveData(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure that you want save " + voucher_no_field.getText() + " ?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() != ButtonType.YES) {
            return;
        }
        if (voucher_no_field.getText().isEmpty()
                || voucher_date_field.getValue().toString().isEmpty()) {
            Utils.showAlert("Check whether the Voucher No. and the Voucher Date is properly filled",
                    2);
            return;
        }
        Connection connection = DatabaseHandler.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO `Collection_Entry_Table`(`Voucher No.`,`Voucher Date`,`Buyer Name`,`Total Amount`) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            preparedStatement.setString(2, formatter.format(voucher_date_field.getValue()));
            preparedStatement.setString(3, buyer_name.getValue());
            preparedStatement.setString(4, total_amount_field.getText());
            preparedStatement.execute();

            for (CollectionItem temp : collectionItemObservableList) {
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

                // Update Bill Entry Table
                sql = "UPDATE `Bill_Entry_Table` SET `Collection Due`=? WHERE `BILL NO.`=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, temp.getDue());
                preparedStatement.setString(2, temp.getBillNo());
                preparedStatement.execute();
            }
            collectionItemObservableList.clear();
            connection.commit();
            Utils.showAlert("Saved Successfully", 1);
            clearAllFields();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Utils.showAlert(ex.toString());
                Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex1.toString(),
                        ex1);
            }
        }

        collectionItemObservableList.clear();
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
            Utils.showAlert(ex.toString());
            Logger.getLogger(AddCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
