package satyamconsignment.common;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

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

    public static void launchPdf(String filename) {
        File pdfFile = new File(filename);

        if (!pdfFile.exists()) {
            showAlert("File does not exist: " + pdfFile.getAbsolutePath());
            return;
        }

        if (!Desktop.isDesktopSupported()) {
            showAlert("Desktop is not supported on this platform.");
            return;
        }

        try {
            Desktop.getDesktop().open(pdfFile);
            showAlert("PDF opened successfully.", 1);
        } catch (IOException e) {
            String msg = ("Error opening PDF: " + e.getMessage());
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, msg, e);
        }
    }

}
