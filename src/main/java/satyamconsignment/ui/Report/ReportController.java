package satyamconsignment.ui.Report;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import satyamconsignment.common.ViewLoader;

public class ReportController implements Initializable {

    @FXML
    private Button supplier_ledger_btn;

    @FXML
    private Button buyer_ledger_brn;

    @FXML
    private Button transport_ledger_btn;

    @FXML
    private Group root;

    private ViewLoader viewLoader;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewLoader = new ViewLoader();
    }

    @FXML
    private void showSupplierLedger(ActionEvent event) {
        viewLoader.loadRoot("/satyamconsignment/ui/Report/SupplierLedger/SupplierLedger.fxml", root);
    }

    @FXML
    private void showBuyerLedger(ActionEvent event) {
        viewLoader.loadRoot("/satyamconsignment/ui/Report/BuyerLedger/BuyerLedger.fxml", root);
    }

    @FXML
    private void showTransportLedger(ActionEvent event) {
        viewLoader.loadRoot("/satyamconsignment/ui/Report/TransportLedger/TransportLedger.fxml", root);
    }
}
