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
    DatabaseHandler databaseHandler;
    Connection conn1, conn2, conn3;
    PreparedStatement ps1, ps2, ps3;
    ResultSet rs1, rs2, rs3;
    String sql, sql2;

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
        try {
            // TODO
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            supplierList = new ArrayList();
            buyerList = new ArrayList();
            transportList = new ArrayList();
            databaseHandler = DatabaseHandler.getInstance();
            conn1 = databaseHandler.getConnection();
            conn2 = databaseHandler.getConnection();
            conn3 = databaseHandler.getConnection();
            ps1 = conn1.prepareStatement("select * from Supplier_Master_Table order by name collate nocase");
            ps2 = conn2.prepareStatement("select * from Buyer_Master_Table order by name collate nocase");
            ps3 = conn3.prepareStatement("select * from Transport_Master_Table order by name collate nocase");
            rs1 = ps1.executeQuery();
            rs2 = ps2.executeQuery();
            rs3 = ps3.executeQuery();
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (rs1.next()) {
                supplierList.add(rs1.getString("name"));
            }
            while (rs2.next()) {
                buyerList.add(rs2.getString("name"));
            }
            while (rs3.next()) {
                transportList.add(rs3.getString("name"));
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
            sql = "select * from `Bill_Entry_Table` where `Bill No.`=? collate nocase";
            ps1 = conn1.prepareStatement(sql);
            ps1.setString(1, bill_no_field_2.getText());
            rs1 = ps1.executeQuery();
            sql2 = "select * from `LR_Table` where `Bill No.`=? collate nocase";
            ps1 = conn1.prepareStatement(sql2);
            ps1.setString(1, bill_no_field_2.getText());
            rs2 = ps1.executeQuery();
            if (rs1.isClosed()) {
                Rrc.showAlert("Record not found. Please try again later. ", 2);
            } else {
                supplier_field_2.setText(rs1.getString("Supplier Name"));
                buyer_name_field_2.setText(rs1.getString("Buyer Name"));
                bill_date_field_2.setText(rs1.getString("Bill Date"));
                transport_field_2.setText(rs1.getString("Transport"));
                lr_date_field_2.setText(rs1.getString("LR Date"));
                bill_amount_field_2.setText(rs1.getString("Bill Amount"));
                list.clear();
                while (rs2.next()) {
                    list.add(new LR(rs2.getString("Bill No."), rs2.getString("LR No."), rs2.getString("PM")));
                }
                lr_table_2.setItems(list);
                delete_entry_btn.setDisable(false);
            }

        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want delete " + bill_no_field_2.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                conn1.setAutoCommit(false);
                sql = "DELETE FROM `Bill_Entry_Table` where `Bill No.`=? collate nocase";
                sql2 = "DELETE FROM `LR_Table` where `Bill No.`=? collate nocase";
                ps1 = conn1.prepareStatement(sql);
                ps1.setString(1, bill_no_field_2.getText());
                ps1.execute();
                ps1 = conn1.prepareStatement(sql2);
                ps1.setString(1, bill_no_field_2.getText());
                ps1.execute();
                supplier_field_2.setText("");
                buyer_name_field_2.setText("");
                bill_date_field_2.setText("");
                transport_field_2.setText("");
                lr_date_field_2.setText("");
                bill_amount_field_2.setText("");
                list.clear();
                conn1.commit();
                Rrc.showAlert(bill_no_field_2.getText().toUpperCase() + " Entry was successfully deleted.", 1);
            } catch (SQLException ex) {
                try {
                    conn1.rollback();
                } catch (SQLException ex1) {
                    Rrc.showAlert(ex1.toString());
                    Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Rrc.showAlert(ex.toString());
                Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
