package UserData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract class for managing data by uploading and loading data to/from files.
 */
public abstract class DataManager {
    /**
     * Scanner object for reading input.
     */
    protected Scanner scanner;
    /**
     * FileWriter object for writing data.
     */
    protected FileWriter myWriter;

    /**
     * Constructs a DataManager object.
     */
    public DataManager() {
        loadData();
    }
    /**
     * Uploads the data to a file.
     */
    protected abstract void uploadData();

    /**
     * Loads the data from a file.
     */
    protected abstract void loadData();

    /**
     * Saves the provided lines to the specified file path.
     *
     * @param filePath the file path to save the lines to
     * @param lines the lines to be saved
     */
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

    /**
     * Reads lines from the specified file path.
     *
     * @param filePath the file path to read lines from
     * @return a list of lines read from the file
     * @throws RuntimeException if the file is not found
     */
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