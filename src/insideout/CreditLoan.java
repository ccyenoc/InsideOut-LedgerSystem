/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package insideout;
package com.brendanlee.fopAssignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Loan1 {
    static File loanCSV = new File("C://Users//Brendan//Desktop//LoanCSV.csv");
    double principal , annualInterestRate;
    int repaymentPeriod, loanID, userID;
    LocalDateTime timeNow = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss");
    String formattedTime = timeNow.format(formatter);
    String status;
    private static final Scanner sc = new Scanner(System.in);

    public Loan1(double principal, double annualInterestRate , int repaymentPeriod, int loanID, int userID, String formattedTime , String status) {
        this.principal = principal;
        this.annualInterestRate = annualInterestRate;
        this.repaymentPeriod = repaymentPeriod;
        this.loanID = loanID;
        this.userID = userID;
        this.formattedTime = formattedTime;
        this.status = status;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public int getRepaymentPeriod() {
        return repaymentPeriod;
    }

    public int getLoanID() {
        return loanID;
    }

    public int getUserID() {
        return userID;
    }

    public String getTimeNow() {
        return formattedTime;
    }

    public String getStatus() {
        return status;
    }


    public static void main(String[] args){
        User randomUser = new User("Brendan", "chengjun@gmail.com","1231312", 123);
        //suppose to inherit user data from first part but manually key in just for now

        if(!loanCSV.exists()) {
            try {
                loanCSV.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(loanCSV, true));
                writer.write("userID , loanID , principal, interestRate , period , CreatedAt ,status ");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        while(true) {
            // get choice (button tag)
            switch (choice) {
                case 1:
                    applyLoan(randomUser);
                    break;
                case 2:
                    repayLoan(randomUser);
                    break;
                case 3: return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static Label applyLoan(User user) {
        System.out.println("== Loan Application");
        System.out.println("Enter principal amount:");
        double principal = sc.nextDouble();
        System.out.println("Enter the annual interest rate (%)");
        double annualInterestRate = sc.nextDouble();
        System.out.print("Enter repayment period in months: ");
        int repaymentPeriod = sc.nextInt();
        sc.nextLine();

        double monthlyPayment = calculateMonthlyPayment(principal, annualInterestRate,repaymentPeriod );
        double totalRepayment = calculateTotalRepayment(monthlyPayment, repaymentPeriod);

        System.out.println("\nLoan Details: ");
        System.out.printf("Principal Amount: %.2f\n", principal);
        System.out.printf("Monthly Payment in month(s) : %.2f\n", monthlyPayment);
        System.out.printf("Total Repayment: %.2f\n", totalRepayment);

        // get button choice
        if(choice1.equalsIgnoreCase("Y")) {
            Loan1 newloan = new Loan1(principal,annualInterestRate,repaymentPeriod,1,user.getUserID(),LocalDateTime.now().format(formatter), "Active");
            saveLoan(newloan);
            System.out.println("Loan application successful!");
        } else {
            System.out.println("Loan application failed");
        }
    }

    public static double calculateMonthlyPayment(double principal, double annualInterestRate, double repaymentPeriod) {
        double monthlyInterestRate = annualInterestRate/12;
        return (principal * monthlyInterestRate * (Math.pow(1+monthlyInterestRate,repaymentPeriod)))/(Math.pow((1+monthlyInterestRate),repaymentPeriod)-1);
    }

    public static double calculateTotalRepayment(double monthlyPayment, int repaymentPeriod) {
        return monthlyPayment*repaymentPeriod;
    }

    public static void saveLoan(Loan1 newLoan) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader (loanCSV));
            BufferedWriter writer = new BufferedWriter(new FileWriter(loanCSV, true));
            String line;

            ArrayList<String> lines = new ArrayList<String>();
            while((line = reader.readLine())!=null) {
                lines.add(line);
            }
            reader.close();


            String newLoanString = String.format("%d,%d,%.2f,%.2f,%d,%s,%s",
                    newLoan.getUserID(),
                    1,  //******** generateLoanID(ArrayList lines)
                    newLoan.getPrincipal(),
                    newLoan.getAnnualInterestRate(),
                    newLoan.getRepaymentPeriod(),
                    newLoan.getTimeNow(),
                    "active"
            );

            writer.write("\n"+newLoanString);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void repayLoan(User user) {
        ArrayList<Loan1> activeLoanList = getActiveLoan(user.getUserID());

        if (activeLoanList.isEmpty()) {
            System.out.println("No active loan is found.");
            return;
        }

        int count = 1;
        for (Loan1 loan : activeLoanList) {
            System.out.print("Loan " + count + ": ");
            print(loan);
            count++;
        }

        System.out.println("Enter the loan number to repay (1 to " + activeLoanList.size() + "):");

        int loanNumber = sc.nextInt();

        Loan1 selectedLoan = activeLoanList.get(loanNumber - 1);

        System.out.println("You have selected Loan " + loanNumber + ":");
        print(selectedLoan);

        System.out.println("Enter the amount to repay:");

        double repaymentAmount = sc.nextDouble();

        if (repaymentAmount <= 0) {
            System.out.println("Invalid repayment amount. It must be greater than zero.");
            return;
        }

        // Process repayment logic (assume the loan has a method `repay` to handle the repayment)
        if (repaymentAmount >= selectedLoan.getPrincipal()) {
            System.out.println("Repayment successful. The loan is fully paid off.");
            updateStatus(selectedLoan);
        } else {
            System.out.println("Repayment successful. Remaining balance: " +
                    (selectedLoan.getPrincipal() - repaymentAmount));
            //selectedLoan.updatePrinciple(repaymentAmount);
        }

        if (selectedLoan.getPrincipal() == 0) {
            System.out.println("Loan fully paid off!");
        } else {
            System.out.println("Remaining loan balance: " + selectedLoan.getPrincipal());
        }
    }


    public static ArrayList<Loan1> getActiveLoan(int userID) {
        ArrayList<Loan1> loanList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(loanCSV))) {
            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                try {
                    Loan1 loan = new Loan1(line);

                    if (loan.getUserID() == userID && "active".equalsIgnoreCase(loan.getStatus())) {
                        loanList.add(loan);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the loan CSV file.");
            e.printStackTrace();
        }

        return loanList;
    }


    Loan1(String content) {
        String[] data = content.split(",");
        for(int i =0; i< data.length; i++) {
            data[i] = data[i].trim();
        }

        this.userID = Integer.parseInt(data[0]);
        this.loanID = Integer.parseInt(data[1]);
        this.principal = Double.parseDouble(data[2]);
        this.annualInterestRate = Double.parseDouble(data[3]);
        this.repaymentPeriod = Integer.parseInt(data[4]);
        this.status = data[6];

    }

    public static void print(Loan1 loan) {
        String loanString = String.format(
                "User ID: %d, Loan ID: %d, Principal: %.2f, Annual Interest Rate: %.2f%%, Repayment Period: %d months, Time: %s, Status: %s",
                loan.getUserID(),
                1, // ******** generateLoanID(ArrayList lines)
                loan.getPrincipal(),
                loan.getAnnualInterestRate(),
                loan.getRepaymentPeriod(),
                loan.getTimeNow(),
                "active"
        );

        System.out.println(loanString);
    }

    public static void updateStatus(Loan1 loan) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader (loanCSV));
            BufferedWriter updater = new BufferedWriter(new FileWriter(loanCSV, true));
            String line;

            ArrayList<String> lines = new ArrayList<String>();
            while((line = reader.readLine())!=null) {
                lines.add(line);
            }
            reader.close();

            for(int i=1; i< lines.size();i++) {
                String content = lines.get(i);
                String[] data = content.split(",");
                if(data[1].equalsIgnoreCase(String.valueOf(loan.getLoanID()))) {
                    data[6] = "paid";
                    lines.set(i, String.join(",", data));
                    break;
                }
                }
            for(int i =0; i< lines.size(); i++) {
                updater.write(lines.get(i));
                if(i< lines.size()-1) {
                    updater.write("\n");
                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updatePrinciple(Loan1 loan) {}

}