package UserData;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Products.Inventory;
import Products.Product;

import java.util.Properties;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The Authenticator class is responsible for managing user authentication and data management.
 * It provides methods for validating user and admin credentials, adding users and cards,
 * sending OTPs (One-Time Passwords) via email, and uploading and loading data from files.
 * It also implements the Observer interface to receive notifications when data is updated.
 */
public class Authenticator extends DataManager implements Observer {

    private static Inventory inventory;

    private HashMap<String, Admin> admins;

    private HashMap<String, User> users;

    /**
     * Sets the inventory for the Authenticator.
     *
     * @param inv the Inventory object to set
     */
    public static void setInventory(Inventory inv) {
        inventory = inv;
    }

    /**
     * Returns the users HashMap.
     *
     * @return the HashMap of users
     */
    public HashMap<String, User> getUsers() {
        return users;
    }

    /**
     * Returns the admins HashMap.
     *
     * @return the HashMap of admins
     */
    public HashMap<String, Admin> getAdmins() {
        return admins;
    }
    
    /**
     * Validates the given card for its validity and status.
     *
     * @param card the Card object to validate
     * @return true if the card is valid and active, false otherwise
     */
    public boolean validateCard(Card card) {
        Date today = new Date(System.currentTimeMillis());
        if (card == null || !card.getDate().after(today) || !card.getStatus()) {
            // Card is not valid or status is inactive or user does not have a card
            return false;
        }
        return true;
    }

    /**
     * Sends an OTP (One-Time Password) to the specified email address.
     *
     * @param email the email address to send the OTP to
     * @param code  the OTP code to send
     * @return true if the email is sent successfully, false otherwise
     */
    public static boolean SendOTP(String email, int code) {
        System.out.println("Please wait while sending OTP to your email address.....");
        String host = "smtp.gmail.com";
        String username = "fcaitoffeeshopwork@gmail.com";
        String password = "ppvjxavkmrcpmtvk";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 587);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

            message.setSubject("TOFFE SHOP VERIFICATION CODE");
            message.setText("Your OTP IS : " + code);

            Transport.send(message);
            System.out.println("Email sent successfully!");
            return true;
        } catch (MessagingException e) {
            System.out.println("Failed To Send OTP Email. Check Your Network Connection Or Try Again");
            return false;
        }
    }
    /**
     * Generates a random OTP (One-Time Password).
     *
     * @return the generated OTP
     */
    public static int generateOTP() {
        return (int) (Math.random() * 900000) + 100000;
    }

    /**
     * Adds a new user with the provided info.
     *
     * @param info the Info object containing user information
     */
    public void addUser(Info info) {
        User user = new User(info);
        users.put(info.getEmail(), user);
        uploadData();
    }
    /**
     * Performs input validation for the given user input based on the provided regex pattern.
     * If the input does not match the pattern, prompts the user with an error message and requests input again.
     *
     * @param userInput     the user input to validate
     * @param regexPattern  the regex pattern to match the input against
     * @param errorMessage  the error message to display if the input is invalid
     * @param promptMessage the message to prompt the user for input
     * @param scanner       the Scanner object for user input
     * @return the validated input
     */
    private String validateInput(String userInput, String regexPattern, String errorMessage, Scanner scanner) {
        Pattern regex = Pattern.compile(regexPattern);
        Matcher matcher = regex.matcher(userInput);
        while (!matcher.matches()){
            System.out.print(errorMessage);
            userInput = scanner.nextLine();
            matcher = regex.matcher(userInput);
        }
        return userInput;
    }
    /**
     * Parses the provided date string into a Date object based on the given format.
     *
     * @param dateString the date string to parse
     * @param format     the format of the date string
     * @return the parsed Date object
     */
    private Date parseDate(String target,String targetDateFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(targetDateFormat);
        Date date = null;
        try {
            date = dateFormat.parse(target);
        } catch (ParseException e) {
            // handle the exception
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * Validates and adds a card to the specified user.
     *
     * @param user       the User object to add the card to
     * @param cardNumber the card number
     * @param date       the expiry date of the card (in "MM/yy" format)
     * @param pin        the CVV code of the card
     */
    public void addCard(User user, String cardNumber, String date, int pin) {
        scanner = new Scanner(System.in);
        cardNumber = validateInput(cardNumber, "\\b(?:\\d[ -]*?){13,16}\\b",
                "Please enter a valid credit card number (13 to 16-digit numeric value): ", scanner);
        date = validateInput(date, "^(0[1-9]|1[0-2])\\/\\d{2}$",
                "Please enter a valid expiry date, make sure to use the 'MM/yy' format (e.g., 01/23): ", scanner);
        String cvv = validateInput(Integer.toString(pin), "^\\d{3}$",
                "Please enter a valid CVV, make sure to enter a 3-digit code from the back of your card: ", scanner);
        pin = Integer.parseInt(cvv);
        Date expDate = parseDate(date, "MM/yy");
        user.setUserCard(cardNumber, expDate, pin);
        uploadData();
    }

    /**
     * Returns the User object corresponding to the given info.
     *
     * @param info the Info object containing user information
     * @return the User object if found, null otherwise
     */
    public User getUser(Info info) {
        return users.get(info.getEmail());
    }

    /**
     * Returns the Admin object corresponding to the given info.
     *
     * @param info the Info object containing admin information
     * @return the Admin object if found, null otherwise
     */
    public Admin getAdmin(Info info) {
        return admins.get(info.getEmail());
    }

    /**
     * Validates the admin password for the given info.
     *
     * @param info the Info object containing admin information
     * @return true if the password is valid, false otherwise
     */
    public boolean validateAdminPass(Info info) {
        return admins.get(info.getEmail()).getInfo().getPassword().equals(info.getPassword());
    }
    
    /**
     * Validates the user password for the given info.
     *
     * @param info the Info object containing user information
     * @return true if the password is valid and the account is active, false otherwise
     */
    public boolean validateUserPass(Info info) {
        return users.get(info.getEmail()).getUserInfo().getPassword().equals(info.getPassword())
                && users.get(info.getEmail()).isAccountActive();
    }
    /**
     * Validates the provided user information.
     * @param info The Info object containing the user information.
     */
    private void validation(Info info){
        scanner = new Scanner(System.in);
        if (info.getName() != null && info.getAddress() != null) {
            // Validate name
            String name = validateInput(info.getName(), "^[A-Za-z- ']+$", "Please Enter A Valid Name: ", scanner);
            info.setName(name);
            // Validate shipping address
            String address = validateInput(info.getAddress(), "^[A-Za-z0-9- ',]+$",
                    "Please Enter A Valid Shipping Address: ", scanner);
            info.setAddress(address);
        }
        // Validate email
        String email = validateInput(info.getEmail(), "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                "Please Enter A Valid Email Address: ", scanner);
        info.setEmail(email);

        // Validate password
        String password = validateInput(info.getPassword(), "^[A-Za-z0-9]{6,}$",
                "Please Enter A Valid Password (at least 6 characters): ", scanner);
        info.setPassword(password);
    }
    /**
     * Validates the user information for a regular user.
     * @param info The Info object containing the user information.
     * @return true if the user is valid, false otherwise.
     */
    public boolean validateUser(Info info) {
        validation(info); //Regex
        return users.containsKey(info.getEmail());
    }
    /**
     * Validates the user information for an admin.
     * @param info The Info object containing the user information.
     * @return true if the admin is valid, false otherwise.
     */
    public boolean validateAdmin(Info info){
        validation(info);
        return admins.containsKey(info.getEmail());
    }
    /**
     * Uploads the user and admin data to the database.
     */
    @Override
    protected void uploadData() {
        List<String> userLines = new ArrayList<>();
        List<String> cardLines = new ArrayList<>();
        List<String> orderLines = new ArrayList<>();
        userLines.add("Name,Email,Password,Address,Status\n");
        cardLines.add("Email,Card Number,Expiry Date,CVV\n");
        orderLines.add("Email,Order ID,Quantity/ProductID,Total Price,Shipping Address,Date\n");
        SimpleDateFormat orderDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat cardDateFormat = new SimpleDateFormat("MM/yy");
        for (Map.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();
            String name = user.getUserInfo().getName();
            String email = user.getUserInfo().getEmail();
            String password = user.getUserInfo().getPassword();
            String address = user.getUserInfo().getAddress();
            boolean status = user.isAccountActive();
            String line = String.format("%s,%s,%s,%s,%s\n", name, email, password, address, status);
            userLines.add(line);

            if (user.getUserCard() != null) {
                String cardNumber = user.getUserCard().getCardNumber();
                String expDate = cardDateFormat.format(user.getUserCard().getDate());
                int pin = user.getUserCard().getPin();
                String cardLine = String.format("%s,%s,%s,%s\n", email, cardNumber, expDate, pin);
                cardLines.add(cardLine);
            }
            if (!user.getPrevOrders().getOrders().isEmpty()) {
                for (Order prevOrders : user.getPrevOrders().getOrders()) {
                    int orderID = prevOrders.getID();
                    String orderDetails = "";
                    for (Map.Entry<Product, Integer> item : prevOrders.getProducts().entrySet()) {
                        int productQuantity = item.getValue();
                        int productID = item.getKey().getProductID();
                        orderDetails += productQuantity + "|" + productID + "/";
                    }
                    Double totalPrice = prevOrders.getTotalPrice();
                    String shipAddress = prevOrders.getShipAddress();
                    String shipDate = orderDateFormat.format(prevOrders.getDate());
                    String orderLine = String.format("%s,%s,%s,%s,%s,%s\n", email, orderID, orderDetails, totalPrice,
                            shipAddress, shipDate);
                    orderLines.add(orderLine);
                }
            }
        }
        saveToFile(System.getProperty("user.dir") + "/src/UserData/orders.csv", orderLines);
        saveToFile(System.getProperty("user.dir") + "/src/UserData/users.csv", userLines);
        saveToFile(System.getProperty("user.dir") + "/src/UserData/cards.csv", cardLines);
    }
    /**
     * Loads user, admin, card, and order data from CSV files.
     */
    @Override
    protected void loadData() {
        List<String> userLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/users.csv");
        List<String> adminLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/admins.csv");
        List<String> cardLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/cards.csv");
        List<String> orderLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/orders.csv");
        users = new HashMap<String, User>();
        admins = new HashMap<String, Admin>();
        
        for (String line : userLines) {
            String[] data = line.split(",", 5);
            String name = data[0];
            String email = data[1];
            String password = data[2];
            String address = data[3];
            boolean status = Boolean.parseBoolean(data[4]);
            Info info = new Info(name, email, password, address);
            User user = new User(info);
            user.setStatus(status);
            users.put(info.getEmail(), user);
        }
        for (String line : orderLines) {
            String[] data = line.split(",", 6);
            String email = data[0];
            String[] orderDetails = data[2].split("/");
            HashMap<Product,Integer> orderProducts = new HashMap<Product,Integer>();
            for (String items : orderDetails) {
                String[] item = items.split("\\|");
                int quantity = Integer.parseInt(item[0]);
                int productID = Integer.parseInt(item[1]);
                Product product = inventory.getProducts().get(productID);
                orderProducts.put(product, quantity);
            }
            double totalPrice = Double.parseDouble(data[3]);
            String shippingAddress = data[4];
            Date shipDate = parseDate(data[5],"yyyy-MM-dd HH:mm");
            Order prevOrder = new Order(totalPrice, shipDate, shippingAddress, orderProducts);
            users.get(email).getPrevOrders().addOrder(prevOrder);
        }
        for (String line : cardLines) {
            String[] data = line.split(",", 4);
            String email = data[0];
            String cardNumber = data[1];
            String expDate = data[2];
            int pin = Integer.parseInt(data[3]);
            Date date = parseDate(expDate,"MM/yy");
            users.get(email).setUserCard(cardNumber, date, pin);
        }
        for (String line : adminLines) {
            String[] data = line.split(",", 4);
            String name = data[0];
            String email = data[1];
            String password = data[2];
            String address = data[3];
            Info info = new Info(name, email, password, address);
            Admin admin = new Admin(info,this);
            admins.put(info.getEmail(), admin);
        }
    }
    /**
     * Updates the data by uploading it.
     */
    @Override
    public void onUpdate() {
        uploadData();
    }

}
