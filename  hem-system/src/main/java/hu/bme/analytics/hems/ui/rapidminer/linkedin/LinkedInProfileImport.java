package hu.bme.analytics.hems.ui.rapidminer.linkedin;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.Project;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.entities.TaskSet;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkedInProfileImport {
	public LinkedInProfileImport() {}

	private List<String> l_profileLinks = null;
	
	private final static Logger LOGGER = Logger.getLogger(LinkedInProfileImport.class.getName());
	
	public void importProfilesIntoDB(String profilesPath, ProgressIndicator pi_profileImport) {
		Task task = new Task<Void>() {
			@Override protected Void call() throws Exception {
				try {
					l_profileLinks = LinkedInUtil.getLinkedInProfileList(profilesPath);

					//Reading one-by-one the links from the profile list
					for(int i = 0; i < l_profileLinks.size(); i++) {
						Thread.sleep(5000);
						
						//TODO: AN INTERNAL WHILE CYCLE TO REACH A FILLED TASK DESCRIPTION
						
						String actProfile = l_profileLinks.get(i);
						URL urlProfil = new URL(actProfile);
						
				    	Document parsedProfile = Jsoup.parse(urlProfil, 8000);

				    	//search for the name in order to create the employee profile
				    	String[] fullName = LinkedInUtil.getPersonNameSeparated(parsedProfile);
				    	Employee empLinkedIn = LinkedInUtil.addLinkedInPerson(fullName[0], fullName[1]);
				    	
				    	//create the technical project task based on the LinkedIn profile experience
				    	String taskDescription = getProfileExperience(parsedProfile);
				    	ProjectTask taskLinkedIn = LinkedInUtil.addLinkedInTask(taskDescription);
				    	LOGGER.info("The following task description was retreived from the profile: " + taskDescription);
				    	
				    	//get the LinkedIn project which is a technical project in DB
				    	Project prjLinkedIn = LinkedInUtil.getLinkedInProject();
				    	
				    	//commit project data
				    	TaskSet taskSetLinkedIn = prjLinkedIn.assignTaskToEmployee(empLinkedIn, taskLinkedIn);
				    	App.get().taskSetRep.save(taskSetLinkedIn);
				    	App.get().prjRep.save(prjLinkedIn);
				    	
				    	Platform.runLater(new ProgressIndicatorThread(pi_profileImport, l_profileLinks.size(), i+1));
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
		    }

			@Override
			protected void succeeded() {}
		};
		
		Thread thrExpBarchart = new Thread(task);
		thrExpBarchart.start();
	}

	private String getProfileExperience(Document parsedProfile) {
		StringBuilder sbExperience = new StringBuilder();
		
		//Select the experience div in order to get all of the historical experience data
		//id=background-experience-container
		Elements expNodes = parsedProfile.select("div[id=background-experience-container]");
		for (Element currExpNode : expNodes) {
			
			//P tag which contains "description" class inside of the experience tag is a detailed description of the job
			//class=description
			Elements descNodes = currExpNode.select("p[class*=description]");
			for(Element currDescNode : descNodes) {
				sbExperience.append(currDescNode.text());
				sbExperience.append(" ");
			}
		}
		
		return sbExperience.toString();
	}
	
	private class ProgressIndicatorThread implements Runnable {
		private ProgressIndicator pi_profileImport;
		private double finalValue;
		private double actualValue;
		
		public ProgressIndicatorThread(ProgressIndicator pi_profileImport, double finalValue, double actualValue) {
			this.pi_profileImport = pi_profileImport;
			this.finalValue = finalValue;
			this.actualValue = actualValue;
		}
		
		@Override
		public void run() {
			double progressStatus = actualValue / finalValue;
			pi_profileImport.setProgress(progressStatus);
		}
		
	}
}
