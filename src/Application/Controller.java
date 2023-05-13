import PaymentSystem.CashOnDelivery;
import PaymentSystem.Ewallet;
import PaymentSystem.Payment;
import Products.Inventory;
import Products.Product;
import UserData.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * The Controller class manages the interaction between the user/admin and the Toffee Shop system.
 * It handles user registration, login, menu display, cart management, order placement, and other related operations.
 */
public class Controller {
    /**
     * The scanner object used for user input.
     */
    private Scanner scanner;
    /**
     * The user's choice input.
     */
    private int choice;
    /**
     * The inventory object that stores the available products.
     */
    private Inventory inventory;
    /**
     * The authenticator object for user authentication and authorization.
     */
    private Authenticator authenticator;
    /**
     * Indicates whether a user is currently logged in.
     */
    private boolean loggedUser;
    /**
     * The active user object.
     */
    private User activeUser;
    /**
     * The admin object.
     */
    private Admin admin;
    /**
     * Indicates whether an admin is currently logged in.
     */
    private boolean loggedAdmin;

    /**
     * Constructs a new Controller instance with the given inventory and authenticator.
     *
     * @param inventory     the inventory object to be used
     * @param authenticator the authenticator object to be used
     */
    public Controller(Inventory inventory, Authenticator authenticator) {
        this.inventory = inventory;
        this.authenticator = authenticator;
        loggedUser = false;
    }

    /**
     * Handles the user registration process.
     * Prompts the user to enter the required information and validates it.
     * If the registration is successful, the user is added to the authenticator.
     */
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
        int otp = Authenticator.generateOTP();
        if (!Authenticator.SendOTP(info.getEmail(), otp)) 
            return;
        System.out.println("Please Enter OTP Sent To Your Email Address: " + info.getEmail());
        String input = scanner.nextLine();
        while(!input.equals(Integer.toString(otp))) {
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
                    authenticator.validateUser(info);
                }
                otp = Authenticator.generateOTP();
                Authenticator.SendOTP(info.getEmail(), otp);
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
    /**
     * Handles the change of cart items based on the given choice.
     * @param choice the user's choice for cart item modification
     */
    private void changeCartItems(int choice) {
        System.out.println("Please Enter Product Number:");
        int id = scanner.nextInt();
        if (choice == 1) {
            activeUser.getUserCart().increaseAmount(id);
        } else if (choice == 2) {
            activeUser.getUserCart().decreaseAmount(id);
        } else {
            activeUser.getUserCart().removeProduct(id);
        }
    }
    /**
     * Displays the main menu based on the user/admin login status.
     */
    private void displayMenu() {
        System.out.println("========== Welcome To Toffee Shop ==========");
        System.out.println("1 - View Catalog Of Products Available");
        if (loggedUser) {
            System.out.println("2 - View Previous Orders");
            System.out.println("3 - Update Credit Card Details");
            System.out.println("4 - Exit");
        }

        if(loggedAdmin){
            System.out.println("2 - Update Catalog");
            System.out.println("3 - Update Product");
            System.out.println("4 - View Orders");
            System.out.println("5 - Change User Status");
            System.out.println("6 - Exit");
        }

        if (!loggedUser && !loggedAdmin) {
            System.out.println("2 - Register");
            System.out.println("3 - Log In");
            System.out.println("4 - Exit");
        }

    }
    /**
     * Updates the credit card information for the active user.
     */
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
    /**
     * Redeems a voucher for the given order.
     * @param order the order for which the voucher is being redeemed
     */
    private void redeemVoucher(Order order) {
        while (true) {
            if (activeUser.getUserCart().getTotalPrice() > 0 && !activeUser.getVouchers().isEmpty()) {
                    System.out.println("Available Vouchers:");
                    displayVouchers(activeUser.getVouchers(),true);
                    System.out.println("Please Enter Voucher Number: ");
                    choice = scanner.nextInt();
                    if(activeUser.getVouchers().get(choice-1).getAmount()>=activeUser.getUserCart().getTotalPrice()){
                        order.setDiscount(activeUser.getUserCart().getTotalPrice());
                        activeUser.getVouchers().get(choice-1).setAmount(activeUser.getVouchers().get(choice-1).getAmount()-activeUser.getUserCart().getTotalPrice());
                    }else{
                        order.setDiscount(activeUser.getVouchers().get(choice-1).getAmount());
                        activeUser.getVouchers().remove(choice - 1);
                    }
                    System.out.println("Do You Want to Redeem Another Voucher? (Yes):(No)");
                    String vAnswer = scanner.nextLine();
                    if(vAnswer.equals("No")){
                        break;
                    }
                    
            }else{
                System.out.println("Cant Add Voucher to Your Order");
                break;
            }
        }
    }
    /**
     * Takes the required user information for authentication.
     *
     * @return the Info object containing the user's email and password
     */
    private Info takeInfo(){
        String email, password, input;
        scanner.nextLine();
        System.out.print("Enter Email: ");
        input = scanner.nextLine();
        email = input;
        System.out.print("Enter Password: ");
        input = scanner.nextLine();
        password = input;
        Info info = new Info();
        info.setEmail(email);
        info.setPassword(password);
        return info;
    }
    /**
     * Handles the user login process.
     * Prompts the user to choose between user login and admin login.
     * Validates the credentials and logs in the user/admin if they are valid.
     */
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
    /**
     * Handles the user login process.
     * Prompts the user to enter their email and password.
     * Validates the credentials and logs in the user if they are valid.
     */
    private void loginUser() {
        Info info = takeInfo();
        if (authenticator.validateUser(info) && authenticator.validateUserPass(info)) {
            loggedUser = true;
            activeUser = authenticator.getUser(info);
            List<Observer> observers = new ArrayList<>(); 
            observers.add(authenticator);
            observers.add(inventory);
            activeUser.getUserCart().setObservers(observers);
            System.out.println("Login Has Been Successful, Welcome " + activeUser.getUserInfo().getName());
        } else {
            System.out.println("Invalid Credentials, Either The Password Is Incorrect Or Account Is Suspended");
        }
    }
    /**
     * Handles the admin login process.
     * Prompts the admin to enter their email and password.
     * Validates the credentials and logs in the admin if they are valid.
     */
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
    /**
     * Checks out the user's cart by choosing a shipping address and payment option.
     * Creates an order object and processes the payment.
     * Offers the user the choice to redeem a voucher for the order if they have any.
     * Updates the inventory and clears the user's cart.
     */
    private void checkOut() {
        //Choosing shipping address
        scanner.nextLine();
        String address = null;
        System.out.println("PLEASE CHOOSE ADDRESS");
        System.out.println("1 - Same Account Address");
        System.out.println("2 - Add New Address");
        choice = scanner.nextInt();
        if (choice == 1) {
            address = activeUser.getUserInfo().getAddress();
        } else if (choice == 2) {
            System.out.println("Enter A New Address");
            scanner.nextLine();
            address = scanner.nextLine();
        }
        System.out.println("Please Choose Payment Option");
        System.out.println("1 - Cash On Delivery");
        System.out.println("2 - Credit Card");
        System.out.println("3 - Back");
        Payment payment = null;
        choice = scanner.nextInt();
        if (choice == 1) {
            payment = new CashOnDelivery(activeUser);
        } else if (choice == 2) {
            payment = new Ewallet(activeUser, authenticator);
        } else if (choice == 3) {
            return;
        }
        Date today = new Date(System.currentTimeMillis());
        Order order = new Order(activeUser.getUserCart().getTotalPrice(), today, address,
                new HashMap<>(activeUser.getUserCart().getProducts()));
        if (activeUser.getUserCart().checkOut(payment)) {
            scanner.nextLine();
            while (true) {
                System.out.println("Do You Want to Redeem (Voucher):(Loyalty Points):(Exit) ");
                String answer = scanner.nextLine();
                if (answer.equals("Voucher")) {
                    redeemVoucher(order);
                    break;
                } else if (answer.equals("Loyalty Points")) {
                    redeemLoyalty(order);
                    break;
                } else if (answer.equals("Exit")) {
                    break;
                }
            }
            order.displaySummary();
            System.out.println("Do You Want to Confirm? (Yes):(No)");
            String cAnswer = scanner.nextLine();
            if (cAnswer.equals("Yes")) {
                activeUser.getPrevOrders().addOrder(order);
                activeUser.setLoyaltyPoints(calculateLoyaltyPoints(order.getTotalPrice()));
                activeUser.getUserCart().emptyCart();
                System.out.println("Checkout Successfully");
            }
        } else {
            System.out.println("Error In Payment");
        }
    }
    /**
     * Calculates loyalty points based on the order total.
     * 
     * @param orderTotal the total amount of the order
     * @return the calculated loyalty points
     */
    public int calculateLoyaltyPoints(Double orderTotal) {
        int loyaltyPoints = 0;
        int dollarPerPoint = inventory.getLoyalty();
        int intervals = (int) (orderTotal / dollarPerPoint);
        loyaltyPoints = intervals;
        System.out.println(loyaltyPoints);
        return loyaltyPoints;
    }
    /**
     * Redeems loyalty points for the given order.
     * 
     * @param order the order to redeem loyalty points for
     */
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
    /**
     * Displays the user's previous orders and provides options to re-order or view order details.
     */
    private void prevOrders(){
        if (activeUser.getPrevOrders().getOrders().size() == 0) {
            System.out.println("There Are No Previous Orders");
            return;
        }
        activeUser.getPrevOrders().viewOrders();
        while (true){
            System.out.println("Please Choose Action");
            System.out.println("1 - Re-Order");
            System.out.println("2 - View Order Details");
            System.out.println("3 - Back");
            choice = scanner.nextInt();
            if(choice==1 || choice==2){
                System.out.println("Please Choose Order ID");
                int id = scanner.nextInt();
                if(id < 1 || id > activeUser.getPrevOrders().getOrders().size()){
                    System.out.println("Invalid ID Number");
                }else{
                    if (choice == 1) {
                        Order order = activeUser.getPrevOrders().getOrders().get(id - 1);
                        HashMap<Product, Integer> productMap = new HashMap<>(order.getProducts());
                        if (!activeUser.getUserCart().setProducts(productMap)) {
                            System.out.println("Error: Can't Add Item To The Cart The Product Is Out Of Stock");
                            return;
                        }
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
    /**
     * Displays the contents of the user's cart and provides options to modify the items or proceed to checkout.
     */
    private void viewCart() {
        activeUser.getUserCart().display();
        if (activeUser.getUserCart().empty()) 
            return;
        while (true) {
            System.out.println("Please Choose Action");
            System.out.println("1 - Increase Amount");
            System.out.println("2 - Decrease Amount");
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
    /**
     * Adds an item to the user's cart based on the item number.
     */
    private void addItem() {
        scanner.nextLine();
        System.out.println("Please Enter Item Number: ");
        choice = scanner.nextInt();
        if (choice <= 0 || choice > inventory.getProducts().size()) {
            System.out.println("Error: Invalid Index.");
            return;
        } 
        if (!activeUser.getUserCart().addProduct(inventory.getProducts().get(choice - 1), 1)) {
            System.out.println("Error: Can't Add Item To The Cart The Product Is Out Of Stock");
            return;
        }
        System.out.println(inventory.getProducts().get(choice - 1));
    }
    /**
     * Starts the application and handles user interactions.
     */
    public void start() {
        scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            choice = scanner.nextInt();
            if (choice == 1) {
                inventory.display();
                while (true) {
                    System.out.println("Please Choose Action:");
                    System.out.println("1 - Add An Item To Cart");
                    System.out.println("2 - Go To Cart");
                    System.out.println("3 - Buy A Voucher");
                    System.out.println("4 - Go Back");
                    choice = scanner.nextInt();
                    if (loggedUser) {
                        if (choice == 1) {
                            addItem();
                        } else if (choice == 2) {
                            viewCart();
                        } else if (choice == 3) {
                            buyVoucher();
                            displayVouchers(activeUser.getVouchers(),true);
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
                    }else if(choice==4){
                        break;
                    }

                }else if(loggedAdmin){
                    if(choice==2){
                        admin.updateCatalog();
                    }else if(choice==3){
                        admin.updateProduct();
                    }else if(choice==4){
                        admin.viewAllOrders();
                    }else if(choice==5){
                        admin.changeUserStatus();
                    }else if(choice==6){
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
    /**
     * Displays a list of vouchers with the option to view voucher codes or amounts.
     * 
     * @param vouchers    the list of vouchers to display
     * @param displayCode flag indicating whether to display voucher codes or amounts
     */
    private void displayVouchers(List<Voucher> vouchers,boolean displayCode) {

        if (vouchers.isEmpty()) {
            System.out.println("No Vouchers Available.");
            return;
        }
        int cnt = 1;
        if(displayCode==true){
            String format = "%-5s %-20s $%-10s\n";
            System.out.format(format, "No.", "Voucher Code", "Amount");
            for (Voucher voucher : vouchers) {
                System.out.format(format, cnt++, voucher.getCode(), String.format("%.2f", voucher.getAmount()));
            }
        }else{
            String format = "%-5s $%-10s\n";
            System.out.format(format, "No.","Amount");
            for (Voucher voucher : vouchers) {
                System.out.format(format, cnt++, String.format("%.2f", voucher.getAmount()));
            }
        }
    }
    /**
     * Allows the user to buy a voucher from the available options.
     */
    private void buyVoucher(){
        while(true){
            displayVouchers(inventory.getVouchers(),false);
            choice = scanner.nextInt();
            if (choice >= 0 && choice <= inventory.getVouchers().size()) {
                Payment payment = new Ewallet(activeUser, authenticator);
                if (!payment.payOrder()) {
                    System.out.println("Payment Error...");
                    return;
                }
                activeUser.addVouchers(inventory.getVouchers().get(choice - 1));
                inventory.removeVoucher(choice - 1);
                break;
            }
        }
    }
}