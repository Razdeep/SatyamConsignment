/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satyamconsignment.ui.Master;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import satyamconsignment.common.ViewLoader;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class MasterController implements Initializable {

    @FXML
    private Button supplier_master_btn;
    @FXML
    private Button buyer_master_btn;
    @FXML
    private Button transport_master_btn;
    @FXML
    private Group root;

    private ViewLoader viewLoader;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewLoader = new ViewLoader();
    }

    @FXML
    private void showSupplierMaster(ActionEvent event) {
        viewLoader.loadRoot("/satyamconsignment/ui/Master/Supplier/MasterSupplier.fxml", root);
    }

    @FXML
    private void showBuyerMaster(ActionEvent event) {
        viewLoader.loadRoot("/satyamconsignment/ui/Master/Buyer/MasterBuyer.fxml", root);
    }

    @FXML
    private void showTransportMaster(ActionEvent event) {
        viewLoader.loadRoot("/satyamconsignment/ui/Master/Transport/MasterTransport.fxml", root);
    }
}
