package satyamconsignment.ui.Master.Transport;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.ui.Master.Supplier.SupplierController;

public class TransportController implements Initializable {

    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ObservableList<String> buyerList;

    @FXML
    private Button add_btn;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button delete_btn;
    @FXML
    private Button refresh_btn;
    @FXML
    private TextField add_field;
    @FXML
    private Button rename_btn;
    @FXML
    private TextField rename_field;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        buyerList = FXCollections.observableArrayList();
        refreshList();
    }

    @FXML
    private void addMaster(ActionEvent event) {
        if (add_field.getText().compareTo("") == 0) {
            Utils.showAlert(
                    "Add field is left blank. Please ensure to fill up the field properly.");
            return;
        }
        try {
            String sql = "INSERT INTO `Transport_Master_Table`(`Name`) VALUES (?);";
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, add_field.getText());
            ps.execute();
            refreshList();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(),
                    ex);
        }

    }

    @FXML
    private void deleteMaster(ActionEvent event) {
        if (listView.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Supplier to be deleted not selected. Please Retry");
            return;
        }
        try {
            String sql = "DELETE FROM `Transport_Master_Table` WHERE name=?";
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, listView.getSelectionModel().getSelectedItem());

            ps.execute();

            refreshList();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(),
                    ex);
        }

    }

    @FXML
    private void refreshList() {
        try {
            String sql = "select * from Transport_Master_Table order by name collate nocase";
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            buyerList.clear();
            while (rs.next()) {
                buyerList.add(rs.getString("name"));
            }
            listView.getItems().clear();
            listView.getItems().setAll(buyerList);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(),
                    ex);
        }
    }

    @FXML
    private void renameMaster(ActionEvent event) {
        if (rename_field.getText().compareTo("") == 0) {
            Utils.showAlert(
                    "Rename field is left blank. Please ensure to fill up the field properly.");
            return;
        }
        try {
            String sql = "UPDATE `Transport_Master_Table` SET Name=? WHERE Name=?";
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rename_field.getText());
            ps.setString(2, listView.getSelectionModel().getSelectedItem());

            ps.execute();
            Utils.showAlert("Success", 1);
            refreshList();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(),
                    ex);
        }

    }
}
