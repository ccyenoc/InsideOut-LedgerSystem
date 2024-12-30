package insideout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static insideout.InsideOut.store; // method to store in main class
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javafx.scene.control.Label;

public class Savings {
    private String username="";
    private double percentage=0.0;
    private double debit=0.0;  // get debit from recorddebitandcredit
    private double savings=0.0; // update the balance at recorddebitandcredit at the end of month
    private double totalSavings=0.0; // for display purpose at view balance
    private double savingPerMonth=0.0;
    protected String savingFile="/Users/cye/NewFolder/InsideOut/src/savings - Sheet1.csv";
    private String recorddebitandcredit="/Users/cye/NewFolder/InsideOut/src/recorddebitandcredit.csv";
    protected boolean deductdebit=false;
    private boolean headerinfile=true;
    private Label lbl;
    private String transactionID="";
    
    public Savings(String username,String percentage){
        this.username=username;
        this.percentage=Double.parseDouble(percentage);
        // check whether user have a pending saving before updating the percentage
        checkValidPercentage();
    }
    
    public Savings(double debit,String name){
        this.debit=debit;
        this.username=name;
        findPercentage();
    }

    public Savings(String username){
        this.username=username;
    }
    
    // update saving percentage enter by user
    // check whether user got savings pending
    // if no saving pending then ok
    // if have saving pending popupMessage()
    
    public void checkValidPercentage(){
        String line="";
        boolean header=true;
        
        ArrayList<String> addLine=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
            pending:{
            while((line=reader.readLine())!=null){
                if(header==true){
                    header=false;
                    continue;
                }
 
                String row[]=line.split(",");
                if(row[0].equals(username)){
                    addLine.add(line);
                } 
            }
            
            int lastIndex=addLine.size()-1;
            if(lastIndex<0){
              lbl=new Label(percentage+"% will be deducted on next debit as saving.");
              deductdebit = true; 
              updateSavingPercentage();
            }else{
                String[] check=addLine.get(lastIndex).split(",");
                if(check.length<4){
                    lbl = new Label("There are Pending Saving.\nPlease make debit before next saving.");
                    deductdebit = false; 
                    break pending;
                }
                else{
                    lbl=new Label(percentage+"% will be deducted on next debit as saving.");
                    deductdebit = true;      
                    updateSavingPercentage();
                }         
        }
        }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void updateSavingPercentage() {
      savingFileID();
      String line=username+","+transactionID+","+percentage;
      String str="";
      ArrayList<String> getLast=new ArrayList<>();
      boolean header=true;
      try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
          while((line=reader.readLine())!=null){
             if(header){
               header=false;
               getLast.add(line);
               continue;
             }
             
             getLast.add(line); 
          }
          
          int lastIndex=getLast.size()-1;
          String row[]=getLast.get(lastIndex).split(",");
          if(row[7]!=null && Double.parseDouble(row[7])<0){
              getLast.set(lastIndex,line+","+""+","+""+","+""+","+""+","+"");
               try(BufferedWriter writer=new BufferedWriter (new FileWriter(savingFile))){
           for (int i = 0; i < getLast.size(); i++) {
                  writer.write(getLast.get(i));
 
                if (i<getLast.size()-1) {
                writer.newLine();  // Add newline only if it's not the last element
    }
}
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
              
          }
          else{
           BufferedWriter bw = null;
           try {
               bw = new BufferedWriter(new FileWriter(savingFile, true));  // true to append
                 if(headerinfile==true){
                    bw.newLine();
                }
                bw.write(line);  // Write the line
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                   if (bw != null) {
                       bw.close();  // Always close the BufferedWriter
                    }
               } catch (IOException e) {
                  e.printStackTrace();
               }
        }
          }
    }catch(IOException ex){
        ex.printStackTrace();
      }
    }

    // update debit amount into savingCSV
    public void updateDebitAmount(double percentage){
        savings=debit*(percentage/100); 
        totalSavings=getTotalSavings(username);
        totalSavings+=savings;
        savingPerMonth=savingPerMonth();
        savingPerMonth+=savings;

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        String update=debit+","+savings+","+totalSavings+","+date+","+savingPerMonth;
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
                   if(row[3].isEmpty()){ // pendingSavings but no debited amount yet 
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
           for (int i = 0; i < addLine.size(); i++) {
                  writer.write(addLine.get(i));
 
                if (i<addLine.size()-1) {
                writer.newLine();  // Add newline only if it's not the last element
    }
}
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void findPercentage(){
        try (BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
           String line="";
           ArrayList<String> findUser=new ArrayList<>();
           boolean header=true;
           
           while((line=reader.readLine())!=null){
               if(header==true){
                   header=false;
                   continue;
               }
  
               String []row=line.split(",");
               if(username.equals(row[0])){
                   findUser.add(line);
               }
           }
           
          int lastindex=findUser.size()-1;
          String rows[]=findUser.get(lastindex).split(",");
          this.percentage=Double.parseDouble(rows[2]);
          updateDebitAmount(percentage);
         
       }catch (IOException ex){
           ex.printStackTrace();
       }
    }
    
    
   public void savingFileID(){
    String line="";
    ArrayList<String> str=new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(savingFile))) {
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
        int ID=Integer.parseInt(row[1].replace("SV",""))+1;
         transactionID="SV"+String.format("%06d,",ID);
        } 
        else{
           transactionID="SV"+String.format("%06d,",1);
        }
    }catch (IOException e) {
       e.printStackTrace();
    }

            }
     
   public double getSavings(){
       return savings;
   }
   
    public Label getLabel(){
        return lbl;
    }
    
    public boolean getdeductStatus(){
        return deductdebit;
    }
    
    public void setdeductStatus(boolean deductdebit){
        this.deductdebit=deductdebit;
    }
    
    public double savingPerMonth(){
      try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
           String line="";
           ArrayList<String> findUser=new ArrayList<>();
           boolean header=true;
           
           while((line=reader.readLine())!=null){
               if(header==true){
                   header=false;
                   continue;
               }
  
               String []row=line.split(",");
               if(username.equals(row[0])){
                   findUser.add(line);
               }
           }
           
           
          int lastindex=findUser.size()-1;
          if(lastindex<=0){
              savingPerMonth=0.0;
          }      
          else{
            String rows[]=findUser.get(lastindex).split(",");
            if(rows[1]==null && rows[3]==null){ // debited
               int lastindexs=findUser.size()-2;
               String lines[]=findUser.get(lastindexs).split(",");
               savingPerMonth=Double.parseDouble(lines[7]);               
             }
            else{
             savingPerMonth=Double.parseDouble(rows[7]);
            }
          }
      
      }catch(IOException ex){
         ex.printStackTrace();
      }
      
      return savingPerMonth;
    
    }
    
    public void isEndOfMonth() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
Calendar current = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));

        // Set the due date to Dec 31, 2025, 00:00:00 MYT
        Calendar dueDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        dueDate.set(2025, Calendar.DECEMBER, 31, 0, 0, 0);
        dueDate.set(Calendar.MILLISECOND, 0);

        // Check if current is strictly after the due date
        if (current.after(dueDate)) {
            endOfMonthUpdateCsv();
        } else {
            System.out.println("It's not past the due date yet.");
        }
        
    }
    
    public void endOfMonthUpdateCsv() {
        getTotalSavingsPerMonth();
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
                if(row[0].equals(username) && row.length>=5){ 
                    balance=Double.parseDouble(row[6]); // get user last balance
                }
            }
            
            balance+=totalSavings;
          
           readLastTransactionID();
           StringBuilder line=new StringBuilder();
           Date date = new Date();
           SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
           line.append(username).append(",").append(transactionID).append(",").append("Savings").append(",").append(savings).append(",")
                   .append("Savings").append(",").append(date).append(",").append(balance).append(",").append("Savings");
           
           store(recorddebitandcredit,String.valueOf(line));
            
           StringBuilder labelSavingperMonth=new StringBuilder();
           labelSavingperMonth.append("").append(",").append("").append(",").append("").append(",").append("").append(",")
                   .append("").append(",").append("").append(",").append("").append("-1");
            store(savingFile,String.valueOf(labelSavingperMonth));
           
           
            }catch(IOException ex){
            ex.printStackTrace();
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
 
    
    public double getTotalSavings(String name){ // total savings of user (user visible)
       double saving=0.0;
       try (BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
           String line="";
           ArrayList<String> findUser=new ArrayList<>();
           ArrayList<String> findLastTotal=new ArrayList<>();
           boolean header=true;
           
           while((line=reader.readLine())!=null){
               if(header==true){
                   header=false;
                   continue;
               }
  
               String []row=line.split(",");
               if(name.equals(row[0])){
                   findUser.add(line);
               }
           }
           
           
          int lastindex=findUser.size()-1;
          if(lastindex<=0){
              saving=0.0;
          }      
          else{
            String rows[]=findUser.get(lastindex).split(",");
            if(rows.length<4 && rows.length>0){ // debited
               int lastindexs=findUser.size()-2;
               String lines[]=findUser.get(lastindexs).split(",");
               saving=Double.parseDouble(lines[5]);               
             }
            else{
             saving=Double.parseDouble(rows[5]);
            }
          }
          
         
       }catch (IOException ex){
           ex.printStackTrace();
       }

    return saving;
   }
    
    public void getTotalSavingsPerMonth(){ // total savings of user (user visible)
       double saving=0.0;
       try (BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
           String line="";
           ArrayList<String> findUser=new ArrayList<>();
           ArrayList<String> findLastTotal=new ArrayList<>();
           boolean header=true;
           
           while((line=reader.readLine())!=null){
               if(header==true){
                   header=false;
                   continue;
               }
  
               String []row=line.split(",");
               if(username.equals(row[0])){
                   findUser.add(line);
               }
           }
           
           
          int lastindex=findUser.size()-1;
          if(lastindex<=0){
              saving=0.0;
          }      
          else{
            String rows[]=findUser.get(lastindex).split(",");
            if(rows.length<4 && rows.length>0){ // debited
               int lastindexs=findUser.size()-2;
               String lines[]=findUser.get(lastindexs).split(",");
               saving=Double.parseDouble(lines[7]);               
             }
            else{
             saving=Double.parseDouble(rows[7]);
            }
          }
          
          this.totalSavings=saving;
       }catch (IOException ex){
           ex.printStackTrace();
       }


   }
    
}