package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import backenddev.*;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FileHandler fileHandler = new FileHandler("error.log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            ExceptionHandler.logger.addHandler(fileHandler);
            fileHandler.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));	
			FXMLLoader loader2 = new FXMLLoader(getClass().getResource("SecondScene.fxml"));
			Parent root2 = loader2.load();
			Parent root = loader.load();
			SecondSceneController controller2 = loader2.getController();
			MainSceneController controller = loader.getController();
			Border.setLine();
			controller.init();
			primaryStage.setOnCloseRequest(event -> {
	            System.exit(0);
	        });
			TerminalChecker obj1 = new TerminalChecker();
			obj1.start();
			Border.startVehicles();
			Scene scene1 = new Scene(root);
			primaryStage.setScene(scene1);
			primaryStage.show();
			Scene scene2 = new Scene(root2);
			controller.getScene2.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					primaryStage.setScene(scene2);
				}
			});
			controller2.goToFirstScene.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					primaryStage.setScene(scene1);
				}
			});
			MainSceneController.listOfButtons.addListener((ListChangeListener<Button>) change -> {
	            while (change.next()) {
	                if (change.wasRemoved()) {
	                	if(!controller.pane2.getChildren().isEmpty()) {
	                		controller.pane2.getChildren().remove(MainSceneController.listOfButtons.size()-5);
	                	}
	            		else 
	            			if(controller.pane1.getChildren().size() <= 5)
	            				controller.pane1.getChildren().remove(MainSceneController.listOfButtons.size());
	                }
	            }
	        });
			SecondSceneController.listOfVehiclesWithIncidents.addListener((ListChangeListener<Vehicle>) ch -> {
	            while (ch.next()) {
	                if (ch.wasAdded()) {
	        			final int currentIndex = SecondSceneController.listOfVehiclesWithIncidents.size();
	                	String s = SecondSceneController.listOfVehiclesWithIncidents.get(currentIndex-1).name;
	                	Button b = new Button(s);
	                	b.setOnAction(event -> {
	        	            Alert alert = new Alert(AlertType.INFORMATION);
	        	            alert.setTitle("Information Dialog");
	        	            alert.setHeaderText(null);
	        	            String vehicleInfo = SecondSceneController.listOfVehiclesWithIncidents.get(currentIndex-1).name + "\n";
	        	            vehicleInfo+=SecondSceneController.listOfVehiclesWithIncidents.get(currentIndex-1).numOfPassengers + "\n";
	        	            for (Person p : SecondSceneController.listOfVehiclesWithIncidents.get(currentIndex-1).passengers) vehicleInfo += (p.GetId() + "\n");
	        	            if(b.getText() == "Truck") {
	        	            	try{
	        	            		List<String> fileLines = Files.readAllLines(Paths.get(Terminals.pathToTxtFile));
	        	            		long truckId = SecondSceneController.listOfVehiclesWithIncidents.get(currentIndex-1).getId();
	        	            		CharSequence stringId = String.valueOf(truckId);
		        	            	for(String line : fileLines) {
		        	            		if(line.substring(9,11).contains(stringId)) {
		        	            			vehicleInfo+=line + ".\n";
		        	            			break;
		        	            		}		        	        
		        	            	}	
	        	            	}catch(Exception e) {
	        	            		ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
	        	            	}	        	                  	         
	        	            }
	        	            else {
	        	            	vehicleInfo+="Driver has invalid documents.\n";
	        	            }
	        	            
	         	            alert.setContentText(vehicleInfo);
	        	            alert.showAndWait();
	        	        });
	                	controller2.illegalPane.getChildren().add(b);
	                	controller2.illegalPane.getChildren().get(controller2.illegalPane.getChildren().size() - 1).setTranslateY(controller2.pane3Y+=40);
	                	controller2.illegalPane.getChildren().get(controller2.illegalPane.getChildren().size() - 1).setTranslateX(controller2.pane3X);
	                	if(controller2.pane3Y > 350) {
	                		controller2.pane3Y = 0;
	                		controller2.pane3X += 70;
	                	}
	                }
	            }
	        });
			SecondSceneController.listOfVehiclesWOIncidents.addListener((ListChangeListener<Vehicle>) ch -> {
	            while (ch.next()) {
	                if (ch.wasAdded()) {
	        			final int currentIndex = SecondSceneController.listOfVehiclesWOIncidents.size();
	                	String s = SecondSceneController.listOfVehiclesWOIncidents.get(currentIndex-1).name;
	                	Button b = new Button(s);
	                	b.setOnAction(event -> {
	        	            Alert alert = new Alert(AlertType.INFORMATION);
	        	            alert.setTitle("Information Dialog");
	        	            alert.setHeaderText(null);
	        	            String vehicleInfo = SecondSceneController.listOfVehiclesWOIncidents.get(currentIndex-1).name + "\n";
	        	            vehicleInfo+="Vehicle id: " + SecondSceneController.listOfVehiclesWOIncidents.get(currentIndex-1).getId()+ "\n";
	        	            vehicleInfo+=SecondSceneController.listOfVehiclesWOIncidents.get(currentIndex-1).numOfPassengers + "\n";
	        	            for (Person p : SecondSceneController.listOfVehiclesWOIncidents.get(currentIndex-1).passengers) vehicleInfo += (p.GetId() + "\n");
	        	            String vehicleName = SecondSceneController.listOfVehiclesWOIncidents.get(currentIndex-1).name;
	        	            try{
	        	            	List<String> fileLines = Files.readAllLines(Paths.get(Terminals.pathToTxtFile));
	        	            	for(String line : fileLines) {
	        	            		if(line.substring(0,3).contains(vehicleName) && 
	        	            				line.substring(7,9).contains(String.valueOf(SecondSceneController.
	        	            						listOfVehiclesWOIncidents.get(currentIndex-1).getId()))) {
	        	            			vehicleInfo+=line + " has been thrown out of the bus. " + "\n";
	        	            		}
	        	            	}
	        	            }catch(Exception e) {
	        	            	ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);	        	            	
	        	            }
	         	            alert.setContentText(vehicleInfo);
	        	            alert.showAndWait();
	        	        });
	                	controller2.legalPane.getChildren().add(b);
	                	controller2.legalPane.getChildren().get(controller2.legalPane.getChildren().size() - 1).setTranslateY(controller2.pane3YL+=40);
	                	controller2.legalPane.getChildren().get(controller2.legalPane.getChildren().size() - 1).setTranslateX(controller2.pane3XL);
	                	if(controller2.pane3YL > 350) {
	                		controller2.pane3YL = 0;
	                		controller2.pane3XL += 70;
	                	}
	                }
	            }
	        });
		} catch(Exception e) {
			 ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
		}
	}
	
	public static void main(String[] args) {
		MainSceneController.startTime = System.currentTimeMillis();
		launch(args);
	}
}