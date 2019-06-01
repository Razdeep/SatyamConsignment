
package satyamconsignment.ui.Input.BillEntry;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;
import satyamconsignment.ui.Main.MainController;


public class BillEntryController implements Initializable {
    Rrc rrc;
    @FXML
    private Group root;
    @FXML
    private TextField supplier_field;
    @FXML
    private TextField buyer_name_field;
    @FXML
    private TextField bill_no_field;
    @FXML
    private TextField transport_field;
    @FXML
    private JFXDatePicker date_field;
    @FXML
    private JFXDatePicker lr_date;
    @FXML
    private TextField bill_amount_field;
    @FXML
    private TextField pm_field;
    @FXML
    private TextField lr_field;
    @FXML
    private JFXButton save_btn;
    @FXML
    private JFXButton clear_btn;
    
    List<String> supplierList,buyerList,transportList;
    DatabaseHandler databaseHandler;
    Connection conn1,conn2,conn3;
    PreparedStatement ps1,ps2,ps3;
    ResultSet rs1,rs2,rs3;
    String sql,sql2;
    ObservableList<LR> list=FXCollections.observableArrayList();
    DateTimeFormatter formatter;
    
    @FXML
    private Button add_btn;
    private TableColumn<LR, String> lrNo;
    private TableColumn<LR, String> pm;
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
            rrc=new Rrc();
            formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
            supplierList=new ArrayList();
            buyerList=new ArrayList();
            transportList=new ArrayList();
            databaseHandler=DatabaseHandler.getInstance();
            conn1=databaseHandler.getConnection();
            conn2=databaseHandler.getConnection();
            conn3=databaseHandler.getConnection();
            ps1=conn1.prepareStatement("select * from Supplier_Master_Table order by name collate nocase");
            ps2=conn2.prepareStatement("select * from Buyer_Master_Table order by name collate nocase");
            ps3=conn3.prepareStatement("select * from Transport_Master_Table order by name collate nocase");
            rs1=ps1.executeQuery();
            rs2=ps2.executeQuery();
            rs3=ps3.executeQuery();
           } catch (SQLException ex) {
                rrc.showAlert(ex.toString());
                Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while(rs1.next())
            {
                supplierList.add(rs1.getString("name"));
            }
            while(rs2.next())
            {
                buyerList.add(rs2.getString("name"));
            }
            while(rs3.next())
            {
                transportList.add(rs3.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TextFields.bindAutoCompletion(supplier_field, supplierList);
        TextFields.bindAutoCompletion(buyer_name_field, buyerList);
        TextFields.bindAutoCompletion(transport_field, transportList);
        
        lr_no_col.setCellValueFactory(new PropertyValueFactory<>("lrNo"));
        pm_col.setCellValueFactory(new PropertyValueFactory<>("pm"));
        
        lr_no_col_2.setCellValueFactory(new PropertyValueFactory<>("lrNo"));
        pm_col_2.setCellValueFactory(new PropertyValueFactory<>("pm"));
    }    

    private void showInputScreen(ActionEvent event) {
        rrc=new Rrc();
        try {
            Parent parent=FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Input/Input.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
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
        list.clear();
    }

    @FXML
    private void addToList(ActionEvent event) {
        if(bill_no_field.getText().isEmpty()||lr_field.getText().isEmpty()||pm_field.getText().isEmpty())
        {
            rrc.showAlert("Please ensure that the Bill No., LR No. and PM fields are filled up properly.", 2);
        }
        else
        {
            list.add(new LR(bill_no_field.getText(),lr_field.getText(),pm_field.getText()));
            lrTable.setItems(list);
            lr_field.clear();
            pm_field.clear();
        }
    }

    @FXML
    private void save(ActionEvent event) {
       if(supplier_field.getText().isEmpty() || buyer_name_field.getText().isEmpty() 
                || bill_no_field.getText().isEmpty() || date_field.getValue().toString().isEmpty() 
                || transport_field.getText().isEmpty()||lr_date.getValue().toString().isEmpty()
                || bill_amount_field.getText().isEmpty() || list.isEmpty())
        {
            rrc.showAlert("Please ensure to fill up all the fields");
        } 
        else
        {
             /*  Code for saving data into Bill_Entry_Table */
            try {
                conn1.setAutoCommit(false);
                conn1=databaseHandler.getConnection();
                sql="INSERT INTO `Bill_Entry_Table`(`Supplier Name`,`Buyer Name`,`Bill No.`,`Bill Date`,`Transport`,`LR Date`,`Bill Amount`,`Collection Due`,`Due`) VALUES (?,?,?,?,?,?,?,?,?);";
                ps1=conn1.prepareStatement(sql);
                ps1.setString(1, supplier_field.getText());
                ps1.setString(2, buyer_name_field.getText());
                ps1.setString(3, bill_no_field.getText());
                ps1.setString(4, formatter.format(date_field.getValue()));
                ps1.setString(5, transport_field.getText());
                ps1.setString(6, formatter.format(lr_date.getValue()));
                ps1.setString(7, bill_amount_field.getText());
                ps1.setString(8, bill_amount_field.getText());
                ps1.setString(9, bill_amount_field.getText());
                ps1.execute();
                
                /* Code for Entering Data into LR_Table*/
               for(LR temp:list)
               {
                    sql="INSERT INTO `LR_Table`(`Bill No.`,`LR No.`,`PM`) VALUES (?,?,?)";
                    ps1=conn1.prepareStatement(sql);
                    ps1.setString(1, bill_no_field.getText());
                    ps1.setString(2, temp.getLrNo());
                    ps1.setString(3, temp.getPm());
                    ps1.execute();
                }
                clearAllFields();
                conn1.commit();
                rrc.showAlert("Saved Successfully",1);
            } catch (SQLException ex) {
                try {
                    conn1.rollback();
                } catch (SQLException ex1) {
                    rrc.showAlert(ex1.toString());
                    Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                rrc.showAlert(ex.toString());
                Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
              
      
    }
    @FXML
    private void replaceInList(ActionEvent event) {
        if(lrTable.getSelectionModel().getSelectedIndex()==-1)
        {
            rrc.showAlert("Please select an item from the LR Table",2);
        }
        else{
            if(bill_no_field.getText().isEmpty()||lr_field.getText().isEmpty()||pm_field.getText().isEmpty())
            {
                rrc.showAlert("Please ensure that the Bill No., LR No. and PM fields are filled up properly.", 2);
            }
            else
            {
                list.set(lrTable.getSelectionModel().getSelectedIndex(), new LR(bill_no_field.getText(),lr_field.getText(),pm_field.getText()));
                lr_field.clear();
                pm_field.clear();
            }
        }
    }

    @FXML
    private void deleteFromList(ActionEvent event) {
        if(lrTable.getSelectionModel().getSelectedIndex()==-1)
        {
            rrc.showAlert("Please select an item from the LR Table",2);
        }
        else{
            list.remove(lrTable.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void getDetails(ActionEvent event) {
        try {
            sql="select * from `Bill_Entry_Table` where `Bill No.`=? collate nocase";
            ps1=conn1.prepareStatement(sql);
            ps1.setString(1, bill_no_field_2.getText());
            rs1=ps1.executeQuery();
            sql2="select * from `LR_Table` where `Bill No.`=? collate nocase";
            ps1=conn1.prepareStatement(sql2);
            ps1.setString(1, bill_no_field_2.getText());
            rs2=ps1.executeQuery();
            if(rs1.isClosed())
            {
                rrc.showAlert("Record not found. Please try again later. ",2);
            }
            else{
                supplier_field_2.setText(rs1.getString("Supplier Name"));
                buyer_name_field_2.setText(rs1.getString("Buyer Name"));
                bill_date_field_2.setText(rs1.getString("Bill Date"));
                transport_field_2.setText(rs1.getString("Transport"));
                lr_date_field_2.setText(rs1.getString("LR Date"));
                bill_amount_field_2.setText(rs1.getString("Bill Amount"));
                list.clear();
                while(rs2.next())
                {
                    list.add(new LR(rs2.getString("Bill No."),rs2.getString("LR No."),rs2.getString("PM")));
                }
                lr_table_2.setItems(list);
                delete_entry_btn.setDisable(false);
            }
            
        } catch (SQLException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert=new Alert(AlertType.CONFIRMATION, "Are you sure that you want delete " + bill_no_field_2.getText()+" ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        
        if(alert.getResult()==ButtonType.YES)
        {
        try {
            conn1.setAutoCommit(false);
            sql="DELETE FROM `Bill_Entry_Table` where `Bill No.`=? collate nocase";
            sql2="DELETE FROM `LR_Table` where `Bill No.`=? collate nocase";
            ps1=conn1.prepareStatement(sql);
            ps1.setString(1, bill_no_field_2.getText());
            ps1.execute();
            ps1=conn1.prepareStatement(sql2);
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
            rrc.showAlert(bill_no_field_2.getText().toUpperCase()+" Entry was successfully deleted.",1);
        } catch (SQLException ex) {
            try {
                conn1.rollback();
            } catch (SQLException ex1) {
                rrc.showAlert(ex1.toString());
                Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex1);
            }
            rrc.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
    }
}
