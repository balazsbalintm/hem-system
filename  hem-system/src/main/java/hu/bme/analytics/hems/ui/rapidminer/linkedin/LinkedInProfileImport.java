package hu.bme.analytics.hems.ui.rapidminer.linkedin;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.LinkedInProfile;
import hu.bme.analytics.hems.entities.Project;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.entities.TaskSet;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkedInProfileImport {
	public LinkedInProfileImport() {}

	private List<String> l_profileLinks = null;
	
	private final static Logger LOGGER = Logger.getLogger(LinkedInProfileImport.class.getName());
	
	public void importProfilesIntoDB(String profilesPath) {
		try {
			l_profileLinks = LinkedInUtil.getLinkedInProfileList(profilesPath);

			List<LinkedInProfile> l_linkedInProfiles = new ArrayList<LinkedInProfile>();

			//Reading one-by-one the links from the profile list
			for(String actProfile : l_profileLinks) {
				Thread.sleep(5000);
				URL urlProfil = new URL(actProfile);
				
		    	Document parsedProfile = Jsoup.parse(urlProfil, 8000);

		    	//search for the name in order to create the employee profile
		    	String[] fullName = LinkedInUtil.getPersonNameSeparated(parsedProfile);
		    	Employee empLinkedIn = LinkedInUtil.addLinkedInPerson(fullName[0], fullName[1]);
		    	
		    	//create the technical project task based on the LinkedIn profile experience
		    	String taskDescription = getProfileExperience(parsedProfile);
		    	ProjectTask taskLinkedIn = LinkedInUtil.addLinkedInTask(taskDescription);
		    	LOGGER.info("The following task description was retreived from the profile: ");
		    	LOGGER.info(taskDescription);
		    	
		    	//get the LinkedIn project which is a technical project in DB
		    	Project prjLinkedIn = LinkedInUtil.getLinkedInProject();
		    	
		    	//commit project data
		    	TaskSet taskSetLinkedIn = prjLinkedIn.assignTaskToEmployee(empLinkedIn, taskLinkedIn);
		    	App.get().taskSetRep.save(taskSetLinkedIn);
		    	App.get().prjRep.save(prjLinkedIn);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
