import PaymentSystem.CashOnDelivery;
import PaymentSystem.Ewallet;
import PaymentSystem.Payment;
import Products.Inventory;
import Products.Product;
import UserData.Info;
import UserData.Manager;
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
    private Manager manager;
    private boolean loggedUser;
    private User activeUser;

    public Controller(Inventory inventory, Manager manager) {
        this.inventory = inventory;
        this.manager = manager;
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

        Info info = new Info();
        info.setName(name);
        info.setEmail(email);
        info.setPassword(password);
        info.setAddress(shippingAddress);

        //TODO: validate data + generate OTP from Authenticator
        if (manager.registerUser(info)) {
            System.out.println("Register Has Been Successful, you can login now");
            System.out.println("Returning back to main menu");
        } else {
            System.out.println("Error,Email already used");
        }
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
        System.out.println("2 - Register");
        System.out.println("3 - Log In");
        System.out.println("4 - Exit");
    }

    public void redeemVoucher() {
        while (true) {
            System.out.println("DO YOU WANT TO REDEEM A VOUCHER? (yes):(NO)");
            String vAnswer = scanner.nextLine();
            if (vAnswer == "YES") {
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
                    // order.addVoucher(vouchers.get(choice-1));
                    activeUser.getVouchers().remove(choice - 1);
                } else {
                    System.out.println("No vouchers available.");
                    break;
                }
            } else {
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
        if (manager.loginUser(info)) {
            loggedUser = true;
            activeUser = manager.getUser(info);
            System.out.println("Login Has Been Successful, Welcome " + activeUser.getUserInfo().getName());
        } else {
            System.out.println("Invalid,Credentials");
        }
    }

    public void viewCart() {
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
                Payment payment = null;
                Date today = new Date(System.currentTimeMillis());
                Order order = new Order(activeUser.getUserCart().getTotalPrice(), today,
                        activeUser.getUserInfo().getAddress(), activeUser.getUserCart().getProducts());
                while (true) {
                    System.out.println("PLEASE CHOOSE PAYMENT OPTION");
                    System.out.println("1- Cash On Delivery");
                    System.out.println("2- EWallet");
                    choice = scanner.nextInt();
                    if (choice == 1) {
                        payment = new CashOnDelivery(order, activeUser);
                        break;
                    } else if (choice == 2) {
                        payment = new Ewallet(order, activeUser);
                        break;
                    }
                }
                String address;
                while (true) {
                    System.out.println("PLEASE CHOOSE address");
                    System.out.println("1- SAME ACCOUNT ADDRESS");
                    System.out.println("2- ADD NEW ADDRESS");
                    choice = scanner.nextInt();
                    if (choice == 1) {
                        //todo
                        break;
                    } else if (choice == 2) {
                        //todo
                        break;
                    }
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
                        System.out.println("checkOut successfully");
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
        } else {
            System.out.println(inventory.getProducts().get(choice - 1));
            activeUser.getUserCart().addProduct(inventory.getProducts().get(choice - 1));
            //TODO: CHECK QUANTITY AND AVAILABILITY
            //Done
        }
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
                    System.out.println("3 - Go Back");
                    choice = scanner.nextInt();
                    if (choice == 1) {
                        if (loggedUser) {
                            addItem();
                        } else {
                            System.out.println("YOU MUST BE A USER TO ADD AN ITEM TO CART");
                            break;
                        }
                    } else if (choice == 2) {
                        if (loggedUser) {
                            viewCart();
                        } else {
                            System.out.println("YOU MUST BE A USER TO ADD AN ITEM TO CART");
                            break;
                        }
                    } else if (choice == 3) {
                        break;
                    }
                }
            } else if (choice == 2) {
                register();
            } else if (choice == 3) {
                login();
            } else if (choice == 4) {
                break;
            }
        }
    }
}