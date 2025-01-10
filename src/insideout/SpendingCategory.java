package insideout;

import javafx.scene.chart.PieChart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Label;
import static insideout.InsideOut.piechart;

public class SpendingCategory {
    private static Label lbl=new Label();
    private static String username;
    
    public SpendingCategory(String username){
      this.username=username;
    }
    
    public static PieChart SpendingCategoryChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Spending Distribution");

        String filePath = "src/recordcredit.csv";
        Map<String, Double> userCategoryAmounts = new HashMap<>();
        double totalAmount=0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                String name = data[0];
                double amount = Double.parseDouble(data[3]);
                String category = data[7];

                if (username.equals(name)) {
                    userCategoryAmounts.put(category, userCategoryAmounts.getOrDefault(category, 0.0) + amount);
                    totalAmount += amount;
                }
            }

            if (userCategoryAmounts.isEmpty()) {
                lbl=new Label("No Data Found!");
            } else {
                for (Map.Entry<String, Double> entry : userCategoryAmounts.entrySet()) {
                    String category = entry.getKey();
                    double amount = entry.getValue();
                    double percentage = (amount / totalAmount) * 100;

                    pieChart.getData().add(new PieChart.Data(category + "(" + String.format("%.2f", percentage) + "%)", amount));
                   
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        piechart(pieChart);
        return pieChart;
    }
    
    public static Label getLabel(){
      return lbl;
    }
    
  
   
}