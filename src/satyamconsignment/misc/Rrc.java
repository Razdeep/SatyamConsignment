package satyamconsignment.misc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Rrc {
    public void showAlert(String message)
    {
        Alert alert= new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
    public void showAlert(String message,int choice)
    {
        Alert alert;
        switch(choice)
        {
            case 0:
            alert= new Alert(AlertType.ERROR);
            alert.setContentText(message);
            alert.show();
            break;
            case 1:
            alert= new Alert(AlertType.INFORMATION);
            alert.setContentText(message);
            alert.show();
            break;
            case 2:
            alert= new Alert(AlertType.WARNING);
            alert.setContentText(message);
            alert.show();
            break;
            case 3:
            alert= new Alert(AlertType.CONFIRMATION);
            alert.setContentText(message);
            alert.show();
            break;
            default:
            showAlert("Programming Error. Select 1->error 2->info 3->warn 4->confirm");
            break;
        }
    }
    public void loadWindow(String loc,String title)
    {
        try {
            Parent parent=FXMLLoader.load(getClass().getResource(loc));
            Stage stage=new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle(title);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Rrc.class.getName()).log(Level.SEVERE, null, ex);
            this.showAlert(ex.toString());
        }
    }
    public void loadWindowMaximized(String loc,String title)
    {
        try {
            Parent parent=FXMLLoader.load(getClass().getResource(loc));
            Stage stage=new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle(title);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Rrc.class.getName()).log(Level.SEVERE, null, ex);
            this.showAlert(ex.toString());
        }
    }
}
