package insideout;

import static insideout.InsideOut.store; // method to store in main class
import static insideout.InsideOut.transactionID;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.scene.control.Label;

public class Debit {
    private String username="";
    private String recorddebitandcredit="/Users/cye/NewFolder/InsideOut/src/recorddebitandcredit.csv";
    private String recorddebit="/Users/cye/NewFolder/InsideOut/src/recorddebit.csv";
    private double amount=0.0;
    private String description="";
    private String type="";
    private String category="";
    private Label lbl=new Label();
    private static String transactioninfo="";
    private static String transactioninfoDebitcsv="";
    private static int transactionID=1;
    private static double balance=0.0;
    private static ArrayList<String> getBalance=new ArrayList<>();
    
    public Debit(String username,double amount,String description,String type,String category){
        this.username=username;
        this.amount=amount;
        this.description=description;
        this.type=type;
        this.category=category;
        updateDebit();
    }
    
    
    private void updateDebit(){
        Savings debit=new Savings(amount);
        System.out.println("Savings : "+amount);
        boolean userStatus=debit.getdeductStatus();
        System.out.println("userStatus "+userStatus);
        
        String line="";
        readLastTransactionID();
        try(BufferedReader reader=new BufferedReader(new FileReader(recorddebitandcredit));){
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if(header){
                    header=false;
                    continue;
                }
                
            String [] columns=line.split(",");
           
            if(columns[0].equals(username)){ // if username is not the target , then loop it again
                    getBalance.add(line);
                }
            else{
                continue;
            }
            } 

            // to find the last balance user hold
            if(!getBalance.isEmpty()){
            int index=getBalance.size()-1;
            String []splitedrow=getBalance.get(index).split(",");
            int balanceIndex=splitedrow.length-2;
            balance=Double.parseDouble(splitedrow[balanceIndex]);
            }
        }catch (IOException ex){
            ex.printStackTrace();
            }      
        
        String yesno="";
        if(amount<=0){
            lbl=new Label("Cash Amount can't be negative or zero!");
        }
        else{
            if(userStatus==false){
                balance+=amount;
                yesno="No";
            }
            else{
                double savings=debit.getSavings();
                System.out.println("Savings is "+savings);
                balance+=(amount-savings); // note : savings will only be added to balance at end of month eg (31 august);   
                yesno="Yes";
                userStatus=false;
                debit.setdeductStatus(userStatus);
            }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        lbl=new Label("Succesfully Debited");       
        transactioninfo = username + "," + transactionID + ","+type+","+amount+"," +description+","+ date + "," + balance+","+category;
        transactioninfoDebitcsv = username + "," + transactionID + ","+type+","+amount+"," +description+","+ date + "," + balance+","+category+","+yesno;
        transactionID++;
        store(recorddebitandcredit,transactioninfo); // record for Transaction class
        store(recorddebit,transactioninfoDebitcsv); // record for debit csv
        }
 
    }
    
    public Label getLabel(){
        return lbl;
    }
    
    // for savingFile to calculate saving
    public double getDebit(){
        return amount;
    }
            
    public void readLastTransactionID() {
    String line;
    try (BufferedReader reader = new BufferedReader(new FileReader(recorddebitandcredit))) {
        boolean header = true;
        while ((line = reader.readLine()) != null) {
            if (header) {
                header = false;
                continue;
            }
            String[] columns = line.split(",");
            if (columns.length > 1) {  // Ensure there are enough columns in the row
           try {
        int lastID = Integer.parseInt(columns[1].trim());  // Parse transaction ID (column 1 should be the ID)
        if (lastID >= transactionID) {
            transactionID = lastID + 1;  // Update transaction ID to the next number
            transactionID=Integer.parseInt(String.format("%08d",transactionID));
        }
    } catch (NumberFormatException e) {
        // If parsing fails, this row is skipped (invalid transaction ID format)
        System.out.println("Invalid transaction ID in row: " + line);
    }
}
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
    
   
}
