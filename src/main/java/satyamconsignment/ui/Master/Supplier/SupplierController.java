package satyamconsignment.ui.Master.Supplier;

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

public class SupplierController implements Initializable {

    ObservableList<String> supplierList;

    @FXML
    private Button add_btn;

    @FXML
    private TextField add_field;

    @FXML
    private Button rename_btn;

    @FXML
    private TextField rename_field;

    @FXML
    private Button delete_btn;

    @FXML
    private Button refresh_btn;

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        supplierList = FXCollections.observableArrayList();
        refreshList();
    }

    @FXML
    private void addMaster(ActionEvent event) {
        if (add_field.getText().compareTo("") == 0) {
            Utils.showAlert("Add field is left blank. Please ensure to fill up the field properly.");
            return;
        }
        try {
            String sql = "INSERT INTO `Supplier_Master_Table`(`Name`) VALUES (?);";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, add_field.getText());
            ps.execute();
            refreshList();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void renameMaster(ActionEvent event) {

        if (rename_field.getText().compareTo("") == 0) {
            Utils.showAlert("Rename field is left blank. Please ensure to fill up the field properly.");
            return;
        }
        try {
            String sql = "UPDATE `Supplier_Master_Table` SET Name=? WHERE Name=?";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rename_field.getText());
            ps.setString(2, listView.getSelectionModel().getSelectedItem());

            ps.execute();
            Utils.showAlert("Success", 1);
            refreshList();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void deleteMaster(ActionEvent event) {
        if (listView.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Supplier to be deleted not selected. Please Retry");
            return;
        }
        try {
            String sql = "DELETE FROM `Supplier_Master_Table` WHERE name=?";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, listView.getSelectionModel().getSelectedItem());

            ps.execute();

            refreshList();
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void refreshList() {
        try {
            String sql = "select * from Supplier_Master_Table order by name collate nocase";
            Connection conn = DatabaseHandler.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            supplierList.clear();
            while (rs.next()) {
                supplierList.add(rs.getString("name"));
            }
            listView.getItems().clear();
            listView.getItems().setAll(supplierList);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
