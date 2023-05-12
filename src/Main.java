import Products.Inventory;
import UserData.Authenticator;

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Authenticator.setInventory(inventory);
        Authenticator auth = new Authenticator();
        Controller controller = new Controller(inventory, auth);
        controller.start();
    }
}