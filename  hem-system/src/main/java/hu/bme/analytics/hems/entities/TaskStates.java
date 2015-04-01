package hu.bme.analytics.hems.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TaskStates {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String state;
	
	@ElementCollection
	private Set<String> validStates = new HashSet<String>(Arrays.asList("OPEN", "CLOSED", "IN_PROGRESS", "CANCELLED"));
	
	public TaskStates() {}
	public TaskStates(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if(validStates.contains(state))
			this.state = state;
	}

	public Set<String> getValidStates() {
		return validStates;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
