package insideout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

// for time display 
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.*;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;

import org.mindrot.jbcrypt.BCrypt;

public class InsideOut extends Application {
    
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final double ASPECT_RATIO = 16.0 / 9.0;
    public static boolean isUser=false;
    public static String Username="";
    
    @Override
    public void start(Stage primaryStage) {
        
// homepage (registration && login page)
        StackPane stackpane = new StackPane();
        AnchorPane homepage = new AnchorPane();
        homepage.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(homepage);
        Button register=new Button("Register"); 
        Button login=new Button("Log In");
        buttonfontsize(register);
        buttonfontsize(login);
        register.setPrefSize(100,35);
        login.setPrefSize(100,35);
        AnchorPane.setTopAnchor(register,300.0);
        AnchorPane.setLeftAnchor(register,200.0);
        AnchorPane.setLeftAnchor(login,400.0);
        AnchorPane.setTopAnchor(login,300.0);
        Label insideout=new Label("Inside Out");
        insideout.setFont(Font.font("Anton", 90)); 
        AnchorPane.setTopAnchor(insideout,50.0);
        AnchorPane.setLeftAnchor(insideout,160.0);
        Scene pagehomepage = new Scene (homepage,700,400);
        pagehomepage.setFill(Color.web("#a8c4f4"));
        
        Image piggybank = new Image("file:/Users/cye/NewFolder/InsideOut/src/piggybank.png"); 
        ImageView piggybankview=new ImageView(piggybank);
        piggybankview.setFitWidth(75);  
        piggybankview.setFitHeight(75); 
        Image money=new Image("file:/Users/cye/NewFolder/InsideOut/src/money.png");
        ImageView moneyview=new ImageView(money);
        moneyview.setFitWidth(75);  
        moneyview.setFitHeight(75); 
        Image moneybag=new Image("file:/Users/cye/NewFolder/InsideOut/src/moneybag.png");
        ImageView moneybagview=new ImageView(moneybag);
        moneybagview.setFitWidth(75);  
        moneybagview.setFitHeight(75); 
        Image coin=new Image("file:/Users/cye/NewFolder/InsideOut/src/coin.png");
        ImageView coinview=new ImageView(coin);
        coinview.setFitWidth(75);  
        coinview.setFitHeight(75); 
      
        piggybankview.setLayoutX(200);
        piggybankview.setLayoutY(200);
        moneyview.setLayoutX(270);
        moneyview.setLayoutY(200);
        moneybagview.setLayoutX(340);
        moneybagview.setLayoutY(200);
        coinview.setLayoutX(410);
        coinview.setLayoutY(200);
        
        
        stackpane.getChildren().addAll(register,login,insideout,piggybankview,moneybagview,moneyview);
        homepage.getChildren().addAll(insideout,register,login,piggybankview,moneybagview,moneyview,coinview);
       
        
// registration page 
        AnchorPane registration=new AnchorPane();
        registration.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(registration);
        Label steps = new Label("STEPS TO REGISTER :");
        steps.setFont(Font.font("Anton", 23)); 
        Label step1 = new Label ("1. ENTER USERNAME BY CLICKING ON "+"\nType your username...");
        step1.setFont(Font.font("Anton", 16)) ;
        Label step2 = new Label ("2. ENTER YOUR EMAIL BY CLICKING ON "+"\nType your email...");
        step2.setFont(Font.font("Anton", 16));
        Label step3 = new Label ("3. ENTER YOUR PASSWORD BY CLICKING ON "+"\nType your password...");
        step3.setFont(Font.font("Anton", 16));
        Label step4 = new Label ("4. CLICK THE Confirm BUTTON BELOW TO SAVE ");
        step4.setFont(Font.font("Anton", 16));
        Label step5=new Label ("5.CLICK THE Log In BUTTON TO LOG IN");
        step5.setFont(Font.font("Anton", 16));
        steps.setLayoutX(360);
        steps.setLayoutY(50);
        step1.setLayoutX(360);
        step1.setLayoutY(85);
        step2.setLayoutX(360);
        step2.setLayoutY(135);
        step3.setLayoutX(360);
        step3.setLayoutY(185);
        step4.setLayoutX(360);
        step4.setLayoutY(235);
        step5.setLayoutX(360);
        step5.setLayoutY(263);
        Label registrationtitle=new Label("REGISTRATION");
        registrationtitle=header(registrationtitle,registration);
        
           // registration guidelines
        yellowrec(registration);
        
        Button tologinpage=new Button("Confirm");
        buttonfontsize(tologinpage);
        tologinpage.setLayoutX(400);
        tologinpage.setLayoutY(300);
        tologinpage.setPrefSize(100,18);
        
        
        // method call
        Label username=instruction(100,"username");
        TextField inputusername= input("Enter your username :","username", 100.0 , 50.0);
        Label email=instruction(180,"email");
        TextField inputemail= input("Enter your email :","email",180.0 ,50.0);
        Label password=instruction(260,"password");
        TextField inputpassword =input("Enter your password:","password", 270.0 , 50.0);
        
        String[] registername={""};
        inputusername.textProperty().addListener((observable, oldValue, newValue) -> {
             registername[0] = newValue.trim();});
        String [] registeremail={""};
        inputemail.textProperty().addListener((observable, oldValue, newValue) -> {
            registeremail[0] = newValue.trim();}); 
        String[] registerpassword={""};
        inputpassword.textProperty().addListener((observable, oldValue, newValue) -> {
             registerpassword[0] = newValue.trim();}); 
        
        
        stackpane.getChildren().add(tologinpage);      
        registration.getChildren().addAll(inputusername,inputpassword,inputemail,tologinpage,steps,step1,step2,step3,step4,step5
        ,registrationtitle,username,email,password);
        Scene pageregistration=new Scene (registration,700,400);
        pageregistration.setFill(Color.web("#a8c4f4"));
        
// log in page
        AnchorPane logIn=new AnchorPane();
        whiterec(logIn);
        logIn.setStyle("-fx-background-color: #a8c4f4;");
        Label logintitle=new Label("LOG IN");
        logintitle=header(logintitle,logIn);
        Button loginbtn=new Button("Log In");
        buttonfontsize(loginbtn);
        AnchorPane.setTopAnchor(loginbtn,300.0);
        AnchorPane.setLeftAnchor(loginbtn,400.0);
        
        // method call
        username=instruction(100,"username");
        TextField loginusername=input("Enter your username :","username", 100.0 , 50.0);
        email=instruction(180,"email");
        TextField loginemail=input("Enter your email :","email",180.0 ,50.0);
        password=instruction(260,"password");
        TextField loginpassword =input("Enter your password:","password", 270.0 , 50.0);
        Scene pagelogin=new Scene(logIn,700,400);
        pagelogin.setFill(Color.web("#a8c4f4"));

        String[] name = {""};
        loginusername.textProperty().addListener((observable, oldValue, newValue) -> {
             name[0] = newValue.trim();}); 

        String[] useremail = {""};
        loginemail.textProperty().addListener((observable, oldValue, newValue) -> {
              useremail[0] = newValue.trim();});

        String[] userpassword={""};
        loginpassword.textProperty().addListener((observable, oldValue, newValue) -> {
              userpassword[0] = newValue.trim();});

        logIn.getChildren().addAll(loginbtn,username,loginusername,loginpassword,password,loginemail,email,logintitle);
        
// main page
        AnchorPane mainPage=new AnchorPane();
        whiterec(mainPage);
        Scene pagemainPage= new Scene (mainPage,700,400);
        pagemainPage.setFill(Color.web("#a8c4f4"));
        mainPage.setStyle("-fx-background-color: #a8c4f4;");
        currenttime(mainPage);
        Button debitbtn =new Button ("Debit");
        Button creditbtn=new Button("Credit");        
        Button savingbtn=new Button("Savings");       
        Button historybtn=new Button("History");       
        Button creditloanbtn=new Button("Credit Loan");       
        Button interestpredictorbtn=new Button("Deposit Interest Predictor");       
        Button logoutbtn=new Button("Log Out");
        mainpagebtn(debitbtn,260,20,213,75);
        mainpagebtn(creditbtn,260,20,213,365);
        mainpagebtn(savingbtn,260,20,258,75);
        mainpagebtn(historybtn,260,20,258,365);
        mainpagebtn(creditloanbtn,260,20,303,75);
        mainpagebtn(interestpredictorbtn,260,20,303,365);
        
        Rectangle menu = new Rectangle(607,150);  // Width, Height
        menu.setFill(Color.web("#FEEBA8"));  // Fill the rectangle with blue color
        menu.setArcWidth(20);     // Horizontal radius of the corner
        menu.setArcHeight(20);    // Vertical radius of the corner
        menu.setLayoutX(45);
        menu.setLayoutY(200);
        
        Label welcome=new Label("Welcome ");// get username afterward
        welcome=header(welcome,mainPage);
        welcome.setFont(Font.font("Anton", 60));
        mainPage.getChildren().addAll(menu,welcome,debitbtn,creditbtn,historybtn,creditloanbtn,interestpredictorbtn,logoutbtn,savingbtn); 
        
// debit page
         AnchorPane debit=new AnchorPane();
         Scene pagedebit=new Scene(debit,700,400);
         debit.setStyle("-fx-background-color: #a8c4f4;");
         whiterec(debit);
         pagedebit.setFill(Color.web("#a8c4f4"));
         Label debittitle= new Label("Debit");
         debittitle=header(debittitle,debit);
         Label amountinstruction=instruction(100,"Debit Amount");
         Label descriptioninstruction=instruction( 160,"Description");     
         
         // to enter amount of debit
        // Create the TextField for amount debit
        TextField amountdebit = input("Enter Debit Amount:", "Debit Amount", 100.0, 50.0);
        TextArea descriptiond = description("Enter Description:", "Description", 185.0, 50.0);

        String[] descriptiondstr = {""};
        descriptiond.textProperty().addListener((observable, oldValue, newValue) -> {
        descriptiondstr[0] = newValue; // Update the description dynamically as user types
        });


        Button confirmdebit = new Button("Confirm");
        confirmdebit.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        confirmdebit.setFont(Font.font("Anton", 20)); 
        confirmdebit.setLayoutX(500);
        confirmdebit.setLayoutY(300);

       confirmdebit.setOnAction(e -> {
       final String input = amountdebit.getText(); // Get the text entered by the user in amountdebit TextField
       try{
          double debitamount =Double.parseDouble(input); 
          Debit(debitamount, descriptiondstr[0], "Debit");
          
       }catch(Exception ex){
           Label wrongcashformat=new Label("Wrong Cash Format eg.1000");
           popupMessage(wrongcashformat);
           ex.printStackTrace();
       }
         });



         
         ImageView piggybankdebit=new ImageView(piggybank);
         piggybankdebit.setLayoutX(150);
         piggybankdebit.setLayoutY(40);
         piggybankdebit.setFitWidth(50);
         piggybankdebit.setFitHeight(50);
         ImageView coindebit=new ImageView(coin);
         coindebit.setLayoutX(185);
         coindebit.setLayoutY(42);
         coindebit.setFitWidth(60);
         coindebit.setFitHeight(60);
         
         
         debit.getChildren().addAll(confirmdebit,amountdebit,descriptiond,debittitle,amountinstruction,descriptioninstruction,piggybankdebit,coindebit);
      
         
// credit page
         AnchorPane credit=new AnchorPane();
         Scene pagecredit=new Scene(credit,700,400);
         credit.setStyle("-fx-background-color: #a8c4f4;");
         whiterec(credit);
         pagecredit.setFill(Color.web("#a8c4f4"));
         Label credittitle= new Label("Credit");
         credittitle=header(credittitle,credit);
         amountinstruction=instruction(100,"Credit Amount");
         descriptioninstruction=instruction( 160,"Description");     
         
         // to enter amount of debit/credit
        TextField amountcredit = input("Enter Debit Amount:", "Debit Amount", 100.0, 50.0);
        TextArea descriptionc = description("Enter Description:", "Description", 185.0, 50.0);

        String[] descriptioncstr = {""};
        descriptionc.textProperty().addListener((observable, oldValue, newValue) -> {
        descriptioncstr[0] = newValue; // Update the description dynamically as user types
        });


       Button confirmcredit = new Button("Confirm");
       confirmcredit.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
       confirmcredit.setFont(Font.font("Anton", 20)); 
       confirmcredit.setLayoutX(500);
       confirmcredit.setLayoutY(300);
       confirmcredit.setOnAction(e -> {
       final String input = amountcredit.getText(); // Get the text entered by the user in amountdebit TextField
       try{
          double creditamount =Double.parseDouble(input); 
          Credit(creditamount, descriptioncstr[0], "Credit");
          
       }catch(Exception ex){
           Label wrongcashformat=new Label("Wrong Cash Format eg.1000");
           popupMessage(wrongcashformat);
           ex.printStackTrace();
       }
         });
         credit.getChildren().addAll(amountcredit,descriptionc,credittitle,amountinstruction,descriptioninstruction,confirmcredit);
         
// history page
        
        AnchorPane history=new AnchorPane();
        Scene pagehistory=new Scene (history,700,400);
        history.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(history);
        pagehistory.setFill(Color.web("#a8c4f4"));
        Label historytitle=new Label("History");
        historytitle=header(historytitle,history);
        historytitle.toFront();
        historytitle.setVisible(true);
        history.getChildren().add(historytitle);
        
        
        TableView<Transaction> tableView = new TableView<>();
        
  
        TabPane tabPane = new TabPane();
        tabPane.setPrefWidth(635);  
        tabPane.setPrefHeight(258); 
        AnchorPane.setTopAnchor(tabPane, 107.0);
        AnchorPane.setLeftAnchor(tabPane, 30.0);
        tableView.setStyle("-fx-background-radius: 15; -fx-background-color: #a8c4f4;");

        Tab overviewTab = new Tab("Overview");
        overviewTab.setClosable(false); // Disables the close button
        TableView<Transaction> tableViewOverview = new TableView<>();
        TableColumn<Transaction, String> TransactionIDOverview = new TableColumn<>("TransactionID");
        TransactionIDOverview.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        TransactionIDOverview.setPrefWidth(113);  // Column width


        TableColumn<Transaction, String> TimeOverview = new TableColumn<>("Time");
        TimeOverview.setCellValueFactory(new PropertyValueFactory<>("time"));
        TimeOverview.setPrefWidth(183);  // Column width
        
        TableColumn<Transaction, String> AmountOverview = new TableColumn<>("Amount");
        AmountOverview.setCellValueFactory(new PropertyValueFactory<>("amount"));
        AmountOverview.setPrefWidth(109);  // Column width
        
        TableColumn<Transaction, String> DescriptionOverview = new TableColumn<>("Description");
        DescriptionOverview.setCellValueFactory(new PropertyValueFactory<>("description"));
        DescriptionOverview.setPrefWidth(230); 
        overviewTab.setContent(tableViewOverview);
        
        Tab debitTab = new Tab("Debit");
        debitTab.setClosable(false); // Disables the close button
        TableView<Transaction> tableViewDebit = new TableView<>();
        TableColumn<Transaction, String> TransactionIDDebit = new TableColumn<>("TransactionID");
        TransactionIDDebit.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        TransactionIDDebit.setPrefWidth(113);

        TableColumn<Transaction, String> TimeDebit = new TableColumn<>("Time");
        TimeDebit.setCellValueFactory(new PropertyValueFactory<>("time"));
        TimeDebit.setPrefWidth(183);
        
        TableColumn<Transaction, String> AmountDebit = new TableColumn<>("Amount");
        AmountDebit.setCellValueFactory(new PropertyValueFactory<>("amount"));
        AmountDebit.setPrefWidth(109); 
        
        TableColumn<Transaction, String> DescriptionDebit = new TableColumn<>("Description");
        DescriptionDebit.setCellValueFactory(new PropertyValueFactory<>("description"));
        DescriptionDebit.setPrefWidth(230); 
        debitTab.setContent(tableViewDebit);
        
        Tab creditTab = new Tab("Credit");
        creditTab.setClosable(false); // Disables the close button
        TableView<Transaction> tableViewCredit = new TableView<>();
        TableColumn<Transaction, String> TransactionIDCredit = new TableColumn<>("TransactionID");
        TransactionIDCredit.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        TransactionIDCredit.setPrefWidth(113);


        TableColumn<Transaction, String> TimeCredit = new TableColumn<>("Time");
        TimeCredit.setCellValueFactory(new PropertyValueFactory<>("time"));
        TimeCredit.setPrefWidth(183);
        
        TableColumn<Transaction, String> AmountCredit = new TableColumn<>("Amount");
        AmountCredit.setCellValueFactory(new PropertyValueFactory<>("amount"));
        AmountCredit.setPrefWidth(109); 
        
        TableColumn<Transaction, String> DescriptionCredit = new TableColumn<>("Description");
        DescriptionCredit.setCellValueFactory(new PropertyValueFactory<>("description"));
        DescriptionCredit.setPrefWidth(230); 
        creditTab.setContent(tableViewCredit);
        

        tableViewOverview.getColumns().addAll(TransactionIDOverview, TimeOverview, AmountOverview, DescriptionOverview);
        tableViewDebit.getColumns().addAll(TransactionIDDebit, TimeDebit, AmountDebit, DescriptionDebit);
        tableViewCredit.getColumns().addAll(TransactionIDCredit, TimeCredit, AmountCredit, DescriptionCredit);

        Transaction data = new Transaction(); 
        data.readFile();
        tableViewDebit.setItems(data.getDebitData());
        tableViewCredit.setItems(data.getCreditData());
        tableViewOverview.setItems(data.getOverviewData());
        tabPane.getTabs().addAll(overviewTab,debitTab,creditTab);
       
        history.getChildren().add(tabPane);
        pagehistory.setFill(Color.web("#a8c4f4"));

        
// saving page
        AnchorPane saving=new AnchorPane();
        Scene pagesaving=new Scene(saving,700,400);
        saving.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(saving);
        pagesaving.setFill(Color.web("#a8c4f4"));
        Label savingtitle=new Label("Saving");
        savingtitle=header(savingtitle,saving);
        
        Label savingactivationlbl=new Label("Are you sure to activate it?");
        savingactivationlbl.setLayoutX(50);
        savingactivationlbl.setLayoutY(85);
        savingactivationlbl.setFont(Font.font("Anton", 35));
        
        Label enterSavingPercentagelbl=new Label("Enter percentage(%) to be deducted from the next debit :");
        enterSavingPercentagelbl.setLayoutX(50);
        enterSavingPercentagelbl.setLayoutY(200);
        enterSavingPercentagelbl.setFont(Font.font("Anton", 25));
        enterSavingPercentagelbl.setVisible(false);
        enterSavingPercentagelbl.setManaged(false);
                  
        Button yes=new Button("Yes");
        yes.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        yes.setFont(Font.font("Anton", 15)); 
        yes.setPrefSize(50,15);
        yes.setLayoutX(50);
        yes.setLayoutY(150);
        yes.setOnAction(e->{
            enterSavingPercentagelbl.setVisible(true);
            enterSavingPercentagelbl.setManaged(true);
            enterPercentage(saving);
        });
        
        
        Button no=new Button("No");
        no.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        no.setFont(Font.font("Anton", 15)); 
        no.setLayoutX(120);
        no.setLayoutY(150);
        no.setPrefSize(50,15);
        no.setOnAction(e->{
            Label selectother=new Label("Saving Not Activated, proceed with other page");
            popupMessage(selectother);
        });
        
        

        TextField enterPercentage=input("Enter the percentage(%) to be deducted from next debit :", "percentage(%)", 100.0, 50.0);
        
        
        
        
        saving.getChildren().addAll(savingtitle,yes,no,savingactivationlbl,enterSavingPercentagelbl);
        
// credit loan page
         AnchorPane creditloan=new AnchorPane();
         Scene pagecreditloan=new Scene(creditloan,700,400);
         creditloan.setStyle("-fx-background-color: #a8c4f4;");
         whiterec(creditloan);
         pagecreditloan.setFill(Color.web("#a8c4f4"));
         Label creditloantitle=new Label("Credit Loan");
         creditloantitle=header(creditloantitle,creditloan);
         
         creditloan.getChildren().addAll(creditloantitle);
         
// predited deposit page
        AnchorPane predicteddeposit=new AnchorPane();
        Scene pagedeposit=new Scene(predicteddeposit,700,400);
        predicteddeposit.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(predicteddeposit);
        pagecreditloan.setFill(Color.web("#a8c4f4"));
        Label deposittitle=new Label("Predicted Deposit");
        deposittitle=header(deposittitle,predicteddeposit);
        Label bank=new Label("Bank :");
        bank.setLayoutX(50);
        bank.setLayoutY(100);
        bank.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        bank.setFont(Font.font("Anton", 23));  // Set the font family and size here
        
        
        bankSelection(predicteddeposit);
        
        Button arrow=new Button("↓");
        arrow.setLayoutX(250);
        arrow.setLayoutY(150);
        arrow.setOnAction(e->showBankSelection(predicteddeposit));
        
        TextArea displayDeposit = new TextArea();
        displayDeposit.setEditable(false); // Make it read-only
        displayDeposit.setWrapText(true);
        displayDeposit.setLayoutX(50);
        displayDeposit.setLayoutY(100);
        displayDeposit.setPrefWidth(200);
        displayDeposit.setPrefHeight(100); // Adjust height as nee
        Button displayPredictedDepositbtn=new Button("Calculate Predicted Deposit");
        
           
        
        
        predicteddeposit.getChildren().addAll(deposittitle,arrow,bank);
       
        submenu(primaryStage,debit,pagehomepage,pagedebit,pagecredit,pagesaving,pagehistory,pagecreditloan,pagedeposit);
        submenu(primaryStage,credit,pagehomepage,pagedebit,pagecredit,pagesaving,pagehistory,pagecreditloan,pagedeposit);
        submenu(primaryStage,history,pagehomepage,pagedebit,pagecredit,pagesaving,pagehistory,pagecreditloan,pagedeposit);
        submenu(primaryStage,saving,pagehomepage,pagedebit,pagecredit,pagesaving,pagehistory,pagecreditloan,pagedeposit);
        submenu(primaryStage,creditloan,pagehomepage,pagedebit,pagecredit,pagesaving,pagehistory,pagecreditloan,pagedeposit);
        submenu(primaryStage,predicteddeposit,pagehomepage,pagedebit,pagecredit,pagesaving,pagehistory,pagecreditloan,pagedeposit);
       
        
// button action (navigation,setScene)
        primaryStage.setScene(pagehomepage);
        register.setOnAction(e-> primaryStage.setScene(pageregistration)); // to registration page
        login.setOnAction(e-> primaryStage.setScene(pagelogin)); // homepage to log in page
        tologinpage.setOnAction(e->{
                register(registername[0],registeremail[0],registerpassword[0]);
                if(registrationValid==true){
                primaryStage.setScene(pagelogin);}
        }); // registration to log in page
        loginbtn.setOnAction(e-> {
          logIn(name[0], useremail[0], userpassword[0]);
          if(isUser==true){
            primaryStage.setScene(pagemainPage);}
                }); //from login to mainpage
        debitbtn.setOnAction(e-> primaryStage.setScene(pagedebit));
        creditbtn.setOnAction(e->primaryStage.setScene(pagecredit));
        historybtn.setOnAction(e->primaryStage.setScene(pagehistory));
        savingbtn.setOnAction(e->primaryStage.setScene(pagesaving));
        interestpredictorbtn.setOnAction(e->primaryStage.setScene(pagedeposit));
        
        primaryStage.setTitle("InsideOut");
        primaryStage.show();
        // Add listener to maintain aspect ratio
        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double newHeight = newWidth.doubleValue() / ASPECT_RATIO;
            primaryStage.setHeight(newHeight);
        });

        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double newWidth = newHeight.doubleValue() * ASPECT_RATIO;
            primaryStage.setWidth(newWidth);
        });

        primaryStage.show();
        Image icon = new Image("file:/Users/cye/NewFolder/InsideOut/src/insideout/insideoutlogo.png"); 
        primaryStage.getIcons().add(icon);
        
       
       
    }
   
 
// method(s)

// decorations
    
    public void whiterec(AnchorPane pane){
        Rectangle whiterec = new Rectangle(635,335);  // Width, Height
        whiterec.setFill(Color.WHITE);  // Fill the rectangle with blue color
        whiterec.setArcWidth(20);     // Horizontal radius of the corner
        whiterec.setArcHeight(20);    // Vertical radius of the corner
        AnchorPane.setTopAnchor(whiterec,30.0);
        AnchorPane.setLeftAnchor(whiterec,30.0);
        pane.getChildren().add(whiterec);
    }
    
    public void yellowrec(AnchorPane pane){
        Rectangle yellowrec = new Rectangle(320,300);  // Width, Height
        yellowrec.setFill(Color.web("#FEEBA8"));  // Fill the rectangle with blue color
        yellowrec.setArcWidth(20);     // Horizontal radius of the corner
        yellowrec.setArcHeight(20);    // Vertical radius of the corner
        AnchorPane.setTopAnchor(yellowrec,45.0);
        AnchorPane.setLeftAnchor(yellowrec,325.0);
        pane.getChildren().add(yellowrec);
    }
    
    public Label header(Label label,AnchorPane pane){
        Label header=label;
        AnchorPane.setTopAnchor(header, 30.0);
        AnchorPane.setLeftAnchor(header, 50.0);
        label.setFont(Font.font("Anton", 45));
        return header;
    }
    
    // yellow button
    public void buttonfontsize(Button button){
        button.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 20)); 
    }
    
    // button
        public void mainpagebtn(Button button, double width, double height, double topAnchor, double leftAnchor) {
        button.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 15));
        button.setPrefSize(260,20);
        
        button.setPrefSize(width, height); 
        AnchorPane.setTopAnchor(button, topAnchor); 
        AnchorPane.setLeftAnchor(button, leftAnchor); 
    }
        public void submenubtn(Button button, double width, double height, double topAnchor, double leftAnchor) {
        button.setStyle("-fx-background-color:#fff8e3;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 15));
        button.setPrefSize(260,20);
        
        button.setPrefSize(width, height); 
        AnchorPane.setTopAnchor(button, topAnchor); 
        AnchorPane.setLeftAnchor(button, leftAnchor); 
    }
 
        public static Label currenttime(AnchorPane pane) {
        Label time = new Label();  // display the time
        time.setFont(Font.font("Anton", 70)); 
        time.setLayoutX(410);
        time.setLayoutY(20);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Timeline timeline = new Timeline(  // control the time
            new KeyFrame(Duration.seconds(1), event -> {
                String currentTime = LocalTime.now().format(timeFormatter);
                time.setText(currentTime);
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
       

        pane.getChildren().add(time);
        return time;
    }
        
    
// input 
    public TextField input(String text,String type,double top,double left){
        // create label
        Label label = new Label(text);
        label.setFont(Font.font("Anton", 15));
        AnchorPane.setTopAnchor(label, top);
        AnchorPane.setLeftAnchor(label, left);
        
        // user input section
        TextField textField = new TextField();
        textField.setPromptText("Type your "+type+" here...");
        textField.setPrefColumnCount(19);
        textField.setStyle("-fx-font-size: 14px; -fx-background-color:#FFFFFF ;-fx-text-fill: black; -fx-border-radius: 5px;");
        textField.setFont(Font.font("Anton", 12));
        AnchorPane.setTopAnchor(textField, top + 25);
        AnchorPane.setLeftAnchor(textField, left);

        // line
        Line line = new Line(left, top + 55, left + 230, top + 55); // Adjust the length of the line
        line.setStroke(Color.BLACK);

        // Add label and line to the AnchorPane (not returned, but you can add them in your main method)
        return textField;
        
    }
   
 
    public Label instruction(int inputY,String type){
        Label instruction=new Label("Enter your "+type+":");
        instruction.setFont(Font.font("Anton", 15));
        instruction.setLayoutX(50);
        instruction.setLayoutY(inputY);
        return instruction;
    }
    
    public TextArea description(String text,String type,double top,double left){
        Label label = new Label(text); // to display Enter your text :
        label.setFont(Font.font("Anton", 15));
        AnchorPane.setTopAnchor(label, top);
        AnchorPane.setLeftAnchor(label, left);
        
        // user input section
        TextArea area = new TextArea();
        area.setPromptText("Type your "+type+" here..."); // to display Type your type here ... section
        area.setPrefWidth(400);  // Set the preferred width
        area.setPrefHeight(160);  
        area.setWrapText(true); // this ensures when text type exceed the width of textarea thn the text will be displayed at the nxt line
        area.setStyle("-fx-font-size: 15px; -fx-background-color:#FFFFFF ; -fx-text-fill: black; -fx-border-radius: 5px;");
        area.setFont(Font.font("Anton", 12));
        AnchorPane.setTopAnchor(area, top);
        AnchorPane.setLeftAnchor(area, left);

        // line
        Line line = new Line(left, top + 55, left + 230, top + 55); // Adjust the length of the line
        line.setStroke(Color.BLACK);

        // Add label and line to the AnchorPane (not returned, but you can add them in your main method)
        return area;
    }
    
     
 // navigation methods
    
    public void submenu(Stage primaryStage,AnchorPane pane,Scene homepage, Scene debit,Scene credit,Scene saving,Scene history,Scene creditloan,Scene deposit){
        Rectangle yellowrec = new Rectangle(240,335);  // Width, Height
        yellowrec.setFill(Color.web("#f4d2d2"));  // Fill the rectangle with blue color
        yellowrec.setArcWidth(20);     // Horizontal radius of the corner
        yellowrec.setArcHeight(20);    // Vertical radius of the corner
        AnchorPane.setTopAnchor(yellowrec,30.0);
        AnchorPane.setLeftAnchor(yellowrec,425.0);
        pane.getChildren().add(yellowrec);
        
        Button submenu=new Button("☰");
        submenu.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        submenu.setFont(Font.font("Anton", 15));
        submenu.setLayoutX(600);
        submenu.setLayoutY(42);
        submenu.setPrefWidth(35);
        submenu.setPrefHeight(35);
        
        submenu.setOnAction(e -> showSubmenu(pane));
   

        pane.getChildren().add(submenu);
        
        Button debitbtn =new Button ("Debit");
        Button creditbtn=new Button("Credit");        
        Button savingbtn=new Button("Savings");       
        Button historybtn=new Button("History");       
        Button creditloanbtn=new Button("Credit Loan");       
        Button interestpredictorbtn=new Button("Deposit Interest Predictor");       
        Button logoutbtn=new Button("Log Out");
        submenubtn(debitbtn,190,20,85,455);  // just for color and coordinate
        submenubtn(creditbtn,190,20,125,455); 
        submenubtn(savingbtn,190,20,165,455); 
        submenubtn(historybtn,190,20,205,455); 
        submenubtn(creditloanbtn,190,20,245,455); 
        submenubtn(interestpredictorbtn,190,20,285,455); 
        submenubtn(logoutbtn,190,20,325,452); 
        logoutbtn.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        debitbtn.setVisible(false);
        creditbtn.setVisible(false);
        savingbtn.setVisible(false);
        historybtn.setVisible(false);
        creditloanbtn.setVisible(false);
        interestpredictorbtn.setVisible(false);
        logoutbtn.setVisible(false);
        yellowrec.setVisible(false);
        
        debitbtn.setManaged(false);
        creditbtn.setManaged(false);
        savingbtn.setManaged(false);
        historybtn.setManaged(false);
        creditloanbtn.setManaged(false);
        interestpredictorbtn.setManaged(false);
        logoutbtn.setManaged(false);
        yellowrec.setManaged(false);
        
        debitbtn.getStyleClass().add("submenu");
        creditbtn.getStyleClass().add("submenu");
        savingbtn.getStyleClass().add("submenu");
        historybtn.getStyleClass().add("submenu");
        creditloanbtn.getStyleClass().add("submenu");
        interestpredictorbtn.getStyleClass().add("submenu");
        logoutbtn.getStyleClass().add("submenu");
        yellowrec.getStyleClass().add("submenu");
        

        pane.getChildren().addAll(debitbtn, creditbtn, savingbtn, historybtn, creditloanbtn, interestpredictorbtn, logoutbtn);

        debitbtn.setOnAction(e -> primaryStage.setScene(debit));
        creditbtn.setOnAction(e -> primaryStage.setScene(credit));
        savingbtn.setOnAction(e -> primaryStage.setScene(saving));
        historybtn.setOnAction(e -> primaryStage.setScene(history));
        creditloanbtn.setOnAction(e -> primaryStage.setScene(creditloan));
        interestpredictorbtn.setOnAction(e -> primaryStage.setScene(deposit));
        logoutbtn.setOnAction(e -> primaryStage.setScene(homepage)); // For example, go back to Page 1 when "Log Out" is clicked
        
 
    }
    
    public void showSubmenu(AnchorPane pane) {
    // Find all buttons added to the pane
    for (javafx.scene.Node node : pane.getChildren()) {
         if (node.getStyleClass().contains("submenu")) {
            node.setVisible(!node.isVisible()); // Toggle visibility
            node.setManaged(!node.isManaged()); // Toggle layout inclusion
        }
    }
    } 
    
    private String selectedBank=""; // it should be static as
    
    public void bankSelection(AnchorPane pane){
        
        
       Label instruction = new Label("Select Your Bank");
       instruction.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
       instruction.setFont(Font.font("Anton", 23));  // Set the font family and size here
       instruction.setLayoutX(50);
       instruction.setLayoutY(130);

        // button to click 
        Button RHB =new Button("RHB");
        bankSelectionbtn(RHB,50,170,15,150);
        Button MayBank=new Button("MayBank");
        bankSelectionbtn(MayBank,50,205,15,150);
        Button HongLeong=new Button("Hong Leong");
        bankSelectionbtn(HongLeong,50,240,15,150);
        Button Alliance=new Button("Alliance");
        bankSelectionbtn(Alliance,50,275,15,150);
        Button AmBank=new Button("AmBank");
        bankSelectionbtn(AmBank,50,310,15,150);
        Button StandardChartered=new Button("Standard Chartered");
        bankSelectionbtn(StandardChartered,50,345,15,150);   
        Button confirmbtn=new Button("Confirm");
        confirmbtn.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        confirmbtn.setFont(Font.font("Anton", 15));
        confirmbtn.setLayoutX(300);
        confirmbtn.setLayoutY(150);
        
        // to show user bank
        Label rhblbl=new Label("RHB");
        rhblbl.setVisible(false);
        rhblbl.setManaged(false);
        Label maybanklbl=new Label("MayBank");
        maybanklbl.setVisible(false);
        maybanklbl.setManaged(false);
        Label hongleonglbl=new Label ("Hong Leong");
        hongleonglbl.setVisible(false);
        hongleonglbl.setManaged(false);
        Label alliancelbl=new Label("Alliance");
        alliancelbl.setVisible(false);
        alliancelbl.setManaged(false);
        Label ambanklbl=new Label("AmBank");
        ambanklbl.setVisible(false);
        ambanklbl.setManaged(false);
        Label standardcharteredlbl=new Label("Standard Chartered");
        standardcharteredlbl.setVisible(false);
        standardcharteredlbl.setManaged(false);
        
        // bg of the button
        Rectangle bankselectionBG=new Rectangle();
        bankselectionBG.setFill(Color.web("#f4d2d2"));  // Fill the rectangle with blue color
        bankselectionBG.setArcWidth(20);     // Horizontal radius of the corner
        bankselectionBG.setArcHeight(20);    // Vertical radius of the corner
        bankselectionBG.setLayoutX(40);
        bankselectionBG.setLayoutY(250);
        
       // adding nodes into style class so that can control its visibility 
        RHB.getStyleClass().add("bankSelection");
        MayBank.getStyleClass().add("bankSelection");
        HongLeong.getStyleClass().add("bankSelection");
        Alliance.getStyleClass().add("bankSelection");
        AmBank.getStyleClass().add("bankSelection");
        StandardChartered.getStyleClass().add("bankSelection");
        
        Label depositLabel = new Label("Predited Deposit :");
        
        
        
        RHB.setOnAction(e -> { 
        label(rhblbl);
        MayBank.setVisible(false);
        MayBank.setManaged(false);
        HongLeong.setVisible(false);
        HongLeong.setManaged(false);
        Alliance.setVisible(false);
        Alliance.setManaged(false);
        AmBank.setVisible(false);
        AmBank.setManaged(false);
        StandardChartered.setVisible(false);
        StandardChartered.setManaged(false);
        instruction.setVisible(false); 
        selectedBank="RHB";
        });
        MayBank.setOnAction(e -> { 
        RHB.setVisible(false);
        RHB.setManaged(false);
        HongLeong.setVisible(false);
        HongLeong.setManaged(false);
        Alliance.setVisible(false);
        Alliance.setManaged(false);
        AmBank.setVisible(false);
        AmBank.setManaged(false);
        StandardChartered.setVisible(false);
        StandardChartered.setManaged(false);
        label(maybanklbl); 
        showBankSelection(pane); 
        instruction.setVisible(false);
        selectedBank="MayBank";
        });
        HongLeong.setOnAction(e -> { 
        RHB.setVisible(false);
        RHB.setManaged(false);
        MayBank.setVisible(false);
        MayBank.setManaged(false);
        Alliance.setVisible(false);
        Alliance.setManaged(false);
        AmBank.setVisible(false);
        AmBank.setManaged(false);
        StandardChartered.setVisible(false);
        StandardChartered.setManaged(false);
        label(hongleonglbl);
        instruction.setVisible(false); 
        selectedBank="HongLeong";
        });
        Alliance.setOnAction(e -> { 
        RHB.setVisible(false);
        RHB.setManaged(false);
        MayBank.setVisible(false);
        MayBank.setManaged(false);
        HongLeong.setVisible(false);
        HongLeong.setManaged(false);
        AmBank.setVisible(false);
        AmBank.setManaged(false);
        StandardChartered.setVisible(false);
        StandardChartered.setManaged(false);
        label(alliancelbl);
        instruction.setVisible(false);
        selectedBank="Allaince";
        });
        AmBank.setOnAction(e -> { 
        RHB.setVisible(false);
        RHB.setManaged(false);
        MayBank.setVisible(false);
        MayBank.setManaged(false);
        HongLeong.setVisible(false);
        HongLeong.setManaged(false);
        Alliance.setVisible(false);
        Alliance.setManaged(false);
        StandardChartered.setVisible(false);
        StandardChartered.setManaged(false);
        label(ambanklbl);
        instruction.setVisible(false); 
        selectedBank="AmBank";
        });
        StandardChartered.setOnAction(e -> { 
        RHB.setVisible(false);
        RHB.setManaged(false);
        MayBank.setVisible(false);
        MayBank.setManaged(false);
        HongLeong.setVisible(false);
        HongLeong.setManaged(false);
        Alliance.setVisible(false);
        Alliance.setManaged(false);
        AmBank.setVisible(false);
        AmBank.setManaged(false);
        label(standardcharteredlbl);
        instruction.setVisible(false); 
        selectedBank="StandardChartered";
        });
        
        confirmbtn.setOnAction(c -> {
        if (selectedBank != null) {
            // Perform calculation and update the label
            depositCalculator(selectedBank, depositLabel, pane);
        } else {
            depositLabel.setText("Please select a bank first!");
        }
    });
        
  
        
        pane.getChildren().addAll(confirmbtn,bankselectionBG,RHB,MayBank,HongLeong,Alliance,AmBank,StandardChartered,instruction,rhblbl,maybanklbl,hongleonglbl,alliancelbl,ambanklbl,standardcharteredlbl);
        
    }
    
    public void bankSelectionbtn(Button btn,double x,double y,double height,double width){
        btn.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        btn.setFont(Font.font("Anton", 15));
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        btn.setPrefSize(width,height);
        btn.setVisible(false);
        btn.setManaged(false);
        btn.toFront();
    }
    
    
    public void setLabelVisibility(Label label) {
    label.setVisible(false);
    label.setManaged(false);  // Initially set the label to not be visible or managed
    }
    
    public void label(Label label){
       label.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
       label.setFont(Font.font("Anton", 23));  // Set the font family and size here
       label.setLayoutX(100);
       label.setLayoutY(150);
       label.setVisible(true);
       label.setManaged(true);
    }
    
    public void showBankSelection(AnchorPane pane) {
    // Find all buttons added to the pane
    for (javafx.scene.Node node : pane.getChildren()) {
         if (node.getStyleClass().contains("bankSelection")) {
            node.setVisible(!node.isVisible()); 
            node.setManaged(!node.isManaged()); 
        }
        
    }
    } 
    

// functions
// log in page
    public static String userinfo="/Users/cye/NewFolder/InsideOut/src/userinfo - Sheet1.csv";
    public static void logIn(String name,String email,String password){
       LogIn userLogIn=new LogIn(name,email,password);
       Label lbl=userLogIn.login();
       Username=userLogIn.getName();
       popupMessage(lbl);
       
    }  
   
// registration page
    static boolean registrationValid=false;
    public static void register(String username,String email,String password){
        Registration userRegister=new Registration(username,email,password);
        Label message=userRegister.register();
        popupMessage(message);  
    }
    
// record debit and credit
    static void store(String file,String content) {
        String line;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));

            bw.newLine();
            bw.write(content);
        }
        catch(IOException e){
            e.printStackTrace();

        }
        finally {
            
             try {
                if (bw != null) {
                    bw.close(); // Close BufferedWriter
                }
            }
            catch (IOException e ){

                e.printStackTrace();
            }

        }
    }

    static String transactioninfo="";
    static int transactionID=1;
    static double balance=0.0;
    static ArrayList<String> getBalance=new ArrayList<>();
    static void Debit(double amount,String description,String type){
        Debit debit=new Debit(Username,amount,description,type);
        Label message=debit.getLabel();
        popupMessage(message);
        
    }
    
    static void Credit(double amount,String description,String type){
        Credit credit=new Credit(Username,amount,description,type);
        Label message=credit.getLabel();
        popupMessage(message);
    }
 
// savings
    public static void enterPercentage(AnchorPane pane){
        TextField enterPercentage = new TextField();
        enterPercentage.setPromptText("Enter percentage(%) here :");
        enterPercentage.setStyle("-fx-text-fill:black;");
        enterPercentage.setFont(Font.font("Anton", 15)); 
        enterPercentage.setPrefSize(150,15);
        enterPercentage.setLayoutX(50);
        enterPercentage.setLayoutY(250);
        String[] savingPercentage = {""};
        enterPercentage.textProperty().addListener((observable, oldValue, newValue) -> {
        savingPercentage[0] = newValue.trim();
        });
        
        Button confirm=new Button("Confirm");
        confirm.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        confirm.setFont(Font.font("Anton", 15));
        confirm.setPrefSize(100,20);
        confirm.setLayoutX(220);
        confirm.setLayoutY(250);
        confirm.setOnAction(e-> {
            Label savingSuccesful=new Label(savingPercentage[0]+"% will be deducted on next debit as saving.");
            popupMessage(savingSuccesful);
        });
        
        pane.getChildren().addAll(enterPercentage,confirm);
    }
    
// predicted deposit
    static String predictedDepositfilepath= "/Users/cye/NewFolder/InsideOut/src/predictDeposit.csv";
    static BufferedReader reader=null;
    static BufferedWriter writer=null;
    
    public void userInList(){
        String line="";
       try(BufferedReader reader=new BufferedReader(new FileReader(predictedDepositfilepath))){
           inlist:{
               while((line=reader.readLine())!=null){
                   String[]word=line.split(",");
                   if(word[0].equals(Username)){
                       break inlist;
                   }
                   else {
                       break;
                   }
               }
               
               try(BufferedWriter writer=new BufferedWriter(new FileWriter(predictedDepositfilepath,true))){
                   writer.write(Username);
                   writer.newLine();
                   break inlist;
               }catch(IOException ex){
                   ex.printStackTrace();
               }
           }
       }catch(IOException ex){
           ex.printStackTrace();
       }
    }
       
    public void depositCalculator(String bank,Label showDeposit,AnchorPane pane){
        PredictedDeposit deposit=new PredictedDeposit(bank,Username);
        double predictedDeposit=deposit.getDeposit();

    showDeposit.setText("RM " + String.format("%.2f", predictedDeposit));
    showDeposit.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
    showDeposit.setFont(Font.font("Anton", 23));  // Set the font family and size here
    showDeposit.setLayoutX(50);
    showDeposit.setLayoutY(260);
    
    if (!pane.getChildren().contains(showDeposit)) {
    pane.getChildren().add(showDeposit);// Add the button only if it's not already in the parent
    }
    else{
        pane.getChildren().remove(showDeposit);
        pane.getChildren().add(showDeposit);
    }
    
}
     
// pop up message
    public static void popupMessage(Label label) {
     label.setStyle("-fx-text-fill:black;");
     label.setFont(Font.font("Anton", 15));
     label.setLayoutX(110);
     label.setLayoutY(50);
    Stage popupStage = new Stage();
    StackPane root = new StackPane(label);

    Scene scene = new Scene(root,300,100);
    popupStage.setScene(scene);
    popupStage.show();

    PauseTransition delay = new PauseTransition(Duration.seconds(2));
    delay.setOnFinished(event -> popupStage.close());
    delay.play();
}
   
 // main method
    public static void main (String[] args){
        launch(args);
    }
    
}