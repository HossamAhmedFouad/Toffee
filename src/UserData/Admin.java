package UserData;

import Products.Availability;
import Products.Inventory;
import Products.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Class representing an admin user.
 */
public class Admin{

    private Info info;
    private Inventory inventory;
    private Authenticator authenticator;
    private Scanner scanner = new Scanner(System.in);
    private Observer observer;

    /**
     * Get the info of the admin user.
     *
     * @return the admin user's info
     */
    public Info getInfo() {
        return info;
    }

    /**
     * Set the inventory for the admin user.
     *
     * @param inventory the inventory to set
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Set the authenticator for the admin user.
     *
     * @param authenticator the authenticator to set
     */
    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * Constructs an admin user with the specified info and observer.
     *
     * @param info     the info of the admin user
     * @param observer the observer to notify of updates
     */
    public Admin(Info info, Observer observer){
        this.info = info;
        this.observer = observer;
    }

    /**
     * Loads a product from user input.
     *
     * @return the loaded product
     */
    Product loadProduct(){
        String name,category,brand,unitType;
        double price,discountPercentage;
        Availability status = Availability.onSale;
        int quantity,choice;
        scanner.nextLine();
        System.out.print("Name: "); name = scanner.nextLine();
        System.out.print("Category: "); category = scanner.nextLine();
        System.out.print("Brand: "); brand = scanner.nextLine();
        System.out.print("Unit Type: "); unitType = scanner.nextLine();
        System.out.print("Price: "); price = scanner.nextDouble();
        System.out.print("Discount Percentage: "); discountPercentage=scanner.nextDouble();
        System.out.print("Quantity: "); quantity = scanner.nextInt();
        System.out.println("Please Choose Availability: ");
        System.out.println("1 - On Sale");
        System.out.println("2 - Not On Sale");
        System.out.println("3 - Out of Stock");
        choice = scanner.nextInt();
        if(choice==1) status = Availability.onSale;
        if(choice==2) status = Availability.notOnSale;
        if(choice==3) status = Availability.outOfStock;
        Product product = new Product(name,price,category,brand,unitType,quantity);
        product.setStatus(status);
        product.setDiscountPercentage(discountPercentage);
        return product;
    }

    /**
     * Updates the catalog by adding or removing a product.
     */
    public void updateCatalog(){
        int choice;
        while (true){
            System.out.println("Please Choose An Option");
            System.out.println("1 - Add Product To Catalog");
            System.out.println("2 - Remove Product From Catalog");
            System.out.println("3 - Go Back");
            choice = scanner.nextInt();
            if(choice==1){
                inventory.addProduct(loadProduct());
                break;
            }else if(choice==2){
                inventory.display();
                System.out.println("Select Product ID To Delete");
                choice = scanner.nextInt();
                Product product = inventory.getProducts().get(choice - 1);
                if(choice>0 && choice<=inventory.getProducts().size()) inventory.removeProduct(product);
                break;
            }else if(choice==3){
                break;
            }
        }
    }

    /**
     * Updates a product in the catalog.
     */
    public void updateProduct(){
        inventory.display();
        System.out.println("Select Product ID To Update");
        int choice = scanner.nextInt();
        if(choice>0 && choice<=inventory.getProducts().size()) inventory.editProduct(inventory.getProducts().get(choice-1),loadProduct());
    }

    /**
     * Displays all orders made by users.
     */
    public void viewAllOrders(){
        for (Map.Entry<String, User> entry : authenticator.getUsers().entrySet()) {
            User user = entry.getValue();
            System.out.println("User: " + user.getUserInfo().getName());
            System.out.println("Email: " + user.getUserInfo().getEmail());
            System.out.println("-------------------------");

            if (!user.getPrevOrders().getOrders().isEmpty()) {
                for (Order prevOrder : user.getPrevOrders().getOrders()) {
                    prevOrder.displaySummary();
                    System.out.println("===============================================");
                }
            }
            System.out.println();
        }
    }

    /**
     * Changes the status (active/suspended) of a user.
     */
    public void changeUserStatus() {
        int idx = 1;
        List<String> mails = new ArrayList<>();
        for (User usr : authenticator.getUsers().values()) {
            String formattedOutput = String.format("%-5s %-20s %s", idx, usr.getUserInfo().getName(),
                    usr.getUserInfo().getEmail());
            System.out.println(formattedOutput);
            mails.add(usr.getUserInfo().getEmail());
            idx++;
        }
        System.out.println("Please Select A User");
        int choice = scanner.nextInt();
        if (choice <= 0 || choice > mails.size()) {
            System.out.println("Error: Invalid Index");
            return;
        }
        boolean userStatus = authenticator.getUsers().get(mails.get(choice - 1)).isAccountActive();
        if (userStatus) {
            System.out.println("Do You Want To Suspend? " + mails.get(choice - 1) + " (Y/N)");
        } else {
            System.out.println("Do You Want To Unsuspend? " + mails.get(choice - 1) + " (Y/N)");
        }
        scanner.nextLine();
        String ans = scanner.nextLine();
        if (ans.equals("N")) {
            return;
        }
        authenticator.getUsers().get(mails.get(choice - 1)).setStatus(!userStatus);
        observer.onUpdate();
    }
}
