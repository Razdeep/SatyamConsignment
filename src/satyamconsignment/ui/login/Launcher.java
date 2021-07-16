package satyamconsignment.ui.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import satyamconsignment.misc.Rrc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Rrc rrc = new Rrc();
        Parent root;
        try {
            String address1 = "Login.fxml";
            String address2 = "/satyamconsignment/ui/Main/Main.fxml";
            root = FXMLLoader.load(getClass().getResource(address2));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/satyamconsignment/icons/Icon.png")));
            stage.setTitle("Satyam Consignment v2.0");
        } catch (IOException ex) {
            Rrc.showAlert(ex.toString());
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
