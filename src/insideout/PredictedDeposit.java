package insideout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PredictedDeposit {
   
    private String bank="";
    private String predictDepositFile="src/predcitedDeposit.csv";
    private String getBalanceFile="src/recorddebitandcredit.csv";
    private String username="";
    private ArrayList<String> lines=new ArrayList<>();
    private ArrayList<String> addLine=new ArrayList<>();
    private double balance=0.0;
    private double deposit=0.0;
    private double monthlyDeposit=0.0;
    
   public PredictedDeposit() {
      
    }
   
   public double addBalance(){ // targer is to get user last balance
       String line="";
       double addBalance=0.0;
       boolean header=true;
       ArrayList<String> findUser=new ArrayList<>();
       ArrayList<String> editFile=new ArrayList<>();
     try(BufferedReader reader=new BufferedReader(new FileReader(getBalanceFile))){
        while((line=reader.readLine())!=null){
          if(header ==true){
              header=false;
             continue;
          }
          
          String row[]=line.split(",");
          if(row[0].equals(username)){
              findUser.add(line); // add into findUser to seek for the last balance   
          }
        }
        
        int lastIndex=findUser.size()-1;
        String findBalance[]=findUser.get(lastIndex).split(",");
        addBalance=Double.parseDouble(findBalance[6]);
        this.balance=addBalance;
        String info=username+","+addBalance;
        updateBalance(info); 
        
        
     }catch (IOException ex){
       ex.printStackTrace();
     }
     return addBalance;
   }
   
   public void updateBalance(String info){ // to update the csv file with the balance(overwriter)
       String line="";
       boolean header=true;
       ArrayList<String> editFile=new ArrayList<>();
      try(BufferedReader reader=new BufferedReader(new FileReader(predictDepositFile))){
          while((line=reader.readLine())!=null){
            if(header==true){
               header=false;
               editFile.add(line);
            }
            
            editFile.add(line);
          }

             editFile.add(info);
            overwrite(editFile);
            
          }catch(IOException ex){
               ex.printStackTrace();
      }
   
          
      }

    
    public void updateBank(){ // update Bank
        String line;
       double addBalance=0.0;
        int userindex=0;
            boolean header = true;
            try(BufferedReader reader=new BufferedReader(new FileReader(predictDepositFile))){
            while ((line = reader.readLine()) != null) {
                if(header){
                    lines.add(line);
                    header=false;
                    continue;
                }               
               
               String[] column = line.split(","); 
               if(column[0].equals(username) && column.length==2){ // if it is user and do not have a bank yet
                     line=line+","+bank;
                     lines.add(line);
               }else{
                  lines.add(line); // if not username or the user do not have a pending calculating predicted deposit
               }
       
            }
            reader.close();
            overwrite(lines);
            }
        catch (IOException e) {
            // Handle exception if the file is not found or there's an issue reading it
            System.err.println("Error reading the file: " + e.getMessage());
        }
       
   
    }
    
    
    public void calculateDeposit(){
       Date date = new Date();
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
       double predictedDeposit=0.0;
       ArrayList<String> updatedeposit=new ArrayList<>();
       switch(bank){
                        case "RHB": predictedDeposit=this.balance*0.026;break;
                        case "MayBank": predictedDeposit=this.balance*0.025;break;
                        case "HongLeong": predictedDeposit=this.balance*0.023;break;
                        case "Alliance": predictedDeposit=this.balance*0.0285;break;
                        case "Ambank": predictedDeposit=this.balance*0.0255;break;
                        default: predictedDeposit=this.balance*0.0265;break;
                    
                    }
       deposit=predictedDeposit;
       calculateMonthlyDeposit(predictedDeposit);
       String line="";
       boolean header=true;
       try(BufferedReader reader=new BufferedReader(new FileReader(predictDepositFile))){
            while ((line = reader.readLine()) != null) {
                if(header){
                    updatedeposit.add(line);
                    header=false;
                    continue;
                }               
               
               String[] column = line.split(","); 
               if(column[0].equals(username)&& column.length<4){ // if it is user and do not have a bank yet
                     line=line+","+predictedDeposit+","+date;
                     updatedeposit.add(line);
               }else{
                    updatedeposit.add(line); // if not username or the user do not have a pending calculating predicted deposit
               }
       
            }
            reader.close();
            overwrite(updatedeposit);
            }
        catch (IOException e) {
            // Handle exception if the file is not found or there's an issue reading it
            System.err.println("Error reading the file: " + e.getMessage());
        }
       
   }
    
    public void calculateMonthlyDeposit(double yearly){
       monthlyDeposit=yearly/12;
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
    
    
 
    public void overwrite(ArrayList<String> lines){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(predictDepositFile))) {
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }       
       writer.close();
    } catch (IOException e) {
        System.err.println("Error writing to the file: " + e.getMessage());
    }
    }
    
   

    
}

    