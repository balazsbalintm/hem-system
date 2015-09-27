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
import org.jsoup.select.Elements;

public class LinkedInProcessor {
	private static String EXPERIENCE_CONTAINER = "background-experience";
	private static String EXPERIENCE_HEADER = "header";
	private static String EXPERIENCE_LOGO = "experience-logo";
	private static String EXPERIENCE_DESCRIPTION = "description";
	
	private List<String> l_profileLinks = new ArrayList<String>();
	
	public LinkedInProcessor(String profiles) {
		try {
			
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
				/*InputStream is_profile = new URL(actProfile).openStream();
				XMLInputFactory factory = XMLInputFactory.newFactory();
				XMLStreamReader reader = factory.createXMLStreamReader(is_profile);*/
				
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setIgnoringElementContentWhitespace(true);
				
				URL urlProfil = new URL(actProfile);
				    try {
				    	Document parsedProfile = Jsoup.parse(urlProfil, 8000);
				        Element element = parsedProfile.getElementById(EXPERIENCE_CONTAINER);
				        
				        Elements projectElements =new Elements();
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
				        }
				        
				    } catch (Exception e) {
				    	
				    }
			}
			
			/*
			 * The words that should be searched in case of text mining. Search the public profile and insert it into the datbase:
			 * div - background-experience-container
			 * 		<div id="experience-538432817"... ></div>
			 * 		---> experience-ID
			 * 		<h5> --> logo
			 * 		<h4> --> position
			 * 		<h5> --> company
			 */
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
