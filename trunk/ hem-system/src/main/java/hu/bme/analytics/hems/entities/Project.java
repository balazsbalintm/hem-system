package hu.bme.analytics.hems.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String projectName;

	@OneToMany
	private Map<Employee, TaskSet> m_assignments = new HashMap<Employee, TaskSet>();
	
	protected Project() {
		
	}
	
	public Project(String projectName) {
		super();
		this.projectName = projectName;
	}

	public TaskSet assignTaskToEmployee(Employee emp, ProjectTask task) {
		TaskSet taskSet;
		if (!m_assignments.containsKey(emp)){
			Set<ProjectTask> s_task = new HashSet<ProjectTask>();
			s_task.add(task);
			taskSet = new TaskSet(s_task);
			
			m_assignments.put(emp, taskSet);
		} else {
			taskSet = m_assignments.get(emp);
			taskSet.getTasks().add(task);
		}
		
		return taskSet;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
