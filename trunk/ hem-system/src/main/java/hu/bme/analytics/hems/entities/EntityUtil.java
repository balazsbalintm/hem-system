package hu.bme.analytics.hems.entities;

import hu.bme.analytics.hems.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class EntityUtil {
	
	@Value("${app.rm.modelpath}")
	private String modelPath;
	
	@Value("${app.rm.csvpath}")
	private String outputCsvPath;
	
	public void toCsvEmpAndTasks() {
		Iterator<Project> it_allProjects = App.get().projectRep.findAll().iterator();
		
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
	
	private class CsvCreator {
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
