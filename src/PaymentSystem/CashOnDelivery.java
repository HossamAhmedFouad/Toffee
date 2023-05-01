package PaymentSystem;

import UserData.Authenticator;
import UserData.Order;
import UserData.User;

public class CashOnDelivery extends Payment {
    @Override
    public boolean payOrder() {
        User user=getUser();
        if(user.getUserInfo().getAddress().isEmpty()){ //
            return false;
        }

        return true;
    }
}
