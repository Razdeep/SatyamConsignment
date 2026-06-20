package satyamconsignment.service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import satyamconsignment.common.Constants;
import satyamconsignment.common.Utils;
import satyamconsignment.model.SupplierLedgerAgeRow;
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

        String jrxmlFileName;
        JRBeanCollectionDataSource dataSource;

        if (isAgewise) {
            jrxmlFileName = "SupplierLedgerAge.jrxml";
            List<SupplierLedgerAgeRow> supplierLedgerAgeRows =
                    paymentService.fetchPendingBillsForSupplier(supplierName).stream()
                            .map(it -> SupplierLedgerAgeRow.builder()
                                    .billNo(it.getBillNo())
                                    .billDate(it.getBillDate())
                                    .billAmount(it.getBillAmount())
                                    .buyerName(it.getBuyerName())
                                    .days((int)
                                            ChronoUnit.DAYS.between(Utils.parseDate(it.getBillDate()), LocalDate.now()))
                                    .build())
                            .toList();
            dataSource = new JRBeanCollectionDataSource(supplierLedgerAgeRows);
        } else {
            jrxmlFileName = "SupplierLedger.jrxml";
            List<SupplierLedgerRow> supplierLedgerRows = paymentService.getPaymentDetailsForSupplier(supplierName);
            dataSource = new JRBeanCollectionDataSource(supplierLedgerRows);
        }

        String jrxmlFilePath = "/jrxml/" + jrxmlFileName;
        Map<String, Object> payload = new HashMap<>();
        payload.put("supplierName", supplierName);

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, payload, dataSource);
            JasperExportManager.exportReportToPdfFile(jprint, Constants.REPORT_FILE_NAME);
            Utils.launchPdf(Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException | IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(SupplierService.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
