package UserData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class Authenticator {
    private Card card;
    private HashMap<String, User> users = new HashMap<String, User>();

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
    public boolean validateInfo(Info info){
        //TODO : validates info given
        //Done
        return users.containsKey(info.getEmail());
    }

    public boolean validateOTP(String otp){
        //TODO: validates OTP
        return true;
    }
}
