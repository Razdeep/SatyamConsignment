package satyamconsignment.ui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import satyamconsignment.misc.Rrc;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    Rrc rrc;
    @FXML
    private Label label;
    @FXML
    private JFXButton login_btn;
    @FXML
    private JFXPasswordField password_field;
    @FXML
    private BorderPane root;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rrc = new Rrc();

    }

    @FXML
    private void openMainWindow(ActionEvent event) {
        if (password_field.getText().compareTo("satyam") == 0) {
            try {
                //rrc.loadWindowMaximized("/satyamconsignment/ui/Main/Main.fxml", "Satyam Consignment");
                Parent parent = FXMLLoader.load(getClass().getResource("/ui/Main.fxml"));
                //root.getChildren().setAll(parent);
                root.setCenter(parent);
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                Rrc.showAlert(ex.toString());
            }
        }
    }

}
