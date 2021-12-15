package socialnetwork.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.containers.UserList;
import socialnetwork.service.AbstractService;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static socialnetwork.domain.constants.PersonalConstants.*;

public class AllUsersController {
    private AbstractService service;
    private User logged;

    ObservableList<String> modelString = FXCollections.observableArrayList();

   @FXML private ListView allUsers;
   @FXML private Label userName;
   @FXML private TextField searchBar;



    public void init(AbstractService service,User logged) {
        this.logged = logged;
        this.service = service;
        UserService userService = new UserService(service,logged);

        allUsers.setItems(modelString);
        try{modelString.setAll(StreamSupport.stream(service.getUsers().spliterator(),false)
                .map(x -> x.getFullName() + " (" + x.getEmail() + ") "  )
                .collect(Collectors.toList()));
        } catch (Exception e) {e.printStackTrace();}

        //set the name label
        userName.setText(logged.getFullName());
        allUsers.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
                    (event.getTarget() != null || ((GridPane) event.getTarget()).getChildren().size() > 0)) {
                String string = event.getTarget().toString();
                try {
                    User clicked = service.findUserByEmail(string.substring(string.indexOf("(") + 1, string.indexOf(")")));
                    if (clicked.getEmail().equals(logged.getEmail())) {
                        run(service, clicked, logged);
                    } else {
                        runFriends(service, clicked, logged);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void run(AbstractService service,User main, User logged){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-view.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserPageController controller = loader.getController();
        controller.init(service, main,logged);

        Stage stages = new Stage();
        stages.setTitle("Yahoo");
        Scene scene = new Scene(root);
        stages.setScene(scene);
        stages.show();
    }

    public void runFriends(AbstractService service,User clicked, User logged){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-otherPOV-view.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserPageController controller = loader.getController();
        controller.init(service, clicked,logged);

        Stage stages = new Stage();
        stages.setTitle("Yahoo2");
        Scene scene = new Scene(root);
        stages.setScene(scene);
        stages.show();
    }
}
