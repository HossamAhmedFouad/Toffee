package UserData;

import java.util.Date;

public class Card {
    private String cardNumber;
    private Date date;
    private boolean status = true;
    private int pin;

    public Card(String cardNumber, Date date, int pin) {
        this.cardNumber = cardNumber;
        this.date = date;
        this.pin = pin;
    }
    public void terminate(){
        //TODO: terminate card and make status not active
        status = false;
    }

    public Date getDate() {
        return date;
    }

    public void update() {
        // TODO: update card details, may need to take parameters and update uml diagram
    }
    public boolean getStatus() {
        return status;
    }
    public int getPin(){
        return pin;
    }
}
