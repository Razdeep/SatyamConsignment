package satyamconsignment.common;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private static AlertType getAlertTypeFromCustomValue(int choice) {
        switch (choice) {
            case 0:
                return AlertType.ERROR;
            case 1:
                return AlertType.INFORMATION;
            case 2:
                return AlertType.WARNING;
            case 3:
                return AlertType.CONFIRMATION;
            default:
                showAlert("Programming Error. Select 1->error 2->info 3->warn 4->confirm");
        }
        return AlertType.NONE;
    }

    public static void showAlert(String message, int choice) {
        Alert alert = new Alert(getAlertTypeFromCustomValue(choice), message);
        alert.show();
    }

    public static void launchPdf(String filename) throws IOException {
        File pdfFile = new File(filename);

        if (!pdfFile.exists()) {
            throw new IOException("File does not exist: " + pdfFile.getAbsolutePath());
        }

        if (!Desktop.isDesktopSupported()) {
            throw new IOException("Desktop is not supported on this platform.");
        }

        Desktop.getDesktop().open(pdfFile);
    }

    public static String formatDate(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
        try {
            LocalDate date = LocalDate.parse(dateString, inputFormatter);
            return date.format(outputFormatter);
        } catch (DateTimeParseException ex) {
            try {
                // already in desired format
                LocalDate.parse(dateString, outputFormatter);
                return dateString;
            } catch (DateTimeParseException ex2) {
                logger.warning(ex2.toString());
                throw ex2;
            }
        }
    }
}
