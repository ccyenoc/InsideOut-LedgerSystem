package insideout;

import static insideout.InsideOut.graphs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javafx.scene.control.Label;

public class SpendingTrend{
    private static Label lbl=new Label();
    private String targetUsername;
    private String Category;
    public SpendingTrend(String targetUsername,String Category){
      this.targetUsername=targetUsername;
      this.Category=Category;
    }
    
  public BarChart<String, Number> SpendingTrendGraph(){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Spending trends ");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Spending trends in Category: " + Category);

        XYChart.Series<String, Number> creditSeries = new XYChart.Series<>();
        creditSeries.setName("Spending trends for a month");

        String filePath = "src/recordcredit.csv";

        Map<String, Double> monthlyCreditTotals = new HashMap<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM yyyy");
        
        barChart.setCategoryGap(50);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                String username = data[0];
                String category = data[7];
                String date = data[5];
                double creditAmount = Double.parseDouble(data[3]);

                if (username.equalsIgnoreCase(targetUsername) && category.equalsIgnoreCase(Category)) {
                    try {
                        Date parsedDate = inputFormat.parse(date);
                        String month = outputFormat.format(parsedDate);

                        monthlyCreditTotals.put(month, monthlyCreditTotals.getOrDefault(month, 0.0) + creditAmount);
                        
        
                    } catch (ParseException e) {
                        System.out.println("Invalid date format: " + date);
                    }
                    
                      for (Map.Entry<String, Double> entry : monthlyCreditTotals.entrySet()) {
                          creditSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                       }
                }
                else{
                  lbl=new Label("No Data For "+Category+" Category!");
                }
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        graphs(barChart);
        barChart.getData().add(creditSeries);
        
        return barChart;
        
    }
  
  public static Label getLabel(){
    return lbl;
  }

   
}