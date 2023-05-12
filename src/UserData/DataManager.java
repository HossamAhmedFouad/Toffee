package UserData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public abstract class DataManager {
    protected Scanner scanner;
    
    protected FileWriter myWriter;
    
    public DataManager() {
        loadData();
    }

    protected abstract void uploadData();
    
    protected abstract void loadData();

    protected void saveToFile(String filePath, List<String> lines) {
        try {
            myWriter = new FileWriter(filePath);
            for (String line : lines) {
                myWriter.write(line);
            }
            myWriter.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected List<String> readFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            File myFile = new File(filePath);
            scanner = new Scanner(myFile);
            scanner.nextLine();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
