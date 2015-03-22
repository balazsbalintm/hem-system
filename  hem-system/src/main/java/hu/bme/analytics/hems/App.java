package hu.bme.analytics.hems;

import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.Project;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.entities.TaskSet;
import hu.bme.analytics.hems.entities.repositories.EmployeeRepository;
import hu.bme.analytics.hems.entities.repositories.ProjectRepository;
import hu.bme.analytics.hems.entities.repositories.ProjectTaskRepository;
import hu.bme.analytics.hems.entities.repositories.TaskSetRepository;
import hu.bme.analytics.hems.ui.controller.ViewNavigator;
import hu.bme.analytics.hems.ui.view.Views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
public class App extends AbstractJavaFxApplicationSupport {

	//Singleton
	private static App instance = null; 
	public static App get() {
		return instance;
	} 
	
	
	@Value("${app.ui.title}")
	private String windowTitle;

	private static String MAIN_VIEW_LOC = "./ui/view/MainView.fxml";

	/* ****************** */
	private static boolean IS_DATA_UPLOAD_MODE = false;
	/* ****************** */

	@Autowired
	public EmployeeRepository empRep;
	@Autowired
	public ProjectRepository projectRep;
	@Autowired
	public ProjectTaskRepository prjTaskRepository;
	@Autowired
	public TaskSetRepository taskSetRepository;

	@Override
	public void start(Stage stage) throws Exception {
		//set the singleton instance
		instance = this;
		
		//decide if data upload mode or normal production
		if (!IS_DATA_UPLOAD_MODE) {
			stage.setScene(createScene(loadMainPane()));
			stage.setTitle(windowTitle);
			stage.show();

			/*
			 * stage.setTitle(windowTitle); stage.setScene(new
			 * Scene(mainLayout)); stage.setResizable(true);
			 * stage.centerOnScreen(); stage.show();
			 * 
			 * repository.save(new Employee("Jack", "Bauer"));
			 * repository.save(new Employee("Chloe", "O'Brian"));
			 * repository.save(new Employee("Kim", "Bauer"));
			 * repository.save(new Employee("David", "Palmer"));
			 * repository.save(new Employee("Michelle", "Dessler"));
			 * 
			 * // fetch all customers
			 * System.out.println("Customers found with findAll():");
			 * System.out.println("-------------------------------"); for
			 * (Employee emp : repository.findAll()) {
			 * mainLayout.getHelloWorldComponent().addLabelToComponent
			 * (emp.toString()); }
			 */
		} else {
			DataCreator();
		}
	}

	private Pane loadMainPane() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(MAIN_VIEW_LOC));
		ViewNavigator.setMainController(loader.getController());
		ViewNavigator.loadView(Views.LoginView);

		return mainPane;
	}

	private Scene createScene(Pane mainPane) {
		Scene scene = new Scene(mainPane);

		return scene;
	}

	public static void main(String[] args) {
		launchApp(App.class, args);
	}

	/* ****************DATA CREATION ACTIVITY**************** */
	private void DataCreator() {
		// Creation of employees
		Employee emp1 = new Employee("Akos", "Toth");
		Employee emp2 = new Employee("Gabor", "Kovacs");
		Employee emp3 = new Employee("Adam", "Ciceri");

		empRep.save(emp1);
		empRep.save(emp2);
		empRep.save(emp3);

		// creation of tasks
		ProjectTask task1 = new ProjectTask(
				"Modelling of system",
				"Engineering Systems Analyst",
				"Engineering Systems Analyst Dorking Surrey Salary ****K Our client is located in Dorking, Surrey and are looking for Engineering Systems Analyst our client provides specialist software development Keywords Mathematical Modelling, Risk Analysis, System Modelling, Optimisation, MISER, PIONEEER Engineering Systems Analyst Dorking Surrey Salary ****K");
		ProjectTask task2 = new ProjectTask(
				"Material performance design",
				"Stress Engineer Glasgow",
				"Stress Engineer Glasgow Salary **** to **** We re currently looking for talented engineers to join our growing Glasgow team at a variety of levels. The roles are ideally suited to high calibre engineering graduates with any level of appropriate experience, so that we can give you the opportunity to use your technical skills to provide high quality input to our aerospace projects, spanning both aerostructures and aeroengines. In return, you can expect good career opportunities and the chance for advancement and personal and professional development, support while you gain Chartership and some opportunities to possibly travel or work in other offices, in or outside of the UK. The Requirements You will need to have a good engineering degree that includes structural analysis (such as aeronautical, mechanical, automotive, civil) with some experience in a professional engineering environment relevant to (but not limited to) the aerospace sector. You will need to demonstrate experience in at least one or more of the following areas: Structural/stress analysis Composite stress analysis (any industry) Linear and nonlinear finite element analysis Fatigue and damage tolerance Structural dynamics Thermal analysis Aerostructures experience You will also be expected to demonstrate the following qualities: A strong desire to progress quickly to a position of leadership Professional approach Strong communication skills, written and verbal Commercial awareness Team working, being comfortable working in international teams and self managing PLEASE NOTE SECURITY CLEARANCE IS REQUIRED FOR THIS ROLE Stress Engineer Glasgow Salary **** to ****");
		ProjectTask task3 = new ProjectTask(
				"Implementation embedded system",
				"CNC Programmer",
				"Working within a small but busy sub contract ISO accredited sub contract machine shop. Must be able to programme straight from drawings programming and operating a Bridgeport VMC or a Haas VMC with Heidenhain controls. Machining 1 off,s to small batch work. Hours of work are  Mon  Thurs 8am  5.00pm and 3.00pm finish on a Friday");

		prjTaskRepository.save(task1);
		prjTaskRepository.save(task2);
		prjTaskRepository.save(task3);

		// creation of project
		Project prj1 = new Project("Building modernization");
		TaskSet tskSet1 = prj1.assignTaskToEmployee(emp1, task1);
		TaskSet tskSet2 = prj1.assignTaskToEmployee(emp2, task2);
		TaskSet tskSet3 = prj1.assignTaskToEmployee(emp3, task3);

		taskSetRepository.save(tskSet1);
		taskSetRepository.save(tskSet2);
		taskSetRepository.save(tskSet3);

		projectRep.save(prj1);
		System.out.println("Upload finished");
	}
}
