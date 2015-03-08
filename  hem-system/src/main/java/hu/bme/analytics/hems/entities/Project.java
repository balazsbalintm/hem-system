package hu.bme.analytics.hems.entities;

import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@OneToMany(targetEntity=Employee.class, fetch=FetchType.EAGER)
	private List<Employee> l_employees;
	
	@OneToMany(targetEntity=Task.class, fetch=FetchType.EAGER)
	private List<Task> l_tasks;
	
	@ElementCollection
	private Map<Employee, Task> m_assignments;
	
	protected Project() {
		
	}
	
	public Project(String projectName, List<Employee> l_employees, List<Task> l_tasks, Map<Employee, Task> m_assignments) {
		this.projectName = projectName;
		this.l_employees = l_employees;
		this.l_tasks = l_tasks;
		this.m_assignments = m_assignments;
	}

	
	@Override
	public String toString() {
		return "Project [id=" + id + ", projectName=" + projectName
				+ ", l_employees=" + l_employees + ", l_tasks=" + l_tasks
				+ ", m_assignments=" + m_assignments + "]";
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

	public List<Employee> getL_employees() {
		return l_employees;
	}

	public void setL_employees(List<Employee> l_employees) {
		this.l_employees = l_employees;
	}

	public List<Task> getL_tasks() {
		return l_tasks;
	}

	public void setL_tasks(List<Task> l_tasks) {
		this.l_tasks = l_tasks;
	}

	public Map<Employee, Task> getM_assignments() {
		return m_assignments;
	}

	public void setM_assignments(Map<Employee, Task> m_assignments) {
		this.m_assignments = m_assignments;
	}
	
}
