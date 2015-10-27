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
						String actProfile = l_profileLinks.get(i);
						
						Document parsedProfile = null;
				    	String taskExpDescription = null; 
				    	String[] fullName = null;
				    	int j = 0;
				    	while (taskExpDescription == null && j < 4) {
				    		//reach the linked in profile's URL
				    		URL urlProfil = new URL(actProfile);
				    		parsedProfile = Jsoup.parse(urlProfil, 8000);

				    		//get the name out of the parsed source
				    		fullName = LinkedInUtil.getPersonNameSeparated(parsedProfile);
				    		LOGGER.info("Name:" + fullName);
				    		
				    		//get the experience description out of the parser source
				    		taskExpDescription = getFullProfileExperience(parsedProfile);
				    		LOGGER.info("Task description: " + taskExpDescription);
				    		
				    		j++;
				    		Thread.sleep(5000);
				    	}
				    	
				    	//COMMIT ALL DATA TO DB
				    	//search for the name in order to create the employee profile
				    	Employee empLinkedIn = LinkedInUtil.addLinkedInPerson(fullName[0], fullName[1]);
				    	
				    	//get the LinkedIn project which is a technical project in DB
				    	Project prjLinkedIn = LinkedInUtil.getLinkedInProject();
				    	
				    	//create task, add it to the project with assigning to the linked in profile
				    	ProjectTask taskLinkedIn = LinkedInUtil.addLinkedInTask(taskExpDescription);
				    	
				    	//first, remove all assignments that had before (only need the latest import)
				    	prjLinkedIn.getM_assignments().remove(empLinkedIn);
				    	TaskSet taskSetLinkedIn = prjLinkedIn.assignTaskToEmployee(empLinkedIn, taskLinkedIn);
				    	
				    	//save objects
				    	App.get().taskSetRep.save(taskSetLinkedIn);
				    	App.get().prjRep.save(prjLinkedIn);
				    	
				    	LOGGER.info("LINKEDIN: data persisted from the web!");
				    	
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

	private String getFullProfileExperience(Document parsedProfile) {
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
			
			//H4 tag which contains the "Title" of the position.
			//It is important to include into the experience as in many cases the person uses other description words in the position title and description.
			Elements titleNodes = currExpNode.select("h4");
			for(Element currTitleNode : titleNodes) {
				sbExperience.append(currTitleNode.text());
				sbExperience.append(" ");
			}
		}
		
		return sbExperience.toString();
	}
	
	/**
	 *	A process indicator thread which updates LinkedInProfileImporter's process indicator's component. 
	 */
	private class ProgressIndicatorThread implements Runnable {
		private ProgressIndicator pi_profileImport;
		private double finalValue;
		private double actualValue;
		
		/**
		 * @param pi_profileImport The ProcessIndicator JavaFX components which should be updated.
		 * @param finalValue Expected highest (final) value which should be reached. Ie.: 10
		 * @param actualValue Progress value which falls between [0,finalValue]
		 */
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
