package satyamconsignment.ui.Report;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import satyamconsignment.common.Utils;

public class ReportController implements Initializable {

    Utils utils;
    @FXML
    private Button supplier_ledger_btn;
    @FXML
    private Button buyer_ledger_brn;
    @FXML
    private Button transport_ledger_btn;
    @FXML
    private Button back_btn;
    @FXML
    private Button commission_report_btn;
    @FXML
    private Group root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utils = new Utils();
    }

    @FXML
    private void loadInitialScreen(ActionEvent event) {
    }

    @FXML
    private void showSupplierLedger(ActionEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource(
                    "/satyamconsignment/ui/Report/SupplierLedger/SupplierLedger.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showBuyerLedger(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Report/BuyerLedger/BuyerLedger.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showTransportLedger(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(
                    "/satyamconsignment/ui/Report/TransportLedger/TransportLedger.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showCommissionReport(ActionEvent event) {
    }
}
