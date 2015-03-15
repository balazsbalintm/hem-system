package hu.bme.analytics.hems.ui.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class ViewNavigator {
    /** The main application layout controller. */
    private static MainController mainController;
 
    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        ViewNavigator.mainController = mainController;
    }
    
    public static void loadView(String fxml) {
        try {
            mainController.setView(FXMLLoader.load(ViewNavigator.class.getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}
