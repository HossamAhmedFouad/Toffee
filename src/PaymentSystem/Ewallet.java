package PaymentSystem;
import UserData.Authenticator;
import java.util.Scanner;
import UserData.User;
/**
 * A class representing an e-wallet payment method.
 * Extends the Payment class.
 */
public class Ewallet extends Payment {
    /**
     * An authenticator object for validating the user's card information.
     */
    Authenticator authenticator;
    /**
     * Constructor for creating an Ewallet object.
     * @param user the user making the payment
     * @param authenticator an authenticator object for validating the user's card information
     */
    public Ewallet(User user, Authenticator authenticator) {
        super(user);
        this.authenticator = authenticator;
    }
    /**
     * Overrides the payOrder method in Payment class to implement e-wallet payment logic.
     * @return boolean representing whether or not the payment was successful
     */
    @Override
    public boolean payOrder() {
        if (!authenticator.validateCard(user.getUserCard())) {
            System.out.println("Card Is Not Valid.");
            return false;
        }
        System.out.println("Please Enter Card CVV: ");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        if (input != user.getUserCard().getPin()) {
            System.out.println("Pin Is Incorrect.");
            return false;
        }
        return true;
    }  
}