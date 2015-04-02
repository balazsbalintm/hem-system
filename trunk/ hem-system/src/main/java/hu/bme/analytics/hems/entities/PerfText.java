package hu.bme.analytics.hems.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PerfText {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(targetEntity=Project.class, fetch=FetchType.EAGER)
	private Project project;
	
	@ManyToOne(targetEntity=Employee.class, fetch=FetchType.EAGER)
	private Employee employee;
	
	private String perfEvaluationText;
	
	protected PerfText() {}

	public PerfText(Project project, Employee employee, String perfEvaluationText) {
		super();
		this.project = project;
		this.employee = employee;
		this.perfEvaluationText = perfEvaluationText;
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

	public String getPerfEvaluationText() {
		return perfEvaluationText;
	}

	public void setPerfEvaluationText(String perfEvaluationText) {
		this.perfEvaluationText = perfEvaluationText;
	}

	@Override
	public String toString() {
		return "PerfText [id=" + id + ", project=" + project + ", employee="
				+ employee + ", perfEvaluationText=" + perfEvaluationText + "]";
	}
	
	
}
