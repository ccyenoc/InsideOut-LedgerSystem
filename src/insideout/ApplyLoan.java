package insideout;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static insideout.InsideOut.store;
import javafx.scene.control.Label;

public class ApplyLoan{
    private String username="";
    private double principal=0.0;
    private double annualInterest=0.0;
    private double totalRepaymentAmount=0.0;
    private double monthlyRepayment=0.0;
    private int paymentFrequency=0;
    private int month=0;
    private String dueDate="";
    private String currentDate="";
    private String nextPaymentDate="";
    private Date date;
    private String LoanID="";
    private Label lbl;
    
    private String applyFile="src/creditloan-apply.csv";
    public ApplyLoan(){
    }
   
   public void setUsername(String username){
     this.username=username;
   }
   
   public void setPrincipal(String principal){
     this.principal=Double.parseDouble(principal);
   }
   
   public void setAnnualInterest(String annualInterest){
   this.annualInterest=Double.parseDouble(annualInterest);
   }
   
   public void setTime(String month){
    this.month=Integer.parseInt(month);
       double year = Double.parseDouble(month) / 12;
   }
   
   public void setPaymentFrequency(String frequency){
     if(frequency.equals("Monthly")){
       this.paymentFrequency=1;
     }
     else if(frequency.equals("Quarterly")){
       this.paymentFrequency=3;
     }
      else if(frequency.equals("Semi-Annually")){
       this.paymentFrequency=6;
     }
      else{
       this.paymentFrequency=12;
      }
   }
   
   public boolean validateInputs(){
       boolean valid=true;
                if (principal < 1000) {
                    lbl=new Label("Principal must be greater or equal to 1000");
                    valid=false;
                } else if (annualInterest <= 0 || annualInterest >= 100) {
                    lbl=new Label("Annual interest rate must be \ngreater than 0 and smaller than 100");
                    valid=false;
                } else if (month> 480 || month<= 0) {
                    lbl=new Label("Repayment period must not exceed 40 years \nor less than or equals to zero");
                    valid=false;
                }
                else if (month%paymentFrequency != 0) {
                    lbl=new Label("Repayment period must be divisible\nby the payment frequency period");
                    valid=false;
                }
       return valid;
   }
   
   public void CurrentDate(){
       SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
       formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur")); // Set time zone to MYT
       date = new Date();
       String formattedDate = formatter.format(date);
       this.currentDate = formattedDate;
   }
   
   public void calculateDueDate(){
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.add(Calendar.MONTH,month);
     Date due= calendar.getTime();
     dueDate=String.valueOf(due);
   }
   
   public void calculateNextPaymentDate(){
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.add(Calendar.MONTH, 1);
     Date nextPayment= calendar.getTime();
     nextPaymentDate=String.valueOf(nextPayment);
   }

   // note that in this class Total Repayment Amount is always be Blance as 
   //they are applying for the first time, deduction will be carried at Repayment Class
   public void calculateTotalRepayment (){
            double periodicInterestRate = (annualInterest / 100) / (12 / paymentFrequency);
            int numberOfPayments = month/ paymentFrequency;
            double periodicPayment = Double.parseDouble(String.format("%.2f",
                    (principal * periodicInterestRate * Math.pow(1 + periodicInterestRate, numberOfPayments)) /
                            (Math.pow(1 + periodicInterestRate, numberOfPayments) - 1)));
            totalRepaymentAmount=periodicPayment * numberOfPayments;
            calculatePeriodicPayment();
    }
   
    public void calculatePeriodicPayment(){
            double periodicInterestRate = (annualInterest/ 100) / (12 / paymentFrequency);
            int numberOfPayments = month/ paymentFrequency;
            monthlyRepayment=Double.parseDouble(String.format("%.2f",
                    (principal * periodicInterestRate * Math.pow(1 + periodicInterestRate, numberOfPayments)) /
                            (Math.pow(1 + periodicInterestRate, numberOfPayments) - 1)));
        }
   
   public void updateCSV(){
      LoanID();
      StringBuilder str=new StringBuilder();
      str.append(username).append(",").append(LoanID).append(",").append(principal).append(",").
      append(annualInterest).append(",").append(String.format("%.2f,",totalRepaymentAmount)).append(String.format("%.2f,",totalRepaymentAmount)).
      append(month).append(",").append(paymentFrequency).append(",").append(currentDate).append(",").append(dueDate).append(",").append("Active").
      append(",").append(nextPaymentDate).append(",").append(monthlyRepayment); // status will change once outstanding balance is 0 (controlled by repayment class too)
      store(applyFile,String.valueOf(str));
      lbl=new Label("Loan Has Been Applied\nNote:"+String.format("%.2f",totalRepaymentAmount)+" should be paid before\n"+dueDate);
   }
   
   public Label getLabel(){
     return lbl;
   }
   
   public void LoanID(){
    String line;
    ArrayList<String> str=new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(applyFile))) {
        boolean header = true;
        while ((line = reader.readLine()) != null) {
            if (header) {
                header = false;
                continue;
            }
            
            str.add(line);
        }
        
        if(!str.isEmpty()){
        int lastIndex=str.size()-1;
        String row[]=str.get(lastIndex).split(",");
        int lastID=Integer.parseInt(row[1].replace("AL",""));
        LoanID="AL"+String.format("%06d",(lastID+1));
        } 
        else{
           LoanID="AL"+String.format("%06d",1); 
        }
    }catch (IOException e) {
       e.printStackTrace();
    }

            }
   
   public String getTotalOustandingBalance(String username){
     String line;
     boolean header=true;
     double totaloutstandingbalance=0.0;
     ArrayList<String> findUser=new ArrayList<>();
     try(BufferedReader reader=new BufferedReader(new FileReader(applyFile))){
       while((line=reader.readLine())!=null){
           if(header){
               header=false;
               continue;
           }    
     
           String row[]=line.split(",");
           if(row[0].equals(username)){
               totaloutstandingbalance+=Double.parseDouble(row[5]);
           }
       }
       
     
     
     }catch(IOException ex){
       ex.printStackTrace();
     }

   return String.valueOf(totaloutstandingbalance);
   }
   
   
   

}