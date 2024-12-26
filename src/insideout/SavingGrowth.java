package insideout;
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
import java.util.Scanner;
import javafx.scene.layout.AnchorPane;
import static insideout.InsideOut.graphs;

public class SavingGrowth {

    public static BarChart<String, Number>  SavingGrowthChart(String targetUsername,AnchorPane pane) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Savings");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Savings Growth for User: " + targetUsername);
        barChart.setCategoryGap(50);

        XYChart.Series<String, Number> monthlySavingsSeries = new XYChart.Series<>();
        monthlySavingsSeries.setName("Total Savings");

        String filePath = "/Users/cye/NewFolder/InsideOut/src/savings - Sheet1.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
             // Start with a high value
            int monthCount = 0; // To limit to 4 months
            double previousMonthlySavings = Double.MAX_VALUE;
            while ((line = br.readLine()) != null && monthCount < 5) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                String username = data[0];
                double currentMonthlySavings = Double.parseDouble(data[7]);
                double totalSavings = Double.parseDouble(data[5]);
                
                if (username.equals(targetUsername) && currentMonthlySavings <= previousMonthlySavings) {
                    
                    monthCount++;
                    monthlySavingsSeries.getData().add(new XYChart.Data<>("Month " + monthCount, totalSavings));
                    previousMonthlySavings = currentMonthlySavings;
                }else{

                    monthlySavingsSeries.getData().add(new XYChart.Data<>("Month " + monthCount, totalSavings));
                    previousMonthlySavings = currentMonthlySavings;
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphs(barChart);
        barChart.getData().addAll(monthlySavingsSeries);
        pane.getChildren().add(barChart);
        
        return barChart;
    }

   
}