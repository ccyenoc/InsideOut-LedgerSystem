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
    private double debitTotal=0.0;
    private double creditTotal=0.0;
    // for overView
    private StringProperty transactionID = new SimpleStringProperty("");
    private StringProperty time = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty balance = new SimpleStringProperty("");
    private String overview = "src/transactions.csv";
   
    private ObservableList<Transaction> debitData = FXCollections.observableArrayList();
    private ObservableList<Transaction> creditData = FXCollections.observableArrayList();
    private ObservableList<Transaction> overviewData = FXCollections.observableArrayList();
    private ObservableList<Transaction> appliedLoanData=FXCollections.observableArrayList();
   
    // for loan applied
     private String loanApplied="src/creditloan-apply.csv";
     private final StringProperty loanID = new SimpleStringProperty("");
     private final StringProperty principal = new SimpleStringProperty("");
     private final StringProperty interest = new SimpleStringProperty("");
     private final StringProperty totalLoan = new SimpleStringProperty("");
     private final StringProperty outstandingBalance = new SimpleStringProperty("");
     private final StringProperty period = new SimpleStringProperty("");
     private final StringProperty applyTime = new SimpleStringProperty("");
     private final StringProperty dueTime = new SimpleStringProperty("");
     private final StringProperty status = new SimpleStringProperty("");
     private final StringProperty date = new SimpleStringProperty("");
     
     // for debit
     private String debitFile="src/recorddebit.csv";
     private final StringProperty deducted = new SimpleStringProperty("");
     
     // for credit
     private String creditFile="src/recordcredit.csv";
     private final StringProperty category = new SimpleStringProperty("");
     
     public Transaction() {
    }
     
    public Transaction(String name) {
        this.username.set(name);
        readFile();
    }

    //constructor for overview
    public Transaction(String name, String transactionID, String time, String amount, String description,double balance) {
        this.username.set(name);
        this.transactionID.set(transactionID);
        this.time.set(time);
        this.amount.set(amount);
        this.description.set(description);
        this.balance.set(String.valueOf(balance));
    }
    
    // constructor for dbeit
     public Transaction(String name, String transactionID, String date, String amount, String description,String deducted) {
        this.username.set(name);
        this.transactionID.set(transactionID);
        this.date.set(date);
        this.amount.set(amount);
        this.description.set(description);
        this.deducted.set(deducted);
    }
     
   // constructor for credit
     public Transaction(String name, String transactionID, String date, double amount, String description,String category) {
        this.username.set(name);
        this.transactionID.set(transactionID);
        this.date.set(date);
        this.amount.set(String.valueOf(amount));
        this.description.set(description);
        this.category.set(category);
    }
     
    
    // constructor for loan applied
    public Transaction(String loanID,String principal,String interest,String totalLoan,String outstandingBalance,String period,String applyTime,String dueTime,String status){
    this.loanID.set(loanID);
    this.principal.set(principal);
    this.interest.set(interest);
    this.totalLoan.set(totalLoan);
    this.outstandingBalance.set(outstandingBalance);
    this.period.set(period);
    this.applyTime.set(applyTime);
    this.dueTime.set(dueTime);
    this.status.set(status);
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
        return unformatCSV(description.get());
    }


    // Method to read CSV and populate the data lists
    public void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(debitFile));
                BufferedReader br=new BufferedReader(new FileReader(creditFile));
                BufferedReader read=new BufferedReader(new FileReader(loanApplied));
                BufferedReader breader=new BufferedReader(new FileReader(overview))) {
            String readDebit;
            String readCredit;
            String readLoan;
            String line;
            
            ArrayList<Transaction> debitList = new ArrayList<>(); // for debit 
            ArrayList<Transaction> creditList = new ArrayList<>();  // for credit
            ArrayList<Transaction> overviewList = new ArrayList<>(); // for overview
            ArrayList<Transaction> LoanApply=new ArrayList<>(); // for loan apply

            // for debit
            boolean header = true;
            while ((readDebit = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] columns = splitCSVLine(readDebit, 9);
                String name=columns[0];
                String debitID=columns[1];
                String amount=columns[3];
                String description=columns[4];
                String date=columns[5];
                String deducted=columns[8];
                

                if (username.get().equals(name)) {
                    debitTotal+=Double.parseDouble(amount);
                   debitList.add(new Transaction(name,debitID,date,amount,description,deducted));
               }

            debitData.setAll(debitList);
            }
            
            // for credit
            boolean head=true;
            while((readCredit=br.readLine())!=null){
              if(head==true){
                head=false;
                continue;
              }

                String[] row = splitCSVLine(readCredit, 8);
              String name=row[0];
              String creditID=row[1];
              double amount=Double.parseDouble(row[3]);
              String description=row[4];
              String date=row[5];
              String category=row[7];
            
               if (username.get().equals(name)) {
                   creditTotal+=amount;
                   creditList.add(new Transaction(name,creditID,date,amount,description,category));
               }
                creditData.setAll(creditList);
            }
            
            // for overview 
            boolean skipheader=true;
            while((line=breader.readLine())!=null){
              if(skipheader==true){
                skipheader=false;
                continue;
              }
              
                String[] columns = line.split(",");
                for(int i=0;i<columns.length;i++){
                 System.out.println(columns[i]);;
                }
               
                String name = columns[0];
              
               if (username.get().equals(name)) {
                   String lines[] = splitCSVLine(line, 8);
                    String type = lines[2];
                    String amount = lines[3];
                    String description = lines[4];
                    String transactionID = lines[1];
                    String time = lines[5];
                    double balance=Double.parseDouble(lines[6]);
                
                    overviewList.add(new Transaction(name, transactionID, time, amount, description,balance));
               }
                 overviewData.setAll(overviewList);
            }
            
            while((readLoan=read.readLine())!=null){
            if(head==true){
               head=false;
               continue;
            }

            String lines[]=readLoan.split(",");
            String user= lines[0];

                if (username.get().equals(user)) {
                    String loanID = lines[1];
                    String principal = lines[2];
                    String interest = lines[3];
                    String totalLoan = lines[4];
                    String outstandingBalance = lines[5];
                    String period = lines[6];
                    String applyTime = lines[8];
                    String dueTime = lines[9];
                    String status = lines[10];
              LoanApply.add(new Transaction(loanID, principal,interest, totalLoan, outstandingBalance,period,applyTime,dueTime,status));
            }
          }
          
          appliedLoanData.setAll(LoanApply);
           
           
           
            
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
    
    public ObservableList<Transaction> getAppliedLoan() {
        return appliedLoanData;
    }
    
  
    public String getDeducted(){
      return deducted.get();
    }
    
    public String getCategory(){
      return category.get();
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
    
    public StringProperty balanceProperty(){
      return balance;
    }
    
    public StringProperty deductedProperty(){
        return deducted;
    }
    
    public StringProperty categoryProperty(){
      return category;
    }
    
    public StringProperty loanIDProperty(){
        return loanID;
    }
    
    public StringProperty dateProperty() {
    return date;
}

    
    
     public String getLoanID(){ 
         return loanID.get(); }
     
   
    public String getPrincipal() { 
        return principal.get(); }
    
    public StringProperty principalProperty(){
        return principal; }

    public String getInterest(){
        return interest.get(); }
    
    public StringProperty interestProperty(){
        return interest; }

    public String getTotalLoan(){
        return totalLoan.get(); }
    
    public StringProperty totalLoanProperty(){
        return totalLoan; }

    public String getOutstandingBalance(){
        return outstandingBalance.get(); }
    
    public StringProperty outstandingBalanceProperty(){
        return outstandingBalance; }

    public String getPeriod(){ 
        return period.get(); }
    
    public StringProperty periodProperty(){
        return period; }

    public String getApplyTime(){
        return applyTime.get(); }
    
    public StringProperty applyTimeProperty(){
        return applyTime; }

    public String getDueTime(){ 
        return dueTime.get(); }
    
    public StringProperty dueTimeProperty(){
        return dueTime; }

    public String getStatus(){
        return status.get(); }
    
    public StringProperty statusProperty(){
        return status; }

    public double getDebitTotal(){
     return debitTotal;
    }
    
    public double getCreditTotal(){
      return creditTotal;
    }
    
    public String balance(String username){
        String str="";
        String lastBalance="";
        boolean header=true;
        try(BufferedReader reader=new BufferedReader(new FileReader(overview))){
            while((str=reader.readLine())!=null){
          if(header==true){
           header=false;
           continue;
          }
          
          String row[]=str.split(",");
          if(row[0].equals(username)){
            System.out.println("equals");
            lastBalance=row[6];
          } else {
              lastBalance = String.valueOf(0.00);

          }
            }
          
        }catch(IOException ex){
          ex.printStackTrace();
        }
       String viewBalance="RM "+lastBalance;
       return viewBalance;
    }

    private static String[] splitCSVLine(String line, int z) {
        String[] result = new String[z];
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes=false;
        int index=0;

        for (int i=0; i < line.length(); i++) {
            char c=line.charAt(i);
            if (c=='"') {
                inQuotes = !inQuotes; // Toggle quotes
            } else if (c == ',' && !inQuotes) {
                result[index++] = unformatCSV(currentField.toString());
                currentField.setLength(0); // Clear the field
            } else {
                currentField.append(c);
            }
        }
        result[index] = unformatCSV(currentField.toString()); // Add last field
        return result;
    }
    
    public static String unformatCSV(String value) {
           if (value == null || value.isEmpty()) return ""; // Handle empty values
           if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1); // Remove surrounding quotes
           }
        return value.replace("\"\"", "\""); // Unescape double quotes
       }
    
}