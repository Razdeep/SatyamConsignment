package satyamconsignment.ui.Report.BuyerLedger;

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

public class BuyerLedgerController implements Initializable {
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ObservableList<String> buyerList;

    @FXML
    private RadioButton buyer_ledger_radio;

    @FXML
    private ToggleGroup choice;

    @FXML
    private RadioButton agewise_outstanding_radio;

    @FXML
    private Button generate_pdf_btn;

    @FXML
    private ComboBox<String> buyer_name_combo;

    @FXML
    private Button launch_pdf_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            databaseHandler = DatabaseHandler.getInstance();
            conn = databaseHandler.getConnection();
            ps = conn.prepareStatement("select * from Buyer_Master_Table order by name collate nocase");
            rs = ps.executeQuery();
            buyerList = FXCollections.observableArrayList();
            while (rs.next()) {
                buyerList.add(rs.getString("name"));
            }
            buyer_name_combo.setItems(buyerList);
        } catch (SQLException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(BuyerLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void generatePDF(ActionEvent event) {
        String jrxmlFileName;
        if (agewise_outstanding_radio.isSelected()) {
            jrxmlFileName = "BuyerLedgerAge.jrxml";
        } else {
            jrxmlFileName = "BuyerLedger.jrxml";
        }
        String jrxmlFilePath = "/satyamconsignment/ui/Report/BuyerLedger/" + jrxmlFileName;

        Map<String, Object> map = new HashMap<>();
        map.put("buyerName", buyer_name_combo.getSelectionModel().getSelectedItem());

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFilePath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, Constants.REPORT_FILE_NAME);
            Utils.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(BuyerLedgerController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        Utils.launchPdf(Constants.REPORT_FILE_NAME);
    }

    @FXML
    private void launchPdf(ActionEvent event) {
        Utils.launchPdf(Constants.REPORT_FILE_NAME);
    }
}
