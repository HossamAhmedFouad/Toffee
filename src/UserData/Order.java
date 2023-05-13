package UserData;
import Products.Product;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * The Order class represents an order.
 */
public class Order {
    private static int id = 0; // The ID counter for generating unique order IDs
    private int orderID; // The unique ID of the order
    private double totalPrice; // The total price of the order
    private Date date; // The date of the order
    private String shipAddress; // The shipping address of the order
    private HashMap<Product,Integer> products; // The products and their quantities in the order
    private double discount; // The discount applied to the order
    
    /**
     * Constructs an order with the specified details.
     *
     * @param totalPrice   the total price of the order
     * @param date         the date of the order
     * @param shipAddress  the shipping address for the order
     * @param products     the map of products and their quantities in the order
     */
    public Order(double totalPrice, Date date, String shipAddress, HashMap<Product, Integer> products) {
        id++;
        orderID = id;
        this.totalPrice = totalPrice;
        this.date = date;
        this.shipAddress = shipAddress;
        this.products = products;
        discount = 0;
    }

    /**
     * Returns the ID of the order.
     *
     * @return the order ID
     */
    public int getID() {
        return orderID;
    }

    /**
     * Returns the total price of the order.
     *
     * @return the total price of the order
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Returns the date of the order.
     *
     * @return the date of the order
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Returns the shipping address for the order.
     *
     * @return the shipping address
     */
    public String getShipAddress() {
        return shipAddress;
    }

    /**
     * Returns the map of products and their quantities in the order.
     *
     * @return the map of products and their quantities
     */
    public HashMap<Product,Integer>  getProducts() {
        return products;
    }

    /**
     * Sets the discount for the order.
     *
     * @param discount the discount to set
     */
    public void setDiscount(double discount){
        this.discount = discount;
    }
    /**
     * Displays a summary of the order, including sub-total, individual product details, delivery fee,
     * discount (if applicable), and the order total.
     */
    public void displaySummary() {
        double subTotal = totalPrice;
        int items = 0;
        double deliveryFee = 20;
        double orderTotal = subTotal + deliveryFee - discount;
        
        System.out.println("Order Summary:");
        System.out.printf("%-20s%.2f EGP\n", "Sub-total", subTotal);
        
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            
            items += quantity;
            
            System.out.printf("%-20s%s\n", "Product Name:", product.getName());
            System.out.printf("%-20s%s\n", "Brand:", product.getBrand());
            System.out.printf("%-20s%s\n", "Category:", product.getCategory());
            System.out.printf("%-20s%.2f EGP\n", "Price per Item:", product.getPrice());
            System.out.printf("%-20s%d\n", "Quantity:", quantity);
            System.out.println("--------------------------");
        }
        
        System.out.printf("%-20s%d\n", "Items", items);
        System.out.printf("%-20s%.2f EGP\n", "Delivery Fee", deliveryFee);
        
        if (discount > 0) {
            System.out.printf("%-20s%.2f EGP\n", "Discount:", discount);
        }
        
        System.out.printf("%-20s%.2f EGP\n", "Order Total", orderTotal);
        System.out.printf("%-20s%s\n", "Order Date:", date);
    }
}
