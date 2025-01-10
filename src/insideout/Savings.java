package insideout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javafx.scene.control.Label;
import java.math.RoundingMode;

public class Savings {
    private String username="";
    private double percentage=0.0;
    private double debit = 0.0;  // get debit from transaction
    private double savings = 0.0; // update the balance at transaction at the end of month
    private double totalSavings=0.0; // for display purpose at view balance
    private double savingPerMonth=0.0;
    protected String savingFile = "src/savings.csv";
    private String transaction = "src/transactions.csv";
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
                if(header){
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
              updateSavingPercentage();
            }else{
                String[] check=addLine.get(lastIndex).split(",");
                if(check.length<4){
                    lbl = new Label("There are Pending Saving.\nPlease make debit before next saving.");
                    break pending;
                }
                else{
                    lbl = new Label(percentage + "% will be deducted on next debit as saving.");
                    updateSavingPercentage();
                }
        }
        }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    // condition need to be handled
    // 1. user added savings per month into balance

    public void updateSavingPercentage() {
      savingFileID();
      String line=username+","+transactionID+","+percentage;
       appendToFile(savingFile,line);
    }


    // update debit amount into savingCSV
    // user's monthly saving has been added into balance
    // (totalsaving=saving);
    public void updateDebitAmount(double percentage){

        savings = debit * (percentage / 100);
        totalSavings=getTotalSavings(username);
        totalSavings+=savings;

        String str="";
        boolean header=true;
        ArrayList<String> getLast=new ArrayList<>();
        ArrayList<String> findUserSavingPerMonth=new ArrayList<>();
        int lastUserIndex = -1;
         try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
          while((str=reader.readLine())!=null){
             if(header){
               header=false;
               getLast.add(str);
               continue;
             }

             String row[]=str.split(",");
             if(username.equals(row[0])){
                 getLast.add(str);
              findUserSavingPerMonth.add(str);
              lastUserIndex=getLast.size()-1; // index for getLast ArrayList(to set)
              continue;
           }
              getLast.add(str);
          }

          // condition need to be handled:
          // 1. user index=1 (savingpermonth=savings) (condition where first data in csv)
          // 2.user index=2(savingpermonth=+saving if "No") (savingpermonth=saving if "Yes");
          int userLastIndex=findUserSavingPerMonth.size()-1; // index to find the user savingpermonth(whether added to balance or not)
           if(lastUserIndex>1){  // condition where previously got data in csv(no matter the user)

               if(userLastIndex-1<0){ // user first saving
                 savingPerMonth=savings;
               }
               else{
                 String content[]=findUserSavingPerMonth.get(userLastIndex-1).split(","); // to find the last savingpermonth
                if(content[8].equalsIgnoreCase("No")){ // havent add to balance
                    savingPerMonth = Double.parseDouble(content[7]) + savings;
                }else{  // added to balance
                    savingPerMonth = savings;
                 }
                }
            } else {// condition where first data
               savingPerMonth = savings;
          }

             BigDecimal monthlySaving=new BigDecimal(savingPerMonth);
        BigDecimal totalSaving=new BigDecimal(totalSavings);
        BigDecimal saving=new BigDecimal(savings);
        monthlySaving=monthlySaving.setScale(2,RoundingMode.HALF_UP);
        totalSaving=totalSaving.setScale(2,RoundingMode.HALF_UP);
        saving=saving.setScale(2,RoundingMode.HALF_UP);

             Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        String oriLine[]=getLast.get(lastUserIndex).split(",");
        ArrayList<String> updated=new ArrayList<String>(Arrays.asList(oriLine));
        updated.add(String.valueOf(debit));
        updated.add(String.valueOf(saving));
        updated.add(String.valueOf(totalSaving));
        updated.add(String.valueOf(date));
        updated.add(String.valueOf(monthlySaving));
        updated.add("No");
        String fullSavingLine=String.join(",",updated);
        getLast.set(lastUserIndex,fullSavingLine);
        updateFile(getLast,savingFile);

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
               if(header){
                   header=false;
                   continue;
               }

                String []row=line.split(",");
               if(username.equals(row[0])){
                   findUser.add(line);
               }
           }

            int lastindex=findUser.size()-1;
          if(lastindex>=0){
          String rows[]=findUser.get(lastindex).split(",");
          this.percentage=Double.parseDouble(rows[2]);
          updateDebitAmount(percentage);
          }
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
        int ID=0;
        String row[]=str.get(lastIndex).split(",");
        if(row[1]==null){
            int index=str.size()-2;
            String content[]=str.get(index).split(",");
            ID=Integer.parseInt(content[1].replace("SV",""))+1;
        }else{
            ID=Integer.parseInt(row[1].replace("SV",""))+1;
        }
         transactionID="SV"+String.format("%06d",ID);
        }
        else{
           transactionID="SV"+String.format("%06d",1);
        }
    }catch (IOException e) {
       e.printStackTrace();
    }

            }


    public void isEndOfMonth(String username) {
        this.username=username;
        boolean lastday=false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        Calendar current = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
       /* current.set(Calendar.DAY_OF_MONTH, current.getActualMaximum(Calendar.DAY_OF_MONTH)); // testing
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);*/

        //last day of moth at 00.00.00
        Calendar endOfMonth = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        endOfMonth.set(Calendar.MINUTE, 0);
        endOfMonth.set(Calendar.SECOND, 0);
        endOfMonth.set(Calendar.MILLISECOND, 0);


        // next month first day
         Calendar nextMonth = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        nextMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)+1);
        nextMonth.set(Calendar.HOUR_OF_DAY, 0);
        nextMonth.set(Calendar.MINUTE, 0);
        nextMonth.set(Calendar.SECOND, 0);
        nextMonth.set(Calendar.MILLISECOND, 0);

        // last day of last month
        // use to handle the condition where user might not log in at the end of month
        Calendar endOfLastMonth = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        endOfLastMonth.set(Calendar.DAY_OF_MONTH, 1);
        endOfLastMonth.add(Calendar.DAY_OF_MONTH, -1);
        endOfLastMonth.set(Calendar.HOUR_OF_DAY, 0);
        endOfLastMonth.set(Calendar.MINUTE, 0);
        endOfLastMonth.set(Calendar.SECOND, 0);
        endOfLastMonth.set(Calendar.MILLISECOND, 0);

        ArrayList<String> findUser=new ArrayList<>();
        // Check if current is strictly after the due date
        // condition to handle :
        // user did not log in during end of month(auto debit to the acc next time he/she login)
       boolean addSaving=false;
        // check whether today is the last day of current month
        if ((current.after(endOfMonth) || current.equals(endOfMonth)) && current.before(nextMonth)){
            lastday=true;
            addSaving=true;
        }

        // check if user last transaction is not this month or not end of month
        // if not the last day of month check whether last transactin date is before last day of last month
        Calendar lastTransaction=Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        // if it is last day this part will skip
        // if it is not last day it will check for last transaction day to check whether savings for last month updated
        if(lastday==false){
            try (BufferedReader br = new BufferedReader(new FileReader(transaction))) {
           String ln="";
           while((ln=br.readLine())!=null){
               String rows[] = splitCSVLine(ln, 8);
             if(rows[0].equals(username)){
                 try{
                     Date lastTransactionDate = sdf.parse(rows[5]);
                     lastTransaction.setTime(lastTransactionDate);
                 lastTransaction.setTime(lastTransactionDate);
                 }catch(ParseException c){
                   c.printStackTrace();
                 }
             }
           }
         }catch(IOException ex){
           ex.printStackTrace();
         }

          if(lastTransaction.before(endOfLastMonth)){
            addSaving=true;
          }
        }

        // if last transaction date is before endoflastmonth
        // means that user does not login to the acc at the last day of month
        // since if user log in into acc at last day, it wil update saving
        // eg. lastTransaction 30 Jan 2025 , current 3 Feb 2025

        System.out.println("Current date: " + sdf.format(current.getTime()));
        System.out.println("End of month date: " + sdf.format(endOfMonth.getTime()));
        System.out.println("Last transaction date: " + sdf.format(lastTransaction.getTime()));

        if(addSaving==true){
        String line="";
        boolean header=true;
        boolean updated=false;
        boolean shouldUpdate=false;
        double lastSavings=Double.MAX_VALUE;
        try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
          while((line=reader.readLine())!=null){
              if(header){
                header=false;
                continue;
              }

              // this handle the condition which
              // user login to account more than one time at the end of month
              // if not handled, then the monthly saving will be added everytime user login
              String row[]=line.split(",");
              if(row[0].equals(username) && row.length==9){
                  if(Double.parseDouble(row[7])<lastSavings){
                       shouldUpdate=true; // true when current month accumulated savings is less than last accumulated savings
                      lastSavings=Double.parseDouble(row[7]); // compare the lastSaving with current savings, if less that means it should be updated
                   }


                  if (row[8].contains("|") && shouldUpdate == false) { // shouldUpdate is only for the last day of month which checks for whether savings updated on that day
                        updated=true;
                    }

                  findUser.add(line); // add user info into findUser arrayList once found
              }
          }

            // user found baru can update
          // also the length should be exactly 8
          // conditions to handle :
          // 1. no data in file
          // 2. user activate savings but havent debited
          // 3. user login twice perday (during end of month)

            if(findUser.size()!=0){ // if user is found in csv
          String today = sdf.format(new Date());
          int index=0;
                if (updated==false) { // if updated and it is not the same with current time
                     index = findUser.size() - 1;
                    if (findUser.get(index).split(",").length < 4) { // if user hasn't debited
                        index = findUser.size() - 2; // get the previous line
                    }

            if(index>=0){ // got data in csv file
            String content[]=findUser.get(index).split(","); // get the last index
            while (content.length <= 3) {
                   index--;
                  if (index < 0) {
                     throw new IllegalArgumentException("No valid data found in findUser.");
             }
            content = findUser.get(index).split(","); // Update content with the new index
        }

                this.totalSavings=Double.parseDouble(content[7]);
                System.out.println("Total savings " + totalSavings);
            endOfMonthUpdateCsv(totalSavings,today);
          }
          }
        }


        }catch(IOException ex){
         ex.printStackTrace();
        }
        }
        // if current time is before the last day of the month
        // it checks for whether last month added savings into balance or not

    }


    public void endOfMonthUpdateCsv(double totalSavings,String today) {
        getTotalSavingsPerMonth();
        double balance=0.0;
        String userLastInfo="";
        String readline="";

        boolean header=true;
        try (BufferedReader reader = new BufferedReader(new FileReader(transaction));
            BufferedReader br=new BufferedReader(new FileReader(savingFile))){
            while((readline=reader.readLine())!=null){
                if(header){
                    header=false;
                    continue;
                }

                String row[] = splitCSVLine(readline, 8);
                if (row[0].equals(username)) {
                    balance=Double.parseDouble(row[6]); // get user last balance
                }
            }

            balance+=totalSavings;
            BigDecimal bd = new BigDecimal(balance);

            userLastInfo=readLastTransactionID();
           String getLastTransactionDate[]=userLastInfo.split(",");

            StringBuilder line=new StringBuilder();
           Date date = new Date();
           line.append(username).append(",").append(transactionID).append(",").append("Savings").append(",").append(String.format("%.2f",totalSavings)).append(",")
                   .append("Savings").append(",").append(date).append(",").append(bd).append(",").append("Savings");

            appendToFile(transaction, String.valueOf(line));
           
           String str="";
           boolean head=true;
           int lastIndexSaving=-1;
           ArrayList<String> Saving=new ArrayList<>();
           while((str=br.readLine())!=null){
             if(head){
                 Saving.add(str);
                 head=false;
                 continue;     
             }
              String content[]=str.split(",");
                if(content[0].equals(username)){ // find username (!! might not be debited )
                    Saving.add(str);
                    lastIndexSaving=Saving.size()- 1;
                }
            
            }
  
           String content[]=Saving.get(lastIndexSaving).split(",");
           if(content.length<=3){
             lastIndexSaving--;
           }
           
           for(int i=0;i<Saving.size();i++){
             if(i==lastIndexSaving){
               String split[]=Saving.get(i).split(",");
               split[8]="Yes|" + today;
               String update=String.join(",",split);
               Saving.set(i,update);
             }
           
           }
           
            updateFile(Saving,savingFile);
            }catch(IOException ex){
            ex.printStackTrace();
        }
       

    }
    
    public String readLastTransactionID() {
    String line="";
    ArrayList<String> str=new ArrayList<>();
    String userLastInfo="";
        try (BufferedReader reader = new BufferedReader(new FileReader(transaction))) {
        boolean header = true;
        while ((line = reader.readLine()) != null) {
            if (header) {
                header = false;
                continue;
            }
            String row[]=line.split(",");
            if(row[0].equals(username)){
              userLastInfo=line;
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
    
    return userLastInfo;
}
 
    
    public double getTotalSavings(String name){ // total savings of user (user visible)
       double saving=0.0;
       try (BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
           String line="";
           ArrayList<String> findUser=new ArrayList<>();
           ArrayList<String> findLastTotal=new ArrayList<>();
           boolean header=true;
           
           while((line=reader.readLine())!=null){
               if(header){
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
               if(header){
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
    
    public void updateFile(ArrayList<String> list,String filepath){
     try(BufferedWriter writer=new BufferedWriter (new FileWriter(filepath))){
           for (int i = 0; i < list.size(); i++) {
                  writer.write(list.get(i));
 
                if (i<list.size()-1) {
                writer.newLine();  // Add newline only if it's not the last element
    }
}
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    // when status will be true?
    // length<=3 && username match
    public boolean updateDeductStatus(){
        boolean deductdebit=false;
        String line="";
        boolean header=true;
        int lastIndex=-1;
        ArrayList<String> addLine=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
            pending:{
            while((line=reader.readLine())!=null){
                if(header){
                    header=false;
                    continue;
                }
 
                String row[]=line.split(",");
                if(row[0].equals(username)){
                    addLine.add(line);
                    lastIndex=addLine.size()-1;
                } 
            }

            if(lastIndex<0){ // meaning that no username matches 
              deductdebit = false; 
            }else{ // username match
                String[] check=addLine.get(lastIndex).split(","); // get the content of user last line
                if(check.length<4){
                    deductdebit = true; 
                    break pending;
                }
                else{
                    deductdebit = false;      
                }         
        }
        }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
        return deductdebit;
    }
    
    public void cancelSaving(){
        String line="";
        boolean header=true;
        int userLastIndex=0;
        ArrayList<String> update=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(savingFile))){
         while((line=reader.readLine())!=null){
           if(header){
             update.add(line);
             header=false;
             continue;
           }
           
           String row[]=line.split(",");
           if(row[0].equals(username)){
             update.add(line);
             userLastIndex=update.size()-1;
             continue;
           }
           
           update.add(line);
           
         }
           String checkPendingSaving[]=update.get(userLastIndex).split(",");
           if(checkPendingSaving.length<4){
             update.remove(userLastIndex);
             updateFile(update,savingFile);
           }
           

        }catch(IOException ex){
          ex.printStackTrace();
        }
    }
    
    public static void appendToFile(String filePath, String data) {
        boolean headerExists = false;
        String firstLine ="";
        // Read the file to check if header exists
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            firstLine=reader.readLine();
            if (firstLine != null) {
                headerExists = true; // Header found
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now append data, if the header exists, skip writing it again
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (headerExists) {
                writer.newLine(); 
            }

            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
      public double getSavings(){
       return savings;
   }
   
    public Label getLabel(){
        return lbl;
    }

    private static String[] splitCSVLine(String line, int z) {
        String[] result = new String[z];
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        int index = 0;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
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