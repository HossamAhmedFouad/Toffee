import PaymentSystem.CashOnDelivery;
import PaymentSystem.Ewallet;
import PaymentSystem.Payment;
import Products.Inventory;
import Products.Product;
import UserData.Info;
import UserData.Manager;
import UserData.Order;
import UserData.User;

import java.sql.Date;
import java.util.Scanner;

public class Controller {

    private Inventory inventory;
    private Manager manager;
    private boolean loggedUser;
    private User activeUser;

    public Controller(Inventory inventory, Manager manager){
        this.inventory = inventory;
        this.manager = manager;
        loggedUser = false;
    }

    public void displayMenu(){
        System.out.println("WELCOME PLEASE CHOOSE ACTION");
        System.out.println("1 - View Catalog Of Products Available");
        System.out.println("2 - Register");
        System.out.println("3 - Log In");
        System.out.println("4 - Exit");
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true){
            displayMenu();
            choice = scanner.nextInt();
            if(choice==1){
                inventory.display();
                while (true){
                    System.out.println("PLEASE CHOOSE ACTION");
                    System.out.println("1 - Add an item to cart");
                    System.out.println("2 - Go to cart");
                    System.out.println("3 - Go Back");
                    choice = scanner.nextInt();
                    if(choice==1){
                        if(loggedUser){
                            System.out.println("PLEASE ENTER ITEM NUMBER: ");
                            choice = scanner.nextInt();
                            if(choice<=0 || choice>inventory.getProducts().size()){
                                activeUser.getUserCart().addProduct(inventory.getProducts().get(choice-1)); //TODO: CHECK QUANTITY AND AVAILABILITY
                            }else{
                                System.out.println("INVALID NUMBER");
                            }
                        }else{
                            System.out.println("YOU MUST BE A USER TO ADD AN ITEM TO CART");
                            break;
                        }
                    }else if(choice==2){
                        if(loggedUser){
                            activeUser.getUserCart().display();
                            if(!activeUser.getUserCart().empty()){
                                while (true){
                                    System.out.println("PLEASE CHOOSE ACTION");
                                    System.out.println("1 - Increase amount");
                                    System.out.println("2 - Decrease amount");
                                    System.out.println("3 - Remove Item");
                                    System.out.println("4 - Checkout");
                                    System.out.println("5 - Back");

                                    choice = scanner.nextInt();
                                    if(choice==1 || choice==2 || choice==3){
                                        System.out.println("PLEASE ENTER PRODUCT NUMBER");
                                        int id = scanner.nextInt();
                                        if(choice==1) {
                                            activeUser.getUserCart().increaseAmount(id);
                                        }
                                        else if(choice==2){
                                            activeUser.getUserCart().decreaseAmount(id);
                                        }
                                        else{
                                            activeUser.getUserCart().removeProduct(id);
                                        }
                                    }else if(choice==4){
                                        Payment payment = null;
                                        while (true){
                                            System.out.println("PLEASE CHOOSE PAYMENT OPTION");
                                            System.out.println("1- Cash On Delivery");
                                            System.out.println("2- EWallet");
                                            choice = scanner.nextInt();
                                            if(choice==1){
                                                payment = new CashOnDelivery();
                                                break;
                                            }
                                            else if(choice==2){
                                                payment = new Ewallet();
                                                break;
                                            }
                                            if(activeUser.getUserCart().checkOut(payment)){
                                                System.out.println("total price: "+activeUser.getUserCart().getTotalPrice());
                                                System.out.println("DO YOU WANT TO CONFIRM? (YES):(NO)");
                                                String answer=scanner.nextLine();
                                                if(answer=="YES"){
                                                    Date today = new Date(System.currentTimeMillis());
                                                    Order order=new Order(activeUser.getUserCart().getTotalPrice(), today, activeUser.getUserInfo().getAddress(), activeUser.getUserCart().getProducts(), payment);
                                                    activeUser.getOrders().addOrder(order);
                                                    activeUser.getUserCart().emptyCart();
                                                    System.out.println("checkOut successfully");
                                                    break;
                                                }
                                            }else{
                                                System.out.println("error in payment");
    
                                            }
                                        }

                                        
                                        //TODO : Start payment mode
                                    }else if(choice==5){
                                        break;
                                    }
                                }
                            }
                        }else{
                            System.out.println("YOU MUST BE A USER TO ADD AN ITEM TO CART");
                            break;
                        }
                    }else if(choice==3){
                        break;
                    }
                }

            }else if(choice==2){
                String name,email,password,shippingAddress;
                String input;
                System.out.println("Enter name: "); input = scanner.nextLine(); name = input;
                System.out.println("Enter email: "); input = scanner.nextLine(); email = input;
                System.out.println("Enter password: "); input = scanner.nextLine(); password = input;
                System.out.println("Enter Shipping Address: "); input = scanner.nextLine(); shippingAddress = input;

                //TODO: validate data + generate OTP
                User user = new User();
                if(manager.registerUser(user)){
                    System.out.println("Register Has Been Successful, you can login now");
                    System.out.println("Returning back to main menu");
                }else{
                    //TODO: show unsuccessful message
                }

            }else if(choice==3){
                String email,password,input;
                System.out.println("Enter email: "); input = scanner.nextLine(); email = input;
                System.out.println("Enter password: "); input = scanner.nextLine(); password = input;
                Info info = new Info();
                info.setEmail(email);
                info.setPassword(password);
                if(manager.loginUser(info)){
                    loggedUser = true;
                    activeUser = manager.getUser(info);
                    System.out.println("Login Has Been Successful, Welcome " + activeUser.getUserInfo().getName());
                }else{
                    //TODO: show unsuccessful message
                }
            }else if(choice==4){
                break;
            }
        }

    }
}
