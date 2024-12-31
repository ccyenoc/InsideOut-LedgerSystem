package insideout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public class Transaction {
    private StringProperty transactionID = new SimpleStringProperty("");
    private StringProperty time = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private StringProperty username = new SimpleStringProperty("");
    private String filepath = "src/recorddebitandcredit.csv";
    private double balance=0.0;
    private ObservableList<Transaction> debitData = FXCollections.observableArrayList();
    private ObservableList<Transaction> creditData = FXCollections.observableArrayList();
    private ObservableList<Transaction> overviewData = FXCollections.observableArrayList();
    private double debitTotal=0.0;
    private double creditTotal=0.0;
   
    
    public Transaction(String name) {
        this.username.set(name);
        readFile();
    }

    // Constructor with transaction details
    public Transaction(String name, String transactionID, String time, String amount, String description) {
        this.username.set(name);
        this.transactionID.set(transactionID);
        this.time.set(time);
        this.amount.set(amount);
        this.description.set(description);
    }
   

    // Getter methods
    public String getTransactionID() {
        return transactionID.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getDescription() {
        return description.get();
    }

    // Property methods for JavaFX binding
    public StringProperty transactionIDProperty() {
        return transactionID;
    }

    public StringProperty timeProperty() {
        return time;
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // Method to read CSV and populate the data lists
    public void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            ArrayList<Transaction> debitList = new ArrayList<>();
            ArrayList<Transaction> creditList = new ArrayList<>();
            ArrayList<Transaction> overviewList = new ArrayList<>();
            boolean header = true;

            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] columns = line.split(",");
                String type = columns[2];
                String amount = columns[3];
                String description = columns[4];
                String transactionID = columns[1];
                String time = columns[5];
                String name = columns[0];
                

                if (username.get().equals(name)) {
                    balance=Double.parseDouble(columns[6]);
                    overviewList.add(new Transaction(name, transactionID, time, amount, description));
                    if ("debit".equalsIgnoreCase(type)) {
                        debitTotal+=Double.parseDouble(amount);
                        debitList.add(new Transaction(name, transactionID, time, amount, description));
                    } else if ("credit".equalsIgnoreCase(type)) {
                        creditTotal+=Double.parseDouble(amount);
                        creditList.add(new Transaction(name, transactionID, time, amount, description));
                    }
                }
            }

            debitData.setAll(debitList);
            creditData.setAll(creditList);
            overviewData.setAll(overviewList);
           

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Getter methods for ObservableLists
    public ObservableList<Transaction> getDebitData() {
        return debitData;
    }

    public ObservableList<Transaction> getCreditData() {
        return creditData;
    }

    public ObservableList<Transaction> getOverviewData() {
        return overviewData;
    }
    
    public double getDebitTotal(){
        return debitTotal;
    }
    
    public double getCreditTotal(){
        return creditTotal;
    }
    
    public Label getBalance(){
       Label lbl=new Label("RM "+String.valueOf(balance));
       return lbl;
    }
    
}