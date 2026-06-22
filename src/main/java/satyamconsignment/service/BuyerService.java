package satyamconsignment.service;

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
import satyamconsignment.model.BuyerLedgerAgeRow;
import satyamconsignment.model.BuyerLedgerRow;
import satyamconsignment.repository.BuyerRepository;

public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final CollectionService collectionService;

    public BuyerService(BuyerRepository buyerRepository, CollectionService collectionService) {
        this.buyerRepository = buyerRepository;
        this.collectionService = collectionService;
    }

    public List<String> getAllBuyers() throws SQLException {
        return buyerRepository.getAllBuyers();
    }

    public void saveBuyer(String buyerName) throws SQLException {
        buyerRepository.saveBuyer(buyerName);
    }

    public void deleteBuyer(String buyerName) throws SQLException {
        buyerRepository.deleteBuyer(buyerName);
    }

    public void renameBuyer(String oldName, String newName) throws SQLException {
        buyerRepository.renameBuyer(oldName, newName);
    }

    public void generatePdf(String buyerName, boolean isAgewise) throws SQLException, JRException {
        String jrxmlFileName;
        JRBeanCollectionDataSource dataSource;

        if (isAgewise) {
            jrxmlFileName = "BuyerLedgerAge.jrxml";
            List<BuyerLedgerAgeRow> buyerLedgerAgeRows = collectionService.fetchPendingBillsForBuyer(buyerName).stream()
                    .map(it -> BuyerLedgerAgeRow.builder()
                            .billNo(it.getBillNo())
                            .billDate(it.getBillDate())
                            .billAmount(it.getBillAmount())
                            .supplierName(it.getSupplierName())
                            .days((int) ChronoUnit.DAYS.between(Utils.parseDate(it.getBillDate()), LocalDate.now()))
                            .build())
                    .toList();
            dataSource = new JRBeanCollectionDataSource(buyerLedgerAgeRows);
        } else {
            jrxmlFileName = "BuyerLedger.jrxml";
            List<BuyerLedgerRow> buyerLedgerRows = collectionService.getCollectionDetailsForBuyer(buyerName);
            dataSource = new JRBeanCollectionDataSource(buyerLedgerRows);
        }

        String jrxmlFilePath = "/jrxml/" + jrxmlFileName;
        Map<String, Object> payload = new HashMap<>();
        payload.put("buyerName", buyerName);

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, payload, dataSource);
            JasperExportManager.exportReportToPdfFile(jprint, Constants.REPORT_FILE_NAME);
        } catch (JRException ex) {
            Logger.getLogger(BuyerService.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
