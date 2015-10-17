package hu.bme.analytics.hems.ui.rapidminer.linkedin;

public class LinkedInProfile {
	private String name;
	private double experienceInYears;
	private String profilePicLink;
	
	
	public LinkedInProfile(String name, double experienceInYears,
			String profilePicLink) {
		super();
		this.name = name;
		this.experienceInYears = experienceInYears;
		this.profilePicLink = profilePicLink;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getExperienceInYears() {
		return experienceInYears;
	}


	public void setExperienceInYears(double experienceInYears) {
		this.experienceInYears = experienceInYears;
	}


	public String getProfilePicLink() {
		return profilePicLink;
	}


	public void setProfilePicLink(String profilePicLink) {
		this.profilePicLink = profilePicLink;
	}
	
}
