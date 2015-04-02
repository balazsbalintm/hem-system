package hu.bme.analytics.hems.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PerfStat {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(targetEntity=Project.class, fetch=FetchType.EAGER)
	private Project project;
	
	@ManyToOne(targetEntity=Employee.class, fetch=FetchType.EAGER)
	private Employee employee;
	
	private Integer nrOfOpenTasks;
	private Integer nrOfClosedTasks;
	private Integer nrOfInprogressTasks;
	private Integer spentTimeInH;

	protected PerfStat() {

	}

	public PerfStat(Project project, Employee employee) {
		this.project = project;
		this.employee = employee;
	}
	
	

	public PerfStat(Project project, Employee employee, Integer nrOfOpenTasks,
			Integer nrOfClosedTasks, Integer nrOfInprogressTasks,
			Integer spentTimeInH) {
		super();
		this.project = project;
		this.employee = employee;
		this.nrOfOpenTasks = nrOfOpenTasks;
		this.nrOfClosedTasks = nrOfClosedTasks;
		this.nrOfInprogressTasks = nrOfInprogressTasks;
		this.spentTimeInH = spentTimeInH;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Integer getNrOfOpenTasks() {
		return nrOfOpenTasks;
	}

	public void setNrOfOpenTasks(Integer nrOfOpenTasks) {
		this.nrOfOpenTasks = nrOfOpenTasks;
	}

	public Integer getNrOfClosedTasks() {
		return nrOfClosedTasks;
	}

	public void setNrOfClosedTasks(Integer nrOfClosedTasks) {
		this.nrOfClosedTasks = nrOfClosedTasks;
	}

	public Integer getNrOfInprogressTasks() {
		return nrOfInprogressTasks;
	}

	public void setNrOfInprogressTasks(Integer nrOfInprogressTasks) {
		this.nrOfInprogressTasks = nrOfInprogressTasks;
	}

	public Integer getSpentTimeInH() {
		return spentTimeInH;
	}

	public void setSpentTimeInH(Integer spentTimeInH) {
		this.spentTimeInH = spentTimeInH;
	}
	
	public int getSumTasks(){
		return nrOfClosedTasks + nrOfInprogressTasks + nrOfOpenTasks;
	}
	
}
