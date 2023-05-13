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
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Authenticator auth = Inventory.getAuthenticator();
        Controller controller = new Controller(inventory, auth);
        controller.start();
    }
}
