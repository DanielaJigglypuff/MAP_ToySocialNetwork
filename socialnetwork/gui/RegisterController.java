package socialnetwork.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.gui.utils.Window;
import socialnetwork.service.AbstractService;

import java.io.IOException;

import static socialnetwork.domain.constants.PersonalConstants.*;

public class RegisterController extends AbstractController {
    private AbstractService service;

    public TextField emailField;
    public TextField fistNameField;
    public TextField lastNameField;
    private final Stage stage;

    @FXML
    private Button registerButton;

    public RegisterController() {
        service = new AbstractService(URL, USERNAME, PASSWORD);
        stage = new Stage();
        stage.setTitle("Register");
    }

    public void run() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Window.ALERT(e.getMessage());
        }
    }

    public void register() {
        try {
            service.register(emailField.getText(), fistNameField.getText(), lastNameField.getText());
        } catch (Exception e) {
            Window.ALERT(e.getMessage());
        }
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
        Window.CONFIRMATION("User successfully added");
    }
}
