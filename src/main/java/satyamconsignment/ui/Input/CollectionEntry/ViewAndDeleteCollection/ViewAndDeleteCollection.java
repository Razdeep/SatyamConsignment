package satyamconsignment.ui.Input.CollectionEntry.ViewAndDeleteCollection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.model.CollectionItem;

public class ViewAndDeleteCollection implements Initializable {
    private List<CollectionItem> collectionItemList;
    private List<String> buyerNameComboList;
    private List<String> billNoComboList;
    private DateTimeFormatter formatter;

    @FXML
    private TextField voucher_no_field;
    @FXML
    private DatePicker voucher_date_field;
    @FXML
    private Button replace_collection_btn;
    @FXML
    private Button delete_collection_btn;
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
    private Button get_details_btn;
    @FXML
    private Button delete_entry_btn;

    @FXML
    private TableColumn<?, ?> bill_date_col;
    @FXML
    private TextField voucher_date;
    @FXML
    private TextField buyer_name;

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
        // int totalAmount = 0;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        collectionItemList = FXCollections.observableArrayList();
        buyerNameComboList = FXCollections.observableArrayList();
        billNoComboList = FXCollections.observableArrayList();

        bill_no_col.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory("billAmount"));
        supplier_col.setCellValueFactory(new PropertyValueFactory("supplierName"));
        bill_date_col.setCellValueFactory(new PropertyValueFactory("billDate"));
        amount_collection_col.setCellValueFactory(new PropertyValueFactory("amountCollected"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory("ddNo"));
        bank_col.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory("ddDate"));
    }

    @FXML
    private void getDetails(ActionEvent event) {

        if (voucher_no_field.getText().isEmpty()) {
            Utils.showAlert("Voucher No. Field is kept empty. Please fill the voucher no.");
            return;
        }
        Connection connection = DatabaseHandler.getInstance().getConnection();
        try {
            String sql = "select * from `Collection_Entry_Table` where `Voucher No.`=? collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            ResultSet collectionResultSet = preparedStatement.executeQuery();
            if (collectionResultSet.isClosed()) {
                Utils.showAlert("No Results found", 1);
                return;
            }

            voucher_date.setText(collectionResultSet.getString("Voucher Date"));
            buyer_name.setText(collectionResultSet.getString("Buyer Name"));
            total_amount_field.setText(collectionResultSet.getString("Total Amount"));
            display_board_label.setText(collectionResultSet.getString("Buyer Name"));

            collectionItemList.clear();
            sql = "select * from `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            collectionResultSet = preparedStatement.executeQuery();
            if (collectionResultSet.isClosed()) {
                Utils.showAlert("No Results found", 1);
                return;
            }
            while (collectionResultSet.next()) {
                collectionItemList.add(new CollectionItem(collectionResultSet.getString("Bill No."),
                        collectionResultSet.getString("Bill Date"),
                        collectionResultSet.getString("Bill Amount"),
                        collectionResultSet.getString("Supplier Name"),
                        collectionResultSet.getString("collection due"),
                        collectionResultSet.getString("amount collected"),
                        collectionResultSet.getString("bank"),
                        collectionResultSet.getString("DD No."),
                        collectionResultSet.getString("DD Date")));
            }
            collection_tableview.setItems(FXCollections.observableArrayList(collectionItemList));
            delete_entry_btn.setDisable(false);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
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

        Connection connection = DatabaseHandler.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            String sql = "SELECT `Bill No.`,`Amount Collected` FROM `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                sql = "UPDATE `Bill_Entry_Table` SET `Collection Due`=`Collection Due`+? WHERE `BILL NO.`=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, rs.getString("Amount Collected"));
                preparedStatement.setString(2, rs.getString("Bill No."));
                preparedStatement.execute();
            }

            sql = "DELETE FROM `Collection_Entry_Table` where `Voucher No.`=? collate nocase";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            preparedStatement.execute();

            sql = "DELETE FROM `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voucher_no_field.getText());
            preparedStatement.execute();
            collectionItemList.clear();
            voucher_date.setText("");
            buyer_name.setText("");
            display_board_label.setText("");
            total_amount_field.setText("");
            connection.commit();
            Utils.showAlert(
                    voucher_no_field.getText().toUpperCase() + " Entry was successfully deleted.",
                    1);
            updateLastVoucher();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Utils.showAlert(ex1.toString());
                Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE,
                        ex.toString(), ex1);
            }
        }
    }

    @FXML
    private void printCollection(ActionEvent event) {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String pdfFileName = "Report.pdf";
            String jrxmlFileName = "Collection.jrxml";
            JasperReport jasperReport = JasperCompileManager
                    .compileReport(getClass().getResourceAsStream(jrxmlFileName));
            Map map = new HashMap();
            map.put("voucherNo", voucher_no_field.getText());
            map.put("voucherDate", voucher_date.getText());
            map.put("buyerName", buyer_name.getText());
            map.put("billAmount", total_amount_field.getText());
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, connection);
            JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
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
            Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
        }
    }
}
