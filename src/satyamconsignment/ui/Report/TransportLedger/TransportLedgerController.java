
package satyamconsignment.ui.Report.TransportLedger;

import com.ibm.icu.impl.duration.DateFormatter;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import satyamconsignment.misc.DatabaseHandler;
import satyamconsignment.misc.Rrc;


public class TransportLedgerController implements Initializable {
    Rrc rrc;
    DatabaseHandler databaseHandler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ObservableList<String> transportList;
    String jrxmlFileName,pdfFileName;
    DateTimeFormatter formatter;
    Map map;
    @FXML
    private ComboBox<String> transport_name_combo;
    @FXML
    private JFXDatePicker from_date;
    @FXML
    private JFXDatePicker to_date;
    @FXML
    private Button generate_pdf_report_btn;
    @FXML
    private Button launch_report_btn;
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        try {
            formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
            pdfFileName="Report.pdf";
            rrc=new Rrc();
            databaseHandler=DatabaseHandler.getInstance();
            conn=databaseHandler.getConnection();
            ps=conn.prepareStatement("select * from Transport_Master_Table order by name collate nocase");
            rs=ps.executeQuery();
            transportList = FXCollections.observableArrayList();
            while(rs.next())
            {
                transportList.add(rs.getString("name"));
            }
            transport_name_combo.setItems(transportList);
            
        } catch (SQLException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(TransportLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }    

    @FXML
    private void generatePDF(ActionEvent event) {
        try {
            
            jrxmlFileName = "TransportLedger.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlFileName));
            map=new HashMap();
            map.put("transportName", transport_name_combo.getSelectionModel().getSelectedItem());
            //map.put("fromDate", from_date.getValue().format(formatter));
            //map.put("toDate", to_date.getValue().format(formatter));
            JasperPrint jprint = JasperFillManager.fillReport(jasperReport, map, conn);
            JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
            rrc.showAlert("Report Successfully Generated", 1);

        } catch (JRException ex) {
            rrc.showAlert(ex.toString());
            Logger.getLogger(TransportLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    
}
