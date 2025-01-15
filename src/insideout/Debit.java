package insideout;

import static insideout.InsideOut.splitCSVLine;
import static insideout.InsideOut.store; // method to store in main class
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Debit {
    private String username="";
    private String recorddebitandcredit = "src/transactions.csv";
    private String recorddebit="src/recorddebit.csv";
    private double amount=0.0;
    private String description="";
    private String type="";
    private String category="";
    private Label lbl=new Label();
    private String transactioninfo="";
    private String transactioninfoDebitcsv="";
    private String transactionID="";
    private String debitID="";
    private double balance=0.0;
    private ArrayList<String> getBalance=new ArrayList<>();
    
    public Debit(String username,double amount,String description,String type,String category){
        this.username=username;
        this.amount=amount;
        this.description=description;
        this.type=type;
        this.category=category;
        updateDebit();
    }
    
    
    private void updateDebit(){
        Savings savings = new Savings(amount, username);
        boolean status = savings.updateDeductStatus(); // get the status(true--debit need to be deducted)

        String line = "";
        debitID();
        readLastTransactionID();
        try (BufferedReader reader = new BufferedReader(new FileReader(recorddebitandcredit));) {
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] columns = line.split(",");

                if (columns[0].equals(username)) { // if username is not the target , then loop it again
                    getBalance.add(line);
                } else {
                    continue;
                }
            }

            // to find the last balance user hold
            if (!getBalance.isEmpty()) {
                int index = getBalance.size() - 1;
                String[] splitedrow = getBalance.get(index).split(",");
                int balanceIndex = splitedrow.length - 2;
                balance = Double.parseDouble(splitedrow[balanceIndex]);
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String yesno = "";
        if (amount <= 0) {
            lbl = new Label("Cash Amount can't be negative or zero!");
        } else {
            if (status == false) {
                balance += amount;
                yesno = "No";
            } else {
                savings.findPercentage();
                double saving = savings.getSavings();
                balance += (amount - saving); // note : savings will only be added to balance at end of month eg (31 august);
                yesno = "Yes";
            }

            BigDecimal bd = new BigDecimal(balance);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            lbl = new Label("Succesfully Debited");
            transactioninfoDebitcsv = username + "," + debitID + "," + type + "," + String.format("%.2f", amount) + "," + description + "," + String.valueOf(dateFormat.format(date)) + "," + bd + "," + category + "," + yesno;
            store(recorddebit, transactioninfoDebitcsv); // record for debit csv
            transactioninfo = username + "," + transactionID + "," + type + "," + String.format("%.2f", amount) + "," + description + "," + String.valueOf(dateFormat.format(date)) + "," + bd + "," + category;
            store(recorddebitandcredit, transactioninfo);
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
    String line="";
    ArrayList<String> str=new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(recorddebitandcredit))) {
        boolean header = true;
        while ((line = reader.readLine()) != null) {
            if (header) {
                header = false;
                continue;
            }
            
            str.add(line);
        }
        
        if(str.size()!=0){
        int lastIndex=str.size()-1;
        String row[]=str.get(lastIndex).split(",");
        int ID=Integer.parseInt(row[1].replace("TS",""))+1;
        transactionID="TS"+String.format("%06d",ID);
        } 
        else{
           transactionID="TS"+String.format("%06d",1);
        }

    }catch (IOException e) {
       e.printStackTrace();
    }
}

    public void debitID() {
        String line = "";
        ArrayList<String> str = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(recorddebit))) {
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                str.add(line);
            }

            if (str.size() != 0) {
                int lastIndex = str.size() - 1;
                String row[] = splitCSVLine(str.get(lastIndex), 9);
                int ID = Integer.parseInt(row[1].replace("DB", "")) + 1;
                debitID = "DB" + String.format("%06d", ID);
            } else {
                debitID = "DB" + String.format("%06d", 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
    
   

