package socialnetwork.ui;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.InputException;
import socialnetwork.domain.util.SpecificList;
import socialnetwork.service.UserService;

import java.util.List;
import java.util.Scanner;

import static socialnetwork.domain.constants.Constants.DATEFORMATTER;

/**
 * The interface for the user.
 */
public class UserUI extends AbstractUI {
    private final User user;

    public UserUI(AbstractUI ui, User user) {
        this.service = new UserService(ui.service, user);
        this.user = user;
    }

    @Override
    public String name() { return user.getFirstName(); }

    @Override
    public void run() throws Exception {
        try {
            String input = "";
            do {
                try {
                    input = input();
                    switch (input) {
                        case "0": break;
                        case "1": sendRequest(); break;
                        case "2": acceptRequest(); break;
                        case "3": declineRequest(); break;
                        case "4": deleteFriend(); break;
                        case "5": acceptedFriendships(); break;
                        case "6": pendingFriendships(); break;
                        case "7": sendMessage(); break;
                        case "8": messagesWith(); break;
                        default: throw new InputException("Invalid input");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
//throw e;
                }
                System.out.println("\n");
            } while (!input.equals("0"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
//throw e;
        }
        System.out.println("Goodbye");
    }

    @Override
    protected String input() {
        System.out.println("*****************************************");
        System.out.println("1 - Send a friend request");
        System.out.println("2 - Accept a friend request");
        System.out.println("3 - Decline a friend request");
        System.out.println("4 - Delete a friend");
        System.out.println("5 - Show friends");
        System.out.println("6 - Show pending list");
        System.out.println("7 - Send a message");
        System.out.println("8 - Show a conversation");
        System.out.println("0 - Logout");
        System.out.println("Introduce one of the numbers:");
        return (new Scanner(System.in)).nextLine();
    }

    /**
     * UI sends a request.
     */
    private void sendRequest() throws Exception {
        System.out.println("Introduce the email:");
        String email = (new Scanner(System.in)).nextLine();
        service.sendRequest(email);
        System.out.println("Request sent");
    }

    /**
     * UI accepts a requet.
     */
    private void acceptRequest() throws Exception {
        System.out.println("Introduce the email:");
        String email = (new Scanner(System.in)).nextLine();
        service.acceptRequest(email);
        System.out.println("Request accepted");
    }

    /**
     * UI declines a request.
     */
    private void declineRequest() throws Exception {
        System.out.println("Introduce the email:");
        String email = (new Scanner(System.in)).nextLine();
        service.declineRequest(email);
        System.out.println("Request declined");
    }

    /**
     * UI deletes a request.
     */
    private void deleteFriend() throws Exception {
        System.out.println("Introduce the email:");
        String email = (new Scanner(System.in)).nextLine();
        service.deleteFriend(email);
        System.out.println("Friend deleted");
    }

    /**
     * Shows a specific list;
     * @param list the specific list.
     */
    protected void showList(SpecificList list) throws Exception {
        list.process().forEach(friendship -> System.out.println("Friendship: " + friendship.
                theOtherFriend(this.user).getLastName() + " | " + friendship.theOtherFriend(this.user).
                getFirstName() + " | " + friendship.getDate().toLocalDate().format(DATEFORMATTER)));
    }

    /**
     * Shows the accepted friendships.
     */
    private void acceptedFriendships() throws Exception {
        System.out.println("Accepted friendships:");
        showList(this.service::acceptedFriendships);
    }

    /**
     * Shows the pending list.
     */
    private void pendingFriendships() throws Exception {
        System.out.println("Pending list:");
        showList(this.service::pendingFriendships);
    }

    private void sendMessage() throws Exception {
        System.out.println("Introduce receivers' emails separated only by comma!");
        System.out.println("Introduce - if you don't want this message to be a reply");
        System.out.println("Introduce receivers' emails and the base text id separated by space:");
        List<String> attributes = List.of((new Scanner(System.in)).nextLine().split(" "));
        if (attributes.size() != 2)
            throw new InputException("Wrong inputs");
        System.out.println("Introduce the message:");
        String text = new Scanner(System.in).nextLine();
        service.sendMessage(attributes.get(0), text ,attributes.get(1));
        System.out.println("Message sent");
    }

    private void messagesWith() throws Exception {
        System.out.println("Introduce the email:");
        String email = (new Scanner(System.in)).nextLine();
        service.messagesWith(email).forEach(message -> {
        if (message.getFrom().equals(user)) {
            if (message.getReply() != null)
                System.out.println("   Reply at: " + message.getReply().getMessage());
            System.out.println(message.getDate().format(DATEFORMATTER) + " " + message.getMessage());
        } else {
            if (message.getReply() != null)
                System.out.println("                                           Reply at: " + message.getReply().getMessage());
            System.out.println("                                        "
                    + message.getDate().format(DATEFORMATTER) + " " + message.getMessage());

        }
        });
    }
}
