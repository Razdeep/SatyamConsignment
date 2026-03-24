package satyamconsignment.ui.Input.BillEntry.AddBill;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.entity.BillEntity;
import satyamconsignment.entity.LREntity;
import satyamconsignment.model.LR;
import satyamconsignment.repository.BillRepository;
import satyamconsignment.service.BillService;
import satyamconsignment.ui.Input.BillEntry.BillEntryController;

public class AddBill implements Initializable {

    @FXML
    private TextField supplier_field;

    @FXML
    private TextField buyer_name_field;

    @FXML
    private TextField bill_no_field;

    @FXML
    private TextField transport_field;

    @FXML
    private DatePicker date_field;

    @FXML
    private DatePicker lr_date;

    @FXML
    private TextField bill_amount_field;

    @FXML
    private TextField pm_field;

    @FXML
    private TextField lr_field;

    @FXML
    private Button save_btn;

    @FXML
    private Button clear_btn;

    @FXML
    private Button add_btn;

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

    final ObservableList<LR> lrList = FXCollections.observableArrayList();

    private BillService billService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ArrayList<String> supplierList = new ArrayList<>();
            ArrayList<String> buyerList = new ArrayList<>();
            ArrayList<String> transportList = new ArrayList<>();
            Connection connection = DatabaseHandler.getInstance().getConnection();
            ResultSet supplierResultSet = connection
                    .prepareStatement("select * from Supplier_Master_Table order by name collate nocase")
                    .executeQuery();
            ResultSet buyerResultSet = connection
                    .prepareStatement("select * from Buyer_Master_Table order by name collate nocase")
                    .executeQuery();
            ResultSet transportResultSet = connection
                    .prepareStatement("select * from Transport_Master_Table order by name collate nocase")
                    .executeQuery();
            while (supplierResultSet.next()) {
                supplierList.add(supplierResultSet.getString("name"));
            }
            while (buyerResultSet.next()) {
                buyerList.add(buyerResultSet.getString("name"));
            }
            while (transportResultSet.next()) {
                transportList.add(transportResultSet.getString("name"));
            }

            TextFields.bindAutoCompletion(supplier_field, supplierList);
            TextFields.bindAutoCompletion(buyer_name_field, buyerList);
            TextFields.bindAutoCompletion(transport_field, transportList);

            lr_no_col.setCellValueFactory(new PropertyValueFactory<>("lrNo"));
            pm_col.setCellValueFactory(new PropertyValueFactory<>("pm"));

            billService = new BillService(new BillRepository());

        } catch (Exception ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void addToList(ActionEvent event) {
        if (bill_no_field.getText().isEmpty()
                || lr_field.getText().isEmpty()
                || pm_field.getText().isEmpty()) {
            Utils.showAlert("Please ensure that the Bill No., LR No. and PM fields are filled up properly.", 2);
            return;
        }
        lrList.add(new LR(bill_no_field.getText(), lr_field.getText(), pm_field.getText()));
        lrTable.setItems(lrList);
        lr_field.clear();
        pm_field.clear();
    }

    @FXML
    private void save(ActionEvent event) {
        if (supplier_field.getText().isEmpty()
                || buyer_name_field.getText().isEmpty()
                || bill_no_field.getText().isEmpty()
                || date_field.getValue().toString().isEmpty()
                || transport_field.getText().isEmpty()
                || lr_date.getValue().toString().isEmpty()
                || bill_amount_field.getText().isEmpty()
                || lrList.isEmpty()) {
            Utils.showAlert("Please ensure to fill up all the fields");
            return;
        }

        List<LREntity> lrEntityList = lrList.stream()
                .map(it -> new LREntity(it.getBillNo(), it.getLrNo(), it.getPm()))
                .toList();

        BillEntity billEntity = BillEntity.builder()
                .supplierName(supplier_field.getText())
                .buyerName(buyer_name_field.getText())
                .billNo(bill_no_field.getText())
                .billDate(date_field.getValue().toString())
                .transport(transport_field.getText())
                .lrDate(lr_date.getValue().toString())
                .billAmount(bill_amount_field.getText())
                .lrEntities(lrEntityList)
                .build();
        try {
            billService.saveBill(billEntity);
            clearAllFields();
            Utils.showAlert("Saved Successfully", 1);
        } catch (Exception ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(BillEntryController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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
        lrList.clear();
    }

    @FXML
    private void replaceInList(ActionEvent event) {
        if (lrTable.getSelectionModel().getSelectedIndex() == -1) {
            Utils.showAlert("Please select an item from the LR Table", 2);
            return;
        }
        if (bill_no_field.getText().isEmpty()
                || lr_field.getText().isEmpty()
                || pm_field.getText().isEmpty()) {
            Utils.showAlert("Please ensure that the Bill No., LR No. and PM fields are filled up properly.", 2);
            return;
        }
        lrList.set(
                lrTable.getSelectionModel().getSelectedIndex(),
                new LR(bill_no_field.getText(), lr_field.getText(), pm_field.getText()));
        lr_field.clear();
        pm_field.clear();
    }

    @FXML
    private void deleteFromList(ActionEvent event) {
        if (lrTable.getSelectionModel().getSelectedIndex() == -1) {
            Utils.showAlert("Please select an item from the LR Table", 2);
            return;
        }
        lrList.remove(lrTable.getSelectionModel().getSelectedIndex());
    }
}
