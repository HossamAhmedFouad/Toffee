package PaymentSystem;

public class CashOnDelivery extends Payment {
    @Override
    public boolean payOrder() {
        return false;
    }
}
