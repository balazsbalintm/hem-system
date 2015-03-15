package hu.bme.analytics.hems.ui.controller;

import hu.bme.analytics.hems.encrypt.PasswordEncryptionService;
import hu.bme.analytics.hems.ui.view.Views;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginFormController {
	@FXML
	private TextField tf_username;
	@FXML
	private TextField tf_password;
	
	public LoginFormController() {
	}
	
	@FXML
	protected void loginClickHandler(MouseEvent me) {
		if( PasswordEncryptionService.authenticateFromLocalFile("admin", tf_password.getText()) ) {
			ViewNavigator.loadView(Views.HemsView);			
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Wrong username and/or password!");
			alert.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> resetForm());
		}
		
	}
	
	private void resetForm() {
		tf_username.setText("");
		tf_password.setText("");
	}
	
}
