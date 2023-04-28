import Products.Inventory;
import UserData.Manager;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Inventory(),new Manager());
        controller.start();
    }
}