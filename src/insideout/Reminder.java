package insideout;

import javax.swing.text.Style;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Properties;
import javafx.scene.control.Label;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Reminder {
    String[] overdue;
    String[] userID;
    String[] repaymentPeriod;
    String[] remainingPayment;
    final String filename = "src/creditloan-apply.csv";
    private static final String EMAIL_FROM = "woiaijdwj@gmail.com";
    private static final String APP_PASSWORD = "cwwl zggf qwwf bcdm";
    private static Label lbl;

    public Reminder(){
        try {
            Scanner reader = new Scanner(new FileReader(this.filename));
            reader.nextLine();
            int indexCount = 0 ;
            while (reader.hasNextLine()){
                indexCount++;
                String line = reader.nextLine();
            }
            System.out.println(indexCount);
            this.overdue = new String[indexCount];
            this.userID = new String[indexCount];
            this.repaymentPeriod = new String[indexCount];
            this.remainingPayment = new String[indexCount];
            reader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            Scanner reader = new Scanner(new FileReader(this.filename));
            reader.nextLine();
            for (int i = 0 ; i < overdue.length ; i ++  ){
                String[]line = reader.nextLine().split(",");
                this.overdue[i] = line[9];
                this.userID[i]  = line[0];
                this.repaymentPeriod[i] = line[6];
                this.remainingPayment[i] = line[5];

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void notification(String username) throws Exception{
        Date duedate=new Date();
        String remainingPayment  = "";

        for (int i = 0 ; i < userID.length ; i ++ ){
            //Actually its not useerID ...
            if (username.equals(userID[i])){
                 SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                 duedate = dateFormat.parse(overdue[i]);
                 remainingPayment = this.remainingPayment[i];
                break;
            }
        }
        Date currentTime =new Date();

        int compare=currentTime.compareTo(duedate);
        if (currentTime.after(duedate) || currentTime.equals(duedate)){
            Message message =new MimeMessage(getEmailSession());
            message.setFrom(new InternetAddress(EMAIL_FROM));
            String EMAIL_TO = "liangyao0808@gmail.com";
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
            message.setSubject("Remaining Payment");
            message.setText("RM" + remainingPayment + " required to pay before " + duedate);
            Transport.send(message);
            lbl=new Label("Reminder!\nAuto-deduction from balance will be taken to pay for loan(s)");
        }

    }
   
    private static Session getEmailSession(){
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(EMAIL_FROM , APP_PASSWORD);
            }
        });
    }

    private static Properties getGmailProperties(){
        Properties prop = new Properties();
        prop.put("mail.smtp.auth" , "true");
        prop.put("mail.smtp.starttls.enable" , "true");
        prop.put("mail.smtp.host" , "smtp.gmail.com");
        prop.put("mail.smtp.port" , "587");
        prop.put("mail.smtp.ssl.trust" , "smtp.gmail.com");
        return prop;
    }

    public static Label getLabel(){
        return lbl;   
    }

}