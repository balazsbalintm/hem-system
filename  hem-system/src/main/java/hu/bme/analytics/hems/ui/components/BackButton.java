package hu.bme.analytics.hems.ui.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BackButton extends ImageView{
	private ProjectIssueStatStackPane pissp;
	
	public BackButton() {
		super();
		
		try {
			setImage(new Image(new FileInputStream(new File(".\\src\\main\\java\\hu\\bme\\analytics\\hems\\ui\\style\\back-button.png"))));
			this.setScaleX(0.2);
			this.setScaleY(0.2);
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		this.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				pissp.setBackToTopLevel();
			}
		});
	}

	public void setPissp(ProjectIssueStatStackPane pissp) {
		this.pissp = pissp;
	}
	
	
}
