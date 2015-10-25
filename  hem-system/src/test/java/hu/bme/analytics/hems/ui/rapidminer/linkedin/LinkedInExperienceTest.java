package hu.bme.analytics.hems.ui.rapidminer.linkedin;

public class LinkedInExperienceTest {

	public static void main(String[] args) {
		String path_profiles = "E:\\Workplace\\github\\hem-system\\ hem-system\\linkedin-profiles-list.csv";

		LinkedInProfileImport lipi = new LinkedInProfileImport();
		lipi.importProfilesIntoDB(path_profiles);
	}

}
