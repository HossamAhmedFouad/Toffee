package Products;

import UserData.Info;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Inventory {
    /**
     * Attributes
     */
    private List<Product> products = new ArrayList<Product>();
    /**
     * Operations
     */
    public Inventory(){
        Scanner scanner;
        try {
            scanner = new Scanner(new File(System.getProperty("user.dir") + "/src/Products/stock.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            String[] data = line.split(",",7);
            products.add(new Product(data[0],Double.parseDouble(data[1]),data[2],data[3],data[4],Integer.parseInt(data[5])));
        }
        scanner.close();

    }
    public void addProduct(Product product){
        products.add(product);
    }

    public void removeProduct(Product product){
        if(products.isEmpty()) return;
        products.removeIf(x -> x.getName().equals(product.getName()));
    }

    public void editProduct(Product old, Product edit){
        if(products.isEmpty()) return;
        products.removeIf(x -> x.getName().equals(old.getName()));
        products.add(edit);
    }

    public void setAvailability(Product product, Availability status){
        for (Product value : products) {
            if (value.getName().equals(product.getName())) {
                value.setStatus(status);
                break;
            }
        }
    }
    public List<Product> getProducts(){
        return products;
    }
    public void display(){
        if(products.isEmpty()){
            System.out.println("There are no products");
            return;
        }
        int cnt = 1;
        for(Product product : products){
            System.out.println("=====PRODUCT " + cnt++ +" =======");
            System.out.println("Name: " + product.getName());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Category: " + product.getCategory());
            System.out.println("Brand: " + product.getBrand());
            System.out.println("Unit Type: " + product.getUnitType());
            System.out.println("Status: " + product.getStatus());
            System.out.println("Discount: " + product.getDiscountPercentage());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println("=========================");
        }
    }

    public List<Product> search(String name){
        List<Product>found = new ArrayList<>();
        for(Product product : products){
            if(product.getName().equals(name)) found.add(product);
        }
        return found;
    }

}
