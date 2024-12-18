package insideout;

import static insideout.InsideOut.Username;
import static insideout.InsideOut.isUser;
import static insideout.InsideOut.popupMessage;
import static insideout.InsideOut.userinfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.Label;
import org.mindrot.jbcrypt.BCrypt;

public class LogIn {
    private String name="";
    private String email="";
    private String password="";
    private static String userinfo="/Users/cye/NewFolder/InsideOut/src/userinfo - Sheet1.csv";
    
    public LogIn(String name,String email,String password){
        this.name=name;
        this.email=email;
        this.password=password;
        
    }
    
    public Label login(){
        String line="";
        Label lbl=new Label();
       try(BufferedReader reader=new BufferedReader(new FileReader(userinfo))){
           boolean header=true;
           found:{
           while((line=reader.readLine())!=null){
               if (header) {
                header = false;
                continue;
            }
              String findUser[]=line.split(",");
              if(name.equals(findUser[0]) && BCrypt.checkpw(password,findUser[3]) && email.equals(findUser[2])){
                       Username=name;
                       isUser=true;
                       lbl=new Label("Welcome to InsideOut!");
                       getName();
                       break found;
                }
           }

           lbl=new Label("User Not Found!"); 
     
           }
           }catch (IOException ex){
                 ex.printStackTrace();
           }
       
       return lbl;
    }
    
    public String getName(){
        return name;
    }
}
