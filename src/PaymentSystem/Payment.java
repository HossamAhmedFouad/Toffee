package PaymentSystem;

import UserData.User;

/**
 * Abstract class representing a payment.
 */
public abstract class Payment {
    /**
     * The user associated with the payment.
     */
    protected User user;

    /**
     * Constructs a payment with the specified user.
     *
     * @param user the user associated with the payment
     */
    public Payment(User user){
        this.user = user;
    }

    /**
     * Performs the payment for the order.
     *
     * @return true if the payment is successful, false otherwise
     */
    public abstract boolean payOrder();
}
