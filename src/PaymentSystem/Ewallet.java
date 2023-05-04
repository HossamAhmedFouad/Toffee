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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter pin: "); 
        int input = scanner.nextInt();
        scanner.close();
        if(input != user.getUserCard().getPin()){
            return false;
        }
        return true;
    }  
}
