package hu.bme.analytics.hems.ui.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class AboutScene extends GridPane{
	private static String STYLE_GRID = "-fx-border-radius: 10 10;-fx-background-radius: 10 10;-fx-padding: 20 20;-fx-background-color:linear-gradient(#38424b 0.0%, #1f2429 20.0%, #191d22 100.0%),linear-gradient(#20262b, #191d22),radial-gradient(center 50.0% 0.0%, radius 100.0%,rgba(114.0,131.0,148.0,0.9), rgba(255.0,255.0,255.0,0.0));";
	private static String STYLE_QUESTION = "-fx-fill: white;-fx-text-fill: white;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );";
	private Button btn_close = new Button("Close");
	
	public AboutScene(Popup parent) {
		//set style
		this.setStyle(STYLE_GRID);
		
		//build structure
		Text tx_whoQ = new Text("Who has done this?");
		tx_whoQ.setStyle(STYLE_QUESTION);
		this.add(tx_whoQ, 0, 0);
		this.add(new Text("Balint Balazs, in hope for a thesis grade 5."), 1, 0);
		
		Text tx_whatQ = new Text("What's this?");
		tx_whatQ.setStyle(STYLE_QUESTION);
		this.add(tx_whatQ, 0, 1);
		this.add(new Text("This is a showcase application for JavaFX, Spring and RapidMiner integration together. It's awesome!"), 1, 1);
		
		Text tx_whenQ = new Text("When was it made?");
		tx_whenQ.setStyle(STYLE_QUESTION);
		this.add(tx_whenQ, 0, 2);
		this.add(new Text("2015, graduation year - hopefully."), 1, 2);
		this.add(btn_close, 1, 3);
		
		btn_close.setOnAction(new CloseButtonHandler(parent));
	}
	
	private class CloseButtonHandler implements EventHandler<ActionEvent> {
		private Popup parent;
		
		public CloseButtonHandler(Popup parent) {
			this.parent = parent;
		}
		
		@Override
		public void handle(ActionEvent event) {
			parent.hide();
		}
		
	}
	
}
