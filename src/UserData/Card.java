package UserData;

import java.util.Date;

/**
 * Represents a card with a card number, expiration date, status, and PIN.
 */
public class Card {
    private String cardNumber;
    private Date date;
    private boolean status;
    private int pin;

    /**
     * Constructs a Card object with the specified card number, expiration date, and PIN.
     *
     * @param cardNumber the card number
     * @param date the expiration date of the card
     * @param pin the PIN associated with the card
     */
    public Card(String cardNumber, Date date, int pin) {
        this.cardNumber = cardNumber;
        this.date = date;
        this.pin = pin;
        this.status = true; // Set status to true by default
    }

    /**
     * Returns the expiration date of the card.
     *
     * @return the expiration date of the card
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the status of the card.
     *
     * @return the status of the card
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Returns the PIN associated with the card.
     *
     * @return the PIN associated with the card
     */
    public int getPin() {
        return pin;
    }

    /**
     * Returns the card number.
     *
     * @return the card number
     */
    public String getCardNumber() {
        return cardNumber;
    }
}
