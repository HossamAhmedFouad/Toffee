package PaymentSystem;

import UserData.Authenticator;
import UserData.Order;
import UserData.User;

public abstract class Payment {
    private Order order;
    private User user;
    private Authenticator auth;

    public abstract boolean payOrder();

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Authenticator getAuth() {
        return auth;
    }

    public void setAuth(Authenticator auth) {
        this.auth = auth;
    }
}
