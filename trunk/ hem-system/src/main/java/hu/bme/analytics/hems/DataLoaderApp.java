package hu.bme.analytics.hems;

import hu.bme.analytics.hems.entities.repositories.EmployeeRepository;
import hu.bme.analytics.hems.entities.repositories.PerfStatRepository;
import hu.bme.analytics.hems.entities.repositories.PerfTextRepository;
import hu.bme.analytics.hems.entities.repositories.ProjectRepository;
import hu.bme.analytics.hems.entities.repositories.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataLoaderApp implements CommandLineRunner {

	@Autowired
	EmployeeRepository empRepository;
	@Autowired
	PerfStatRepository perStatRepository;
	@Autowired
	PerfTextRepository perfTextRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	TaskRepository taskRepository;
	
	@Override
	public void run(String... arg0) throws Exception {
		
	}

}
