package tuner;

import java.util.List;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

public class Grapher extends Application {
	
	XYChart.Series<Number, Number> series = new XYChart.Series<>();;
	private ExecutorService executor;
    private List<Pitch> data = new ArrayList<>();
	
    private NumberAxis xAxis;
    
    private void init(Stage primaryStage) {
    		xAxis = new NumberAxis();
		xAxis.setLabel("Time (s)");
		
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Pitch (Hz)");
		
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };
        
        lineChart.setAnimated(false);
        lineChart.setTitle("Pitch");
        
        lineChart.getData().add(series);
        
        primaryStage.setScene(new Scene(lineChart));
    }
    
	public void start(Stage stage) throws Exception {
		init(stage);
		stage.show();
		
		prepareTimeline();
	}
	
	public void addData(Pitch pitch) {
		data.add(pitch);
	}
	
	//-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }
    
    private void addDataToSeries() {
        for (int i = 0; i < data.size(); i++) { //-- add 20 numbers to the plot+
            if (data.isEmpty()) break;
            series.getData().add(new XYChart.Data<>(data.get(i).getTime(), data.get(i).getPitch()));
        }
    }
	
	
	public static void main(String[] args) {
        Application.launch(args);
    }
}
