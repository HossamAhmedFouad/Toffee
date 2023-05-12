package Products;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import UserData.Observer;
import UserData.User;
import UserData.Authenticator;
import UserData.DataManager;
import UserData.Info;
import UserData.Voucher;

public class Inventory extends DataManager implements Observer{

    private List<Product> products;
    private List<Voucher> vouchers;
    private static Authenticator authenticator;
    private int loyalty = 20;
    
    public void addProduct(Product product) {
        products.add(product);
        uploadData();
    }
    
    public static void setAuthenticator(Authenticator auth) {
        authenticator = auth;
    }
    
    public void removeProduct(Product product){
        if(products.isEmpty()) return;
        products.removeIf(x -> x.getProductID() == product.getProductID());
        Product.setID(Product.getID()-1);
        uploadData();
    }

    public void editProduct(Product old, Product edit){
        if (products.isEmpty())
            return;
        edit.setProductID(old.getProductID());
        products.removeIf(x -> x.getProductID() == old.getProductID());
        products.add(edit);
        Product.setID(Product.getID()-1);
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
    
    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public void removeVoucher(int idx) {
        vouchers.remove(idx);
        uploadData();
    }
    
    @Override
    protected void uploadData() {
        List<String> productLines = new ArrayList<>();
        List<String> voucherLines = new ArrayList<>();
        productLines.add("ID,Name,Price,Category,Brand,Unit Type,Quantity,Discount Percentage,Availability\n");
        voucherLines.add("Email,Code,Amount\n");
        for (Product product : products) {
            int id = product.getProductID();
            String name = product.getName();
            Double price = product.getPrice();
            String category = product.getCategory();
            String brand = product.getBrand();
            String unitType = product.getUnitType();
            int quantity = product.getQuantity();
            Double discount = product.getDiscountPercentage();
            Availability availability = product.getStatus();
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", id, name, price, category, brand, unitType,
                    quantity, discount, availability);
            productLines.add(line);
        }
        for (Voucher voucher : vouchers) {
            String email = "null";
            String code = voucher.getCode();
            Double amount = voucher.getAmount();
            
            String line = String.format("%s,%s,%s\n", email, code, amount);
            voucherLines.add(line);
        }
        for (Map.Entry<String, User> entry : authenticator.getUsers().entrySet()) {
            User user = entry.getValue();
            for(Voucher voucher : user.getVouchers()){
                String email = entry.getKey();
                String code = voucher.getCode();
                Double amount = voucher.getAmount();
                String line = String.format("%s,%s,%s\n", email, code, amount);
                voucherLines.add(line);
            }
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
            String[] data = line.split(",", 10);
            String name = data[1];
            Double price = Double.parseDouble(data[2]);
            String category = data[3];
            String brand = data[4];
            String unitType = data[5];
            int quantity = Integer.parseInt(data[6]);
            Double discount = Double.parseDouble(data[7]);
            Availability availability = null;
            String status = data[8];
            if (status.equals("onSale")) {
                availability = Availability.onSale;
            } else if (status.equals("notOnSale")) {
                availability = Availability.notOnSale;
            } else if (status.equals("outOfStock")) {
                availability = Availability.outOfStock;
            }
            Product product = new Product(name, price, category, brand, unitType, quantity);
            product.setDiscountPercentage(discount);
            product.setStatus(availability);
            products.add(product);
        }
        authenticator.onLoad();
        for (String line : voucherLines) {
            String[] data = line.split(",", 3);
            String email = data[0];
            String code = data[1];
            Double amount = Double.parseDouble(data[2]);
            if (!email.equals("null")) {
                Info info = new Info();
                info.setEmail(email);
                User user = authenticator.getUser(info);
                user.addVouchers(new Voucher(code, amount));
            } else {
                vouchers.add(new Voucher(code, amount));
            }
        }
    }

    public int getLoyalty(){
        return loyalty;
    }

    public void setLoyalty(int loyalty){
        this.loyalty = loyalty;
    }

    @Override
    public void onUpdate() {
        uploadData();
    }

    @Override
    public void onLoad() {
        loadData();
    }


}
