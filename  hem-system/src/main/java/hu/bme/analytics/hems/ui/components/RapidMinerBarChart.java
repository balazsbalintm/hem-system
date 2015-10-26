package hu.bme.analytics.hems.ui.components;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.PersonDistanceResult;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

public class RapidMinerBarChart extends StackPane {

	public RapidMinerBarChart() {
	}
	
	public void setCandidateSearchResultData(List<PersonDistanceResult> l_personDistances) {
		this.getChildren().clear();
		
		final NumberAxis xAxis = new NumberAxis(0.0, 1.0, 0.05);
		final CategoryAxis yAxis = new CategoryAxis();
		final BarChart<Number, String> bc = new BarChart<Number, String>(xAxis, yAxis);
		bc.setTitle("Candidate Search result summary");
		xAxis.setLabel("Relevancy");
		xAxis.setTickLabelRotation(90);
		yAxis.setLabel("Candidate");
		
		List<Boolean> l_intExtEmps = new ArrayList<Boolean>();		
		XYChart.Series serResults = new XYChart.Series();
		for(int i = 0; i < l_personDistances.size(); i++) {
			PersonDistanceResult actPersDistance = l_personDistances.get(i);
			Employee emp = App.get().empRep.findOne( (long)actPersDistance.getPersonId() );
			
			
			serResults.getData().add(
					new XYChart.Data(
							Math.round( actPersDistance.getDistance()*100.0)/100.0,
							emp.getFirstName() + " " + emp.getLastName()
							)
					);
			
			l_intExtEmps.add(emp.isInternalEmployee());
		}		
		
		bc.getData().addAll(serResults);
		bc.setLegendVisible(false);
		updateBarChartColors(bc, l_intExtEmps);
		
		this.getChildren().add(bc);
	}
	
	private void updateBarChartColors(BarChart<Number,String> barChart, List<Boolean> l_intExtEmps){
		int j = 0;
		for(Node node:barChart.lookupAll(".default-color0.chart-bar")) {
			if (l_intExtEmps.get(j)) {
				node.setStyle("-fx-bar-fill: #ADFFB9;");
			} else {
				node.setStyle("-fx-bar-fill: #82A1FF;");
			}
			j++;
	    }
	}
}
