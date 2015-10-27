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
	private static boolean TEST_MODE = false;
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

	//static constants for holding words that should be looked for in the LinkedIn HTML code
	private static String MONTH = "month";
	private static String MONTHS = "months";
	private static String YEAR = "year";
	private static String YEARS = "years";
	private static String EXPERIENCE_CONTAINER = "background-experience";
	/**
	 * Based on the parsed LinkedIn profile, the function digs up the year of experience related entries, sums it and returns it as the sum experience in years of the profile.
	 * @return In a double format the number of years that many experience have. For example: 6.43 (years)
	 */
	private double getYearsOfExperience(Element parsedProfile) {
		//get the div which contains the work experience (jobs, not projects) of the profile
		Element projects = parsedProfile.getElementById(EXPERIENCE_CONTAINER);
		if (projects == null)
			return -1;
		
		//the year and month related entries in the experience div
		Elements expDates = projects.select("span[class=experience-date-locale]");
		if(expDates == null || expDates.size() == 0 )
			return -1;
		
		//walk through the found year and month related entries
		double sumYear = 0;
		double sumMonths = 0;
		for(Element actExpDate : expDates) {
			String actExpDateInHTML = actExpDate.html();
			//remove parenthesis and replace it with space
			actExpDateInHTML = actExpDateInHTML.replace("(", " ");
			actExpDateInHTML = actExpDateInHTML.replace(")", " ");
			
			//split the text based on space char. Example input: "6 years and 1 month"
			String[] actExpDateArray = actExpDateInHTML.split(" ");
			
			for(int i = 0; i < actExpDateArray.length; i++) {
				//below logic assumes if year/years/month/months found as a text, then on the previous(i-1)
				//position a number should stay. For example: 1 year, 6 years, 1 month, 5 months
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
		//return the year and the month articulated in years (6 month / 12 = 0.5 year)
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
