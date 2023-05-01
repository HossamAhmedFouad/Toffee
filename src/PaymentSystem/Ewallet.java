package PaymentSystem;

import java.util.Scanner;

import UserData.Order;
import UserData.User;

public class Ewallet extends Payment {
    public Ewallet(Order order, User user) {
        super(order, user);
    }

    @Override
    public boolean payOrder() {
        User user=getUser();
        if(user.getUserInfo().getAddress().isEmpty()||!user.getUserCard().validate()){
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        int input;
        System.out.println("please, enter pin: "); 
        input = scanner.nextInt();
        if(input!=user.getUserCard().getPin()){
            return false;
        }
        return true;
    }  
}
