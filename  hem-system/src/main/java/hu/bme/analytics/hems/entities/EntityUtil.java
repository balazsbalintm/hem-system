package hu.bme.analytics.hems.entities;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.HemsProps;
import hu.bme.analytics.hems.ui.rapidminer.InternalExternalSelector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EntityUtil {
	
	static {
		outputCsvPath = HemsProps.get().getProperty(HemsProps.RM_CSVPATH);
	}
	
	private static String outputCsvPath;
	
	public static void toCsvEmpAndTasks(InternalExternalSelector intExtSel) {
		Iterator<Project> it_allProjects = App.get().prjRep.findAll().iterator();
		
		CsvCreator csvCreator = new CsvCreator(outputCsvPath);
		//CSV HEADER STRUCTURE
		csvCreator.headerCreator("personId",
								 "personName",
								 "taskId",
								 "taskDesc");
		
		while (it_allProjects.hasNext()) {
			Map<Employee, TaskSet> actualPrj = it_allProjects.next().getM_assignments();
			Set<Employee> emps = actualPrj.keySet();
			
			for(Employee emp : emps) {
				if(	  (intExtSel == InternalExternalSelector.INTERNAL && !emp.isInternalEmployee())
				   || (intExtSel == InternalExternalSelector.EXTERNAL && emp.isInternalEmployee())) {
					continue;
				}
				
				TaskSet tasks = actualPrj.get(emp);

				for (ProjectTask task : tasks.getTasks()) {
					//append PERSON_ID
					csvCreator.appendToLine( Long.toString(emp.getId()) );
					//append PERSON_NAME
					csvCreator.appendToLine( emp.getFirstName() + " " + emp.getLastName() );
					//append TASK_ID
					csvCreator.appendToLine( task.getId().toString() );
					//append TASK_DESC
					csvCreator.appendToLine( task.getTaskDescription() );
					
					csvCreator.flushLineToFile();
				}
			}
		}
		csvCreator.flushToFile();
	}
	
	
	/*CSV builder*/
	private static class CsvCreator {
		private File file;
		
		private StringBuilder sb_csvFile = new StringBuilder();
		private StringBuilder sb_csvLine = new StringBuilder();
		
		public CsvCreator(String outputPath) {
			try {
				
				file = new File(outputPath);
				file.createNewFile();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		void headerCreator(String... headers) {
			sb_csvLine.delete(0, sb_csvLine.length());
			
			for(String header : headers) {
				sb_csvLine.append("\"");
				sb_csvLine.append(header);
				sb_csvLine.append("\";");
			}
			sb_csvLine.append("\r\n");
		}
		
		void appendToLine(String item_value) {
			sb_csvLine.append("\"");
			sb_csvLine.append(item_value);
			sb_csvLine.append("\";");
		}
		
		void flushLineToFile () {
			sb_csvLine.append("\r\n");		
			
			sb_csvFile.append(sb_csvLine.toString());
			sb_csvLine.delete(0, sb_csvLine.length());
		}
		
		void flushToFile () {
			try {
				PrintWriter pw = new PrintWriter(file);
				
				pw.write(sb_csvFile.toString());
				pw.flush();
				pw.close();
				sb_csvFile.delete(0, sb_csvFile.length());
				
			} catch (FileNotFoundException e) {e.printStackTrace();}
			
		}
	}
}
