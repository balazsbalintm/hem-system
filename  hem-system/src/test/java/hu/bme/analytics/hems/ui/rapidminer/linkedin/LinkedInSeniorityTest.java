package hu.bme.analytics.hems.ui.rapidminer.linkedin;

import hu.bme.analytics.hems.entities.LinkedInProfile;
import hu.bme.analytics.hems.ui.components.LinkedInBarChart;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LinkedInSeniorityTest extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		String path_profiles = "E:\\Workplace\\github\\hem-system\\ hem-system\\linkedin-profiles-list.csv";
		List<LinkedInProfile> l_expResults = null;
		
		LinkedInSeniorityProcessor lip = new LinkedInSeniorityProcessor();
		l_expResults = lip.getLinkedInProfiles(path_profiles);
		
		LinkedInBarChart libc_experience = new LinkedInBarChart();
		libc_experience.setLinkedInProfiles(l_expResults);
		
		primaryStage.setTitle("Hello World!");
        
        StackPane root = new StackPane();
        root.getChildren().add(libc_experience);
        primaryStage.setScene(new Scene(root, 500, 650));
        primaryStage.show();
	}
	
}
