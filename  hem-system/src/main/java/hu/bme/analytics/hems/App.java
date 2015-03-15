package hu.bme.analytics.hems;

import hu.bme.analytics.hems.entities.repositories.EmployeeRepository;
import hu.bme.analytics.hems.ui.controller.MainController;
import hu.bme.analytics.hems.ui.controller.ViewNavigator;
import hu.bme.analytics.hems.ui.view.Views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
public class App extends AbstractJavaFxApplicationSupport {

	@Value("${app.ui.title}")
	private String windowTitle;
	
	private String MAIN_VIEW_LOC = "./ui/view/MainView.fxml";

	@Autowired
	EmployeeRepository repository;

	// @Autowired
	// private MainLayout mainLayout;

	@Override
	public void start(Stage stage) throws Exception {
		// BorderPane page = (BorderPane)
		// FXMLLoader.load(App.class.getResource("./ui/view/LoginLayout.fxml"));

		stage.setScene(createScene(loadMainPane()));
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

	private Pane loadMainPane() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(MAIN_VIEW_LOC));
		ViewNavigator.setMainController(loader.getController());
		ViewNavigator.loadView(Views.LoginView);

		return mainPane;
	}
	
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
            mainPane
        );
 
        return scene;
    }

	public static void main(String[] args) {
		launchApp(App.class, args);
	}

}
