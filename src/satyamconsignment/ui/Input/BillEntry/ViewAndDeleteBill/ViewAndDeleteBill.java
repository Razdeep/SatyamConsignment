package satyamconsignment.ui.Input.BillEntry.ViewAndDeleteBill;

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
import satyamconsignment.ui.Input.BillEntry.BillEntryController;
import satyamconsignment.ui.Input.BillEntry.LR;
import satyamconsignment.ui.Main.MainController;

import java.io.IOException;
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

public class ViewAndDeleteBill implements Initializable {
    List<String> supplierList, buyerList, transportList;
    ObservableList<LR> list = FXCollections.observableArrayList();
    DateTimeFormatter formatter;
    @FXML
    private Group root;

    @FXML
    private TextField supplier_field_2;
    @FXML
    private TableView<LR> lr_table_2;
    @FXML
    private TableColumn<LR, String> lr_no_col_2;
    @FXML
    private TableColumn<LR, String> pm_col_2;
    @FXML
    private TextField buyer_name_field_2;
    @FXML
    private TextField bill_date_field_2;
    @FXML
    private TextField transport_field_2;
    @FXML
    private TextField bill_amount_field_2;
    @FXML
    private TextField lr_date_field_2;
    @FXML
    private Button get_details_btn;
    @FXML
    private Button delete_entry_btn;
    @FXML
    private TextField bill_no_field_2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResultSet supplierListResultSet = null, buyerListResultSet = null, transportListResultSet = null;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
            Connection connection = databaseHandler.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("select * from Supplier_Master_Table order by name collate nocase");
            supplierListResultSet = preparedStatement.executeQuery();

            preparedStatement = connection.prepareStatement("select * from Buyer_Master_Table order by name collate nocase");
            buyerListResultSet = preparedStatement.executeQuery();

            preparedStatement = connection.prepareStatement("select * from Transport_Master_Table order by name collate nocase");
            transportListResultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            supplierList = new ArrayList();
            buyerList = new ArrayList();
            transportList = new ArrayList();
            while (supplierListResultSet.next()) {
                supplierList.add(supplierListResultSet.getString("name"));
            }
            while (buyerListResultSet.next()) {
                buyerList.add(buyerListResultSet.getString("name"));
            }
            while (transportListResultSet.next()) {
                transportList.add(transportListResultSet.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lr_no_col_2.setCellValueFactory(new PropertyValueFactory<>("lrNo"));
        pm_col_2.setCellValueFactory(new PropertyValueFactory<>("pm"));
    }

    private void showInputScreen(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Input/Input.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void getDetails(ActionEvent event) {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String sql = "select * from `Bill_Entry_Table` where `Bill No.`=? collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bill_no_field_2.getText());
            ResultSet billResultSet = preparedStatement.executeQuery();
            String sql2 = "select * from `LR_Table` where `Bill No.`=? collate nocase";
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setString(1, bill_no_field_2.getText());
            ResultSet lrPmResultSet = preparedStatement.executeQuery();

            if (billResultSet.isClosed()) {
                Rrc.showAlert("Record not found. Please try again later.", 2);
                return;
            }

            supplier_field_2.setText(billResultSet.getString("Supplier Name"));
            buyer_name_field_2.setText(billResultSet.getString("Buyer Name"));
            bill_date_field_2.setText(billResultSet.getString("Bill Date"));
            transport_field_2.setText(billResultSet.getString("Transport"));
            lr_date_field_2.setText(billResultSet.getString("LR Date"));
            bill_amount_field_2.setText(billResultSet.getString("Bill Amount"));
            list.clear();
            while (lrPmResultSet.next()) {
                list.add(new LR(lrPmResultSet.getString("Bill No."), lrPmResultSet.getString("LR No."), lrPmResultSet.getString("PM")));
            }
            lr_table_2.setItems(list);
            delete_entry_btn.setDisable(false);
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want delete " + bill_no_field_2.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {
            return;
        }

        Connection connection = DatabaseHandler.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            String sql = "DELETE FROM `Bill_Entry_Table` where `Bill No.`=? collate nocase";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bill_no_field_2.getText());
            preparedStatement.execute();

            String sql2 = "DELETE FROM `LR_Table` where `Bill No.`=? collate nocase";
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setString(1, bill_no_field_2.getText());
            preparedStatement.execute();
            supplier_field_2.setText("");
            buyer_name_field_2.setText("");
            bill_date_field_2.setText("");
            transport_field_2.setText("");
            lr_date_field_2.setText("");
            bill_amount_field_2.setText("");
            list.clear();
            connection.commit();
            Rrc.showAlert(bill_no_field_2.getText().toUpperCase() + " Entry was successfully deleted.", 1);
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Rrc.showAlert(ex1.toString());
                Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Rrc.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
