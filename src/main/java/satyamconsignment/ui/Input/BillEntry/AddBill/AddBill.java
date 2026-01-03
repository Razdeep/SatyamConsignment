package satyamconsignment.ui.Input.BillEntry.AddBill;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.controlsfx.control.textfield.TextFields;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.model.LR;
import satyamconsignment.ui.Input.BillEntry.BillEntryController;

public class AddBill implements Initializable {

    @FXML
    private TextField supplier_field;

    @FXML
    private TextField buyer_name_field;

    @FXML
    private TextField bill_no_field;

    @FXML
    private TextField transport_field;

    @FXML
    private DatePicker date_field;

    @FXML
    private DatePicker lr_date;

    @FXML
    private TextField bill_amount_field;

    @FXML
    private TextField pm_field;

    @FXML
    private TextField lr_field;

    @FXML
    private Button save_btn;

    @FXML
    private Button clear_btn;

    @FXML
    private Button add_btn;

    @FXML
    private TableView<LR> lrTable;

    @FXML
    private TableColumn<LR, String> lr_no_col;

    @FXML
    private TableColumn<LR, String> pm_col;

    @FXML
    private Button replace_btn;

    @FXML
    private Button delete_btn;

    final ObservableList<LR> lrList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ArrayList<String> supplierList = new ArrayList<>();
            ArrayList<String> buyerList = new ArrayList<>();
            ArrayList<String> transportList = new ArrayList<>();
            Connection connection = DatabaseHandler.getInstance().getConnection();
            ResultSet supplierResultSet = connection
                    .prepareStatement("select * from Supplier_Master_Table order by name collate nocase")
                    .executeQuery();
            ResultSet buyerResultSet = connection
                    .prepareStatement("select * from Buyer_Master_Table order by name collate nocase")
                    .executeQuery();
            ResultSet transportResultSet = connection
                    .prepareStatement("select * from Transport_Master_Table order by name collate nocase")
                    .executeQuery();
            while (supplierResultSet.next()) {
                supplierList.add(supplierResultSet.getString("name"));
            }
            while (buyerResultSet.next()) {
                buyerList.add(buyerResultSet.getString("name"));
            }
            while (transportResultSet.next()) {
                transportList.add(transportResultSet.getString("name"));
            }

            TextFields.bindAutoCompletion(supplier_field, supplierList);
            TextFields.bindAutoCompletion(buyer_name_field, buyerList);
            TextFields.bindAutoCompletion(transport_field, transportList);

            lr_no_col.setCellValueFactory(new PropertyValueFactory<>("lrNo"));
            pm_col.setCellValueFactory(new PropertyValueFactory<>("pm"));

        } catch (Exception ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void addToList(ActionEvent event) {
        if (bill_no_field.getText().isEmpty()
                || lr_field.getText().isEmpty()
                || pm_field.getText().isEmpty()) {
            Utils.showAlert("Please ensure that the Bill No., LR No. and PM fields are filled up properly.", 2);
            return;
        }
        lrList.add(new LR(bill_no_field.getText(), lr_field.getText(), pm_field.getText()));
        lrTable.setItems(lrList);
        lr_field.clear();
        pm_field.clear();
    }

    @FXML
    private void save(ActionEvent event) {
        if (supplier_field.getText().isEmpty()
                || buyer_name_field.getText().isEmpty()
                || bill_no_field.getText().isEmpty()
                || date_field.getValue().toString().isEmpty()
                || transport_field.getText().isEmpty()
                || lr_date.getValue().toString().isEmpty()
                || bill_amount_field.getText().isEmpty()
                || lrList.isEmpty()) {
            Utils.showAlert("Please ensure to fill up all the fields");
            return;
        }
        /* Code for saving data into Bill_Entry_Table */
        Connection connection = DatabaseHandler.getInstance().getConnection();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            String sql =
                    "INSERT INTO `Bill_Entry_Table`(`Supplier Name`,`Buyer Name`,`Bill No.`,`Bill Date`,`Transport`,`LR Date`,`Bill Amount`,`Collection Due`,`Due`) VALUES (?,?,?,?,?,?,?,?,?);";

            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, supplier_field.getText());
            preparedStatement.setString(2, buyer_name_field.getText());
            preparedStatement.setString(3, bill_no_field.getText());
            preparedStatement.setString(4, formatter.format(date_field.getValue()));
            preparedStatement.setString(5, transport_field.getText());
            preparedStatement.setString(6, formatter.format(lr_date.getValue()));
            preparedStatement.setString(7, bill_amount_field.getText());
            preparedStatement.setString(8, bill_amount_field.getText());
            preparedStatement.setString(9, bill_amount_field.getText());
            preparedStatement.execute();

            /* Code for Entering Data into LR_Table */
            for (LR lr : lrList) {
                sql = "INSERT INTO `LR_Table`(`Bill No.`,`LR No.`,`PM`) VALUES (?,?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, bill_no_field.getText());
                preparedStatement.setString(2, lr.getLrNo());
                preparedStatement.setString(3, lr.getPm());
                preparedStatement.execute();
            }
            clearAllFields();
            connection.commit();
            Utils.showAlert("Saved Successfully", 1);
        } catch (Exception ex) {
            try {
                connection.rollback();
            } catch (Exception ex1) {
                Utils.showAlert(ex1.toString());
                Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, ex1.toString(), ex1);
            }
            Utils.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void clearAllFields() {
        supplier_field.clear();
        buyer_name_field.clear();
        bill_no_field.clear();
        transport_field.clear();
        date_field.setValue(null);
        lr_date.setValue(null);
        bill_amount_field.clear();
        pm_field.clear();
        lr_field.clear();
        lrList.clear();
    }

    @FXML
    private void replaceInList(ActionEvent event) {
        if (lrTable.getSelectionModel().getSelectedIndex() == -1) {
            Utils.showAlert("Please select an item from the LR Table", 2);
            return;
        }
        if (bill_no_field.getText().isEmpty()
                || lr_field.getText().isEmpty()
                || pm_field.getText().isEmpty()) {
            Utils.showAlert("Please ensure that the Bill No., LR No. and PM fields are filled up properly.", 2);
            return;
        }
        lrList.set(
                lrTable.getSelectionModel().getSelectedIndex(),
                new LR(bill_no_field.getText(), lr_field.getText(), pm_field.getText()));
        lr_field.clear();
        pm_field.clear();
    }

    @FXML
    private void deleteFromList(ActionEvent event) {
        if (lrTable.getSelectionModel().getSelectedIndex() == -1) {
            Utils.showAlert("Please select an item from the LR Table", 2);
            return;
        }
        lrList.remove(lrTable.getSelectionModel().getSelectedIndex());
    }
}
