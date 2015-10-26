package hu.bme.analytics.hems.ui.components;

import hu.bme.analytics.hems.entities.LinkedInProfile;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import org.springframework.beans.factory.annotation.Autowired;

public class LinkedInBarChart extends StackPane {
	public LinkedInBarChart() {}
	
	@Autowired
	public void setLinkedInProfiles(List<LinkedInProfile> l_linkedInProfiles) {
		double maxExperience = java.util.Collections.max(l_linkedInProfiles).getExperienceInYears();
		
		final NumberAxis xAxis = new NumberAxis(0.0, maxExperience, 0.5);
		final CategoryAxis yAxis = new CategoryAxis();
		final BarChart<Number, String> bc = new BarChart<Number, String>(xAxis, yAxis);
		bc.setTitle("Linked in profile - experiences");
		xAxis.setLabel("Experience in years");
		xAxis.setTickLabelRotation(90);
		yAxis.setLabel("Profile owner");
		
		XYChart.Series serResults = new XYChart.Series();
		for(int i = 0; i < l_linkedInProfiles.size(); i++) {
			LinkedInProfile actProfile = l_linkedInProfiles.get(i);
			
			XYChart.Data data = new XYChart.Data(
					actProfile.getExperienceInYears(),
					actProfile.getName()
					);
			
			serResults.getData().add(data);
			
		}

		
		bc.getData().addAll(serResults);
		
		
		for(int i = 0; i < l_linkedInProfiles.size(); i++) {
			LinkedInProfile actProfile = l_linkedInProfiles.get(i);
			
			StringBuilder sb = new StringBuilder();
			sb.append("-fx-background-image: url(\"");
			sb.append(actProfile.getProfilePicLink());
			sb.append("\");");
			sb.append("-fx-background-repeat: no-repeat;-fx-background-size: contain;-fx-background-radius:15;");
			
			for(Node n:bc.lookupAll(".data" + i + ".chart-bar")) {
				n.setStyle(sb.toString());
			}
		}
		
		this.getChildren().add(bc);
	}

}
