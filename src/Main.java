import Products.Inventory;
import UserData.Authenticator;

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Authenticator auth = new Authenticator();
        Authenticator.setInventory(inventory);
        Inventory.setAuthenticator(auth);
        inventory.onLoad();
        Controller controller = new Controller(inventory, auth);
        controller.start();
    }
}