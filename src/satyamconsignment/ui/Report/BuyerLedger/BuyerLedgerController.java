package satyamconsignment.ui.Report.BuyerLedger;

import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.*;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;

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


public class BuyerLedgerController implements Initializable {
    Rrc rrc;
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ObservableList<String> buyerList;
    String jrxmlFileName, pdfFileName;
    Map map;


    @FXML
    private JFXRadioButton buyer_ledger_radio;
    @FXML
    private ToggleGroup choice;
    @FXML
    private JFXRadioButton agewise_outstanding_radio;
    @FXML
    private Button generate_pdf_btn;
    @FXML
    private ComboBox<String> buyer_name_combo;
    @FXML
    private Button launch_pdf_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            pdfFileName = "Report.pdf";
            rrc = new Rrc();
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
            Rrc.showAlert(ex.toString());
            Logger.getLogger(BuyerLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void generatePDF(ActionEvent event) {

        try {
            if (agewise_outstanding_radio.isSelected()) {
                jrxmlFileName = "BuyerLedgerAge.jrxml";
            } else {
                jrxmlFileName = "BuyerLedger.jrxml";
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFileName));
            map = new HashMap();
            map.put("buyerName", buyer_name_combo.getSelectionModel().getSelectedItem());
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
            Rrc.showAlert("Report Successfully Generated", 1);
        } catch (JRException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(BuyerLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void launchPdf(ActionEvent event) {
    }

}
