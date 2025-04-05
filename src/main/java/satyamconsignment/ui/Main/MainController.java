package satyamconsignment.ui.Main;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import satyamconsignment.common.Utils;

public class MainController implements Initializable {

    Utils utils;
    @FXML
    private JFXButton input_btn;
    @FXML
    private BorderPane root;
    @FXML
    private JFXButton report_btn;
    @FXML
    private JFXButton master_btn;
    @FXML
    private MenuItem welcome_screen;
    @FXML
    private MenuItem contact;
    @FXML
    private MenuItem about;
    @FXML
    private MenuItem instructions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utils = new Utils();
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Main/WelcomeScreen/WelcomeScreen.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showInputScreen(ActionEvent event) {
        try {
            Parent parent = FXMLLoader
                    .load(getClass().getResource("/satyamconsignment/ui/Input/Input.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showReportScreen(ActionEvent event) {

        try {
            Parent parent = FXMLLoader
                    .load(getClass().getResource("/satyamconsignment/ui/Report/Report.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showMasterScreen(ActionEvent event) {
        try {
            Parent parent = FXMLLoader
                    .load(getClass().getResource("/satyamconsignment/ui/Master/Master.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showWelcomeScreen(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Main/WelcomeScreen/WelcomeScreen.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showContactScreen(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(
                    getClass().getResource("/satyamconsignment/ui/Main/Contact/Contact.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showAboutScreen(ActionEvent event) {
        try {
            Parent parent = FXMLLoader
                    .load(getClass().getResource("/satyamconsignment/ui/Main/About/About.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showInstructionScreen(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Main/InstructionScreen/Instructions.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showInputHistory(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Main/InputHistory/InputHistory.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showCollectionHistory(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(
                    "/satyamconsignment/ui/Main/CollectionHistory/CollectionHistory.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showPaymentHistory(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass()
                    .getResource("/satyamconsignment/ui/Main/PaymentHistory/PaymentHistory.fxml"));
            root.setCenter(parent);
        } catch (IOException ex) {
            Utils.showAlert(ex.toString());
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
