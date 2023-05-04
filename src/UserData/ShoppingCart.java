package UserData;

import PaymentSystem.Payment;
import Products.Availability;
import Products.Product;

import java.util.HashMap;

public class ShoppingCart {
    private HashMap<Product,Integer> products = new HashMap<Product, Integer>();
    private double totalPrice=0;
    private double discount=0;

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product,Integer> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount=discount;
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
            if (idx - 1 == 0) {
                addProduct(product);
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
            System.out.println("ERR INVALID ID");
            return;
        }
        for(Product product : products.keySet()){
            if (idx - 1 == 0) {
                product.setQuantity(product.getQuantity() + products.get(product));
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

    public boolean checkOut(Payment payment){
        if(payment.payOrder()){
            //empty cart
            return true;
        }
        return false;
        //TODO: checkout and empty cart
    }

    public void addProduct(Product product) {
        if (product.getStatus() != Availability.outOfStock) {
            product.setQuantity(product.getQuantity() - 1);
        } else {
            System.out.println("Product is out of stock");
            return;
        }
        products.put(product, products.getOrDefault(product, 0) + 1);
        totalPrice += product.getPrice();
    }

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
        if(discount>0){System.out.printf("%-20s%.2f EGP\n", "Delivery Fee", deliveryFee);}
        System.out.println("Order Total:");
        System.out.printf("%.2f", orderTotal);
    }

    public boolean empty(){
        return products.isEmpty();
    }

}
