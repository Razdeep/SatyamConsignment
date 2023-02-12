package satyamconsignment.ui.Input.CollectionEntry.ViewAndDeleteCollection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;
import satyamconsignment.ui.Input.CollectionEntry.Collection;
import satyamconsignment.ui.Input.CollectionEntry.CollectionEntryController;

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

public class ViewAndDeleteCollection implements Initializable {
    Rrc rrc;
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    String sql;
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
    private Button get_details_btn;
    @FXML
    private Button delete_entry_btn;
    @FXML
    private TextField voucher_no_field_2;
    @FXML
    private TableColumn<Collection, String> bill_no_col_2;
    @FXML
    private TableColumn<Collection, String> bill_amt_col_2;
    @FXML
    private TableColumn<Collection, String> supplier_col_2;
    @FXML
    private TableColumn<Collection, String> amount_collection_col_2;
    @FXML
    private TableColumn<Collection, String> bank_col_2;
    @FXML
    private TableColumn<Collection, String> dd_no_col_2;
    @FXML
    private TableColumn<Collection, String> dd_date_col_2;
    @FXML
    private TextField total_amount_field_2;
    @FXML
    private TableView<Collection> collection_tableview_2;
    @FXML
    private TableColumn<?, ?> bill_date_col_2;
    @FXML
    private Label display_board_label_2;
    @FXML
    private TextField voucher_date_2;
    @FXML
    private TextField buyer_name_2;

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
        rrc = new Rrc();
        totalAmount = 0;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        databaseHandler = DatabaseHandler.getInstance();
        conn = databaseHandler.getConnection();

        list = FXCollections.observableArrayList();
        buyerNameComboList = FXCollections.observableArrayList();
        billNoComboList = FXCollections.observableArrayList();
//        bill_no_col.setCellValueFactory(new PropertyValueFactory("billNo"));
//        bill_amt_col.setCellValueFactory(new PropertyValueFactory("billAmount"));
//        supplier_col.setCellValueFactory(new PropertyValueFactory("supplierName"));
//        amount_collection_col.setCellValueFactory(new PropertyValueFactory("amountCollected"));
//        dd_no_col.setCellValueFactory(new PropertyValueFactory("ddNo"));
//        bank_col.setCellValueFactory(new PropertyValueFactory("bank"));
//        dd_date_col.setCellValueFactory(new PropertyValueFactory("ddDate"));
//        collection_tableview.setItems(list);
//        fillBuyerNameCombo();

        bill_no_col_2.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col_2.setCellValueFactory(new PropertyValueFactory("billAmount"));
        supplier_col_2.setCellValueFactory(new PropertyValueFactory("supplierName"));
        bill_date_col_2.setCellValueFactory(new PropertyValueFactory("billDate"));
        amount_collection_col_2.setCellValueFactory(new PropertyValueFactory("amountCollected"));
        dd_no_col_2.setCellValueFactory(new PropertyValueFactory("ddNo"));
        bank_col_2.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_date_col_2.setCellValueFactory(new PropertyValueFactory("ddDate"));

//        updateLastVoucher();
    }


    @FXML
    private void getDetails(ActionEvent event) {

        if (voucher_no_field_2.getText().isEmpty()) {
            Rrc.showAlert("Voucher No. Field is kept empty. Please fill the voucher no.");
        } else {
            try {
                sql = "select * from `Collection_Entry_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs = ps.executeQuery();

                voucher_date_2.setText(rs.getString("Voucher Date"));
                buyer_name_2.setText(rs.getString("Buyer Name"));
                total_amount_field_2.setText(rs.getString("Total Amount"));
                display_board_label_2.setText(rs.getString("Buyer Name"));

                list.clear();
                sql = "select * from `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs = ps.executeQuery();
                if (rs.isClosed()) {
                    Rrc.showAlert("No Results found", 1);
                } else {
                    while (rs.next()) {
                        list.add(new Collection(rs.getString("Bill No."), rs.getString("Bill Date"), rs.getString("Bill Amount"),
                                rs.getString("Supplier Name"), rs.getString("collection due"), rs.getString("amount collected"),
                                rs.getString("bank"), rs.getString("DD No."),
                                rs.getString("DD Date")));
                    }
                    collection_tableview_2.setItems(list);
                    delete_entry_btn.setDisable(false);
                }
            } catch (SQLException ex) {
                Rrc.showAlert(ex.toString());
                Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
    }


    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want delete " + voucher_no_field_2.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                //EDIT
                conn.setAutoCommit(false);
                sql = "SELECT `Bill No.`,`Amount Collected` FROM `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    sql = "UPDATE `Bill_Entry_Table` SET `Collection Due`=`Collection Due`+? WHERE `BILL NO.`=?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, rs.getString("Amount Collected"));
                    ps.setString(2, rs.getString("Bill No."));
                    ps.execute();
                }


                sql = "DELETE FROM `Collection_Entry_Table` where `Voucher No.`=? collate nocase";

                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                ps.execute();

                sql = "DELETE FROM `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";

                ps = conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                ps.execute();
                list.clear();
                voucher_date_2.setText("");
                buyer_name_2.setText("");
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
                    Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex1);
                }
                Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
    }

    @FXML
    private void printCollection(ActionEvent event) {
        try {
            String pdfFileName = "Report.pdf";
            String jrxmlFileName = "Collection.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFileName));
            Map map = new HashMap();
            map.put("voucherNo", voucher_no_field_2.getText());
            map.put("voucherDate", voucher_date_2.getText());
            map.put("buyerName", buyer_name_2.getText());
            map.put("billAmount", total_amount_field_2.getText());
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
            Rrc.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    private void updateLastVoucher() {
        try {
            String sql = "SELECT MAX(`Voucher No.`) from `COLLECTION_ENTRY_TABLE`;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            String answer = rs.getString("Max(`Voucher No.`)");
            last_voucher_field.setText("Last Voucher No. : " + answer);
        } catch (SQLException ex) {
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
