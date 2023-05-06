package Products;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import UserData.DataManager;
import UserData.Voucher;

public class Inventory extends DataManager{

    public void addProduct(Product product){
        products.add(product);
        uploadData();
    }

    public void removeProduct(Product product){
        if(products.isEmpty()) return;
        products.removeIf(x -> x.getName().equals(product.getName()));
        uploadData();
    }

    public void editProduct(Product old, Product edit){
        if(products.isEmpty()) return;
        products.removeIf(x -> x.getName().equals(old.getName()));
        products.add(edit);
        uploadData();
    }

    public void setAvailability(Product product, Availability status) {
        for (Product value : products) {
            if (value.getName().equals(product.getName())) {
                value.setStatus(status);
                break;
            }
        }
        uploadData();
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void display(){
        if(products.isEmpty()){
            System.out.println("There Are No Products");
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

    public List<Product> search(String name) {
        List<Product> found = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equals(name))
                found.add(product);
        }
        return found;
    }
    
    public List<Voucher> getVoucher() {
        return vouchers;
    }

    public void update() {
        uploadData();
    }
    
    @Override
    protected void uploadData() {
        try {
            myWriter = new FileWriter(System.getProperty("user.dir") + "/src/Products/stock.csv");
            for (Product product : products) {
                String name = product.getName();
                Double price = product.getPrice();
                String category = product.getCategory();
                String brand = product.getBrand();
                String unitType = product.getUnitType(); 
                int quantity = product.getQuantity();
                String line = String.format("%s,%s,%s,%s,%s,%s\n", name, price, category, brand, unitType, quantity);
                myWriter.write(line);
            }
            myWriter.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            myWriter = new FileWriter(System.getProperty("user.dir") + "/src/Products/vouchers.csv");
            for (Voucher voucher : vouchers) {
                String code = voucher.getCode();
                Double amount = voucher.getAmount();
                String line = String.format("%s,%s\n", code, amount);
                myWriter.write(line);
            }
            myWriter.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        //Loading vouchers data
        try {
            scanner = new Scanner(new File(System.getProperty("user.dir") + "/src/Products/vouchers.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] data = line.split(",", 3);
            String code = data[0];
            Double amount = Double.parseDouble(data[1]);
            vouchers.add(new Voucher(code,amount));
        }
        //Loading products data
        try {
            scanner = new Scanner(new File(System.getProperty("user.dir") + "/src/Products/stock.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] data = line.split(",", 7);
            String name = data[0];
            Double price = Double.parseDouble(data[1]);
            String category = data[2];
            String brand = data[3];
            String unitType = data[4]; 
            int quantity = Integer.parseInt(data[5]);
            products.add(new Product(name, price, category, brand, unitType, quantity));
        }
    }

}
