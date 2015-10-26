package hu.bme.analytics.hems.entities.repositories;

import hu.bme.analytics.hems.entities.Project;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	Project findByProjectNameOrderByProjectNameAsc(String projectName);
	
	List<Project> findByIsInternalProjectOrderByProjectNameAsc(Boolean isInternalProject);
	
}
