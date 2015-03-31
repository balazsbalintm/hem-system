package hu.bme.analytics.hems.ui.rapidminer;

import hu.bme.analytics.hems.entities.PersonDistanceResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.XMLException;

public class ModelCaller {
	private static String PERSON_ID = "personId";
	private static String ID = "id";
	private static String REQUEST = "request";
	private static String DISTANCE = "distance";	
	
	private static Map<Double, Double> m_reqIdAndResults = new HashMap<Double, Double>();
	
	public static List<PersonDistanceResult> executeCandidateSearchModel(String searchText) {
		try {
			RapidMiner.init();
			Process process = RapidMiner.readProcessFile(new File("C:\\Users\\Balint\\Desktop\\hrManagmentProcess"));
	
	
			process.getOperator("search_expression").setParameter("text", searchText);
	
			List<PersonDistanceResult> l_results = new ArrayList<PersonDistanceResult>();
	
			IOContainer ioResult = process.run();
			if (ioResult.getElementAt(0) instanceof ExampleSet) {
				ExampleSet resultSet = (ExampleSet) ioResult.getElementAt(0);
	
				for (Example example : resultSet) {
					Iterator<Attribute> allAtts = example.getAttributes()
							.allAttributes();
					double requestId = -1;
					double distance = -1;
	
					while (allAtts.hasNext()) {
						Attribute a = allAtts.next();
	
						if (REQUEST.equals(a.getName())) {
							requestId = example.getValue(a);
						} else if (DISTANCE.equals(a.getName())) {
							distance = example.getValue(a);
						}
					}
					m_reqIdAndResults.put(requestId, distance);
				}
			}
	
			// gets the person id + ID together
			if (ioResult.getElementAt(1) instanceof ExampleSet) {
				ExampleSet resultSet = (ExampleSet) ioResult.getElementAt(1);
	
				for (Example example : resultSet) {
					Iterator<Attribute> allAtts = example.getAttributes()
							.allAttributes();
					double personId = -1;
					double id = -1;
	
					while (allAtts.hasNext()) {
						Attribute a = allAtts.next();
	
						if (PERSON_ID.equals(a.getName())) {
							personId = example.getValue(a);
						} else if (ID.equals(a.getName())) {
							id = example.getValue(a);
						}
					}
	
					l_results.add(new PersonDistanceResult(personId, m_reqIdAndResults.get(id)));
				}
			}
	
			Collections.sort(l_results);
			Collections.reverse(l_results);
			
			return l_results;
			
		} catch (InstantiationException | IllegalAccessException | XMLException | IOException | OperatorException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<PersonDistanceResult>();
	}
}