package UserData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Authenticator extends DataManager {
    private String otp;

    public HashMap<String, User> getUsers() {
        return users;
    }

    private HashMap<String, User> users;
    
    public boolean validateCard(Card card) {
        Date today = new Date(System.currentTimeMillis());
        if (card == null || card.getDate().after(today) || !card.getStatus()) {
            // Card is not valid or status is inactive or user does not have a card
            return false;
        }
        return true;
    }

    public String generateOTP() {
        //TODO: generate otp and send it to the email
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 6;
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            builder.append(randomChar);
        }
        otp = builder.toString();
        System.out.println(otp);
        return otp;
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

    private Date parseDate(String target) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
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
        Date expDate = parseDate(date);
        user.setUserCard(cardNumber, expDate, pin);
        uploadData();
    }

    public User getUser(Info info) {
        return users.get(info.getEmail());
    }

    public boolean validatePass(Info info) {
        return users.get(info.getEmail()).getUserInfo().getPassword().equals(info.getPassword());
    }
    
    public boolean validateInfo(Info info) {
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
    
        return users.containsKey(info.getEmail());
    }

    @Override
    protected void uploadData() {
        List<String> userLines = new ArrayList<>();
        List<String> cardLines = new ArrayList<>();
        userLines.add("Name,Email,Password,Address\n");
        cardLines.add("Email,Card Number,Expiry Date,CVV\n");
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
                String expDate = (user.getUserCard().getDate().getMonth() + 1) + "/" + (user.getUserCard().getDate().getYear() - 100);
                int pin = user.getUserCard().getPin();
                String cardLine = String.format("%s,%s,%s,%s\n", email, cardNumber, expDate, pin);
                cardLines.add(cardLine);
            }
        }
        saveToFile(System.getProperty("user.dir") + "/src/UserData/users.csv", userLines);
        saveToFile(System.getProperty("user.dir") + "/src/UserData/cards.csv", cardLines);
    }

    @Override
    protected void loadData() {
        List<String> userLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/users.csv");
        List<String> cardLines = readFromFile(System.getProperty("user.dir") + "/src/UserData/cards.csv");
        users = new HashMap<String, User>();
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

        for (String line : cardLines) {
            String[] data = line.split(",", 4);
            String email = data[0];
            String cardNumber = data[1];
            String expDate = data[2];
            int pin = Integer.parseInt(data[3]);
            Date date = parseDate(expDate);
            users.get(email).setUserCard(cardNumber, date, pin);
        }
    }
}
