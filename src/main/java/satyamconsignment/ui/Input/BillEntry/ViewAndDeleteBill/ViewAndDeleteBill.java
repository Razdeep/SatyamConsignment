package satyamconsignment.ui.Input.BillEntry.ViewAndDeleteBill;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.model.LR;

public class ViewAndDeleteBill implements Initializable {

    private static final Logger logger = Logger.getLogger(ViewAndDeleteBill.class.getName());

    private List<String> supplierList, buyerList, transportList;
    private List<LR> lrpmList;
    private DateTimeFormatter formatter;

    @FXML
    private Group root;

    @FXML
    private TextField supplier_field;
    @FXML
    private TableView<LR> lr_table;
    @FXML
    private TableColumn<LR, String> lr_no_col;
    @FXML
    private TableColumn<LR, String> pm_col;
    @FXML
    private TextField buyer_name_field;
    @FXML
    private TextField bill_date_field;
    @FXML
    private TextField transport_field;
    @FXML
    private TextField bill_amount_field;
    @FXML
    private TextField lr_date_field;
    @FXML
    private Button get_details_btn;
    @FXML
    private Button delete_entry_btn;
    @FXML
    private TextField bill_no_field;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        supplierList = new ArrayList<>();
        buyerList = new ArrayList<>();
        transportList = new ArrayList<>();
        lrpmList = new ArrayList<>();

        try {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
            Connection connection = databaseHandler.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from Supplier_Master_Table order by name collate nocase");
            ResultSet supplierListResultSet = preparedStatement.executeQuery();

            preparedStatement = connection.prepareStatement(
                    "select * from Buyer_Master_Table order by name collate nocase");
            ResultSet buyerListResultSet = preparedStatement.executeQuery();

            preparedStatement = connection.prepareStatement(
                    "select * from Transport_Master_Table order by name collate nocase");
            ResultSet transportListResultSet = preparedStatement.executeQuery();

            while (!supplierListResultSet.isClosed()) {
                supplierList.add(supplierListResultSet.getString("name"));
                supplierListResultSet.next();
            }
            while (!buyerListResultSet.isClosed()) {
                buyerList.add(buyerListResultSet.getString("name"));
                buyerListResultSet.next();
            }
            while (!transportListResultSet.isClosed()) {
                transportList.add(transportListResultSet.getString("name"));
                transportListResultSet.next();
            }

        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            logger.log(Level.SEVERE, ex.toString(), ex);
        }
        lr_no_col.setCellValueFactory(new PropertyValueFactory<>("lrNo"));
        pm_col.setCellValueFactory(new PropertyValueFactory<>("pm"));
    }

    @FXML
    private void getDetails(ActionEvent event) {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "select * from `Bill_Entry_Table` where `Bill No.`=? collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bill_no_field.getText());
            ResultSet billResultSet = preparedStatement.executeQuery();
            String sql2 = "select * from `LR_Table` where `Bill No.`=? collate nocase";
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setString(1, bill_no_field.getText());
            ResultSet lrPmResultSet = preparedStatement.executeQuery();

            if (billResultSet.isClosed()) {
                Utils.showAlert("Record not found. Please try again later.", 2);
                return;
            }

            supplier_field.setText(billResultSet.getString("Supplier Name"));
            buyer_name_field.setText(billResultSet.getString("Buyer Name"));
            bill_date_field.setText(billResultSet.getString("Bill Date"));
            transport_field.setText(billResultSet.getString("Transport"));
            lr_date_field.setText(billResultSet.getString("LR Date"));
            bill_amount_field.setText(billResultSet.getString("Bill Amount"));
            lrpmList.clear();
            while (lrPmResultSet.next()) {
                lrpmList.add(new LR(lrPmResultSet.getString("Bill No."),
                        lrPmResultSet.getString("LR No."), lrPmResultSet.getString("PM")));
            }
            lr_table.setItems(FXCollections.observableArrayList(lrpmList));
            delete_entry_btn.setDisable(false);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure that you want delete " + bill_no_field.getText() + " ?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() != ButtonType.YES) {
            return;
        }

        Connection connection = DatabaseHandler.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            String sql = "DELETE FROM `Bill_Entry_Table` where `Bill No.`=? collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bill_no_field.getText());
            preparedStatement.execute();

            String sql2 = "DELETE FROM `LR_Table` where `Bill No.`=? collate nocase";
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setString(1, bill_no_field.getText());
            preparedStatement.execute();
            supplier_field.setText("");
            buyer_name_field.setText("");
            bill_date_field.setText("");
            transport_field.setText("");
            lr_date_field.setText("");
            bill_amount_field.setText("");
            lrpmList.clear();
            connection.commit();
            Utils.showAlert(
                    bill_no_field.getText().toUpperCase() + " Entry was successfully deleted.", 1);
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Utils.showAlert(ex1.toString());
                logger.log(Level.SEVERE, ex1.toString(), ex1);
            }
            Utils.showAlert(ex.toString());
            logger.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    public void updateBill(ActionEvent actionEvent) {
        Connection connection = DatabaseHandler.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String sql = "UPDATE `Bill_Entry_Table` SET `Supplier Name`=?,`Buyer Name`=?,`Bill Date`=?,"
                    + "`Transport`=?,`LR Date`=?,`Bill Amount`=?,`Collection Due`=?,`Due`=? WHERE `Bill No.`=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, supplier_field.getText());
            preparedStatement.setString(2, buyer_name_field.getText());
            preparedStatement.setString(3, bill_date_field.getText());
            preparedStatement.setString(4, transport_field.getText());
            preparedStatement.setString(5, lr_date_field.getText());
            preparedStatement.setString(6, bill_amount_field.getText());
            preparedStatement.setString(7, bill_amount_field.getText());
            preparedStatement.setString(8, bill_amount_field.getText());
            preparedStatement.setString(9, bill_no_field.getText());
            preparedStatement.execute();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            logger.log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
