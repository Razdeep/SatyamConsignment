package satyamconsignment.ui.Report.TransportLedger;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import net.sf.jasperreports.engine.*;
import satyamconsignment.common.Constants;
import satyamconsignment.common.DatabaseHandler;
import satyamconsignment.common.Utils;
import satyamconsignment.repository.TransportRepository;
import satyamconsignment.service.TransportService;

public class TransportLedgerController implements Initializable {

    @FXML
    private CheckBox all_time_checkbox;

    @FXML
    private ComboBox<String> transport_name_combo;

    @FXML
    private DatePicker from_date;

    @FXML
    private DatePicker to_date;

    @FXML
    private Button generate_pdf_report_btn;

    @FXML
    private Button launch_report_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TransportService transportService = new TransportService(new TransportRepository());
        try {
            List<String> transportList = transportService.getAllTransports();
            transport_name_combo.setItems(FXCollections.observableArrayList(transportList));
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(TransportLedgerController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    @FXML
    private void generatePDF(ActionEvent event) {
        Connection conn = DatabaseHandler.getInstance().getConnection();

        String jrxmlFileName = "TransportLedger.jrxml";
        String jrxmlFilePath = "/satyamconsignment/ui/Report/TransportLedger/" + jrxmlFileName;

        Map<String, Object> map = new HashMap<>();
        map.put("transportName", transport_name_combo.getSelectionModel().getSelectedItem());
        map.put("fromDate", "01-01-2000");
        map.put("toDate", "31-12-2100");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (!all_time_checkbox.isSelected()) {
            if (from_date.getValue() == null || to_date.getValue() == null) {
                Utils.showAlert("Select dates properly");
                return;
            }
            map.put("fromDate", from_date.getValue().format(formatter));
            map.put("toDate", to_date.getValue().format(formatter));
        }

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(TransportLedgerController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        Utils.launchPdf(Constants.REPORT_FILE_NAME);
    }

    @FXML
    private void launchPdf(ActionEvent event) {
        Utils.launchPdf(Constants.REPORT_FILE_NAME);
    }
}
