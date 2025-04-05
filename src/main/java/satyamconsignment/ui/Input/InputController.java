package satyamconsignment.ui.Input;

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
import javafx.scene.Group;
import javafx.scene.Parent;
import satyamconsignment.common.Utils;

public class InputController implements Initializable {

	Utils utils;
	@FXML
	private JFXButton bill_entry_btn;
	@FXML
	private JFXButton collection_entry_brn;
	@FXML
	private JFXButton payment_entry_btn;
	@FXML
	private Group root2;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	@FXML
	private void showBillEntryScreen(ActionEvent event) {
		try {
			Parent parent = FXMLLoader
					.load(getClass().getResource("/satyamconsignment/ui/Input/BillEntry/BillEntry.fxml"));
			root2.getChildren().setAll(parent);
		} catch (IOException ex) {
			Utils.showAlert(ex.toString());
			Logger.getLogger(InputController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
		}
	}

	@FXML
	private void showCollectionEntryScreen(ActionEvent event) {
		try {
			Parent parent = FXMLLoader
					.load(getClass().getResource("/satyamconsignment/ui/Input/CollectionEntry/CollectionEntry.fxml"));
			root2.getChildren().setAll(parent);
		} catch (IOException ex) {
			Utils.showAlert(ex.toString());
			Logger.getLogger(InputController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
		}
	}

	@FXML
	private void showPaymentEntryScreen(ActionEvent event) {
		try {
			Parent parent = FXMLLoader
					.load(getClass().getResource("/satyamconsignment/ui/Input/PaymentEntry/PaymentEntry.fxml"));
			root2.getChildren().setAll(parent);
		} catch (IOException ex) {
			Utils.showAlert(ex.toString());
			Logger.getLogger(InputController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
		}
	}
}
