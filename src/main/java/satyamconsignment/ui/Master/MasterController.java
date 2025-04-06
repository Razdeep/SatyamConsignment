/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satyamconsignment.ui.Master;

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

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class MasterController implements Initializable {

    Utils utils;
    @FXML
    private Button supplier_master_btn;
    @FXML
    private Button buyer_master_btn;
    @FXML
    private Button transport_master_btn;
    @FXML
    private Group root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utils = new Utils();
    }

    @FXML
    private void showSupplierMaster(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Master/Supplier/MasterSupplier.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showBuyerMaster(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(
                    getClass().getResource("/satyamconsignment/ui/Master/Buyer/MasterBuyer.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showTransportMaster(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Master/Transport/MasterTransport.fxml"));
            root.getChildren().setAll(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MasterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
