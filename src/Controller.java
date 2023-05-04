import PaymentSystem.CashOnDelivery;
import PaymentSystem.Ewallet;
import PaymentSystem.Payment;
import Products.Inventory;
import Products.Product;
import UserData.Authenticator;
import UserData.Info;
import UserData.Order;
import UserData.User;
import UserData.Voucher;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Controller {
    Scanner scanner;
    int choice;
    private Inventory inventory;
    private Authenticator authenticator;
    private boolean loggedUser;
    private User activeUser;

    public Controller(Inventory inventory, Authenticator authenticator) {
        this.inventory = inventory;
        this.authenticator = authenticator;
        loggedUser = false;
    }

    public void register() {
        scanner.nextLine();
        String name, email, password, shippingAddress;
        System.out.print("Enter name: ");
        name = scanner.nextLine();
        System.out.print("Enter email: ");
        email = scanner.nextLine();
        System.out.print("Enter password: ");
        password = scanner.nextLine();
        System.out.print("Enter Shipping Address: ");
        shippingAddress = scanner.nextLine();

        Info info = new Info(name,email,password,shippingAddress);

        //TODO: validate data + generate OTP from Authenticator
        //Done
        if (authenticator.validateInfo(info)) {
            System.out.println("Error, Email already used");
            return;
        }
        info.setOTP(authenticator.generateOTP());
        System.out.println("Please enter OTP sent to your email address: " + info.getEmail());
        String input = scanner.nextLine();
        while(input != info.getOTP()) {
            System.out.println("Incorrect OTP...");
            System.out.println("1 - Resend OTP");
            System.out.println("2 - Try Again");
            System.out.println("3 - Change Email Address");
            System.out.println("4 - Cancel Registration");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1 || choice == 3) {
                if (choice == 3) {
                    System.out.print("Enter email: ");
                    email = scanner.nextLine();
                    info.setEmail(email);
                }
                info.setOTP(authenticator.generateOTP());
                System.out.println("Please enter OTP sent to your email address: " + info.getEmail());
                input = scanner.nextLine();
            } else if (choice == 2) {
                input = scanner.nextLine();
            } else if(choice == 4){
                return;
            }
        }
        authenticator.addUser(info);
        activeUser = authenticator.getUser(info);
        System.out.println("Register Has Been Successful, you can login now");
        System.out.println("Returning back to main menu");
    }

    public void changeCartItems(int choice) {
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

    public void displayMenu() {
        System.out.println("WELCOME PLEASE CHOOSE ACTION");
        System.out.println("1 - View Catalog Of Products Available");
        if (!loggedUser) {
            System.out.println("2 - Register");
            System.out.println("3 - Log In");
        }
        System.out.println("4 - Exit");
    }

    public void redeemVoucher() {
        while (true) {
            System.out.println("DO YOU WANT TO REDEEM A VOUCHER? (yes):(NO)");
            String vAnswer = scanner.nextLine();
            if (vAnswer == "YES" && activeUser.getUserCart().getTotalPrice()>0) {
                List<Voucher> vouchers = activeUser.getVouchers();
                if (!vouchers.isEmpty()) {
                    System.out.println("Available vouchers:");
                    for (Voucher voucher : vouchers) {
                        String formattedVoucher = "Voucher Code: " + voucher.getCode() + "\n";
                        formattedVoucher += "Amount: " + voucher.getAmount() + "\n";
                        System.out.print(formattedVoucher);
                    }
                    System.out.println("PLEASE ENTER VOUCHER NUMBER: ");
                    choice = scanner.nextInt();
                    if(vouchers.get(choice-1).getAmount()>=activeUser.getUserCart().getTotalPrice()){
                        activeUser.getUserCart().setDiscount(activeUser.getUserCart().getTotalPrice());
                        vouchers.get(choice-1).setAmount(vouchers.get(choice-1).getAmount()-activeUser.getUserCart().getTotalPrice());
                    }else{
                        activeUser.getUserCart().setDiscount(vouchers.get(choice-1).getAmount());
                        vouchers.remove(choice-1);
                    }
                    activeUser.getVouchers().remove(choice - 1);
                } else {
                    System.out.println("No vouchers available.");
                    break;
                }
            }else if(activeUser.getUserCart().getTotalPrice()==0){
                System.out.println("CANT ADD VOUCHER TO YOUR ORDER");
            }else {
                break;
            }
        }
    }

    public void login() {
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
        if (authenticator.validateInfo(info)) {
            loggedUser = true;
            activeUser = authenticator.getUser(info);
            System.out.println("Login Has Been Successful, Welcome " + activeUser.getUserInfo().getName());
        } else {
            System.out.println("Invalid, Credentials");
        }
    }

    public void viewCart() {
        activeUser.getUserCart().display();
        if (activeUser.getUserCart().empty()) {
            System.out.println("Cart is empty!");
            return;
        }
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
                Payment payment = null;
                Date today = new Date(System.currentTimeMillis());
                Order order = new Order(activeUser.getUserCart().getTotalPrice(), today,
                        activeUser.getUserInfo().getAddress(), activeUser.getUserCart().getProducts());
                //Payment section
                System.out.println("PLEASE CHOOSE PAYMENT OPTION");
                System.out.println("1 - Cash On Delivery");
                System.out.println("2 - EWallet");
                System.out.println("3 - Back");
                choice = scanner.nextInt();
                if (choice == 1) {
                    payment = new CashOnDelivery(order, activeUser);
                } else if (choice == 2) {
                    payment = new Ewallet(order, activeUser);
                }else if (choice == 3) {
                    continue;
                }
                //Choosing shipping address
                String address;
                System.out.println("PLEASE CHOOSE ADDRESS");
                System.out.println("1 - Same account address");
                System.out.println("2 - Add new address");
                choice = scanner.nextInt();
                if (choice == 1) {
                    //Todo
                    break;
                } else if (choice == 2) {
                    //Todo
                    break;
                }
                if (activeUser.getUserCart().checkOut(payment)) {
                    scanner.nextLine();
                    System.out.println("Total price: " + activeUser.getUserCart().getTotalPrice());
                    redeemVoucher();
                    System.out.println("DO YOU WANT TO CONFIRM? (YES):(NO)");
                    String cAnswer = scanner.nextLine();
                    if (cAnswer.equals("YES")) {
                        activeUser.getOrders().addOrder(order);
                        activeUser.getUserCart().emptyCart();
                        System.out.println("Checkout successfully");
                    }
                    break;
                } else {
                    System.out.println("Error in payment");
                }
                //TODO : Start payment mode
            } else if (choice == 5) {
                return;
            }
        }
    }

    public void addItem() {
        scanner.nextLine();
        System.out.println("PLEASE ENTER ITEM NUMBER: ");
        choice = scanner.nextInt();
        if (choice <= 0 || choice > inventory.getProducts().size()) {
            System.out.println("INVALID NUMBER");
            return;
        } 
        System.out.println(inventory.getProducts().get(choice - 1));
        activeUser.getUserCart().addProduct(inventory.getProducts().get(choice - 1));
        //TODO: CHECK QUANTITY AND AVAILABILITY
        //Done
    }

    public void start() {
        scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            choice = scanner.nextInt();
            if (choice == 1) {
                inventory.display();
                System.out.println("PLEASE CHOOSE ACTION");
                System.out.println("1 - Add an item to cart");
                System.out.println("2 - Go to cart");
                System.out.println("3 - Go Back");
                choice = scanner.nextInt();
                if (choice == 1) {
                    if (loggedUser) {
                        addItem();
                    } else {
                        System.out.println("YOU MUST BE A USER TO ADD AN ITEM TO CART");
                        continue;
                    }
                } else if (choice == 2) {
                    if (loggedUser) {
                        viewCart();
                    } else {
                        System.out.println("YOU MUST BE A USER TO ADD AN ITEM TO CART");
                    }
                } else if (choice == 3) {
                    continue;
                }
            } else if (choice == 2 && !loggedUser) {
                register();
            } else if (choice == 3 && !loggedUser) {
                login();
            } else if (choice == 4) {
                break;
            }
        }
    }
}