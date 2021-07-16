package satyamconsignment.ui.Report;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import satyamconsignment.misc.Rrc;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReportController implements Initializable {


    Rrc rrc;
    @FXML
    private JFXButton supplier_ledger_btn;
    @FXML
    private JFXButton buyer_ledger_brn;
    @FXML
    private JFXButton transport_ledger_btn;
    @FXML
    private JFXButton back_btn;
    @FXML
    private JFXButton commission_report_btn;
    @FXML
    private Group root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rrc = new Rrc();
    }

    @FXML
    private void loadInitialScreen(ActionEvent event) {
    }

    @FXML
    private void showSupplierLedger(ActionEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Report/SupplierLedger/SupplierLedger.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void showBuyerLedger(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Report/BuyerLedger/BuyerLedger.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showTransportLedger(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/satyamconsignment/ui/Report/TransportLedger/TransportLedger.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showCommissionReport(ActionEvent event) {
    }


}
