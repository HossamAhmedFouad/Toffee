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

public class Authenticator extends DataManager implements CartObserver {

    private static Inventory inventory;

    private HashMap<String, Admin> admins;

    private HashMap<String, User> users;

    public static void setInventory(Inventory inv) {
        inventory = inv;
    }
    
    public HashMap<String, User> getUsers() {
        return users;
    }

    public HashMap<String, Admin> getAdmins() {
        return admins;
    }
    
    public boolean validateCard(Card card) {
        Date today = new Date(System.currentTimeMillis());
        if (card == null || !card.getDate().after(today) || !card.getStatus()) {
            // Card is not valid or status is inactive or user does not have a card
            return false;
        }
        return true;
    }

    public static boolean SendOTP(String email, int code) {
        String host = "smtp.gmail.com";
        String username = "fcai.toffeeshop@gmail.com";
        String password = "dfpzbhgihyfxtbjp";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
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
            throw new RuntimeException(e);
        }
    }
    
    public static int generateOTP() {
        return (int) (Math.random() * 900000) + 100000;
    }

    public void addUser(Info info) {
        User user = new User(info);
        users.put(info.getEmail(), user);
        uploadData();
    }

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

    public void addCard(User user, String cardNumber, String date, int pin) {
        scanner = new Scanner(System.in);
        cardNumber = validateInput(cardNumber, "\\b(?:\\d[ -]*?){13,16}\\b",
                "Please enter a valid credit card number (13 to 16-digit numeric value): ", scanner);
        date = validateInput(date, "^(0[1-9]|1[0-2])\\/\\d{2}$",
                "Please enter a valid expiry date, make sure to use the 'MM/yy' format (e.g., 01/23): ", scanner);
        String cvv = validateInput(Integer.toString(pin), "^\\d{3}$",
                "Please enter a valid CVV, make sure to enter a 3-digit code from the back of your card: ", scanner);
        pin = Integer.parseInt(cvv);
        Date expDate = parseDate(date,"MM/yy");
        user.setUserCard(cardNumber, expDate, pin);
        uploadData();
    }

    public User getUser(Info info) {
        return users.get(info.getEmail());
    }

    public Admin getAdmin(Info info){
        return admins.get(info.getEmail());
    }

    public boolean validateAdminPass(Info info) {
        return admins.get(info.getEmail()).getInfo().getPassword().equals(info.getPassword());
    }
    
    public boolean validateUserPass(Info info) {
        return users.get(info.getEmail()).getUserInfo().getPassword().equals(info.getPassword());
    }


    public void validation(Info info){
        scanner = new Scanner(System.in);
        if (info.getName() != null && info.getAddress() != null) {
            // Validate name
            String name = validateInput(info.getName(), "^[A-Za-z- ']+$", "Please enter a valid name: ", scanner);
            info.setName(name);
            // Validate shipping address
            String address = validateInput(info.getAddress(), "^[A-Za-z0-9- ',]+$",
                    "Please enter a valid shipping address: ", scanner);
            info.setAddress(address);
        }
        // Validate email
        String email = validateInput(info.getEmail(), "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                "Please enter a valid email address: ", scanner);
        info.setEmail(email);

        // Validate password
        String password = validateInput(info.getPassword(), "^[A-Za-z0-9]{6,}$",
                "Please enter a valid password (at least 6 characters): ", scanner);
        info.setPassword(password);
    }

    public boolean validateUser(Info info) {
        validation(info);
        return users.containsKey(info.getEmail());
    }

    public boolean validateAdmin(Info info){
        validation(info);
        return admins.containsKey(info.getEmail());
    }

    @Override
    protected void uploadData() {
        List<String> userLines = new ArrayList<>();
        List<String> cardLines = new ArrayList<>();
        List<String> orderLines = new ArrayList<>();
        userLines.add("Name,Email,Password,Address\n");
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
            String line = String.format("%s,%s,%s,%s\n", name, email, password, address);
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
                        int productID = item.getKey().getID();
                        orderDetails += productQuantity + "|" + productID + "/";
                    }
                    Double totalPrice = prevOrders.getTotalPrice();
                    String shipAddress = prevOrders.getShipAddress();
                    String shipDate = orderDateFormat.format(prevOrders.getDate());
                    // String shipDate = (prevOrders.getDate().getYear() + 1900) + "-" + (prevOrders.getDate().getMonth() + 1) + "-"
                    //         + (prevOrders.getDate().getDay() + 7);
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

    @Override
    protected void loadData() {
        List<String> userLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/users.csv");
        List<String> adminLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/admins.csv");
        List<String> cardLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/cards.csv");
        List<String> orderLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/orders.csv");
        users = new HashMap<String, User>();
        admins = new HashMap<String, Admin>();
        
        for (String line : userLines) {
            String[] data = line.split(",", 4);
            String name = data[0];
            String email = data[1];
            String password = data[2];
            String address = data[3];
            Info info = new Info(name, email, password, address);
            User user = new User(info);
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
                for (Product product : inventory.getProducts()) {
                    if (productID == product.getID()) {
                        orderProducts.put(product, quantity);
                        break;
                    }
                }
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
            Admin admin = new Admin(info);
            admins.put(info.getEmail(), admin);
        }
    }

    @Override
    public void onCheckout() {
        uploadData();
    }
}
