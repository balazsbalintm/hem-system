package hu.bme.analytics.hems.entities;


public class EmployeeTask {
	private Long empId;
	private String empName;
	private Long taskId;
	private String taskName;
	
	public EmployeeTask() {
	}
	
	public EmployeeTask(Long empId, String empName, Long taskId, String taskName) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.taskId = taskId;
		this.taskName = taskName;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
}
