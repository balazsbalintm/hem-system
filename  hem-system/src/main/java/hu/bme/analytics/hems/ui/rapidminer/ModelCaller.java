package hu.bme.analytics.hems.ui.rapidminer;

import hu.bme.analytics.hems.HemsProps;
import hu.bme.analytics.hems.entities.EntityUtil;
import hu.bme.analytics.hems.entities.PersonDistanceResult;
import hu.bme.analytics.hems.ui.controller.HemsController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.XMLException;

public class ModelCaller {
	private final static Logger LOGGER = Logger.getLogger(ModelCaller.class.getName());
	
	//STATIC INIT
	static {
		PATH_MODEL = HemsProps.get().getProperty(HemsProps.RM_MODELPATH);
		PATH_CSV = HemsProps.get().getProperty(HemsProps.RM_CSVPATH);
	}
	
	//CONSTANTS
	private static String PERSON_ID = "personId";
	private static String ID = "id";
	private static String REQUEST = "request";
	private static String DISTANCE = "distance";
	
	//MODEL+CSV PATH
	private static String PATH_MODEL;
	private static String PATH_CSV;
	
	private static Map<Double, Double> m_reqIdAndResults = new HashMap<Double, Double>();
	
	public static List<PersonDistanceResult> executeCandidateSearchModel(String searchText, InternalExternalSelector intExtSel) {
		try {
			EntityUtil.toCsvEmpAndTasks(intExtSel);
			
			RapidMiner.init();
			Process process = RapidMiner.readProcessFile(new File(PATH_MODEL));
	
			process.getOperator("input_csv").setParameter("csv_file", PATH_CSV);
			LOGGER.info("MODELCALLER: Path of input file: " + PATH_CSV);
			process.getOperator("search_expression").setParameter("text", searchText);
			LOGGER.info("MODELCALLER: Expression searched for: " + searchText);
			
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
					LOGGER.info("MODELCALLER: Request ID - " + requestId + "; Distance - " + distance);
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