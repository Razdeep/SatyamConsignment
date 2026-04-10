package satyamconsignment.controller.report;

import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.*;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.repository.SupplierRepository;
import satyamconsignment.service.SupplierService;

public class SupplierLedgerController implements Initializable {

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
        SupplierService supplierService = new SupplierService(new SupplierRepository());
        try {
            List<String> supplierList = supplierService.getAllSuppliers();
            supplier_name_combo.setItems(FXCollections.observableArrayList(supplierList));
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void generatePDF(ActionEvent event) {
        Connection conn = DatabaseHandler.getInstance().getConnection();

        String jrxmlFileName;
        if (agewise_outstanding_radio.isSelected()) {
            jrxmlFileName = "SupplierLedgerAge.jrxml";
        } else {
            jrxmlFileName = "SupplierLedger.jrxml";
        }
        String jrxmlFilePath = "/jrxml/" + jrxmlFileName;
        Map<String, Object> map = new HashMap<>();
        map.put("supplierName", supplier_name_combo.getSelectionModel().getSelectedItem());

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jprint, Constants.REPORT_FILE_NAME);
            Utils.launchPdf(Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException | IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void launchPdf(ActionEvent event) {
        try {
            Utils.launchPdf(Constants.REPORT_FILE_NAME);
            Utils.showAlert("PDF opened successfully.", 1);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
