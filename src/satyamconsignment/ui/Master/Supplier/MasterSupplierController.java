package satyamconsignment.ui.Master.Supplier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MasterSupplierController implements Initializable {

    Rrc rrc;
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    //List<String> supplierList;
    ObservableList<String> supplierList;
    String temp;
    @FXML
    private JFXButton add_btn;
    @FXML
    private JFXTextField add_field;
    @FXML
    private JFXButton rename_btn;
    @FXML
    private JFXTextField rename_field;
    @FXML
    private JFXButton delete_btn;
    @FXML
    private JFXButton refresh_btn;
    @FXML
    private JFXListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        supplierList = FXCollections.observableArrayList();
        rrc = new Rrc();
        refreshList();
    }

    @FXML
    private void addMaster(ActionEvent event) {
        if (add_field.getText().compareTo("") == 0) {
            Rrc.showAlert("Add field is left blank. Please ensure to fill up the field properly.");

        } else {
            try {
                String sql = "INSERT INTO `Supplier_Master_Table`(`Name`) VALUES (?);";
                conn = databaseHandler.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setString(1, add_field.getText());
                ps.execute();
                refreshList();
            } catch (SQLException ex) {
                Rrc.showAlert(ex.toString());
                Logger.getLogger(MasterSupplierController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //
    //
    //Bugs to be fixed in rename master
    //
    //
    @FXML
    private void renameMaster(ActionEvent event) {

        if (rename_field.getText().compareTo("") == 0) {
            Rrc.showAlert("Rename field is left blank. Please ensure to fill up the field properly.");
        } else {
            try {
                String sql = "UPDATE `Supplier_Master_Table` SET Name=? WHERE Name=?";
                conn = databaseHandler.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setString(1, rename_field.getText());
                ps.setString(2, listView.getSelectionModel().getSelectedItem());

                //rrc.showAlert(listView.getSelectionModel().getSelectedItem(),1);
                boolean execute = false;
                ps.execute();
                if (execute == true) {
                    Rrc.showAlert("Success", 1);
                }
                refreshList();
            } catch (SQLException ex) {
                Rrc.showAlert(ex.toString());
                Logger.getLogger(MasterSupplierController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteMaster(ActionEvent event) {
        if (listView.getSelectionModel().getSelectedItem() == null) {
            Rrc.showAlert("Supplier to be deleted not selected. Please Retry");
        } else {
            try {
                String sql = "DELETE FROM `Supplier_Master_Table` WHERE name=?";
                conn = databaseHandler.getConnection();
                ps = conn.prepareStatement(sql);

                ps.setString(1, listView.getSelectionModel().getSelectedItem());


                ps.execute();

                refreshList();
            } catch (SQLException ex) {
                Rrc.showAlert(ex.toString());
                Logger.getLogger(MasterSupplierController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void refreshList() {
        try {
            String sql = "select * from Supplier_Master_Table order by name collate nocase";
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            supplierList.clear();
            while (rs.next()) {
                supplierList.add(rs.getString("name"));
            }
            listView.getItems().clear();
            listView.getItems().setAll(supplierList);
        } catch (SQLException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(MasterSupplierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
