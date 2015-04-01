package hu.bme.analytics.hems.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.prefs.InvalidPreferencesFormatException;

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

	@OneToMany(fetch= FetchType.EAGER)
	private Map<Employee, TaskSet> m_assignments = new HashMap<Employee, TaskSet>();
	
	private double qualityImportance = 0.9;
	private double timeImportance = 0.1;
	
	protected Project() {
		
	}

	

	public Project(String projectName, double qualityImportance,
			double timeImportance) {
		super();
		this.projectName = projectName;
		
		try {
			checkQualityAndTimeImp(qualityImportance, timeImportance);
		} catch (InvalidPreferencesFormatException e) {
			this.qualityImportance = 0.5;
			this.timeImportance = 0.5;
		}
	}

	private void checkQualityAndTimeImp(double qualityImportance, double timeImportance) throws InvalidPreferencesFormatException{
		if(qualityImportance < 0 || timeImportance < 0 || qualityImportance > 1 || timeImportance > 1) 
			throw new InvalidPreferencesFormatException("Values cannot be under 0 or above 1!");
		
		if(qualityImportance + timeImportance != 1)
			throw new InvalidPreferencesFormatException("Quality + time importance has to be 1 in sum!");
		
		this.qualityImportance = qualityImportance;
		this.timeImportance = timeImportance;
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

	public Map<Employee, TaskSet> getM_assignments() {
		return m_assignments;
	}

	public void setM_assignments(Map<Employee, TaskSet> m_assignments) {
		this.m_assignments = m_assignments;
	}



	public double getQualityImportance() {
		return qualityImportance;
	}



	public void setQualityImportance(double qualityImportance) {
		try {
			checkQualityAndTimeImp(qualityImportance, this.timeImportance);
		} catch (InvalidPreferencesFormatException e) {
			return;
		}
	}



	public double getTimeImportance() {
		return timeImportance;
	}

	public void setTimeImportance(double timeImportance) {
		try {
			checkQualityAndTimeImp(this.qualityImportance, timeImportance);
		} catch (InvalidPreferencesFormatException e) {
			return;
		}
	}
	
	public void setTimeAndQualityImportance(double qualityImportance, double timeImportance) {
		try {
			checkQualityAndTimeImp(qualityImportance, timeImportance);
		} catch (InvalidPreferencesFormatException e) {
			this.qualityImportance = 0.5;
			this.timeImportance = 0.5;
		}
	}
	
}
