package hu.bme.analytics.hems.ui.controller;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.HemsProps;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.EmployeeTask;
import hu.bme.analytics.hems.entities.LinkedInProfile;
import hu.bme.analytics.hems.entities.PerfStat;
import hu.bme.analytics.hems.entities.PerfText;
import hu.bme.analytics.hems.entities.PersonDistanceResult;
import hu.bme.analytics.hems.entities.Project;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.entities.TaskSet;
import hu.bme.analytics.hems.ui.components.AboutScene;
import hu.bme.analytics.hems.ui.components.LinkedInBarChart;
import hu.bme.analytics.hems.ui.components.ProjectIssueStatStackPane;
import hu.bme.analytics.hems.ui.components.RapidMinerBarChart;
import hu.bme.analytics.hems.ui.rapidminer.CandidateSearchService;
import hu.bme.analytics.hems.ui.rapidminer.InternalExternalSelector;
import hu.bme.analytics.hems.ui.rapidminer.linkedin.LinkedInProfileImport;
import hu.bme.analytics.hems.ui.rapidminer.linkedin.LinkedInSeniorityProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import org.springframework.stereotype.Component;

@Component
public class HemsController {
	private final static Logger LOGGER = Logger.getLogger(HemsController.class.getName());
	
	//data browser
	@FXML private TableView<Employee> tbl_persons;
	@FXML private TableView<Project> tbl_projects;
	@FXML private TableView<ProjectTask> tbl_tasks;
	@FXML private TableView<EmployeeTask> tbl_assignments;
	
	//candidate search
	@FXML private TextField tf_candidateSearch;
	@FXML private GridPane g_cadidateResult;
	@FXML private RapidMinerBarChart rmbc_candidateResults;
	@FXML private BorderPane bp_candSearch;
	@FXML private ComboBox<InternalExternalSelector> cb_candidateType;
	
	//performance evaluation
	@FXML private Tab tab_perfEval;
	@FXML private ComboBox<Project> cb_perfEval_project;
	@FXML private ComboBox<Employee> cb_perfEval_employee;
	@FXML private TextArea ta_perfText;
	
	//issue statistics
	@FXML private Tab tab_issueStats;
	@FXML private VBox vb_issueStatFrame;
	@FXML private VBox vb_issueStat;
	@FXML private PieChart pc_prjOverall;
	@FXML private ComboBox<Project> cb_issueStat_project;
	
	//LinkedIn functions
	@FXML private Tab tab_linkedIn;
	@FXML private BorderPane bp_linkedIn;
	@FXML private LinkedInBarChart libc_experiences;
	@FXML private ProgressIndicator pi_profileImport;
	
	public HemsController() {}
	
	//**********************
	//DATA BROWSER FUNCTIONS
	//**********************
	public void personDataDownloadClickHandler(MouseEvent me) {
		Iterator<Employee> it_emps = App.get().empRep.findAll().iterator();
		List<Employee> l_emps = new ArrayList<Employee>();
		
		while(it_emps.hasNext()) {
			l_emps.add(it_emps.next());
		}
		
		TableColumn<Employee,Long> empIdCol = new TableColumn<Employee,Long>();
		empIdCol.setText("Emp id");
		empIdCol.setCellValueFactory(new PropertyValueFactory<Employee,Long>("id"));
		
		TableColumn<Employee,String> empFirstNameCol = new TableColumn<Employee,String>();
		empFirstNameCol.setText("First name");
		empFirstNameCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("firstName"));
		
		TableColumn<Employee,String> empLastNameCol = new TableColumn<Employee,String>();
		empLastNameCol.setText("Last name");
		empLastNameCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("lastName"));
		
		ObservableList<Employee> lfx_emps = FXCollections.observableList(l_emps);
		tbl_persons.setItems(lfx_emps);
		
		tbl_persons.getColumns().addAll(empIdCol,empFirstNameCol,empLastNameCol);
	}
	
	public void taskDataDownloadClickHandler(MouseEvent me) {
		Iterator<ProjectTask> it_tasks = App.get().prjTaskRep.findAll().iterator();
		List<ProjectTask> l_tasks = new ArrayList<ProjectTask>();
		
		while(it_tasks.hasNext()) {
			l_tasks.add(it_tasks.next());
		}
		
		TableColumn<ProjectTask,Long> taskIdCol = new TableColumn<ProjectTask,Long>();
		taskIdCol.setText("Task id");
		taskIdCol.setCellValueFactory(new PropertyValueFactory<ProjectTask,Long>("id"));
		
		TableColumn<ProjectTask,String> taskNameCol = new TableColumn<ProjectTask,String>();
		taskNameCol.setText("Task name");
		taskNameCol.setCellValueFactory(new PropertyValueFactory<ProjectTask,String>("taskName"));
		
		TableColumn<ProjectTask,String> taskPositionCol = new TableColumn<ProjectTask,String>();
		taskPositionCol.setText("Task position");
		taskPositionCol.setCellValueFactory(new PropertyValueFactory<ProjectTask,String>("taskPosition"));
		
		TableColumn<ProjectTask,String> taskDescCol = new TableColumn<ProjectTask,String>();
		taskDescCol.setText("Task Description");
		taskDescCol.setCellValueFactory(new PropertyValueFactory<ProjectTask,String>("taskDescription"));
		
		ObservableList<ProjectTask> lfx_tasks = FXCollections.observableList(l_tasks);
		tbl_tasks.setItems(lfx_tasks);
		
		tbl_tasks.getColumns().addAll(taskIdCol,taskNameCol,taskPositionCol, taskDescCol);
	}
	
	public void projectDataDownloadClickHandler(MouseEvent me) {
		Iterator<Project> it_projects = App.get().prjRep.findAll().iterator();
		List<Project> l_projects = new ArrayList<Project>();
		
		while(it_projects.hasNext()) {
			l_projects.add(it_projects.next());
		}
		
		TableColumn<Project,Long> projectIdCol = new TableColumn<Project,Long>();
		projectIdCol.setText("Project id");
		projectIdCol.setCellValueFactory(new PropertyValueFactory<Project,Long>("id"));
		
		TableColumn<Project,String> taskNameCol = new TableColumn<Project,String>();
		taskNameCol.setText("Project name");
		taskNameCol.setCellValueFactory(new PropertyValueFactory<Project,String>("projectName"));
		
		TableColumn<Project,Double> taskQualityImpCol = new TableColumn<Project,Double>();
		taskQualityImpCol.setText("Quality importance");
		taskQualityImpCol.setCellValueFactory(new PropertyValueFactory<Project,Double>("qualityImportance"));
		
		TableColumn<Project,Double> taskTimeImpCol = new TableColumn<Project,Double>();
		taskTimeImpCol.setText("Time importance");
		taskTimeImpCol.setCellValueFactory(new PropertyValueFactory<Project,Double>("timeImportance"));
		
		ObservableList<Project> lfx_projects = FXCollections.observableList(l_projects);
		tbl_projects.setItems(lfx_projects);
		
		tbl_projects.getColumns().addAll(projectIdCol, taskNameCol, taskQualityImpCol, taskTimeImpCol);
	}
	
	public void assignmentDataDownloadClickHandler(MouseEvent me) {
		Iterator<Project> it_projects = App.get().prjRep.findAll().iterator();
		List<EmployeeTask> l_employeeTasks = new ArrayList<EmployeeTask>();
		
		while(it_projects.hasNext()) {
			Project project = it_projects.next();
			Map<Employee, TaskSet> assignments = project.getM_assignments();
			
			Iterator<Employee> it_assKeys = assignments.keySet().iterator();
			while (it_assKeys.hasNext()) {
				Employee emp = it_assKeys.next(); 
				TaskSet taskSet = assignments.get(emp);
				Iterator<ProjectTask> it_tasks = taskSet.getTasks().iterator();
				
				while(it_tasks.hasNext()) {
					ProjectTask task = it_tasks.next();
					
					l_employeeTasks.add(new EmployeeTask(emp.getId(), emp.getFirstName() + " " + emp.getLastName(), task.getId(), task.getTaskName()));
				}
			}
		}
		
		
		
		TableColumn<EmployeeTask,Long> empIdCol = new TableColumn<EmployeeTask,Long>();
		empIdCol.setText("Employee id");
		empIdCol.setCellValueFactory(new PropertyValueFactory<EmployeeTask,Long>("empId"));
		
		TableColumn<EmployeeTask,String> empNameCol = new TableColumn<EmployeeTask,String>();
		empNameCol.setText("Employee name");
		empNameCol.setCellValueFactory(new PropertyValueFactory<EmployeeTask,String>("empName"));
		
		TableColumn<EmployeeTask,Long> taskQualityImpCol = new TableColumn<EmployeeTask,Long>();
		taskQualityImpCol.setText("Task Id");
		taskQualityImpCol.setCellValueFactory(new PropertyValueFactory<EmployeeTask,Long>("taskId"));
		
		TableColumn<EmployeeTask,Long> taskTimeImpCol = new TableColumn<EmployeeTask,Long>();
		taskTimeImpCol.setText("Task name");
		taskTimeImpCol.setCellValueFactory(new PropertyValueFactory<EmployeeTask,Long>("taskName"));
		
		ObservableList<EmployeeTask> lfx_empTasks = FXCollections.observableList(l_employeeTasks);
		tbl_assignments.setItems(lfx_empTasks);
		
		tbl_assignments.getColumns().addAll(empIdCol, empNameCol, taskQualityImpCol, taskTimeImpCol);
	}
	
	
	//****************
	//CANDIDATE SEARCH
	//****************
	public void candidateSearchSelectedHandler(Event evt){
		LOGGER.info("CANDIDATE SEARCH TAB SELECTED!");
		
		//fill up candidate types combo box
		cb_candidateType.getItems().clear();
		cb_candidateType.getItems().add(InternalExternalSelector.INTERNAL);
		cb_candidateType.getItems().add(InternalExternalSelector.EXTERNAL);
		cb_candidateType.getItems().add(InternalExternalSelector.BOTH);
		
		//setting a default value for the candidate type selector
		cb_candidateType.setValue(InternalExternalSelector.INTERNAL);
	}
	
	public void searchCandidateClickHandler(MouseEvent me) {
		rmbc_candidateResults.setVisible(false);

		//Add the process indicator to the grid 
		ProgressIndicator progressInd = new ProgressIndicator();
		bp_candSearch.setCenter(progressInd);
		progressInd.setVisible(true);
		progressInd.toFront();
		
		//Additional service for the Process indicator
		CandidateSearchService css = new CandidateSearchService( tf_candidateSearch.getText(),  cb_candidateType.getValue());
		css.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				List<PersonDistanceResult> results = css.getValue();
				rmbc_candidateResults.setCandidateSearchResultData(results);
				
				bp_candSearch.setCenter(rmbc_candidateResults);
				rmbc_candidateResults.setVisible(true);
			}
		});
		
		css.restart();
	}

	
	
	//********
	//TOP MENU
	//********
	public void closeButtonClickHandler(ActionEvent evt) {
		System.exit(0);
	}
	
	private Popup popup = null;
	public void aboutButtonClickHandler(ActionEvent evt) {
		if(popup == null)
			popup = new Popup();
		
		if(!popup.isShowing()){
			popup.setX(300);
		    popup.setY(200);
			popup.getContent().addAll(new AboutScene(popup));
			popup.show(App.get().mainStage);
		}
	}
	
	
	
	//**********************
	//PERFORMANCE EVALUATION
	//**********************
	public void performanceEvaluationSelectedHandler(Event evt) {
		if(!tab_perfEval.isSelected())
			return;
		
		//clear the form to default format
		cb_perfEval_project.getItems().clear();
		cb_perfEval_employee.getItems().clear();
		ta_perfText.setText("");
		
		//fill the comboboxes with the available values
		List<Project> l_projects = App.get().prjRep.findByIsInternalProjectOrderByProjectNameAsc(true);
		if(l_projects != null) {
			Iterator<Project> it_projects = l_projects.iterator(); 

			while(it_projects.hasNext()) {
				Project actPrj = it_projects.next();
				cb_perfEval_project.getItems().add(actPrj);
			}
			
		}
	}
	
	public void cbPerfEvalProjectChangeHandler(Event evt) {
		Project selectedProject = (Project)cb_perfEval_project.getValue();
		Set<Employee> s_employees = selectedProject.getM_assignments().keySet();
		
		cb_perfEval_employee.getItems().addAll(s_employees);
	}
	
	public void cbPerfEvalEmployeeChangeHandler(Event evt) {
		Project selectedPrj = cb_perfEval_project.getValue();
		Employee selectedEmp = cb_perfEval_employee.getValue();
		PerfText empsPerfText = App.get().perfTextRep.findByProjectAndEmployee(selectedPrj, selectedEmp);
		
		if(empsPerfText != null) {
			ta_perfText.setText( empsPerfText.getPerfEvaluationText() );
		} else {
			ta_perfText.setText("");
		}
	}
	
	public void saveEvalClickHandler(Event evt) {
		Project selectedPrj = cb_perfEval_project.getValue();
		Employee selectedEmp = cb_perfEval_employee.getValue();
		PerfText empsPerfText = App.get().perfTextRep.findByProjectAndEmployee(selectedPrj, selectedEmp);
		
		if(empsPerfText != null) {
			empsPerfText.setPerfEvaluationText( ta_perfText.getText() );
			App.get().perfTextRep.save(empsPerfText);
		} else {
			empsPerfText = new PerfText(selectedPrj, selectedEmp, ta_perfText.getText());
			App.get().perfTextRep.save(empsPerfText);
		}
		
		Alert alertSuccessfulSave = new Alert(AlertType.INFORMATION);
		alertSuccessfulSave.setTitle("Information Dialog");
		alertSuccessfulSave.setHeaderText("Successful performance evaluation save");
		alertSuccessfulSave.setContentText("Successfully saved the entered performance evaluation!");
		alertSuccessfulSave.showAndWait();
	}
	
	
	
	//****************
	//ISSUE STATISTICS
	//****************
	public void issueStatsSelectedHandler(Event evt) {
		if(!tab_issueStats.isSelected())
			return;
		
		//clear the form to default format
		cb_issueStat_project.getItems().clear();
		vb_issueStatFrame.setVisible(false);
		vb_issueStat.setVisible(false);
		vb_issueStat.getChildren().clear();
		
		//fill the combo boxes with the available values
		List<Project> l_projects = App.get().prjRep.findByIsInternalProjectOrderByProjectNameAsc(true);
		if(l_projects != null) {
			Iterator<Project> it_projects = l_projects.iterator(); 

			while(it_projects.hasNext()) {
				Project actPrj = it_projects.next();
				cb_issueStat_project.getItems().add(actPrj);
			}
			
		}
	}
	
	public void cbIssueStatProjectChangeHandler(Event evt) {
		//fill up employee combobox
		Project selectedProject = (Project)cb_issueStat_project.getValue();
		
		//show the overall pie chart
		vb_issueStatFrame.setVisible(true);
		vb_issueStat.setVisible(true);
		List<Employee> emps = new ArrayList<Employee>(selectedProject.getM_assignments().keySet());
		
		ObservableList<Data> overallData = FXCollections.observableArrayList();
		Map<Employee, PerfStat> m_empPerfStat = new HashMap<Employee, PerfStat>();
		for(Employee emp : emps) {
			PerfStat perfStat = App.get().perfStatRep.findByProjectAndEmployee(selectedProject, emp);
			m_empPerfStat.put(emp, perfStat);
			overallData.add(new PieChart.Data(emp.getFullName(), perfStat.getSumTasks()));
		}
		ProjectIssueStatStackPane pissp = new ProjectIssueStatStackPane(overallData, m_empPerfStat);
		vb_issueStat.getChildren().add(pissp);
	}
	
	//******************
	//LINKEDIN FUNCTIONS
	//******************
	public void linkedInSelectedHandler(Event evt) {
		LOGGER.info("LINKED-IN TAB SELECTED");
	}
	
	public void linkedInProfileImportClickHandler(MouseEvent evt) {
		pi_profileImport.setVisible(true);
		pi_profileImport.setProgress(-1.0);
		
		LinkedInProfileImport lipi = new LinkedInProfileImport();
		lipi.importProfilesIntoDB(HemsProps.get().getProperty(HemsProps.LI_PROFILES), pi_profileImport);
	}
	
	public void linkedInProfileSeniorityClickHandler(MouseEvent evt) {
		libc_experiences.setVisible(false);
		
		//start showing the process indicator that look-up process is running
		ProgressIndicator progressInd = new ProgressIndicator();
		bp_linkedIn.setCenter(progressInd);
		progressInd.setVisible(true);
		progressInd.toFront();

		LinkedInSeniorityProcessor linkedInProc = new LinkedInSeniorityProcessor();
		
		Task task = new Task<Void>() {
			@Override protected Void call() throws Exception {
				List<LinkedInProfile> l_linkedInProfiles = linkedInProc.getLinkedInProfiles( HemsProps.get().getProperty(HemsProps.LI_PROFILES) );
				libc_experiences.setLinkedInProfiles(l_linkedInProfiles);
				libc_experiences.setVisible(true);
				
				return null;
		    }

			@Override
			protected void succeeded() {
				bp_linkedIn.setCenter(libc_experiences);
			}
		};
		
		Thread thrExpBarchart = new Thread(task);
		thrExpBarchart.start();
	}
	
}
