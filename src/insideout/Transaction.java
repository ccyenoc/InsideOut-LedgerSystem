package insideout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static insideout.InsideOut.Username;

public class Transaction {
    private StringProperty transactionID;
    private StringProperty time;
    private StringProperty amount;
    private StringProperty description;
    private final String filepath="/Users/cye/NewFolder/InsideOut/src/recorddebitandcredit - Sheet1.csv";
    private ObservableList<Transaction> debitData = FXCollections.observableArrayList();
    private ObservableList<Transaction> creditData = FXCollections.observableArrayList();
    private ObservableList<Transaction> overviewData= FXCollections.observableArrayList();
    
    // Constructor
    public Transaction(){
        this.transactionID = new SimpleStringProperty("0");
        this.time = new SimpleStringProperty("0");
        this.amount = new SimpleStringProperty("0");
        this.description = new SimpleStringProperty("0");
    }
    
    public Transaction(String transactionID, String time, String amount, String description) {
        this.transactionID = new SimpleStringProperty(transactionID);
        this.time = new SimpleStringProperty(time);
        this.amount = new SimpleStringProperty(amount);
        this.description = new SimpleStringProperty(description);
    }

    // Getters and Setters (using StringProperty for JavaFX binding)
    public String getTime() {
        return time.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getDescription() {
        return description.get();
    }

    // Properties (if needed for binding)
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


    public void readFile(){
    try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            ArrayList<Transaction> debitList=new ArrayList<>();
            ArrayList<Transaction> creditList=new ArrayList<>();
            ArrayList<Transaction> overviewList=new ArrayList<>();
            boolean header=true;
            while ((line = reader.readLine()) != null) {
                if(header==true){
                    header=false;
                    continue;
                }
                
                String[] columns = line.split(","); 
                String type = columns[2];          
                String amount = columns[3];        
                String description = columns[4];   
                String transactionID=columns[1];
                String time=columns[5];  
                String name=columns[0];
                
                if(Username.equals(name)){
                overviewList.add(new Transaction(transactionID,time,amount,description));
                if ("debit".equalsIgnoreCase(type)) {
                    debitList.add(new Transaction(transactionID,time,amount,description));
                } else if ("credit".equalsIgnoreCase(type)) {
                    creditList.add(new Transaction(transactionID,time,amount,description));
                }
             }
                else{
                    continue;
                }
                debitData.setAll(debitList);
                creditData.setAll(creditList);
                overviewData.setAll(overviewList);
            }
            
             
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
   public ObservableList<Transaction> getDebitData() {
        return debitData;
    }

    public ObservableList<Transaction> getCreditData() {
        return creditData;
    }
    
    public ObservableList<Transaction> getOverviewData() {
        return overviewData;
    }
        
    }