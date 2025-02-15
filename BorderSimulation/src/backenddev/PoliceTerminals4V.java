package backenddev;

import java.util.concurrent.Semaphore;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import application.MainSceneController;
import application.SecondSceneController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class PoliceTerminals4V extends Terminals{
	public final Semaphore PoliceSem4V = new Semaphore(2,true);
	public PoliceTerminals4V(){super();}
	public boolean processVehicle(Vehicle v){
		int NoP = v.getPassengersNumber();
		if(v instanceof Bus) {
			try
			{
					FileHandler fileHandler = new FileHandler("error.log");
	            	SimpleFormatter simpleFormatter = new SimpleFormatter();
	            	fileHandler.setFormatter(simpleFormatter);
	            	ExceptionHandler.logger.addHandler(fileHandler);
	            	fileHandler.close();
			MainSceneController.checkIfPaused();
			sleep(NoP * 100);
			MainSceneController.checkIfPaused();
		}
			catch(Exception ie){
				ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
			}
		}
		else{
			try{
				FileHandler fileHandler = new FileHandler("error.log");
	            SimpleFormatter simpleFormatter = new SimpleFormatter();
	            fileHandler.setFormatter(simpleFormatter);
	            ExceptionHandler.logger.addHandler(fileHandler);
	            fileHandler.close();
				MainSceneController.checkIfPaused();
				sleep(NoP * 500);
				MainSceneController.checkIfPaused();
		}
		catch(Exception ie){
			ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
		}
		}
		if(!checkDocuments(v)) return false;
		return true;
	}
	public void sendToPoliceTerminal(Vehicle v){
		int NoP = v.getPassengersNumber();
		Integer takenTerminal;
		synchronized(MainSceneController.listOfButtons) {	
			MainSceneController.checkIfPaused();
			Platform.runLater(()->MainSceneController.updateButtonNames());
		}
				System.out.println(v.name + " with " + v.getPassengersNumber() + " arrived at police terminal");
				if(MainSceneController.firstPoliceTerminal == 1) {
					takenTerminal = 1;
					Platform.runLater(() -> {
						MainSceneController.listOfPTButtons.get(0).setText(v.name);
						MainSceneController.listOfPTButtons.get(0).setOnAction(event -> {
				            Alert alert = new Alert(AlertType.INFORMATION);
				            alert.setTitle("Information Dialog");
				            alert.setHeaderText(null);
				            String vehicleInfo = v.name + "\n";
				            vehicleInfo+=v.numOfPassengers + "\n";
				            for (Person p : v.passengers) vehicleInfo += (p.GetId() + "\n");
			 	            alert.setContentText(vehicleInfo);
				            alert.showAndWait();
				        });
					});
				}
				else {																									
					takenTerminal = 2;
					Platform.runLater(()->{
						MainSceneController.listOfPTButtons.get(1).setText(v.name);										
						Platform.runLater(() -> {
							MainSceneController.listOfPTButtons.get(1).setText(v.name);
							MainSceneController.listOfPTButtons.get(1).setOnAction(event -> {
					            Alert alert = new Alert(AlertType.INFORMATION);
					            alert.setTitle("Information Dialog");
					            alert.setHeaderText(null);
					            String vehicleInfo = v.name + "\n";
					            vehicleInfo+=v.numOfPassengers + "\n";
					            for (Person p : v.passengers) vehicleInfo += (p.GetId() + "\n");
				 	            alert.setContentText(vehicleInfo);
					            alert.showAndWait();
					        });
						});
					});
				}
				if(takenTerminal == 1) MainSceneController.firstPoliceTerminal = 0;
				else MainSceneController.secondPoliceTerminal = 0;
				if(!processVehicle(v)) {
					System.out.println(v.name + " with " + NoP+ "->" + v.getPassengersNumber() + " has been removed from further processing");
					v.typeOfBreach = "Driver has invalid documents";
					Platform.runLater(()->{
						SecondSceneController.updateIncidentList(v);
					});
					if(takenTerminal == 1) {
						Platform.runLater(()->{
						MainSceneController.listOfPTButtons.get(0).setText("PT1");
					});
						MainSceneController.firstPoliceTerminal = 1;
					}
					else {
						Platform.runLater(()->{
							MainSceneController.listOfPTButtons.get(1).setText("PT2");
						});
						MainSceneController.secondPoliceTerminal = 1;
					}
					Border.pV.PoliceSem4V.release();
					MainSceneController.cdl.countDown();
					return;
		}
				System.out.println(v.name + " with " + NoP+ "->" + v.getPassengersNumber() + " has been processed on police terminal");
				if(takenTerminal == 1) {
					Platform.runLater(()->{
					MainSceneController.listOfPTButtons.get(0).setText("PT1");
				});
					MainSceneController.firstPoliceTerminal = 1;
				}
				else {
					Platform.runLater(()->{
						MainSceneController.listOfPTButtons.get(1).setText("PT2");
					});
					MainSceneController.secondPoliceTerminal = 1;
				}
				Border.cV.sendToCustomsTerminal(v);
	}
}

