package UserData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;
import java.util.Date;
import java.util.Map;


public class Authenticator extends DataManager {
    private String otp;

    public boolean validateCard(Card card) {
        Date today = new Date(System.currentTimeMillis());
        if (card == null || card.getDate().after(today) || !card.getStatus()) {
            // Card is not valid or status is inactive
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
        //TODO: check validation of the card  (regex)
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
        System.out.println(users.containsKey(info.getEmail()));
        return users.containsKey(info.getEmail());
    }

    @Override
    protected void uploadData() {
        try {
            myWriter = new FileWriter(System.getProperty("user.dir") + "/src/UserData/users.csv");
            for (Map.Entry<String, User> entry : users.entrySet()) {
                User user = entry.getValue();
                String name = user.getUserInfo().getName();
                String email = user.getUserInfo().getEmail();
                String password = user.getUserInfo().getPassword();
                String address = user.getUserInfo().getAddress();
                String line = String.format("%s,%s,%s,%s\n", name, email, password, address);
                myWriter.write(line);
            }
            myWriter.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            myWriter = new FileWriter(System.getProperty("user.dir") + "/src/UserData/cards.csv");
            for (Map.Entry<String, User> entry : users.entrySet()) {
                User user = entry.getValue();
                if (user.getUserCard() == null)
                    continue;
                String email = user.getUserInfo().getEmail();
                String cardNumber = user.getUserCard().getCardNumber();
                String expDate = (user.getUserCard().getDate().getMonth()+1) + "/" + (user.getUserCard().getDate().getYear()-100);
                int pin = user.getUserCard().getPin();
                String line = String.format("%s,%s,%s,%s\n", email, cardNumber, expDate, pin);
                myWriter.write(line);
            }
            myWriter.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        try {
            File myFile = new File(System.getProperty("user.dir") + "/src/UserData/users.csv");
            scanner = new Scanner(myFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] data = line.split(",", 4);
            String name = data[0];
            String email = data[1];
            String password = data[2];
            String address = data[3];
            Info info = new Info(name, email, password, address);
            User user = new User(info);
            users.put(info.getEmail(), user);
        }

        try {
            scanner = new Scanner(new File(System.getProperty("user.dir") + "/src/UserData/cards.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
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
