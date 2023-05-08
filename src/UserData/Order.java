package UserData;

import Products.Product;

import java.util.Date;
import java.util.HashMap;

public class Order {

    private double totalPrice;
    private Date date;
    private String shipAddress;
    private HashMap<Product,Integer> products;

    private double discount;


    
    public Order(double totalPrice, Date date, String shipAddress, HashMap<Product,Integer>  products) {
        this.totalPrice = totalPrice;
        this.date = date;
        this.shipAddress = shipAddress;
        this.products = products;
        discount = 0;
    }

    public Order(Order other){
        this.totalPrice = other.totalPrice;
        this.date = other.date;
        this.shipAddress = other.shipAddress;
        this.products = other.products;
        this.discount = other.discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public HashMap<Product,Integer>  getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product,Integer>  products) {
        this.products = products;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }

    public double getDiscount(){
        return discount;
    }

    public void displaySummary(){
        double subTotal = totalPrice;
        int items=0;
        double deliveryFee = 20;
        double orderTotal = subTotal + deliveryFee-discount;
        for (Product product : products.keySet()) {
            items+=products.get(product);
        }
        System.out.println("Order Summary:");
        System.out.printf("%-20s%.2f EGP\n", "Sub-total", subTotal);
        System.out.printf("%-20s%d\n", "Items", items);
        System.out.printf("%-20s%.2f EGP\n", "Delivery Fee", deliveryFee);
        if(discount>0){System.out.printf("%-20s%.2f EGP\n", "Discount: ", discount);}
        System.out.print("Order Total:");
        System.out.printf("%.2f\n", orderTotal);
    }


    public int calcLoyalty(){
        //TODO : Calculate loyalty points
        return 0;
    }
}
