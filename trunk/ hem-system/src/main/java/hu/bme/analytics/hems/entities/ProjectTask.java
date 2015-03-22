package hu.bme.analytics.hems.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ProjectTask {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String taskName;
	private String taskPosition;
	
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
	
}
