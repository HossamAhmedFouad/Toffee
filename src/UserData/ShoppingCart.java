package UserData;

import PaymentSystem.Payment;
import Products.Availability;
import Products.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ShoppingCart class represents a shopping cart containing products.
 * It provides methods to add, remove, and manage the products in the cart.
 */
public class ShoppingCart {
    private HashMap<Product,Integer> products = new HashMap<Product, Integer>();
    private double totalPrice = 0;
    private List<Observer> observers;

    /**
     * Returns the products in the shopping cart.
     *
     * @return The products in the shopping cart.
     */
    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    /**
     * Sets the products in the shopping cart to the specified products.
     *
     * @param otherProducts The products to set in the shopping cart.
     * @return true if the products were successfully set, false otherwise.
     */
    public boolean setProducts(HashMap<Product, Integer> otherProducts) {
        for (Map.Entry<Product, Integer> entry : otherProducts.entrySet()) {
            if (!addProduct(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        this.products = otherProducts;
        return true;
    }

    /**
     * Sets the observers for the shopping cart.
     *
     * @param observers The observers to set.
     */
    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    /**
     * Returns the total price of the products in the shopping cart.
     *
     * @return The total price of the products in the shopping cart.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Increases the quantity of a product in the shopping cart by 1.
     *
     * @param idx The index of the product to increase the quantity.
     */
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

    /**
     * Decreases the quantity of a product in the shopping cart by 1.
     *
     * @param idx The index of the product to decrease the quantity.
     */
    public void decreaseAmount(int idx){
        if (idx < 1 || idx > products.size()) {
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

    /**
     * Removes a product from the shopping cart.
     *
     * @param idx The index of the product to remove.
     */
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

    /**
     * Empties the shopping cart.
     * Clears the products and sets the total price to zero.
     * Notifies the observers about the update.
     */
    public void emptyCart(){
        products.clear();
        totalPrice = 0;
        for (Observer observer : observers) {
            observer.onUpdate();
        }
    }

    /**
     * Checks out the shopping cart using the specified payment method.
     *
     * @param payment The payment method to use for the checkout.
     * @return true if the payment was successful, false otherwise.
     */
    public boolean checkOut(Payment payment){
        if(payment.payOrder()){
            return true;
        }
        return false;
    }

    /**
     * Adds a product with the specified quantity to the shopping cart.
     *
     * @param product  The product to add to the shopping cart.
     * @param quantity The quantity of the product to add.
     * @return true if the product was successfully added, false otherwise.
     */
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

    /**
     * Displays the contents of the shopping cart.
     * Prints the product details, quantity, and price for each item in the cart.
     * If the cart is empty, it prints a message indicating so.
     */
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

    /**
     * Checks if the shopping cart is empty.
     *
     * @return true if the shopping cart is empty, false otherwise.
     */
    public boolean empty(){
        return products.isEmpty();
    }

}
