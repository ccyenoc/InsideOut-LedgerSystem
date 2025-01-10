package insideout;

import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PredictedDeposit {
   
    private String bank="";
    private String bankFile="src/bank.csv";
    private String getBalanceFile = "src/transactions.csv";
    private String username="";
    private ArrayList<String> lines=new ArrayList<>();
    private ArrayList<String> addLine=new ArrayList<>();
    private double balance=0.0;
    private double deposit=0.0;
    private double monthlyDeposit=0.0;
    private Label lbl = new Label();
    
    public PredictedDeposit() {  
    }
   
   public void getBalance(){ // targer is to get user last balance
       String line="";
       double balance=0.0;
       boolean header=true;
       ArrayList<String> findUser=new ArrayList<>();
     try(BufferedReader reader=new BufferedReader(new FileReader(getBalanceFile))){
        while((line=reader.readLine())!=null){
          if(header){
              header=false;
             continue;
          }
          
          String row[]=line.split(",");
          if(row[0].equals(username)){
              findUser.add(line); // add into findUser to seek for the last balance   
          }
        }

         if (findUser.size() != 0) {
             int lastIndex = findUser.size() - 1;
             String findBalance[] = findUser.get(lastIndex).split(",");
             balance = Double.parseDouble(findBalance[6]);
             this.balance = balance;
         } else {
             lbl = new Label("No balance yet!");
         }

     }catch (IOException ex){
       ex.printStackTrace();
     }

   }
   
    public void calculateDeposit(){
       Date date = new Date();
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
       double predictedDeposit=0.0;
       String str;
       try(BufferedReader reader=new BufferedReader(new FileReader(bankFile))){
          while((str=reader.readLine())!=null){
            String findBank[]=str.split(",");
            if(this.bank.equalsIgnoreCase(findBank[0])){
              this.deposit=Double.parseDouble(findBank[1])*this.balance;
              break;
            }
          }  
           
       }catch(IOException ex){
         ex.printStackTrace();
       }
      
       calculateMonthlyDeposit();
   }
    
    public void calculateMonthlyDeposit(){
       monthlyDeposit=this.deposit/12;
    }
    
    public double getMonthlyDeposit(){
      return monthlyDeposit;
    }
    
    public double getDeposit(){
        return deposit;
    }
    
    public void setBank(String bank){
     this.bank=bank;
    }
    
    public void setName(String username){
      this.username=username;
    }

    public Label getlbl() {
        return lbl;
    }
}

    