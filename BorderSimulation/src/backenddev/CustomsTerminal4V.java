package backenddev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import application.MainSceneController;
import application.SecondSceneController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class CustomsTerminal4V extends Terminals{
	public final Semaphore CustomsSem4V = new Semaphore(1);
	public CustomsTerminal4V(){super();}
	public ArrayList<Person> peopleWithIllegalItems = new ArrayList<Person>();
	private void checkBusLuggage(Bus b) {
		ArrayList<Person> newListOfPersons = new ArrayList<Person>(b.passengers);
		ArrayList<Integer> newItemsList = new ArrayList<Integer>(b.items);
		int counter = 0;
		while(counter<newListOfPersons.size()) 
		{
			if(newItemsList.get(counter) == 2) 
			{
				try{
					FileHandler fileHandler = new FileHandler("error.log");
		            SimpleFormatter simpleFormatter = new SimpleFormatter();
		            fileHandler.setFormatter(simpleFormatter);
		            ExceptionHandler.logger.addHandler(fileHandler);
		            fileHandler.close();
					synchronized(Terminals.lock) {
					if(Border.cV.peopleWithIllegalItems.isEmpty() && Border.cT.trucksWithIncidents.isEmpty()) {
						LocalDateTime currentDateTime = LocalDateTime.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
				        String formattedDateTime = currentDateTime.format(formatter);
				        Terminals.pathToTxtFile = formattedDateTime + ".txt";
					}
					Border.cV.peopleWithIllegalItems.add(newListOfPersons.get(counter));
					BufferedWriter writeObj = new BufferedWriter(new FileWriter(Terminals.pathToTxtFile,true));	
					writeObj.write("Bus id:" + b.getId() + ". Person id: " + newListOfPersons.get(counter).GetId() + "\n");
					writeObj.close();
					}
				}
				catch(IOException e) {
					ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
				}
				newListOfPersons.remove(counter);
				newItemsList.remove(counter);
			}
			else counter++;
		}
		b.setPassengersNumber(newListOfPersons.size());
		b.setPassengers(newListOfPersons);
		b.updateItems(newItemsList);
	}
	public void sendToCustomsTerminal(Vehicle v){
		int NoP = v.getPassengersNumber();
		try{
				Border.cV.CustomsSem4V.acquire();
				FileHandler fileHandler = new FileHandler("error.log");
            	SimpleFormatter simpleFormatter = new SimpleFormatter();
            	fileHandler.setFormatter(simpleFormatter);
            	ExceptionHandler.logger.addHandler(fileHandler);
            	fileHandler.close();
				System.out.println(v.name + " with " + v.getPassengersNumber() + " arrived at customs terminal");
				Platform.runLater(()->{
					MainSceneController.listOfCTButtons.get(0).setText(v.name);
					MainSceneController.listOfCTButtons.get(0).setOnAction(event -> {
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
				Border.pV.PoliceSem4V.release();
				processVehicle(v); 
				System.out.println(v.name + " with " +NoP + "->" + v.getPassengersNumber() + " has been processed on customs terminal");
				Platform.runLater(()->{
					MainSceneController.listOfCTButtons.get(0).setText("CT1");
				});
				Platform.runLater(()->SecondSceneController.updateLegalList(v));
				Border.cV.CustomsSem4V.release();
				MainSceneController.cdl.countDown();
				try {
					FileHandler fHandler = new FileHandler("error.log");
		            SimpleFormatter sFormatter = new SimpleFormatter();
		            fHandler.setFormatter(sFormatter);
		            ExceptionHandler.logger.addHandler(fHandler);
		            fHandler.close();
					MainSceneController.cdl.await();
					MainSceneController.timeline.stop();
				}
				catch(Exception e) {
					ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
				}
		}catch(Exception ie){
			ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
		}
	}
	public void processVehicle(Vehicle v){
		int NoP = v.getPassengersNumber();
		if(v instanceof Bus){
			try{
				FileHandler fileHandler = new FileHandler("error.log");
	            SimpleFormatter simpleFormatter = new SimpleFormatter();
	            fileHandler.setFormatter(simpleFormatter);
	            ExceptionHandler.logger.addHandler(fileHandler);
	            fileHandler.close();
	            MainSceneController.checkIfPaused();
	            checkBusLuggage((Bus)v);
	            sleep(NoP * 100);
	            MainSceneController.checkIfPaused();
		}catch(Exception ie){
			ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
		}
		}
		else {
			try{
				FileHandler fileHandler = new FileHandler("error.log");
	            SimpleFormatter simpleFormatter = new SimpleFormatter();
	            fileHandler.setFormatter(simpleFormatter);
	            ExceptionHandler.logger.addHandler(fileHandler);
	            fileHandler.close();
				MainSceneController.checkIfPaused();
				sleep(2000);
				MainSceneController.checkIfPaused();
		}
		catch(Exception ie){
			ExceptionHandler.logger.log(Level.SEVERE,ie.fillInStackTrace().toString(), ie);
		}
		}
	}
}
