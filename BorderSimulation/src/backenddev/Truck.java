package backenddev;

import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import application.MainSceneController;

public class Truck extends Vehicle{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int limit = 3;
	public boolean hasMoreWeight(){
		return declaredLuggageWeight < realLuggageWeight;
	} 
	private double declaredLuggageWeight;
	private double realLuggageWeight;
	private void setRealWeight() {
		if(PossibilityCalculator.calculate(20)) {
			Random random = new Random();
			int x = random.nextInt(30);
			this.realLuggageWeight = this.declaredLuggageWeight + (float)this.declaredLuggageWeight*((float)x/(float)100);
		}
		else this.realLuggageWeight = this.declaredLuggageWeight;
	}
	public Truck(double DLW){
		super();	
		this.name = "Truck";
		declaredLuggageWeight = DLW;
		setRealWeight();
		numOfPassengers = setPassengerNumber(limit);
		for(int i = 0;i<numOfPassengers;i++) {
			passengers.add(new Person());
		}
	}
	@Override
	public void moveVehicle(){
		if(isFirst()){
			MainSceneController.checkIfPaused();
			try {
				FileHandler fileHandler = new FileHandler("error.log");
	            SimpleFormatter simpleFormatter = new SimpleFormatter();
	            fileHandler.setFormatter(simpleFormatter);
	            ExceptionHandler.logger.addHandler(fileHandler);
	            fileHandler.close();
				Border.pT.PoliceSem4T.acquire();
			} catch (Exception e) {
				ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
			}
			synchronized(Border.line) {
				Border.line.removeFirst();
			}
			Border.pT.sendToPoliceTerminal(this);
			}
	}
}
