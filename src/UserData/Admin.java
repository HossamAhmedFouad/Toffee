package UserData;

import Products.Availability;
import Products.Inventory;
import Products.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin {

    private Inventory inventory;
    private String username,password;
    private Authenticator authenticator;
    private Scanner scanner;


    public Admin(String username,String password,Inventory inventory,Authenticator authenticator){
        this.username =username;
        this.password = password;
        this.inventory = inventory;
        this.authenticator = authenticator;
    }

    Product loadProduct(){
        String name,category,brand,unitType;
        double price,discountPercentage;
        Availability status = Availability.onSale;
        int quantity,choice;
        scanner.nextLine();
        System.out.println("Name: "); name = scanner.nextLine();
        System.out.println("Category: "); category = scanner.nextLine();
        System.out.println("Brand: "); brand = scanner.nextLine();
        System.out.println("Unit Type: "); unitType = scanner.nextLine();
        System.out.println("Price: "); price = scanner.nextDouble();
        System.out.println("Discount Percentage: "); discountPercentage=scanner.nextDouble();
        System.out.println("Quantity: "); quantity = scanner.nextInt();
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
        scanner = new Scanner(System.in);
        int choice;
        while (true){
            System.out.println("PLEASE CHOOSE AN OPTION");
            System.out.println("1 - Add Product To Catalog");
            System.out.println("2 - Remove Product From Catalog");
            scanner.nextLine();
            choice = scanner.nextInt();
            if(choice==1){
                inventory.addProduct(loadProduct());
                break;
            }else if(choice==2){
                inventory.display();
                System.out.println("Select Product ID To Delete");
                choice = scanner.nextInt();
                if(choice>0 && choice<=inventory.getProducts().size()) inventory.getProducts().remove(choice-1);
                break;
            }
        }

    }

    public void updateProduct(Product product){
        inventory.display();
        System.out.println("Select Product ID To Update");
        int choice = scanner.nextInt();
        if(choice>0 && choice<=inventory.getProducts().size()) inventory.editProduct(inventory.getProducts().get(choice-1),loadProduct());

    }

    public void updateLoyaltyVal(int val){
        inventory.setLoyalty(val);
    }

    public void viewAllOrders(){
        //TODO: display all orders from CSV file
    }

    public void suspendUser(){
        int idx = 1;
        List<String> mails = new ArrayList<>();
        for(User usr : authenticator.getUsers().values()){
            System.out.println(idx + " " + usr.getUserInfo().getName() + " " + usr.getUserInfo().getEmail());
            mails.add(usr.getUserInfo().getEmail());
            idx++;
        }
        System.out.println("Please Select A User to suspend");
        int choice = scanner.nextInt();
        authenticator.getUsers().get(mails.get(choice-1)).setAccountActive(false);
    }



}
