package UserData;
/**
 * The Voucher class represents a voucher that can be used for discounts or promotions.
 */
public class Voucher {
    private String code;
    private double amount;

    /**
     * Creates a new Voucher instance with the specified code and amount.
     *
     * @param code   The voucher code.
     * @param amount The amount of discount or value associated with the voucher.
     */
    public Voucher(String code, double amount) {
        this.code = code;
        this.amount = amount;
    }

    /**
     * Returns the voucher code.
     *
     * @return The voucher code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the amount of discount or value associated with the voucher.
     *
     * @return The amount of discount or value.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of discount or value associated with the voucher.
     *
     * @param amount The amount of discount or value to set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}

