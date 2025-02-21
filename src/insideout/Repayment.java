package insideout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import javafx.scene.control.Label;

import static insideout.InsideOut.store;
// updatestatus first
// then checkDecution

public class Repayment {
    private String balanceFile = "src/transactions.csv";
    private String applyFile="src/creditloan-apply.csv";
    private String repaymentFile = "src/creditloan-repay.csv";
    private ArrayList<Integer> userIndex=new ArrayList<>();
    private ArrayList<String> findUser=new ArrayList<>();
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
    private int paymentFrequency = 0;
    
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
            //Date date = new Date();
            String dateString = "Thu May 15 23:58:45 GMT+08:00 2025";
            Date date = dateFormat.parse(dateString);
            Date monthlypaymentDate = dateFormat.parse(monthlyDeductionDate);

            int compare = date.compareTo(monthlypaymentDate);

            if (name.equals(username) && ((compare >= 0 && (status.equals("Active"))) || status.equals("Overdue"))) {
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
        this.LoanID = row[1];
        this.paymentFrequency = Integer.parseInt(row[7]);
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
            BigDecimal outstanding = BigDecimal.valueOf(newOutstandingBalance).setScale(2, RoundingMode.HALF_UP);
            row[5] = outstanding.toString();
            if (Math.abs(newOutstandingBalance) < 0.01) {
                row[5] = "0.00";
                row[10] = "Paid";
            }
            String updatedLine = String.join(",", row);
            fileContent.set(userIndex.get(i), updatedLine);
        } else {
            try {
                nextMonthPaymentDate = calculateNextPaymentDate(dateFormat.parse(row[11]));
                row[11] = nextMonthPaymentDate;
                String updatedLine = String.join(",", row);
                fileContent.set(userIndex.get(i), updatedLine);
                lbl = new Label("Balance Insufficient\nto carry out Auto Repayment!");
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
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
             appendFile(repaymentFile, updateRepayment);
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

    public void appendFile(String filepath, String line) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String lastLine = null;
            String currentLine;

            // Read through the file to get the last line
            while ((currentLine = reader.readLine()) != null) {
                lastLine = currentLine;  // Keep updating the lastLine until the end of the file
            }

            // Now append the line
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
                // Check if we need to add a newline before appending
                if (lastLine != null && !lastLine.isEmpty()) {
                    writer.write(System.lineSeparator()); // Add a newline if the file is not empty
                }
                writer.write(line);  // Write the new line to the file
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Label getLabel(){
      return lbl;
    }
    
    public void setUsername(){
        this.username = username;
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

                  }
                }

                BufferedWriter updater = null;
                try {
                    updater = new BufferedWriter(new FileWriter(applyFile));
                    for (String content : lines) {
                        updater.write(content);
                        updater.newLine();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (updater != null) {
                        try {
                            updater.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
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


    public void updateOutstandingBalance(ArrayList<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(applyFile))) {
            for (String content : lines) {
                writer.write(content);
                writer.newLine();
            }
        } catch (IOException ex) {
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
                  overdueLoan.add(row[1]);
                  continue;
              }
            }

        }catch(IOException ex){
          ex.printStackTrace();
        }

        return overdueLoan;

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
             double outstanding=Double.parseDouble(row[5])-repayment;
             if(Math.abs(outstanding)< 0.01){
                 row[10]="Paid";
                 row[5] = "0.00";
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
        store(repaymentFile, String.valueOf(builder));
    
    }
    
    public String calculateNextPaymentDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
        calendar.add(Calendar.MONTH, this.paymentFrequency); // Add one month
    Date nextPayment = calendar.getTime();
    String nextPaymentDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(nextPayment);
        return String.valueOf(nextPaymentDate);
    }

}
