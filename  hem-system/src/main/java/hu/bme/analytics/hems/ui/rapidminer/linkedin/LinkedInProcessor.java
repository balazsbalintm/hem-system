package hu.bme.analytics.hems.ui.rapidminer.linkedin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class LinkedInProcessor {
	private static String EXPERIENCE_CONTAINER = "background-experience";
	private static boolean TEST_MODE = true;
	
	private List<String> l_profileLinks = new ArrayList<String>();
	
	public List<LinkedInProfile> getLinkedInProfiles(String profiles) {
		if(TEST_MODE) {
			return getTestData();
		}
		
		try {
			List<LinkedInProfile> l_linkedInProfiles = new ArrayList<LinkedInProfile>();
			
			File f_profileLinks = new File(profiles);
			FileInputStream fis = new FileInputStream(f_profileLinks);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			
			//Reading linked-in profiles as inputs
			String line = null;
			while ((line = br.readLine()) != null) {
				l_profileLinks.add(line);
			}
			br.close();
			
			
			//Reading one-by-one the links from the profile list
			for(String actProfile : l_profileLinks) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setIgnoringElementContentWhitespace(true);
				Thread.sleep(5000);
				URL urlProfil = new URL(actProfile);
				    try {
				    	Document parsedProfile = Jsoup.parse(urlProfil, 8000);
				        
				        String personName = getPersonName(parsedProfile);
				        System.out.println("Person name: " + personName);
				        
				        double sumYearsOfExp = getYearsOfExperience(parsedProfile);
				        System.out.println("Sum experience: " + sumYearsOfExp);
				        
				        String profilePicLink = getProfilePicLink(parsedProfile);
				        System.out.println("Profile pic link: " + profilePicLink);
				        
				        l_linkedInProfiles.add(new LinkedInProfile(personName, sumYearsOfExp, profilePicLink));
				        
				        /*Elements projectElements =new Elements();
				        
				        Elements outSiderElements = element.select("div[id^=experience-");
				        for(Element currElement : outSiderElements){
				        	Elements projectElement = currElement.select("div[id$=-view]");
				        	projectElements.addAll(projectElement);
				        }
				        
				        for (Element el : projectElements) {
				        	Elements description = el.getElementsByClass(EXPERIENCE_DESCRIPTION);
				        	System.out.println(description.get(0).unwrap());
				        	
				        	Elements position = el.select("h4");
				        	String positionText = position.get(0).select("a").get(0).unwrap().toString();
				        	
				        	System.out.println(positionText);
				        }*/
				        
				    } catch (Exception e) {
				    	
				    }
			}
			
			return l_linkedInProfiles;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<LinkedInProfile>();
	}
	
	private String getProfilePicLink(Document parsedProfile) {
		Element profilePicDiv = parsedProfile.select("div[class=profile-picture]").get(0);
		Element profilePicImg = profilePicDiv.select("img").get(0);
		
		return profilePicImg.attr("src");
	}

	private String getPersonName(Document parsedProfile) {
		Elements personNameElements = parsedProfile.select("title");
		if(personNameElements == null || personNameElements.size() == 0) {
			return "Not determinable name";
		}
		
		Node personNameElement = personNameElements.get(0).unwrap();
		return personNameElement.toString().split("\\|")[0];
	}

	private static String MONTH = "month";
	private static String MONTHS = "months";
	private static String YEAR = "year";
	private static String YEARS = "years";
	private double getYearsOfExperience(Element parsedProfile) {
		Element projects = parsedProfile.getElementById(EXPERIENCE_CONTAINER);
		Elements expDates = projects.select("span[class=experience-date-locale]");
		
		double sumYear = 0;
		double sumMonths = 0;
		for(Element actExpDate : expDates) {
			String actExpDateInHTML = actExpDate.html();
			actExpDateInHTML = actExpDateInHTML.replace("(", " ");
			actExpDateInHTML = actExpDateInHTML.replace(")", " ");
			
			String[] actExpDateArray = actExpDateInHTML.split(" ");
			
			for(int i = 0; i < actExpDateArray.length; i++) {
				String currentWord = actExpDateArray[i];
				if(MONTH.equals(currentWord) || MONTHS.equals(currentWord)) {
					System.out.println("MONTH: " + actExpDateArray[i-1]);
					sumMonths += Double.parseDouble(actExpDateArray[i-1]);
				}
				
				if(YEAR.equals(currentWord) || YEARS.equals(currentWord)) {
					System.out.println("YEAR: " + actExpDateArray[i-1]);
					sumYear += Double.parseDouble(actExpDateArray[i-1]);
				}
			}
		}
		System.out.println("---------------------");
		return sumYear + sumMonths/12;
	}
	
	/**
	 * Test data generator function. Returns dummy LinkedIn profiles.
	 * @return Dummy LinkedIn profile list.
	 */
	private List<LinkedInProfile> getTestData() {
		List<LinkedInProfile> l_expResults = new ArrayList<LinkedInProfile>();
		l_expResults.add(new LinkedInProfile("Balint Balazs", 6.833333333333334, "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/3/005/05e/2ee/27bf820.jpg"));
		l_expResults.add(new LinkedInProfile("Peter Kovacs", 11.25, "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/4/000/13f/2a3/1e9c98e.jpg"));
		l_expResults.add(new LinkedInProfile("Adam Somorjai", 6.5, "https://media.licdn.com/mpr/mpr/shrinknp_400_400/AAEAAQAAAAAAAACsAAAAJGUxNTg2N2E2LTRkN2QtNGI4MS05MDU5LTdjZDU0ZTRhOTFmYQ.jpg"));
		
		return l_expResults;
	}
}
