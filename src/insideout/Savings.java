package insideout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static insideout.InsideOut.store; // method to store in main class
import static insideout.InsideOut.transactionID;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.Label;

public class Savings {
    private static String username="";
    private static double percentage=0.0;
    private static double debit=0.0;  // get debit from recorddebitandcredit
    private static double savings=0.0; // update the balance at recorddebitandcredit at the end of month
    private static double totalSavings=0.0; // for display purpose at view balance
    private static String savingFile="/Users/cye/NewFolder/InsideOut/src/savings - Sheet1.csv";
    private static String recorddebitandcredit="/Users/cye/NewFolder/InsideOut/src/recorddebitandcredit.csv";
    private static boolean deductdebit=false;
    private static Label lbl;
    
    public Savings(String username,String percentage){
        this.username=username;
        this.percentage=Double.parseDouble(percentage);
        // check whether user have a pending saving before updating the percentage
        updateSavingPercentage();
    }
    
    public Savings(double debit){ // default constructor
        this.debit=debit;
        updateDebitAmount();
    }
    
    public Savings(){
       isEndOfMonth();
    }
    
    // update saving percentage enter by user
    // check whether user got savings pending
    // if no saving pending then ok
    // if have saving pending popupMessage()
    public static void updateSavingPercentage(){
        String readerLine="";
        boolean header=true;
        try(BufferedReader reader=new BufferedReader(new FileReader (savingFile))){
            pendingSaving:{
            while((readerLine=reader.readLine())!=null){
                if(header == true){
                    header=false;
                    continue;
                }
                
                String row[]=readerLine.split(",");
                if(username.equals(row[0])){ 
                    if(row.length<4 || row[2].isEmpty()){ // pendingSavings but no debited amount yet 
                        lbl=new Label("There are Pending Saving.\nPlease make debit before next saving. "); 
                        deductdebit=true;
                        break pendingSaving;
                    }
                }else{ // not user then skip
                    continue;     
            }
        }
          
            // if no pendingSaving add a percentage into the file
          String writerLine=username+","+transactionID+","+percentage;
          try(BufferedWriter writer=new BufferedWriter(new FileWriter(savingFile,true))){
              writer.write(writerLine);
          }
          lbl=new Label(percentage+"% will be deducted on next debit as saving.");
          deductdebit=true; // next debit need to be deducted for savings
          
        }
        }catch(IOException ex){
           ex.printStackTrace();
    }
    }
    
    
    // update debit amount into savingCSV
    public static void updateDebitAmount(){
        savings=debit*(percentage/100); 
        totalSavings+=savings;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        String update=debit+","+savings+","+totalSavings+","+date;
        String readline="";
        String writeline="";
        boolean header=true;
        StringBuilder buildLine=new StringBuilder();
        ArrayList<String> addLine=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
            while((readline=reader.readLine())!=null){
                if(header==true){
                    addLine.add(readline);
                    header=false;
                    continue;
                }
                
                String []row=readline.split(",");
                if(row[0].equals(username)){
                   if(row.length<4 || row[2].isEmpty()){ // pendingSavings but no debited amount yet 
                       for(int i=0;i<row.length;i++){
                       buildLine.append(row[i]).append(",");
                       }
                       buildLine.append(update);
                       addLine.add(String.valueOf(buildLine));
                    }
                   else{
                       addLine.add(readline);
                   }
                }else{
                    addLine.add(readline);
                }    
            }
           
        }
        catch(IOException ex){
            ex.printStackTrace();
                }
        
        try(BufferedWriter writer=new BufferedWriter (new FileWriter(savingFile))){
            for(String updatecsv:addLine){
                writer.write(updatecsv);
                writer.newLine();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    
   public static void readLastTransactionID() {
    String line="";
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
   
   public static double getSavings(){
       return savings;
   }
   
    public static Label getLabel(){
        return lbl;
    }
    
    public static boolean getdeductStatus(){
        return deductdebit;
    }
    
    public static void setdeductStatus(boolean userStatus){
        deductdebit=userStatus;
    }
    
    public static void isEndOfMonth() {
        Calendar calendar = Calendar.getInstance();
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);  

        if(currentDay==lastDayOfMonth){
            endOfMonthUpdateCsv();
        }
        
    }
    
    public static void endOfMonthUpdateCsv() {
        double balance=0.0;
        String readline="";
        boolean header=true;
        ArrayList<String> addLine=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(recorddebitandcredit))){
            while((readline=reader.readLine())!=null){
                if(header==true){
                    header=false;
                    continue;
                }
                
                String row[]=readline.split(",");
                if(row[0].equals(username)){
                    addLine.add(readline);
                }
            }
            
            int lastbalanceindex=addLine.size()-1;
            String getLastBalance[]=addLine.get(lastbalanceindex).split(",");
            balance=Double.parseDouble(getLastBalance[6]);
            balance+=totalSavings;
            
           
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
           readLastTransactionID();
           StringBuilder line=new StringBuilder();
           Date date = new Date();
           SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
           line.append(username).append(",").append(transactionID).append(",").append("Savings").append(",").append(savings).append(",")
                   .append("Savings").append(",").append(date).append(",").append(balance).append(",");
           
           store(recorddebitandcredit,String.valueOf(line));

    }
}



