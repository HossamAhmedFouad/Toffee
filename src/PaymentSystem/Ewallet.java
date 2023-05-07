package PaymentSystem;

import UserData.Authenticator;
import java.util.Scanner;

import UserData.Order;
import UserData.User;

public class Ewallet extends Payment {
    Authenticator authenticator;
    public Ewallet(Order order, User user) {
        super(order, user);
        authenticator = new Authenticator();
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
