package insideout;
import static insideout.InsideOut.getBalance;
import static insideout.InsideOut.popupMessage;
import static insideout.InsideOut.store;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.scene.control.Label;
public class Credit {
    private static String username="";
    private static String recorddebitandcredit="src/recorddebitandcredit.csv";
    private static String recordcredit="src/recordcredit.csv";
    private static double amount=0.0;
    private static String description="";
    private String transactionID="";
    private String creditID="";
    private static String type="";
    private static String category="";
    private static double balance=0.0;
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
           int index = getBalance.size() - 1; 
            String[] splitedrow = getBalance.get(index).split(",");
            String balanceStr = splitedrow[6]; 
            balance = Double.parseDouble(balanceStr); 
            System.out.println("Balance "+balance);
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
        BigDecimal bd = new BigDecimal(balance);
        bd=bd.setScale(2, RoundingMode.HALF_UP);
            String transactioninfo = username + "," + transactionID + "," + type + "," + String.format("%2f", amount) + "," + description + "," + date + "," + bd + "," + category;
            String transactioninfoCreditcsv = username + "," + creditID + "," + type + "," + String.format("%2f", amount) + "," + description + "," + date + "," + bd + "," + category;
        lbl=new Label("Succesfully Credited");
        store(recordcredit, transactioninfoCreditcsv);
        store(recorddebitandcredit, transactioninfo);
        }
 
    }
    }
    public Label getLabel(){
        return lbl;
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
        int ID=Integer.parseInt(row[1].replace("CD",""))+1;
        creditID="CD"+String.format("%06d",ID);
        } 
        else{
           creditID="CD"+String.format("%06d",1);
        }
    }catch (IOException e) {
       e.printStackTrace();
    }

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
    
}
