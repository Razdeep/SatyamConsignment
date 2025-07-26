package satyamconsignment.ui.Report.SupplierLedger;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.*;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;

public class SupplierLedgerController implements Initializable {
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ObservableList<String> supplierList;

    @FXML
    private ComboBox<String> supplier_name_combo;
    @FXML
    private ToggleGroup choice;
    @FXML
    private RadioButton supplier_ledger_radio;
    @FXML
    private RadioButton agewise_outstanding_radio;
    @FXML
    private Button generate_pdf_btn;
    @FXML
    private Button launch_pdf_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            databaseHandler = DatabaseHandler.getInstance();
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement(
                    "select * from Supplier_Master_Table order by name collate nocase");
            rs = ps.executeQuery();
            supplierList = FXCollections.observableArrayList();
            while (rs.next()) {
                supplierList.add(rs.getString("name"));
            }
            supplier_name_combo.setItems(supplierList);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
        }
    }

    @FXML
    private void generatePDF(ActionEvent event) {
        String jrxmlFileName;
        if (agewise_outstanding_radio.isSelected()) {
            jrxmlFileName = "SupplierLedgerAge.jrxml";
        } else {
            jrxmlFileName = "SupplierLedger.jrxml";
        }
        String jrxmlFilePath = "/satyamconsignment/ui/Report/SupplierLedger/" + jrxmlFileName;
        Map<String, Object> map = new HashMap<>();
        map.put("supplierName", supplier_name_combo.getSelectionModel().getSelectedItem());

        try {

            JasperReport jasperReport = JasperCompileManager
                    .compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jprint, Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE,
                    ex.toString(), ex);
        }
        Utils.launchPdf(Constants.REPORT_FILE_NAME);
    }

    @FXML
    private void launchPdf(ActionEvent event) {
        Utils.launchPdf(Constants.REPORT_FILE_NAME);
    }
}
