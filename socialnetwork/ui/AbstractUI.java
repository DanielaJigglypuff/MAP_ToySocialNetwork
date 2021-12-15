package socialnetwork.ui;

import socialnetwork.domain.exceptions.FileException;
import socialnetwork.service.AbstractService;

import java.util.Scanner;

import static socialnetwork.domain.constants.PersonalConstants.*;

/**
 * Abstract interface.
 */
public class AbstractUI {
    protected AbstractService service;

    public AbstractUI() {
        try {
            service = new AbstractService(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Logs in the admin/ user.
     */
    public void login() throws Exception {
        System.out.println("*****************************************");
        System.out.println("Introduce the email for login: ");
        String email = (new Scanner(System.in)).nextLine();
        try {
            AbstractUI ui = null;
            if (email.equals("admin")) ui = new AdministratorUI(this);
            else if (service.findUserByEmail(email) != null) ui = new UserUI(this, service.findUserByEmail(email));
            if (ui != null) {
                System.out.println("Welcome " + ui.name() + "!");
                ui.run();
                return;
            }
            System.out.println("invalid email");
        } catch (FileException e) {
            System.out.println(e.getMessage());
//throw e;
        }
    }

    /**
     * Runs the menu loop.
     */
    public void run() throws Exception {}

    /**
     * Shows the menu;
     * @return the input;
     */
    protected String input() { return null; }

    public String name() { return null; }
}
