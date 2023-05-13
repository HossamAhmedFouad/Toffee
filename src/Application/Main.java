package Application;

import Products.Inventory;
import UserData.Authenticator;

/**
 * The entry point for the application.
 */
public class Main {

    /**
     * The main method that starts the application.
     *
     * @param args The command-line arguments.
     */
    /*Note: Email Authentication doesn't work on some devices, but works on others.
    We don't know the reason so we added a way to bypass authentication if needed.*/
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Authenticator auth = Inventory.getAuthenticator();
        Controller controller = new Controller(inventory, auth);
        controller.start();
    }
}
