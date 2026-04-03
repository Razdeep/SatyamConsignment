package satyamconsignment.ui.Input.CollectionEntry.ViewAndDeleteCollection;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.entity.CollectionEntity;
import satyamconsignment.model.CollectionItem;
import satyamconsignment.repository.CollectionRepository;
import satyamconsignment.service.CollectionService;

public class ViewAndDeleteCollection implements Initializable {
    private List<CollectionItem> collectionItemList;

    @FXML
    private TextField voucher_no_field;

    @FXML
    private TableView<CollectionItem> collection_tableview;

    @FXML
    private TableColumn<CollectionItem, String> bill_no_col;

    @FXML
    private TableColumn<CollectionItem, String> bill_amt_col;

    @FXML
    private TableColumn<CollectionItem, String> supplier_col;

    @FXML
    private TableColumn<CollectionItem, String> amount_collection_col;

    @FXML
    private TableColumn<CollectionItem, String> bank_col;

    @FXML
    private TableColumn<CollectionItem, String> dd_no_col;

    @FXML
    private TableColumn<CollectionItem, String> dd_date_col;

    @FXML
    private Label display_board_label;

    @FXML
    private TextField total_amount_field;

    @FXML
    private Button get_details_btn;

    @FXML
    private Button delete_entry_btn;

    @FXML
    private TableColumn<?, ?> bill_date_col;

    @FXML
    private TextField voucher_date;

    @FXML
    private TextField buyer_name;

    @FXML
    private Button print_btn;

    private CollectionService collectionService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        collectionService = new CollectionService(new CollectionRepository());

        collectionItemList = FXCollections.observableArrayList();

        bill_no_col.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        bill_amt_col.setCellValueFactory(new PropertyValueFactory<>("billAmount"));
        supplier_col.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        bill_date_col.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        amount_collection_col.setCellValueFactory(new PropertyValueFactory<>("amountCollected"));
        dd_no_col.setCellValueFactory(new PropertyValueFactory<>("ddNo"));
        bank_col.setCellValueFactory(new PropertyValueFactory<>("bank"));
        dd_date_col.setCellValueFactory(new PropertyValueFactory<>("ddDate"));
    }

    @FXML
    private void getDetails(ActionEvent event) {

        if (voucher_no_field.getText().isEmpty()) {
            Utils.showAlert("Voucher No. Field is kept empty. Please fill the voucher no.");
            return;
        }

        try {
            CollectionEntity collectionEntity = collectionService.getCollection(voucher_no_field.getText());

            if (null == collectionEntity) {
                Utils.showAlert("Collection voucher not found");
                return;
            }

            collectionItemList = collectionEntity.getItems().stream()
                    .map(it -> CollectionItem.builder()
                            .billNo(it.getBillNo())
                            .billAmount(it.getBillAmount())
                            .billDate(it.getBillDate())
                            .supplierName(it.getSupplierName())
                            .amountCollected(it.getAmountCollected())
                            .bank(it.getBank())
                            .ddNo(it.getDdNo())
                            .ddDate(it.getDdDate())
                            .build())
                    .toList();

            voucher_date.setText(collectionEntity.getVoucherDate());
            buyer_name.setText(collectionEntity.getBuyerName());
            total_amount_field.setText(collectionEntity.getTotalAmount());
            display_board_label.setText(collectionEntity.getBuyerName());

            collection_tableview.setItems(FXCollections.observableArrayList(collectionItemList));
            delete_entry_btn.setDisable(false);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void deleteEntry(ActionEvent event) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure that you want delete " + voucher_no_field.getText() + " ?",
                ButtonType.YES,
                ButtonType.NO,
                ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() != ButtonType.YES) {
            return;
        }

        try {
            collectionService.deleteCollection(voucher_no_field.getText());
            collectionItemList.clear();
            voucher_date.setText("");
            buyer_name.setText("");
            display_board_label.setText("");
            total_amount_field.setText("");
            Utils.showAlert(voucher_no_field.getText().toUpperCase() + " Entry was successfully deleted.", 1);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void printCollection(ActionEvent event) {
        try {
            Connection connection = DatabaseHandler.getInstance().getConnection();
            String jrxmlFileName = "/satyamconsignment/ui/Input/CollectionEntry/Collection.jrxml";
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFileName));
            Map<String, Object> map = new HashMap<>();
            map.put("voucherNo", voucher_no_field.getText());
            map.put("voucherDate", voucher_date.getText());
            map.put("buyerName", buyer_name.getText());
            map.put("billAmount", total_amount_field.getText());
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, connection);
            JasperExportManager.exportReportToPdfFile(jprint, Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ViewAndDeleteCollection.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        Utils.launchPdf(Constants.REPORT_FILE_NAME);
    }
}
