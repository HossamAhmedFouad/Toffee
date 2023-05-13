package UserData;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user in the system.
 * It contains information about the user, their card details, account status, shopping cart, previous orders,
 * loyalty points, and vouchers.
 */
public class User {
    private Info userInfo;
    private Card userCard;
    private boolean accountActive = true;
    private ShoppingCart userCart = new ShoppingCart();
    private PreviousOrders orders;
    private int loyaltyPoints;
    private List<Voucher> vouchers = new ArrayList<Voucher>();

    /**
     * Creates a new User instance with the specified user information.
     *
     * @param info The user information.
     */
    public User(Info info) {
        userInfo = info;
        orders = new PreviousOrders();
    }

    /**
     * Returns the user information.
     *
     * @return The user information.
     */
    public Info getUserInfo() {
        return userInfo;
    }

    /**
     * Returns the user's card details.
     *
     * @return The user's card details.
     */
    public Card getUserCard() {
        return userCard;
    }

    /**
     * Sets the user's card details.
     *
     * @param cardNumber The card number.
     * @param date       The card's expiration date.
     * @param pin        The card's PIN number.
     */
    public void setUserCard(String cardNumber, Date date, int pin) {
        this.userCard = new Card(cardNumber, date, pin);
    }

    /**
     * Checks if the user's account is active.
     *
     * @return true if the account is active, false otherwise.
     */
    public boolean isAccountActive() {
        return accountActive;
    }

    /**
     * Sets the user's account status.
     *
     * @param accountActive The account status to set.
     */
    public void setStatus(boolean accountActive) {
        this.accountActive = accountActive;
    }

    /**
     * Returns the user's shopping cart.
     *
     * @return The user's shopping cart.
     */
    public ShoppingCart getUserCart() {
        return userCart;
    }

    /**
     * Returns the user's loyalty points.
     *
     * @return The user's loyalty points.
     */
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    /**
     * Sets the user's loyalty points.
     *
     * @param loyaltyPoints The loyalty points to set.
     */
    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    /**
     * Returns the user's vouchers.
     *
     * @return The user's vouchers.
     */
    public List<Voucher> getVouchers() {
        return vouchers;
    }

    /**
     * Returns the user's previous orders.
     *
     * @return The user's previous orders.
     */
    public PreviousOrders getPrevOrders() {
        return orders;
    }

    /**
     * Adds a voucher to the user's list of vouchers.
     *
     * @param voucher The voucher to add.
     */
    public void addVouchers(Voucher voucher) {
        this.vouchers.add(voucher);
    }
}

