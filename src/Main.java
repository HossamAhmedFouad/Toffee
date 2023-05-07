import Products.Inventory;
import UserData.Authenticator;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Inventory(),new Authenticator());
        controller.start();
        controller = null;
        System.gc();
//        System.out.println(System.getProperty("user.dir"));
    }
}