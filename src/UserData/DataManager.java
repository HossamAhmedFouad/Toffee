package UserData;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


import Products.Product;

public abstract class DataManager {
    protected Scanner scanner;
    protected FileWriter myWriter;
    protected HashMap<String, User> users = new HashMap<String, User>();
    protected List<Product> products = new ArrayList<Product>();
    protected List<Voucher> vouchers = new ArrayList<Voucher>();
    
    public DataManager() {
        loadData();
        scanner.close();
    }
    
    protected abstract void uploadData();
    
    protected abstract void loadData();
}
