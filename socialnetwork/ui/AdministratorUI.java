package socialnetwork.ui;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.InputException;
import socialnetwork.service.AdministratorService;

import java.util.List;
import java.util.Scanner;

/**
 * The interface of the administrator.
 */
public class AdministratorUI extends AbstractUI {
    public AdministratorUI(AbstractUI ui) {
        this.service = new AdministratorService(ui.service);
    }

    @Override
    public String name() { return "admin"; }

    @Override
    public void run() throws Exception {
        try {
            String input = "";
            do {
                try {
                    input = input();
                    switch (input) {
                        case "0": break;
                        case "1": addUser(); break;
                        case "2": deleteUser(); break;
                        case "3": updateUser(); break;
                        case "4": addFriendship(); break;
                        case "5": deleteFriendship(); break;
                        case "6": updateFriendship(); break;
                        case "7": showEverything(); break;
                        case "8": showFriendshipByMonth(); break;
                        case "9": showSameFirstName(); break;
                        case "10": showSameLastName(); break;
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
        System.out.println("1 - Add user");
        System.out.println("2 - Delete user");
        System.out.println("3 - Update user");
        System.out.println("4 - Add friendship");
        System.out.println("5 - Delete friendship");
        System.out.println("6 - Update friendship");
        System.out.println("7 - Show everything");
        System.out.println("8 - Show filtered friends by month");
        System.out.println("9 - Show users with the same first name");
        System.out.println("10 - Show users with the same last name");
        System.out.println("0 - Exit");
        System.out.println("Introduce one of the numbers:");
        return (new Scanner(System.in)).nextLine();
    }

    /**
     * UI adds an user;
     */
    private void addUser() throws Exception {
        System.out.println("Introduce email, first name and second name, separated by space:");
        List<String> attributes = List.of((new Scanner(System.in)).nextLine().split(" "));
        if (attributes.size() != 3)
            throw new InputException("wrong inputs for user attributes");
        service.addUser(attributes.get(0),attributes.get(1),attributes.get(2));
        System.out.println("User added");
    }

    /**
     * UI deletes an user;
     */
    private void deleteUser() throws Exception {
        System.out.println("Introduce email:");
        String attribute = (new Scanner(System.in)).nextLine();
        service.deleteUser(attribute);
        System.out.println("User deleted");
    }

    /**
     * UI updates an user;
     */
    private void updateUser() throws Exception {
        System.out.println("Introduce email, first name and second name, separated by space:");
        List<String> attributes = List.of((new Scanner(System.in)).nextLine().split(" "));
        if (attributes.size() != 3)
            throw new InputException("wrong inputs for user attributes");
        service.updateUser(attributes.get(0),attributes.get(1),attributes.get(2));
        System.out.println("User updated");
    }

    /**
     * UI adds a friendship;
     */
    private void addFriendship() throws Exception {
        System.out.println("Introduce first and second email, separated by space:");
        List<String> attributes = List.of((new Scanner(System.in)).nextLine().split(" "));
        if (attributes.size() != 2)
            throw new InputException("wrong inputs for emails");
        service.addFriendship(attributes.get(0),attributes.get(1));
        System.out.println("Friendship added");
    }

    /**
     * UI deletes a friendship;
     */
    private void deleteFriendship() throws Exception {
        System.out.println("Introduce first and second email, separated by space:");
        List<String> attributes = List.of((new Scanner(System.in)).nextLine().split(" "));
        if (attributes.size() != 2)
            throw new InputException("wrong inputs for emails");
        service.deleteFriendship(attributes.get(0),attributes.get(1));
        System.out.println("Friendship deleted");
    }

    /**
     * UI updates a friendship;
     */
    private void updateFriendship() throws Exception {
        System.out.println("Introduce first, second, third and fourth email, separated by space:");
        List<String> attributes = List.of((new Scanner(System.in)).nextLine().split(" "));
        if (attributes.size() != 4)
            throw new InputException("wrong inputs for emails");
        service.updateFriendship(attributes.get(0),attributes.get(1),attributes.get(2),attributes.get(3));
        System.out.println("Friendship updated");
    }

    /**
     * Shows the friendships and users.
     */
    private void showEverything() throws Exception {
        System.out.println("Users:");
        for (User user : service.getUsers())
            System.out.println(user.toString());
        System.out.println("\nFriendships:");
        for (Friendship friendship : service.getFriendships())
            System.out.println(friendship.toString());
    }

    private void showFriendshipByMonth() throws Exception {
        System.out.println("Introduce email and month(in number format please) separated by space: ");
        List<String> attributes = List.of((new Scanner(System.in)).nextLine().split(" "));
        if (attributes.size() != 2)
            throw new InputException("Wrong inputs");
        String fin = service.getFriendshipByMonth(service.findUserByEmail(attributes.get(0)),Integer.parseInt(attributes.get(1)));
        System.out.println(fin);
    }

    private void showSameFirstName() throws Exception {
        System.out.println("Introduce the first name : ");
        String first_name = new Scanner(System.in).nextLine();
        List<User> fin = service.findUsersByFirstName(first_name);
        fin.stream().forEach(user -> System.out.println(user.toString()));
    }

    private void showSameLastName() throws Exception {
        System.out.println("Introduce the last name : ");
        String last_name = new Scanner(System.in).nextLine();
        List<User> fin = service.findUsersByLastName(last_name);
        fin.stream().forEach(user -> System.out.println(user.toString()));
    }
}
