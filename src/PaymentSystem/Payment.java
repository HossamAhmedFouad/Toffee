package PaymentSystem;
import UserData.User;

public abstract class Payment {
    protected User user;

    public Payment(User user){
        this.user = user;
    }

    public abstract boolean payOrder();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
