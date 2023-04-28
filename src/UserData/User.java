package UserData;

import java.util.List;


public class User {
    private Info userInfo;
    private Card userCard;
    private boolean accountActive;
    private ShoppingCart userCart;

    private PreviousOrders orders;
    private int loyaltyPoints;
    private List<Voucher>vouchers;


//    public User(Info userInfo, Card userCard, boolean accountActive, ShoppingCart shoppingCart, int loyaltyPoints, List<Voucher> vouchers) {
//        this.userInfo = userInfo;
//        this.userCard = userCard;
//        this.accountActive = accountActive;
//        this.userCart = shoppingCart;
//        this.loyaltyPoints = loyaltyPoints;
//        this.vouchers = vouchers;
//    }

    public Info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Info userInfo) {
        this.userInfo = userInfo;
    }

    public Card getUserCard() {
        return userCard;
    }

    public void setUserCard(Card userCard) {
        this.userCard = userCard;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }

    public ShoppingCart getUserCart() {
        return userCart;
    }

    public void setUserCart(ShoppingCart userCart) {
        this.userCart = userCart;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public PreviousOrders getOrders() {
        return orders;
    }

    public void setOrders(PreviousOrders orders) {
        this.orders = orders;
    }
}
