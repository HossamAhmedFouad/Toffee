package Products;

import java.util.ArrayList;
import java.util.List;

import UserData.CartObserver;
import UserData.DataManager;
import UserData.Voucher;

public class Inventory extends DataManager implements CartObserver{

    private List<Product> products;
    private List<Voucher> vouchers;
    private int loyalty = 20;
    
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
    
    @Override
    protected void uploadData() {
        List<String> productLines = new ArrayList<>();
        List<String> voucherLines = new ArrayList<>();
        productLines.add("ID,Name,Price,Category,Brand,Unit Type,Quantity\n");
        voucherLines.add("Code,Amount\n");
        for (Product product : products) {
            int id = product.getID();
            System.out.println(id);
            String name = product.getName();
            Double price = product.getPrice();
            String category = product.getCategory();
            String brand = product.getBrand();
            String unitType = product.getUnitType();
            int quantity = product.getQuantity();
            String line = String.format("%s,%s,%s,%s,%s,%s,%s\n", id, name, price, category, brand, unitType, quantity);
            productLines.add(line);
        }
        for (Voucher voucher : vouchers) {
            String code = voucher.getCode();
            Double amount = voucher.getAmount();
            String line = String.format("%s,%s\n", code, amount);
            voucherLines.add(line);
        }
    
        saveToFile(System.getProperty("user.dir") + "/src/Products/stock.csv", productLines);
        saveToFile(System.getProperty("user.dir") + "/src/Products/vouchers.csv", voucherLines);
    }

    @Override
    protected void loadData() {
        List<String> productLines = readFromFile(System.getProperty("user.dir") + "/src/Products/stock.csv");
        List<String> voucherLines = readFromFile(System.getProperty("user.dir") + "/src/Products/vouchers.csv");
        products = new ArrayList<Product>();
        vouchers = new ArrayList<Voucher>();
        for (String line : productLines) {
            String[] data = line.split(",", 8);
            int id = Integer.parseInt(data[0]);
            String name = data[1];
            Double price = Double.parseDouble(data[2]);
            String category = data[3];
            String brand = data[4];
            String unitType = data[5];
            int quantity = Integer.parseInt(data[6]);
            Product product = new Product(name, price, category, brand, unitType, quantity);
            product.setID(id);
            products.add(product);
        }
    
        for (String line : voucherLines) {
            String[] data = line.split(",", 2);
            String code = data[0];
            Double amount = Double.parseDouble(data[1]);
            vouchers.add(new Voucher(code, amount));
        }
    }

    public int getLoyalty(){
        return loyalty;
    }

    public void setLoyalty(int loyalty){
        this.loyalty = loyalty;
    }

    @Override
    public void onCheckout() {
        uploadData();
    }

}
