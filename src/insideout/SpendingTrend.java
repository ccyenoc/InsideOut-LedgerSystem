package insideout;

import static insideout.InsideOut.graphs;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Label;

import static insideout.InsideOut.splitCSVLine;

public class SpendingTrend{
    private static Label lbl=new Label();
    private String targetUsername;
    private String Category;
    public SpendingTrend(String targetUsername,String Category){
      this.targetUsername=targetUsername;
      this.Category=Category;
    }
    
  public BarChart<String, Number> SpendingTrendGraph(){
      lbl = new Label();
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
          boolean dataFound = false;
          while ((line = br.readLine()) != null) {
              if (isHeader) {
                  isHeader = false;
                  continue;
              }

              String[] data = splitCSVLine(line, 8);
              String username = data[0].trim();
              String category = data[7].trim();
              String date = data[5].trim();
              double creditAmount = Double.parseDouble(data[3].trim());

              if (username.equalsIgnoreCase(targetUsername) && category.equalsIgnoreCase(Category)) {
                  dataFound = true;  // Data found for the category
                  try {
                      Date parsedDate = inputFormat.parse(date);
                      String month = outputFormat.format(parsedDate);

                      monthlyCreditTotals.put(month, monthlyCreditTotals.getOrDefault(month, 0.0) + creditAmount);

                  } catch (ParseException e) {
                      System.out.println("Invalid date format: " + date);
                  }
              }
          }

          // After processing all data, check if data was found and update the label
          if (!dataFound) {
              lbl = new Label("No Data For " + Category + " Category!");
          } else {
              for (Map.Entry<String, Double> entry : monthlyCreditTotals.entrySet()) {
                  creditSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
              }
          }

      } catch (IOException e) {
          e.printStackTrace();
      }

      // Add the series data to the chart and return
      graphs(barChart);
      barChart.getData().add(creditSeries);
      return barChart;
  }

    public static Label getLabel() {
      return lbl;
  }


}