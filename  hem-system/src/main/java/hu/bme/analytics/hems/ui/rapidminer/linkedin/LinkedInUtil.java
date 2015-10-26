package hu.bme.analytics.hems.ui.rapidminer.linkedin;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.Project;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.entities.TaskStates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class LinkedInUtil {
	public static List<String> getLinkedInProfileList(String profilesPath) throws IOException {
		List<String> l_profileLinks = new ArrayList<String>();
		
		File f_profileLinks = new File(profilesPath);
		FileInputStream fis = new FileInputStream(f_profileLinks);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		//Reading linked-in profiles as inputs
		String line = null;
		while ((line = br.readLine()) != null) {
			l_profileLinks.add(line);
		}
		br.close();
		
		return l_profileLinks;
	}
	
	public static String getProfilePicLink(Document parsedProfile) {
		Element profilePicDiv = parsedProfile.select("div[class=profile-picture]").get(0);
		Element profilePicImg = profilePicDiv.select("img").get(0);
		
		return profilePicImg.attr("src");
	}

	public static String[] getPersonNameSeparated(Document parsedProfile) {
		String fullName = getPersonName(parsedProfile);
		String[] result = new String[2];
		
		if(fullName == null)
			return result;
		
		int sepIndx = fullName.indexOf(" ");
		result[0] = fullName.substring(0, sepIndx).trim();
		result[1] = fullName.substring(sepIndx+1, fullName.length()).trim();
		
		return result;
	}
	
	public static String getPersonName(Document parsedProfile) {
		Elements personNameElements = parsedProfile.select("title");
		if(personNameElements == null || personNameElements.size() == 0) {
			return "Not determinable name";
		}
		
		Node personNameElement = personNameElements.get(0).unwrap();
		return personNameElement.toString().split("\\|")[0];
	}
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private static String LINKEDIN_PROJECT_NAME = "LINKEDIN";
	/**
	 * For LinkedIn development purposes there needs to be a "special" project in the database which is not used for normal performance evaluation, but 
	 * just as a technicality as otherwise cannot exploit   
	 * @return LinkedIn project.
	 */
	public static Project getLinkedInProject() {
		Project linkedInProject = App.get().prjRep.findByProjectNameOrderByProjectNameAsc(LINKEDIN_PROJECT_NAME);

		try {
			if(linkedInProject == null) {
				linkedInProject = new Project("LINKEDIN", sdf.parse("01-01-0001"), sdf.parse("01-01-2100"), 1.0, 0, false);
				App.get().prjRep.save(linkedInProject);
			}
		} catch (ParseException e) {e.printStackTrace();}
		
		return linkedInProject;
	}
	
	/**
	 * During the LinkedIn search of the candidates, in order to integrate their profile into the RapidMiner 
	 * their profile has to be saved into the database. After the insert, RapidMiner can reach these profiles.
	 * @param firstName First name of the profile.
	 * @param lastName	Last name of the profile.
	 * @return The created employee object.
	 */
	public static Employee addLinkedInPerson(String firstName, String lastName){
		Employee emp = App.get().empRep.findByFirstNameAndLastNameOrderByLastNameAsc(firstName, lastName);
		
		if(emp == null) {
			emp = new Employee(firstName, lastName, false);
			App.get().empRep.save(emp);
		}
		
		return emp;
	}

	public static ProjectTask addLinkedInTask(String taskDescription) {
		ProjectTask linkedInTask = null;
		try {
			TaskStates linkedInTaskState = new TaskStates("OPEN");
			linkedInTask = new ProjectTask(
						"LINKEDIN TASK",
						"LINKEDIN TASK",
						taskDescription,
						linkedInTaskState,
						sdf.parse("01-01-2100"),
						null,
						0.0);
		
			App.get().taskStatesRep.save(linkedInTaskState);
			App.get().prjTaskRep.save(linkedInTask);
			
			return linkedInTask;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
