package PaymentSystem;

import UserData.Authenticator;
import java.util.Scanner;
import UserData.User;

public class Ewallet extends Payment {
    Authenticator authenticator;
    
    public Ewallet(User user, Authenticator authenticator) {
        super(user);
        this.authenticator = authenticator;
    }

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
