package UserData;

import java.util.List;
import java.util.Scanner;

public class Manager {
    private List<User> users;
    private Authenticator auth;

    public boolean registerUser(User user) {
        //TODO: authenticate

        users.add(user);
        return true;
    }

    public boolean loginUser(Info info){
        //TODO: authenticate
        return true;
    }

    public String generateOTP(){
        return "";
    }

    public User getUser(Info info){
        for(User user : users){
            if(user.getUserInfo().getEmail().equals(info.getEmail()) && user.getUserInfo().getPassword().equals(info.getPassword())) return user;
        }
        User err = new User(); err.getUserInfo().setName("NAU"); // NOT A USER
        return err;
    }


}