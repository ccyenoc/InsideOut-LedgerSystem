package insideout;

import static insideout.InsideOut.registrationValid;
import static insideout.InsideOut.store;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.scene.control.Label;
import org.mindrot.jbcrypt.BCrypt;

public class Registration{
    private static String username="";
    private static String email="";
    private static String password="";
    private static String userinfo = "src/userinfo.csv";

    public Registration(String username,String email,String password){
        this.username=username;
        this.email=email;
        this.password = password;
    }

    public Label register(){
        String line="";
        String newUserInfo="";
        Label lbl=new Label();
        boolean header=true;
        int lineIndex=0;
        ArrayList<String> lines=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(userinfo))){
            userFound:{
                while ((line = reader.readLine()) != null) { // to check if there is exist user and username taken
                    if (header) {
                        header = false;
                        lines.add(line);
                        continue;
                    }

                    String checkUser[] = line.split(",");
                    for (int i = 0; i < checkUser.length; i++) {
                        if (checkUser[2].equals(email)) { // check whether user exists
                            lbl = new Label("User Exist!");
                            break userFound;
                        } else if (checkUser[0].equals(username)) { // check if username taken
                            lbl = new Label("Username Taken!");
                            break userFound;
                        }
                    }
                    lineIndex++;
                    lines.add(line);
                }


                // when it is a new user and username is unique, check password and email format
                String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{3,}$";
                boolean validEmail = Pattern.matches(emailRegex, email);
                if (validEmail == true) {
                    registrationValid = true;
                }

                if (!validEmail) {
                    lbl = new Label("Invalid Email !");
                    break userFound;
                } else if ((password.length() < 8 ||  // requirement 1: length >=8
                        !password.matches(".*[A-Z].*") || // requirement 2: consist atleast 1 Uppercase
                        ! password.matches(".*[a-z].*") || // requirement 3: consist atleast 1 lowercase
                        !password.matches(".*\\d.*") || // requirement 4: consist atleast 1 digit
                        !password.matches(".*[@$!%*?&^#_+=[\\\\]{}|;:',<>./].*"))) // requirement 5: consist at least 1 special character
                {
                    lbl = new Label("Enter a Strong Password!");
                    break userFound;
                }

                if (registrationValid == true) {
                    // generate userID
                    int ID = 0;
                    if (lines.size() == 1) {
                        ID = 1;
                    } else {
                        String[] findLastID = lines.get(lineIndex).split(",");
                        // id without IO
                        int numID = Integer.parseInt(findLastID[1].replace("IO", ""));
                        ID = numID + 1;
                    }
                    String userID = "IO" + String.format("%7s", ID).replace(" ", "0");

                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    newUserInfo = username + "," + userID + "," + email + "," + hashedPassword;
                    store(userinfo,newUserInfo);
                }
            }

        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return lbl;
    }


}
