package PaymentSystem;

import UserData.Authenticator;
import UserData.Order;
import UserData.User;

public class CashOnDelivery extends Payment {
    public CashOnDelivery(Order order, User user) {
        super(order, user);
    }

    @Override
    public boolean payOrder() {
        User user=getUser();
        if(user.getUserInfo().getAddress().isEmpty()){ //
            return false;
        }

        return true;
    }
}
