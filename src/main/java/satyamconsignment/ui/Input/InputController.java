package satyamconsignment.ui.Input;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import satyamconsignment.common.ViewLoader;

public class InputController implements Initializable {

    @FXML
    private Button bill_entry_btn;
    @FXML
    private Button collection_entry_brn;
    @FXML
    private Button payment_entry_btn;
    @FXML
    private Group root;

    private ViewLoader viewLoader;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewLoader = new ViewLoader();
    }

    @FXML
    private void showBillEntryScreen(ActionEvent event) {
        viewLoader.load("/satyamconsignment/ui/Input/BillEntry/BillEntry.fxml", root);
    }

    @FXML
    private void showCollectionEntryScreen(ActionEvent event) {
        viewLoader.load("/satyamconsignment/ui/Input/CollectionEntry/CollectionEntry.fxml",
                root);
    }

    @FXML
    private void showPaymentEntryScreen(ActionEvent event) {
        viewLoader.load("/satyamconsignment/ui/Input/PaymentEntry/PaymentEntry.fxml", root);
    }
}
