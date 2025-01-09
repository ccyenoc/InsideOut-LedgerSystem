package insideout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static insideout.InsideOut.store;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Calendar;
import javafx.scene.control.Label;
// updatestatus first
// then checkDecution


public class Repayment {
    private String balanceFile="src/recorddebitandcredit.csv";
    private String applyFile="src/creditloan-apply.csv";
    private String repaymentFile="src/creditloan-repay - Sheet1.csv";
    private ArrayList<Integer> userIndex=new ArrayList<>();
    private ArrayList<String> findUser=new ArrayList<>();
    private ArrayList<String> info=new ArrayList<>();
    private ArrayList<String> activeLoan=new ArrayList<>();
    private ArrayList<String> overdueLoan=new ArrayList<>();
    private int index=0;
    private String RepaymentID="";
    private String LoanID="";
    private String username="";
    private String status="";
    private Label lbl;
    private String transactionID="";
    private double balance=-1.0;
    private double outstandingBalance=0.0;
    private double totalLoan=0.0;
    private ArrayList<String> rewriteApply=new ArrayList<>();
    
    private boolean overdue=false;
    
    // default constructor
    public Repayment(String username){
        this.username=username;
    }
    
    public void checkDeduction(){
    String line = "";
    boolean header = true;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    ArrayList<String> fileContent = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(applyFile))) {
        while ((line = reader.readLine()) != null) {
            if (header) {
                header = false;
                fileContent.add(line); 
                continue;
            }

            fileContent.add(line); 
            String row[] = line.split(",");
            String name = row[0];
            String status = row[10];
            String monthlyDeductionDate = row[11];

            Date date = new Date();
            Date monthlypaymentDate = dateFormat.parse(monthlyDeductionDate);

            int compare = date.compareTo(monthlypaymentDate);

            if (name.equals(username) && (compare >= 0 && (status.equals("Active") || status.equals("Overdue")))) {
                userIndex.add(fileContent.size() - 1); 
                findUser.add(line);
            }
        }


        if (!findUser.isEmpty()) {
            MonthlyDeduction(findUser, fileContent);
        }
    } catch (IOException | ParseException ex) {
        ex.printStackTrace();
    }
}

public void MonthlyDeduction(ArrayList<String> list, ArrayList<String> fileContent) {
    for (int i = 0; i < list.size(); i++) {
        String row[] = list.get(i).split(",");
        double monthlyPayment = Double.parseDouble(row[12]);
        double outstandingBalance = Double.parseDouble(row[5]);
        if(outstandingBalance<monthlyPayment){
          monthlyPayment=outstandingBalance; // condition where the outstanding Balance is less then monthly payment 
        } 
        double balance = deductBalance(monthlyPayment);
        this.LoanID=row[1];
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String nextMonthPaymentDate="";
        try{
           nextMonthPaymentDate=calculateNextPaymentDate(dateFormat.parse(row[11]));
        }catch(ParseException ex){
            ex.printStackTrace();
        }
        row[11]=nextMonthPaymentDate;
        if (balance>=0) { 
            double newOutstandingBalance = outstandingBalance-monthlyPayment;
            row[5] = String.valueOf(newOutstandingBalance);
            if (Math.abs(newOutstandingBalance) < 0.01) {
                row[10] = "Paid";
            }
            String updatedLine = String.join(",", row);
            fileContent.set(userIndex.get(i), updatedLine);
        }
    }

    // Write the updated file content back to the CSV
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(applyFile))) {
        for (String content : fileContent) {
            writer.write(content);
            writer.newLine();
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

   
    // only deduct the balance in recorddebitandcredit file
    // and also add repayment history to repaymentFile
    public double deductBalance(double repayment){ //deduct balance   
      SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
      Date date=new Date(); // get current time
      String line="";
      boolean header=true;
      String lastBalance[]={""};
      try(BufferedReader reader=new BufferedReader(new FileReader(balanceFile))){ // get user final balance;
         while((line=reader.readLine())!=null){
           if(header){
               header=false;
               continue;
           }
           
           String row[]=line.split(",");
           if(row[0].equals(username)){
              lastBalance[0]=line;
           }
           
         }
         
         String Balance[]=lastBalance[0].split(",");
         balance=Double.parseDouble(Balance[6]);
         balance=balance-repayment; // deduct with repayment amount
         if(balance>=0){
           readLastTransactionID();
           // update the new balance to recorddebitandcredit file
           StringBuilder updateNewBalance = new StringBuilder().append(username).append(",").append(transactionID).
                   append(",").append("Repayment").append(",").append(repayment).append(",").append("Loan ID: ").append(LoanID).
                   append(",").append(date).append(",").append(String.format("%.2f",(balance))).append(",").append("Monthly Repayment");
           appendFile(balanceFile,String.valueOf(updateNewBalance));
           calculateOutstandingBalance(repayment);
           // update the repayment csv
           RepaymentID();
           getTotalLoan(LoanID);
           String updateRepayment=username+","+RepaymentID+","+LoanID+","+totalLoan+","+repayment+","+(getTotalPaid(LoanID)+repayment)+","+date;
           appendFile(repaymentFile,updateRepayment.toString());
         }
         else{
         balance=-1.0;
         lbl=new Label("Insufficient Balance!");
         }
      
      }catch(IOException ex){
        ex.printStackTrace();
      }
      return balance;
    }
    
    public void appendFile(String filepath,String line) {
   
    ArrayList<String> str=new ArrayList<>();
    try(BufferedWriter writer=new BufferedWriter(new FileWriter(filepath,true))){
         writer.newLine();
         writer.write(line);
    
    }catch (IOException e) {
       e.printStackTrace();
    }
    }
    
    public Label getLabel(){
      return lbl;
    }
    
    public void setUsername(){
     this.username=username;
    }
    
    
    // update status first when login
    public void updateStatus () {
          SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
          Date date=new Date();
          Date dueDate=new Date();
             
            try (BufferedReader reader=new BufferedReader(new FileReader(applyFile))){
                String line;
                boolean header=true;
                int index=1;
                ArrayList<String> lines = new ArrayList<>();
                ArrayList<Integer> indexUser=new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    if(header){
                     header=false;
                     lines.add(line);
                     continue;
                    }
                    
                    String row[]=line.split(",");
                    if(row[0].equals(username)){
                    indexUser.add(index);} // only add user index in the whole file
                    
                    lines.add(line);
                    index++;
                }
                
                reader.close();

                for(int i=0;i<lines.size();i++){
                    for(int j=0;j<indexUser.size();j++){
                        if(indexUser.get(j)==i){
                      String[] data = lines.get(i).split(",");
                      try{
                          String dateString = data[9].replace("Asia/Kuala_Lumpur", "MYT");
                          dueDate = dateFormat.parse(dateString);

                      } catch (ParseException e) {
                           e.printStackTrace();}

                      if (!data[10].equalsIgnoreCase("paid")) { // loan active
                        if (Math.abs(Double.parseDouble(data[6].trim())) < 0.01) {
                            data[10] = "Paid";
                        } else if(dueDate.compareTo(date)<0 || dueDate.compareTo(date)==0) {
                            data[10] = "Overdue";
                        }
                        lines.set(i, String.join(",", data));
                    }
                    }
                        else{
                           continue; 
                        }
                  }
                }

                try(BufferedWriter updater = new BufferedWriter(new FileWriter(applyFile))){
                for (int i=0;i<lines.size();i++) {
                    if(i==lines.size()-1){
                       updater.write(lines.get(i));
                     }
                    else{
                        updater.write(lines.get(i));
                        updater.newLine();
                    }
                     
                }
                updater.close();
                }catch(IOException ex){
                  ex.printStackTrace();
                }
            }catch (Exception e) {
                System.err.println("Error updating the status in LoanCSV");
                e.printStackTrace();
            }
        }
    
    public void RepaymentID(){
    String line="";
    ArrayList<String> str=new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(repaymentFile))) {
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
        int lastID=Integer.parseInt(row[1].replace("RP",""));
        RepaymentID="RP"+String.format("%06d",(lastID+1));
        } 
        else{
           RepaymentID="RP"+String.format("%06d",1); 
        }
    }catch (IOException e) {
       e.printStackTrace();
    }

            }
    
    public double calculateOutstandingBalance(double repayment){
         double remaining=outstandingBalance-repayment;
         return remaining;
      }
    
    
    
   public void updateOutstandingBalance(ArrayList<String> lines){
     String line="";
     try(BufferedWriter writer=new BufferedWriter(new FileWriter(applyFile))){
         for(String content:lines){
          writer.write(content);
          writer.newLine();
         }
                 
     }catch(IOException ex){
      ex.printStackTrace();
     }
    }
   
   public void readLastTransactionID() {
    String line="";
    ArrayList<String> str=new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(balanceFile))) {
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
    
    public double getTotalPaid(String loanID){
      String line="";
      boolean header=true;
      double lastTotal=0.0;
      ArrayList<String> findTotal=new ArrayList<>();
      try(BufferedReader reader=new BufferedReader(new FileReader(repaymentFile))){
        while((line=reader.readLine())!=null){
           if(header){
             header=false;
             continue;
           }
           
           
           String[] row=line.split(",");
           if(row[1].equals(loanID)){
             findTotal.add(row[5]);
           }
           
           if(findTotal.isEmpty()){
             lastTotal=0.0;
           }
           else{
             int index=findTotal.size()-1;
             lastTotal=Double.parseDouble(findTotal.get(index));
           }
        
        }
      }catch(IOException ex){
        ex.printStackTrace();
      }
      return lastTotal;
    }
    
    public void getTotalLoan(String loanID){
        String line="";
        boolean header=true;
       try(BufferedReader reader=new BufferedReader(new FileReader(applyFile))){
           while((line=reader.readLine())!=null){
             if(header){
                 header=false;
                 continue;
             }
             
             String []row=line.split(",");
             if(row[1].equals(loanID)){
              totalLoan=Double.parseDouble(row[4]);
             }
             
           }
       }catch(IOException ex){
         ex.printStackTrace();
       }
    }
    
    public void checkOverdue(){
    String line="";
    boolean header=true;
    try(BufferedReader reader=new BufferedReader(new FileReader(applyFile))){
        while((line=reader.readLine())!=null){
          if(header){
            header=false;
            continue;
          }
          
          String row[]=line.split(",");
          if(row[0].equals(username) && row[10].equalsIgnoreCase("Overdue") ){
            overdue=true;
            break;
          }
        
        }
    
    }catch(IOException ex){
       ex.printStackTrace();
    }
    }
    
    public boolean getOverdue(){
     return overdue;
    }
    
    public ArrayList<String> getActiveLoanIDList(){
        String line="";
        boolean header=true;
        try(BufferedReader reader=new BufferedReader(new FileReader(applyFile))){
            while((line=reader.readLine())!=null){
              if(header){
                  header=false;
                  continue;
              }
              
              String row[]=line.split(",");
              if(row[0].equals(username) && row[10].equalsIgnoreCase("Active")){
                  activeLoan.add(row[1]);
                  continue;
              }
            }

        }catch(IOException ex){
          ex.printStackTrace();
        }
     
        return activeLoan;

   }
    
    public ArrayList<String> getOverdueLoanIDList(){
        String line="";
        boolean header=true;
        try(BufferedReader reader=new BufferedReader(new FileReader(applyFile))){
            while((line=reader.readLine())!=null){
              if(header){
                  header=false;
                  continue;
              }
              
              String row[]=line.split(",");
              if(row[0].equals(username) && row[10].equalsIgnoreCase("Overdue")){
                  activeLoan.add(row[1]);
                  continue;
              }
            }

        }catch(IOException ex){
          ex.printStackTrace();
        }
     
        return activeLoan;

   }
    
    public void deductLoan(String loanID,double repayment){
     ArrayList<String> lines=new ArrayList<>();
     String line="";
     boolean header=true;
     String totalLoan="";
        String date = "";
     try(BufferedReader reader =new BufferedReader(new FileReader(applyFile))){
         quit:{
       while((line=reader.readLine())!=null){
         if(header){
             header=false;
             if (lines.isEmpty()) {
                        lines.add(line);
                    }
                    continue; 
            
         }
         
         String row[]=line.split(",");
         StringBuilder builder=new StringBuilder();

           if(row[0].equals(username) && row[1].equals(loanID)){
             System.out.println(row[5]);
             double outstanding=Double.parseDouble(row[5])-repayment;
             System.out.println("Outstanding " + outstanding);
             System.out.println(Math.abs(outstanding));
             if(Math.abs(outstanding)< 0.01){
                 row[10]="Paid";
             }
             else if(outstanding<0){
               lbl=new Label("Amount is greater than outstanding balance");
               break quit;
             }
             
             if(row[10].equals("Paid")){
              lbl=new Label("Loan Repayment Cleared.");
             }
             
             row[5]=String.format("%.2f",outstanding);
             totalLoan=row[4];
               if (row[10].equalsIgnoreCase("Paid")) {
                   date = row[11];
                   date = calculateNextPaymentDate(date);
               }
             for(int i=0;i<row.length;i++){
                 if(i==row.length-1){
                   builder.append(row[i]);
                 }
                 else{
                  builder.append(row[i]).append(",");
                 }
             }
             lines.add(String.valueOf(builder));
             continue;
         }
          lines.add(line);
       }
       updateOutstandingBalance(lines);
       updateRepaymentFile(loanID,repayment,totalLoan);
         }
     }catch(IOException ex){
         ex.printStackTrace();
    }
}
    
    public void updateRepaymentFile(String loanID,double repaymentamount,String totalrepaymentamount){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date=new Date(); 
        StringBuilder builder=new StringBuilder();
        RepaymentID();
        String line="";
        double totalLoanPaid=-1.0;
        boolean header=true;
        try(BufferedReader reader=new BufferedReader(new FileReader(repaymentFile))){
        while((line=reader.readLine())!=null){
           if(header){
           header=false;
           continue;
           }
           
           String row[]=line.split(",");
           if(row[0].equals(username) && row[2].equals(loanID) ){
               totalLoanPaid=Double.parseDouble(row[4]);
               continue;
           }
        }
        if(totalLoanPaid<0){
         totalLoanPaid=0.0;
        }
         totalLoanPaid+=repaymentamount;
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
        
        builder.append(username).append(",").append(RepaymentID).append(",").append(loanID).append(",").append(totalrepaymentamount)
                .append(",").append(String.format("%.2f",repaymentamount)).append(",").append(String.format("%.2f",totalLoanPaid)).append(",").append(date);
       appendFile(repaymentFile,String.valueOf(builder));
    
    }
    
    public String calculateNextPaymentDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MONTH, 1); // Add one month
    Date nextPayment = calendar.getTime();
    String nextPaymentDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(nextPayment);
    return String.valueOf(nextPayment);
    }

    public String calculateNextPaymentDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String nextPaymentDate = "";
        try {
            Date parsedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            calendar.add(Calendar.MONTH, 1);
            Date nextPayment = calendar.getTime();
            nextPaymentDate = String.valueOf(nextPayment);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return nextPaymentDate;
    }



}
