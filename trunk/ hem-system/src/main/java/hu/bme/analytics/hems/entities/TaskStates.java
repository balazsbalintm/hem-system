package hu.bme.analytics.hems.entities;

import javax.persistence.Entity;

@Entity
public enum TaskStates {
	OPEN,
	CLOSED,
	IN_PROGRESS,
	CANCELLED
}
