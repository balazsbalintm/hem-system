package hu.bme.analytics.hems.ui.rapidminer.linkedin;

public class LinkedinProcessorTest {

	public static void main(String[] args) {
		String path_profiles = "E:\\Workplace\\github\\hem-system\\ hem-system\\linkedin-profiles-list.csv";
		LinkedInProcessor lip = new LinkedInProcessor();
		lip.getLinkedInProfiles(path_profiles);
		
	}

}
