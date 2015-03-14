package hu.bme.analytics.hems.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
		System.out.println("Clicked");
	}
	
}
