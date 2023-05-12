package UserData;

import Products.Availability;
import Products.Inventory;
import Products.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Admin{

    private Info info;
    private Inventory inventory;
    private Authenticator authenticator;
    private Scanner scanner = new Scanner(System.in);
    private Observer observer;

    public Info getInfo() {
        return info;
    }
    public void setInfo(Info info) {
        this.info = info;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public Admin(Info info, Observer observer){
        this.info = info;
        this.observer = observer;
    }

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
        System.out.println("PLEASE CHOOSE AVAILABILITY");
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

    public void updateCatalog(){
        int choice;
        while (true){
            System.out.println("PLEASE CHOOSE AN OPTION");
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

    public void updateProduct(){
        inventory.display();
        System.out.println("Select Product ID To Update");
        int choice = scanner.nextInt();
        if(choice>0 && choice<=inventory.getProducts().size()) inventory.editProduct(inventory.getProducts().get(choice-1),loadProduct());
    }

    public void updateLoyaltyVal(int val){
        inventory.setLoyalty(val);
    }

    public void viewAllOrders(){
        for (Map.Entry<String, User> entry : authenticator.getUsers().entrySet()) {
            User user = entry.getValue();
            System.out.println("User: " + user.getUserInfo().getName());
            System.out.println("Email: " + user.getUserInfo().getEmail());
            System.out.println("-------------------------");
        
            if (!user.getPrevOrders().getOrders().isEmpty()) {
                for (Order prevOrder : user.getPrevOrders().getOrders()) {
                    prevOrder.displaySummary();
                }
            }
            System.out.println();
        }
    }

    public void changeUserStatus(){
        int idx = 1;
        List<String> mails = new ArrayList<>();
        for (User usr : authenticator.getUsers().values()) {
            String formattedOutput = String.format("%-5s %-20s %s", idx, usr.getUserInfo().getName(), usr.getUserInfo().getEmail());
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
            System.out.println("Do you want to suspend? " + mails.get(choice - 1) + " (Y/N)");
        } else {
            System.out.println("Do you want to unsuspend? " + mails.get(choice - 1) + " (Y/N)");
        }
        scanner.nextLine();
        String ans = scanner.nextLine();
        if(ans.equals("N")) {
            return;
        }
        authenticator.getUsers().get(mails.get(choice - 1)).setStatus(!userStatus);
        observer.onUpdate();
    }
}
