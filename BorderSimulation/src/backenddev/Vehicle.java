package backenddev;
import java.io.Serializable;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import application.*;

public abstract class Vehicle extends Thread implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public int numOfPassengers;
	public String name;
	public String typeOfBreach;
	public ArrayList<Person> passengers = new ArrayList<>();
	public void setPassengersNumber(int setToThis) {
		this.numOfPassengers = setToThis;
	}
	public void moveVehicle(){
		if(isFirst()){
			MainSceneController.checkIfPaused();
			try {
				FileHandler fileHandler = new FileHandler("error.log");
	            SimpleFormatter simpleFormatter = new SimpleFormatter();
	            fileHandler.setFormatter(simpleFormatter);
	            ExceptionHandler.logger.addHandler(fileHandler);
	            fileHandler.close();
				Border.pV.PoliceSem4V.acquire();
			} catch (Exception e) {
				ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
			}
			synchronized(Border.line) {
				Border.line.removeFirst();
				}
			Border.pV.sendToPoliceTerminal(this);
		}
	}
	public ArrayList<Person> getPassengers(){
		return passengers;
	}
	public void setPassengers(ArrayList<Person> al) {
		this.passengers = al;
	}
	protected boolean isInLine(){
		synchronized(Border.line){
			return Border.line.contains(this);
		}
	}
	public int setPassengerNumber(int limit){
		Random random = new Random();
		int genNumber = random.nextInt(limit)+1;
		return genNumber;
	}
	protected boolean isFirst(){
		synchronized(Border.line){
			return this == Border.line.peek();
		}
	}
	public int getPassengersNumber(){
		return numOfPassengers;
	}
	public Vehicle(){
		super();
	}
	public void run(){
		Border.decrementCdl();
		Border.waitForIt();
		do {
			moveVehicle();
		}
		while(isInLine());
	}
}
