package UserData;
/**
 * Represents the personal information of a user.
 */
public class Info {
    private String name, email, password, address;

    /**
     * Constructs an `Info` object with the specified name, email, password, and address.
     *
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     * @param address the address of the user
     */
    public Info(String name, String email, String password, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    /**
     * Constructs an empty `Info` object.
     */
    public Info(){
        
    }

    /**
     * Returns the name of the user.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the address of the user.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
}