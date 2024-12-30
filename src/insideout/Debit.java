package insideout;

import static insideout.InsideOut.store; // method to store in main class
import insideout.InsideOut;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javafx.scene.control.Label;

public class Debit {
    private String username="";
    private String recorddebitandcredit="/Users/cye/NewFolder/InsideOut/src/recorddebitandcredit.csv";
    private String recorddebit="/Users/cye/NewFolder/InsideOut/src/recorddebit.csv";
    private String savingFile="/Users/cye/NewFolder/InsideOut/src/savings - Sheet1.csv";
    private double amount=0.0;
    private String description="";
    private String type="";
    private String category="";
    private Label lbl=new Label();
    private String transactioninfo="";
    private String transactioninfoDebitcsv="";
    private String transactionID="";
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
        InsideOut insideOut = new InsideOut();
        boolean status=insideOut.userStatus;
        Savings debit=new Savings(amount,username);
        String line="";
        debitID();
        try(BufferedReader reader=new BufferedReader(new FileReader(recorddebit));){
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
            int balanceIndex=splitedrow.length-3;
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
            if(status==false){
                balance+=amount;
                yesno="No";
            }
            else{
                double savings=debit.getSavings();
                balance+=(amount-savings); // note : savings will only be added to balance at end of month eg (31 august);   
                yesno="Yes";              
            }
            
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        lbl=new Label("Succesfully Debited");       
        transactioninfoDebitcsv = username + "," + transactionID + ","+type+","+amount+"," +description+","+ date + "," + balance+","+category+","+yesno;
        store(recorddebit,transactioninfoDebitcsv); // record for debit csv
        readLastTransactionID();
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
        
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        transactioninfo = username + "," + transactionID + ","+type+","+amount+"," +description+","+ date + "," + balance+","+category;
        store(recorddebitandcredit,transactioninfo); 
    }catch (IOException e) {
       e.printStackTrace();
    }
}
    
    public void debitID(){
    String line="";
    ArrayList<String> str=new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(recorddebit))) {
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
        int ID=Integer.parseInt(row[1].replace("DB",""))+1;
        transactionID="DB"+String.format("%06d",ID);
        } 
        else{
           transactionID="DB"+String.format("%06d",1);
        }
    }catch (IOException e) {
       e.printStackTrace();
    }

            }
    

}
    
   

