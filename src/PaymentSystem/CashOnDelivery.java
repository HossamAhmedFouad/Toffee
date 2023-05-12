package PaymentSystem;
import UserData.User;

public class CashOnDelivery extends Payment {
    
    public CashOnDelivery(User user) {
        super(user);
    }

    @Override
    public boolean payOrder() {
        if(user.getUserInfo().getAddress().isEmpty()){
            return false;
        }
        return true;
    }
}
