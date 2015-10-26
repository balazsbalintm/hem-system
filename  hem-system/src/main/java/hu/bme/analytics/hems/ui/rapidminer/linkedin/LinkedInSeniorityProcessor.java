package hu.bme.analytics.hems.ui.rapidminer.linkedin;

import hu.bme.analytics.hems.entities.LinkedInProfile;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkedInSeniorityProcessor {
	private final static Logger LOGGER = Logger.getLogger(LinkedInSeniorityProcessor.class.getName());
	private static String EXPERIENCE_CONTAINER = "background-experience";
	private static boolean TEST_MODE = true;
	private static int TRY_LIMIT = 4;
	
	private List<String> l_profileLinks = null;
	
	public List<LinkedInProfile> getLinkedInProfiles(String profilesPath) {
		if(TEST_MODE) {
			return getTestData();
		}
		
		try {
			l_profileLinks = LinkedInUtil.getLinkedInProfileList(profilesPath);

			List<LinkedInProfile> l_linkedInProfiles = new ArrayList<LinkedInProfile>();
			//Reading one-by-one the links from the profile list
			for(String actProfile : l_profileLinks) {
				Document parsedProfile = null;
		    	String personName = null;
		    	String profilePicLink = null;
		    	double sumYearsOfExp = -1.0;
		    	int j = 0;
		    	while (sumYearsOfExp < 0 && j < TRY_LIMIT) {
		    		//reach the linked in profile's URL
		    		URL urlProfil = new URL(actProfile);
		    		parsedProfile = Jsoup.parse(urlProfil, 8000);

			        personName = LinkedInUtil.getPersonName(parsedProfile);
			        LOGGER.info("Person name: " + personName);
			        
			        sumYearsOfExp = getYearsOfExperience(parsedProfile);
			        LOGGER.info("Sum experience: " + sumYearsOfExp);
			        
			        profilePicLink = LinkedInUtil.getProfilePicLink(parsedProfile);
			        LOGGER.info("Profile pic link: " + profilePicLink);
		    		
		    		j++;
		    		Thread.sleep(5000);
		    	}
		    	
		    	//if we reached the limit without success, not to add to the list the null values
		    	if(TRY_LIMIT == j)
		    		continue;
		        
		        l_linkedInProfiles.add(new LinkedInProfile(personName, sumYearsOfExp, profilePicLink));
			}
			
			return l_linkedInProfiles;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<LinkedInProfile>();
	}

	private static String MONTH = "month";
	private static String MONTHS = "months";
	private static String YEAR = "year";
	private static String YEARS = "years";
	private double getYearsOfExperience(Element parsedProfile) {
		Element projects = parsedProfile.getElementById(EXPERIENCE_CONTAINER);
		if (projects == null)
			return -1;
		
		Elements expDates = projects.select("span[class=experience-date-locale]");
		if(expDates == null || expDates.size() == 0 )
			return -1;
		
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
					LOGGER.info("MONTH: " + actExpDateArray[i-1]);
					sumMonths += Double.parseDouble(actExpDateArray[i-1]);
				}
				
				if(YEAR.equals(currentWord) || YEARS.equals(currentWord)) {
					LOGGER.info("YEAR: " + actExpDateArray[i-1]);
					sumYear += Double.parseDouble(actExpDateArray[i-1]);
				}
			}
		}
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
