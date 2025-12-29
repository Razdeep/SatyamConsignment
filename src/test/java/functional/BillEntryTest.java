package functional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class BillEntryTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/satyamconsignment/ui/Main/Main.fxml")
        );

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    @Test
    void testIfLoadedProperly() {
        waitForFxEvents();
//        clickOn("#input_btn");
//
//        verifyThat("#bill_entry_btn", NodeMatchers.isVisible());
//        clickOn("#bill_entry_btn");
//
//        verifyThat("#TabButtonAdd", NodeMatchers.isVisible());
//        clickOn("#TabButtonAdd");
//
//        verifyThat("#supplier_field", NodeMatchers.isVisible());
////        clickOn("#supplier_field");
//
//        interact(() -> {
//            lookup("#supplier_field").queryTextInputControl().setText("ajanta");
//        });
//
//
//        verifyThat("#buyer_name_field", NodeMatchers.isVisible());
//        clickOn("#buyer_name_field");
//
//        write("hello");
    }


}
