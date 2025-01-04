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
    private ObservableList<Transaction> appliedLoanData=FXCollections.observableArrayList();
    private double creditTotal=0.0;
   
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
        return description.get();
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
        
        String str;
        boolean head=true;
        try(BufferedReader br=new BufferedReader(new FileReader(loanApplied))){
          ArrayList<Transaction> LoanApply=new ArrayList<>();
          while((str=br.readLine())!=null){
            if(head==true){
               head=false;
               continue;
            }
            
            
            String lines[]=str.split(",");
            String user= lines[0];
            String loanID= lines[1];
            String principal = lines[2];
            String interest = lines[3];
            String totalLoan = lines[4];
            String outstandingBalance = lines[5];
            String period=lines[6];
            String applyTime=lines[8];
            String dueTime=lines[9];
            String status=lines[10];
            if(username.get().equals(user)){
              LoanApply.add(new Transaction(loanID, principal,interest, totalLoan, outstandingBalance,period,applyTime,dueTime,status));
            }
          }
          
          appliedLoanData.setAll(LoanApply);
            
        }catch(IOException ex){
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
    
    public double getDebitTotal(){
        return debitTotal;
    }
    
    public double getCreditTotal(){
        return creditTotal;
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
    
     public String getLoanID(){ 
         return loanID.get(); }
     
     public StringProperty loanIDProperty(){
        return loanID; }

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

    public Label getBalance(){
       Label lbl=new Label("RM "+String.valueOf(balance));
       return lbl;
    }
    
}