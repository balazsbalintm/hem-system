package hu.bme.analytics.hems.entities.repositories;

import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.PerfStat;
import hu.bme.analytics.hems.entities.Project;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PerfStatRepository extends CrudRepository<PerfStat, Long> {
	public List<PerfStat> findByProject(Project project);
	public PerfStat findByProjectAndEmployee(Project project, Employee employee);
}
