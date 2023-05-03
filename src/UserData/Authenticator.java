package UserData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class Authenticator {
    private Card card;
    private HashMap<String, User> users = new HashMap<String, User>();
    private String otp;
    private String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public Authenticator() {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(System.getProperty("user.dir") + "/src/UserData/users.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] data = line.split(",", 4);

            Info info = new Info(data[0], data[1], data[2], data[3]);
            User user = new User(info);
            users.put(info.getEmail(), user);
        }
        scanner.close();
    }

    public User getUser(Info info){
        return users.get(info.getEmail());
    }

    public boolean validateInfo(Info info) {
        //TODO : validates info given
        //Done
        return users.containsKey(info.getEmail());
    }

    public void addUser(User user,Info info) {
        users.put(info.getEmail(), user);
        try {
            FileWriter myWriter = new FileWriter(System.getProperty("user.dir") + "/src/UserData/users.csv",true);
            myWriter.append(
                    info.getName() + "," + info.getEmail() + "," + info.getPassword() + "," + info.getAddress());
            myWriter.append("\n");
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateOTP() {
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

    
    public boolean validateOTP(String otp){
        //TODO: validates OTP
        //Done
        return true;
    }
}
