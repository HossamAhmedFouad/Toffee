package UserData;

import PaymentSystem.Payment;
import Products.Availability;
import Products.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private HashMap<Product,Integer> products = new HashMap<Product, Integer>();
    private double totalPrice = 0;
    private List<Observer> observers;
    
    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public boolean setProducts(HashMap<Product, Integer> otherProducts) {
        for (Map.Entry<Product, Integer> entry : otherProducts.entrySet()) {
            if (!addProduct(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        this.products = otherProducts;
        return true;
    }
    
    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void increaseAmount(int idx) {
        if (idx < 1 || idx > products.size()) {
            System.out.println("INVALID ID");
            return;
        }
        for (Product product : products.keySet()) {
            if (idx - 1 == 0) {
                addProduct(product,1);
                break;
            }
            idx--;
        }
    }
    
    public void decreaseAmount(int idx){
        if(idx<1 || idx>products.size()){
            System.out.println("INVALID ID");
            return;
        }
        for (Product product : products.keySet()) {
            if(idx-1==0) {
                if(products.get(product)==1) products.remove(product);
                else products.put(product, products.get(product) - 1);
                product.setQuantity(product.getQuantity() + 1);
                totalPrice -= product.getPrice();
                break;
            }
            idx--;
        }
    }

    public void removeProduct(int idx){
        if(idx<1 || idx>products.size()){
            System.out.println("INVALID ID");
            return;
        }
        for(Product product : products.keySet()){
            if (idx - 1 == 0) {
                product.setQuantity(product.getQuantity() + products.get(product));
                totalPrice -= product.getPrice() * products.get(product);
                products.remove(product);
                break;
            }
            idx--;
        }
    }

    public void emptyCart(){
        products.clear();
        totalPrice = 0;
        for (Observer observer : observers) {
            observer.onUpdate();
        }
    }

    public boolean checkOut(Payment payment){
        if(payment.payOrder()){
            //empty cart
            return true;
        }
        return false;
        //TODO: checkout and empty cart
    }

    public boolean addProduct(Product product, int quantity) {
        if (product.getStatus() != Availability.outOfStock && product.getQuantity() - quantity >= 0) {
            product.setQuantity(product.getQuantity() - quantity);
        } else {
            return false;
        }
        products.put(product, products.getOrDefault(product, 0) + quantity);
        totalPrice += (product.getPrice() * quantity);
        return true;
    }
    //this two fun can put in controller instead of here
    public void display(){
        if(products.isEmpty()){
            System.out.println("Cart Is Empty");
            return;
        }
        int cnt = 1;
        String format = "%-5s %-20s %-5s $%-10s\n";
        System.out.format(format, "No.", "Product", "Qty", "Price");
        for(Product product : products.keySet()){
            System.out.format(format, cnt++, product.getName(), products.get(product), String.format("%.2f", product.getPrice()*products.get(product)));
        }
        System.out.format("\n%-25s $%-10.2f\n", "Total Price:", totalPrice);
    }

    public boolean empty(){
        return products.isEmpty();
    }

}
