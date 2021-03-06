package hu.bme.analytics.hems;

import hu.bme.analytics.hems.entities.Employee;
import hu.bme.analytics.hems.entities.PerfStat;
import hu.bme.analytics.hems.entities.Project;
import hu.bme.analytics.hems.entities.ProjectTask;
import hu.bme.analytics.hems.entities.TaskSet;
import hu.bme.analytics.hems.entities.TaskStates;
import hu.bme.analytics.hems.entities.repositories.EmployeeRepository;
import hu.bme.analytics.hems.entities.repositories.PerfStatRepository;
import hu.bme.analytics.hems.entities.repositories.PerfTextRepository;
import hu.bme.analytics.hems.entities.repositories.ProjectRepository;
import hu.bme.analytics.hems.entities.repositories.ProjectTaskRepository;
import hu.bme.analytics.hems.entities.repositories.TaskSetRepository;
import hu.bme.analytics.hems.entities.repositories.TaskStatesRepository;
import hu.bme.analytics.hems.ui.controller.ViewNavigator;
import hu.bme.analytics.hems.ui.view.Views;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

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
	@Value("${app.properties.path}")
	private String hemsPropsPath;
	
	public Stage mainStage;
	
	/* ****************** */
	private final static Logger LOGGER = Logger.getLogger(App.class.getName());
	private static String MAIN_VIEW_LOC = "./ui/view/MainView.fxml";
	private static boolean IS_DATA_UPLOAD_MODE = false;
	/* ****************** */

	@Autowired
	public EmployeeRepository empRep;
	@Autowired
	public ProjectRepository prjRep;
	@Autowired
	public ProjectTaskRepository prjTaskRep;
	@Autowired
	public TaskSetRepository taskSetRep;
	@Autowired
	public TaskStatesRepository taskStatesRep;
	@Autowired
	public PerfTextRepository perfTextRep;
	@Autowired
	public PerfStatRepository perfStatRep;

	@Override
	public void start(Stage stage) throws Exception {
		//set the singleton instance
		instance = this;
		mainStage = stage;
		HemsProps.init(hemsPropsPath);
		
		//decide if data upload mode or normal production
		if (!IS_DATA_UPLOAD_MODE) {
			stage.setScene(createScene(loadMainPane()));
			stage.setTitle(windowTitle);
			stage.show();

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
		Employee emp1 = new Employee("Akos", "Toth", true);
		Employee emp2 = new Employee("Gabor", "Kovacs", true);
		Employee emp3 = new Employee("Adam", "Ciceri", true);

		empRep.save(emp1);
		empRep.save(emp2);
		empRep.save(emp3);
		LOGGER.info("TEST DATA: Employees saved!");

		// creation of tasks
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			TaskStates task1State = new TaskStates("CLOSED");
			ProjectTask task1 = new ProjectTask(
					"Modelling of system",
					"Engineering Systems Analyst",
					"Engineering Systems Analyst Dorking Surrey Salary ****K Our client is located in Dorking, Surrey and are looking for Engineering Systems Analyst our client provides specialist software development Keywords Mathematical Modelling, Risk Analysis, System Modelling, Optimisation, MISER, PIONEEER Engineering Systems Analyst Dorking Surrey Salary ****K",
					task1State,
					sdf.parse("20-01-2015"),
					sdf.parse("25-01-2015"),
					0.9);
			TaskStates task2State = new TaskStates("OPEN");
			ProjectTask task2 = new ProjectTask(
					"Material performance design",
					"Stress Engineer Glasgow",
					"Stress Engineer Glasgow Salary **** to **** We re currently looking for talented engineers to join our growing Glasgow team at a variety of levels. The roles are ideally suited to high calibre engineering graduates with any level of appropriate experience, so that we can give you the opportunity to use your technical skills to provide high quality input to our aerospace projects, spanning both aerostructures and aeroengines. In return, you can expect good career opportunities and the chance for advancement and personal and professional development, support while you gain Chartership and some opportunities to possibly travel or work in other offices, in or outside of the UK. The Requirements You will need to have a good engineering degree that includes structural analysis (such as aeronautical, mechanical, automotive, civil) with some experience in a professional engineering environment relevant to (but not limited to) the aerospace sector. You will need to demonstrate experience in at least one or more of the following areas: Structural/stress analysis Composite stress analysis (any industry) Linear and nonlinear finite element analysis Fatigue and damage tolerance Structural dynamics Thermal analysis Aerostructures experience You will also be expected to demonstrate the following qualities: A strong desire to progress quickly to a position of leadership Professional approach Strong communication skills, written and verbal Commercial awareness Team working, being comfortable working in international teams and self managing PLEASE NOTE SECURITY CLEARANCE IS REQUIRED FOR THIS ROLE Stress Engineer Glasgow Salary **** to ****",
					task2State,
					sdf.parse("20-06-2015"),
					null,
					0.0);
			TaskStates task3State = new TaskStates("IN_PROGRESS");
			ProjectTask task3 = new ProjectTask(
					"Implementation embedded system",
					"CNC Programmer",
					"Working within a small but busy sub contract ISO accredited sub contract machine shop. Must be able to programme straight from drawings programming and operating a Bridgeport VMC or a Haas VMC with Heidenhain controls. Machining 1 off,s to small batch work. Hours of work are  Mon  Thurs 8am  5.00pm and 3.00pm finish on a Friday",
					task3State,
					sdf.parse("20-03-2015"),
					null,
					0.0);

			taskStatesRep.save(task1State);
			taskStatesRep.save(task2State);
			taskStatesRep.save(task3State);
			
			prjTaskRep.save(task1);
			prjTaskRep.save(task2);
			prjTaskRep.save(task3);
			LOGGER.info("TEST DATA: Tasks and task statuses saved!");
			
			// creation of project
			Project prj1 = new Project("Building modernization", sdf.parse("01-01-2015"), sdf.parse("01-09-2015"), 0.9, 0.1, true);
			TaskSet tskSet1 = prj1.assignTaskToEmployee(emp1, task1);
			TaskSet tskSet2 = prj1.assignTaskToEmployee(emp2, task2);
			TaskSet tskSet3 = prj1.assignTaskToEmployee(emp3, task3);
	
			taskSetRep.save(tskSet1);
			taskSetRep.save(tskSet2);
			taskSetRep.save(tskSet3);
			LOGGER.info("TEST DATA: Task sets are saved!");
	
			prjRep.save(prj1);
			LOGGER.info("TEST DATA: Projects are saved!");
			
			//creation of Performance statistics
			PerfStat emp1PerfStat = new PerfStat(prj1, emp1, 10, 5, 0, 100);
			PerfStat emp2PerfStat = new PerfStat(prj1, emp2, 5, 15, 6, 170);
			PerfStat emp3PerfStat = new PerfStat(prj1, emp3, 0, 6, 3, 20);
			
			perfStatRep.save(emp1PerfStat);
			perfStatRep.save(emp2PerfStat);
			perfStatRep.save(emp3PerfStat);
			LOGGER.info("TEST DATA: Project and employee assignments are saved!");

			
			LOGGER.info("TEST DATA: Upload of TEST employee, task and project data is FINISHED!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
