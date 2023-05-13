package UserData;

import Products.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The PreviousOrders class represents a collection of previous orders made by a user.
 * It provides methods to manage and retrieve orders.
 */
public class PreviousOrders {
    private List<Order> orders;

    /**
     * Constructs an empty PreviousOrders object.
     */
    public PreviousOrders(){
        orders = new ArrayList<>();
    }

    /**
     * Returns the list of orders.
     *
     * @return The list of orders.
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Sets the list of orders.
     *
     * @param orders The list of orders to set.
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * Reorders the products from a given order.
     * Returns a HashMap of products and quantities from the order.
     *
     * @param order The order to reorder.
     * @return A HashMap of products and quantities.
     */
    public HashMap<Product,Integer> reorder(Order order){
        return order.getProducts();
    }

    /**
     * Displays the summary of a specific order.
     *
     * @param order The order to display.
     */
    public void displayOrder(Order order){
        order.displaySummary();
    }

    /**
     * Displays the summary of all orders.
     */
    public void viewOrders(){
        for(int i=1;i<=orders.size();i++){
            System.out.println("========== ORDER ID " + i + " ===============");
            orders.get(i-1).displaySummary();
            System.out.println("====================================");
        }
    }

    /**
     * Adds a new order to the list of orders.
     *
     * @param order The order to add.
     */
    public void addOrder(Order order){
        orders.add(order);
    }
}