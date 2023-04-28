package UserData;

import PaymentSystem.Payment;
import Products.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart {
    private HashMap<Product,Integer> products = new HashMap<Product, Integer>();
    private double totalPrice;

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product,Integer> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void increaseAmount(int idx){
        if(idx<1 || idx>products.size()){
            System.out.println("ERR INVALID ID");
            return;
        }
        for(Product product : products.keySet()){
            if(idx-1==0) {
                products.put(product,products.get(product)+1);
                totalPrice+=product.getPrice();
                break;
            }
            idx--;
        }

    }
    public void decreaseAmount(int idx){
        if(idx<1 || idx>products.size()){
            System.out.println("ERR INVALID ID");
            return;
        }
        for(Product product : products.keySet()){
            if(idx-1==0) {
                if(products.get(product)==1) products.remove(product);
                else products.put(product, products.get(product) - 1);
                totalPrice-=product.getPrice();
                break;
            }
            idx--;
        }
    }

    public void removeProduct(int idx){
        if(idx<1 || idx>products.size()){
            System.out.println("ERR INVALID ID");
            return;
        }
        for(Product product : products.keySet()){
            if(idx-1==0) {
                totalPrice-= product.getPrice() * products.get(product);
                products.remove(product);
                break;
            }
            idx--;
        }
    }

    public void emptyCart(){
        products.clear();
        totalPrice = 0;
    }

    public void checkOut(Payment payment,String ShippingAddress){
        //TODO: checkout and empty cart
    }

    public void addProduct(Product product){
        products.put(product,products.getOrDefault(product,0)+1);
    }

    public void display(){
        if(products.isEmpty()){
            System.out.println("Cart Is Empty");
            return;
        }
        int cnt = 1;
        String space = "     ";
        System.out.println("#     Name     Quantity");
        for(Product product : products.keySet()){
            System.out.println(cnt++ + space + product.getName() + space +  products.get(product));
        }
    }

    public boolean empty(){
        return products.isEmpty();
    }

}
