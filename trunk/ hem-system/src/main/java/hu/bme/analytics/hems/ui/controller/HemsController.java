package hu.bme.analytics.hems.ui.controller;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.EmployeeTask;
import hu.bme.analytics.hems.entities.PersonDistanceResult;
import hu.bme.analytics.hems.entities.Project;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.entities.TaskSet;
import hu.bme.analytics.hems.ui.components.AboutScene;
import hu.bme.analytics.hems.ui.rapidminer.ModelCaller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import org.springframework.stereotype.Component;

@Component
public class HemsController {

	@FXML private TableView<Employee> tbl_persons;
	@FXML private TableView<Project> tbl_projects;
	@FXML private TableView<ProjectTask> tbl_tasks;
	@FXML private TableView<EmployeeTask> tbl_assignments;
	@FXML private TextField tf_candidateSearch;
	@FXML private GridPane g_cadidateResult;
	
	public HemsController() {}
	
	
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
		Iterator<ProjectTask> it_tasks = App.get().prjTaskRepository.findAll().iterator();
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
		Iterator<Project> it_projects = App.get().projectRep.findAll().iterator();
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
		Iterator<Project> it_projects = App.get().projectRep.findAll().iterator();
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
	
	public void searchCandidateClickHandler(MouseEvent me) {
		List<PersonDistanceResult> results = ModelCaller.executeCandidateSearchModel( tf_candidateSearch.getText() );
		
		g_cadidateResult.getChildren().clear();
		
		Text hdr_sim = new Text("Similarity");
		hdr_sim.getStyleClass().add("header");
		
		Text hdr_name = new Text("Name");
		hdr_name.getStyleClass().add("header");
		
		g_cadidateResult.addRow(0, hdr_sim, hdr_name);
		
		int i = 1;
		for (PersonDistanceResult actualRes : results) {
			Employee emp = App.get().empRep.findOne( (long)actualRes.getPersonId() );
			g_cadidateResult.addRow(i, new Text(Double.toString( Math.round( actualRes.getDistance()*100.0)/100.0 )), new Text(emp.getFirstName() + " " + emp.getLastName()) );
			i++;
		}
	}
	
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
}
