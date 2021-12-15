package socialnetwork.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.gui.utils.Window;
import socialnetwork.service.AbstractService;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserPageController {
    private User mainUser;
    private User logged;
    private AbstractService service;
    private UserService userService;
    private Stage mainStage;

    @FXML private Button seeUsersButton;
    @FXML private Button seeFriendReqButton;
    @FXML private Button sendMessagesProfileButton;

    @FXML private Button acceptButton;
    @FXML private Button sendFriendReqButton;
    @FXML private Button removeFriendReqButton;
    @FXML private Button removeFriendButton;
    @FXML private Button declineButton;
    @FXML private Label nameLabel;
    @FXML private ListView friends;

    @FXML protected Text welcomeText;
    @FXML protected TextField search;

    @FXML private TableView<User> table;
    @FXML protected TableColumn<User, String> emailColumn;
    @FXML protected TableColumn<User, Button> buttonColumn;
    @FXML protected Button searchButton;

    ObservableList<String> modelString = FXCollections.observableArrayList();

    public void init(AbstractService service, User user, User logged) {
        this.service = service;
        this.mainUser = user;
        this.logged = logged;
        UserService userService = new UserService(service, logged);

        if(mainUser.equals(logged)){
            acceptButton.setVisible(false);
            sendFriendReqButton.setVisible(false);
            removeFriendButton.setVisible(false);
            removeFriendReqButton.setVisible(false);
            seeFriendReqButton.setVisible(true);
            declineButton.setVisible(false);

        }
        else {
            try {
                //if they are friends wohoo
                if(userService.acceptedFriendships().stream().anyMatch(x -> x.isFriend(mainUser))){
                    acceptButton.setVisible(false);
                    sendFriendReqButton.setVisible(false);
                    removeFriendButton.setVisible(true);
                    removeFriendReqButton.setVisible(false);
                    seeFriendReqButton.setVisible(false);
                    declineButton.setVisible(false);
                }
                else {
                    //if the request was sent TO logged
                    if(userService.pendingFriendships().stream()
                            .anyMatch(x -> x.getRight().equals(user) && x.getLeft().equals(logged))){
                        acceptButton.setVisible(false);
                        sendFriendReqButton.setVisible(false);
                        removeFriendButton.setVisible(false);
                        removeFriendReqButton.setVisible(true);
                        seeFriendReqButton.setVisible(false);
                        declineButton.setVisible(false);
                    }
                    //if the request was sent BY logged
                    else if(userService.pendingFriendships().stream()
                            .anyMatch(x -> (x.getRight().equals(logged) && x.getLeft().equals(user)))){
                        acceptButton.setVisible(true);
                        sendFriendReqButton.setVisible(false);
                        removeFriendButton.setVisible(false);
                        removeFriendReqButton.setVisible(false);
                        seeFriendReqButton.setVisible(false);
                        declineButton.setVisible(true);
                    }
                    //if there is no friend request and they are not friends already
                    else {
                        acceptButton.setVisible(false);
                        sendFriendReqButton.setVisible(true);
                        removeFriendButton.setVisible(false);
                        removeFriendReqButton.setVisible(false);
                        seeFriendReqButton.setVisible(false);
                        declineButton.setVisible(false);
                    }
                }
            } catch (Exception e) {e.printStackTrace();}

        }


                //set the friendsList label
        friends.setItems(modelString);
        try {modelString.setAll(userService.acceptedFriendships().stream()
                    .map(x -> x.theOtherFriend(mainUser).getFullName() + "(" + x.theOtherFriend(mainUser).getEmail() + ")")
                    .collect(Collectors.toList()));
        } catch (Exception e) {e.printStackTrace();}

                //double click on friends
        friends.setOnMouseClicked(event -> {
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
                //set the name label
        nameLabel.setText(user.getFullName());

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
        stages.setTitle("Friend's Profile");
        Scene scene = new Scene(root);
        stages.setScene(scene);
        stages.show();
    }

    public void handleSeeFriendReqButton(ActionEvent event)throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("friend-request-view.fxml"));
        Parent friendReqView = loader.load();

        Scene friendReqViewScene = new Scene(friendReqView);

        //access the controller and call a method
        FriendReqController controller = loader.getController();
        controller.init(service,logged);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(friendReqViewScene);
        window.show();

    }

    public void handleSeeUsersButton(ActionEvent event)throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("all-users-view.fxml"));
        Parent allUsersView = loader.load();

        Scene allUsersViewScene = new Scene(allUsersView);

        //access the controller and call a method
        AllUsersController controller = loader.getController();
        controller.init(service,logged);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(allUsersViewScene);
        window.show();

    }

    public void handleSendMessageProfileButton(ActionEvent event)throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("messages-view.fxml"));
        Parent allUsersView = loader.load();

        Scene allUsersViewScene = new Scene(allUsersView);

        //access the controller and call a method
        MessagesController controller = loader.getController();
        controller.init(service,logged,mainUser);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(allUsersViewScene);
        window.show();

    }

    public void handleNewButton(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-otherPOV-view.fxml"));
        Parent root = null;
        try {root = loader.load();} catch (IOException e) {e.printStackTrace();}
        UserPageController controller = loader.getController();
        controller.init(service, mainUser,logged);

        System.out.println("merge buttonul  " + event.getEventType().getName());
    }

    public void handleSendFriendREQButton(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-otherPOV-view.fxml"));
        Parent root = null;
        try {root = loader.load();} catch (IOException e) {e.printStackTrace();}
        UserPageController controller = loader.getController();
        controller.init(service, mainUser,logged);
        UserService userService = new UserService(service, logged);

        userService.sendRequest(mainUser.getEmail());

        Window.CONFIRMATION("Friend request sent");
    }

    public void handleAcceptFriendREQButton(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-otherPOV-view.fxml"));
        Parent root = null;
        try {root = loader.load();} catch (IOException e) {e.printStackTrace();}
        UserPageController controller = loader.getController();
        controller.init(service, mainUser,logged);
        UserService userService = new UserService(service, logged);

        userService.acceptRequest(mainUser.getEmail());

        Window.CONFIRMATION("Friend request Accepted");
    }

    public void handleDenyFriendREQButton(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-otherPOV-view.fxml"));
        Parent root = null;
        try {root = loader.load();} catch (IOException e) {e.printStackTrace();}
        UserPageController controller = loader.getController();
        controller.init(service, mainUser,logged);
        UserService userService = new UserService(service, logged);

        userService.declineRequest(mainUser.getEmail());

        Window.CONFIRMATION("Friendship Denied");
    }

    public void handleUnfriendButton(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-otherPOV-view.fxml"));
        Parent root = null;
        try {root = loader.load();} catch (IOException e) {e.printStackTrace();}
        UserPageController controller = loader.getController();
        controller.init(service, mainUser,logged);
        UserService userService = new UserService(service, logged);

        userService.deleteFriend(mainUser.getEmail());

        Window.CONFIRMATION("Friend removed");
    }

    public void handleRemoveFriendREQButton(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-otherPOV-view.fxml"));
        Parent root = null;
        try {root = loader.load();} catch (IOException e) {e.printStackTrace();}
        UserPageController controller = loader.getController();
        controller.init(service, mainUser,logged);
        UserService userService = new UserService(service, logged);

        userService.declineRequest(mainUser.getEmail());

        Window.CONFIRMATION("Friend request unsent");
    }

}