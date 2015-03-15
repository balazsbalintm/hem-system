package hu.bme.analytics.hems.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;


public class MainController {
    @FXML
    private StackPane mainHolder;
 
    public void setView(Node node) {
    	mainHolder.getChildren().setAll(node);
    }
}
