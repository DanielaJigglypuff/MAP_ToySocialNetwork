package socialnetwork.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.gui.utils.Window;
import socialnetwork.service.AbstractService;

import java.io.IOException;
import java.util.UUID;

import static socialnetwork.domain.constants.PersonalConstants.*;

public class LoginController extends AbstractController {
    private AbstractService service;
    public TextField emailField;
    private final Stage stage;
    private final UserPageController userPageController = new UserPageController();

    @FXML private Button loginButton;

    public LoginController() {
        service = new AbstractService(URL, USERNAME, PASSWORD);
        stage = new Stage();
        stage.setTitle("Log in");
    }

    public void run() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("log-in-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Window.ALERT(e.getMessage());
        }
    }

    public void login(ActionEvent event) throws Exception {
        if (emailField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't be empty", ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                User user = service.findUserByEmail(emailField.getText());
                User logged = service.findUserByEmail(emailField.getText());
                UUID id = user.getId();
                Stage stage = (Stage) loginButton.getScene().getWindow();

                userPageController.run(service,user,logged);

            } catch (Exception e) {
                Window.ALERT(e.getMessage());
            }
        }
    }
}