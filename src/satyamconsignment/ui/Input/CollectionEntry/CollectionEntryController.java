
package satyamconsignment.ui.Input.CollectionEntry;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;


public class CollectionEntryController implements Initializable {

    
    @FXML
    private TextField dd_no_field;
    @FXML
    private TextField gr_field;
    @FXML
    private TextField rd_field;
    @FXML
    private TextField cd_field;
    @FXML
    private TextField bc_field;
    @FXML
    private JFXDatePicker dd_date_field;
    @FXML
    private JFXButton back_btn;
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
    private TableColumn<Collection, String> gr_col;
    @FXML
    private TableColumn<Collection, String> rd_col;
    @FXML
    private TableColumn<Collection, String> cd_col;
    @FXML
    private TableColumn<Collection, String> bc_col;
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
    String buyerName,supplierName,billDate,billAmount;
    DateTimeFormatter formatter;
    int previouslyDue;
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
    private TableColumn<Collection, String> gr_col_2;
    @FXML
    private TableColumn<Collection, String> rd_col_2;
    @FXML
    private TableColumn<Collection, String> cd_col_2;
    @FXML
    private TableColumn<Collection, String> bc_col_2;
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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rrc=new Rrc();
        totalAmount=0;
        formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        databaseHandler=DatabaseHandler.getInstance();
        conn=databaseHandler.getConnection();
        
        list=FXCollections.observableArrayList();
        buyerNameComboList=FXCollections.observableArrayList();
        billNoComboList=FXCollections.observableArrayList();
        bill_no_col.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory("billAmount"));
        supplier_col.setCellValueFactory(new PropertyValueFactory("supplierName"));
        gr_col.setCellValueFactory(new PropertyValueFactory("gr"));
        rd_col.setCellValueFactory(new PropertyValueFactory("rd"));
        cd_col.setCellValueFactory(new PropertyValueFactory("cd"));
        bc_col.setCellValueFactory(new PropertyValueFactory("bc"));
        amount_collection_col.setCellValueFactory(new PropertyValueFactory("amountCollected"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory("ddNo"));
        bank_col.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory("ddDate"));
        collection_tableview.setItems(list);
        fillBuyerNameCombo();
        
        bill_no_col_2.setCellValueFactory(new PropertyValueFactory("billNo"));
        bill_amt_col_2.setCellValueFactory(new PropertyValueFactory("billAmount"));
        supplier_col_2.setCellValueFactory(new PropertyValueFactory("supplierName"));
        bill_date_col_2.setCellValueFactory(new PropertyValueFactory("billDate"));
        gr_col_2.setCellValueFactory(new PropertyValueFactory("gr"));
        rd_col_2.setCellValueFactory(new PropertyValueFactory("rd"));
        cd_col_2.setCellValueFactory(new PropertyValueFactory("cd"));
        bc_col_2.setCellValueFactory(new PropertyValueFactory("bc"));
        amount_collection_col_2.setCellValueFactory(new PropertyValueFactory("amountCollected"));
        dd_no_col_2.setCellValueFactory(new PropertyValueFactory("ddNo"));
        bank_col_2.setCellValueFactory(new PropertyValueFactory("bank"));
        dd_date_col_2.setCellValueFactory(new PropertyValueFactory("ddDate"));
    }    

    @FXML
    private void fetchData(ActionEvent event) {
        try {
            
            sql="Select * from Bill_Entry_Table where `Bill No.`=?";
            ps=conn.prepareStatement(sql);
            ps.setString(1, bill_no_combo.getValue());
            rs=ps.executeQuery();
            buyerName=rs.getString("Buyer Name");
            supplierName=rs.getString("Supplier Name");
            billDate=rs.getString("Bill Date");
            billAmount=rs.getString("Bill Amount");
            previouslyDue=Integer.parseInt(rs.getString("Collection Due"));
            supplier_name_view.setText(supplierName);
            bill_date_view.setText(billDate);
            bill_amount_view.setText(billAmount);
            updateCollectionDue();
            buyer_name_view.setDisable(true);
            
        } catch (SQLException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addCollection(ActionEvent event) {
        if(bill_no_combo.getValue().isEmpty()||billAmount.isEmpty()||buyerName.isEmpty()||supplierName.isEmpty()||bank_field.getText().isEmpty()||
                gr_field.getText().isEmpty()||rd_field.getText().isEmpty()||cd_field.getText().isEmpty()||bc_field.getText().isEmpty()||
                collection_due_field.getText().isEmpty()||amount_collected_field.getText().isEmpty()||dd_no_field.getText().isEmpty()||dd_date_field.getValue().toString().isEmpty())
                {
                    rrc.showAlert("Please check whether the fields are properly filled or not.",2);
                }
                else{
                    list.add(new Collection(bill_no_combo.getValue(),bill_date_view.getText(),billAmount,supplierName,
                    gr_field.getText(),rd_field.getText(),cd_field.getText(),bc_field.getText(),collection_due_field.getText(),amount_collected_field.getText(),
                    bank_field.getText(),dd_no_field.getText(),formatter.format(dd_date_field.getValue())));
                    updateTotalAmount();
                    clearRepeatingFields();
                    
                }
    }

    @FXML
    private void replaceCollection(ActionEvent event) {
        if(collection_tableview.getSelectionModel().getSelectedIndex()==-1)
        {
            rrc.showAlert("Please select an item from the Collection Table",2);
        }
       else {
        if(bill_no_combo.getValue().isEmpty()||billAmount.isEmpty()||buyerName.isEmpty()||supplierName.isEmpty()||bank_field.getText().isEmpty()||
                gr_field.getText().isEmpty()||rd_field.getText().isEmpty()||cd_field.getText().isEmpty()||bc_field.getText().isEmpty()||
                collection_due_field.getText().isEmpty()||amount_collected_field.getText().isEmpty()||dd_no_field.getText().isEmpty()||dd_date_field.getValue().toString().isEmpty())
                {
                    rrc.showAlert("Please check whether the fields are properly filled or not.",2);
                }
                else{
            list.set(collection_tableview.getSelectionModel().getSelectedIndex(),
                    new Collection(bill_no_combo.getValue(),bill_date_view.getText(),billAmount,supplierName,
                    gr_field.getText(),rd_field.getText(),cd_field.getText(),bc_field.getText(),collection_due_field.getText(),
                    amount_collected_field.getText(),bank_field.getText(),dd_no_field.getText(),dd_date_field.getValue().toString()));
            updateTotalAmount();
                    }
    }
    }

    @FXML
    private void deleteCollection(ActionEvent event) {
        if(collection_tableview.getSelectionModel().getSelectedIndex()==-1)
        {
            rrc.showAlert("Please select an item from the LR Table",2);
        }
        else{
            list.remove(collection_tableview.getSelectionModel().getSelectedIndex());
            updateTotalAmount();
        }
    }

    private void clearRepeatingFields() {
        //bill_no_combo.setValue("");
        buyerName=null;
        billDate=null;
        supplierName=null;
        billAmount=null;
        //buyer_name_view.setText("");
        bill_date_view.setText("");
        supplier_name_view.setText("");
        bill_amount_view.setText("");
        gr_field.setText("0");
        rd_field.setText("0");
        cd_field.setText("0");
        bc_field.setText("0");
        collection_due_field.setText("0");
        amount_collected_field.setText("");
        bank_field.setText("");
        dd_no_field.setText("");
        
        dd_date_field.setValue(null);
    }
    private void clearAllFields() {
        clearRepeatingFields();
        //error
        voucher_no_field.setText("");
        voucher_date_field.setValue(null);
        buyer_name_view.setDisable(false);
        //buyer_name_view.setValue(null);
        billNoComboList.clear();
        //bill_no_combo.setValue(null);
        
    }

    @FXML
    private void updateCollectionDue() {
        collection_due_field.setText(Integer.toString(previouslyDue
                -Integer.parseInt(gr_field.getText())
                -Integer.parseInt(rd_field.getText())
                -Integer.parseInt(cd_field.getText())
                -Integer.parseInt(bc_field.getText())
                -Integer.parseInt(amount_collected_field.getText())));
    }

    private void fillBuyerNameCombo() {
        try {
            
            sql="select Name from `Buyer_Master_Table` order by name collate nocase";
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            
            buyerNameComboList.clear();
            while(rs.next())
            {
                buyerNameComboList.add(rs.getString("Name"));
            }
            
            
            buyer_name_view.setItems(buyerNameComboList);
            
        } catch (SQLException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void fillBillNoCombo() {
        try {
            
            sql="select `Bill No.` from `Bill_Entry_Table` where `Buyer Name`=? and not `Collection Due`='0' order by `Bill No.` collate nocase";
            
            ps=conn.prepareStatement(sql);
            //rrc.showAlert(buyer_name_view.getValue());
            ps.setString(1, buyer_name_view.getValue());
            rs=ps.executeQuery();
            
            billNoComboList.clear();
            while(rs.next())
            {
                billNoComboList.add(rs.getString("Bill No."));
            }
            bill_no_combo.setItems(billNoComboList);
            display_board_label.setText(buyer_name_view.getValue());
            
        } catch (SQLException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void saveData(ActionEvent event) {
        if(voucher_no_field.getText().isEmpty()||voucher_date_field.getValue().toString().isEmpty())
        {
            rrc.showAlert("Check whether the Voucher No. and the Voucher Date is properly filled",2);
        }
        else{
            try {
                conn.setAutoCommit(false);
                sql="INSERT INTO `Collection_Entry_Table`(`Voucher No.`,`Voucher Date`,`Buyer Name`,`Total Amount`) VALUES (?,?,?,?)";
                ps=conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field.getText());
                ps.setString(2, formatter.format(voucher_date_field.getValue()));
                ps.setString(3, buyer_name_view.getValue());
                ps.setString(4, total_amount_field.getText());
                ps.execute();
                
                for(Collection temp:list)
                {
                    sql="INSERT INTO `Collection_Entry_Extended_Table`(`Voucher No.`,`Supplier Name`,`Bill No.`,`Bill Date`,`Bill Amount`,`GR`,`RD`,`CD`,`BC`,`Collection Due`,`Amount Collected`,`Bank`,`DD No.`,`DD Date`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                    ps=conn.prepareStatement(sql);
                    ps.setString(1, voucher_no_field.getText());
                    ps.setString(2, temp.getSupplierName());
                    ps.setString(3, temp.getBillNo());
                    ps.setString(4, temp.getBillDate());
                    ps.setString(5, temp.getBillAmount());
                    ps.setString(6, temp.getGr());
                    ps.setString(7, temp.getRd());
                    ps.setString(8, temp.getCd());
                    ps.setString(9, temp.getBc());
                    ps.setString(10, temp.getDue());
                    ps.setString(11, temp.getAmountCollected());
                    ps.setString(12, temp.getBank());
                    ps.setString(13, temp.getDdNo());
                    ps.setString(14, temp.getDdDate());
                    
                    ps.execute();
                    
                    //Update Bill Entry Table
                    sql="UPDATE `Bill_Entry_Table` SET `Collection Due`=? WHERE `BILL NO.`=?";
                    ps=conn.prepareStatement(sql);
                    ps.setString(1, temp.getDue());
                    ps.setString(2, temp.getBillNo());
                    ps.execute();
                }
                list.clear();
                conn.commit();
                rrc.showAlert("Saved Successfully",1);
                //clearAllFields();
                loadScreenAgain();
              } catch (SQLException ex) {
                  rrc.showAlert(ex.toString());
                try {
                    conn.rollback();
                } catch (SQLException ex1) {
                    rrc.showAlert(ex.toString());
                    Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void getDetails(ActionEvent event) {
        
        if(voucher_no_field_2.getText().isEmpty())
        {
            rrc.showAlert("Voucher No. Field is kept empty. Please fill the voucher no.");
        }
        else
        {
            try {
                sql="select * from `Collection_Entry_Table` where `Voucher No.`=? collate nocase";
                ps=conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs=ps.executeQuery();
                
                voucher_date_2.setText(rs.getString("Voucher Date"));
                buyer_name_2.setText(rs.getString("Buyer Name"));
                total_amount_field_2.setText(rs.getString("Total Amount"));
                display_board_label_2.setText(rs.getString("Buyer Name"));
                
                list.clear();
                sql="select * from `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
                ps=conn.prepareStatement(sql);
                ps.setString(1, voucher_no_field_2.getText());
                rs=ps.executeQuery();
                if(rs.isClosed())
                {
                    rrc.showAlert("No Results found",1);
                }
                else{
                    while(rs.next())
                    {
                        list.add(new Collection(rs.getString("Bill No."),rs.getString("Bill Date"),rs.getString("Bill Amount"),
                                                rs.getString("Supplier Name"),rs.getString("gr"),
                                                rs.getString("rd"),rs.getString("cd"),rs.getString("bc"),
                                                rs.getString("collection due"),rs.getString("amount collected"),
                                                rs.getString("bank"),rs.getString("DD No."),
                                                rs.getString("DD Date")));
                    }
                    collection_tableview_2.setItems(list);
                    delete_entry_btn.setDisable(false);
                }
            } catch (SQLException ex) {
                rrc.showAlert(ex.toString());
                Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert=new Alert(AlertType.CONFIRMATION, "Are you sure that you want delete " + voucher_no_field_2.getText()+" ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        
        if(alert.getResult()==ButtonType.YES)
        {
        try {
            //EDIT
            conn.setAutoCommit(false);
            sql="SELECT `Bill No.`,`Amount Collected`,`GR`,`RD`,`CD`,`BC` FROM `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
            ps=conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field_2.getText());
            rs=ps.executeQuery();
            while(rs.next())
            {
                sql="UPDATE `Bill_Entry_Table` SET `Collection Due`=`Collection Due`+?+?+?+?+? WHERE `BILL NO.`=?";
                ps=conn.prepareStatement(sql);
                ps.setString(1, rs.getString("Amount Collected"));
                ps.setString(2, rs.getString("GR"));
                ps.setString(3, rs.getString("RD"));
                ps.setString(4, rs.getString("CD"));
                ps.setString(5, rs.getString("BC"));
                ps.setString(6, rs.getString("Bill No."));
                ps.execute();
            }
            
            
            sql="DELETE FROM `Collection_Entry_Table` where `Voucher No.`=? collate nocase";
            
            ps=conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field_2.getText());
            ps.execute();
            
            sql="DELETE FROM `Collection_Entry_Extended_Table` where `Voucher No.`=? collate nocase";
            
            ps=conn.prepareStatement(sql);
            ps.setString(1, voucher_no_field_2.getText());
            ps.execute();
            list.clear();
            voucher_date_2.setText("");
            buyer_name_2.setText("");
            display_board_label_2.setText("");
            total_amount_field_2.setText("");
            conn.commit();
            rrc.showAlert(voucher_no_field_2.getText().toUpperCase()+" Entry was successfully deleted.",1);
        } catch (SQLException ex) {
            rrc.showAlert(ex.toString());
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                rrc.showAlert(ex1.toString());
                Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }

    private void updateTotalAmount() {
        totalAmount=0;
        for(Collection temp: list)
        {
            totalAmount+=Integer.parseInt(temp.getAmountCollected());
        }
        total_amount_field.setText(Integer.toString(totalAmount));
    }

    private void loadScreenAgain() {
        try {
            Parent parent=FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Input/CollectionEntry/CollectionEntry.fxml"));
            root2.getChildren().setAll(parent);
        } catch (IOException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(CollectionEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
