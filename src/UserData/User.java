package UserData;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class User{
    private Info userInfo;
    private Card userCard;
    private boolean accountActive = true;
    private ShoppingCart userCart = new ShoppingCart();
    private PreviousOrders orders;
    private int loyaltyPoints;
    private List<Voucher>vouchers=new ArrayList<Voucher>();


    public User(Info info){
        userInfo = info;
        orders = new PreviousOrders();
    }

    public Info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Info userInfo) {
        this.userInfo = userInfo;
    }

    public Card getUserCard() {
        return userCard;
    }

    public void setUserCard(String cardNumber, Date date, int pin) {
        this.userCard = new Card(cardNumber, date, pin);
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setStatus(boolean accountActive) {
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

    public PreviousOrders getPrevOrders() {
        return orders;
    }

    public void setOrders(PreviousOrders orders) {
        this.orders = orders;
    }
    public void addVouchers(Voucher voucher) {
        this.vouchers.add(voucher);
    }

}
