package insideout;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Reminder {
    ArrayList<String> overdue=new ArrayList<>();
    ArrayList<String> active=new ArrayList<>();
    private String username;
    final String loanFile = "src/creditloan-apply.csv";
    final String userFileName = "src/userinfo.csv";
    public String EMAIL_TO;
    private boolean activeloanfound = false;
    private int overdueloansize = 0;
    private int activeloansize = 0;
    static final String EMAIL_FROM = "lojingyang051104@gmail.com";
    static final String APP_PASSWORD = "hcaz vsjh ngsx eixs";

    Properties props = new Properties();


    public Reminder(String username) {
        this.username=username;
    }

    public void checkLoan() {
        try {
            Scanner reader = new Scanner(new FileReader(this.loanFile));
            reader.nextLine();
            while (reader.hasNextLine()){
             String line=reader.nextLine();
             SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));

             String row[]=line.split(",");
                // String current = "Sat Feb 08 18:41:16 GMT+08:00 2025"; // for testing
                //  Date date=dateFormat.parse(current); // for testing

                Date date = new Date();
                date = dateFormat.parse(String.valueOf(date));
             Date nextPaymentDate=dateFormat.parse(row[11]);
             Calendar calendar = Calendar.getInstance();
                calendar.setTime(nextPaymentDate);
             calendar.add(Calendar.DATE, -1);
                Date sendEmailDate = calendar.getTime();
                sendEmailDate = dateFormat.parse(String.valueOf(sendEmailDate));

             if(row[0].equals(this.username)){
             if(row[10].equalsIgnoreCase("Overdue")){
             overdue.add(row[1]); // add loanID (so odd index represent ID && even index represent RemainingPyament)
             overdue.add(row[5]); // add the remaining payment of laon (to be displayed
             }
             else if(row[10].equalsIgnoreCase("Active") && 
                     (date.before(nextPaymentDate) || date.equals(nextPaymentDate)) && // <=nextPaymentDate
                     (date.after(sendEmailDate) || date.equals(sendEmailDate))){
             active.add(row[1]); // add LoanID
             active.add(row[5]); // add remaining Payment
             active.add(row[11]); // add dueDate of loan
             }
             }

        }

            overdueloansize = overdue.size();
            activeloansize = active.size();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void reminderNotification() throws Exception{
            for(int i=0;i<active.size();i=i+3){
            Message message =new MimeMessage(getEmailSession(props));
            message.setFrom(new InternetAddress(EMAIL_FROM));
            getUserEmail(username);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.EMAIL_TO));
            message.setSubject("Remaining Payment");
            message.setText("LoanID : "+ active.get(i)+" RM "+active.get(i+1)+ " is required to pay before " + active.get(i+2)+
                    "\nAutoDeduction from balance will be conducted.Please ensure there is sufficient amount of balance.");
            Transport.send(message);
        
            }

    }
    
    public void overdueLoanNotification() throws Exception{
            for(int i=0;i<overdue.size();i=i+2){
            Message message =new MimeMessage(getEmailSession(props));
            message.setFrom(new InternetAddress(EMAIL_FROM));
            getUserEmail(username);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.EMAIL_TO));
            message.setSubject("OverDue Payment");
            message.setText("Overdue payment: " + "\n");
            message.setText("You are not allowed to use the function of credit and debit.");
            message.setText("LoanID : "+ overdue.get(i)+"RM" + overdue.get(i+1) + " required to pay ASAP.");
            Transport.send(message);

            }
    }

    private static Session getEmailSession(Properties props) {
        Properties prop = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
            }
        });
    }



    public void getUserEmail(String username){
        try{
            Scanner reader = new Scanner (new FileReader (userFileName));
            reader.nextLine();

            while(reader.hasNextLine()){
                String[] line = reader.nextLine().split(",");

                if (line[0].equals(username)){
                    this.EMAIL_TO = line[2];
                }
            }

            reader.close();
        }
        catch (Exception e){
            System.out.print(e.getMessage());
        }
    }

    public int getActiveLoanSize() {
        return activeloansize;
    }

    public int getOverdueLoansSize() {
        return overdueloansize;
    }

}