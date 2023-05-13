package PaymentSystem;

import UserData.User;

/**
 * Class representing a cash on delivery payment method.
 * Extends the Payment class.
 */
public class CashOnDelivery extends Payment {

    /**
     * Constructs a cash on delivery payment with the specified user.
     *
     * @param user the user associated with the payment
     */
    public CashOnDelivery(User user) {
        super(user);
    }

    /**
     * Performs the cash on delivery payment for the order.
     * Checks if the user's address is empty and returns false if it is.
     *
     * @return true if the payment is successful, false otherwise
     */
    @Override
    public boolean payOrder() {
        if(user.getUserInfo().getAddress().isEmpty()){
            return false;
        }
        return true;
    }
}