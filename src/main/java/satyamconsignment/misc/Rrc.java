package satyamconsignment.misc;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Rrc {

	public static void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.show();
	}

	private static AlertType getAlertTypeFromCustomValue(int choice) {
		switch (choice) {
			case 0 :
				return AlertType.ERROR;
			case 1 :
				return AlertType.INFORMATION;
			case 2 :
				return AlertType.WARNING;
			case 3 :
				return AlertType.CONFIRMATION;
			default :
				showAlert("Programming Error. Select 1->error 2->info 3->warn 4->confirm");
		}
		return AlertType.NONE;
	}

	public static void showAlert(String message, int choice) {
		Alert alert = new Alert(getAlertTypeFromCustomValue(choice), message);
		alert.show();
	}
}
