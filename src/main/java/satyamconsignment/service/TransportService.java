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
import satyamconsignment.model.TransportLedgerRow;
import satyamconsignment.repository.TransportRepository;

public class TransportService {

    private final TransportRepository transportRepository;

    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public List<String> getAllTransports() throws SQLException {
        return transportRepository.getAllTransports();
    }

    public void saveTransport(String transportName) throws SQLException {
        transportRepository.saveTransport(transportName);
    }

    public void deleteTransport(String transportName) throws SQLException {
        transportRepository.deleteTransport(transportName);
    }

    public void renameTransport(String oldName, String newName) throws SQLException {
        transportRepository.renameTransport(oldName, newName);
    }

    public void generatePdf(String transportName, String fromDate, String toDate) throws SQLException {
        List<TransportLedgerRow> transportLedgerRows =
                transportRepository.getTransportReportFor(transportName, fromDate, toDate);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transportLedgerRows);

        String jrxmlFileName = "TransportLedger.jrxml";
        String jrxmlFilePath = "/jrxml/" + jrxmlFileName;

        Map<String, Object> map = new HashMap<>();
        map.put("transportName", transportName);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, Constants.REPORT_FILE_NAME);
            Utils.launchPdf(Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (IOException | JRException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(TransportService.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }
}
