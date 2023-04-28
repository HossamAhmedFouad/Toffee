package UserData;

import java.util.Date;

public class Card {
    private String cardNumber;
    private Date date;
    private String status;
    private int pin;
    private Authenticator auth;

    public void validate(){
        //TODO : Validate card data to follow world wide card details
    }
    public void terminate(){
        //TODO: terminate card and make status not active
    }

    public void update(){
        // TODO: update card details, may need to take parameters and update uml diagram
    }
}
