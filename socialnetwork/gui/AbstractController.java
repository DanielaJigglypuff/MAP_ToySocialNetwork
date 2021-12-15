package socialnetwork.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import socialnetwork.gui.utils.Window;
import socialnetwork.service.AbstractService;

import static socialnetwork.domain.constants.PersonalConstants.*;

public class AbstractController {

    protected AbstractService service;

    protected void setService() {
        try {
            service = new AbstractService(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            Window.ALERT(e.getMessage());
        }
    }

    @FXML
    protected void registerButtonClick() {
        RegisterController controller = new RegisterController();
        controller.run();

    }

    @FXML
    protected void loginButtonClick() {
        LoginController controller = new LoginController();
        controller.run();
    }

    @FXML
    protected void adminButtonClick(ActionEvent event) {

    }


}
