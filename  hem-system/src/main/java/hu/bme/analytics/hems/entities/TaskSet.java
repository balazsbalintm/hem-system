package hu.bme.analytics.hems.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class TaskSet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(fetch=FetchType.EAGER)
	private Set<ProjectTask> tasks = new HashSet<ProjectTask>();

	
	public TaskSet(Set<ProjectTask> tasks) {
		super();
		this.tasks = tasks;
	}

	public TaskSet() {
	}

	public Set<ProjectTask> getTasks() {
		return tasks;
	}

	public void setTasks(Set<ProjectTask> tasks) {
		this.tasks = tasks;
	}
	
	
}
