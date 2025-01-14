package insideout;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.util.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.*;
import javafx.scene.chart.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.concurrent.Task;

public class InsideOut extends Application {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static boolean isUser = false;
    public static String Username = "";
    public static double debitTotal = 0.0;
    public static double creditTotal = 0.0;
    private static boolean debitcategorySelected = false;
    private static boolean creditcategorySelected = false;

    private static TableView<Transaction> tableViewOverview = new TableView<>();
    private static TableView<Transaction> tableViewDebit = new TableView<>();
    private static TableView<Transaction> tableViewCredit = new TableView<>();
    private static TableView<Transaction> tableViewLoanApplied = new TableView<>();

    private static ArrayList<Node> clearNodes = new ArrayList<>();
    private static final double ASPECT_RATIO = 7 / 4;

    protected static boolean userStatus = false;
    private boolean overdue = false;

    @Override
    public void start(Stage primaryStage) {
        URL hourglass = getClass().getResource("/resources/hourglass.png");
        URL clover = getClass().getResource("/resources/clover.png");
        URL coin = getClass().getResource("/resources/coin.png");
        URL comp = getClass().getResource("/resources/comp.png");
        URL file = getClass().getResource("/resources/file.png");
        URL logo = getClass().getResource("/resources/insideoutlogo.png");
        URL key = getClass().getResource("/resources/key.png");
        URL lock = getClass().getResource("/resources/lock.png");
        URL money = getClass().getResource("/resources/money.png");
        URL moneybag = getClass().getResource("/resources/moneybag.png");
        URL moneyfly = getClass().getResource("/resources/moneyfly.png");
        URL piggybank = getClass().getResource("/resources/piggybank.png");
        URL purse = getClass().getResource("/resources/purse.png");
        URL questionmark = getClass().getResource("/resources/questionmark.png");
        URL star = getClass().getResource("/resources/star.png");
        URL tree = getClass().getResource("/resources/tree.png");

        if (hourglass == null) System.err.println("Resource not found: hourglass.png");
        if (clover == null) System.err.println("Resource not found: clover.png");
        if (coin == null) System.err.println("Resource not found: coin.png");
        if (comp == null) System.err.println("Resource not found: comp.png");
        if (file == null) System.err.println("Resource not found: file.png");
        if (logo == null) System.err.println("Resource not found: insideoutlogo.png");
        if (key == null) System.err.println("Resource not found: key.png");
        if (lock == null) System.err.println("Resource not found: lock.png");
        if (money == null) System.err.println("Resource not found: money.png");
        if (moneybag == null) System.err.println("Resource not found: moneybag.png");
        if (moneyfly == null) System.err.println("Resource not found: moneyfly.png");
        if (piggybank == null) System.err.println("Resource not found: piggybank.png");
        if (purse == null) System.err.println("Resource not found: purse.png");
        if (questionmark == null) System.err.println("Resource not found: questionmark.png");
        if (star == null) System.err.println("Resource not found: star.png");
        if (tree == null) System.err.println("Resource not found: tree.png");

        if (hourglass == null || clover == null || coin == null || comp == null ||
                file == null || logo == null || key == null || lock == null ||
                money == null || moneybag == null || moneyfly == null || piggybank == null ||
                purse == null || questionmark == null || star == null || tree == null) {
            System.err.println("One or more resources are missing. Please check the resources folder.");
        }

// anchorpane and scene
        StackPane stackpane = new StackPane();
        AnchorPane homepage = new AnchorPane();
        Scene pagehomepage = new Scene(homepage, 700, 400);

        AnchorPane registration = new AnchorPane();
        Scene pageregistration = new Scene(registration, 700, 400);

        AnchorPane logIn = new AnchorPane();
        Scene pagelogin = new Scene(logIn, 700, 400);

        AnchorPane mainPage = new AnchorPane();
        Scene pagemainPage = new Scene(mainPage, 700, 400);

        AnchorPane debit = new AnchorPane();
        Scene pagedebit = new Scene(debit, 700, 400);

        AnchorPane credit = new AnchorPane();
        Scene pagecredit = new Scene(credit, 700, 400);

        AnchorPane history = new AnchorPane();
        Scene pagehistory = new Scene(history, 700, 400);

        AnchorPane saving = new AnchorPane();
        Scene pagesaving = new Scene(saving, 700, 400);
        StackPane spSaving = new StackPane();

        AnchorPane creditloan = new AnchorPane();
        Scene pagecreditloan = new Scene(creditloan, 700, 400);
        StackPane spCreditLoan = new StackPane();

        AnchorPane applyLoan = new AnchorPane();
        Scene pageapplyLoan = new Scene(applyLoan, 700, 400);
        StackPane spApply = new StackPane();

        AnchorPane repay = new AnchorPane();
        Scene pagerepay = new Scene(repay, 700, 400);
        StackPane spRepay = new StackPane();

        AnchorPane predicteddeposit = new AnchorPane();
        Scene pagedeposit = new Scene(predicteddeposit, 700, 400);
        StackPane spDeposit = new StackPane();

        AnchorPane viewGraph = new AnchorPane();
        Scene pageViewGraph = new Scene(viewGraph, 700, 400);

        AnchorPane viewBalance = new AnchorPane();
        Scene pageViewBalance = new Scene(viewBalance, 700, 400);

        AnchorPane viewLoanHistory = new AnchorPane();
        Scene pageviewLoanHistory = new Scene(viewLoanHistory, 700, 400);

// homepage (registration && login page)

        homepage.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(homepage);
        Button register = new Button("Register");
        Button login = new Button("Log In");
        buttonfontsize(register);
        buttonfontsize(login);
        register.setPrefSize(100, 35);
        login.setPrefSize(100, 35);
        AnchorPane.setTopAnchor(register, 300.0);
        AnchorPane.setLeftAnchor(register, 200.0);
        AnchorPane.setLeftAnchor(login, 400.0);
        AnchorPane.setTopAnchor(login, 300.0);
        Label insideout = new Label("Inside Out");
        insideout.setFont(Font.font("Anton", 90));
        AnchorPane.setTopAnchor(insideout, 50.0);
        AnchorPane.setLeftAnchor(insideout, 160.0);
        pagehomepage.setFill(Color.web("#a8c4f4"));

        Image piggybankimg = new Image(piggybank.toString());
        ImageView piggybankview = new ImageView(piggybankimg);
        piggybankview.setFitWidth(75);
        piggybankview.setFitHeight(75);
        Image moneyimg = new Image(money.toString());
        ImageView moneyview = new ImageView(moneyimg);
        moneyview.setFitWidth(75);
        moneyview.setFitHeight(75);
        Image moneybagimg = new Image(moneybag.toString());
        ImageView moneybagview = new ImageView(moneybagimg);
        moneybagview.setFitWidth(75);
        moneybagview.setFitHeight(75);
        Image coinimg = new Image(coin.toString());
        ImageView coinview = new ImageView(coinimg);
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

        stackpane.getChildren().addAll(register, login, insideout, piggybankview, moneybagview, moneyview);
        homepage.getChildren().addAll(insideout, register, login, piggybankview, moneybagview, moneyview, coinview);


// registration page 
        registration.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(registration);
        Label steps = new Label("STEPS TO REGISTER :");
        steps.setFont(Font.font("Anton", 23));
        Label step1 = new Label("1. SET USERNAME BY CLICKING ON " + "\nType your username...");
        step1.setFont(Font.font("Anton", 16));
        Label step2 = new Label("2. SET EMAIL BY CLICKING ON " + "\nType your email...");
        step2.setFont(Font.font("Anton", 16));
        Label step3 = new Label("3. SET PASSWORD BY CLICKING ON " + "\nType your password...");
        step3.setFont(Font.font("Anton", 16));
        Label step4 = new Label("4. CLICK THE Confirm BUTTON BELOW TO SAVE ");
        step4.setFont(Font.font("Anton", 16));
        Label step5 = new Label("*PASSWORD SHOULD CONSIST OF\n1 UPPERCASE 1 SPECIAL CHARACTER AND LENGTH OF 8*");
        step5.setFont(Font.font("Anton", 13));
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
        Label registrationtitle = new Label("REGISTRATION");
        registrationtitle = header(registrationtitle, registration);

        Button backtoMain = new Button("Home Page");
        AnchorPane.setTopAnchor(backtoMain, 305.0);
        AnchorPane.setLeftAnchor(backtoMain, 490.0);
        backtoMain.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        backtoMain.setFont(Font.font("Anton", 17));
        backtoMain.setPrefSize(100, 15);
        backtoMain.setOnAction(e -> primaryStage.setScene(pagehomepage));

        // registration guidelines
        yellowrec(registration);

        Button tologinpage = new Button("Confirm");
        tologinpage.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        tologinpage.setFont(Font.font("Anton", 17));
        tologinpage.setLayoutX(375);
        tologinpage.setLayoutY(305);
        tologinpage.setPrefSize(100, 15);


        // method call
        Label username = instruction(100, "username");
        TextField inputusername = input("Enter your username :", "username", 100.0, 50.0);
        Label email = instruction(180, "email");
        TextField inputemail = input("Enter your email :", "email", 180.0, 50.0);
        Label password = instruction(260, "password");
        TextField inputpassword = input("Enter your password:", "password", 270.0, 50.0);

        String[] registername = {""};
        inputusername.textProperty().addListener((observable, oldValue, newValue) -> {
            registername[0] = newValue.trim();
        });
        String[] registeremail = {""};
        inputemail.textProperty().addListener((observable, oldValue, newValue) -> {
            registeremail[0] = newValue.trim();
        });
        String[] registerpassword = {""};
        inputpassword.textProperty().addListener((observable, oldValue, newValue) -> {
            registerpassword[0] = newValue.trim();
        });


        clearNodes.add(inputusername);
        clearNodes.add(inputemail);
        clearNodes.add(inputpassword);

        registration.getChildren().addAll(inputusername, inputpassword, inputemail, tologinpage, steps, step1, step2, step3, step4, step5
                , registrationtitle, username, email, password, backtoMain);
        pageregistration.setFill(Color.web("#a8c4f4"));

// log in page
        whiterec(logIn);
        loginrec(logIn);
        logIn.setStyle("-fx-background-color: #a8c4f4;");
        Label logintitle = new Label("LOG IN");
        logintitle = header(logintitle, logIn);

        Button loginbtn = new Button("Log In");
        buttonfontsize(loginbtn);
        AnchorPane.setTopAnchor(loginbtn, 295.0);
        AnchorPane.setLeftAnchor(loginbtn, 350.0);

        Button backtoMainPage = new Button("Home Page");
        buttonfontsize(backtoMainPage);
        AnchorPane.setTopAnchor(backtoMainPage, 295.0);
        AnchorPane.setLeftAnchor(backtoMainPage, 480.0);
        backtoMainPage.setOnAction(e -> primaryStage.setScene(pagehomepage));

        Image keyimg = new Image(key.toString());
        ImageView keyview = new ImageView(keyimg);
        keyview.setFitWidth(50);
        keyview.setFitHeight(50);
        keyview.setLayoutX(185);
        keyview.setLayoutY(40);


        // method call
        username = instruction(100, "username");
        TextField loginusername = input("Enter your username :", "username", 100.0, 50.0);
        email = instruction(180, "email");
        TextField loginemail = input("Enter your email :", "email", 180.0, 50.0);
        password = instruction(260, "password");
        TextField loginpassword = input("Enter your password:", "password", 270.0, 50.0);

        pagelogin.setFill(Color.web("#a8c4f4"));

        clearNodes.add(loginusername);
        clearNodes.add(loginemail);
        clearNodes.add(loginpassword);


        String[] name = {""};
        loginusername.textProperty().addListener((observable, oldValue, newValue) -> {
            name[0] = newValue.trim();
        });

        String[] useremail = {""};
        loginemail.textProperty().addListener((observable, oldValue, newValue) -> {
            useremail[0] = newValue.trim();
        });

        String[] userpassword = {""};
        loginpassword.textProperty().addListener((observable, oldValue, newValue) -> {
            userpassword[0] = newValue.trim();
        });


        logIn.getChildren().addAll(loginbtn, username, loginusername, loginpassword, password, loginemail, email, logintitle, keyview, backtoMainPage);


// main page
        whiterec(mainPage);
        pagemainPage.setFill(Color.web("#a8c4f4"));
        mainPage.setStyle("-fx-background-color: #a8c4f4;");

        tologinpage.setOnAction(e -> {
            register(registername[0], registeremail[0], registerpassword[0]);
            if (registrationValid == true) {
                primaryStage.setScene(pagelogin);
                Label lbl = new Label("Registration Succesful.");
                popupMessage(lbl);
            }
        }); // registration to log in page

        Label usernamelbl[] = new Label[1];
        usernamelbl[0] = new Label();
        loginbtn.setOnAction(e -> {

            removeLabelById(mainPage, "usernameLabel");
            removeLabelById(mainPage, "useridLabel");
            Username = logIn(name[0], useremail[0], userpassword[0], mainPage);
            if (isUser == true) {
                primaryStage.setScene(pagemainPage);
                // send reminder if there is loan dueing
                Repayment check = new Repayment(Username);
                check.updateStatus();
                check.checkDeduction();
                check.checkOverdue();
                this.overdue = check.getOverdue(); // check if there is any overdue loan
                if (overdue == true) {
                    alertMessage("OverdueLoan", "Overdue Loan Found", "Please make repayment now.\nDebit and Credit function will be disabled\nuntil the loan is fully paid!");
                }

                // add savings into balance when reaches end of month
                Savings endmonth = new Savings(Username);
                endmonth.isEndOfMonth(Username);
                Reminder reminder = new Reminder(Username);
                reminder.checkLoan();

                Task<Void> reminderTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {

                        if (reminder.getActiveLoanSize() > 0) {
                            reminder.reminderNotification();
                        }

                        if (reminder.getOverdueLoansSize() > 0) {
                            reminder.overdueLoanNotification();
                        }

                        return null;
                    }

                    @Override
                    protected void failed() {
                        System.err.println("Failed to send reminder email: " + getException().getMessage());
                    }
                };

                new Thread(reminderTask).start();

                if (reminder.getActiveLoanSize() > 0) {
                    alertMessage("Loan Repayment", "Auto Monthly Loan Repayment", "Reminder!\nAuto-deduction from balance will be taken to pay for loan(s)");
                }

                usernamelbl[0] = new Label(Username);
                usernamelbl[0].setId("usernameLabel");
                usernamelbl[0].setFont(Font.font("Anton", 50));
                AnchorPane.setTopAnchor(usernamelbl[0], 100.0);
                AnchorPane.setLeftAnchor(usernamelbl[0], 100.0);
                mainPage.getChildren().add(usernamelbl[0]);
            }
        }); //from login to mainpage

        currenttime(mainPage);
        Button debitbtn = new Button("Debit");
        Button creditbtn = new Button("Credit");
        Button savingbtn = new Button("Savings");
        Button historybtn = new Button("History");
        Button creditloanbtn = new Button("Credit Loan");
        Button interestpredictorbtn = new Button("Deposit Interest Predictor");
        Button viewGraphsbtn = new Button("Graphs");
        Button logoutbtn = new Button("Log Out");

        mainpagebtn(debitbtn, 260, 20, 183, 75);
        mainpagebtn(creditbtn, 260, 20, 183, 365);
        mainpagebtn(savingbtn, 260, 20, 223, 75);
        mainpagebtn(historybtn, 260, 20, 223, 365);
        mainpagebtn(creditloanbtn, 260, 20, 263, 75);
        mainpagebtn(interestpredictorbtn, 260, 20, 263, 365);
        mainpagebtn(viewGraphsbtn, 260, 20, 303, 75);
        mainpagebtn(logoutbtn, 260, 20, 303, 365);


        Rectangle menu = new Rectangle(607, 180);  // Width, Height
        menu.setFill(Color.web("#FEEBA8"));  // Fill the rectangle with blue color
        menu.setArcWidth(20);     // Horizontal radius of the corner
        menu.setArcHeight(20);    // Vertical radius of the corner
        menu.setLayoutX(45);
        menu.setLayoutY(170);

        Label welcome = new Label("Welcome ");// get username afterward
        welcome = header(welcome, mainPage);
        welcome.setFont(Font.font("Anton", 60));


        mainPage.getChildren().addAll(menu, welcome, debitbtn, creditbtn, historybtn, creditloanbtn, interestpredictorbtn, logoutbtn, savingbtn, viewGraphsbtn);

// debit page

        whiterec(debit);

        debit.setStyle("-fx-background-color: #a8c4f4;");
        pagedebit.setFill(Color.web("#a8c4f4"));
        Label debittitle = new Label("Debit");
        debittitle = header(debittitle, debit);
        Label amountinstruction = instruction(100, "Debit Amount");
        Label descriptioninstruction = instruction(160, "Description");

        // to enter amount of debit
        // Create the TextField for amount debit
        TextField amountdebit = input("Enter Debit Amount:", "Debit Amount", 100.0, 50.0);
        TextArea descriptiond = description("Enter Description:", "Description", 185.0, 50.0);

        clearNodes.add(amountdebit);
        clearNodes.add(descriptiond);

        String[] descriptiondstr = {""};
        descriptiond.textProperty().addListener((observable, oldValue, newValue) -> {
            descriptiondstr[0] = newValue; // Update the description dynamically as user types
        });

        Label select = new Label();
        select.setText("Click on a button which best describe the transaction");
        select.setFont(Font.font("Anton", 15));
        select.setLayoutX(50);
        select.setLayoutY(250);

        Label categoryselected = new Label();
        categoryselected.setText("Category Selected :");
        categoryselected.setFont(Font.font("Anton", 15));
        categoryselected.setLayoutX(50);
        categoryselected.setLayoutY(280);

        selectCategory(debit);

        Button confirmdebit = new Button("Confirm");
        confirmdebit.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        confirmdebit.setFont(Font.font("Anton", 20));
        confirmdebit.setLayoutX(500);
        confirmdebit.setLayoutY(300);


        confirmdebit.setOnAction(e -> {
            ApplyLoan checkOverdue = new ApplyLoan();
            checkOverdue.setUsername(Username);
            boolean overdued = checkOverdue.findOverdue();
            if (overdued == false) {
                final String input = amountdebit.getText(); // Get the text entered by the user in amountdebit TextField
                try {
                    int descriptiondlength = descriptiondstr[0].split("\\s+").length;
                    String category = getCat();

                    boolean nocat = false;
                    boolean descriptionword = false;
                    boolean nodescriptiond = false;
                    if (descriptiondlength > 200) {
                        descriptionword = true;
                    } else if (descriptiondstr[0].isEmpty()) {
                        nodescriptiond = true;
                    }

                    if (category.equals("")) {
                        nocat = true;
                    }

                    if (descriptionword == false && nodescriptiond == false && nocat == false) {
                        double debitamount = Double.parseDouble(input);
                        if (descriptiondstr[0].contains(",")) {
                            descriptiondstr[0] = formatCSV(descriptiondstr[0]);
                        }

                        Debit(debitamount, descriptiondstr[0], "Debit", category);
                    } else if (descriptionword == true) {
                        Label wordcount = new Label("Description need to be less than 200 word!");
                        popupMessage(wordcount);
                    } else if (nodescriptiond == true) {
                        Label enterd = new Label("Enter Description!");
                        popupMessage(enterd);
                    } else if (nocat == true) {
                        Label selectcat = new Label("Select Category");
                        popupMessage(selectcat);
                    }

                } catch (Exception ex) {
                    Label wrongcashformat = new Label("Wrong Cash Format eg.1000");
                    popupMessage(wrongcashformat);
                    ex.printStackTrace();
                }
            } else {
                Label lbl = new Label("Clear Loan Repayment Before Making other Transactions.");
                popupMessage(lbl);
            }
            clearAllNodes(clearNodes);
        });

        ImageView piggybankdebitimg = new ImageView(piggybankimg);
        piggybankdebitimg.setLayoutX(150);
        piggybankdebitimg.setLayoutY(40);
        piggybankdebitimg.setFitWidth(50);
        piggybankdebitimg.setFitHeight(50);
        ImageView coindebitimg = new ImageView(coinimg);
        coindebitimg.setLayoutX(185);
        coindebitimg.setLayoutY(42);
        coindebitimg.setFitWidth(60);
        coindebitimg.setFitHeight(60);


        debit.getChildren().addAll(categoryselected, select, confirmdebit, amountdebit, descriptiond, debittitle, amountinstruction, descriptioninstruction, piggybankdebitimg, coindebitimg);


// credit page
        credit.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(credit);
        pagecredit.setFill(Color.web("#a8c4f4"));
        Label credittitle = new Label("Credit");
        credittitle = header(credittitle, credit);
        amountinstruction = instruction(100, "Credit Amount");
        descriptioninstruction = instruction(160, "Description");

        Image moneyflyimg = new Image(moneyfly.toString());
        ImageView moneyflyview = new ImageView(moneyflyimg);
        moneyflyview.setLayoutX(160);
        moneyflyview.setLayoutY(40);
        moneyflyview.setFitWidth(60);
        moneyflyview.setFitHeight(50);

        ImageView coincredit = new ImageView(coinimg);
        coincredit.setLayoutX(205);
        coincredit.setLayoutY(40);
        coincredit.setFitWidth(60);
        coincredit.setFitHeight(60);

        // to enter amount of debit/credit
        TextField amountcredit = input("Enter Debit Amount:", "Credit Amount", 100.0, 50.0);
        TextArea descriptionc = description("Enter Description:", "Description", 185.0, 50.0);

        String[] descriptioncstr = {""};
        descriptionc.textProperty().addListener((observable, oldValue, newValue) -> {
            descriptioncstr[0] = newValue; // Update the description dynamically as user types
        });

        clearNodes.add(amountcredit);
        clearNodes.add(descriptionc);

        Label selectcredit = new Label();
        selectcredit.setText("Click on a button which best describe the transaction");
        selectcredit.setFont(Font.font("Anton", 15));
        selectcredit.setLayoutX(50);
        selectcredit.setLayoutY(250);

        Label categoryselectedcredit = new Label();
        categoryselectedcredit.setText("Category Selected :");
        categoryselectedcredit.setFont(Font.font("Anton", 15));
        categoryselectedcredit.setLayoutX(50);
        categoryselectedcredit.setLayoutY(280);


        selectCategory(credit);


        Button confirmcredit = new Button("Confirm");
        confirmcredit.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        confirmcredit.setFont(Font.font("Anton", 20));
        confirmcredit.setLayoutX(500);
        confirmcredit.setLayoutY(300);

            confirmcredit.setOnAction(e -> {
                ApplyLoan checkOverdue = new ApplyLoan();
                checkOverdue.setUsername(Username);
                boolean overdued = checkOverdue.findOverdue();
                if (overdued == false) {
                    final String input = amountcredit.getText(); // Get the text entered by the user in amountdebit TextField
                    try {
                        String categoryCredit = getCat();
                        boolean descriptionword = false;
                        boolean nodecriptionc = false;
                        boolean nocategory = false;

                        if (descriptioncstr[0].split("\\s+").length > 200) {
                            descriptionword = true;
                        } else if (descriptioncstr[0].isEmpty()) {
                            nodecriptionc = true;
                        }

                        if (categoryCredit.equals("")) {
                            nocategory = true;
                        }

                        if (descriptionword == false && nodecriptionc == false && nocategory == false) {
                            if (descriptioncstr[0].contains(",")) {
                                descriptioncstr[0] = formatCSV(descriptioncstr[0]);
                            }
                            double creditamount = Double.parseDouble(input);
                            Credit(creditamount, descriptioncstr[0], "Credit", categoryCredit, overdue);
                        } else if (descriptionword == true) {
                            Label wordcount = new Label("Description need to be less than 200 word!");
                            popupMessage(wordcount);
                        } else if (nodecriptionc == true) {
                            Label enterdes = new Label("Enter Description!");
                            popupMessage(enterdes);
                        } else if (nocategory == true) {
                            Label selectCategory = new Label("Select Category");
                            popupMessage(selectCategory);
                        }


                    } catch (Exception ex) {
                        Label wrongcashformat = new Label("Wrong Cash Format eg.1000");
                        popupMessage(wrongcashformat);
                        ex.printStackTrace();
                    }
                } else {
                    Label lbl = new Label("Clear Loan Repayment Before Making other Transactions.");
                    popupMessage(lbl);
                }
                clearAllNodes(clearNodes);
            });

        credit.getChildren().addAll(amountcredit, descriptionc, credittitle, amountinstruction, descriptioninstruction,
                confirmcredit, categoryselectedcredit, selectcredit, moneyflyview, coincredit);

// history page     

        history.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(history);
        pagehistory.setFill(Color.web("#a8c4f4"));
        Label historytitle = new Label("History");
        historytitle = header(historytitle, history);
        historytitle.toFront();
        historytitle.setVisible(true);
        history.getChildren().add(historytitle);

        Image hourglassimg = new Image(hourglass.toString());
        ImageView hourglassview = new ImageView(hourglassimg);
        hourglassview.setLayoutX(200);
        hourglassview.setLayoutY(40);
        hourglassview.setFitWidth(60);
        hourglassview.setFitHeight(50);

        TabPane tabPane = new TabPane();
        tabPane.setPrefWidth(635);
        tabPane.setPrefHeight(258);
        AnchorPane.setTopAnchor(tabPane, 107.0);
        AnchorPane.setLeftAnchor(tabPane, 30.0);

        // overview
        Tab overviewTab = new Tab("Overview");
        overviewTab.setClosable(false); // Disables the close button

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

        // debit
        Tab debitTab = new Tab("Debit");
        debitTab.setClosable(false); // Disables the close button
        TableColumn<Transaction, String> TransactionIDDebit = new TableColumn<>("TransactionID");
        TransactionIDDebit.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        TransactionIDDebit.setPrefWidth(113);

        TableColumn<Transaction, String> TimeDebit = new TableColumn<>("Date");
        TimeDebit.setCellValueFactory(new PropertyValueFactory<>("date"));
        TimeDebit.setPrefWidth(183);

        TableColumn<Transaction, String> AmountDebit = new TableColumn<>("Amount");
        AmountDebit.setCellValueFactory(new PropertyValueFactory<>("amount"));
        AmountDebit.setPrefWidth(109);

        TableColumn<Transaction, String> DescriptionDebit = new TableColumn<>("Description");
        DescriptionDebit.setCellValueFactory(new PropertyValueFactory<>("description"));
        DescriptionDebit.setPrefWidth(230);

        TableColumn<Transaction, String> deductstatus = new TableColumn<>("Deducted");
        deductstatus.setCellValueFactory(new PropertyValueFactory<>("deducted"));
        deductstatus.setPrefWidth(230);

        debitTab.setContent(tableViewDebit);

        // credit
        Tab creditTab = new Tab("Credit");
        creditTab.setClosable(false); // Disables the close button
        TableColumn<Transaction, String> TransactionIDCredit = new TableColumn<>("TransactionID");
        TransactionIDCredit.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        TransactionIDCredit.setPrefWidth(113);


        TableColumn<Transaction, String> TimeCredit = new TableColumn<>("Date");
        TimeCredit.setCellValueFactory(new PropertyValueFactory<>("date"));
        TimeCredit.setPrefWidth(183);

        TableColumn<Transaction, String> AmountCredit = new TableColumn<>("Amount");
        AmountCredit.setCellValueFactory(new PropertyValueFactory<>("amount"));
        AmountCredit.setPrefWidth(109);

        TableColumn<Transaction, String> DescriptionCredit = new TableColumn<>("Description");
        DescriptionCredit.setCellValueFactory(new PropertyValueFactory<>("description"));
        DescriptionCredit.setPrefWidth(230);

        TableColumn<Transaction, String> categoryCredit = new TableColumn<>("Category");
        categoryCredit.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCredit.setPrefWidth(230);

        creditTab.setContent(tableViewCredit);

        // loan applied
        Tab appliedloanTab = new Tab("Loan Applied");
        appliedloanTab.setClosable(false); // Disables the close button
        TableColumn<Transaction, String> LoanID = new TableColumn<>("LoanID");
        LoanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        LoanID.setPrefWidth(90);

        TableColumn<Transaction, String> Principal = new TableColumn<>("Principal");
        Principal.setCellValueFactory(new PropertyValueFactory<>("Principal"));
        Principal.setPrefWidth(90);

        TableColumn<Transaction, String> Interest = new TableColumn<>("Interest Rate");
        Interest.setCellValueFactory(new PropertyValueFactory<>("Interest"));
        Interest.setPrefWidth(90);

        TableColumn<Transaction, String> TotalLoan = new TableColumn<>("Total Loan");
        TotalLoan.setCellValueFactory(new PropertyValueFactory<>("totalLoan"));
        TotalLoan.setPrefWidth(90);

        TableColumn<Transaction, String> outstandingBalance = new TableColumn<>("Outstanding Balance");
        outstandingBalance.setCellValueFactory(new PropertyValueFactory<>("outstandingBalance"));
        outstandingBalance.setPrefWidth(90);

        TableColumn<Transaction, String> period = new TableColumn<>("Period");
        period.setCellValueFactory(new PropertyValueFactory<>("period"));
        period.setPrefWidth(70);

        TableColumn<Transaction, String> appliedTime = new TableColumn<>("Apply Time");
        appliedTime.setCellValueFactory(new PropertyValueFactory<>("applyTime"));
        appliedTime.setPrefWidth(200);

        TableColumn<Transaction, String> dueTime = new TableColumn<>("Due Time");
        dueTime.setCellValueFactory(new PropertyValueFactory<>("dueTime"));
        dueTime.setPrefWidth(200);

        TableColumn<Transaction, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setPrefWidth(70);

        appliedloanTab.setContent(tableViewLoanApplied);

        tableViewOverview.getColumns().addAll(TransactionIDOverview, TimeOverview, AmountOverview, DescriptionOverview);
        tableViewDebit.getColumns().addAll(TransactionIDDebit, TimeDebit, AmountDebit, DescriptionDebit, deductstatus);
        tableViewCredit.getColumns().addAll(TransactionIDCredit, TimeCredit, AmountCredit, DescriptionCredit, categoryCredit);
        tableViewLoanApplied.getColumns().addAll(LoanID, Principal, Interest, TotalLoan, outstandingBalance, period, appliedTime, dueTime, status);
        tabPane.getTabs().addAll(overviewTab, debitTab, creditTab, appliedloanTab);

        history.getChildren().addAll(tableViewOverview, tableViewDebit, tableViewCredit, tabPane, hourglassview, tableViewLoanApplied);

        pagehistory.setFill(Color.web("#a8c4f4"));


// saving page
        saving.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(saving);
        pagesaving.setFill(Color.web("#a8c4f4"));
        Label savingtitle = new Label("Saving");
        savingtitle = header(savingtitle, saving);

        Label savingactivationlbl = new Label("Are you sure to activate it?");
        savingactivationlbl.setLayoutX(50);
        savingactivationlbl.setLayoutY(85);
        savingactivationlbl.setFont(Font.font("Anton", 35));

        Label enterSavingPercentagelbl = new Label("Enter percentage(%) to be deducted from the next debit :");
        enterSavingPercentagelbl.setLayoutX(50);
        enterSavingPercentagelbl.setLayoutY(200);
        enterSavingPercentagelbl.setFont(Font.font("Anton", 25));
        enterSavingPercentagelbl.setVisible(false);
        enterSavingPercentagelbl.setManaged(false);

        Image treeimg = new Image(tree.toString());
        ImageView treeview = new ImageView(treeimg);
        treeview.setLayoutX(200);
        treeview.setLayoutY(40);
        treeview.setFitWidth(60);
        treeview.setFitHeight(50);


        Button yes = new Button("Yes");
        yes.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        yes.setFont(Font.font("Anton", 15));
        yes.setPrefSize(50, 15);
        yes.setLayoutX(50);
        yes.setLayoutY(150);
        yes.setOnAction(e -> {
            enterSavingPercentagelbl.setVisible(true);
            enterSavingPercentagelbl.setManaged(true);
            enterPercentage(saving);
        });


        Button no = new Button("No");
        no.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        no.setFont(Font.font("Anton", 15));
        no.setLayoutX(120);
        no.setLayoutY(150);
        no.setPrefSize(50, 15);
        no.setOnAction(e -> {
            Savings cancel = new Savings(Username);
            cancel.cancelSaving();
            Label selectother = new Label("Next debit will not be deducted for saving.");
            popupMessage(selectother);
        });


        TextField enterPercentage = input("Enter the percentage(%) to be deducted from next debit :", "percentage(%)", 100.0, 50.0);
        clearNodes.add(enterPercentage);


        saving.getChildren().addAll(savingtitle, yes, no, savingactivationlbl, enterSavingPercentagelbl, treeview);

// credit loan page
        creditloan.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(creditloan);
        pagecreditloan.setFill(Color.web("#a8c4f4"));
        Label creditloantitle = new Label("Credit Loan");
        creditloantitle = header(creditloantitle, creditloan);

        Button applyLoanbtn = new Button("Apply");
        Button repaybtn = new Button("Repay");
        AnchorPane.setTopAnchor(applyLoanbtn, 150.0);
        AnchorPane.setLeftAnchor(applyLoanbtn, 60.0);
        AnchorPane.setTopAnchor(repaybtn, 150.0);
        AnchorPane.setLeftAnchor(repaybtn, 150.0);
        applyLoanbtn.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        applyLoanbtn.setFont(Font.font("Anton", 20));
        repaybtn.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        repaybtn.setFont(Font.font("Anton", 20));


        Label repayOrapply = new Label("Click Apply button to Apply Credit Loan or Repay button to Pay Credit Loan.");
        AnchorPane.setTopAnchor(repayOrapply, 107.0);
        AnchorPane.setLeftAnchor(repayOrapply, 50.0);
        repayOrapply.setStyle("-fx-text-fill:black;");
        repayOrapply.setFont(Font.font("Anton", 20));

        Rectangle orangerec = new Rectangle(590, 130);
        orangerec.setFill(Color.web("#F37400"));
        orangerec.setArcWidth(20);
        orangerec.setArcHeight(20);
        AnchorPane.setTopAnchor(orangerec, 215.0);
        AnchorPane.setLeftAnchor(orangerec, 50.0);

        Label note = new Label("!! Note !!");
        AnchorPane.setTopAnchor(note, 225.0);
        AnchorPane.setLeftAnchor(note, 80.0);
        note.setStyle("-fx-text-fill:white;");
        note.setFont(Font.font("Anton", 30));

        Label reminder = new Label("* Debit and Credit action could not be conducted \n  if loan is not paid after due date");
        AnchorPane.setTopAnchor(reminder, 265.0);
        AnchorPane.setLeftAnchor(reminder, 80.0);
        reminder.setStyle("-fx-text-fill:white;");
        reminder.setFont(Font.font("Anton", 20));

        creditloan.getChildren().addAll(orangerec, creditloantitle, repaybtn, applyLoanbtn, repayOrapply, note, reminder);

        // apply loan page

        applyLoan.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(applyLoan);
        pageapplyLoan.setFill(Color.web("#a8c4f4"));
        Label loantitle = new Label("Credit Loan - Application");
        loantitle = header(loantitle, applyLoan);
        applyLoanbtn.setOnAction(e -> primaryStage.setScene(pageapplyLoan));

        Label principallbl = new Label("Principal Amount");
        AnchorPane.setTopAnchor(principallbl, 100.0);
        AnchorPane.setLeftAnchor(principallbl, 50.0);
        principallbl.setStyle("-fx-text-fill:black;");
        principallbl.setFont(Font.font("Anton", 20));

        PeriodSelection(applyLoan);
        Button selectPeriod = new Button("☰");
        AnchorPane.setTopAnchor(selectPeriod, 300.0);
        AnchorPane.setLeftAnchor(selectPeriod, 220.0);
        selectPeriod.setPrefHeight(30.0);
        selectPeriod.setPrefWidth(30.0);
        selectPeriod.setStyle("-fx-text-fill:black;");
        selectPeriod.setFont(Font.font("Anton", 10));
        selectPeriod.setOnAction(e -> showPeriodSelection(applyLoan));

        applyLoan.getChildren().addAll(loantitle, principallbl, selectPeriod);

        // repay page

        repay.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(repay);
        pageapplyLoan.setFill(Color.web("#a8c4f4"));
        Label repaytitle = new Label("Credit Loan - Repay");
        loantitle = header(repaytitle, repay);
        repaybtn.setOnAction(e -> primaryStage.setScene(pagerepay));

        Label selectRepay = new Label("Select Loan to Repay ");
        AnchorPane.setTopAnchor(selectRepay, 100.0);
        AnchorPane.setLeftAnchor(selectRepay, 50.0);
        selectRepay.setStyle("-fx-text-fill:black;");
        selectRepay.setFont(Font.font("Anton", 20));


        chooseLoan(repay);

        Label amountRepaylbl = new Label("Amount");
        AnchorPane.setTopAnchor(amountRepaylbl, 250.0);
        AnchorPane.setLeftAnchor(amountRepaylbl, 50.0);
        amountRepaylbl.setStyle("-fx-text-fill:black;");
        amountRepaylbl.setFont(Font.font("Anton", 20));

        Button viewLoanHistorybtn = new Button("Loan History");
        AnchorPane.setTopAnchor(viewLoanHistorybtn, 30.0);
        AnchorPane.setLeftAnchor(viewLoanHistorybtn, 50.0);
        buttonfontsize(viewLoanHistorybtn);
        viewLoanHistorybtn.setOnAction(e -> primaryStage.setScene(pagehistory));


        repay.getChildren().addAll(repaytitle, selectRepay, amountRepaylbl, viewLoanHistory);

// predited deposit page

        predicteddeposit.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(predicteddeposit);
        pagecreditloan.setFill(Color.web("#a8c4f4"));
        Label deposittitle = new Label("Predicted Deposit");
        deposittitle = header(deposittitle, predicteddeposit);
        Label bank = new Label("Bank :");
        bank.setLayoutX(50);
        bank.setLayoutY(100);
        bank.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        bank.setFont(Font.font("Anton", 30));  // Set the font family and size here

        Image questionmarkimg = new Image(questionmark.toString());
        ImageView questionmarkview = new ImageView(questionmarkimg);
        questionmarkview.setLayoutX(400);
        questionmarkview.setLayoutY(50);
        questionmarkview.setFitWidth(40);
        questionmarkview.setFitHeight(40);

        ImageView coinpredictimg = new ImageView(coinimg);
        coinpredictimg.setFitWidth(60);
        coinpredictimg.setFitHeight(60);
        coinpredictimg.setLayoutX(430);
        coinpredictimg.setLayoutY(40);

        bankSelection(predicteddeposit, spDeposit);

        Button arrow = new Button("↓");
        arrow.setLayoutX(400);
        arrow.setLayoutY(160);
        arrow.setOnAction(e -> showBankSelection(predicteddeposit));

        TextArea displayDeposit = new TextArea();
        displayDeposit.setEditable(false); // Make it read-only
        displayDeposit.setWrapText(true);
        displayDeposit.setLayoutX(50);
        displayDeposit.setLayoutY(100);
        displayDeposit.setPrefWidth(200);
        displayDeposit.setPrefHeight(100); // Adjust height as nee
        Button displayPredictedDepositbtn = new Button("Calculate Predicted Deposit");
        clearNodes.add(displayDeposit);

        predicteddeposit.getChildren().addAll(deposittitle, arrow, bank, coinpredictimg, questionmarkview);
        // bar chart

        viewGraph.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(viewGraph);
        pageViewGraph.setFill(Color.web("#a8c4f4"));
        Label ViewGraph = new Label("Graphs");
        ViewGraph = header(ViewGraph, viewGraph);

        Button trend = new Button("Spending Trend");
        graphsbtn(trend);
        AnchorPane.setTopAnchor(trend, 100.0);
        AnchorPane.setLeftAnchor(trend, 50.0);
        BarChart<String, Number>[] spendingTrend = new BarChart[1]; // initialize barChart here

        CategoryAxis x = new CategoryAxis(); // Category axis for the x-axis
        NumberAxis y = new NumberAxis(); // Number axis for the y-axi
        spendingTrend[0] = new BarChart<>(x, y);
        spendingTrend[0].setVisible(false);
        spendingTrend[0].setManaged(false);


        Button Saving = new Button("Saving Growth");
        graphsbtn(Saving);
        AnchorPane.setTopAnchor(Saving, 140.0);
        AnchorPane.setLeftAnchor(Saving, 50.0);
        BarChart<String, Number>[] savingGrowth = new BarChart[1]; // initialize barChart here


        CategoryAxis xAxis = new CategoryAxis(); // Category axis for the x-axis
        NumberAxis yAxis = new NumberAxis(); // Number axis for the y-axi
        savingGrowth[0] = new BarChart<>(xAxis, yAxis);
        savingGrowth[0].setVisible(false);
        savingGrowth[0].setManaged(false);


        Button loan = new Button("Loan Repayment");
        graphsbtn(loan);
        AnchorPane.setTopAnchor(loan, 180.0);
        AnchorPane.setLeftAnchor(loan, 50.0);
        PieChart[] loanpie = new PieChart[1];
        loanpie[0] = new PieChart();
        loanpie[0].setVisible(false);
        loanpie[0].setManaged(false);

        Button cat = new Button("Spending Category");
        graphsbtn(cat);
        AnchorPane.setTopAnchor(cat, 220.0);
        AnchorPane.setLeftAnchor(cat, 50.0);
        PieChart[] spendingCategory = new PieChart[1];
        spendingCategory[0] = new PieChart();
        spendingCategory[0].setVisible(false);
        spendingCategory[0].setManaged(false);


        spendingTrendCategorybtn[0] = new Button("btn1");
        spendingTrendCategorybtn[1] = new Button("btn2");
        spendingTrendCategorybtn[2] = new Button("btn3");
        spendingTrendCategorybtn[3] = new Button("btn4");
        Saving.setOnAction(e -> {
            hideAllButtons(spendingTrendCategorybtn);
            removeExistingGraphs(viewGraph);
            SavingGrowth savingchart = new SavingGrowth(Username);
            savingGrowth[0] = savingchart.SavingGrowthChart();
            Label lbl = savingchart.getLabel();
            if (!lbl.getText().isEmpty()) {
                popupMessage(lbl);
            } else {
                viewGraph.getChildren().add(savingGrowth[0]);
                clearNodes.add(savingGrowth[0]);
                setupSubmenu(viewGraph);
            }
        });

        cat.setOnAction(e -> {
                    hideAllButtons(spendingTrendCategorybtn);
                    removeExistingGraphs(viewGraph);
                    SpendingCategory spendingcat = new SpendingCategory(Username);
                    spendingCategory[0] = spendingcat.SpendingCategoryChart();
                    Label lbl = spendingcat.getLabel();
                    if (!lbl.getText().isEmpty()) {
                        popupMessage(lbl);
                    } else {
                        viewGraph.getChildren().add(spendingCategory[0]);
                        clearNodes.add(spendingCategory[0]);
                        setupSubmenu(viewGraph);
                    }
                }
        );

        loan.setOnAction(e -> {
                    hideAllButtons(spendingTrendCategorybtn);
                    removeExistingGraphs(viewGraph);
                    LoanRepayment loanrepayment = new LoanRepayment(Username);
                    loanpie[0] = loanrepayment.LoanRepaymentGraph();
                    Label lbl = loanrepayment.getLabel();
                    if (!lbl.getText().isEmpty()) {
                        popupMessage(lbl);
                    } else {
                        viewGraph.getChildren().add(loanpie[0]);
                        clearNodes.add(loanpie[0]);
                        setupSubmenu(viewGraph);
                    }
                }
        );

        String category[] = {""};

        trend.setOnAction(e -> {
            SpendingTrendCategory(viewGraph, spendingTrend[0], spendingCategory[0], savingGrowth[0]);
            removeExistingGraphs(viewGraph);
        });

        viewGraph.getChildren().addAll(ViewGraph, trend, Saving, cat, loan);


// view account balance page
        viewBalance.setStyle("-fx-background-color: #a8c4f4;");
        whiterec(viewBalance);
        pageViewBalance.setFill(Color.web("#a8c4f4"));
        Label viewBalancelbl = new Label("Balance,Savings & Loans");
        viewBalancelbl = header(viewBalancelbl, viewBalance);

        toViewBalance(primaryStage, pageViewBalance, debit, viewBalance);
        toViewBalance(primaryStage, pageViewBalance, credit, viewBalance);
        toViewBalance(primaryStage, pageViewBalance, creditloan, viewBalance);
        toViewBalance(primaryStage, pageViewBalance, saving, viewBalance);


        Label Balance = new Label("Balance :");
        AnchorPane.setTopAnchor(Balance, 90.0);
        AnchorPane.setLeftAnchor(Balance, 50.0);
        Balance.setStyle("-fx-text-fill:black;");
        Balance.setFont(Font.font("Anton", 30));

        Label Savings = new Label("Savings :");
        AnchorPane.setTopAnchor(Savings, 180.0);
        AnchorPane.setLeftAnchor(Savings, 50.0);
        Savings.setStyle("-fx-text-fill:black;");
        Savings.setFont(Font.font("Anton", 30));

        Label Loans = new Label("Loans :");
        AnchorPane.setTopAnchor(Loans, 270.0);
        AnchorPane.setLeftAnchor(Loans, 50.0);
        Loans.setStyle("-fx-text-fill:black;");
        Loans.setFont(Font.font("Anton", 30));


        viewBalance.getChildren().addAll(viewBalancelbl, Balance, Savings, Loans);

        submenu(primaryStage, debit, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, credit, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, history, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, saving, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, creditloan, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, predicteddeposit, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, applyLoan, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, repay, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, viewBalance, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);
        submenu(primaryStage, viewGraph, pagehomepage, pagedebit, pagecredit, pagesaving, pagehistory, pagecreditloan, pagedeposit, pageViewGraph);


// button action (navigation,setScene)
        primaryStage.setScene(pagehomepage);
        register.setOnAction(e -> primaryStage.setScene(pageregistration)); // to registration page
        login.setOnAction(e -> primaryStage.setScene(pagelogin)); // homepage to log in page
        debitbtn.setOnAction(e -> primaryStage.setScene(pagedebit));
        creditbtn.setOnAction(e -> primaryStage.setScene(pagecredit));
        creditloanbtn.setOnAction(e -> primaryStage.setScene(pagecreditloan));
        historybtn.setOnAction(e -> {
            Transaction(Username);
            primaryStage.setScene(pagehistory);
        });
        savingbtn.setOnAction(e -> primaryStage.setScene(pagesaving));
        interestpredictorbtn.setOnAction(e -> primaryStage.setScene(pagedeposit));
        viewGraphsbtn.setOnAction(e -> primaryStage.setScene(pageViewGraph));
        logoutbtn.setOnAction(e -> {
            Username = null;
            loginusername.clear();
            loginemail.clear();
            loginpassword.clear();
            removeLabelById(viewGraph, "balanceLabel");
            isUser = false;
            clearNodes.clear();
            clearNodes.add(loginusername);
            clearNodes.add(loginemail);
            clearNodes.add(loginpassword);

            Label logOut = new Label("Log Out Successfully!");
            popupMessage(logOut);
            primaryStage.setScene(pagehomepage);
        });

        primaryStage.setTitle("InsideOut");
        primaryStage.show();
        // Add listener to maintain aspect ratio
        Image icon = new Image(logo.toString());
        primaryStage.getIcons().add(icon);


    }


// method(s)

// decorations

    public static void whiterec(AnchorPane pane) {
        Rectangle whiterec = new Rectangle(635, 335);
        whiterec.toBack();// Width, Height
        whiterec.setFill(Color.WHITE);  // Fill the rectangle with blue color
        whiterec.setArcWidth(20);     // Horizontal radius of the corner
        whiterec.setArcHeight(20);    // Vertical radius of the corner
        AnchorPane.setTopAnchor(whiterec, 30.0);
        AnchorPane.setLeftAnchor(whiterec, 30.0);
        pane.getChildren().add(whiterec);
    }

    public void yellowrec(AnchorPane pane) {
        Rectangle yellowrec = new Rectangle(320, 300);  // Width, Height
        yellowrec.setFill(Color.web("#FEEBA8"));  // Fill the rectangle with blue color
        yellowrec.setArcWidth(20);     // Horizontal radius of the corner
        yellowrec.setArcHeight(20);    // Vertical radius of the corner
        AnchorPane.setTopAnchor(yellowrec, 45.0);
        AnchorPane.setLeftAnchor(yellowrec, 325.0);
        pane.getChildren().add(yellowrec);
    }

    public void loginrec(AnchorPane pane) {
        Rectangle loginrec = new Rectangle(360, 300);  // Width, Height
        loginrec.setFill(Color.web("#FEEBA8"));  // Fill the rectangle with blue color
        loginrec.setArcWidth(20);     // Horizontal radius of the corner
        loginrec.setArcHeight(20);    // Vertical radius of the corner
        AnchorPane.setTopAnchor(loginrec, 45.0);
        AnchorPane.setLeftAnchor(loginrec, 285.0);
        pane.getChildren().add(loginrec);

        Label main = new Label("InsideOut");
        main.setFont(Font.font("Anton", 35));
        Label slogan = new Label("\"more money                   less money           side\"");
        Label Inside = new Label("INSIDE");
        Label Out = new Label("OUT");
        Inside.setFont(Font.font("Anton", 25));
        Out.setFont(Font.font("Anton", 25));
        slogan.setFont(Font.font("Anton", 18));
        Label forgotpassword = new Label("Forgot Password ?");
        forgotpassword.setFont(Font.font("Anton", 20));
        Label tips = new Label("Tips : Pasword required to be\n1. At Least 1 UpperCase\n2. At Least 1 LowerCase\n3. At Least 1 Special Character");
        tips.setFont(Font.font("Anton", 16));

        main.setLayoutX(400);
        main.setLayoutY(50);
        slogan.setLayoutX(295);
        slogan.setLayoutY(105);
        Inside.setLayoutX(405);
        Inside.setLayoutY(100);
        Out.setLayoutX(565);
        Out.setLayoutY(100);
        forgotpassword.setLayoutX(300);
        forgotpassword.setLayoutY(150);
        tips.setLayoutX(300);
        tips.setLayoutY(185);

        pane.getChildren().addAll(main, slogan, forgotpassword, tips, Inside, Out);
    }

    public Label header(Label label, AnchorPane pane) {
        Label header = label;
        AnchorPane.setTopAnchor(header, 30.0);
        AnchorPane.setLeftAnchor(header, 50.0);
        label.setFont(Font.font("Anton", 45));
        return header;
    }

    // yellow button
    public static void buttonfontsize(Button button) {
        button.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 20));
    }

    public static void graphsbtn(Button button) {
        button.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 15));
        button.setPrefWidth(130.0);
    }

    public static void graphs(BarChart chart) {
        chart.setPrefWidth(480);
        chart.setPrefHeight(330);
        AnchorPane.setLeftAnchor(chart, 170.0);
        AnchorPane.setTopAnchor(chart, 70.0);
        chart.toBack();
    }

    public static void piechart(Node chart) {
        if (chart instanceof Region) { // Region supports setPrefWidth and setPrefHeight
            Region region = (Region) chart;
            region.setPrefWidth(485);
            region.setPrefHeight(340);
        }

        AnchorPane.setLeftAnchor(chart, 200.0);
        AnchorPane.setTopAnchor(chart, 60.0);

    }


    public static void categorybtn(Button button, ListView list) {
        button.setFont(Font.font("Anton", 15));
        button.setStyle("-fx-background-color:#FED760;");
        button.setPrefWidth(150);
        list.setStyle("-fx-background-color:#FED760;");
    }

    // button
    public void mainpagebtn(Button button, double width, double height, double topAnchor, double leftAnchor) {
        button.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 15));
        button.setPrefSize(200, 20);

        button.setPrefSize(width, height);
        AnchorPane.setTopAnchor(button, topAnchor);
        AnchorPane.setLeftAnchor(button, leftAnchor);
    }

    public void submenubtn(Button button, double width, double height, double topAnchor, double leftAnchor) {
        button.setStyle("-fx-background-color:#fff8e3;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 15));
        button.setPrefSize(260, 20);

        button.setPrefSize(width, height);
        AnchorPane.setTopAnchor(button, topAnchor);
        AnchorPane.setLeftAnchor(button, leftAnchor);
    }

    public static Label currenttime(AnchorPane pane) {
        Label time = new Label();  // display the time
        time.setFont(Font.font("Anton", 70));
        time.setLayoutX(410);
        time.setLayoutY(80);

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
    public TextField input(String text, String type, double top, double left) {
        // create label
        Label label = new Label(text);
        label.setFont(Font.font("Anton", 15));
        AnchorPane.setTopAnchor(label, top);
        AnchorPane.setLeftAnchor(label, left);

        // user input section
        TextField textField = new TextField();
        textField.setPromptText("Type your " + type + " here...");
        textField.setPrefColumnCount(15);
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


    public Label instruction(int inputY, String type) {
        Label instruction = new Label("Enter your " + type + ":");
        instruction.setFont(Font.font("Anton", 15));
        instruction.setLayoutX(50);
        instruction.setLayoutY(inputY);
        return instruction;
    }

    public TextArea description(String text, String type, double top, double left) {
        Label label = new Label(text); // to display Enter your text :
        label.setFont(Font.font("Anton", 15));
        AnchorPane.setTopAnchor(label, top);
        AnchorPane.setLeftAnchor(label, left);

        // user input section
        TextArea area = new TextArea();
        area.setPromptText("Type your " + type + " here..."); // to display Type your type here ... section
        area.setPrefWidth(600);  // Set the preferred width
        area.setPrefHeight(60);
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

    public void submenu(Stage primaryStage, AnchorPane pane, Scene homepage, Scene debit, Scene credit, Scene saving, Scene history, Scene creditloan, Scene deposit, Scene pageViewGraph) {
        Rectangle pinkrec = new Rectangle(240, 335);  // Width, Height
        pinkrec.setFill(Color.web("#f4d2d2"));  // Fill the rectangle with blue color
        pinkrec.setArcWidth(20);     // Horizontal radius of the corner
        pinkrec.setArcHeight(20);    // Vertical radius of the corner
        AnchorPane.setTopAnchor(pinkrec, 30.0);
        AnchorPane.setLeftAnchor(pinkrec, 425.0);
        pane.getChildren().add(pinkrec);

        Button submenu = new Button("☰");
        submenu.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        submenu.setFont(Font.font("Anton", 15));
        submenu.setLayoutX(600);
        submenu.setLayoutY(42);
        submenu.setPrefWidth(35);
        submenu.setPrefHeight(35);

        submenu.setOnAction(e -> showSubmenu(pane));


        pane.getChildren().add(submenu);

        Button debitbtn = new Button("Debit");
        Button creditbtn = new Button("Credit");
        Button savingbtn = new Button("Savings");
        Button historybtn = new Button("History");
        Button creditloanbtn = new Button("Credit Loan");
        Button interestpredictorbtn = new Button("Deposit Interest Predictor");
        Button viewGraphsbtn = new Button("Graph");
        Button logoutbtn = new Button("Log Out");
        submenubtn(debitbtn, 190, 20, 85, 455);  // just for color and coordinate
        submenubtn(creditbtn, 190, 20, 125, 455);
        submenubtn(savingbtn, 190, 20, 165, 455);
        submenubtn(historybtn, 190, 20, 205, 455);
        submenubtn(creditloanbtn, 190, 20, 245, 455);
        submenubtn(interestpredictorbtn, 190, 20, 285, 455);
        submenubtn(viewGraphsbtn, 190, 20, 325, 455);
        submenubtn(logoutbtn, 190, 20, 42, 455);
        logoutbtn.setPrefWidth(140);
        logoutbtn.setPrefHeight(20);
        logoutbtn.setStyle("-fx-background-color:#d2e3b7;-fx-text-fill:white;");
        debitbtn.setVisible(false);
        creditbtn.setVisible(false);
        savingbtn.setVisible(false);
        historybtn.setVisible(false);
        creditloanbtn.setVisible(false);
        interestpredictorbtn.setVisible(false);
        viewGraphsbtn.setVisible(false);
        logoutbtn.setVisible(false);
        pinkrec.setVisible(false);

        debitbtn.setManaged(false);
        creditbtn.setManaged(false);
        savingbtn.setManaged(false);
        historybtn.setManaged(false);
        creditloanbtn.setManaged(false);
        interestpredictorbtn.setManaged(false);
        viewGraphsbtn.setManaged(false);
        logoutbtn.setManaged(false);
        pinkrec.setManaged(false);

        pinkrec.getStyleClass().add("submenu");
        debitbtn.getStyleClass().add("submenu");
        creditbtn.getStyleClass().add("submenu");
        savingbtn.getStyleClass().add("submenu");
        historybtn.getStyleClass().add("submenu");
        creditloanbtn.getStyleClass().add("submenu");
        interestpredictorbtn.getStyleClass().add("submenu");
        logoutbtn.getStyleClass().add("submenu");
        viewGraphsbtn.getStyleClass().add("submenu");

        Label submenuLabel = new Label("Submenu");
        debitbtn.setId("submenuBtn1");
        creditbtn.setId("submenuBtn2");
        debitbtn.setId("submenuBtn3");
        savingbtn.setId("submenuBtn4");
        historybtn.setId("submenuBtn5");
        creditloanbtn.setId("submenuBtn6");
        interestpredictorbtn.setId("submenuBtn7");
        logoutbtn.setId("submenuBtn8");
        viewGraphsbtn.setId("submenuBtn9");
        logoutbtn.setId("submenuBtn10");
        pinkrec.setId("submenuBtn11");
        submenu.setId("submenuBtn12");


        pane.getChildren().addAll(debitbtn, creditbtn, savingbtn, historybtn, creditloanbtn, interestpredictorbtn, logoutbtn, viewGraphsbtn);

        debitbtn.setOnAction(e -> primaryStage.setScene(debit));
        creditbtn.setOnAction(e -> primaryStage.setScene(credit));
        savingbtn.setOnAction(e -> primaryStage.setScene(saving));
        historybtn.setOnAction(e ->
        {
            Transaction(Username);
            primaryStage.setScene(history);
        });
        creditloanbtn.setOnAction(e -> primaryStage.setScene(creditloan));
        interestpredictorbtn.setOnAction(e -> primaryStage.setScene(deposit));
        logoutbtn.setOnAction(e -> {
            Username = null;
            clearAllNodes(clearNodes);
            removeLabelById(pane, "balanceLabel");
            isUser = false; // Set user status to false
            Label logOut = new Label("LogOut Succesfully");
            popupMessage(logOut);
            primaryStage.setScene(homepage);
        });
        viewGraphsbtn.setOnAction(e -> primaryStage.setScene(pageViewGraph));


    }

    public void showSubmenu(AnchorPane pane) {
        // Find all buttons added to the pane and toggle their visibility and managed state
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node.getStyleClass().contains("submenu")) {
                node.setVisible(!node.isVisible());
                node.setManaged(!node.isManaged());
            }
        }

    }

    public static void setupSubmenu(AnchorPane pane) {
        ArrayList<Node> childrenCopy = new ArrayList<>(pane.getChildren());
        for (Node child : childrenCopy) {
            if (child.getId() != null && child.getId().startsWith("submenu")) {
                child.toFront();
            }
        }
    }

    private static String[] category = {""};

    public static void selectCategory(AnchorPane pane) {

        ListView<Button> listView = new ListView<>();
        AnchorPane.setTopAnchor(listView, 250.0);  // 50px from the top of the parent
        AnchorPane.setLeftAnchor(listView, 50.0); // 100px from the left of the parent
        Button Food = new Button("Food");
        Button Transportation = new Button("Transportation");
        Button Fashion = new Button("Fashion");
        Button Others = new Button("Others");
        categorybtn(Food, listView);
        categorybtn(Transportation, listView);
        categorybtn(Fashion, listView);
        categorybtn(Others, listView);
        Label lbl = new Label();
        lbl.setFont(Font.font("Anton", 15));
        lbl.setLayoutX(50);
        lbl.setLayoutY(300);

        Food.setOnAction(e -> {
            category[0] = "Food";
            lbl.setText("Food");
        });
        Transportation.setOnAction(e -> {
            category[0] = "Transportation";
            lbl.setText("Transportation");
        });

        Fashion.setOnAction(e -> {
            category[0] = "Fashion";
            lbl.setText("Fashion");
        });
        Others.setOnAction(e -> {
            category[0] = "Others";
            lbl.setText("Others");
        });
        clearNodes.add(lbl);
        listView.getItems().addAll(Food, Transportation, Fashion, Others);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(listView);
        scrollPane.setStyle("-fx-border-width: 0;");

        scrollPane.setPrefHeight(80);
        scrollPane.setPrefWidth(180);
        AnchorPane.setTopAnchor(scrollPane, 280.0);  // 50px from the top of the parent
        AnchorPane.setLeftAnchor(scrollPane, 250.0); // 100px from the left of the parent

        pane.getChildren().addAll(scrollPane, lbl);

    }

    public static String getCat() {
        return category[0];
    }

    private static String selectedBank = ""; // it should be static as

    public void bankSelection(AnchorPane pane, StackPane stackpane) {
        Label instruction = new Label("Select Your Bank");
        instruction.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        instruction.setFont(Font.font("Anton", 30));  // Set the font family and size here
        instruction.setLayoutX(50);
        instruction.setLayoutY(150);

        Label yearlyDeposit = new Label("Yearly Deposit :");
        yearlyDeposit.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        yearlyDeposit.setFont(Font.font("Anton", 30));  // Set the font family and size here
        yearlyDeposit.setLayoutX(50);
        yearlyDeposit.setLayoutY(200);

        Label monthlyDeposit = new Label("Monthly Deposit :");
        monthlyDeposit.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        monthlyDeposit.setFont(Font.font("Anton", 30));  // Set the font family and size here
        monthlyDeposit.setLayoutX(300);
        monthlyDeposit.setLayoutY(200);

        // button to click 
        Button RHB = new Button("RHB");
        bankSelectionbtn(RHB, 450, 170);
        Button MayBank = new Button("MayBank");
        bankSelectionbtn(MayBank, 450, 205);
        Button HongLeong = new Button("Hong Leong");
        bankSelectionbtn(HongLeong, 450, 240);
        Button Alliance = new Button("Alliance");
        bankSelectionbtn(Alliance, 450, 275);
        Button AmBank = new Button("AmBank");
        bankSelectionbtn(AmBank, 450, 310);
        Button StandardChartered = new Button("Standard Chartered");
        bankSelectionbtn(StandardChartered, 450, 345);
        Button confirmbtn = new Button("Confirm");
        confirmbtn.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        confirmbtn.setFont(Font.font("Anton", 15));
        confirmbtn.setLayoutX(300);
        confirmbtn.setLayoutY(155);
        // to show user bank
        Label rhblbl = new Label("RHB");
        rhblbl.setVisible(false);
        rhblbl.setManaged(false);
        Label maybanklbl = new Label("MayBank");
        maybanklbl.setVisible(false);
        maybanklbl.setManaged(false);
        Label hongleonglbl = new Label("Hong Leong");
        hongleonglbl.setVisible(false);
        hongleonglbl.setManaged(false);
        Label alliancelbl = new Label("Alliance");
        alliancelbl.setVisible(false);
        alliancelbl.setManaged(false);
        Label ambanklbl = new Label("AmBank");
        ambanklbl.setVisible(false);
        ambanklbl.setManaged(false);
        Label standardcharteredlbl = new Label("Standard Chartered");
        standardcharteredlbl.setVisible(false);
        standardcharteredlbl.setManaged(false);

        // bg of the button
        Rectangle bankselectionBG = new Rectangle();
        bankselectionBG.setFill(Color.web("#f4d2d2"));  // Fill the rectangle with blue color
        bankselectionBG.setArcWidth(20);     // Horizontal radius of the corner
        bankselectionBG.setArcHeight(20);    // Vertical radius of the corner
        bankselectionBG.setLayoutX(450);
        bankselectionBG.setLayoutY(250);

        // adding nodes into style class so that can control its visibility
        RHB.getStyleClass().add("bankSelection");
        MayBank.getStyleClass().add("bankSelection");
        HongLeong.getStyleClass().add("bankSelection");
        Alliance.getStyleClass().add("bankSelection");
        AmBank.getStyleClass().add("bankSelection");
        StandardChartered.getStyleClass().add("bankSelection");

        Label depositLabel = new Label("Predited Deposit (Annually):");
        Label monthlylbl = new Label("Predited Deposit (Monthly) :");


        RHB.setOnAction(e -> {
            label(rhblbl);
            hideAllLabels(maybanklbl, hongleonglbl, alliancelbl, ambanklbl, standardcharteredlbl);
            hideAllButtons(RHB, MayBank, HongLeong, Alliance, AmBank, StandardChartered);
            instruction.setVisible(false);
            selectedBank = "RHB";
        });
        MayBank.setOnAction(e -> {
            hideAllLabels(rhblbl, hongleonglbl, alliancelbl, ambanklbl, standardcharteredlbl);
            hideAllButtons(RHB, MayBank, HongLeong, Alliance, AmBank, StandardChartered);
            label(maybanklbl);
            showBankSelection(pane);
            instruction.setVisible(false);
            selectedBank = "MayBank";
        });
        HongLeong.setOnAction(e -> {
            hideAllLabels(rhblbl, maybanklbl, alliancelbl, ambanklbl, standardcharteredlbl);
            hideAllButtons(RHB, MayBank, HongLeong, Alliance, AmBank, StandardChartered);
            label(hongleonglbl);
            instruction.setVisible(false);
            selectedBank = "HongLeong";
        });
        Alliance.setOnAction(e -> {
            hideAllLabels(rhblbl, maybanklbl, hongleonglbl, ambanklbl, standardcharteredlbl);
            hideAllButtons(RHB, MayBank, HongLeong, Alliance, AmBank, StandardChartered);
            label(alliancelbl);
            instruction.setVisible(false);
            selectedBank = "Alliance";
        });
        AmBank.setOnAction(e -> {
            hideAllLabels(rhblbl, maybanklbl, hongleonglbl, alliancelbl, standardcharteredlbl);
            hideAllButtons(RHB, MayBank, HongLeong, Alliance, AmBank, StandardChartered);
            label(ambanklbl);
            instruction.setVisible(false);
            selectedBank = "AmBank";
        });
        StandardChartered.setOnAction(e -> {
            hideAllLabels(rhblbl, maybanklbl, hongleonglbl, alliancelbl, ambanklbl);
            StandardChartered.setVisible(false);
            StandardChartered.setManaged(false);
            hideAllButtons(RHB, MayBank, HongLeong, Alliance, AmBank, StandardChartered);
            label(standardcharteredlbl);
            instruction.setVisible(false);
            selectedBank = "Standard Chartered";
        });

        confirmbtn.setOnAction(c -> {
            if (selectedBank != null) {
                // Perform calculation and update the label
                depositCalculator(selectedBank, depositLabel, monthlylbl, pane);
            } else {
                depositLabel.setText("Please select a bank first!");
            }
        });


        pane.getChildren().addAll(yearlyDeposit, monthlyDeposit, confirmbtn, rhblbl, maybanklbl, hongleonglbl, alliancelbl, ambanklbl, standardcharteredlbl, bankselectionBG, RHB, MayBank, HongLeong, Alliance, AmBank, StandardChartered,
                instruction);

    }

    public static void hideAllLabels(Label... labels) {
        for (Label label : labels) {
            label.setVisible(false);
            label.setManaged(false);
        }
    }

    public static void hideAllButtons(Button... buttons) {
        for (Button btn : buttons) {
            btn.setVisible(false);
            btn.setManaged(false);
        }
    }

    public static void bankSelectionbtn(Button btn, double x, double y) {
        btn.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        btn.setFont(Font.font("Anton", 15));
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        btn.setPrefWidth(150.0);
        btn.setPrefHeight(15.0);
        btn.setVisible(false);
        btn.setManaged(false);
        btn.toFront();
    }


    public static void setLabelVisibility(Label label) {
        label.setVisible(false);
        label.setManaged(false);  // Initially set the label to not be visible or managed
    }

    public static void label(Label label) {
        label.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        label.setFont(Font.font("Anton", 23));  // Set the font family and size here
        label.setLayoutX(50);
        label.setLayoutY(150);
        label.setVisible(true);
        label.setManaged(true);
    }

    public static void showBankSelection(AnchorPane pane) {
        // Find all buttons added to the pane
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node.getStyleClass().contains("bankSelection")) {
                node.setVisible(!node.isVisible());
                node.setManaged(!node.isManaged());
            }

        }

    }

    public static void viewBarChart() {
        Transaction barchart = new Transaction(Username);
        debitTotal = barchart.getDebitTotal();
        creditTotal = barchart.getCreditTotal();
    }

    public static void viewBarChartbtn(Button btn) {
        btn.setStyle("-fx-background-color:#fff8e3;-fx-text-fill:black;");
        btn.setFont(Font.font("Anton", 15));
        btn.setPrefSize(150, 20);

        AnchorPane.setTopAnchor(btn, 30.0);
        AnchorPane.setLeftAnchor(btn, 250.0);
        btn.setOnAction(e -> viewBarChart());
    }

    // functions
// log in page
    public String logIn(String name, String email, String password, AnchorPane pane) {
        LogIn userLogIn = new LogIn(name, email, password);
        Label lbl = userLogIn.login();
        name = userLogIn.getName();
        lbl.setId("usernameLabel");

        Rectangle rec = new Rectangle(180, 35);  // Width, Height
        rec.setFill(Color.web("#f1dadb"));
        rec.setArcWidth(20);
        rec.setArcHeight(20);
        AnchorPane.setTopAnchor(rec, 30.0);
        AnchorPane.setLeftAnchor(rec, 485.0);
        pane.getChildren().add(rec);
        Label userID = userLogIn.getID();
        if (userID != null) {
            userID.setId("useridLabel");
            String userIDstr = userID.getText();
            Label useridlbl = new Label("UserID:" + userIDstr);
            useridlbl.setFont(Font.font("Anton", 20));
            AnchorPane.setTopAnchor(useridlbl, 33.0);
            AnchorPane.setLeftAnchor(useridlbl, 510.0);
            pane.getChildren().add(useridlbl);
        }

        popupMessage(lbl);


        return name;
    }

    // registration page
    static boolean registrationValid = false;

    public void register(String username, String email, String password) {
        Registration userRegister = new Registration(username, email, password);
        Label message = userRegister.register();
        if (message != null) {
            popupMessage(message);
        }
    }

    // record debit and credit
    public static void store(String file, String content) {
        String line = "";
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.newLine();
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (bw != null) {
                    bw.close(); // Close BufferedWriter
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getBalance = new ArrayList<>();

    public static void Debit(double amount, String description, String type, String category) {
        Debit debit = new Debit(Username, amount, description, type, category);
        Label message = debit.getLabel();
        popupMessage(message);
    }

    public void Credit(double amount, String description, String type, String category, boolean overdue) {
        Credit credit = new Credit(Username, amount, description, type, category);
        Label message = credit.getLabel();
        popupMessage(message);
    }

    // history
    public void Transaction(String name) {
        Transaction data = new Transaction(name);
        data.readFile();
        tableViewOverview.setItems(data.getOverviewData());
        tableViewDebit.setItems(data.getDebitData());
        tableViewCredit.setItems(data.getCreditData());
        tableViewLoanApplied.setItems(data.getAppliedLoan());
    }

    // apply loan
    public void PeriodSelection(AnchorPane pane) {
        TextField enterprincipal = new TextField();
        enterprincipal.setPromptText("Enter Principal Amount(RM)...");
        enterprincipal.setStyle("-fx-font-size: 14px;-fx-text-fill: black; -fx-border-radius: 5px;");
        enterprincipal.setLayoutX(50);
        enterprincipal.setLayoutY(130);
        enterprincipal.setFont(Font.font("Anton", 15));
        enterprincipal.setPrefWidth(180);
        clearNodes.add(enterprincipal);

        String[] principal = {""};
        enterprincipal.textProperty().addListener((observable, oldValue, newValue) -> {
            principal[0] = newValue; // Update the description dynamically as user types
        });


        Label interestRatelbl = new Label("Interest Rate %");
        AnchorPane.setTopAnchor(interestRatelbl, 160.0);
        AnchorPane.setLeftAnchor(interestRatelbl, 50.0);
        interestRatelbl.setStyle("-fx-text-fill:black;");
        interestRatelbl.setFont(Font.font("Anton", 20));

        TextField enterrate = new TextField();
        enterrate.setPromptText("Enter Interest Rate(%)...");
        enterrate.setStyle("-fx-font-size: 14px;-fx-text-fill: black; -fx-border-radius: 5px;");
        enterrate.setLayoutX(50);
        enterrate.setLayoutY(190);
        enterrate.setFont(Font.font("Anton"));
        enterrate.setPrefWidth(180);
        clearNodes.add(enterrate);

        String[] rate = {""};
        enterrate.textProperty().addListener((observable, oldValue, newValue) -> {
            rate[0] = newValue; // Update the description dynamically as user types
        });

        Label periodlbl = new Label("Repayment Period (month(s))");
        AnchorPane.setTopAnchor(periodlbl, 230.0);
        AnchorPane.setLeftAnchor(periodlbl, 50.0);
        periodlbl.setStyle("-fx-text-fill:black;");
        periodlbl.setFont(Font.font("Anton", 20));

        TextField entermonth = new TextField();
        entermonth.setPromptText("Enter month(s)...");
        entermonth.setStyle("-fx-font-size: 14px;-fx-text-fill: black; -fx-border-radius: 5px;");
        entermonth.setLayoutX(50);
        entermonth.setLayoutY(260);
        entermonth.setFont(Font.font("Anton"));
        entermonth.setPrefWidth(180);
        clearNodes.add(entermonth);

        String[] month = {""};
        entermonth.textProperty().addListener((observable, oldValue, newValue) -> {
            month[0] = newValue; // Update the description dynamically as user types
        });

        Label selectPeriodlbl = new Label("Payment Frequency");
        AnchorPane.setTopAnchor(selectPeriodlbl, 295.0);
        AnchorPane.setLeftAnchor(selectPeriodlbl, 50.0);
        selectPeriodlbl.setStyle("-fx-text-fill:black;");
        selectPeriodlbl.setFont(Font.font("Anton", 20));

        String period[] = new String[1];
        period[0] = "";
        Button monthly = new Button("Monthly");
        SelectPeriodbtn(monthly, 270, 205, 15, 150);
        Label monthlylbl = new Label("Monthly");
        monthlylbl.setVisible(false);
        monthlylbl.setManaged(false);
        Button quarterly = new Button("Quarterly");
        SelectPeriodbtn(quarterly, 270, 240, 15, 150);
        Label quarterlylbl = new Label("Quarterly");
        quarterlylbl.setVisible(false);
        quarterlylbl.setManaged(false);
        Button semiannual = new Button("Semi-Annually");
        SelectPeriodbtn(semiannual, 270, 275, 15, 150);
        Label semiannuallbl = new Label("Semi-Anually");
        semiannuallbl.setVisible(false);
        semiannuallbl.setManaged(false);
        Button annual = new Button("Annually");
        SelectPeriodbtn(annual, 270, 310, 15, 150);
        Label anuallylbl = new Label("Annually");
        anuallylbl.setVisible(false);
        anuallylbl.setManaged(false);

        monthly.getStyleClass().add("PeriodSelection");
        quarterly.getStyleClass().add("PeriodSelection");
        semiannual.getStyleClass().add("PeriodSelection");
        annual.getStyleClass().add("PeriodSelection");

        monthly.setOnAction(e -> {
            period(monthlylbl);
            period[0] = "Monthly";
            hideAllLabels(quarterlylbl, semiannuallbl, anuallylbl);
            hideAllButtons(monthly, quarterly, semiannual, annual);
        });
        quarterly.setOnAction(e -> {
            hideAllLabels(monthlylbl, semiannuallbl, anuallylbl);
            period(quarterlylbl);
            period[0] = "Quarterly";
            hideAllButtons(monthly, quarterly, semiannual, annual);
        });
        semiannual.setOnAction(e -> {
            hideAllLabels(monthlylbl, quarterlylbl, anuallylbl);
            period(semiannuallbl);
            period[0] = "Semi-Annually";
            hideAllButtons(monthly, quarterly, semiannual, annual);
        });
        annual.setOnAction(e -> {
            hideAllLabels(monthlylbl, quarterlylbl, semiannuallbl);
            period(anuallylbl);
            period[0] = "Annually";
            hideAllButtons(monthly, quarterly, semiannual, annual);
        });

        Button confirmapply = new Button("Confirm");
        buttonfontsize(confirmapply);
        confirmapply.setLayoutX(450);
        confirmapply.setLayoutY(300);
        confirmapply.setPrefSize(100, 18);


        confirmapply.setOnAction(c -> {

            if (principal[0].matches("\\d*\\.?\\d+")
                    && rate[0].matches("\\d*\\.?\\d+") && month[0].matches("\\d*\\.?\\d+")) {

                if (period[0] != null) {
                    try {
                        applyLoan(period[0], principal[0], rate[0], month[0]);
                    } catch (NumberFormatException ex) {
                        Label wrongformat = new Label("Wrong Number Format\nInterest :integer/decimal\nRepayment Period : Whole Number");
                        popupMessage(wrongformat);
                        ex.printStackTrace();
                    }
                } else {
                    Label selectperiodfirst = new Label("Select a Period");
                    popupMessage(selectperiodfirst);
                }
            } else {
                Label lbl = new Label("Enter a Number\neg.whole number/decimal number");
                popupMessage(lbl);
            }
            hideAllLabels(monthlylbl, quarterlylbl, semiannuallbl, anuallylbl);
            clearAllNodes(clearNodes);
        });
        pane.getChildren().addAll(monthly, monthlylbl, quarterly, quarterlylbl, semiannual, semiannuallbl, annual, anuallylbl, confirmapply,
                interestRatelbl, periodlbl, enterprincipal, enterrate, selectPeriodlbl, entermonth);

    }

    public void applyLoan(String frequency, String principal, String rate, String month) {
        ApplyLoan apply = new ApplyLoan();
        apply.setPaymentFrequency(frequency);
        apply.setUsername(Username);
        apply.setPrincipal(principal);
        apply.setAnnualInterest(rate);
        apply.setTime(month);

        boolean valid = apply.validateInputs();
        if (valid) {
            apply.CurrentDate();
            apply.calculateDueDate();
            apply.calculateNextPaymentDate();
            apply.calculateTotalRepayment();
            apply.updateCSV();
            Label applySuccessfull = apply.getLabel();
            popupMessage(applySuccessfull);
        } else {
            Label invalidinputs = apply.getLabel();
            popupMessage(invalidinputs);
        }
    }

    public void showPeriodSelection(AnchorPane pane) {
        // Find all buttons added to the pane
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node.getStyleClass().contains("PeriodSelection")) {
                node.setVisible(!node.isVisible());
                node.setManaged(!node.isManaged());
            }

        }
    }

    public static String getPeriod(String period) {
        return period;
    }

    public static void period(Label label) {
        label.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        label.setFont(Font.font("Anton", 23));  // Set the font family and size here
        label.setLayoutX(60);
        label.setLayoutY(320);
        label.setVisible(true);
        label.setManaged(true);
    }


    public static void SelectPeriodbtn(Button btn, double x, double y, double height, double width) {
        btn.setStyle("-fx-background-color:#FEEBA8;-fx-text-fill:black;");
        btn.setFont(Font.font("Anton", 15));
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        btn.setPrefSize(width, height);
        btn.setVisible(false);
        btn.setManaged(false);
        btn.toFront();
    }

    // repay
    public void chooseLoan(AnchorPane pane) {
        String[] id = {""};

        ArrayList<String> LoanID = new ArrayList<>();
        Button activeloanbtn = new Button("Active Loan");
        graphsbtn(activeloanbtn);
        activeloanbtn.setLayoutX(50);
        activeloanbtn.setLayoutY(140);

        Button overdueloanbtn = new Button("Overdue Loan");
        graphsbtn(overdueloanbtn);
        overdueloanbtn.setLayoutX(200);
        overdueloanbtn.setLayoutY(140);


        Label getID[] = new Label[1];
        getID[0] = new Label("Repay LoanID...");
        getID[0].setFont(Font.font("Anton", 15));
        getID[0].setLayoutX(50);
        getID[0].setLayoutY(200);

        ScrollPane scroll = new ScrollPane();
        scroll.setVisible(false);
        scroll.setManaged(false);
        scroll.setPrefHeight(150);
        scroll.setPrefWidth(150);
        AnchorPane.setTopAnchor(scroll, 130.0);
        AnchorPane.setLeftAnchor(scroll, 350.0);
        pane.getChildren().add(scroll);


        activeloanbtn.setOnAction(e -> {
            if (getID[0] != null) {
                getID[0].setText("Repay LoanID...");
            }

            Repayment activeLoan = new Repayment(Username);
            LoanID.clear();
            LoanID.addAll(activeLoan.getActiveLoanIDList());
            if (!LoanID.isEmpty()) {
                scroll.setVisible(true);
                scroll.setManaged(true);
                setUpRepaybtn(LoanID, scroll, getID[0], pane, selectedLoanID -> {
                    id[0] = selectedLoanID;
                });
            } else {
                scroll.setVisible(false);
                scroll.setManaged(false);
                Label lbl = new Label("No Active Loans Found.");
                popupMessage(lbl);
            }

        });

        overdueloanbtn.setOnAction(e -> {
            if (getID[0] != null) {
                getID[0].setText("Repay LoanID...");
            }

            Repayment overdueLoan = new Repayment(Username);
            LoanID.clear();
            LoanID.addAll(overdueLoan.getOverdueLoanIDList());
            if (!LoanID.isEmpty()) {
                scroll.setVisible(true);
                scroll.setManaged(true);
                setUpRepaybtn(LoanID, scroll, getID[0], pane, selectedLoanID -> {
                    id[0] = selectedLoanID;
                });
            } else {
                scroll.setVisible(false);
                scroll.setManaged(false);
                Label lbl = new Label("No Overdue Loans Found.");
                popupMessage(lbl);
            }

        });

        Button confirmrepay = new Button("Repay");
        buttonfontsize(confirmrepay);
        confirmrepay.setLayoutX(400);
        confirmrepay.setLayoutY(300);
        confirmrepay.setPrefSize(100, 18);

        TextField amountRepay = new TextField();
        amountRepay.setPromptText("Enter Repayment Amount(RM)...");
        amountRepay.setPrefColumnCount(19);
        amountRepay.setStyle("-fx-font-size: 14px; -fx-background-color:#FFFFFF ;-fx-text-fill: black; -fx-border-radius: 5px;");
        amountRepay.setFont(Font.font("Anton", 12));
        AnchorPane.setTopAnchor(amountRepay, 300.0);
        AnchorPane.setLeftAnchor(amountRepay, 50.0);
        clearNodes.add(amountRepay);

        String[] repayamount = {""};
        amountRepay.textProperty().addListener((observable, oldValue, newValue) -> {
            repayamount[0] = newValue;
        });

        confirmrepay.setOnAction(e -> {
            if (getID[0].getText().equals("Repay LoanID...")) {
                Label lbl = new Label("Select a Loan."); // no loan selected
                popupMessage(lbl);
            } else {
                try {
                    Repayment repayment = new Repayment(Username);
                    repayment.deductLoan(id[0], Double.parseDouble(repayamount[0]));
                    Label lbl = repayment.getLabel();
                    Label repaySuccessful = new Label("Repayment Succesfull!");
                    if (lbl != null) {
                        popupMessage(lbl);
                    } else {
                        popupMessage(repaySuccessful);
                    }
                } catch (NumberFormatException ex) {
                    Label wrongcashformat = new Label("Wrong Cash Format eg.1000");
                    popupMessage(wrongcashformat);
                    ex.printStackTrace();
                }
            }
            getID[0].setText("Repay LoanID...");
            clearAllNodes(clearNodes);
        });

        pane.getChildren().addAll(getID[0], activeloanbtn, overdueloanbtn, confirmrepay, amountRepay);

    }


    public static void setUpRepaybtn(ArrayList<String> LoanID, ScrollPane scroll, Label getID, AnchorPane pane, Consumer<String> onLoanSelected) {
        VBox buttonBox = new VBox(10);
        for (int i = 0; i < LoanID.size(); i++) {
            Button btn = new Button(LoanID.get(i));
            btn.setPrefHeight(18);
            btn.setPrefWidth(150);
            buttonBox.getChildren().add(btn);

            String loanID[] = {LoanID.get(i)};

            btn.setOnAction(c -> {
                getID.setText(loanID[0]);
                scroll.setVisible(false);
                scroll.setManaged(false);
                onLoanSelected.accept(loanID[0]);
            });
        }
        scroll.setContent(buttonBox);
        if (!pane.getChildren().contains(scroll)) {
            pane.getChildren().add(scroll);

        }
    }

    // savings
    public void enterPercentage(AnchorPane pane) {
        TextField enterPercentage = new TextField();
        enterPercentage.setPromptText("Enter percentage(%) here :");
        enterPercentage.setStyle("-fx-text-fill:black;");
        enterPercentage.setFont(Font.font("Anton", 15));
        enterPercentage.setPrefSize(150, 15);
        enterPercentage.setLayoutX(50);
        enterPercentage.setLayoutY(250);
        String[] savingPercentage = {""};
        enterPercentage.textProperty().addListener((observable, oldValue, newValue) -> {
            savingPercentage[0] = newValue.trim();
        });
        clearNodes.add(enterPercentage);

        boolean status[] = new boolean[1];
        Button confirm = new Button("Confirm");
        confirm.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        confirm.setFont(Font.font("Anton", 15));
        confirm.setPrefSize(100, 20);
        confirm.setLayoutX(220);
        confirm.setLayoutY(250);
        confirm.setOnAction(e -> {
            if (savingPercentage[0].matches("\\d*\\.?\\d+")) { // only positive allowed
                Savings saving = new Savings(Username, savingPercentage[0]);
                Label lbl = saving.getLabel();
                popupMessage(lbl);
            } else {
                Label lbl = new Label("Enter Number (integer/double)");
                popupMessage(lbl);
            }
        });


        pane.getChildren().addAll(enterPercentage, confirm);
    }

// predicted deposit

    public void depositCalculator(String bank, Label showDeposit, Label showMonthly, AnchorPane pane) {
        PredictedDeposit deposit = new PredictedDeposit();
        deposit.setName(Username);
        deposit.getBalance();
        deposit.setBank(bank);
        deposit.calculateDeposit();

        double predictedDeposit = deposit.getDeposit();
        double monthlyDeposit = deposit.getMonthlyDeposit();
        Label lbl = deposit.getlbl();
        if (lbl != null) {
            popupMessage(lbl);
        }

        showDeposit.setText("RM " + String.format("%.2f", predictedDeposit));
        showDeposit.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        showDeposit.setFont(Font.font("Anton", 23));  // Set the font family and size here
        showDeposit.setLayoutX(50);
        showDeposit.setLayoutY(250);

        showMonthly.setText("RM " + String.format("%.2f", monthlyDeposit));
        showMonthly.setStyle("-fx-background-color:#FFFFFF; -fx-text-fill: black; -fx-border-radius: 5px;");
        showMonthly.setFont(Font.font("Anton", 23));  // Set the font family and size here
        showMonthly.setLayoutX(300);
        showMonthly.setLayoutY(250);


        if (showDeposit != null && !pane.getChildren().contains(showDeposit)) {
            pane.getChildren().add(showDeposit);
        } else {
            pane.getChildren().remove(showDeposit);
            pane.getChildren().add(showDeposit);
        }

        if (showMonthly != null && !pane.getChildren().contains(showMonthly)) {
            pane.getChildren().add(showMonthly);
        } else {
            pane.getChildren().remove(showMonthly);
            pane.getChildren().add(showMonthly);
        }

    }

    public static void toViewBalance(Stage stage, Scene scene, AnchorPane pane, AnchorPane vbpane) {
        Button btn = new Button("View Balance,Savings & Loan ");
        btn.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        btn.setFont(Font.font("Anton", 15));
        AnchorPane.setTopAnchor(btn, 50.0);
        AnchorPane.setLeftAnchor(btn, 270.0);
        btn.setOnAction(e -> {
            viewBalanceSavingsLoan(stage, scene, vbpane);
        });

        pane.getChildren().add(btn);
    }


    public static void viewBalanceSavingsLoan(Stage stage, Scene scene, AnchorPane pane) {
        stage.setScene(scene);
        removeLabelById(pane, "balanceLabel");
        removeLabelById(pane, "savingLabel");
        removeLabelById(pane, "loanLabel");
        Transaction viewbalance = new Transaction();
        String label = viewbalance.balance(Username);
        Label lbl = new Label(label);
        lbl.setId("balanceLabel");
        AnchorPane.setTopAnchor(lbl, 130.0);
        AnchorPane.setLeftAnchor(lbl, 50.0);
        lbl.setStyle("-fx-text-fill:black;");
        lbl.setFont(Font.font("Anton", 30));

        Savings viewsaving = new Savings(Username);
        double totalsavings = viewsaving.getTotalSavings(Username);
        BigDecimal roundedSavings = new BigDecimal(totalsavings).setScale(2, RoundingMode.HALF_UP);  // Rounds to 2 decimal places
        Label savinglbl = new Label("RM " + roundedSavings);
        savinglbl.setId("savingLabel");
        AnchorPane.setTopAnchor(savinglbl, 220.0);
        AnchorPane.setLeftAnchor(savinglbl, 50.0);
        savinglbl.setStyle("-fx-text-fill:black;");
        savinglbl.setFont(Font.font("Anton", 30));

        ApplyLoan getoutstandingbalance = new ApplyLoan();
        String labels = getoutstandingbalance.getTotalOustandingBalance(Username);
        Label loanlbl = new Label("RM " + labels);
        loanlbl.setId("loanLabel");

        AnchorPane.setTopAnchor(loanlbl, 310.0);
        AnchorPane.setLeftAnchor(loanlbl, 50.0);
        loanlbl.setStyle("-fx-text-fill:black;");
        loanlbl.setFont(Font.font("Anton", 30));


        pane.getChildren().addAll(lbl, savinglbl, loanlbl);

    }


    public static void removeLabelById(AnchorPane pane, String labelId) {
        // Iterate through the children of the pane and find the label with the matching ID
        for (Node node : pane.getChildren()) {
            if (node instanceof Label && node.getId() != null && node.getId().equals(labelId)) {
                pane.getChildren().remove(node); // Remove the label
                break; // Stop after removing the label
            }
        }
    }

    // logout
    public void clearAllNodes(ArrayList<Node> nodes) {
        for (Node node : nodes) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            } else if (node instanceof PieChart) {
                PieChart pieChart = (PieChart) node;
                pieChart.getData().clear();
            } else if (node instanceof BarChart) {
                BarChart pieChart = (BarChart) node;
                pieChart.getData().clear();
            } else if (node instanceof TextArea) {
                ((TextArea) node).clear();
            } else if (node instanceof Label) {
                ((Label) node).setText("");
            }
        }

    }

    private static Button[] spendingTrendCategorybtn = new Button[4];

    public void SpendingTrendCategory(AnchorPane pane, Node node1, Node node2, Node node3) {

        Button Fashion = new Button("Fashion");
        Button Transportation = new Button("Transportation");
        Button Food = new Button("Food");
        Button Others = new Button("Others");
        spendingTrendCategorybtn[0] = Fashion;
        spendingTrendCategorybtn[1] = Transportation;
        spendingTrendCategorybtn[2] = Food;
        spendingTrendCategorybtn[3] = Others;

        selectCategorybtn(Fashion);
        selectCategorybtn(Transportation);
        selectCategorybtn(Food);
        selectCategorybtn(Others);

        AnchorPane.setTopAnchor(Food, 100.0);
        AnchorPane.setLeftAnchor(Food, 200.0);

        AnchorPane.setTopAnchor(Transportation, 135.0);
        AnchorPane.setLeftAnchor(Transportation, 200.0);

        AnchorPane.setTopAnchor(Fashion, 170.0);
        AnchorPane.setLeftAnchor(Fashion, 200.0);


        AnchorPane.setTopAnchor(Others, 205.0);
        AnchorPane.setLeftAnchor(Others, 200.0);

        Fashion.setOnAction(e ->
        {
            getSpendingTrendGraph("Fashion", pane, node1);
            hideAllButtons(Fashion, Transportation, Food, Others);
        });
        Transportation.setOnAction(e ->
        {
            getSpendingTrendGraph("Transportation", pane, node1);
            hideAllButtons(Fashion, Transportation, Food, Others);
        });
        Food.setOnAction(e ->
        {
            getSpendingTrendGraph("Food", pane, node1);
            hideAllButtons(Fashion, Transportation, Food, Others);
        });
        Others.setOnAction(e ->
        {
            getSpendingTrendGraph("Others", pane, node1);
            hideAllButtons(Fashion, Transportation, Food, Others);
        });

        pane.getChildren().addAll(Fashion, Transportation, Food, Others);
    }

    public void getSpendingTrendGraph(String cat, AnchorPane pane, Node node) {
        SpendingTrend spendingtrend = new SpendingTrend(Username, cat);
        node = spendingtrend.SpendingTrendGraph();
        Label lbl = spendingtrend.getLabel();
        if (!lbl.getText().isEmpty()) {
            popupMessage(SpendingTrend.getLabel());
        } else {
            pane.getChildren().add(node);
            clearNodes.add(node);
            setupSubmenu(pane);
        }
    }


    public static void hideAllGraphs(BarChart<String, Number>... barCharts) {
        for (BarChart<String, Number> chart : barCharts) {
            chart.setVisible(false);
        }
    }

    public static void removeExistingGraphs(AnchorPane pane) {
        ArrayList<Node> nodesToRemove = new ArrayList<>();

        for (Node node : pane.getChildren()) {
            if (node instanceof BarChart || node instanceof PieChart) {
                nodesToRemove.add(node);
            }
        }

        pane.getChildren().removeAll(nodesToRemove);
    }

    public static void selectCategorybtn(Button button) {
        button.setStyle("-fx-background-color:#FED760;-fx-text-fill:black;");
        button.setFont(Font.font("Anton", 15));
        button.setPrefWidth(130.0);
    }


    // pop up message
    public static void popupMessage(Label label) {
        label.setStyle("-fx-text-fill:black;");
        label.setFont(Font.font("Anton", 15));
        label.setLayoutX(110);
        label.setLayoutY(50);
        Stage popupStage = new Stage();
        StackPane root = new StackPane();
        root.getChildren().add(label); // Add label to StackPane
        Scene scene = new Scene(root, 300, 100);
        popupStage.setScene(scene);
        popupStage.show();
        popupStage.setAlwaysOnTop(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> popupStage.close());
        delay.play();
    }

    public static void alertMessage(String title, String headerText, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static String formatCSV(String value) {
        if (value == null) return ""; // Handle null values
        value = value.replace("\"", "\"\""); // Escape double quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value + "\""; // Wrap in quotes if necessary
        }
        return value;
    }

    public static String[] splitCSVLine(String line, int x) {
        String[] result = new String[x];
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        int index = 0;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result[index++] = unformatCSV(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        result[index] = unformatCSV(currentField.toString());
        return result;
    }

    public static String unformatCSV(String value) {
        if (value == null || value.isEmpty()) return ""; // Handle empty values
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1); // Remove surrounding quotes
        }
        return value.replace("\"\"", "\""); // Unescape double quotes
    }


    //   main method
    public static void main(String[] args) {
        launch(args);
    }

}