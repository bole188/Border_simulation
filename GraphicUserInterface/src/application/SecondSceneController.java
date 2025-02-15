package application;

import backenddev.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.layout.Pane;

public class SecondSceneController {
	@FXML
	public Pane legalPane;
	@FXML
	public Pane buttonPane;
	@FXML
	public Button goToFirstScene;
	@FXML
	public Pane illegalPane;
	public int pane3Y=0;
	public static ObservableList<Vehicle>  listOfVehiclesWithIncidents = FXCollections.observableArrayList();
	public static ObservableList<Vehicle>  listOfVehiclesWOIncidents = FXCollections.observableArrayList();
	public int pane3X=0;
	public int pane3YL=0;
	public int pane3XL = 0;
	public SecondSceneController() {
		
	}
	public static void updateIncidentList(Vehicle v) {
		
		SecondSceneController.listOfVehiclesWithIncidents.add(v);
	}
	public static void updateLegalList(Vehicle v) {
		SecondSceneController.listOfVehiclesWOIncidents.add(v);
	}
}
