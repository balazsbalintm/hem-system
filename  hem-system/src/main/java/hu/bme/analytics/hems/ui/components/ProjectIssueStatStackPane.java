package hu.bme.analytics.hems.ui.components;

import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.PerfStat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import org.springframework.beans.factory.annotation.Autowired;

public class ProjectIssueStatStackPane extends StackPane {
	//Task states constants
	private String TSK_CLOSE = "Closed";
	private String TSK_OPEN = "Open";
	private String TSK_INPROGRESS = "In progress";
	
	//determine on which level the pie chart is. Upper level: people; Lower level: task
	private enum ChartLevel {PEOPLE, TASK};
	
	private PieChart pc_project;
	private ImageView img_levelIcon;
	private BackButton iv_backButton;
	
	private Map<Employee, PerfStat> m_empPerfStat;
	private ObservableList<Data> pieChartData;
	
	/**
	 * @param pieChartData "Person" level data for the pie chart
	 * @param m_empPerfStat A mapping between the employee and its performance statistics. For example: John Smith -> 0 Open 2 Closed 4 In progress
	 */
	public ProjectIssueStatStackPane(ObservableList<Data> pieChartData, Map<Employee, PerfStat> m_empPerfStat) {
		this.m_empPerfStat = m_empPerfStat;		
		this.pieChartData = pieChartData;
		
		//BASIC PIE CHART
		pc_project = new PieChart();
		pc_project.setData(pieChartData);
		this.getChildren().add(pc_project);
		this.setAlignment(pc_project, Pos.CENTER);
		
		//BACK BUTTON SETUP
		iv_backButton = new BackButton();
		iv_backButton.setPissp(this);
		this.getChildren().add(iv_backButton);
		this.setAlignment(iv_backButton, Pos.TOP_LEFT);
		
		//IMG MAN
		setImageToLevel(ChartLevel.PEOPLE);
		setDrillDownData(pieChartData);
	}
	
	@Autowired
	private void setDrillDownData(ObservableList<Data> pieChartData) {
		//binding a click handler to ALL part of the "Person" level pie
		//in case of one of them is clicked, task level pie chart can be displayed
		for(Data highLevelData : pieChartData) {
			highLevelData.getNode().setOnMouseClicked((MouseEvent t) -> {

				//iterate through the employees in the database
				Iterator<Employee> it_emps = m_empPerfStat.keySet().iterator();
				while (it_emps.hasNext()){
					Employee emp = it_emps.next();
					
					//found the employee whose piechart part's was clicked
					if(emp.getFullName().equals(highLevelData.getName())) {
						PerfStat perfStat = m_empPerfStat.get(emp);
						pc_project.setData(FXCollections.observableArrayList(
								new Data(TSK_CLOSE, perfStat.getNrOfClosedTasks()),
								new Data(TSK_INPROGRESS, perfStat.getNrOfInprogressTasks()),
								new Data(TSK_OPEN, perfStat.getNrOfOpenTasks())
						));
					}
				}
				
				//if all data is set, change to "Task" level of display the pie chart
				setImageToLevel(ChartLevel.TASK);				
	        });
		}
	}
	
	/**
	 * Can set the level of chart which should be displayed.
	 * @param chrLvl Level of chart which should be displayed. Person / Task level.
	 */
	private void setImageToLevel(ChartLevel chrLvl){
		try {
			if(this.getChildren().contains(img_levelIcon))
				this.getChildren().remove(img_levelIcon);
			
			if(chrLvl == ChartLevel.PEOPLE) {
				img_levelIcon = new ImageView(new Image(new FileInputStream(new File(".\\src\\main\\java\\hu\\bme\\analytics\\hems\\ui\\style\\man.png"))));
				img_levelIcon.setScaleX(0.6);
				img_levelIcon.setScaleY(0.6);
				pc_project.setData(pieChartData);
				setDrillDownData(pieChartData);
			} else if (chrLvl == ChartLevel.TASK) {
				img_levelIcon = new ImageView(new Image(new FileInputStream(new File(".\\src\\main\\java\\hu\\bme\\analytics\\hems\\ui\\style\\tasks.png"))));
				img_levelIcon.setScaleX(0.6);
				img_levelIcon.setScaleY(0.6);
			}
			
			this.getChildren().add(img_levelIcon);
			this.setAlignment(img_levelIcon, Pos.CENTER);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * In case of "Back" button is clicked, set back to Person level the pie chart 
	 */
	public void setBackToTopLevel(){
		setImageToLevel(ChartLevel.PEOPLE);
	}
}
