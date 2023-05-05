package UserData;

import java.util.List;

public class PreviousOrders {
    private List<Order> orders;

    public PreviousOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void reorder(Order order){
        //TODO : Reorder
    }

    public void displayOrder(Order order){
        //TODO: display order details -- Discount Dilemma
    }

    public void viewOrders(){
        // TODO: display all orders
    }

    public void addOrder(Order order){
        orders.add(order);
    }
}
