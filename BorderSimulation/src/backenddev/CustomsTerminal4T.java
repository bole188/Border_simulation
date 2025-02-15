package backenddev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import application.MainSceneController;
import application.SecondSceneController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class CustomsTerminal4T extends Terminals{
	public final Semaphore CustomsSem4T = new Semaphore(1);
	public CustomsTerminal4T(){super();}
	public ArrayList<Truck> trucksWithIncidents = new ArrayList<Truck>();
	public void sendToCustomsTerminal(Truck t){
		int NoP = t.getPassengersNumber();
		try{
			Platform.runLater(()->MainSceneController.resetPT4T());
			Border.cT.CustomsSem4T.acquire();
				System.out.println(t.name + " with " + NoP + " arrived at customs terminal");
				Platform.runLater(()->{
					MainSceneController.listOfCTButtons.get(1).setText(t.name);
					MainSceneController.listOfCTButtons.get(1).setOnAction(event -> {
			            // Create an Alert (a simple pop-up dialog)
			            Alert alert = new Alert(AlertType.INFORMATION);
			            alert.setTitle("Information Dialog");
			            alert.setHeaderText(null);
			            String vehicleInfo = t.name + "\n";
			            vehicleInfo+=t.numOfPassengers + "\n";
			            for (Person p : t.passengers) vehicleInfo += (p.GetId() + "\n");
		 	            alert.setContentText(vehicleInfo);

			            // Show the dialog
			            alert.showAndWait();
			        });
				});
				Border.pT.PoliceSem4T.release();
				if(!processTruck(t))	Platform.runLater(()->MainSceneController.listOfCTButtons.get(1).setText("CT2"));
				else {
					Platform.runLater(()->SecondSceneController.updateLegalList(t));
					System.out.println(t.name + " with " + NoP + " has been processed on customs terminal");
					Platform.runLater(()->MainSceneController.listOfCTButtons.get(1).setText("CT2"));
					
				}
			Border.cT.CustomsSem4T.release();
			MainSceneController.cdl.countDown();
		}catch(InterruptedException ie){
			ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
		}
		try {
			MainSceneController.cdl.await();
			MainSceneController.timeline.stop();
		}
		catch(InterruptedException e) {
			ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
		}
	}
	private boolean shouldCheckDoc(Truck t) {
		return PossibilityCalculator.calculate(50);
	}
	private boolean checkMass(Truck t) {
		if(t.hasMoreWeight()) {
			System.out.println("has more weight");
			VehiclesWithIncidents.addVehicle(t);
			t.typeOfBreach = "Real weight bigger than declared weight";
			Platform.runLater(()->{
				SecondSceneController.updateIncidentList(t);
			});
			synchronized(Terminals.lock) {
			if(Border.cV.peopleWithIllegalItems.isEmpty() && Border.cT.trucksWithIncidents.isEmpty()) {
				LocalDateTime currentDateTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
		        String formattedDateTime = currentDateTime.format(formatter);
		        Terminals.pathToTxtFile = formattedDateTime + ".txt";
			}
			Border.cT.trucksWithIncidents.add(t);
			try{
				BufferedWriter writeObj = new BufferedWriter(new FileWriter(Terminals.pathToTxtFile,true));
				writeObj.write("Truck id:" + t.getId()  + ". " + t.typeOfBreach + '\n');
				writeObj.close();
				FileHandler fileHandler = new FileHandler("error.log");
	            SimpleFormatter simpleFormatter = new SimpleFormatter();
	            fileHandler.setFormatter(simpleFormatter);
	            ExceptionHandler.logger.addHandler(fileHandler);
	            fileHandler.close();
				}
			catch(IOException e) {
				ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
			}
			}
			System.out.println(t.name + " with " + t.getPassengersNumber() + " cannot pass the border due to its mass");
			return false;
		}
		return true;
	}
	public boolean processTruck(Truck t){
		try{
			MainSceneController.checkIfPaused();
			sleep(500);
			MainSceneController.checkIfPaused();
			if(!checkMass(t)) return false;
			if(shouldCheckDoc(t)) System.out.println("Documentation has been generated");
			FileHandler fileHandler = new FileHandler("error.log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            ExceptionHandler.logger.addHandler(fileHandler);
            fileHandler.close();
		}catch(Exception ie){
			ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
		}
		return true;
	}
}
