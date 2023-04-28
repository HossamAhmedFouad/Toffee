package UserData;

import java.util.Date;
import java.util.PrimitiveIterator.OfInt;

public class Card {
    private String cardNumber;
    private Date date;
    private boolean status;
    private int pin;
    private Authenticator auth;

    public boolean validate(){
        Date today = new Date(System.currentTimeMillis());
        if (!date.after(today)) {
            // Card is not valid
            return false;
        }
        if(status==false){
            return false;
        }

        return true;

        //TODO : Validate card data to follow world wide card details
    }
    public void terminate(){
        //TODO: terminate card and make status not active
    }

    public void update(){
        // TODO: update card details, may need to take parameters and update uml diagram
    }
    public int getPin(){
        return pin;
    }
}
