package hu.bme.analytics.hems.ui.controller;

import hu.bme.analytics.hems.App;
import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.PersonDistanceResult;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.ui.rapidminer.ModelCaller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import org.springframework.stereotype.Component;

@Component
public class HemsController {

	@FXML private TableView<Employee> tbl_persons;
	@FXML private TableView<ProjectTask> tbl_tasks;
	@FXML private TextField tf_candidateSearch;
	@FXML private GridPane g_cadidateResult;
	
	public HemsController() {
		
	}
	
	
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
	
}
