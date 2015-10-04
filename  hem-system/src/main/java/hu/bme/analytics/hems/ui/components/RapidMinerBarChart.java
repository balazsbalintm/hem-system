package hu.bme.analytics.hems.ui.components;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.PersonDistanceResult;

import java.util.List;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

public class RapidMinerBarChart extends StackPane {
	final static String austria = "Austria";
	final static String brazil = "Brazil";
	final static String france = "France";
	final static String italy = "Italy";
	final static String usa = "USA";

	//public RapidMinerBarChart(List<PersonDistanceResult> l_personDistances) {
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
		
		XYChart.Series serResults = new XYChart.Series();
		for(PersonDistanceResult actPersDistance : l_personDistances) {
			Employee emp = App.get().empRep.findOne( (long)actPersDistance.getPersonId() );
			serResults.getData().add(
					new XYChart.Data(
							Math.round( actPersDistance.getDistance()*100.0)/100.0,
							emp.getFirstName() + " " + emp.getLastName()
							)
					);
		}		
		
		bc.getData().addAll(serResults);
		
		this.getChildren().add(bc);
	}
}
