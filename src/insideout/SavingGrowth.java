package insideout;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static insideout.InsideOut.graphs;
import javafx.scene.control.Label;


public class SavingGrowth {
   private static Label lbl=new Label();
   private static String targetUsername;
   public SavingGrowth(String targetUsername){
     this.targetUsername=targetUsername;
   }
    public static BarChart<String, Number>  SavingGrowthChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Savings");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Savings Growth");
        barChart.setCategoryGap(50);

        XYChart.Series<String, Number> monthlySavingsSeries = new XYChart.Series<>();
        monthlySavingsSeries.setName("Total Savings");

        String filePath = "src/savings.csv";

         boolean userFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowCount = 0;
            int monthCount = 0; // To limit to 4 months
            double previousMonthlySavings = Double.MAX_VALUE;
            double currentMonthlySavings = 0;
            double totalSavings = 0;

            while ((line = br.readLine()) != null) {
                if (rowCount == 0) { 
                    rowCount++;
                    continue;
                }

                String[] data = line.split(",");
                if(data.length<4){
                continue;
                }
                String username = data[0];
            
                if (username.equalsIgnoreCase(targetUsername)) {
                    userFound = true; 
                    
                    currentMonthlySavings = Double.parseDouble(data[7]);
                    totalSavings = Double.parseDouble(data[5]);


                    if (currentMonthlySavings <= previousMonthlySavings) { // when total savings less than 
                        monthCount++;
                        monthlySavingsSeries.getData().add(new XYChart.Data<>("Month " + monthCount, totalSavings));
                        previousMonthlySavings = currentMonthlySavings;
                    } else { // greater than or equals 
                        monthlySavingsSeries.getData().add(new XYChart.Data<>("Month " + monthCount, totalSavings));
                        previousMonthlySavings = currentMonthlySavings;
                    }
                }
            }

            if (!userFound) {
               lbl=new Label("No Data Found!"); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        graphs(barChart);
        barChart.getData().addAll(monthlySavingsSeries);
      
        return barChart;
    }
    
    public static Label getLabel(){
       return lbl;    
    }
 

   
}