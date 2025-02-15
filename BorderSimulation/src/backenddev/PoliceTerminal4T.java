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


public class PoliceTerminal4T extends Terminals{
	public final Semaphore PoliceSem4T  = new Semaphore(1);
	public PoliceTerminal4T(){super();}
	public boolean processTruck(Truck t){
		int NoP = t.getPassengersNumber();
		try{
			FileHandler fileHandler = new FileHandler("error.log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            ExceptionHandler.logger.addHandler(fileHandler);
            fileHandler.close();
			MainSceneController.checkIfPaused();
			sleep(NoP * 500);
			MainSceneController.checkIfPaused();
		}catch(Exception ie){
			ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
		}
		if(!checkDocuments(t)) return false;
		return true;
	}
	public void sendToPoliceTerminal(Truck t){
		int NoP = t.getPassengersNumber();
		MainSceneController.checkIfPaused();
		synchronized(MainSceneController.listOfButtons) {	
			MainSceneController.checkIfPaused();
			Platform.runLater(()->MainSceneController.updateButtonNames());
		}
				System.out.println(t.name + " with " + t.getPassengersNumber() + " arrived at police terminal");
				Platform.runLater(()->{
					MainSceneController.setPT4T();
					MainSceneController.listOfPTButtons.get(2).setOnAction(event -> {
			            Alert alert = new Alert(AlertType.INFORMATION);
			            alert.setTitle("Information Dialog");
			            alert.setHeaderText(null);
			            String vehicleInfo = t.name + "\n";
			            vehicleInfo+=t.numOfPassengers + "\n";
			            for (Person p : t.passengers) vehicleInfo += (p.GetId() + "\n");
		 	            alert.setContentText(vehicleInfo);
			            alert.showAndWait();
			        });
					});
				if(!processTruck(t)) {
					System.out.println(t.name + " with " + NoP+ "->" + t.getPassengersNumber() + " has been removed from further processing");
					t.typeOfBreach = "Driver has invalid documents";
					Platform.runLater(()->{
						SecondSceneController.updateIncidentList(t);
					});
					Platform.runLater(()->MainSceneController.resetPT4T());
					Border.pT.PoliceSem4T.release();
					MainSceneController.cdl.countDown();
					return;
				}
				System.out.println(t.name + " with " + NoP+ "->" + t.getPassengersNumber() + " has been processed on police terminal");
				//--
				Border.cT.sendToCustomsTerminal(t);
	}
}
