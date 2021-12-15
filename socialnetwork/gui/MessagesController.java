package socialnetwork.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import socialnetwork.domain.FriendshipWithStatus;
import socialnetwork.domain.ReplyMessage;
import socialnetwork.domain.User;
import socialnetwork.service.AbstractService;
import socialnetwork.service.UserService;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import static socialnetwork.domain.constants.PersonalConstants.*;

public class MessagesController  {

    ObservableList<String> modelLogged = FXCollections.observableArrayList();

    private User logged;
    private User clicked;
    private AbstractService abstractService;

    @FXML private ListView messagesListView;
    @FXML private Label loggedUserLabel;
    @FXML private Label reveiverUserLabel;
    @FXML private Button sendMessageButton;

    public void init(AbstractService abstractService,User logged, User clicked) {
        this.logged = logged;
        this.abstractService = abstractService;
        this.clicked = clicked;

        UserService userService = new UserService(abstractService, logged);

        //set the name label
        loggedUserLabel.setText(logged.getFullName());
        reveiverUserLabel.setText(clicked.getFullName());

        messagesListView.setItems(modelLogged);
        try {modelLogged.setAll(userService.messagesWith(clicked.getEmail()).stream()
                    .map(x -> x.getReply().getMessage().equals(null) ?
                            (x.getFrom().getFullName() + " replied to ->> " + x.getReply().getMessage() + " \n with   " + x.getMessage() + " " + x.getDate().toString())
                            : (x.getFrom().getFullName() + " : " + x.getMessage() + " " + x.getDate().toString()))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


