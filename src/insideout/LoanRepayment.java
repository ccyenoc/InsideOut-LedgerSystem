package insideout;
import static insideout.InsideOut.piechart;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.Label;

public class LoanRepayment {
    private static ArrayList<String> LoanID=new ArrayList<>();
    private static ArrayList<Double> LoanAmount=new ArrayList<>();
    private static double totalLoan=0.0;
    private static Label lbl=new Label();
    private static String targetUsername;
    
    public LoanRepayment(String targetUsername){
      this.targetUsername=targetUsername;
      System.out.println("TragetUsername for LoanRepayment :" +targetUsername);
    }
    
    public PieChart LoanRepaymentGraph() {
        LoanID.clear();
        LoanAmount.clear();
        totalLoan = 0.0;
    
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Loan Repayment Overtime");

        String filePath = "src/creditloan-repay - Sheet1.csv";
        String loanFile="src/creditloan-apply.csv";
        double totalRepaymentAmount = 0;
        double totalLoanPaid = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(loanFile));
             BufferedReader reader=new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            // read loan file to get the total loan per loan 
            // and also get the loan id
            while ((line = br.readLine()) != null) { // for applyLoan File
                if (isHeader) { // skip the header
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                String username = data[0];
                String loanID=data[1];
                double loanamount=Double.parseDouble(data[4]);
                if(username.equals(targetUsername)){ // if it is user
                  LoanID.add(loanID); // add the loanID
                  LoanAmount.add(loanamount); // add the loanAmount
                }
                }
            
      
          // calculate the total loan amount user had applied
          for(int i=0;i<LoanAmount.size();i++){
              totalLoan+=LoanAmount.get(i);    //  get the totalLoanAmount that should be paid
          }
          
          // calculate the total loan user had paid
          // read the 
          if (LoanID.size()==0) {
                lbl=new Label("No Data Found");
            } else {
              
          String str="";
          double loanpaid=0.0;
          while((str=reader.readLine())!=null){ // for repay loan file
            String content[]=str.split(",");
            String loanid=content[2];
            if(LoanID.contains(loanid)){
                loanpaid+=Double.parseDouble(content[4]);     
          }
          }
          
            
                double remainingPayment = totalLoan-loanpaid;
                double remainingPaymentpercentage = (remainingPayment / totalLoan)*100;
                double totalLoanPaidpercentage = (loanpaid / totalLoan)*100;
                pieChart.getData().add(new PieChart.Data("Amount Paid " + String.format("(%.2f", totalLoanPaidpercentage) + "%)", loanpaid));
                pieChart.getData().add(new PieChart.Data("Remaining Payment " + String.format("(%.2f", remainingPaymentpercentage) + "%)", remainingPayment));
            }

        
        }catch (IOException e) {
            e.printStackTrace();
        }

       piechart(pieChart);
       return pieChart;
    }
    
    public static Label getLabel(){
      return lbl;
    }
    
    public static void resetState() {
    LoanID.clear();
    LoanAmount.clear();
    totalLoan = 0.0;
    lbl = new Label();
}

}