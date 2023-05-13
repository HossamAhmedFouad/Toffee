package Products;

/**
 * The Product class represents a product in a store.
 */
public class Product {
    private static int id = 0;
    private int productID;
    private String name;
    private double price;
    private String category;
    private String brand;
    private String unitType;
    private Availability status = Availability.onSale;
    private double discountPercentage;
    private int quantity;

    /**
     * Constructs a Product object with the specified details.
     *
     * @param name      the name of the product
     * @param price     the price of the product
     * @param category  the category of the product
     * @param brand     the brand of the product
     * @param unitType  the unit type of the product
     * @param quantity  the quantity of the product
     */
    public Product(String name, double price, String category, String brand, String unitType, int quantity) {
        id++;
        productID = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.unitType = unitType;
        this.quantity = quantity;
    }

    /**
     * Returns the product ID.
     *
     * @return the product ID
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Sets the product ID.
     *
     * @param ID the product ID to set
     */
    public void setProductID(int ID) {
        productID = ID;
    }

    /**
     * Sets the global ID for the products.
     *
     * @param ID the global ID to set
     */
    public static void setID(int ID) {
        id = ID;
    }

    /**
     * Returns the global ID for the products.
     *
     * @return the global ID
     */
    public static int getID() {
        return id;
    }

    /**
     * Returns the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the product.
     *
     * @return the price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the category of the product.
     *
     * @return the category of the product
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the brand of the product.
     *
     * @return the brand of the product
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Returns the unit type of the product.
     *
     * @return the unit type of the product
     */
    public String getUnitType() {
        return unitType;
    }

    /**
     * Returns the availability status of the product.
     *
     * @return the availability status of the product
     */
    public Availability getStatus() {
        return status;
    }

    /**
     * Sets the availability status of the product.
     *
     * @param status the availability status to set
     */
    public void setStatus(Availability status) {
        this.status = status;
    }

    /**
     * Returns the discount percentage of the product.
     *
     * @return the discount percentage of the product
     */
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    /**
     * Sets the discount percentage of the product.
     *
     * @param discountPercentage the discount percentage to set
     */
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    /**
     * Returns the quantity of the product.
     *
     * @return the quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        int prevQuantity = this.quantity;
        this.quantity = quantity;
        if (this.quantity == 0) {
            this.status = Availability.outOfStock;
        } else if (prevQuantity == 0 && this.quantity > 0) {
            this.status = Availability.notOnSale;
        }
    }
}
