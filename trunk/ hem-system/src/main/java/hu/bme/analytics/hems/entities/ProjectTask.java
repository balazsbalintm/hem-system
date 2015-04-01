package hu.bme.analytics.hems.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class ProjectTask {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String taskName;
	private String taskPosition;
	
	@ManyToOne(targetEntity=TaskStates.class)
	private TaskStates state;
	private Date plannedEndDate;
	private Date actualEndDate;
	private double qualityInPercentage;
	
	@Lob
	@Column( length = 100000 )
	private String taskDescription;
	
	protected ProjectTask() {
	}
	
	public ProjectTask(String taskName, String taskPosition,
			String taskDescription) {
		super();
		this.taskName = taskName;
		this.taskPosition = taskPosition;
		this.taskDescription = taskDescription;
	}
	
	public ProjectTask(String taskName, String taskPosition, String taskDescription, TaskStates state,
			Date plannedEndDate, Date actualEndDate,
			double qualityInPercentage) {
		super();
		this.taskName = taskName;
		this.taskDescription = taskDescription;
		this.taskPosition = taskPosition;
		this.state = state;
		this.plannedEndDate = plannedEndDate;
		this.actualEndDate = actualEndDate;
		setQualityInPercentage(qualityInPercentage);		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskPosition() {
		return taskPosition;
	}

	public void setTaskPosition(String taskPosition) {
		this.taskPosition = taskPosition;
	}

	public TaskStates getState() {
		return state;
	}

	public void setState(TaskStates state) {
		this.state = state;
	}

	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public double getQualityInPercentage() {
		return qualityInPercentage;
	}
	
	public void setQualityInPercentage(double qualityInPercentage) {
		if(qualityInPercentage > 100)
			this.qualityInPercentage = 100;
		else if (qualityInPercentage < 0) 
			this.qualityInPercentage = 0;
		else
			this.qualityInPercentage = qualityInPercentage;
	}
	
}
