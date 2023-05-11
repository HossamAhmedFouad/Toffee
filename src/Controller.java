import PaymentSystem.CashOnDelivery;
import PaymentSystem.Ewallet;
import PaymentSystem.Payment;
import Products.Inventory;
import UserData.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Controller {
    Scanner scanner;
    int choice;
    private Inventory inventory;
    private Authenticator authenticator;
    private boolean loggedUser;
    private User activeUser;

    private Admin admin;

    private boolean loggedAdmin;


    public Controller(Inventory inventory, Authenticator authenticator) {
        this.inventory = inventory;
        this.authenticator = authenticator;
        loggedUser = false;
    }

    private void register() {
        scanner.nextLine();
        String name, email, password, shippingAddress;
        System.out.print("Enter Name: ");
        name = scanner.nextLine();
        System.out.print("Enter Email: ");
        email = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        System.out.print("Enter Shipping Address: ");
        shippingAddress = scanner.nextLine();

        Info info = new Info(name,email,password,shippingAddress);

        if (authenticator.validateUser(info)) {
            System.out.println("Error: Email Already Used");
            return;
        }
        info.setOTP(authenticator.generateOTP());
        System.out.println("Please Enter OTP Sent To Your Email Address: " + info.getEmail());
        String input = scanner.nextLine();
        System.out.println(info.getOTP());
        while(!input.equals(info.getOTP())) {
            System.out.println("Incorrect OTP...");
            System.out.println("1 - Resend OTP");
            System.out.println("2 - Try Again");
            System.out.println("3 - Change Email Address");
            System.out.println("4 - Cancel Registration");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1 || choice == 3) {
                if (choice == 3) {
                    System.out.print("Enter Email: ");
                    email = scanner.nextLine();
                    info.setEmail(email);
                }
                info.setOTP(authenticator.generateOTP());
                System.out.println("Please Enter OTP Sent To Your Email Address: " + info.getEmail());
                input = scanner.nextLine();
            } else if (choice == 2) {
                input = scanner.nextLine();
            } else if(choice == 4){
                return;
            }
        }
        authenticator.addUser(info);
        System.out.println("Register Has Been Successful, You Can Login Now");
        System.out.println("Returning Back To Main Menu...");
    }

    private void changeCartItems(int choice) {
        System.out.println("PLEASE ENTER PRODUCT NUMBER");
        int id = scanner.nextInt();
        if (choice == 1) {
            activeUser.getUserCart().increaseAmount(id);
        } else if (choice == 2) {
            activeUser.getUserCart().decreaseAmount(id);
        } else {
            activeUser.getUserCart().removeProduct(id);
        }
    }

    private void displayMenu() {
        System.out.println("========== Welcome To Toffee Shop ==========");
        System.out.println("1 - View Catalog Of Products Available");
        if (loggedUser) {
            System.out.println("2 - View Previous Orders");
            System.out.println("3 - Update Credit Card Details");
        }

        if(loggedAdmin){
            System.out.println("2 - Update Catalog");
            System.out.println("3 - Update Product");
            System.out.println("4 - View Orders");
            System.out.println("5 - Suspend a User");
            System.out.println("6 - View Statistics");
            System.out.println("7 - Exit");
        }

        if (!loggedUser && !loggedAdmin) {
            System.out.println("2 - Register");
            System.out.println("3 - Log In");
            System.out.println("4 - Exit");
        }

    }
    
    private void updateCard() {
        if (activeUser.getUserCard() == null) {
            System.out.println("There Is No Credit Card In Your Account.");
            System.out.println("Do You Want To Add One? (YES):(NO)");
        } else {
            System.out.println("There Is Credit Card In Your Account.");
            System.out.println("Do You Want To Update It? (YES):(NO)");
        }
        scanner.nextLine();
        String Answer = scanner.nextLine();
        if (Answer.equals("NO"))
            return;
        System.out.print("Please Enter Card Number: ");
        String cardNum = scanner.nextLine();
        System.out.print("Please Enter Expiry Date: ");
        String expDate = scanner.nextLine();
        System.out.print("Please Enter CVV: ");
        int pin = scanner.nextInt();
        authenticator.addCard(activeUser, cardNum, expDate, pin);
        System.out.println("Credit Card Was Updated");
    }
    
    private void redeemVoucher(Order order) {
        while (true) {
            if (activeUser.getUserCart().getTotalPrice() > 0 && !activeUser.getVouchers().isEmpty()) {
                    System.out.println("Available Vouchers:");
                    displayVouchers(activeUser.getVouchers());
                    System.out.println("PLEASE ENTER VOUCHER NUMBER: ");
                    choice = scanner.nextInt();
                    if(activeUser.getVouchers().get(choice-1).getAmount()>=activeUser.getUserCart().getTotalPrice()){
                        order.setDiscount(activeUser.getUserCart().getTotalPrice());
                        activeUser.getVouchers().get(choice-1).setAmount(activeUser.getVouchers().get(choice-1).getAmount()-activeUser.getUserCart().getTotalPrice());
                    }else{
                        order.setDiscount(activeUser.getVouchers().get(choice-1).getAmount());
                        activeUser.getVouchers().remove(choice - 1);
                    }
                    System.out.println("DO YOU WANT TO REDEEM ANOTHER VOUCHER? (YES):(NO)");
                    String vAnswer = scanner.nextLine();
                    if(vAnswer.equals("NO")){
                        break;
                    }
                    
            }else{
                System.out.println("CANT ADD VOUCHER TO YOUR ORDER");
                break;
            }
        }
    }

    private Info takeInfo(){
        String email, password, input;
        scanner.nextLine();
        System.out.print("Enter email: ");
        input = scanner.nextLine();
        email = input;
        System.out.print("Enter password: ");
        input = scanner.nextLine();
        password = input;
        Info info = new Info();
        info.setEmail(email);
        info.setPassword(password);
        return info;
    }

    private void login(){
        while (true){
            System.out.println("Please Choice Option");
            System.out.println("1 - User Login");
            System.out.println("2 - Admin Login");
            int choice = scanner.nextInt();
            if(choice==1){
                loginUser();
                break;
            }else if(choice==2){
                loginAdmin();
                break;
            }
        }
    }

    private void loginUser() {
        Info info = takeInfo();
        if (authenticator.validateUser(info) && authenticator.validateUserPass(info)) {
            loggedUser = true;
            activeUser = authenticator.getUser(info);
            activeUser.getUserCart().setObserver(inventory);
            System.out.println("Login Has Been Successful, Welcome " + activeUser.getUserInfo().getName());
        } else {
            System.out.println("Invalid Credentials");
        }
    }

    private void loginAdmin(){
        Info info = takeInfo();
        if (authenticator.validateAdmin(info) && authenticator.validateAdminPass(info)) {
            loggedAdmin = true;
            admin = authenticator.getAdmin(info);
            admin.setInventory(inventory);
            admin.setAuthenticator(authenticator);
            System.out.println("Login Has Been Successful, Welcome Admin");
        } else {
            System.out.println("Invalid Credentials");
        }
    }

    private void checkOut(){
        //Choosing shipping address
        scanner.nextLine();
        String address=null;
        System.out.println("PLEASE CHOOSE ADDRESS");
        System.out.println("1 - Same Account Address");
        System.out.println("2 - Add New Address");
        choice = scanner.nextInt();
        if (choice == 1) {
            address=activeUser.getUserInfo().getAddress();
        } else if (choice == 2) {
            System.out.println("Enter A New Address");
            address=scanner.nextLine();
        }
        //Payment section
        Payment payment = null;
        Date today = new Date(System.currentTimeMillis());
        Order order = new Order(activeUser.getUserCart().getTotalPrice(), today,address, new HashMap<>(activeUser.getUserCart().getProducts()));
        System.out.println("PLEASE CHOOSE PAYMENT OPTION");
        System.out.println("1 - Cash On Delivery");
        System.out.println("2 - Credit Card");
        System.out.println("3 - Back");
        choice = scanner.nextInt();
        if (choice == 1) {
            payment = new CashOnDelivery(order, activeUser);
        } else if (choice == 2) {
            payment = new Ewallet(order, activeUser);
        }else if (choice == 3) {
            return;
        }
        if (activeUser.getUserCart().checkOut(payment)) {
            scanner.nextLine();
            while(true){
                System.out.println("DO YOU WANT TO REDEEM (VOUCHER):(LOYALITYPOINTES):(EXIT) ");
                String answer=scanner.nextLine();
                if(answer.equals("VOUCHER")){
                    redeemVoucher(order);
                    break;
                }else if(answer.equals("LOYALITYPOINTES")){
                    redeemLoyalty(order);
                    break;
                }else if(answer.equals("EXIT")){
                    break;
                }
            }
            order.displaySummary();
            System.out.println("DO YOU WANT TO CONFIRM? (YES):(NO)");
            String cAnswer = scanner.nextLine();
            if (cAnswer.equals("YES")) {
                activeUser.getPrevOrders().addOrder(order);
                activeUser.setLoyaltyPoints(calculateLoyaltyPoints(order.getTotalPrice()));
                activeUser.getUserCart().emptyCart();
                System.out.println("Checkout Successfully");
            }
        } else {
            System.out.println("Error In Payment");
        }
    }
    public int calculateLoyaltyPoints(Double orderTotal) {
        int loyaltyPoints = 0;
        int dollarPerPoint = inventory.getLoyalty();
        int intervals =  (int) (orderTotal / dollarPerPoint);
        loyaltyPoints = intervals;
        System.out.println(loyaltyPoints);
        return loyaltyPoints;
    }
    public void redeemLoyalty(Order order){
        int pointsPerDollar = inventory.getLoyalty();
        int loyaltyPoints=activeUser.getLoyaltyPoints();
        double orderTotal=order.getTotalPrice();
        double discountAmount = loyaltyPoints * pointsPerDollar;
        if (discountAmount > orderTotal) {
            discountAmount = orderTotal;
        }
        order.setDiscount(discountAmount);       
    }

    private void prevOrders(){
        //TODO: initiate Re-ordering previous order
        if (activeUser.getPrevOrders().getOrders().size() == 0) {
            System.out.println("There Are No Previous Orders");
            return;
        }
        activeUser.getPrevOrders().viewOrders();
        while (true){
            System.out.println("PLEASE CHOOSE ACTION");
            System.out.println("1 - Re-Order");
            System.out.println("2 - View Order Details");
            System.out.println("3 - Back");
            choice = scanner.nextInt();
            if(choice==1 || choice==2){
                System.out.println("PLEASE CHOOSE ORDER ID");
                int id = scanner.nextInt();
                if(id < 1 || id > activeUser.getPrevOrders().getOrders().size()){
                    System.out.println("INVALID ID NUMBER");
                }else{
                    if(choice==1){
                        Order order = activeUser.getPrevOrders().getOrders().get(id-1);
                        activeUser.getUserCart().setProducts(order.getProducts());
                        checkOut();
                        return;
                    }else{
                        activeUser.getPrevOrders().displayOrder(activeUser.getPrevOrders().getOrders().get(id-1));
                    }
                }
            }else if(choice==3){
                break;
            }
        }

    }

    private void viewCart() {
        activeUser.getUserCart().display();
        if (activeUser.getUserCart().empty()) 
            return;
        while (true) {
            System.out.println("PLEASE CHOOSE ACTION");
            System.out.println("1 - Increase amount");
            System.out.println("2 - Decrease amount");
            System.out.println("3 - Remove Item");
            System.out.println("4 - Checkout");
            System.out.println("5 - Back");
            choice = scanner.nextInt();
            if (choice == 1 || choice == 2 || choice == 3) {
                changeCartItems(choice);
            } else if (choice == 4) {
                checkOut();
                return;
            } else if (choice == 5) {
                return;
            }
        }
    }

    private void addItem() {
        scanner.nextLine();
        System.out.println("PLEASE ENTER ITEM NUMBER: ");
        choice = scanner.nextInt();
        if (choice <= 0 || choice > inventory.getProducts().size() || inventory.getProducts().get(choice - 1).getQuantity() == 0) {
            System.out.println("Error: Can't Add Item To The Cart");
            return;
        } 
        System.out.println(inventory.getProducts().get(choice - 1));
        activeUser.getUserCart().addProduct(inventory.getProducts().get(choice - 1));
    }

    public void start() {
        scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            choice = scanner.nextInt();
            if (choice == 1) {
                inventory.display();
                while (true) {
                    System.out.println("PLEASE CHOOSE ACTION");
                    System.out.println("1 - Add an item to cart");
                    System.out.println("2 - Go to cart");
                    System.out.println("3 - Buy a voucher");
                    System.out.println("4 - Go Back");
                    choice = scanner.nextInt();
                    if (loggedUser) {
                        if (choice == 1) {
                            addItem();
                        } else if (choice == 2) {
                            viewCart();
                        } else if (choice == 3) {
                            buyVoucher();
                            displayVouchers(activeUser.getVouchers());
                        } else if (choice == 4) {
                            break;
                        }
                    } else if (choice == 4) {
                        break;
                    } else {
                        System.out.println("You Must Be A User To Do This Action");
                    }

                }
            }else{
                if(loggedUser){
                    if(choice==2){
                        prevOrders();
                    }else if(choice==3){
                        updateCard();
                    }

                }else if(loggedAdmin){
                    if(choice==2){
                        admin.updateCatalog();
                    }else if(choice==3){
                        admin.updateProduct();
                    }else if(choice==4){
                        //TODO: View All Order
                    }else if(choice==5){
                        admin.suspendUser();
                    }else if(choice==6){
                        //TODO: View Statistics
                    }else if(choice==7){
                        break;
                    }
                }else{
                    if(choice==2){
                        register();
                    }else if(choice==3){
                        login();
                    }else if(choice==4){
                        break;
                    }
                }
            }
        }
    }

    private void displayVouchers(List<Voucher> vouchers) {

        if (vouchers.isEmpty()) {
            System.out.println("No vouchers available.");
            return;
        }
        int cnt = 1;
        String format = "%-5s %-20s $%-10s\n";
        System.out.format(format, "No.", "Voucher Code", "Amount");
        for (Voucher voucher : vouchers) {
            System.out.format(format, cnt++, voucher.getCode(), String.format("%.2f", voucher.getAmount()));
        }
    }
    
    private void buyVoucher(){
        while(true){
            displayVouchers(inventory.getVoucher());
            choice=scanner.nextInt();
            if(choice>=0&&choice<=inventory.getVoucher().size()){
                activeUser.addVouchers(inventory.getVoucher().get(choice-1));
                inventory.getVoucher().remove(choice-1);
                break;
            }
        }
    }
}