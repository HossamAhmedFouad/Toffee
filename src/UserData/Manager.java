package UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager {
    private List<User> users = new ArrayList<>();
    private Authenticator auth;

    public boolean registerUser(Info info) {
        //TODO: authenticate
        for(User user : users){
            if(user.getUserInfo().getEmail().equals(info.getEmail())){
                return false;
            };
        }
        users.add(new User(info));
        return true;
    }

    public boolean loginUser(Info info){
        for(User user : users){
            if(user.getUserInfo().getEmail().equals(info.getEmail()) && user.getUserInfo().getPassword().equals(info.getPassword())) return true;
        }
        return false;
    }

    public String generateOTP(){
        return "";
    }

    public User getUser(Info info){
        for(User user : users){
            if(user.getUserInfo().getEmail().equals(info.getEmail()) && user.getUserInfo().getPassword().equals(info.getPassword())) return user;
        }
        User err = new User(new Info()); err.getUserInfo().setName("NAU"); // NOT A USER
        return err;
    }


}