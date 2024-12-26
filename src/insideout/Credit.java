package insideout;
import static insideout.InsideOut.balance;
import static insideout.InsideOut.getBalance;
import static insideout.InsideOut.popupMessage;
import static insideout.InsideOut.store;
import static insideout.InsideOut.transactionID;
import static insideout.InsideOut.transactioninfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.scene.control.Label;
public class Credit {
    private static String username="";
    private static String recorddebitandcredit="/Users/cye/NewFolder/InsideOut/src/recorddebitandcredit.csv";
    private static String recordcredit="/Users/cye/NewFolder/InsideOut/src/recordcredit.csv";
    private static double amount=0.0;
    private static String description="";
    private String transactioninfo="";
    private String transactioninfoCreditcsv="";
    private static String type="";
    private static String category="";
    private Label lbl=new Label();
    
    public Credit(String username,double amount,String description,String type,String category){
        this.username=username;
        this.amount=amount;
        this.description=description;
        this.type=type;
        this.category=category;
        updateCredit();
    }
    
    
    private void updateCredit(){
        String line="";
        creditID();
        try(BufferedReader reader=new BufferedReader(new FileReader(recordcredit));){
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
        
        if(amount<=0){
            lbl=new Label("Cash Amount can't be negative or zero!");
        }
        else{
        balance-=amount;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        
        if(balance<0){
        lbl=new Label("Balance less than 0!");
        }
        else{
        transactioninfoCreditcsv = username + "," + transactionID + ","+type+","+amount+"," +description+","+ date + "," + balance+","+category;
        transactionID++;
        lbl=new Label("Succesfully Credited");
        store(recordcredit,transactioninfoCreditcsv);
        readLastTransactionID();
        }
 
    }
    }
    public Label getLabel(){
        return lbl;
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
        int lastID=Integer.parseInt(row[1]);
        transactionID=lastID+1;
        } 
        else{
           transactionID=1; 
        }
        
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        transactioninfo = username + "," + transactionID + ","+type+","+amount+"," +description+","+ date + "," + balance+","+category;
        store(recorddebitandcredit,transactioninfo); 
    }catch (IOException e) {
       e.printStackTrace();
    }
}
    
    public void creditID(){
    String line="";
    ArrayList<String> str=new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(recordcredit))) {
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
        int lastID=Integer.parseInt(row[1]);
        transactionID=lastID+1;
        } 
        else{
           transactionID=1; 
        }
    }catch (IOException e) {
       e.printStackTrace();
    }

            }
}
