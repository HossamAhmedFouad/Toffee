package UserData;

import PaymentSystem.Payment;
import Products.Product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order {

    private double totalPrice;
    private Date date;
    private String shipAddress;
    private HashMap<Product,Integer> products = new HashMap<Product, Integer>();
    private Payment payment;

    public Order(double totalPrice, Date date, String shipAddress, HashMap<Product,Integer>  products, Payment payment) {
        this.totalPrice = totalPrice;
        this.date = date;
        this.shipAddress = shipAddress;
        this.products = products;
        this.payment = payment;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public int calcLoyalty(){
        //TODO : Calculate loyalty points
        return 0;
    }
}
