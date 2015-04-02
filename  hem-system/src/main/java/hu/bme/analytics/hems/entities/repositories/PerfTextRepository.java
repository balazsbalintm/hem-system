package hu.bme.analytics.hems.entities.repositories;

import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.PerfText;
import hu.bme.analytics.hems.entities.Project;

import org.springframework.data.repository.CrudRepository;

public interface PerfTextRepository extends CrudRepository<PerfText, Long> {
	
	public PerfText findByProjectAndEmployee(Project project, Employee employee);
}
