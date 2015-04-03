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
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import org.springframework.beans.factory.annotation.Autowired;

public class ProjectIssueStatStackPane extends StackPane {
	//Task states consts
	private String TSK_CLOSE = "Closed";
	private String TSK_OPEN = "Open";
	private String TSK_INPROGRESS = "In progress";
	
	private enum ChartLevel {PEOPLE, TASK};
	
	private PieChart pc_project;
	private ImageView img_levelIcon;
	
	private Map<Employee, PerfStat> m_empPerfStat;
	public ProjectIssueStatStackPane(ObservableList<Data> pieChartData, Map<Employee, PerfStat> m_empPerfStat) {
		this.m_empPerfStat = m_empPerfStat;			
		
		//BASIC PIE CHART
		pc_project = new PieChart();
		pc_project.setData(pieChartData);
		this.getChildren().add(pc_project);
		
		//IMG MAN
		setImageToLevel(ChartLevel.PEOPLE);
		setDrillDownData(pieChartData);
	}
	
	@Autowired
	private void setDrillDownData(ObservableList<Data> pieChartData) {
		for(Data highLevelData : pieChartData) {
			highLevelData.getNode().setOnMouseClicked((MouseEvent t) -> {
				
				Iterator<Employee> it_emps = m_empPerfStat.keySet().iterator();
				while (it_emps.hasNext()){
					Employee emp = it_emps.next();
					if(emp.getFullName().equals(highLevelData.getName())) {
						PerfStat perfStat = m_empPerfStat.get(emp);
						pc_project.setData(FXCollections.observableArrayList(
								new Data(TSK_CLOSE, perfStat.getNrOfClosedTasks()),
								new Data(TSK_INPROGRESS, perfStat.getNrOfInprogressTasks()),
								new Data(TSK_OPEN, perfStat.getNrOfOpenTasks())
						));
					}
				}
				
				setImageToLevel(ChartLevel.TASK);				
	        });
		}
	}
	
	private void setImageToLevel(ChartLevel chrLvl){
		try {
			if(this.getChildren().contains(img_levelIcon))
				this.getChildren().remove(img_levelIcon);
			
			if(chrLvl == ChartLevel.PEOPLE) {
				img_levelIcon = new ImageView(new Image(new FileInputStream(new File(".\\src\\main\\java\\hu\\bme\\analytics\\hems\\ui\\style\\man.png"))));
				img_levelIcon.setScaleX(0.6);
				img_levelIcon.setScaleY(0.6);
			} else if (chrLvl == ChartLevel.TASK) {
				img_levelIcon = new ImageView(new Image(new FileInputStream(new File(".\\src\\main\\java\\hu\\bme\\analytics\\hems\\ui\\style\\tasks.png"))));
				img_levelIcon.setScaleX(0.6);
				img_levelIcon.setScaleY(0.6);
			}
			
			this.getChildren().add(img_levelIcon);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
