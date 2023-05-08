package UserData;

import Products.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreviousOrders {
    private List<Order> orders;

    public PreviousOrders(){
        orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public HashMap<Product,Integer> reorder(Order order){
        return order.getProducts();
    }

    public void displayOrder(Order order){
        order.displaySummary();
    }

    public void viewOrders(){
        for(int i=1;i<=orders.size();i++){
            System.out.println("==========ORDER ID " + i + " ===============");
            orders.get(i-1).displaySummary();
            System.out.println("====================================");
        }
    }

    public void addOrder(Order order){
        orders.add(order);
    }
}
