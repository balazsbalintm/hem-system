package hu.bme.analytics.hems;

import hu.bme.analytics.hems.entities.repositories.EmployeeRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
public class App extends AbstractJavaFxApplicationSupport {

	/**
	 * Note that this is configured in application.properties
	 */
	@Value("${app.ui.title}")
	//
	private String windowTitle;

	@Autowired
	EmployeeRepository repository;

	// @Autowired
	// private MainLayout mainLayout;

	@Override
	public void start(Stage stage) throws Exception {

		BorderPane page = (BorderPane) FXMLLoader.load(App.class.getResource("./ui/view/LoginLayout.fxml"));
		Scene scene = new Scene(page);
		stage.setScene(scene);
		stage.setTitle(windowTitle);
		stage.show();

		/*
		 * stage.setTitle(windowTitle); stage.setScene(new Scene(mainLayout));
		 * stage.setResizable(true); stage.centerOnScreen(); stage.show();
		 * 
		 * repository.save(new Employee("Jack", "Bauer")); repository.save(new
		 * Employee("Chloe", "O'Brian")); repository.save(new Employee("Kim",
		 * "Bauer")); repository.save(new Employee("David", "Palmer"));
		 * repository.save(new Employee("Michelle", "Dessler"));
		 * 
		 * // fetch all customers
		 * System.out.println("Customers found with findAll():");
		 * System.out.println("-------------------------------"); for (Employee
		 * emp : repository.findAll()) {
		 * mainLayout.getHelloWorldComponent().addLabelToComponent
		 * (emp.toString()); }
		 */

	}

	public static void main(String[] args) {
		launchApp(App.class, args);
	}

}
