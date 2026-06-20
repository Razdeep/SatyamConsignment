package satyamconsignment.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import satyamconsignment.common.Constants;
import satyamconsignment.common.Utils;
import satyamconsignment.controller.report.SupplierLedgerController;
import satyamconsignment.model.SupplierLedgerRow;
import satyamconsignment.repository.SupplierRepository;

public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final PaymentService paymentService;

    public SupplierService(SupplierRepository supplierRepository, PaymentService paymentService) {
        this.supplierRepository = supplierRepository;
        this.paymentService = paymentService;
    }

    public List<String> getAllSuppliers() throws SQLException {
        return supplierRepository.getAllSuppliers();
    }

    public void saveSupplier(String supplierName) throws SQLException {
        supplierRepository.saveSupplier(supplierName);
    }

    public void deleteSupplier(String supplierName) throws SQLException {
        supplierRepository.deleteSupplier(supplierName);
    }

    public void renameSupplier(String oldName, String newName) throws SQLException {
        supplierRepository.renameSupplier(oldName, newName);
    }

    public void generatePdf(String supplierName, boolean isAgewise) throws SQLException {

        String jrxmlFileName = "SupplierLedger.jrxml";
        String jrxmlFilePath = "/jrxml/" + jrxmlFileName;
        Map<String, Object> payload = new HashMap<>();
        payload.put("supplierName", supplierName);

        List<SupplierLedgerRow> supplierLedgerRows = paymentService.getPaymentDetailsForSupplier(supplierName);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(supplierLedgerRows);

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, payload, dataSource);
            JasperExportManager.exportReportToPdfFile(jprint, Constants.REPORT_FILE_NAME);
            Utils.launchPdf(Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException | IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierLedgerController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
