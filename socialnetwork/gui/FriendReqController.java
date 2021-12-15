package socialnetwork.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.AbstractService;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static socialnetwork.domain.constants.Constants.DATEFORMATTER;

public class FriendReqController  {

    ObservableList<String> modelString = FXCollections.observableArrayList();

    private User logged;
    private AbstractService abstractService;

    @FXML private ListView friendReq;
    @FXML private Label userName;

    public void init(AbstractService service,User user){
        logged = user;
        abstractService = service;
        UserService userService = new UserService(service, user);

        //set the friendsReq List label
        friendReq.setItems(modelString);
        try {modelString.setAll(userService.pendingFriendships().stream()
                        .filter(x -> x.getRight().equals(user))
                .map(x -> x.getLeft().getFullName()  + "(" + x.getLeft().getEmail() + ")"+ x.getDate().format(DATEFORMATTER))
                .collect(Collectors.toList()));
        } catch (Exception e) {e.printStackTrace();}

        //double click on friends
        friendReq.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
                    (event.getTarget() != null || ((GridPane) event.getTarget()).getChildren().size() > 0)) {
                String string = event.getTarget().toString();
                try {
                    User clicked = service.findUserByEmail(string.substring(string.indexOf("(") + 1, string.indexOf(")")));
                    runFriends(service, clicked, logged);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //set the name label
        userName.setText(user.getFullName());

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
